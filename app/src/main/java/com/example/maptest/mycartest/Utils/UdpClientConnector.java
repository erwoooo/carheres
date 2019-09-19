package com.example.maptest.mycartest.Utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by ${Author} on 2017/11/23.
 * Use to 发送UDP数据报
 */

public class UdpClientConnector {

    private static UdpClientConnector mUdpClientConnector;
    private ConnectLinstener mListener;
    private Thread mSendThread;

    private byte receiveData[] = new byte[1024];
    private String mSendHexString;

    private boolean isSend = false;

    public interface ConnectLinstener {
        void onReceiveData(String data);
    }

    public void setOnConnectLinstener(ConnectLinstener linstener) {
        this.mListener = linstener;
    }

    public static UdpClientConnector getInstance() {
        if (mUdpClientConnector == null) {
            mUdpClientConnector = new UdpClientConnector();
        }
        return mUdpClientConnector;
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1000:
                    if (mListener != null) {
                        mListener.onReceiveData(msg.getData().getString("data"));
                    }
                    break;
            }
        }
    };

    /**
     * 创建udp发送连接（服务端ip地址、端口号、超时时间）
     *
     * @param ip
     * @param port
     * @param timeOut
     */
    public void createConnector(final String ip, final int port, final int timeOut) {
        if (mSendThread == null) {
            mSendThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        if (!isSend)
                            continue;
                        DatagramSocket socket = null;
                        try {
                            socket = new DatagramSocket();
                            socket.setSoTimeout(timeOut);
                            InetAddress serverAddress = InetAddress.getByName(ip);
                            byte data[] = mSendHexString.getBytes("utf-8");
                            DatagramPacket sendPacket = new DatagramPacket(data, data.length, serverAddress, port);
                            socket.send(sendPacket);
                            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                            socket.receive(receivePacket);
                            Message msg = new Message();
                            msg.what = 1000;
                            Bundle bundle = new Bundle();
                            bundle.putString("data", new String(receivePacket.getData()));
                            msg.setData(bundle);
                            mHandler.sendMessage(msg);
                            socket.close();
                        } catch (SocketException e) {
                            Log.e("error1",e.toString());
                            Message msg = new Message();
                            msg.what = 1000;
                            Bundle bundle = new Bundle();
                            bundle.putString("data", e.toString());
                            msg.setData(bundle);
                            mHandler.sendMessage(msg);
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            Message msg = new Message();
                            msg.what = 1000;
                            Bundle bundle = new Bundle();
                            bundle.putString("data", e.toString());
                            msg.setData(bundle);
                            mHandler.sendMessage(msg);
                            Log.e("error2",e.toString());
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                            Message msg = new Message();
                            msg.what = 1000;
                            Bundle bundle = new Bundle();
                            bundle.putString("data", e.toString());
                            msg.setData(bundle);
                            mHandler.sendMessage(msg);
                            Log.e("error3",e.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e("error4",e.toString());
                            Message msg = new Message();
                            msg.what = 1000;
                            Bundle bundle = new Bundle();
                            bundle.putString("data", e.toString());
                            msg.setData(bundle);
                            mHandler.sendMessage(msg);
                        }
                        isSend = false;
                    }
                }
            });
            mSendThread.start();
        }
    }

    /**
     * 发送数据
     *
     * @param str
     */
    public void sendStr(final String str) {
        mSendHexString = str;
        isSend = true;
    }


}
