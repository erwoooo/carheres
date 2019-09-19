package com.example.maptest.mycartest.UI.SetUi;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.maptest.mycartest.Bean.CommandResponse;
import com.example.maptest.mycartest.New.NewHttpUtils;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.UI.SetUi.service.K100B;
import com.example.maptest.mycartest.UI.SetUi.service.PostCommand;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.BaseActivity;
import com.example.maptest.mycartest.Utils.ButtonUtils;
import com.example.maptest.mycartest.Utils.CheckNumBer;
import com.example.maptest.mycartest.Utils.http.ResponseCallback;
import com.google.gson.Gson;

import java.io.IOException;


/**
 * Created by ${Author} on 2017/3/23.
 * Use to 设置中心号码
 */

public class CenterNubActivity extends BaseActivity implements View.OnClickListener {
    private ImageView imageView_quit;
    private Switch aSwitch;
    private EditText editText_center;
    private Button button_sure;
    private LinearLayout linearLayout_show;
    private boolean check = true;
    private String commId;
    private JSONObject objectorder;
    private CommandResponse commandResponse;

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 11:
                    if (commandResponse != null){
                        Log.e("commandResponse",commandResponse.toString());
                        PostCommand command = new PostCommand(AppCons.locationListBean.getTerminalID(),commId,AppCons.loginDataBean.getData().getUsername(),commandResponse.getContent());
                        NewHttpUtils.postCommand(new String(new Gson().toJson(command)),CenterNubActivity.this,null);
                    }else {
                        Log.e("commandResponse","no commandResponse");
                        PostCommand command = new PostCommand(AppCons.locationListBean.getTerminalID(),commId,AppCons.loginDataBean.getData().getUsername());
                        NewHttpUtils.postCommand(new String(new Gson().toJson(command)),CenterNubActivity.this,null);
                    }
                    commandResponse = null;
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        initView();
        initClick();

    }

    /**
     * 初始化监听事件
     */
    private void initClick() {
        imageView_quit.setOnClickListener(this);
        button_sure.setOnClickListener(this);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        linearLayout_show = (LinearLayout) findViewById(R.id.linerlayout_center);
        imageView_quit = (ImageView) findViewById(R.id.image_quitcenter);
        aSwitch = (Switch) findViewById(R.id.switch_center);
        editText_center = (EditText) findViewById(R.id.edit_center);
        if (AppCons.ORDERBEN != null){
            editText_center.setText(AppCons.ORDERBEN.getCenter());
        }
        button_sure = (Button) findViewById(R.id.button_center);
        aSwitch.setChecked(true);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /**
             * @param buttonView
             * @param isChecked
             * 点击开关，swicth开，设置，关闭也设置
             */
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    check = true;
                    linearLayout_show.setVisibility(View.VISIBLE);
                } else {
                    check = false;
                    linearLayout_show.setVisibility(View.GONE);
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

    /**
     * @param v
     * 控件点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_quitcenter:
                quitSet();
                break;
            case R.id.button_center:
                //判断字符串是否是电话号码
                if (!ButtonUtils.isFastDoubleClick(R.id.button_center)) {       //判断点击间隔
                    //写你相关操作即可
                    if (CheckNumBer.rexCheckTell(editText_center.getText().toString()) &&  !editText_center.getText().toString().isEmpty()){
                        try {
                            sendDate();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else {
                        Toast.makeText(getApplicationContext(),"请输入合法的电话号码",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }


    /**
     * 〈发送指令〉
     * 〈功能详细描述〉
     *
     * @throws IOException
     * @throws InterruptedException 发送设置中心号码指令
     */
    private void sendDate() throws IOException, InterruptedException {

        if (check) {
            commId = "CENTER,A," + editText_center.getText() + "#";     //指令内容
        } else {
            commId = "CENTER,D#";                                       //指令内容
        }
        objectorder= new JSONObject();
        objectorder.put("terminalID", AppCons.locationListBean.getTerminalID());              //设备的IEMI号
        objectorder.put("deviceProtocol", AppCons.locationListBean.getLocation().getDeviceProtocol());      //设备协议号
        objectorder.put("content", commId);

        NewHttpUtils.sendOrder(objectorder.toJSONString(), CenterNubActivity.this, new ResponseCallback() {
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

    /**
     * 退出当前页面
     */
    private void quitSet() {
        commId = null;
        aSwitch = null;
        editText_center = null;
        button_sure = null;
        imageView_quit = null;
        objectorder = null;
        linearLayout_show = null;
        finish();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("ceactivity", "onStop");

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            quitSet();
        }
        return super.onKeyDown(keyCode, event);
    }



    /**
     * 〈提交中心号码〉
     * 〈功能详细描述〉
     * 将中心号码封装成字符串发送到服务器
     */
    private void postOrder() {
        if (AppCons.ORDERBEN == null){
            AppCons.ORDERBEN = new K100B();
            AppCons.ORDERBEN.setTerminalID(AppCons.locationListBean.getTerminalID());
        }
        if (check){
            AppCons.ORDERBEN.setCenter(editText_center.getText().toString());
        }else {
            AppCons.ORDERBEN.setCenter("");
        }

        AppCons.ORDERBEN.setDeviceProtocol(AppCons.locationListBean.getLocation().getDeviceProtocol());
        Gson gson = new Gson();
        String obj = gson.toJson(AppCons.ORDERBEN);
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
