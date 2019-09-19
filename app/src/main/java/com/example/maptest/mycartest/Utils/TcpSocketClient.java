package com.example.maptest.mycartest.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.maptest.mycartest.New.WeiboDialogUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by ${Author} on 2017/4/11.
 * Use to TCP发送指令
 */

public class TcpSocketClient {
    private static TcpSocketClient tcpSocketClient;
    private Socket mClient;
    private ConnectLinstener mListener;
    private Thread mConnectThread;
    private OutputStream outputStream;
    private ObjectOutputStream oos;
    private InputStream inputStream;
    private Dialog dialog;
    public interface ConnectLinstener {
        void onReceiveData(String data);
    }

    public void setOnConnectLinstener(ConnectLinstener linstener) {
        this.mListener = linstener;
    }

    public static TcpSocketClient getInstance() {
        if (tcpSocketClient == null) {
            tcpSocketClient = new TcpSocketClient();
        }
        Log.d("teeee", "创建对象");
        return tcpSocketClient;
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 100:
                    if (dialog != null)
                        WeiboDialogUtils.closeDialog(dialog);
                    if (mListener != null) {
//                        mListener.onReceiveData(msg.getData().getString("data"));
                        mListener.onReceiveData((String) msg.obj);

                    }
                    break;
                case 99:
                    if (dialog != null)
                        WeiboDialogUtils.closeDialog(dialog);
                    if (mListener != null) {
//                        mListener.onReceiveData(msg.getData().getString("data"));
                        mListener.onReceiveData((String) msg.obj);
                        Log.d("teeee", "接受完成2" + "结果:" + msg.obj);
                    }
                    break;
                case 0:
                    if (dialog != null)
                        WeiboDialogUtils.closeDialog(dialog);
                    Log.d("teeee", "接受完成3" + "结果:" + msg.obj);
                    Toast.makeText((Context) msg.obj, "连接超时，请重新发送", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    if (dialog != null)
                        WeiboDialogUtils.closeDialog(dialog);
                    break;
            }
        }
    };

    /*创建连接*/
    public void createConnect(final Object data, final Context context) {
        if (mConnectThread == null) {
            mConnectThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.d("teeee", "开始连接服务器");
//                        mClient = new Socket("order.carhere.net", 8006);
                        mClient = new Socket("192.168.0.134", 8010);
                        mClient.setSoTimeout(40000);
                        try {
                            outputStream = mClient.getOutputStream();
                            oos = new ObjectOutputStream(outputStream);
                            inputStream = mClient.getInputStream();
                            Log.d("teeee", "服务器连接完成");
                            Log.d("teeee", "开始发送");
                            oos.writeObject(data);
//                            outputStream.write(data.getBytes());
                            Log.d("teeee", "发送完成");

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    if (inputStream != null) {
                                        byte[] buffer = new byte[1024];
                                        int len = -1;
                                        Log.d("teeee", "开始接受1");
                                        try {
                                            Thread.sleep(200);
                                            if (mClient != null) {
                                                String datas = null;
                                                Log.d("teeee", "开始接受2");
                                                while ((len = inputStream.read(buffer)) != -1) {
                                                    Log.d("teeee", "开始接受3");
                                                    datas = new String(buffer, 0, len);
                                                    Log.d("teeee", datas);
                                                }
                                                Message message = new Message();
                                                message.what = 100;
//                                                Bundle bundle = new Bundle();
//                                                bundle.putString("data", datas);
//                                                message.setData(bundle);
                                                message.obj = datas;
                                                mHandler.sendMessage(message);
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                            Log.d("error2", e.toString());
                                            Message message = new Message();
                                            message.what = 0;
                                            message.obj = context;
                                            mHandler.sendMessage(message);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Log.d("error3", e.toString());
                                        } finally {
                                            try {
                                                Thread.sleep(100);
                                                disconnect();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                                Log.d("error4", e.toString());
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                                Log.d("error5", e.toString());
                                            }
                                        }
                                    }
                                }
                            }).start();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Message message = new Message();
                            message.what = 1;
                            mHandler.sendMessage(message);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Message message = new Message();
                        message.what = 1;
                        mHandler.sendMessage(message);
                        Log.d("error1", e.toString());
                    }


                }
            });
            mConnectThread.start();


        }
    }

    /*发送数据*/
    public void socketSend(final Object data, final Context context) {
        if (context != null) {
            showDialog(context);
        }
        try {
            createConnect(data, context);
            Thread.sleep(100);
            Log.d("teeee", "开始接受");
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.d("error7", e.toString());
        }
    }

    /*断开连接*/
    public void disconnect() throws IOException {
        if (mClient != null) {
            Log.d("teeee", "开始断开输入输出");
            outputStream.flush();
            mClient.shutdownInput();
            mClient.shutdownOutput();
            if (inputStream != null) {
                inputStream.close();
                Log.d("teeee", "开始断开输入");
            }
            if (outputStream != null) {
                outputStream.close();
                Log.d("teeee", "开始断开输出");
            }
            if (oos != null){
                oos.close();
            }
            mClient.close();
            mClient = null;
            mConnectThread = null;
            Log.d("teeee", "从服务器断开");
        }
    }

    public void showDialog(Context context) {
        dialog = WeiboDialogUtils.createLoadingDialog(context,"发送中");
    }
}