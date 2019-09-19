package com.example.maptest.mycartest.UI.EquipUi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.UI.CarContralActivity;
import com.example.maptest.mycartest.UI.SearchActivity;
import com.example.maptest.mycartest.UI.warn.WarnMationActivity;
import com.example.maptest.mycartest.UI.warn.WarnTypeActivity;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.BaseActivity;
import com.example.maptest.mycartest.Utils.NotificationsUtils;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import static com.tencent.android.tpush.XGPushManager.registerPush;

/**
 * Created by ${Author} on 2017/3/19.
 * Use to 报警设置
 */

public class SetWarnActivity extends BaseActivity implements View.OnClickListener{
    private Switch switch_recm,switch_voice,switch_shock,switch_allday;
    private LinearLayout linearLayout_auto;
    private RelativeLayout relativeLayout_auto,relativeLayout_types;
    private ImageView imageView_quit;
    private boolean check;
    private int agentId;
    private String terminalID;
    CarContralActivity activity = CarContralActivity.instance;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setwarn);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        try{
            if (AppCons.WARNTYPE == 1){
                terminalID = AppCons.locationListBean.getTerminalID();
            }else {
                terminalID = AppCons.loginDataBean.getData().getTerminalID();
            }
            agentId = AppCons.loginDataBean.getData().getId();
            Log.e("warn","agentId :" + agentId +" terminalID :" + terminalID);
        }catch (Exception e){
            e.printStackTrace();
        }

        getState();
        initView();
        initClick();
        initSwitch();

    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e("setWarn","onResume" + "是否打开" + check);
        if (NotificationsUtils.isNotificationEnabled(getApplicationContext())){
            if (check == true){
                saveState();
                activity.initXgPush(terminalID,agentId);
            }
        }else {
            Toast.makeText(SetWarnActivity.this,"未开启当前通知栏权限，请打开",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("setWarn","onPause");
    }

    /**
     * 接受报警推送通知开关
     */
    private void initSwitch() {
        /*是否接收新消息*/
        switch_recm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( NotificationsUtils.isNotificationEnabled(getApplicationContext())){  //如果获取了通知栏权限
                    if (isChecked){
                        check = true;
                        saveState();
                        activity.initXgPush(terminalID,agentId);

                      Toast.makeText(SetWarnActivity.this,"设置成功，下次启动生效",Toast.LENGTH_SHORT).show();
                    }else {
                        check = false;
                        activity.unLogin();
                        saveState();
                       Toast.makeText(SetWarnActivity.this,"设置成功，下次启动生效",Toast.LENGTH_SHORT).show();
                    }
                }else {                                                                         //如果没有获取到通知栏
                    check = true;
                    Toast.makeText(SetWarnActivity.this,"请先设置允许通知",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.fromParts("package", getPackageName(), null));
                    startActivity(intent);
                }
            }
        });
        /*是否开启声音*/
        switch_voice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Toast.makeText(SetWarnActivity.this,"开启声音提醒",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(SetWarnActivity.this,"关闭声音提醒",Toast.LENGTH_SHORT).show();
                }
            }
        });
        /*是否开启手机震动*/
        switch_shock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Toast.makeText(SetWarnActivity.this,"开启手机震动",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(SetWarnActivity.this,"关闭手机震动",Toast.LENGTH_SHORT).show();
                }
            }
        });
        /*全天提醒*/
        switch_allday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Toast.makeText(SetWarnActivity.this,"开启全天提醒",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(SetWarnActivity.this,"关闭全天提醒",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initClick() {
        relativeLayout_types.setOnClickListener(this);
        relativeLayout_auto.setOnClickListener(this);
        imageView_quit.setOnClickListener(this);

    }

    private void initView() {
        imageView_quit = (ImageView) findViewById(R.id.image_swnquits);
        switch_recm = (Switch) findViewById(R.id.switch_recv);
        switch_voice = (Switch) findViewById(R.id.switch_voice);
        switch_shock = (Switch) findViewById(R.id.switch_shok);
        switch_allday = (Switch) findViewById(R.id.switch_allday);
        linearLayout_auto = (LinearLayout) findViewById(R.id.linerlayout_autoset);
        relativeLayout_auto = (RelativeLayout) findViewById(R.id.relative_autovoice);
        relativeLayout_types = (RelativeLayout) findViewById(R.id.relative_types);

        if (check){
            switch_recm.setChecked(true);
        }else {
            switch_recm.setChecked(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.relative_autovoice:
                startActivity(new Intent(SetWarnActivity.this, WarnMationActivity.class));
                break;
            case R.id.relative_types:
                startActivity(new Intent(SetWarnActivity.this,WarnTypeActivity.class));
                break;
            case R.id.image_swnquits:
                finish();
                break;
        }
    }

    /**
     * 保存当前开关的状态
     */
    private void saveState(){
        SharedPreferences preferences = getSharedPreferences(agentId+terminalID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("check", check);
        editor.commit();
    }
    private void getState(){
        SharedPreferences preferences = getSharedPreferences(agentId+terminalID, Context.MODE_PRIVATE);
        check = preferences.getBoolean("check",false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activity = null;
        terminalID = null;
    }

    protected void requestPermission(int requestCode) {
        // TODO Auto-generated method stub
        // 6.0以上系统才可以判断权限
        // 进入设置系统应用权限界面
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        startActivity(intent);
    }
}
