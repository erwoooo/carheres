package com.example.maptest.mycartest.Utils.bluetoothmanager;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

@SuppressLint("NewApi")
public class BlueToothManager extends BluetoothGattCallback  implements BlueTooth
{
	//UUID服务
	private static String ServiceUUID ="00001802-0000-1000-8000-00805f9b34fb";
	private static String BlueToothUUID = "00002a06-0000-1000-8000-00805f9b34fb";
	//消息
	public static Handler handler = null;
	private IntentFilter intentFilter = null;
	//初始化广播
	private BlueToothBroadcast BlueState = null;
	//初始化类
	private  Context context = null;
	//蓝牙适配器
	private  BluetoothAdapter bluetoothAdapter = null;
	//GATT函数
	public static HashMap<String,BluetoothGatt> ConnectObject = new HashMap<String,BluetoothGatt>();
	//设备查找
	public static HashMap<String,Timer> BlueToothTimer = new HashMap<String,Timer>();
	//Timer函数
	public static HashMap<String,Timer> BlueToothConnect = new HashMap<String,Timer>();
	//构造函数
	public BlueToothManager(Context context)
	{
		this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		this.context = context;
	}

	//检测蓝牙
	@Override
	public boolean CheckBlueTooth()
	{
		if(bluetoothAdapter != null && context != null)
		{
			return  bluetoothAdapter.isEnabled();
		}
		return false;
	}
	//打开蓝牙
	@Override
	public void TurnOnBluetooth()
	{
		boolean enableState = CheckBlueTooth() ?
				true : false;
		if(!enableState)
		{
			if(bluetoothAdapter != null && context != null)
			{
				bluetoothAdapter.enable();
			}
		}
	}

	//开启蓝牙广播消息
	public void RegisterBroadCastReceive()
	{
		if(intentFilter == null && BlueState == null && context != null)
		{
			intentFilter = new IntentFilter();
			BlueState = new BlueToothBroadcast();
			intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
			intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
			intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
			intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
			intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
			context.registerReceiver(BlueState ,intentFilter);
		}
	}
	//注销广播事件
	public void UnRegisterBroadCastReceive()
	{
		if(BlueState != null && intentFilter != null && context != null)
		{
			context.unregisterReceiver(BlueState);
			bluetoothAdapter = null;
			BlueState = null;
			intentFilter = null;
			context = null;
			intentFilter = null;
		}
	}

	private  BluetoothAdapter.LeScanCallback mLeScanCallback =  new BluetoothAdapter.LeScanCallback()
	{
		@Override
		public void onLeScan(BluetoothDevice Device, int rssi, byte[] scanRecord)
		{
			if(Device != null && !TextUtils.isEmpty(Device.getName()))
			{

				if("BT-01".equalsIgnoreCase(Device.getName()))
				{
					Message msg = Message.obtain();
					msg.what = BlueToothState.STATE_FONDED;
					Bundle bundle = new Bundle();
					bundle.putString("Name",Device.getName());
					bundle.putString("Address",Device.getAddress());
					msg.setData(bundle);
					handler.sendMessage(msg);
				}
			}
		}
	};

	//搜索蓝牙设备
	@Override
	public void StartSearchDevice()
	{
		//4.3的版本支持
		if(bluetoothAdapter != null)
		{
			if(bluetoothAdapter.isEnabled() && context != null && handler != null)
			{
				bluetoothAdapter.startLeScan(mLeScanCallback);
				Log.e("bluetooth","search");
				Message msg = Message.obtain();
				Log.e("bluetooth",msg.toString());
				msg.what = BlueToothState.STATE_FOND;
				Log.e("msg",msg.what + "");
				handler.sendMessage(msg);
			}
		}
	}
	//停止搜索蓝牙
	@Override
	public void StopSearchDevice()
	{
		if(bluetoothAdapter != null && (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2))
		{
			if(bluetoothAdapter.isEnabled() && context != null && mLeScanCallback != null)
			{
				Log.e("bluetooth","stop");
				bluetoothAdapter.stopLeScan(mLeScanCallback);
				Message msg = Message.obtain();
				msg.what = BlueToothState.STATE_NOFOND;
				handler.sendMessage(msg);
			}
		}
	}

	//蓝牙设备连接
	@Override
	public void BlueToothConnect(String Address)
	{
		if(bluetoothAdapter != null && context != null)
		{
			if(bluetoothAdapter.isEnabled())
			{
				Log.e("bluetoothaddr2",Address);
				BluetoothDevice BlueToothDevice = bluetoothAdapter.getRemoteDevice(Address);
				BluetoothGatt bluetoothGatt = BlueToothDevice.connectGatt(context, false,mGattCallback);
				if(bluetoothGatt != null)
				{
					if(ConnectObject.get(Address) != null)
					{
						ConnectObject.get(Address).connect();
						Log.e("bluetooth","connect1");
					}
					else
					{
						ConnectObject.put(Address,bluetoothGatt);
						ConnectObject.get(Address).connect();
						Log.e("bluetooth","connect2");
					}
				}
				Message msg = Message.obtain();
				msg.what = BlueToothState.STATE_CONNECTING;
				handler.sendMessage(msg);
				CloseConnect(Address);
			}
		}
	}

	private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback()
	{
		@Override
		public void onConnectionStateChange(BluetoothGatt mBluetoothGatt, int status,
											int newState) {
			if (newState == BluetoothProfile.STATE_CONNECTED)
			{
				if(mBluetoothGatt != null)
				{
					mBluetoothGatt.discoverServices();
				}
			}
		}
	};
	//搜索失败规则
	private void CloseConnect(final String Address)
	{
		Log.e("default","address");
		if(bluetoothAdapter != null && context != null)
		{
			if(bluetoothAdapter.isEnabled())
			{
				if(BlueToothConnect.get(Address)!=null)
				{
					BlueToothConnect.get(Address).cancel();
					BlueToothConnect.remove(Address);
				}
				Timer timer = new Timer();
				BlueToothConnect.put(Address,timer);
				TimerTask  task = new TimerTask (){
					@SuppressLint("NewApi")
					@Override
					public void run()
					{
						Message msg = new Message();
						msg.what = BlueToothState.STATE_CONNECTFAIL;
						Bundle bundle = new Bundle();
						bundle.putString("Address",Address);
						msg.setData(bundle);
						handler.sendMessage(msg);
						if(ConnectObject.get(Address) != null)
						{
							ConnectObject.get(Address).disconnect();
							ConnectObject.get(Address).close();
							ConnectObject.remove(Address);
						}
					}
				};
				BlueToothConnect.get(Address).schedule(task,10000);
			}
		}
	}

	//查找设备，设备报警
	@Override
	public void DiscoverBlueToothDevice(final String Address,final int intervalTime)
	{
		if(this.bluetoothAdapter != null && this.context != null)
		{
			if(this.bluetoothAdapter.isEnabled())
			{
				if(ConnectObject.get(Address) != null)
				{
					BluetoothGattService service = ConnectObject.get(Address)
							.getService(UUID.fromString(ServiceUUID));
					if(service != null)
					{
						final BluetoothGattCharacteristic characteristic = service
								.getCharacteristic(UUID.fromString(BlueToothUUID));
						if(characteristic != null)
						{
							if(BlueToothTimer.get(Address)!=null)
							{
								BlueToothTimer.get(Address).cancel();
							}
							Timer timer = new Timer();
							BlueToothTimer.put(Address, timer);
							TimerTask  task = new TimerTask (){
								@Override
								public void run()
								{
									if(ConnectObject.get(Address) != null && characteristic!= null)
									{
										characteristic.setValue(new byte[] {(byte)0x02});
										characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
										ConnectObject.get(Address).writeCharacteristic(characteristic);
									}
								}
							};
							BlueToothTimer.get(Address).schedule(task,0,intervalTime);
						}
					}
				}
			}
		}
	}

	//取消查找设备，取消报警
	@Override
	public void CancelDiscoverBlueToothDevice(String Address)
	{
		if(bluetoothAdapter != null && context != null)
		{
			if(bluetoothAdapter.isEnabled())
			{
				if(BlueToothTimer.get(Address)!=null)
				{
					BlueToothTimer.get(Address).cancel();
					BlueToothTimer.remove(Address);
				}
				if(ConnectObject.get(Address) != null && context != null)
				{
					BluetoothGattService service = ConnectObject.get(Address)
							.getService(UUID.fromString(ServiceUUID));
					if(service != null)
					{
						BluetoothGattCharacteristic characteristic = service
								.getCharacteristic(UUID.fromString(BlueToothUUID));
						if(characteristic != null)
						{
							characteristic.setValue(new byte[] {(byte)0x00});
							characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
							ConnectObject.get(Address).writeCharacteristic(characteristic);
						}
					}
				}
			}
		}
	}

	//断开连接
	@Override
	public void BlueToothDisconnect(String Address)
	{
		Log.e("myblue",Address);
		if(bluetoothAdapter != null && context != null)
		{
			if(bluetoothAdapter.isEnabled())
			{
				if(BlueToothConnect.get(Address) != null)
				{
					BlueToothConnect.get(Address).cancel();
				}
				if(ConnectObject.get(Address) != null)
				{
					ConnectObject.get(Address).disconnect();
					ConnectObject.get(Address).close();

					ConnectObject.remove(Address);
				}
			}
		}
	}
}
