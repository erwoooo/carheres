package com.example.maptest.mycartest.UI.SetUi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maptest.mycartest.Bean.CommandResponse;
import com.example.maptest.mycartest.New.NewHttpUtils;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.UI.SetUi.service.K100B;
import com.example.maptest.mycartest.UI.SetUi.service.PostCommand;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.BaseActivity;
import com.example.maptest.mycartest.Utils.OnclickListeners;
import com.example.maptest.mycartest.Utils.TimePickActivity;
import com.example.maptest.mycartest.Utils.http.ResponseCallback;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ${Author} on 2019/5/22.
 * Use to
 */

public class TimeOilActivity extends BaseActivity implements View.OnClickListener {
    private TextView text_time_oil;
    private Switch switch_time;
    private LinearLayout linerlayout_time;
    private Button button_time;
    private ImageView image_quittime;
    private static  final int ExpirationTime = 0;
    private String startTime,commId,offTime;
    private CommandResponse commandResponse;
    private boolean check;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 11){
                if (commandResponse != null){
                    PostCommand command = new PostCommand(AppCons.locationListBean.getTerminalID(),commId,AppCons.loginDataBean.getData().getUsername(),commandResponse.getContent());
                    NewHttpUtils.postCommand(new String(new Gson().toJson(command)),TimeOilActivity.this,null);
                }else {
                    PostCommand command = new PostCommand(AppCons.locationListBean.getTerminalID(),commId,AppCons.loginDataBean.getData().getUsername());
                    NewHttpUtils.postCommand(new String(new Gson().toJson(command)),TimeOilActivity.this,null);
                }
                commandResponse = null;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        initView();
    }

    private void initView() {
        button_time = (Button) findViewById(R.id.button_time);
        linerlayout_time = (LinearLayout) findViewById(R.id.linerlayout_time);
        switch_time = (Switch) findViewById(R.id.switch_time);
        text_time_oil = (TextView) findViewById(R.id.text_time_oil);
        image_quittime = (ImageView) findViewById(R.id.image_quittime);
        text_time_oil.setOnClickListener(this);
        image_quittime.setOnClickListener(this);
        button_time.setOnClickListener(new OnclickListeners() {
            @Override
            protected void onNoDoubleClick(View v) {
                sendDate(startTime);
            }
        });
        if ( AppCons.ORDERBEN != null ){
            text_time_oil.setText( AppCons.ORDERBEN.getTimerCut());
            startTime =  AppCons.ORDERBEN.getTimerCut();
            if ( AppCons.ORDERBEN.getPowerSwitch()){
                switch_time.setChecked(true);
                linerlayout_time.setVisibility(View.VISIBLE);
            }

        }
        switch_time.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                check = isChecked;
                if (isChecked){
                    linerlayout_time.setVisibility(View.VISIBLE);
                }else {
                    linerlayout_time.setVisibility(View.GONE);
                    offTime = "0-0-0";
                    sendDate(offTime);
                }
            }
        });
    }


    /**
     * 选择时间
     */
    private void hireExpirationTime() {
        Intent intent = new Intent(this, TimePickActivity.class);
        intent.putExtra("date", text_time_oil.getText());
        startActivityForResult(intent, ExpirationTime);
    }


    /**
     * @param requestCode
     * @param resultCode
     * @param data
     * 获取到返回修改后的信息，并更新UI
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case ExpirationTime:
                    String date = data.getStringExtra("date");
                    startTime = data.getStringExtra("date");
                    text_time_oil.setText(date);
                    break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_time_oil:
                hireExpirationTime();
                break;
            case R.id.image_quittime:
                finish();
                break;
        }
    }

    private void sendDate(final String cutTime)  {
        if (cutTime.equals("") || cutTime == null){
            return;
        }
        String com = cutTime.replaceAll("-",",");
        Log.e("com",com);
        commId = "TIMERCUT,"+com+"#";
        Map<Object,Object> map = new HashMap<>();
        map.put("terminalID", AppCons.locationListBean.getTerminalID());              //设备的IEMI号
        map.put("deviceProtocol", AppCons.locationListBean.getLocation().getDeviceProtocol());      //设备协议号
        map.put("content", commId);
        NewHttpUtils.sendOrder(new Gson().toJson(map), TimeOilActivity.this, new ResponseCallback() {
            @Override
            public void TaskCallBack(Object object) {
                commandResponse = (CommandResponse) object;
                String data = (commandResponse.getContent());
                if (data != null){
                    if (data.contains("OK") || data.contains("ok") || data.contains("Success") || data.contains("successfully") || data.contains("Already")) {
                        Toast.makeText(getApplicationContext(), "设置成功", Toast.LENGTH_SHORT).show();
                        postOrder(cutTime);        //指令收到回应成功后，再保存指令到服务器
                    } else if (data.contains("timeout")) {
                        Toast.makeText(getApplicationContext(), "请求超时", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "设置失败", Toast.LENGTH_SHORT).show();

                    }
                }else {
                    Toast.makeText(getApplicationContext(), "设置失败", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void FailCallBack(Object object) {
                Toast.makeText(getApplicationContext(), "设置超时", Toast.LENGTH_SHORT).show();
            }
        });
        Message message = new Message();
        message.what = 11;
        handler.sendMessage(message);
    }


    private void postOrder(String saveTime) {
        if (AppCons.ORDERBEN == null){
            AppCons.ORDERBEN = new K100B();
            AppCons.ORDERBEN.setTerminalID(AppCons.locationListBean.getTerminalID());
            Log.e("新增",AppCons.ORDERBEN.toString());
        }
        AppCons.ORDERBEN.setPowerSwitch(check);
        Log.e("start",saveTime);
        AppCons.ORDERBEN.setTimerCut(saveTime);
        AppCons.ORDERBEN.setDeviceProtocol(AppCons.locationListBean.getLocation().getDeviceProtocol());


        Gson gson = new Gson();
        String obj = gson.toJson(AppCons.ORDERBEN);
        NewHttpUtils.saveCommand(obj, getApplicationContext(), new ResponseCallback() {
            @Override
            public void TaskCallBack(Object object) {
                Log.e("Object",object.toString());
            }

            @Override
            public void FailCallBack(Object object) {
                Log.e("Object",object.toString());
            }
        });


    }



}
