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
import com.android.volley.RequestQueue;
import com.example.maptest.mycartest.Bean.CommandResponse;
import com.example.maptest.mycartest.New.NewHttpUtils;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.UI.SetUi.service.K100B;
import com.example.maptest.mycartest.UI.SetUi.service.PostCommand;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.BaseActivity;
import com.example.maptest.mycartest.Utils.ButtonUtils;
import com.example.maptest.mycartest.Utils.CheckNumBer;
import com.example.maptest.mycartest.Utils.TcpSocketClient;
import com.example.maptest.mycartest.Utils.http.ResponseCallback;
import com.google.gson.Gson;

import java.io.IOException;


/**
 * Created by ${Author} on 2017/3/23.
 * Use to SOS号码设置
 */

public class SosActivity extends BaseActivity implements View.OnClickListener {
    private ImageView imageView_quit;
    private Switch aSwitch;
    private LinearLayout linearLayout_show;
    private EditText editText1, editText2, editText3;
    private Button button;
    private boolean check = true;
    private TcpSocketClient socketClient;
    private String commId;
    public RequestQueue queue;
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
                        NewHttpUtils.postCommand(new String(new Gson().toJson(command)),SosActivity.this,null);
                    }else {
                        PostCommand command = new PostCommand(AppCons.locationListBean.getTerminalID(),commId,AppCons.loginDataBean.getData().getUsername());
                        NewHttpUtils.postCommand(new String(new Gson().toJson(command)),SosActivity.this,null);
                    }
                    commandResponse = null;
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sos);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        initView();
//        reQuestCenter();
        initClick();
    }

    /**
     * 绑定点击监听
     */
    private void initClick() {
        imageView_quit.setOnClickListener(this);
        button.setOnClickListener(this);
    }

    /**
     * 初始化控件
     */
    private void initView() {

        socketClient = TcpSocketClient.getInstance();
        imageView_quit = (ImageView) findViewById(R.id.image_quitsos);
        aSwitch = (Switch) findViewById(R.id.switch_sos);
        linearLayout_show = (LinearLayout) findViewById(R.id.linerlayout_sos);
        editText1 = (EditText) findViewById(R.id.edit_first);
        editText2 = (EditText) findViewById(R.id.edit_second);
        editText3 = (EditText) findViewById(R.id.edit_third);

        if (AppCons.ORDERBEN != null){
            editText1.setText(AppCons.ORDERBEN.getSos1());
            editText2.setText(AppCons.ORDERBEN.getSos2());
            editText3.setText(AppCons.ORDERBEN.getSos3());
        }

        button = (Button) findViewById(R.id.button_sos);
        aSwitch.setChecked(true);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_quitsos:
                quitSet();
                break;
            case R.id.button_sos:     //正则表达式判断电话号码是否合法
                if (!ButtonUtils.isFastDoubleClick(R.id.button_sos)) {
                    //写你相关操作即可
                    if ((!editText3.getText().toString().isEmpty() && CheckNumBer.rexCheckTell(editText3.getText().toString()))
                            || (!editText2.getText().toString().isEmpty() && CheckNumBer.rexCheckTell(editText2.getText().toString()))
                            || (!editText1.getText().toString().isEmpty() && CheckNumBer.rexCheckTell(editText1.getText().toString()))){
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
     * @throws IOException
     * @throws InterruptedException
     * 发送指令
     */
    private void sendDate() throws IOException, InterruptedException {
        if (check) {
            Log.e("check","open");
            /*打开SOS号码*/
            commId = "SOS,A," + editText1.getText().toString() + "," + editText2.getText().toString() + "," + editText3.getText().toString() + "#";
        } else {
            /*关闭SOS号码*/
            Log.e("check","close");
            commId = "SOS,D," + 1 + "," + 2 + "," + 3 + "#";
        }
        objectorder = new JSONObject();
        objectorder.put("terminalID", AppCons.locationListBean.getTerminalID());              //设备的IEMI号
        objectorder.put("deviceProtocol", AppCons.locationListBean.getLocation().getDeviceProtocol());      //设备协议号
        objectorder.put("content", commId);             //指令

        NewHttpUtils.sendOrder(objectorder.toJSONString(), SosActivity.this, new ResponseCallback() {
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

    private void quitSet() {
        if (socketClient != null)
            socketClient = null;
        if (queue != null)
            queue = null;

        commId = null;
        objectorder = null;
        aSwitch = null;
        button = null;
        editText1 = null;
        editText2 = null;
        editText3 = null;
        linearLayout_show = null;
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
     * 保存SOS号码
     */
    private void postOrder() {

        if (AppCons.ORDERBEN == null){
            AppCons.ORDERBEN = new K100B();
            AppCons.ORDERBEN.setTerminalID(AppCons.locationListBean.getTerminalID());
            Log.e("新增",AppCons.ORDERBEN.toString());
        }
            if (check){
                AppCons.ORDERBEN.setSos1(editText1.getText().toString());
                AppCons.ORDERBEN.setSos2(editText2.getText().toString());
                AppCons.ORDERBEN.setSos3(editText3.getText().toString());
            }else {
                AppCons.ORDERBEN.setSos1("");
                AppCons.ORDERBEN.setSos2("");
                AppCons.ORDERBEN.setSos3("");
            }

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
