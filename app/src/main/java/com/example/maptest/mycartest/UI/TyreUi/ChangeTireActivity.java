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
import android.widget.RelativeLayout;
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
 * Created by ${Author} on 2017/3/7.
 * Use to 换轮胎指令
 */

public class ChangeTireActivity extends BaseActivity implements View.OnClickListener{
    private RelativeLayout relativeLayout1,relativeLayout2,relativeLayout3,relativeLayout4,relativeLayout5,relativeLayout6;
    private ImageView imageView1,imageView2,imageView3,imageView4,imageView5,imageView6;
    private Button button_changes;
    private ImageView imageView_quitchang;
    private int index;

    private String commId;
    private String temId;
    private TcpSocketClient socketClient;
    CommandResponse commandResponse;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 11){
                if (commandResponse != null){
                    PostCommand command = new PostCommand(temId,commId,AppCons.loginDataBean.getData().getUsername(),commandResponse.getContent());
                    NewHttpUtils.postCommand(new String(new Gson().toJson(command)),ChangeTireActivity.this,null);
                }else {
                    PostCommand command = new PostCommand(temId,commId,AppCons.loginDataBean.getData().getUsername());
                    NewHttpUtils.postCommand(new String(new Gson().toJson(command)),ChangeTireActivity.this,null);
                }
                commandResponse = null;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changetire);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        temId = (String) getIntent().getExtras().getSerializable(AppCons.TEST_TYR);
        initView();
        initClick();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.relative1:
                relative1();
                break;
            case R.id.relative2:
                relative2();
                break;
            case R.id.relative3:
                relative3();
                break;
            case R.id.relative4:
                relative4();
                break;
            case R.id.relative5:
                relative5();
                break;
            case R.id.relative6:
                relative6();
                break;
            case R.id.button_tirechange:
                try {
                    sendTyreOrder();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.image_changetirequit:
                quitOrder();
                break;

        }

    }

    /**
     * 选择第一个指令
     */
    private void relative1() {
        index = 0;
        imageView1.setVisibility(View.VISIBLE);
        imageView2.setVisibility(View.GONE);
        imageView3.setVisibility(View.GONE);
        imageView4.setVisibility(View.GONE);
        imageView5.setVisibility(View.GONE);
        imageView6.setVisibility(View.GONE);
    }


    /**
     * 选择第2个指令
     */
    private void relative2() {
        index = 1;
        imageView1.setVisibility(View.GONE);
        imageView2.setVisibility(View.VISIBLE);
        imageView3.setVisibility(View.GONE);
        imageView4.setVisibility(View.GONE);
        imageView5.setVisibility(View.GONE);
        imageView6.setVisibility(View.GONE);
    }

    /**
     * 选择第3个指令
     */
    private void relative3() {
        index = 2;
        imageView1.setVisibility(View.GONE);
        imageView2.setVisibility(View.GONE);
        imageView3.setVisibility(View.VISIBLE);
        imageView4.setVisibility(View.GONE);
        imageView5.setVisibility(View.GONE);
        imageView6.setVisibility(View.GONE);
    }

    /**
     * 选择第4个指令
     */
    private void relative4() {
        index = 3;
        imageView1.setVisibility(View.GONE);
        imageView2.setVisibility(View.GONE);
        imageView3.setVisibility(View.GONE);
        imageView4.setVisibility(View.VISIBLE);
        imageView5.setVisibility(View.GONE);
        imageView6.setVisibility(View.GONE);
    }

    /**
     * 选择第5个指令
     */
    private void relative5() {
        index = 4;
        imageView1.setVisibility(View.GONE);
        imageView2.setVisibility(View.GONE);
        imageView3.setVisibility(View.GONE);
        imageView4.setVisibility(View.GONE);
        imageView5.setVisibility(View.VISIBLE);
        imageView6.setVisibility(View.GONE);
    }

    /**
     * 选择第6个指令
     */
    private void relative6() {
        index = 5;
        imageView1.setVisibility(View.GONE);
        imageView2.setVisibility(View.GONE);
        imageView3.setVisibility(View.GONE);
        imageView4.setVisibility(View.GONE);
        imageView5.setVisibility(View.GONE);
        imageView6.setVisibility(View.VISIBLE);

    }

    /**
     * 初始化控件
     */
    private void initView() {
        socketClient = TcpSocketClient.getInstance();
        relativeLayout1 = (RelativeLayout) findViewById(R.id.relative1);
        relativeLayout2 = (RelativeLayout) findViewById(R.id.relative2);
        relativeLayout3 = (RelativeLayout) findViewById(R.id.relative3);
        relativeLayout4 = (RelativeLayout) findViewById(R.id.relative4);
        relativeLayout5 = (RelativeLayout) findViewById(R.id.relative5);
        relativeLayout6 = (RelativeLayout) findViewById(R.id.relative6);

        imageView1 = (ImageView) findViewById(R.id.image_change1);
        imageView2 = (ImageView) findViewById(R.id.image_change2);
        imageView3 = (ImageView) findViewById(R.id.image_change3);
        imageView4 = (ImageView) findViewById(R.id.image_change4);
        imageView5 = (ImageView) findViewById(R.id.image_change5);
        imageView6 = (ImageView) findViewById(R.id.image_change6);

        button_changes = (Button) findViewById(R.id.button_tirechange);
        imageView_quitchang = (ImageView) findViewById(R.id.image_changetirequit);
    }

    /**
     * 绑定点击监听
     */
    private void initClick() {
        relativeLayout1.setOnClickListener(this);
        relativeLayout2.setOnClickListener(this);
        relativeLayout3.setOnClickListener(this);
        relativeLayout4.setOnClickListener(this);
        relativeLayout5.setOnClickListener(this);
        relativeLayout6.setOnClickListener(this);

        button_changes.setOnClickListener(this);
        imageView_quitchang.setOnClickListener(this);

    }

    /**
     * @throws InterruptedException
     * 胎压轮换指令
     */
    private void sendTyreOrder() throws InterruptedException {
        switch (index){
            case 0:
                commId = "TPMS_C,1#";
                break;
            case 1:
                commId = "TPMS_C,2#";
                break;
            case 2:
                commId = "TPMS_C,3#";
                break;
            case 3:
                commId = "TPMS_C,4#";
                break;
            case 4:
                commId = "TPMS_C,5#";
                break;
            case 5:
                commId = "TPMS_C,6#";
                break;
        }

        JSONObject object = new JSONObject();
        object.put("terminalID", temId);
        object.put("deviceProtocol", 4);      //设备协议号
//        object.put("CommandType", true);
        object.put("content", commId);                //指令

        NewHttpUtils.sendOrder(object.toJSONString(), ChangeTireActivity.this, new ResponseCallback() {
            @Override
            public void TaskCallBack(Object object) {
                commandResponse = (CommandResponse) object;
                String data =  commandResponse.getContent();
                if (data != null){
                    if (data.contains("OK") || data.contains("ok") || data.contains("Success") || data.contains("successfully") || data.contains("Already")) {
                        Toast.makeText(getApplicationContext(), "设置成功", Toast.LENGTH_SHORT).show();
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

    private void quitOrder(){
        if (socketClient != null)
            socketClient = null;

        commId = null;

        temId = null;
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
