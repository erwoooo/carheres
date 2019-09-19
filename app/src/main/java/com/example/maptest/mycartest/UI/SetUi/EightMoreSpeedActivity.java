package com.example.maptest.mycartest.UI.SetUi;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
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

import static com.example.maptest.mycartest.Utils.AppCons.locationListBean;

/**
 * Created by ${Author} on 2018/8/20.
 * Use to
 */

public class EightMoreSpeedActivity extends BaseActivity implements View.OnClickListener{
    private RelativeLayout relative_eight_speed,relative_eight_rest,relative_eight_dis;
    private EditText eight_edit_settime;
    private Button button_send_eight;
    private ImageView image_quit_speed;
    private TextView text_eight_title;
    private int moreSpeed = 0,moreDis = 0;
    private String commId;
    private JSONObject objectorder;
    private CommandResponse commandResponse;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 11){
                if (commandResponse != null){
                    Log.e("commandResponse",commandResponse.toString());
                    PostCommand command = new PostCommand(locationListBean.getTerminalID(),commId,AppCons.loginDataBean.getData().getUsername(),commandResponse.getContent());
                    NewHttpUtils.postCommand(new String(new Gson().toJson(command)),EightMoreSpeedActivity.this,null);
                }else {
                    Log.e("commandResponse","no commandResponse");
                    PostCommand command = new PostCommand(locationListBean.getTerminalID(),commId,AppCons.loginDataBean.getData().getUsername());
                    NewHttpUtils.postCommand(new String(new Gson().toJson(command)),EightMoreSpeedActivity.this,null);
                }
                commandResponse = null;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eight_speed);
        initView();
        iniClick();
    }

    private void iniClick() {
        button_send_eight.setOnClickListener(this);
        image_quit_speed.setOnClickListener(this);
    }

    private void initView() {
        text_eight_title = (TextView) findViewById(R.id.text_eight_title);
        image_quit_speed = (ImageView) findViewById(R.id.image_quit_speed);
        button_send_eight = (Button) findViewById(R.id.button_send_eight);
        eight_edit_settime = (EditText) findViewById(R.id.eight_edit_settime);
        relative_eight_speed = (RelativeLayout) findViewById(R.id.relative_eight_speed);
        relative_eight_rest = (RelativeLayout) findViewById(R.id.relative_eight_rest);
        relative_eight_dis = (RelativeLayout) findViewById(R.id.relative_eight_dis);
        switch (AppCons.ETSELECT){
            case 0:
                text_eight_title.setText("超速报警");
                relative_eight_speed.setVisibility(View.VISIBLE);
                relative_eight_rest.setVisibility(View.GONE);
                relative_eight_dis.setVisibility(View.GONE);
                if (AppCons.ETORDER != null){
                    eight_edit_settime.setText(AppCons.ETORDER.getLimitSpeed() +"");
                }
                break;
            case 1:
                text_eight_title.setText("里程设置");
                relative_eight_speed.setVisibility(View.GONE);
                relative_eight_rest.setVisibility(View.GONE);
                relative_eight_dis.setVisibility(View.VISIBLE);
                if (AppCons.ETORDER != null){
                    eight_edit_settime.setText(AppCons.ETORDER.getMileage() + "");
                }
                break;
            case 2:
                text_eight_title.setText("重启指令");
                relative_eight_speed.setVisibility(View.GONE);
                relative_eight_rest.setVisibility(View.VISIBLE);
                relative_eight_dis.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_send_eight:
                    switch (AppCons.ETSELECT){
                        case 0:
                            moreSpeed = Integer.parseInt(eight_edit_settime.getText().toString());
                            if (moreSpeed > 300){
                                Toast.makeText(getApplicationContext(),"超出范围",Toast.LENGTH_SHORT).show();
                            }else {
                                try {
                                    sendDate();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            break;
                        case 1:

                            try {
                                sendDate();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 2:
                            try {
                                sendDate();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                break;
            case R.id.image_quit_speed:
                finish();
                break;
        }
    }

    private void sendDate() throws IOException, InterruptedException {
        if(locationListBean.getDevice().getDeviceType().equals("Q6H")){
            switch (AppCons.ETSELECT){
                case 0:
                    commId = "overspeed," + eight_edit_settime.getText() + "#";     //超速报警
                    break;
                case 1:
                    commId = "mileage,"+ eight_edit_settime.getText() + "#";    //里程设置
                    break;
                case 2:
                    commId = "AT+RESET;";
                    break;
            }
        }else{
            switch (AppCons.ETSELECT){
                case 0:
                    commId = "overspeed," + eight_edit_settime.getText() + "#";     //超速报警
                    break;
                case 1:
                    commId = "mileage,"+ eight_edit_settime.getText() + "#";    //里程设置
                    break;
                case 2:
                    commId = "RESET#";
                    break;
            }
        }
        objectorder= new JSONObject();
        objectorder.put("terminalID", locationListBean.getTerminalID());              //设备的IEMI号
        objectorder.put("deviceProtocol", locationListBean.getLocation().getDeviceProtocol());      //设备协议号
        objectorder.put("content", commId);
        Log.e("objectorder",objectorder.toJSONString());
        NewHttpUtils.sendOrder(objectorder.toJSONString(), EightMoreSpeedActivity.this, new ResponseCallback() {
            @Override
            public void TaskCallBack(Object object) {
                commandResponse = (CommandResponse) object;
                String data = commandResponse.getContent();
                if (data != null){
                    if (data.contains("OK") || data.contains("ok")|| data.contains("success") || data.contains("Success") || data.contains("successfully") || data.contains("Already")) {
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

                Message message = new Message();
                message.what = 11;
                handler.sendMessage(message);
            }

            @Override
            public void FailCallBack(Object object) {
                Toast.makeText(getApplicationContext(), "设置失败", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void postOrder() {
        if (AppCons.ETORDER == null){
            AppCons.ETORDER = new EightOrderBean();
            AppCons.ETORDER.setTerminalID(locationListBean.getTerminalID());
        }
        switch (AppCons.ETSELECT){
            case 0:
                AppCons.ETORDER.setLimitTime(moreSpeed);
                break;
            case 1:
                AppCons.ETORDER.setMileage(moreDis);
                break;
        }

        AppCons.ETORDER.setDeviceProtocol(locationListBean.getLocation().getDeviceProtocol());
        Gson gson = new Gson();
        String obj = gson.toJson(AppCons.ETORDER);
        NewHttpUtils.saveCommand(obj, getApplicationContext(), new ResponseCallback() {
            @Override
            public void TaskCallBack(Object object) {
                Log.e("中心号码",object.toString());
            }

            @Override
            public void FailCallBack(Object object) {
                Log.e("中心号码失败",object.toString());
            }
        });
    }
}
