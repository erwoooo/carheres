package com.example.maptest.mycartest.UI.SetUi;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.maptest.mycartest.Bean.CommandResponse;
import com.example.maptest.mycartest.New.NewHttpUtils;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.UI.SetUi.service.K100B;
import com.example.maptest.mycartest.UI.SetUi.service.PostCommand;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.BaseActivity;
import com.example.maptest.mycartest.Utils.http.ResponseCallback;
import com.google.gson.Gson;

/**
 * Created by ${Author} on 2018/7/14.
 * Use to
 */

public class SetandCanActivity extends BaseActivity {
    private RadioGroup radiogroup_sc;
    private String commId;
    private Button button_send_sc;
    private CommandResponse commandResponse;
    private RadioButton radioButtonset,radioButtoncan;
    private boolean check = true;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            PostCommand command = new PostCommand(AppCons.locationListBean.getTerminalID(),commId,AppCons.loginDataBean.getData().getUsername(),commandResponse.getContent());
            NewHttpUtils.postCommand(new String(new Gson().toJson(command)),SetandCanActivity.this,null);
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setandcansel);
        radiogroup_sc = (RadioGroup) findViewById(R.id.radiogroup_sc);
        radioButtonset = (RadioButton) findViewById(R.id.radio_set);
        radioButtoncan = (RadioButton) findViewById(R.id.radio_cancel);
        radiogroup_sc.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) findViewById(checkedId);
                String type = radioButton.getText().toString();
                if (type.equals("设防")){
                    commId = "DFS_ON#";
                    check = true;
                }else {
                    commId = "DFS_OFF#";
                    check = false;
                }
            }
        });
        if (AppCons.ORDERBEN != null){
            if (AppCons.ORDERBEN.getDefense()){
                radioButtonset.setChecked(true);
                check = true;
            }else {
                check = false;
                radioButtoncan.setChecked(true);
            }
        }
        button_send_sc = (Button) findViewById(R.id.button_send_sc);
        button_send_sc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commId != null){
                    JSONObject object = new JSONObject();
                    object.put("terminalID", AppCons.locationListBean.getTerminalID());              //设备的IEMI号
                    object.put("deviceProtocol", AppCons.locationListBean.getLocation().getDeviceProtocol());      //设备协议号
                    object.put("content", commId);                 //指令
                    NewHttpUtils.sendOrder(object.toJSONString(), SetandCanActivity.this, new ResponseCallback() {
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
                                handler.sendEmptyMessage(0);
                            }else {
                                Toast.makeText(getApplicationContext(), "设置失败", Toast.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void FailCallBack(Object object) {
                            Toast.makeText(getApplicationContext(), "设置失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    /**
     * 低电报警设置发送到服务器
     */
    private void postOrder() {
        if (AppCons.ORDERBEN == null){
            AppCons.ORDERBEN = new K100B();
            AppCons.ORDERBEN.setTerminalID(AppCons.locationListBean.getTerminalID());
        }
        AppCons.ORDERBEN.setDefense(check);
        AppCons.ORDERBEN.setDeviceProtocol(AppCons.locationListBean.getLocation().getDeviceProtocol());

        Gson gson = new Gson();
        String obj = gson.toJson(AppCons.ORDERBEN);
        NewHttpUtils.saveCommand(obj, getApplicationContext(), new ResponseCallback() {
            @Override
            public void TaskCallBack(Object object) {
                Log.e("Object",object.toString());
                if ((object.toString().contains("true") || object.toString().contains("200")) && AppCons.ORDERBEN != null){


                }
            }

            @Override
            public void FailCallBack(Object object) {
                Log.e("Object",object.toString());
            }
        });

    }
}
