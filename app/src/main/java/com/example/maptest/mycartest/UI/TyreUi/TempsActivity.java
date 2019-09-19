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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.maptest.mycartest.Bean.CommandResponse;
import com.example.maptest.mycartest.New.NewHttpUtils;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.UI.SetUi.service.K100B;
import com.example.maptest.mycartest.UI.SetUi.service.PostCommand;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.BaseActivity;
import com.example.maptest.mycartest.Utils.TcpSocketClient;
import com.example.maptest.mycartest.Utils.http.ResponseCallback;
import com.google.gson.Gson;

/**
 * Created by ${Author} on 2017/3/7.
 * Use to 温度阀值
 */

public class TempsActivity extends BaseActivity implements View.OnClickListener{
    private Button button_tempyes;
    private TextView textView_temps;
    private ImageView imageView_tempquit,imageView_less,imageView_count;
    private SeekBar seekBar;
    private int i = 10;
    private TcpSocketClient socketClient;
    private String commId;
    private String temId;
    CommandResponse commandResponse;
    private Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
//                    textView_temps.setText(((int)(msg.obj) + 60) +"℃");
                    seekBar.setProgress((int)msg.obj);
                    break;
                case 2:
//                    textView_temps.setText(((int)(msg.obj) + 60) +"℃");
                    seekBar.setProgress((int)msg.obj);
                    break;
                case 11:
                    if (commandResponse != null){
                        PostCommand command = new PostCommand(temId,commId,AppCons.loginDataBean.getData().getUsername(),commandResponse.getContent());
                        NewHttpUtils.postCommand(new String(new Gson().toJson(command)),TempsActivity.this,null);
                    }else {
                        PostCommand command = new PostCommand(temId,commId,AppCons.loginDataBean.getData().getUsername());
                        NewHttpUtils.postCommand(new String(new Gson().toJson(command)),TempsActivity.this,null);
                    }
                    commandResponse = null;
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temps);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        temId = (String) getIntent().getExtras().getSerializable(AppCons.TEST_TYR);
        initView();
        initClick();
//        getTyreOrder();
        getSeek();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_tempcount:
                count();
                break;
            case R.id.image_templess:
                less();
                break;
            case R.id.button_tempyes:
                try {
                    sendTyreOrder();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.image_tempsquits:
                quitOrder();
                break;
        }
    }

    /**
     * 温度不等于25，可加
     */
    private void count() {
        Message message1 = new Message();
        if (i != 25){
            i++;
        }
        message1.what = 1;
        message1.obj = i;
        handler.sendMessage(message1);
    }

    /**
     * 温度不等于0可减
     */
    private void less() {
        Message message2 = new Message();
        if (i != 0){
            i--;
        }
        message2.what = 2;
        message2.obj = i;
        handler.sendMessage(message2);

    }

    private void getSeek(){
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {
                i = progress;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView_temps.setText((progress+60)+"℃");          //温度的值默认加60
                    }
                });
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /**
     * 初始化控件
     */
    private void initView() {

//        socketClient = TcpSocketClient.getInstance();
        socketClient = new TcpSocketClient();
        textView_temps = (TextView) findViewById(R.id.text_temps);
        button_tempyes = (Button) findViewById(R.id.button_tempyes);
        imageView_tempquit = (ImageView) findViewById(R.id.image_tempsquits);
        seekBar = (SeekBar) findViewById(R.id.seekbar_temps);
        imageView_count = (ImageView) findViewById(R.id.image_tempcount);
        imageView_less = (ImageView) findViewById(R.id.image_templess);
        if (AppCons.ORDERBEN != null && AppCons.ORDERBEN.getTireSetting() != null){
            textView_temps.setText(AppCons.ORDERBEN.getTireSetting().getTemperature()+"℃");
            seekBar.setProgress(AppCons.ORDERBEN.getTireSetting().getTemperature() - 60);
        }else {
            textView_temps.setText(70+"℃");
        }

    }

    /**
     * 绑定监听
     */
    private void initClick() {
        imageView_tempquit.setOnClickListener(this);
        button_tempyes.setOnClickListener(this);
        imageView_less.setOnClickListener(this);
        imageView_count.setOnClickListener(this);
    }

    /**
     * @throws InterruptedException
     * 发送温度指令
     */
    private void sendTyreOrder() throws InterruptedException {
//        if (i < 60){
//            Toast.makeText(TempsActivity.this,"温度值必须大于60",Toast.LENGTH_SHORT).show();
//        }else {
            commId = "TPMS_T,1,"+(i + 60)+"#";          //所有的范围，默认加60
            JSONObject object = new JSONObject();
            object.put("terminalID", temId);
            object.put("deviceProtocol", 4);      //设备协议号
//            object.put("CommandType", true);
            object.put("content", commId);                //指令


        NewHttpUtils.sendOrder(object.toJSONString(), TempsActivity.this, new ResponseCallback() {
            @Override
            public void TaskCallBack(Object object) {
                commandResponse = (CommandResponse) object;
                String data = (String) commandResponse.getContent();
                if (data != null){
                    if (data.contains("OK") || data.contains("ok") || data.contains("Success") || data.contains("successfully") || data.contains("Already")) {
                        Toast.makeText(getApplicationContext(), "设置成功", Toast.LENGTH_SHORT).show();
                        postTyreOrder();        //指令收到回应成功后，再保存指令到服务器
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
        handler.removeCallbacksAndMessages(null);
        finish();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode){
            quitOrder();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void postTyreOrder(){
        Gson gson = new Gson();

        if (AppCons.ORDERBEN == null){
            AppCons.ORDERBEN = new K100B();
            AppCons.ORDERBEN.setTerminalID(temId);
            Log.e("新增",AppCons.ORDERBEN.toString());
        }
            AppCons.ORDERBEN.getTireSetting().setTemperature(i+60);
            AppCons.ORDERBEN.setDeviceProtocol(1);
            String obj = gson.toJson(AppCons.ORDERBEN);
            Log.e("Order",obj);
            NewHttpUtils.saveCommand(obj, getApplicationContext(), new ResponseCallback() {
                @Override
                public void TaskCallBack(Object object) {
                    if (object != null){
                        Log.e("tyreorder",object.toString());

                    }
                }

                @Override
                public void FailCallBack(Object object) {

                }
            });
    }



}
