package com.example.maptest.mycartest.UI.SetUi;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.example.maptest.mycartest.Adapter.SpinnerAdapter;
import com.example.maptest.mycartest.Bean.CommandResponse;
import com.example.maptest.mycartest.Bean.Gps;
import com.example.maptest.mycartest.Bean.SpineBean;
import com.example.maptest.mycartest.New.NewHttpUtils;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.UI.SetUi.service.K100B;
import com.example.maptest.mycartest.UI.SetUi.service.PostCommand;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.BaseActivity;
import com.example.maptest.mycartest.Utils.ButtonUtils;
import com.example.maptest.mycartest.Utils.PositionUtil;
import com.example.maptest.mycartest.Utils.UtcDateChang;
import com.example.maptest.mycartest.Utils.http.ResponseCallback;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ${Author} on 2017/3/29.
 * Use to 编辑电子围栏
 */

public class EditCrawlActivity extends BaseActivity implements View.OnClickListener {
    private ImageView imageView_quit;
    private EditText editText_name;
    private TextView textView_rdio;
    private Spinner spinner_type, spinner_warn;
    private Button button;
    private SpinnerAdapter adapter, tyAdapter;
    private List<SpineBean> spList;
    private List<SpineBean> tyList;
    private int dis;
    private double mLat, mLon;

    private int tyPst = 0, waPst = 0;
    private String commId;
    private String name;
    private long time;
    private JSONObject object;
    public RequestQueue queue;
    private String jsonOrder;
    private Map<String,Object>mapOrder = new HashMap<>();
    CommandResponse commandResponse;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 4:
                    startActivity(new Intent(EditCrawlActivity.this, CrawlActivity.class));
                    quitSet();
                    break;
                case 11:
                    if (commandResponse != null){
                        PostCommand command = new PostCommand(AppCons.locationListBean.getTerminalID(),commId,AppCons.loginDataBean.getData().getUsername(),commandResponse.getContent());
                        NewHttpUtils.postCommand(new String(new Gson().toJson(command)),EditCrawlActivity.this,null);
                    }else {
                        PostCommand command = new PostCommand(AppCons.locationListBean.getTerminalID(),commId,AppCons.loginDataBean.getData().getUsername());
                        NewHttpUtils.postCommand(new String(new Gson().toJson(command)),EditCrawlActivity.this,null);
                    }
                    commandResponse = null;
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editcrawl);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        dis = (int) getIntent().getExtras().get("dis");
        mLat = (double) getIntent().getExtras().get("la");
        mLon = (double) getIntent().getExtras().get("lo");
        initView();
        initClick();
        dates();
        initSpdate();
    }
    /**
     * 初始化点击事件
     */
    private void initClick() {
        imageView_quit.setOnClickListener(this);
        button.setOnClickListener(this);
    }

    /**
     * 初始化控件
     */
    private void initView() {

        imageView_quit = (ImageView) findViewById(R.id.image_quitsetcrawl);
        editText_name = (EditText) findViewById(R.id.edit_name);
        textView_rdio = (TextView) findViewById(R.id.text_radir);
        spinner_type = (Spinner) findViewById(R.id.spinner_type);
        spinner_warn = (Spinner) findViewById(R.id.spinner_warntype);
        button = (Button) findViewById(R.id.button_setcrawl);
        textView_rdio.setText("围栏半径:" + dis + "米");

    }

    /**
     * @param v
     * 控件的点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_quitsetcrawl:
                quitSet();      //退出
                break;
            case R.id.button_setcrawl:      //发送指令
                if (!ButtonUtils.isFastDoubleClick(R.id.button_setcrawl)) {
                    //写你相关操作即可
                    try {
                        sendDate();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


                break;
        }
    }

    /**
     * 下拉框数据源
     */
    private void dates() {
        spList = new ArrayList<>();         //报警方式
        SpineBean bean1 = new SpineBean("平台");
        SpineBean bean2 = new SpineBean("平台 + 短信");
//        SpineBean bean3 = new SpineBean("平台 + 短信 + 电话");
//        SpineBean bean9 = new SpineBean("平台 + 电话");
        spList.add(bean1);
        spList.add(bean2);
//        spList.add(bean3);
//        spList.add(bean9);

        tyList = new ArrayList<>();     //围栏类型
        SpineBean bean4 = new SpineBean("进围栏");
        SpineBean bean5 = new SpineBean("出围栏");
        SpineBean bean6 = new SpineBean("进出围栏");

        tyList.add(bean4);
        tyList.add(bean5);
        tyList.add(bean6);
    }

    /**
     * 两个下拉框赋值
     */
    private void initSpdate() {
        adapter = new SpinnerAdapter(spList, EditCrawlActivity.this);
        tyAdapter = new SpinnerAdapter(tyList, EditCrawlActivity.this);
        spinner_type.setAdapter(tyAdapter);
        spinner_warn.setAdapter(adapter);

        /*报警类型*/
        spinner_warn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                waPst = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /*围栏类型*/
        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tyPst = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    /**
     * @throws IOException
     * @throws InterruptedException 发送设置的电子围栏
     */
    private void sendDate() throws IOException, InterruptedException {
        Gps gps = PositionUtil.bd09_To_Gps84(mLat, mLon);    /*将电子围栏的经纬度转换成GPS经纬度*/
        Log.e("GPS", "GPS坐标" + gps);
        DecimalFormat df   = new DecimalFormat("######0.000000");
        double lat = Double.parseDouble(df.format(gps.getWgLat()));
        double lon = Double.parseDouble(df.format(gps.getWgLon()));
        switch (tyPst) {       /*围栏类型*/
            case 0:                     /*平台报警*/
                switch (waPst) {
                    case 0:
                        commId = "FENCE,ON,0,N" + lat + ",E" + lon + "," + (dis / 100) + ",IN,0#";        //进围栏平台报警
                        break;
                    case 1:
                        commId = "FENCE,ON,0,N" + lat + ",E" + lon + "," + (dis / 100) + ",IN,1#";        //进围栏平台+短信报警
                        break;
                    case 2:
                        commId = "FENCE,ON,0,N" + lat + ",E" + lon + "," + (dis / 100) + ",IN,2#";        //进围栏平台+短信+电话报警
                        break;
                    case 3:
                        commId = "FENCE,ON,0,N" + lat + ",E" + lon + "," + (dis / 100) + ",IN,3#";        //进围栏平台+电话报警
                        break;
                }
                break;
            case 1:                                                                                                   //出围栏
                switch (waPst) {
                    case 0:
                        commId = "FENCE,ON,0,N" + lat + ",E" + lon + "," + (dis / 100) + ",OUT,0#";   //出围栏平台报警
                        break;
                    case 1:
                        commId = "FENCE,ON,0,N" + lat + ",E" + lon + "," + (dis / 100) + ",OUT,1#";  //出围栏平台+短信报警
                        break;
                    case 2:
                        commId = "FENCE,ON,0,N" + lat + ",E" + lon + "," + (dis / 100) + ",OUT,2#";  //出围栏平台+短信+电话报警
                        break;
                    case 3:
                        commId = "FENCE,ON,0,N" + lat + ",E" + lon + "," + (dis / 100) + ",OUT,3#";  //出围栏平台+电话报警
                        break;
                }
                break;
            case 2:
                switch (waPst) {
                    case 0:
                        commId = "FENCE,ON,0,N" + lat + ",E" + lon + "," + (dis / 100) + ",,0#";   //进出围栏平台报警
                        break;
                    case 1:
                        commId = "FENCE,ON,0,N" + lat + ",E" + lon + "," + (dis / 100) + ",,1#";             //进出围栏平台+短信报警
                        break;
                    case 2:
                        commId = "FENCE,ON,0,N" + lat + ",E" + lon + "," + (dis / 100) + ",,2#";             //进出围栏平台+短信+电话报警
                        break;
                    case 3:
                        commId = "FENCE,ON,0,N" + lat + ",E" + lon + "," + (dis / 100) + ",,3#";             //进出围栏平台+电话报警
                        break;
                }
                break;
        }
        Log.e("当前的时间",UtcDateChang.Local2UTC(new Date()) + "");
        mapOrder.put("terminalID", AppCons.locationListBean.getTerminalID());              //设备的IEMI号
        mapOrder.put("deviceProtocol", AppCons.locationListBean.getLocation().getDeviceProtocol());      //设备协议号
        mapOrder.put("content", commId);
        jsonOrder = JSON.toJSONString(mapOrder);


        NewHttpUtils.sendOrder(jsonOrder, EditCrawlActivity.this, new ResponseCallback() {
            @Override
            public void TaskCallBack(Object object) {
                commandResponse = (CommandResponse) object;
                String data = commandResponse.getContent();
                if (data != null){
                    if (data.contains("OK") || data.contains("ok") || data.contains("Success") || data.contains("successfully") || data.contains("Already")) {
                        Toast.makeText(getApplicationContext(), "设置成功", Toast.LENGTH_SHORT).show();
                        try {
                            postOrder();        //指令收到回应成功后，再保存指令到服务器
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
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
                Toast.makeText(getApplicationContext(), "设置失败", Toast.LENGTH_SHORT).show();
            }
        });
        Message message = new Message();
        message.what = 11;
        handler.sendMessage(message);
    }

    private void quitSet() {

        if (object != null)
            object = null;
        if (queue != null)
            queue = null;
        dis = 0;
        mLon = 0;
        mLat = 0;
        name = null;
        handler.removeCallbacksAndMessages(null);
        handler = null;
        adapter = null;
        button = null;
        commId  = null;
        editText_name = null;
        imageView_quit = null;
        spList.clear();
        spList = null;
        spinner_type = null;
        spinner_warn = null;
        textView_rdio = null;
        tyAdapter = null;
        tyList.clear();
        tyList = null;
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            quitSet();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 将设置的围栏信息提交到服务器
     */
    private void postOrder() throws UnsupportedEncodingException {

        if (editText_name.getText().toString().equals("") || editText_name.getText() == null) {
            name = "默认围栏";
        } else {
            name = editText_name.getText().toString();
        }
        time = UtcDateChang.Local2UTC(new Date());
        Log.d("sssss", time + "  名称 ：" + name);
        if (AppCons.ORDERBEN == null){
            AppCons.ORDERBEN = new K100B();
            AppCons.ORDERBEN.setTerminalID(AppCons.locationListBean.getTerminalID());
            Log.e("新增",AppCons.ORDERBEN.toString());
        }
            AppCons.ORDERBEN.setFenceName(name);
            AppCons.ORDERBEN.setFenceAlarmType(waPst);
            AppCons.ORDERBEN.setFenceLat(mLat);
            AppCons.ORDERBEN.setFenceLon(mLon);
            AppCons.ORDERBEN.setFenceState(true);
            AppCons.ORDERBEN.setFenceLimit(dis);
            AppCons.ORDERBEN.setFenceModel(tyPst);
            AppCons.ORDERBEN.setFenceSetTime(time);
            AppCons.ORDERBEN.setDeviceProtocol(AppCons.locationListBean.getLocation().getDeviceProtocol());
        Gson gson = new Gson();
        String obj = gson.toJson(AppCons.ORDERBEN);
        Log.e("obj",obj);
        Log.e("name",name);
        NewHttpUtils.saveCommand(obj, getApplicationContext(), new ResponseCallback() {
            @Override
            public void TaskCallBack(Object object) {
                Log.e("Object", object.toString());
                if ((object.toString().contains("true") || object.toString().contains("200"))){
                    Message message = new Message();
                    message.what = 4;
                    handler.sendMessage(message);

                }
            }

            @Override
            public void FailCallBack(Object object) {
                Log.e("Object", object.toString());
            }
        });

    }
}
