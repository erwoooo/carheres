package com.example.maptest.mycartest.SwipRecycleView;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.lbsapi.BMapManager;
import com.baidu.lbsapi.MKGeneralListener;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.UI.TyreUi.TyreActivity;
import com.example.maptest.mycartest.UI.warn.WarnMationActivity;
import com.example.maptest.mycartest.Utils.AppCons;
import com.tencent.android.tpush.XGNotifaction;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.XGPushNotifactionCallback;

import java.util.List;

import static com.tencent.android.tpush.XGPushManager.setNotifactionCallback;


/**
 * Created by ${Author} on 2017/3/9.
 * Use to
 */

public class App extends Application {
    private static App instance;
    private static Context context;
    private static App mInstance = null;
    public BMapManager mBMapManager = null;
    public int mNotificationNum = 1;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        if (instance == null) {
            instance = this;
        }
        mInstance = this;
        initEngineManager(this);
//        LeakCanary.install(this);



        // 在主进程设置信鸽相关的内容
//        if (isMainProcess()) {
//            // 为保证弹出通知前一定调用本方法，需要在application的onCreate注册
//            // 收到通知时，会调用本回调函数。
//            // 相当于这个回调会拦截在信鸽的弹出通知之前被截取
//            // 一般上针对需要获取通知内容、标题，设置通知点击的跳转逻辑等等

//
//            XGPushManager
//                    .setNotifactionCallback(new XGPushNotifactionCallback() {
//
//                        @Override
//                        public void handleNotify(XGNotifaction xGNotifaction) {
//                            Log.e("test", "处理信鸽通知：" + xGNotifaction);
//                            // 获取标签、内容、自定义内容
//                            xGNotifaction.doNotify();
//                            String contents = xGNotifaction.getContent();
//                            Log.e("contents",contents);
//                            if (xGNotifaction != null){
//
//                                String title = xGNotifaction.getTitle();
//                                String content = xGNotifaction.getContent();
////
//                                String customContent = xGNotifaction
//                                        .getCustomContent();
//                                JSONObject jsonObject  = JSON.parseObject(customContent);
//                                AppCons.WARN_TEMID = (String) jsonObject.get("id");
//
//                                boolean tire = (boolean) jsonObject.get("tire");
//
//
//                                NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//                                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());
//                                if (tire){
//                                    Log.e("tire","tire" + tire + "1");
//                                    Bundle bundle = new Bundle();
//                                    bundle.putString(AppCons.WARN_TEMID,AppCons.WARN_TEMID);
//                                    Intent intent = new Intent(context,TyreActivity.class).putExtras(bundle);
//                                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//                                    mBuilder.setContentTitle(title)
//                                            .setContentText(content)
//                                            .setPriority(Notification.PRIORITY_MAX)
//                                            .setOngoing(false)
//                                            .setSmallIcon(R.mipmap.logo_96)
//                                            .setAutoCancel(true)
//                                            .setContentIntent(pendingIntent)
//                                            .setSound(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.tire));
//
//                                    Log.e("tire","tire" + tire + "2");
//                                xGNotifaction.doNotify();
//                                }else {
//                                    Bundle bundle = new Bundle();
//                                    bundle.putString("TimID",AppCons.WARN_TEMID);
//                                    Intent intent = new Intent(context,WarnMationActivity.class).putExtras(bundle);
//                                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//                                    mBuilder.setContentTitle(title)
//                                            .setContentText(content)
//                                            .setPriority(Notification.PRIORITY_MAX)
//                                            .setOngoing(false)
//                                            .setSmallIcon(R.mipmap.logo_96)
//                                            .setAutoCancel(true)
//                                            .setContentIntent(pendingIntent)
//                                            .setSound(Uri.parse("android.resource://"+getPackageName()+"/"+ R.raw.normal));
//                                xGNotifaction.doNotify();
//                                }
//                                Log.e("tire","tire" + tire + ":" + mNotificationNum);
//                                mNotificationManager.notify(mNotificationNum, mBuilder.build());
//                                mNotificationNum ++;
//                                Log.e("tire","tire" + tire + ":" + mNotificationNum);
//                            }
//                        }
//                    });
//        }




    }

    public void initEngineManager(Context context) {
        if (mBMapManager == null) {
            mBMapManager = new BMapManager(context);
        }

        if (!mBMapManager.init(new MyGeneralListener())) {
            Toast.makeText(App.getInstance().getApplicationContext(), "BMapManager  初始化错误!",
                    Toast.LENGTH_LONG).show();
        }
        Log.d("ljx", "initEngineManager");
    }

    public static App getInstance() {
        return instance;
    }

    //返回
    public static Context getContextObject(){
        return context;
    }


    public boolean isMainProcess() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }



    // 常用事件监听，用来处理通常的网络错误，授权验证错误等
   public static class MyGeneralListener implements MKGeneralListener {

        @Override
        public void onGetPermissionState(int iError) {
            // 非零值表示key验证未通过
            if (iError != 0) {
                // 授权Key错误：
                Toast.makeText(App.getInstance().getApplicationContext(),
                        "请在AndoridManifest.xml中输入正确的授权Key,并检查您的网络连接是否正常！error: " + iError, Toast.LENGTH_LONG).show();
            } else {

            }
        }
    }
}