package com.example.maptest.mycartest.SwipRecycleView;

/**
 * Created by ${Author} on 2018/7/21.
 * Use to
 */


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.maptest.mycartest.IMyAidlInterface;
import com.example.maptest.mycartest.New.NewHttpUtils;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.UI.CarContralActivity;
import com.example.maptest.mycartest.UI.MainActivity;
import com.example.maptest.mycartest.UI.TyreUi.TyreActivity;
import com.example.maptest.mycartest.UI.warn.TmidBean;
import com.example.maptest.mycartest.UI.warn.WarnMationActivity;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.http.ResponseCallback;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

import static com.example.maptest.mycartest.Utils.AppCons.HEART_BEAT_JSON;
import static com.example.maptest.mycartest.Utils.AppCons.ID;
import static com.example.maptest.mycartest.Utils.AppCons.SOCKET_CUTOFF;
import static com.example.maptest.mycartest.Utils.AppCons.locationListBean;
import static com.example.maptest.mycartest.Utils.JsonUtils.isGoodJson;


/**
 * Created by li300 on 2016/10/7 0007.
 */

public class SocketService extends Service {
    private static final String TAG = "BackService";
    //心跳包频率
    private static final long HEART_BEAT_RATE = 30 * 1000;
    private static final int PUSH_NOTIFICATION_ID = (0x001);
    private static final String PUSH_CHANNEL_ID = "CAR";
    private static final String PUSH_CHANNEL_NAME = "PUSH_NAME";

    public static final String HOST = "push.carhere.net";// //
    public static final int PORT = 8004;

    public static final String MESSAGE_ACTION="com.example.maptest.mycartest.socket";
    public static final String HEART_BEAT_ACTION="com.example.maptest.mycartest.heart";

    public static final String HEART_BEAT_STRING="00";//心跳包内容

    private ReadThread mReadThread;

    private LocalBroadcastManager mLocalBroadcastManager;

    private WeakReference<Socket> mSocket;

    private String messSend;
    private boolean isSocketConnect = true;
    // For heart Beat

    private int alarmState = 0;
    String title;
    private int mNotificationNum = 0;
    private  Context context;


    private Handler mHandler = new Handler();

    private Runnable heartBeatRunnable = new Runnable() {

        @Override
        public void run() {
            if (System.currentTimeMillis() - sendTime >= HEART_BEAT_RATE) {
                Log.e("sendTime",sendTime + "");
                boolean isSuccess = sendMsg(HEART_BEAT_STRING)&&isSocketConnect;//就发送一个HEART_BEAT_STRING过去 如果发送失败，就重新初始化一个socket
                Log.e("isSuccess","isSuccess: " + isSuccess);
                if (!isSuccess) {
                    mHandler.removeCallbacks(heartBeatRunnable);

                    mReadThread.release();

                    releaseLastSocket(mSocket);

                    if (SOCKET_CUTOFF){

                    }else {
                        new InitSocketThread().start();

                    }
                }
            }
            mHandler.postDelayed(this, HEART_BEAT_RATE);
        }
    };

    private long sendTime = 0L;
    private IMyAidlInterface.Stub iBackService = new IMyAidlInterface.Stub() {

        @Override
        public boolean sendMessage(String message) throws RemoteException {
            messSend = message;
            return sendMsg(message)&&isSocketConnect;
        }
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return iBackService;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        context = App.getContextObject();
//        Intent serviceIntent = new Intent(context,SocketService.class);
//        context.startForegroundService(serviceIntent);

        new InitSocketThread().start();


    }

    private void recWarn(){
        TmidBean bean = new TmidBean(ID);
        Gson gson = new Gson();
        String obj = gson.toJson(bean);
        NewHttpUtils.querrWarnOnly(obj, context, new ResponseCallback() {
            @Override
            public void TaskCallBack(Object object) {
                String ob = (String) object;
                Log.e("myrec",ob);

            }
            @Override
            public void FailCallBack(Object object) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        releaseLastSocket(mSocket);
        iBackService = null;
        mReadThread = null;
        heartBeatRunnable = null;
        AppCons.HEART_BEAT_JSON = null;
        mHandler = null;
        stopSelf();
    }

    public boolean sendMsg(String msg)  {
        if (AppCons.HEART_BEAT_JSON != null){
            String messageSend = AppCons.HEART_BEAT_JSON;
            Log.e("sendMessage",messageSend);
            if (null == mSocket || null == mSocket.get()) {
                return false;
            }
            Socket soc = mSocket.get();
            try {
                if (!soc.isClosed() && !soc.isOutputShutdown()) {
                    final OutputStream os = soc.getOutputStream();
                    final String message = messageSend;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                os.write(message.getBytes());
                                os.flush();
                                sendTime = System.currentTimeMillis();//每次发送成数据，就改一下最后成功发送的时间，节省心跳间隔时间
                            } catch (IOException e) {
                                isSocketConnect = false;
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } else {
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            isSocketConnect = true;
            return true;
        }else {
            return  false;
        }

    }

    private void initSocket() {//初始化Socket
                Log.e("initSocket","initSocket");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                    Socket so = new Socket(HOST, PORT);
                    mSocket = new WeakReference<Socket>(so);
                    mReadThread = new ReadThread(so);
                    mReadThread.start();
                    mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);//初始化成功后，就准备发送心跳包
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                }
            }).start();


    }

    private void releaseLastSocket(WeakReference<Socket> mSocket) {
        try {
            if (null != mSocket) {
                Socket sk = mSocket.get();
                if (sk!= null && !sk.isClosed()) {
                    sk.close();
                }
                sk = null;
                mSocket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    class InitSocketThread extends Thread {
        @Override
        public void run() {
            super.run();

            initSocket();
        }
    }
    // Thread to read content from Socket
    class ReadThread extends Thread {
        private WeakReference<Socket> mWeakSocket;
        private boolean isStart = true;

        public ReadThread(Socket socket) {
            mWeakSocket = new WeakReference<Socket>(socket);
        }

        public void release() {
            isStart = false;
            releaseLastSocket(mWeakSocket);
        }

        @Override
        public void run() {
            super.run();
            Socket socket = mWeakSocket.get();
            if (null != socket) {
                try {
                    InputStream is = socket.getInputStream();
                    byte[] buffer = new byte[1024 * 4];
                    int length = 0;
                    while (!socket.isClosed() && !socket.isInputShutdown()
                            && isStart && ((length = is.read(buffer)) != -1)) {

                        if (length > 0) {
                            recWarn();
                            String message = new String(Arrays.copyOf(buffer,
                                    length)).trim();
                            Log.e("message",message);
                            Log.e(TAG, message);
                            if (message != null && message.contains("alarmState")){
                                JSONObject jsonObject  = JSON.parseObject(message);
                                String content = jsonObject.getString("content");
                                alarmState = jsonObject.getInteger("alarmState");
                                String alarmType = jsonObject.getString("alarmType");
                                String timID = jsonObject.getString("terminalID");
                                String flagName = jsonObject.getString("flagName");
                                if (flagName == null){
                                   title = timID;
                                }else {
                                    title = flagName;
                                }
                                doNotify(title,content,timID,alarmType);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    recWarn();
                    if (heartBeatRunnable != null){
                        mHandler.removeCallbacks(heartBeatRunnable);
                    }
                    if (mReadThread != null){
                        mReadThread.release();
                    }
                  if (mSocket != null){
                      releaseLastSocket(mSocket);
                  }
                    if (SOCKET_CUTOFF){
                    }else {
                        recWarn();
                        new InitSocketThread().start();
                    }
                }
            }
        }
    }

   private void doNotify(String title,String content,String timID,String alarmType){
       Log.e("mNotificationNum",mNotificationNum + "");
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           Log.e("mNotificationNum",  "z执行");
           Intent notificationIntent = null;
           NotificationManager mNotificationManager = (NotificationManager) App.getContextObject().getSystemService(NOTIFICATION_SERVICE);
           NotificationChannel channel = new NotificationChannel(PUSH_CHANNEL_ID, PUSH_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
           Log.e("mNotificationNum",  "z执行1");
           channel.enableLights(true);
           channel.setLightColor(Color.RED);
           channel.enableVibration(true);
           Bundle bundle = new Bundle();
           bundle.putString("TimID",timID);
           Log.e("mNotificationNum",  "z执行2");
           if (alarmState == 1){
               notificationIntent = new Intent(context, TyreActivity.class).putExtras(bundle);
               channel.setSound(Uri.parse("android.resource://"+App.getInstance().getPackageName()+"/"+ R.raw.tire),Notification.AUDIO_ATTRIBUTES_DEFAULT);
           }else{
               Log.e("mNotificationNum",  "z执行3");
               channel.setSound(Uri.parse("android.resource://"+App.getInstance().getPackageName()+"/"+ R.raw.normal),Notification.AUDIO_ATTRIBUTES_DEFAULT);
                notificationIntent = new Intent(context, WarnMationActivity.class).putExtras(bundle);
           }
           if (mNotificationManager != null) {
               Log.e("mNotificationNum",  "z执行4");
               mNotificationManager.createNotificationChannel(channel);
           }
           NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
           notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
           PendingIntent pendingIntent = PendingIntent.getActivity(context, mNotificationNum, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
           builder.setContentTitle(title + mNotificationNum)
                   .setContentIntent(pendingIntent)
                   .setContentText(alarmType+(content))
                   .setNumber(mNotificationNum ++)
                   .setTicker(alarmType+(content))
                   .setWhen(System.currentTimeMillis())
//                   .setSmallIcon(R.mipmap.logo_96)
                    .setOnlyAlertOnce(true)
                   .setDefaults(Notification.DEFAULT_ALL);
           Log.e("mNotificationNum",  "z执行5");
           Notification notification = builder.build();
           notification.flags |= Notification.FLAG_AUTO_CANCEL;
           if (mNotificationManager != null) {
               Log.e("mNotificationNum",  "z执行6");
               mNotificationManager.notify(PUSH_NOTIFICATION_ID, notification);
           }

       }else {
           NotificationManager mNotificationManager = (NotificationManager) App.getContextObject().getSystemService(NOTIFICATION_SERVICE);
           NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(App.getContextObject());

           if (alarmState == 1){
               Bundle bundle = new Bundle();
               bundle.putString("TimID",timID);
               Intent intent = new Intent(context,TyreActivity.class).putExtras(bundle);
               PendingIntent pendingIntent = PendingIntent.getActivity(context, mNotificationNum, intent, PendingIntent.FLAG_UPDATE_CURRENT);
               mBuilder.setContentTitle(title)
                       .setContentText(alarmType+(content))
                       .setPriority(Notification.PRIORITY_MAX)
                       .setOngoing(false)
//                       .setSmallIcon(R.mipmap.logo_96)
                       .setAutoCancel(true)
                       .setContentIntent(pendingIntent)
                       .setSound(Uri.parse("android.resource://"+App.getInstance().getPackageName()+"/"+ R.raw.tire));
           }else {
               Bundle bundle = new Bundle();
               bundle.putString("TimID",timID);
               Intent intent = new Intent(context,WarnMationActivity.class).putExtras(bundle);
               PendingIntent pendingIntent = PendingIntent.getActivity(context, mNotificationNum, intent, PendingIntent.FLAG_UPDATE_CURRENT);
               mBuilder.setContentTitle(title)
                       .setContentText(alarmType+(content))
                       .setPriority(Notification.PRIORITY_MAX)
                       .setOngoing(false)
//                       .setSmallIcon(R.mipmap.logo_96)
                       .setAutoCancel(true)
                       .setContentIntent(pendingIntent)
                       .setSound(Uri.parse("android.resource://"+App.getInstance().getPackageName()+"/"+ R.raw.normal));
           }
           mNotificationManager.notify(mNotificationNum, mBuilder.build());
           mNotificationNum ++;
       }
   }

}