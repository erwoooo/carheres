package com.example.maptest.mycartest.UI.SetUi;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.maptest.mycartest.Adapter.SpinnerAdapter;
import com.example.maptest.mycartest.Bean.CommandResponse;
import com.example.maptest.mycartest.Bean.SpineBean;
import com.example.maptest.mycartest.New.NewHttpUtils;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.UI.SetUi.service.K100B;
import com.example.maptest.mycartest.UI.SetUi.service.PostCommand;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.BaseActivity;
import com.example.maptest.mycartest.Utils.http.ResponseCallback;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${Author} on 2018/7/14.
 * Use to
 */

public class SensorActivity extends BaseActivity {
    private Spinner spinner_sensortype;
    private List<SpineBean>list;
    private SpinnerAdapter adapter;
    private int type = 0;
    private Button button_sensor;
    private String commId;
    CommandResponse commandResponse;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            PostCommand command = new PostCommand(AppCons.locationListBean.getTerminalID(),commId,AppCons.loginDataBean.getData().getUsername(),commandResponse.getContent());
            NewHttpUtils.postCommand(new String(new Gson().toJson(command)),SensorActivity.this,null);
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        spinner_sensortype = (Spinner) findViewById(R.id.spinner_sensortype);
        button_sensor = (Button) findViewById(R.id.button_sensor);
        list = new ArrayList<>();
        for (int i = 0 ; i < 5 ; i ++){
            SpineBean bean = new SpineBean(i+1 + "");
            list.add(bean);
        }
        adapter = new SpinnerAdapter(list,this);
        spinner_sensortype.setAdapter(adapter);
        spinner_sensortype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (AppCons.ORDERBEN != null){
            spinner_sensortype.setSelection(AppCons.ORDERBEN.getSensitiveLevel());
            type = AppCons.ORDERBEN.getSensitiveLevel();
        }

        button_sensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (type){
                    case 0:
                        commId = "SENLEVEL,1#";
                        break;
                    case 1:
                        commId = "SENLEVEL,2#";
                        break;
                    case 2:
                        commId = "SENLEVEL,3#";
                        break;
                    case 3:
                        commId = "SENLEVEL,4#";
                        break;
                    case 4:
                        commId = "SENLEVEL,5#";
                        break;
                }
                JSONObject object = new JSONObject();
                object.put("terminalID", AppCons.locationListBean.getTerminalID());              //设备的IEMI号
                object.put("deviceProtocol", AppCons.locationListBean.getLocation().getDeviceProtocol());      //设备协议号
                object.put("content", commId);                 //指令

                NewHttpUtils.sendOrder(object.toJSONString(), SensorActivity.this, new ResponseCallback() {
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
        });
    }


    private void postOrder() {
        if (AppCons.ORDERBEN == null){
            AppCons.ORDERBEN = new K100B();
            AppCons.ORDERBEN.setTerminalID(AppCons.locationListBean.getTerminalID());
        }
        AppCons.ORDERBEN.setSensitiveLevel(type);
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
