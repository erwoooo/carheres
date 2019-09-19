package com.example.maptest.mycartest.Utils;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.maptest.mycartest.UI.TyreUi.TyreActivity;
import com.example.maptest.mycartest.UI.warn.WarnMationActivity;
import com.tencent.android.tpush.XGNotifaction;
import com.tencent.android.tpush.XGPushActivity;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.XGPushNotifactionCallback;

/**
 * Created by ${Author} on 2017/4/20.
 * Use to 基础Activity
 */

public class BaseActivity extends AppCompatActivity {

    /**
     * @return
     * app字体不随手机的设置改变
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config=new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config,res.getDisplayMetrics() );
        return res;
    }
    public BroadcastReceiver rssiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int s = obtainWifiInfo();
            if (s > (-50)){
                //强度良好
            }else if (s < -66){
//                Toast.makeText(getApplicationContext(),"当前WIFI信号强度极差！",Toast.LENGTH_SHORT).show();
            }else if (s == -200){
//                Toast.makeText(getApplicationContext(),"当前WIFI信号已断开！",Toast.LENGTH_SHORT).show();
            }
            Log.e("当前wifi的强度","等级: " + s);
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WifiManager wifi_service = (WifiManager)getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifi_service.getConnectionInfo();
        //设置全屏
/*        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/


    }
    private int obtainWifiInfo() {
        // Wifi的连接速度及信号强度：
        int strength = 0;
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        // WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        WifiInfo info = wifiManager.getConnectionInfo();
        if (info.getBSSID() != null) {
            // 链接信号强度，5为获取的信号强度值在5以内
            strength = WifiManager.calculateSignalLevel(info.getRssi(), 5);
            // 链接速度
            int speed = info.getLinkSpeed();
            // 链接速度单位
            String units = WifiInfo.LINK_SPEED_UNITS;
            // Wifi源名称
            String ssid = info.getSSID();
        }
//        return info.toString();
        return info.getRssi();
    }
    @Override
    protected void onResume() {
        super.onResume();
        XGPushManager.onActivityStarted(this);
        registerReceiver(rssiReceiver,new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        XGPushManager.onActivityStoped(this);
        unregisterReceiver(rssiReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
