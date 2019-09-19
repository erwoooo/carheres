package com.example.maptest.mycartest.Utils.bluetoothmanager;

public interface BlueTooth
{
	//检测蓝牙状态
	boolean CheckBlueTooth();
	//打开蓝牙
	void TurnOnBluetooth();
	//开始广播消息
	void RegisterBroadCastReceive();
	//注销广播事件
	void UnRegisterBroadCastReceive();
	//搜索设备
	void StartSearchDevice();
	//停止搜索
	void StopSearchDevice();
	//蓝牙设备连接
	void BlueToothConnect(String Address);
	//蓝牙查找
	void DiscoverBlueToothDevice(String Address, int intervalTime);
	//蓝牙查找取消
	void CancelDiscoverBlueToothDevice(String Address);
	//断开连接
	void BlueToothDisconnect(String Address);
}
