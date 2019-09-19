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
 * Created by ${Author} on 2017/3/8.
 * Use to 胎压值修改界面
 */

public class TyreValueActivity extends BaseActivity implements View.OnClickListener{

    private ImageView imageView_fdc,imageView_fdl,imageView_fuc,imageView_ful,imageView_ldc,imageView_ldl,imageView_lul,imageView_luc;
    private SeekBar seekBar_fd,seekBar_fu,seekBar_ld,seekBar_lu;
    private TextView textView_fd,textView_fu,textView_ld,textView_lu;
    private ImageView imageView_quitvalue;
    private Button button_value,button1,button2,button3;
    private int a, b , c  ,d ;
    private float f1,f2,f3,f4;
    private int index = 0;
    private String commId;
    private TcpSocketClient socketClient;

    private String temId;
    CommandResponse commandResponse;
    float sumTire;
    private Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    seekBar_fd.setProgress((int)msg.obj);
                    break;
                case 2:
                    seekBar_fd.setProgress((int)msg.obj);
                    break;
                case 3:
                    seekBar_fu.setProgress((int)msg.obj);
                    break;
                case 4:
                    seekBar_fu.setProgress((int)msg.obj);
                    break;
                case 5:
                    seekBar_ld.setProgress((int)msg.obj);
                    break;
                case 6:
                    seekBar_ld.setProgress((int)msg.obj);
                    break;
                case 7:
                    seekBar_lu.setProgress((int)msg.obj);
                    break;
                case 8:
                    seekBar_lu.setProgress((int)msg.obj);
                    break;
                case 11:
                    if (commandResponse != null){
                        PostCommand command = new PostCommand(temId,commId,AppCons.loginDataBean.getData().getUsername(),commandResponse.getContent());
                        NewHttpUtils.postCommand(new String(new Gson().toJson(command)),TyreValueActivity.this,null);
                    }else {
                        PostCommand command = new PostCommand(temId,commId,AppCons.loginDataBean.getData().getUsername());
                        NewHttpUtils.postCommand(new String(new Gson().toJson(command)),TyreValueActivity.this,null);
                    }
                    commandResponse = null;
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tyrevalue);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        temId = (String) getIntent().getExtras().getSerializable(AppCons.TEST_TYR);
        initView();
        initClick();
        getTireState();
        getValues();
    }

    /**
     * 绑定点击监听
     */
    private void initClick() {
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        imageView_fdc.setOnClickListener(this);
        imageView_fdl.setOnClickListener(this);
        imageView_ful.setOnClickListener(this);
        imageView_fuc.setOnClickListener(this);

        imageView_ldc.setOnClickListener(this);
        imageView_ldl.setOnClickListener(this);
        imageView_lul.setOnClickListener(this);
        imageView_luc.setOnClickListener(this);

        button_value.setOnClickListener(this);
        imageView_quitvalue.setOnClickListener(this);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        socketClient = TcpSocketClient.getInstance();

        button1 = (Button) findViewById(R.id.button_tyre0);
        button2 = (Button) findViewById(R.id.button_tyre1);
        button3 = (Button) findViewById(R.id.button_tyre2);

        imageView_fdc = (ImageView) findViewById(R.id.image_fontdowncount);
        imageView_fdl = (ImageView) findViewById(R.id.image_fonttdownless);
        imageView_fuc = (ImageView) findViewById(R.id.image_fontupcount);
        imageView_ful = (ImageView) findViewById(R.id.image_fontupless);

        imageView_ldc = (ImageView) findViewById(R.id.image_laterdowncount);
        imageView_ldl = (ImageView) findViewById(R.id.image_laterdownless);
        imageView_luc = (ImageView) findViewById(R.id.image_laterupcount);
        imageView_lul = (ImageView) findViewById(R.id.image_laterupless);

        textView_fd = (TextView) findViewById(R.id.text_fontdown);
        textView_fu = (TextView) findViewById(R.id.text_fontup);
        textView_ld = (TextView) findViewById(R.id.text_laterdown);
        textView_lu = (TextView) findViewById(R.id.text_laterup);

        seekBar_fd = (SeekBar) findViewById(R.id.seekbar_fontdownvalue);
        seekBar_fu = (SeekBar) findViewById(R.id.seekbar_fontupvalue);
        seekBar_ld = (SeekBar) findViewById(R.id.seekbar_laterdownvalue);
        seekBar_lu = (SeekBar) findViewById(R.id.seekbar_laterupvalue);

        button_value = (Button) findViewById(R.id.button_valueyes);
        imageView_quitvalue = (ImageView) findViewById(R.id.image_valuequit);

        if (AppCons.ORDERBEN != null && AppCons.ORDERBEN.getTireSetting() != null){
            f1 = AppCons.ORDERBEN.getTireSetting().getNo1threshold();
            f2 = AppCons.ORDERBEN.getTireSetting().getNo2threshold();
            f3 = AppCons.ORDERBEN.getTireSetting().getNo3threshold();
            f4 = AppCons.ORDERBEN.getTireSetting().getNo4threshold();

            textView_fd.setText("前轮低压:"+f1+"Bar");
            textView_fu.setText("前轮高压:"+f2+"Bar");
            textView_ld.setText("后轮低压:"+f3+"Bar");
            textView_lu.setText("后轮高压:"+f4+"Bar");
            if (f1 != 0){
                seekBar_fd.setProgress((int)(f1 * 10 - 13));
            }
            if (f2 != 0){
                seekBar_fu.setProgress((int)(f2 * 10 - 26));
            }
            if (f3 != 0){
                seekBar_ld.setProgress((int)(f3 * 10 - 13));
            }
            if (f4 != 0){
                seekBar_lu.setProgress((int)(f4 * 10 - 26));
            }

        }

        a = seekBar_fd.getProgress();
        b = seekBar_fu.getProgress();
        c = seekBar_ld.getProgress();
        d = seekBar_lu.getProgress();
    }

    /**
     * @param v
     * 点击事件
     */
    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.image_fontdowncount:
                    Message message1 = new Message();
                    if (a != 12){
                        a ++;
                    }
                    message1.what = 1;
                    message1.obj = a;
                    handler.sendMessage(message1);

                    break;
                case R.id.image_fonttdownless:
                    Message message2 = new Message();
                    if (a != 0){
                        a --;
                    }
                    message2.what = 2;
                    message2.obj = a;
                    handler.sendMessage(message2);
                    break;
                case R.id.image_fontupcount:
                    Message message3 = new Message();
                    if (b != 24){
                        b ++;
                    }
                    message3.what = 3;
                    message3.obj = b;
                    handler.sendMessage(message3);
                    break;
                case R.id.image_fontupless:
                    Message message4 = new Message();
                    if (b != 0){
                        b --;
                    }
                    message4.what = 4;
                    message4.obj = b;
                    handler.sendMessage(message4);
                    break;
                case R.id.image_laterdowncount:
                    Message message5 = new Message();
                    if (c != 12){
                        c ++;
                    }
                    message5.what = 5;
                    message5.obj = c;
                    handler.sendMessage(message5);
                    break;
                case R.id.image_laterdownless:
                    Message message6 = new Message();
                    if (c != 0){
                        c --;
                    }
                    message6.what = 6;
                    message6.obj = c;
                    handler.sendMessage(message6);
                    break;
                case R.id.image_laterupcount:
                    Message message7 = new Message();
                    if (d != 24){
                        d ++;
                    }
                    message7.what = 7;
                    message7.obj = d;
                    handler.sendMessage(message7);
                    break;
                case R.id.image_laterupless:
                    Message message8 = new Message();
                    if (d != 0){
                        d --;
                    }
                    message8.what = 8;
                    message8.obj = d;
                    handler.sendMessage(message8);

                    break;
                case R.id.image_valuequit:
                    quitOrder();
                    break;
                case R.id.button_valueyes:
                    index = 3;
                    try {
                        sendTyreOrder();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.button_tyre0:
                    index = 0;
                    try {
                        sendTyreOrder();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.button_tyre1:
                    index = 1;
                    try {
                        sendTyreOrder();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.button_tyre2:
                    index = 2;
                    try {
                        sendTyreOrder();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
            }
    }

    /**
     * seekbar滑动，设置阈值
     */
    private void getValues(){
        seekBar_fd.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        a = progress;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView_fd.setText("前轮低压:"+((float)(a + 13)/10)+"Bar");
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
        seekBar_fu.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    b = progress;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView_fu.setText("前轮高压:" + (((float)(b +  26))/10) + "Bar");
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
        seekBar_ld.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    c = progress;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView_ld.setText("后轮低压:" + (((float)(c + 13))/10) +"Bar");
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
        seekBar_lu.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    d = progress;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView_lu.setText("后轮高压:" + (((float)(d + 26))/10 ) + "Bar");
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
     * @throws InterruptedException
     * 发送胎压阈值指令
     */
    private void sendTyreOrder() throws InterruptedException {
            switch (index){
                case 0:
                    commId = "TPMS_P,1,"+((a+13)*10)+"#";
                    sumTire = ((float)(a + 13)/10);
                    break;
                case 1:
                    commId = "TPMS_P,2,"+((b+26)*10)+"#";
                    sumTire = (((float)(b +  26))/10);
                    break;
                case 2:
                    commId = "TPMS_P,3,"+((c + 13)*10)+"#";
                    sumTire = (((float)(c + 13))/10);
                    break;
                case 3:
                    commId = "TPMS_P,4,"+((d + 26 )*10)+"#";
                    sumTire = (((float)(d + 26))/10 );
                    break;
            }
                JSONObject object = new JSONObject();
                object.put("terminalID", temId);
                object.put("deviceProtocol", 4);      //设备协议号
                object.put("content", commId);


        NewHttpUtils.sendOrder(object.toJSONString(), TyreValueActivity.this, new ResponseCallback() {
            @Override
            public void TaskCallBack(Object object) {
                commandResponse = (CommandResponse) object;
                String data = (String) commandResponse.getContent();
                if (data != null){
                    if (data.contains("OK") || data.contains("ok") || data.contains("Success") || data.contains("successfully") || data.contains("Already")) {
                        Toast.makeText(getApplicationContext(), "设置成功", Toast.LENGTH_SHORT).show();
                        postTireOrder(sumTire);        //指令收到回应成功后，再保存指令到服务器
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

    private void postTireOrder(final float tireSum){
        Gson gson = new Gson();
        if (AppCons.ORDERBEN == null){
            AppCons.ORDERBEN = new K100B();
            AppCons.ORDERBEN.setTerminalID(temId);
            Log.e("新增",AppCons.ORDERBEN.toString());
        }
            switch (index){
                case 2:
                    AppCons.ORDERBEN.getTireSetting().setNo1threshold(tireSum);
                    break;
                case 1:
                    AppCons.ORDERBEN.getTireSetting().setNo2threshold(tireSum);
                    break;
                case 0:
                    AppCons.ORDERBEN.getTireSetting().setNo3threshold(tireSum);
                    break;
                case 3:
                    AppCons.ORDERBEN.getTireSetting().setNo4threshold(tireSum);
                    break;
            }
            AppCons.ORDERBEN.setDeviceProtocol(1);
            String obj = gson.toJson(AppCons.ORDERBEN);
            Log.e("t胎压值",obj);
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

    private void getTireState() {

    }

    }

