package com.example.maptest.mycartest.UI.SetUi;

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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.maptest.mycartest.Adapter.SpinnerAdapter;
import com.example.maptest.mycartest.Bean.CommandResponse;
import com.example.maptest.mycartest.Bean.SpineBean;
import com.example.maptest.mycartest.New.NewHttpUtils;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.UI.SetUi.service.K100B;
import com.example.maptest.mycartest.UI.SetUi.service.PostCommand;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.BaseActivity;
import com.example.maptest.mycartest.Utils.ButtonUtils;
import com.example.maptest.mycartest.Utils.http.ResponseCallback;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${Author} on 2017/3/23.
 * Use to 断电报警
 */

public class NoPowerActivity extends BaseActivity implements View.OnClickListener {
    private ImageView imageView_quit;
    private Switch aSwitch;
    private LinearLayout linearLayout_show;
    private Spinner spinner;
    private Button button_sure;
    private SpinnerAdapter adapter;
    private List<SpineBean> spList;
    private int pst = 0;
    private boolean check = true;
    private String commId;
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
                        NewHttpUtils.postCommand(new String(new Gson().toJson(command)),NoPowerActivity.this,null);
                    }else {
                        PostCommand command = new PostCommand(AppCons.locationListBean.getTerminalID(),commId,AppCons.loginDataBean.getData().getUsername());
                        NewHttpUtils.postCommand(new String(new Gson().toJson(command)),NoPowerActivity.this,null);
                    }
                    commandResponse = null;
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nopower);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        initView();
//        reQuestCenter();
        initClick();

    }

    /**
     * 初始化点击事件
     */
    private void initClick() {
        imageView_quit.setOnClickListener(this);
        button_sure.setOnClickListener(this);
    }

    /**
     * 初始化控件
     */
    private void initView() {

        imageView_quit = (ImageView) findViewById(R.id.image_quitnowper);
        aSwitch = (Switch) findViewById(R.id.switch_noper);
        linearLayout_show = (LinearLayout) findViewById(R.id.linerlayout_nowper);
        spinner = (Spinner) findViewById(R.id.spinner_nowper);
        button_sure = (Button) findViewById(R.id.button_nowper);
        dates();
        initSpdate();

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
        if(AppCons.ORDERBEN != null){
            aSwitch.setChecked(AppCons.ORDERBEN.getCutElectric());
            spinner.setSelection(AppCons.ORDERBEN.getCutElectricType());
        }

    }

    /**
     * @param v
     * view点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_nowper:        //发送指令

                if (!ButtonUtils.isFastDoubleClick(R.id.button_nowper)) {
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
            case R.id.image_quitnowper:      //退出
                quitSet();
                break;
        }
    }

    /**
     * 报警方式
     */
    private void dates() {
        spList = new ArrayList<>();
        SpineBean bean1 = new SpineBean("平台");
        SpineBean bean2 = new SpineBean("平台 + 短信");
        SpineBean bean3 = new SpineBean("平台 + 短信 + 电话");
        SpineBean bean4 = new SpineBean("平台 + 电话");
        spList.add(bean1);
        spList.add(bean2);
        spList.add(bean3);
        spList.add(bean4);
    }

    /**
     * 报警方式
     */
    private void initSpdate() {
        adapter = new SpinnerAdapter(spList, NoPowerActivity.this);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pst = position;

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    /**
     * @throws IOException
     * @throws InterruptedException
     * 发送指令
     */
    private void sendDate() throws IOException, InterruptedException {

        if (check) {
            Log.d("teeee", pst + "");
            switch (pst) {
                case 0:
                    commId = "POWERALM,ON,0,10,30#";    //断电平台报警
                    break;
                case 1:
                    commId = "POWERALM,ON,1,10,30#";    //断电平台+短信
                    break;
                case 2:
                    commId = "POWERALM,ON,2,10,30#";    //断电平台+短信+电话
                    break;
                case 3:
                    commId = "POWERALM,ON,3,10,30#";    //断电平台+短信+电话
                    break;
            }
        } else {
            commId = "POWERALM,OFF#";                   //关闭断电报警
        }
        objectorder = new JSONObject();
        objectorder.put("terminalID", AppCons.locationListBean.getTerminalID());              //设备的IEMI号
        objectorder.put("deviceProtocol", AppCons.locationListBean.getLocation().getDeviceProtocol());      //设备协议号
        objectorder.put("content", commId);


        NewHttpUtils.sendOrder(objectorder.toJSONString(), NoPowerActivity.this, new ResponseCallback() {
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

        spList.clear();
        spList = null;
        commId = null;
        adapter = null;
        objectorder = null;
        aSwitch = null;
        button_sure = null;
        imageView_quit = null;
        linearLayout_show = null;
        spinner = null;
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

            AppCons.ORDERBEN.setCutElectric(check);
            AppCons.ORDERBEN.setCutElectricType(pst);
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
