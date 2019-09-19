package com.example.maptest.mycartest.UI.SetUi;

import android.content.pm.ActivityInfo;
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
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.example.maptest.mycartest.Bean.CommandResponse;
import com.example.maptest.mycartest.New.NewHttpUtils;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.UI.SetUi.service.K100B;
import com.example.maptest.mycartest.UI.SetUi.service.PostCommand;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.BaseActivity;
import com.example.maptest.mycartest.Utils.http.ResponseCallback;
import com.google.gson.Gson;

import java.io.IOException;

/**
 * Created by ${Author} on 2017/3/23.
 * Use to油电控制报警指令发送
 */

public class OilContralActivity extends BaseActivity implements View.OnClickListener {
    private ImageView imageView_quit;
    private Switch aSwitch;
    private TextView text_oil;
    private boolean check ;
    private String commId;
    public RequestQueue queue;
    private JSONObject objectorder;
    private boolean first = false,send = true;
    CommandResponse commandResponse;
    private MyOnCheckedChangeListener myOnCheckedChangeListener;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 11){
                if (commandResponse != null){
                    PostCommand command = new PostCommand(AppCons.locationListBean.getTerminalID(),commId,AppCons.loginDataBean.getData().getUsername(),commandResponse.getContent());
                    NewHttpUtils.postCommand(new String(new Gson().toJson(command)),OilContralActivity.this,null);
                }else {
                    PostCommand command = new PostCommand(AppCons.locationListBean.getTerminalID(),commId,AppCons.loginDataBean.getData().getUsername());
                    NewHttpUtils.postCommand(new String(new Gson().toJson(command)),OilContralActivity.this,null);
                }
                commandResponse = null;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oil);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        initView();
        initClick();
    }

    /**
     * 初始化点击事件
     */
    private void initClick() {
        imageView_quit.setOnClickListener(this);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        Log.e("断油电",AppCons.ORDERBEN.toString());
        imageView_quit = (ImageView) findViewById(R.id.image_quitoil);
        aSwitch = (Switch) findViewById(R.id.switch_oil);
        text_oil = (TextView) findViewById(R.id.text_oil);

        if (AppCons.ORDERBEN != null){
            if (AppCons.ORDERBEN.getCutOff()) {            //根据查询的内容，显示开关状态
//                first = true;
                aSwitch.setChecked(true);
                text_oil.setText("断开油电");
            } else {
                aSwitch.setChecked(false);
                text_oil.setText("恢复油电");
            }
            myOnCheckedChangeListener = new MyOnCheckedChangeListener();
            aSwitch.setOnCheckedChangeListener(myOnCheckedChangeListener);
        }else {
            myOnCheckedChangeListener = new MyOnCheckedChangeListener();
            aSwitch.setOnCheckedChangeListener(myOnCheckedChangeListener);
        }



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_quitoil:        //退出
                quitSet();
//                postOrder();
                break;
        }
    }

    private class MyOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
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
                try {
                    sendDate();             //打开开关，发送指令
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                    if (first){         //如果是第一次进，不做任何操作
//
//                    }else {
//                        try {
//                            sendDate();             //打开开关，发送指令
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        first = false;
//                    }
            }
        }
    }

    /**
     * @throws IOException
     * @throws InterruptedException
     * 发送指令
     */
    private void sendDate() throws IOException, InterruptedException {
       if (send){
           if (check) {
               commId = "RELAY,1#";
           } else {
               commId = "RELAY,0#";
           }
           objectorder = new JSONObject();
           objectorder.put("terminalID", AppCons.locationListBean.getTerminalID());              //设备的IEMI号
           objectorder.put("deviceProtocol", AppCons.locationListBean.getLocation().getDeviceProtocol());      //设备协议号
           objectorder.put("content", commId);
           NewHttpUtils.sendOrder(objectorder.toJSONString(), OilContralActivity.this, new ResponseCallback() {
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
                               text_oil.setText("断开油电");
                           }else {
                               text_oil.setText("恢复油电");
                           }
                       } else {
                           send = false;
                           aSwitch.setChecked(check);
                           if (response.contains("timeout")) {
                               Toast.makeText(getApplicationContext(), "请求超时", Toast.LENGTH_SHORT).show();
                           }
                           if (response.contains("RELAY,ERROR:103")) {
                               Toast.makeText(getApplicationContext(),
                                       "指令参数错误", Toast.LENGTH_SHORT).show();
                           }
                           if (response.contains("RELAY,ERROR:104")) {
                               Toast.makeText(getApplicationContext(),
                                       "指令错误", Toast.LENGTH_SHORT).show();
                           }
                           if (response.contains("has been cut")) {
                               Toast.makeText(getApplicationContext(),
                                       "断油电失败！ 已经处于断油电状态！", Toast.LENGTH_SHORT).show();
                           }
                           if (response.contains("GPS has Not FIXED")) {
                               Toast.makeText(getApplicationContext(),
                                       "断油电失败！，ACC 开启时，GPS没有定位或者速度大于20KM/H, 断油操作延后!", Toast.LENGTH_SHORT).show();
                           }
                           if (response.contains("Resume")) {
                               Toast.makeText(getApplicationContext(),
                                       "恢复油电失败！已经处于油电恢复状态！", Toast.LENGTH_SHORT).show();
                           }
                           if (response.contains("control oil Failed")) {
                               Toast.makeText(getApplicationContext(),
                                       "控制油电失败！", Toast.LENGTH_SHORT).show();
                           }
                           if (response.contains("supply cut off")) {
                               Toast.makeText(getApplicationContext(),
                                       "已处于油电切断状态，命令未运行！", Toast.LENGTH_SHORT).show();
                           }
                           if (response.contains("supply to resume")) {
                               Toast.makeText(getApplicationContext(),
                                       "已经在油电供应状态下恢复，命令没有运行！", Toast.LENGTH_SHORT).show();
                           }  if (response.contains("fail")) {
                               Toast.makeText(getApplicationContext(),
                                       "控制油电失败！", Toast.LENGTH_SHORT).show();
                           }

                       }
                   }else{
                       Toast.makeText(getApplicationContext(), "设置失败", Toast.LENGTH_SHORT).show();
                   }
                   Message message = new Message();
                   message.what = 11;
                   handler.sendMessage(message);
               }

               @Override
               public void FailCallBack(Object object) {
                   Toast.makeText(getApplicationContext(), "设置超时", Toast.LENGTH_SHORT).show();
               }
           });

       }else {
           send = true;
       }

    }

    private void quitSet() {

        if (queue != null)
            queue = null;
        commId = null;
        objectorder = null;
        aSwitch = null;
        imageView_quit = null;
        finish();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            quitSet();
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 保存指令
     */
    private void postOrder() {

        if (AppCons.ORDERBEN == null){
            AppCons.ORDERBEN = new K100B();
            AppCons.ORDERBEN.setTerminalID(AppCons.locationListBean.getTerminalID());
            Log.e("新增",AppCons.ORDERBEN.toString());
        }
            AppCons.ORDERBEN.setCutOff(check);
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
