package com.example.maptest.mycartest.UI.TyreUi;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.maptest.mycartest.Bean.CommandResponse;
import com.example.maptest.mycartest.New.NewHttpUtils;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.UI.SetUi.service.PostCommand;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.BaseActivity;
import com.example.maptest.mycartest.Utils.TcpSocketClient;
import com.example.maptest.mycartest.Utils.http.ResponseCallback;
import com.google.gson.Gson;

/**
 * Created by ${Author} on 2017/3/8.
 * Use to 查询胎压
 */

public class QuerryActivity extends BaseActivity implements View.OnClickListener {
    private ImageView imageView_qurryquit;
    private Button button_qurry;
    private String commId;
    private TcpSocketClient socketClient;

    private String temId;
    CommandResponse commandResponse;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 11){
                if (commandResponse != null){
                    PostCommand command = new PostCommand(temId,commId,AppCons.loginDataBean.getData().getUsername(),commandResponse.getContent());
                    NewHttpUtils.postCommand(new String(new Gson().toJson(command)),QuerryActivity.this,null);
                }else {
                    PostCommand command = new PostCommand(temId,commId,AppCons.loginDataBean.getData().getUsername());
                    NewHttpUtils.postCommand(new String(new Gson().toJson(command)),QuerryActivity.this,null);
                }
                commandResponse = null;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qurry);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        temId = (String) getIntent().getExtras().getSerializable(AppCons.TEST_TYR);
        initView();
        initClick();
    }

    private void initClick() {

        imageView_qurryquit.setOnClickListener(this);
        button_qurry.setOnClickListener(this);
    }


    private void initView() {
        socketClient = TcpSocketClient.getInstance();
        imageView_qurryquit = (ImageView) findViewById(R.id.image_querryquits);
        button_qurry = (Button) findViewById(R.id.button_querry);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_querryquits:
               quitOrder();
                break;
            case R.id.button_querry:
                try {
                    sendTyreOrder();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    /**
     * @throws InterruptedException
     * 查询胎压指令
     */
    private void sendTyreOrder() throws InterruptedException {

        commId = "TPMS#";
        JSONObject object = new JSONObject();
        object.put("terminalID", temId);
        object.put("deviceProtocol", 4);      //设备协议号
//        object.put("CommandType", true);
        object.put("content", commId);



        NewHttpUtils.sendOrder(object.toJSONString(), QuerryActivity.this, new ResponseCallback() {
            @Override
            public void TaskCallBack(Object object) {
                commandResponse = (CommandResponse) object;
                String data = (String) commandResponse.getContent();
                if (data != null){
                    String response = data;
                    if (response.contains("OK") || response.contains("ok") || response.contains("Success") || response.contains("successfully") || response.contains("Already")) {
                        Toast.makeText(QuerryActivity.this, "通过", Toast.LENGTH_SHORT).show();
                    } else if (response.contains("timeout")) {
                        Toast.makeText(QuerryActivity.this, "请求超时", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(QuerryActivity.this, "未通过", Toast.LENGTH_SHORT).show();
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

    private void quitOrder(){
        if (socketClient != null)
            socketClient = null;

        commId = null;

        finish();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode){
            quitOrder();
        }
        return super.onKeyDown(keyCode, event);
    }
}
