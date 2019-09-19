package com.example.maptest.mycartest.UI.EquipUi;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.maptest.mycartest.Bean.BlueOrderBean;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.Utils.BaseActivity;
import com.example.maptest.mycartest.Utils.bluetoothmanager.BlueToothManager;

import static android.R.id.list;
import static com.example.maptest.mycartest.Utils.bluetoothmanager.BlueToothManager.handler;

/**
 * Created by ${Author} on 2017/10/9.
 * Use to
 */

public class BlueOrderActivity extends BaseActivity {
    private Switch aSwitch_connect,aSwitch_find;
    private String address;
    private boolean check = false;
    public BlueToothManager bluetooth = null;
    private BlueOrderBean blueOrderBean;
    private ImageView imageView_quit;
    private int openStatues = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        blueOrderBean = (BlueOrderBean) getIntent().getSerializableExtra("bean");
        address = blueOrderBean.getAddress();
        Log.e("address",address);
        initView();
        handler = new Handler()
        {
            public void handleMessage(Message msg)
            {
                super.handleMessage(msg);
                switch(msg.what)
                {
                    case 2:
                        Toast.makeText(getApplicationContext(),"连接成功",Toast.LENGTH_SHORT).show();
                        check = true;
                        openStatues = 1;
                        initNotify();
                        if (!aSwitch_connect.isChecked()){
                            aSwitch_connect.setChecked(true);
                        }
                        break;
                    case 3:
                        Toast.makeText(getApplicationContext(),"断开连接", Toast.LENGTH_LONG).show();
                        openStatues = 2;
                        initNotify();
                        if (aSwitch_connect.isChecked()){
                            aSwitch_connect.setChecked(false);
                        }

                        break;
                    case 6:
                        Toast.makeText(getApplicationContext(),"取消搜索", Toast.LENGTH_LONG).show();
                        break;
                    case 7:
                        Toast.makeText(getApplicationContext(),"连接失败,请重新连接", Toast.LENGTH_LONG).show();
                        aSwitch_connect.setChecked(false);
                        break;
                }
            }
        };
    }

    private void initView() {
        imageView_quit = (ImageView) findViewById(R.id.image_quitconnect);
        imageView_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check){
                    bluetooth.CancelDiscoverBlueToothDevice(address);
                    bluetooth.BlueToothDisconnect(address);
                }
                finish();
            }
        });
        aSwitch_connect = (Switch) findViewById(R.id.switch_connect);
        aSwitch_find = (Switch) findViewById(R.id.switch_connect_search);
        bluetooth = new BlueToothManager(BlueOrderActivity.this);
        bluetooth.RegisterBroadCastReceive();
        bluetooth.TurnOnBluetooth();
        if (blueOrderBean.isStatus()){
            aSwitch_connect.setChecked(true);
            if(bluetooth != null)
            {
                Log.e("openon",address);
                bluetooth.BlueToothConnect(address);

            }
            check = true;
        }
        aSwitch_connect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        if(bluetooth != null)
                        {
                            Log.e("bluetoothaddr1",address);
                            bluetooth.BlueToothConnect(address);

                        }

                    }else {
                        if (check){
                            if(bluetooth != null)
                            {
                                Log.e("bluetoothaddr2",address);
                                bluetooth.BlueToothDisconnect(address);
                            }
                            check = false;
                            aSwitch_find.setChecked(false);
                        }
                    }
            }
        });

        aSwitch_find.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (check){
                    if (isChecked){
                        if(bluetooth != null){
                            Log.e("bluetoothfind",address);
                            bluetooth.DiscoverBlueToothDevice(address,10000);
                        }
                    }else {
                        if(bluetooth != null)
                        {
                            bluetooth.CancelDiscoverBlueToothDevice(address);
                        }

                    }
                }else {
                    Toast.makeText(getApplicationContext(),"请先连接设备",Toast.LENGTH_SHORT).show();
                    aSwitch_find.setChecked(false);
                }

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
//        aSwitch_connect.setChecked(false);
//        aSwitch_find.setChecked(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bluetooth.UnRegisterBroadCastReceive();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (check){
                bluetooth.CancelDiscoverBlueToothDevice(address);
                bluetooth.BlueToothDisconnect(address);
            }
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    private void initNotify(){
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(this);
//        builder.setContentInfo("补充内容");
        if (openStatues == 2){
            builder.setContentText("断开连接");
        }else {
            builder.setContentText("建立连接");
        }

        builder.setContentTitle("智能设备");
//        builder.setSmallIcon(R.mipmap.logo_96);
        builder.setTicker("新消息");
        builder.setAutoCancel(true);

        builder.setWhen(System.currentTimeMillis());//设置时间，设置为系统当前的时间
        Notification notification = builder.build();
        /**
         * vibrate属性是一个长整型的数组，用于设置手机静止和振动的时长，以毫秒为单位。
         * 参数中下标为0的值表示手机静止的时长，下标为1的值表示手机振动的时长， 下标为2的值又表示手机静止的时长，以此类推。
         */
        long[] vibrates = { 0, 0, 1000, 1000 };
        notification.vibrate = vibrates;

        /**
         * 手机处于锁屏状态时， LED灯就会不停地闪烁， 提醒用户去查看手机,下面是绿色的灯光一 闪一闪的效果
         */
        notification.ledARGB = Color.GREEN;// 控制 LED 灯的颜色，一般有红绿蓝三种颜色可选
        notification.ledOnMS = 1000;// 指定 LED 灯亮起的时长，以毫秒为单位
        notification.ledOffMS = 1000;// 指定 LED 灯暗去的时长，也是以毫秒为单位
        notification.flags = Notification.FLAG_SHOW_LIGHTS;// 指定通知的一些行为，其中就包括显示


        //手机设置提示音
//        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//        notification.sound = Uri.parse("file:///android_res/raw/tire.wav");
        if (openStatues == 2){
            notification.sound = Uri.parse("android.resource://" + getPackageName()
                    + "/" + R.raw.disconnect);
        }else {
            notification.sound = Uri.parse("android.resource://" + getPackageName()
                    + "/" + R.raw.connect);
        }

        manager.notify(2, notification);
    }
}
