package com.example.maptest.mycartest.UI.SetUi;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maptest.mycartest.Bean.CommandResponse;
import com.example.maptest.mycartest.Entity.EightOrderBean;
import com.example.maptest.mycartest.New.NewHttpUtils;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.UI.SetUi.service.PostCommand;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.BaseActivity;
import com.example.maptest.mycartest.Utils.http.ResponseCallback;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ${Author} on 2019/5/24.
 * Use to
 */

public class EightOrderActivity extends BaseActivity {
    private RelativeLayout relative_order_shk;
    private Switch switch_shk;
    private TextView text_shk;
    private boolean check;
    private boolean first = false,send = true;
    private String  commId;
    private CommandResponse commandResponse;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 11){
                if (commandResponse != null){
                    PostCommand command = new PostCommand(AppCons.locationListBean.getTerminalID(),commId,AppCons.loginDataBean.getData().getUsername(),commandResponse.getContent());
                    NewHttpUtils.postCommand(new String(new Gson().toJson(command)),EightOrderActivity.this,null);
                }else {
                    PostCommand command = new PostCommand(AppCons.locationListBean.getTerminalID(),commId,AppCons.loginDataBean.getData().getUsername());
                    NewHttpUtils.postCommand(new String(new Gson().toJson(command)),EightOrderActivity.this,null);
                }
                commandResponse = null;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eorder
        );

        initView();
    }

    private void initView() {
        relative_order_shk = (RelativeLayout) findViewById(R.id.relative_order_shk);
        switch_shk = (Switch) findViewById(R.id.switch_shk);
        text_shk = (TextView) findViewById(R.id.text_shk);
        switch_shk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                check = b;
                if (b) {
                    check = true;
                    first = false;
                    try {
                        sendDate();         //关闭开关，发送指令
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    check = false;
                    if (first){         //如果是第一次进，不做任何操作

                    }else {
                        try {
                            sendDate();             //打开开关，发送指令
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        first = false;
                    }
                }
            }
        });

        if (AppCons.ETORDER != null){
            if (AppCons.ETORDER.isVibration()) {            //根据查询的内容，显示开关状态
                first = true;
                switch_shk.setChecked(true);
                text_shk.setText("震动打开");
            } else {
                switch_shk.setChecked(false);
                text_shk.setText("震动关闭");
            }
        }
    }


    private void sendDate() throws IOException, InterruptedException {
        if (send){
            if (check) {
                commId = "AT+SHAKE=1;";        //开油电Switch，连接油电
            } else {
                commId = "AT+SHAKE=0;";        //关油电Switch，断开油电
            }
            Map<Object,Object> objectorder = new HashMap<>();
            objectorder.put("terminalID", AppCons.locationListBean.getTerminalID());              //设备的IEMI号
            objectorder.put("deviceProtocol", AppCons.locationListBean.getLocation().getDeviceProtocol());      //设备协议号
            objectorder.put("content", commId);
            NewHttpUtils.sendOrder(new Gson().toJson(objectorder), EightOrderActivity.this, new ResponseCallback() {
                @Override
                public void TaskCallBack(Object object) {
                    commandResponse = (CommandResponse) object;
                    String response = commandResponse.getContent();
                    if (response != null) {
                        if (response.contains("OK") || response.contains("ok") || response.contains("Success") || response.contains("successfully") || response.contains("Already")) {
                            Toast.makeText(getApplicationContext(), "设置成功", Toast.LENGTH_SHORT).show();
                            postOrder();            //回复成功，保存指令
                            send = true;
                            if (check){
                                text_shk.setText("震动打开");
                            }else {
                                text_shk.setText("震动关闭");
                            }
                        } else {
                            send = false;
                            switch_shk.setChecked(check);
                            if (response.contains("timeout")) {
                                Toast.makeText(getApplicationContext(), "请求超时", Toast.LENGTH_SHORT).show();
                            }
//                            if (response.contains("RELAY,ERROR:103")) {
//                                Toast.makeText(getApplicationContext(),
//                                        "指令参数错误", Toast.LENGTH_SHORT).show();
//                            }
//                            if (response.contains("RELAY,ERROR:104")) {
//                                Toast.makeText(getApplicationContext(),
//                                        "指令错误", Toast.LENGTH_SHORT).show();
//                            }
//                            if (response.contains("has been cut")) {
//                                Toast.makeText(getApplicationContext(),
//                                        "断油电失败！ 已经处于断油电状态！", Toast.LENGTH_SHORT).show();
//                            }
//                            if (response.contains("GPS has Not FIXED")) {
//                                Toast.makeText(getApplicationContext(),
//                                        "断油电失败！，ACC 开启时，GPS没有定位或者速度大于20KM/H, 断油操作延后!", Toast.LENGTH_SHORT).show();
//                            }
//                            if (response.contains("Resume")) {
//                                Toast.makeText(getApplicationContext(),
//                                        "恢复油电失败！已经处于油电恢复状态！", Toast.LENGTH_SHORT).show();
//                            }
//                            if (response.contains("control oil Failed")) {
//                                Toast.makeText(getApplicationContext(),
//                                        "控制油电失败！", Toast.LENGTH_SHORT).show();
//                            }
//                            if (response.contains("supply cut off")) {
//                                Toast.makeText(getApplicationContext(),
//                                        "已处于油电切断状态，命令未运行！", Toast.LENGTH_SHORT).show();
//                            }
//                            if (response.contains("supply to resume")) {
//                                Toast.makeText(getApplicationContext(),
//                                        "已经在油电供应状态下恢复，命令没有运行！", Toast.LENGTH_SHORT).show();
//                            }  if (response.contains("fail")) {
//                                Toast.makeText(getApplicationContext(),
//                                        "控制油电失败！", Toast.LENGTH_SHORT).show();
//                            }

                        }
                    }else{
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
        }else {
            send = true;
        }

    }


    /**
     * 保存指令
     */
    private void postOrder() {

        if (AppCons.ETORDER == null){
            AppCons.ETORDER = new EightOrderBean();
            AppCons.ETORDER.setTerminalID(AppCons.locationListBean.getTerminalID());
            Log.e("新增",AppCons.ETORDER.toString());
        }
        AppCons.ETORDER.setVibration(check);
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
