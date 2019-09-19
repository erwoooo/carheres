package com.example.maptest.mycartest.UI.SetUi;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.maptest.mycartest.Bean.CommandResponse;
import com.example.maptest.mycartest.Entity.EightOrderBean;
import com.example.maptest.mycartest.New.NewHttpUtils;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.UI.SetUi.service.PostCommand;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.BaseActivity;
import com.example.maptest.mycartest.Utils.OnclickListeners;
import com.example.maptest.mycartest.Utils.http.ResponseCallback;
import com.google.gson.Gson;

import java.io.IOException;

/**
 * Created by ${Author} on 2019/5/24.
 * Use to
 */

public class EightCenterActivity extends BaseActivity implements View.OnClickListener{
    private LinearLayout linner_center;
    private Switch switch_ecenter;
    private EditText edit_first,edit_second,edit_third;
    private Button button_ecenter;
    private boolean check = true;
    private String commId;
    private JSONObject objectorder;

    CommandResponse commandResponse;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 11:
                    if (commandResponse != null){
                        PostCommand command = new PostCommand(AppCons.locationListBean.getTerminalID(),commId,AppCons.loginDataBean.getData().getUsername(),commandResponse.getContent());
                        NewHttpUtils.postCommand(new String(new Gson().toJson(command)),EightCenterActivity.this,null);
                    }else {
                        PostCommand command = new PostCommand(AppCons.locationListBean.getTerminalID(),commId,AppCons.loginDataBean.getData().getUsername());
                        NewHttpUtils.postCommand(new String(new Gson().toJson(command)),EightCenterActivity.this,null);
                    }
                    commandResponse = null;
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecenter);
        initView();
    }

    private void initView() {
        linner_center = (LinearLayout) findViewById(R.id.linner_center);
        linner_center.setVisibility(View.VISIBLE);
        edit_first = (EditText) findViewById(R.id.edit_first);
        edit_second = (EditText) findViewById(R.id.edit_second);
        edit_third = (EditText) findViewById(R.id.edit_third);
        button_ecenter = (Button) findViewById(R.id.button_ecenter);
        switch_ecenter = (Switch) findViewById(R.id.switch_ecenter);
        if (AppCons.ETORDER != null){
            edit_first.setText(AppCons.ETORDER.getSos1());
            edit_second.setText(AppCons.ETORDER.getSos2());
            edit_third.setText(AppCons.ETORDER.getSos3());
        }
    }

    @Override
    public void onClick(View view) {
        edit_first.setOnClickListener(this);
        edit_second.setOnClickListener(this);
        edit_third.setOnClickListener(this);
        button_ecenter.setOnClickListener(new OnclickListeners() {
            @Override
            protected void onNoDoubleClick(View v) {
                try {
                    sendDate();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        switch_ecenter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    check = true;
                    linner_center.setVisibility(View.VISIBLE);
                } else {
                    check = false;
                    linner_center.setVisibility(View.GONE);
                    try {
                        sendDate();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void sendDate() throws IOException, InterruptedException {
        if (check) {
            Log.e("check","open");
            /*打开SOS号码*/
            commId = "AT+AUTHNUMBER=" + edit_first.getText().toString() + "," + edit_second.getText().toString() + "," + edit_third.getText().toString()+";";
        } else {
            /*关闭SOS号码*/
            Log.e("check","close");
            commId = "AT+AUTHNUMBER=,,;";
        }
        objectorder = new JSONObject();
        objectorder.put("terminalID", AppCons.locationListBean.getTerminalID());              //设备的IEMI号
        objectorder.put("deviceProtocol", AppCons.locationListBean.getLocation().getDeviceProtocol());      //设备协议号
        objectorder.put("content", commId);             //指令

        NewHttpUtils.sendOrder(objectorder.toJSONString(), EightCenterActivity.this, new ResponseCallback() {
            @Override
            public void TaskCallBack(Object object) {
                commandResponse = (CommandResponse) object;
                String data = commandResponse.getContent();
                if (data != null){
                    if (data.contains("OK") || data.contains("ok") || data.contains("Success") || data.contains("successfully") || data.contains("Already")) {
                        Toast.makeText(getApplicationContext(), "设置成功", Toast.LENGTH_SHORT).show();
                        postOrder();        //指令收到回应成功后，再保存指令到服务器
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


    /**
     * 保存中心号码
     */
    private void postOrder() {

        if (AppCons.ETORDER == null){
            AppCons.ETORDER = new EightOrderBean();
            AppCons.ETORDER.setTerminalID(AppCons.locationListBean.getTerminalID());
            Log.e("新增",AppCons.ETORDER.toString());
        }
        if (check){
            AppCons.ETORDER.setSos1(edit_first.getText().toString());
            AppCons.ETORDER.setSos2(edit_second.getText().toString());
            AppCons.ETORDER.setSos3(edit_third.getText().toString());
        }else {
            AppCons.ETORDER.setSos1("");
            AppCons.ETORDER.setSos2("");
            AppCons.ETORDER.setSos3("");
        }

        AppCons.ETORDER.setDeviceProtocol(AppCons.locationListBean.getLocation().getDeviceProtocol());
        Gson gson = new Gson();
        String obj = gson.toJson(AppCons.ETORDER);
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
