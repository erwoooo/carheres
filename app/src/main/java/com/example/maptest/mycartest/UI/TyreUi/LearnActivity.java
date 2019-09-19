package com.example.maptest.mycartest.UI.TyreUi;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
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
 * Use to 学习指令
 */

public class LearnActivity extends BaseActivity implements View.OnClickListener{
    private RelativeLayout relativeLayout_leftfont,relativeLayout_leftlater,relativeLayout_rightfont,relativeLayout_rightlater;
    private ImageView imageView_leftfont,imageView_leftlater,imageView_rightfont,imageView_rightlater;
    private Button button_learn;
    private ImageView imageView_learn;
    private int index;
    private TcpSocketClient socketClient;
    private String commId;
    private String temId;
    CommandResponse commandResponse;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 11){
                if (commandResponse != null){
                    PostCommand command = new PostCommand(temId,commId,AppCons.loginDataBean.getData().getUsername(),commandResponse.getContent());
                    NewHttpUtils.postCommand(new String(new Gson().toJson(command)),LearnActivity.this,null);
                }else {
                    PostCommand command = new PostCommand(temId,commId,AppCons.loginDataBean.getData().getUsername());
                    NewHttpUtils.postCommand(new String(new Gson().toJson(command)),LearnActivity.this,null);
                }
                commandResponse = null;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        temId = (String) getIntent().getExtras().getSerializable(AppCons.TEST_TYR);
        initView();
        initClick();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.relative_leftfont:
                leftfont();
                break;
            case R.id.relative_leftlater:
                leftlater();
                break;
            case R.id.relative_rightfont:
                rightfont();
                break;
            case R.id.relative_rightlater:
                rightlatr();
                break;
            case R.id.button_learn:
                try {
                    sendTyreOrder();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.image_learnquits:
                quitOrder();
                break;
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        socketClient = TcpSocketClient.getInstance();
        relativeLayout_leftfont = (RelativeLayout) findViewById(R.id.relative_leftfont);
        relativeLayout_leftlater = (RelativeLayout) findViewById(R.id.relative_leftlater);
        relativeLayout_rightfont = (RelativeLayout) findViewById(R.id.relative_rightfont);
        relativeLayout_rightlater = (RelativeLayout) findViewById(R.id.relative_rightlater);

        imageView_leftfont = (ImageView) findViewById(R.id.image_leftfont);
        imageView_leftlater = (ImageView) findViewById(R.id.image_leftlater);
        imageView_rightfont = (ImageView) findViewById(R.id.image_rightfont);
        imageView_rightlater = (ImageView) findViewById(R.id.image_rightlater);
        button_learn = (Button) findViewById(R.id.button_learn);
        imageView_learn = (ImageView) findViewById(R.id.image_learnquits);

    }

    /**
     * 绑定监听
     */
    private void initClick() {
        relativeLayout_rightfont.setOnClickListener(this);
        relativeLayout_rightlater.setOnClickListener(this);
        relativeLayout_leftlater.setOnClickListener(this);
        relativeLayout_leftfont.setOnClickListener(this);

        button_learn.setOnClickListener(this);
        imageView_learn.setOnClickListener(this);
    }

    /**
     * 右后轮
     */
    private void rightlatr() {
        index = 3;
        imageView_rightlater.setVisibility(View.VISIBLE);
        imageView_rightfont.setVisibility(View.GONE);
        imageView_leftlater.setVisibility(View.GONE);
        imageView_leftfont.setVisibility(View.GONE);
    }

    /**
     * 右前轮
     */
    private void rightfont() {
        index = 1;
        imageView_rightlater.setVisibility(View.GONE);
        imageView_rightfont.setVisibility(View.VISIBLE);
        imageView_leftlater.setVisibility(View.GONE);
        imageView_leftfont.setVisibility(View.GONE);
    }

    /**
     * 左后轮
     */
    private void leftlater() {
        index = 2;
        imageView_rightlater.setVisibility(View.GONE);
        imageView_rightfont.setVisibility(View.GONE);
        imageView_leftlater.setVisibility(View.VISIBLE);
        imageView_leftfont.setVisibility(View.GONE);
    }

    /**
     * 左前轮
     */
    private void leftfont() {
        index = 0;
        imageView_rightlater.setVisibility(View.GONE);
        imageView_rightfont.setVisibility(View.GONE);
        imageView_leftlater.setVisibility(View.GONE);
        imageView_leftfont.setVisibility(View.VISIBLE);
    }


    /**
     * @throws InterruptedException
     * 学习指令
     */
    private void sendTyreOrder() throws InterruptedException {
        Log.d("index",index+"");
            switch (index){
                case 0:
                    commId = "TPMS_S,1#";
                    break;
                case 1:
                    commId = "TPMS_S,2#";
                    break;
                case 2:
                    commId = "TPMS_S,3#";
                    break;
                case 3:
                    commId = "TPMS_S,4#";
                    break;
            }

            JSONObject object = new JSONObject();
            object.put("terminalID", temId);
            object.put("deviceProtocol", 4);      //设备协议号
            object.put("content", commId);                 //指令
            Log.d("index",object.toString());


        NewHttpUtils.sendOrder(object.toJSONString(), LearnActivity.this, new ResponseCallback() {
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
