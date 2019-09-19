package com.example.maptest.mycartest.Utils.bluetoothmanager;
/*
 *����״̬
 */
public interface BlueToothState 
{
    //打开蓝牙
    int STATE_ON = 0,
    //关闭蓝牙
    STATE_OFF = 1,
    //蓝牙连接
    STATE_CONNECT = 2,
    //蓝牙断开
    STATE_DISCONNECT = 3,
    //正在连接
    STATE_CONNECTING = 4,
    //蓝牙搜索
    STATE_FOND = 5,
    //蓝牙停止搜索
    STATE_NOFOND = 6,
    //连接失败
    STATE_CONNECTFAIL = 7,
    //发现设备
    STATE_FONDED = 8;
}
