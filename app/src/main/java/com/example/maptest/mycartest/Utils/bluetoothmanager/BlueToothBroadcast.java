package com.example.maptest.mycartest.Utils.bluetoothmanager;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Message;

public class BlueToothBroadcast extends BroadcastReceiver{
	@SuppressLint("NewApi")
	@Override
	public void onReceive(Context context, Intent intent)
	{
		Message msg = new Message();
		String action = intent.getAction();
		if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(action))
		{
			//蓝牙打开
			int onOrOffState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
			switch(onOrOffState)
			{
				case BluetoothAdapter.STATE_ON:
					msg.what = BlueToothState.STATE_ON;
					BlueToothManager.handler.sendMessage(msg);
					break;
				case BluetoothAdapter.STATE_OFF:
					msg.what = BlueToothState.STATE_OFF;
					BlueToothManager.handler.sendMessage(msg);
					break;
			}
		}
		else if(BluetoothDevice.ACTION_ACL_CONNECTED.equals(action))   //连接蓝牙
		{
			final BluetoothDevice Device = intent
					.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			if(BlueToothManager.BlueToothConnect.get(Device.getAddress()) != null)
			{
				BlueToothManager.BlueToothConnect.get(Device.getAddress()).cancel();
				BlueToothManager.BlueToothConnect.remove(Device.getAddress());
			}
			msg.what = BlueToothState.STATE_CONNECT;
			msg.obj = Device.getAddress();
			BlueToothManager.handler.sendMessage(msg);
		}
		else if(BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action))       //断开蓝牙
		{
			BluetoothDevice Device = intent
					.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			if(BlueToothManager.BlueToothTimer.get(Device.getAddress())!=null)
			{
				BlueToothManager.BlueToothTimer.get(Device.getAddress()).cancel();
				BlueToothManager.BlueToothTimer.remove(Device.getAddress());
			}
			if(BlueToothManager.ConnectObject.get(Device.getAddress()) != null)
			{
				BlueToothManager.ConnectObject.remove(Device.getAddress());
			}
			msg.what = BlueToothState.STATE_DISCONNECT;
			msg.obj = Device.getAddress();
			BlueToothManager.handler.sendMessage(msg);
		}
	}
}
