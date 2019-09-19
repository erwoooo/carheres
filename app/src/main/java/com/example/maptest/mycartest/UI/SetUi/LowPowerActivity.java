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
import com.android.volley.RequestQueue;
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
 * Use to 低电报警
 */

public class LowPowerActivity extends BaseActivity implements View.OnClickListener {
    private Switch aSwitch;
    private LinearLayout linearLayout_power;
    private Spinner spinner;
    private Button button_sure;
    private ImageView imageView_qut;
    private SpinnerAdapter adapter;
    private List<SpineBean> spList;
    private int pst;
    private boolean check = true;
    private String commId;
    public RequestQueue queue;

    private JSONObject objectorder;
    CommandResponse commandResponse;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 11){
                if (commandResponse != null){
                    PostCommand command = new PostCommand(AppCons.locationListBean.getTerminalID(),commId,AppCons.loginDataBean.getData().getUsername(),commandResponse.getContent());
                    NewHttpUtils.postCommand(new String(new Gson().toJson(command)),LowPowerActivity.this,null);
                }else {
                    PostCommand command = new PostCommand(AppCons.locationListBean.getTerminalID(),commId,AppCons.loginDataBean.getData().getUsername());
                    NewHttpUtils.postCommand(new String(new Gson().toJson(command)),LowPowerActivity.this,null);
                }
                commandResponse = null;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lowpwer);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        initView();
//        reQuestCenter();
        initClick();

    }

    /**
     * 初始化点击事件
     */
    private void initClick() {
        button_sure.setOnClickListener(this);
        imageView_qut.setOnClickListener(this);
    }

    /**
     * 初始化控件
     */
    private void initView() {

        imageView_qut = (ImageView) findViewById(R.id.image_quitlowper);
        button_sure = (Button) findViewById(R.id.button_lowper);
        spinner = (Spinner) findViewById(R.id.spinner_lowper);
        aSwitch = (Switch) findViewById(R.id.switch_loper);
        linearLayout_power = (LinearLayout) findViewById(R.id.linerlayout_lowper);
        dates();
        initSpdate();

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    check = true;
                    linearLayout_power.setVisibility(View.VISIBLE);
                } else {
                    check = false;
                    linearLayout_power.setVisibility(View.GONE);
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
        if (AppCons.ORDERBEN != null){
            aSwitch.setChecked(AppCons.ORDERBEN.getLowElectric());
            spinner.setSelection(AppCons.ORDERBEN.getLowElectricType());
        }
    }

    /**
     * @param v
     * 每个view的点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_lowper:        //发送指令
                if (!ButtonUtils.isFastDoubleClick(R.id.button_lowper)) {
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
            case R.id.image_quitlowper:     //退出
                quitSet();
                break;
        }
    }


    /**
     * 下拉框数据初始化
     */
    private void dates() {
        spList = new ArrayList<>();
        SpineBean bean1 = new SpineBean("平台");
        SpineBean bean2 = new SpineBean("平台 + 短信");
//        SpineBean bean3 = new SpineBean("平台 + 短信 + 电话");
//        SpineBean bean4 = new SpineBean("平台 + 电话");
        spList.add(bean1);
        spList.add(bean2);
//        spList.add(bean3);
//        spList.add(bean4);

    }

    /**
     * 报警方式
     */
    private void initSpdate() {
        adapter = new SpinnerAdapter(spList, LowPowerActivity.this);
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


    private void sendDate() throws IOException, InterruptedException {

        if (check) {
            Log.d("teeee", pst + "");
            switch (pst) {
                case 0:
                    commId = "BATALM,ON,0#";        //低电平台报警
                    break;
                case 1:
                    commId = "BATALM,ON,1#";        //低电平台+短信报警
                    break;
                case 2:
                    commId = "BATALM,ON,2#";        //低电平台+短信+电话报警
                    break;
                case 3:
                    commId = "BATALM,ON,3#";        //低电平台+电话报警
                    break;
            }
        } else {
            commId = "BATALM,OFF#";                 //关闭低电报警
        }
        objectorder = new JSONObject();
        objectorder.put("terminalID", AppCons.locationListBean.getTerminalID());              //设备的IEMI号
        objectorder.put("deviceProtocol", AppCons.locationListBean.getLocation().getDeviceProtocol());      //设备协议号
        objectorder.put("content", commId);                 //指令

        NewHttpUtils.sendOrder(objectorder.toJSONString(), LowPowerActivity.this, new ResponseCallback() {
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
                Toast.makeText(getApplicationContext(), "设置失败", Toast.LENGTH_SHORT).show();
            }
        });
        Message message = new Message();
        message.what = 11;
        handler.sendMessage(message);
    }

    /**
     * 退出，清空数据
     */
    private void quitSet() {

        if (queue != null)
            queue = null;
        commId = null;
        objectorder = null;
        aSwitch = null;
        adapter = null;
        button_sure = null;
        imageView_qut = null;
        linearLayout_power = null;
        spList.clear();
        spList = null;
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
     * 低电报警设置发送到服务器
     */
    private void postOrder() {
        if (AppCons.ORDERBEN == null){
            AppCons.ORDERBEN = new K100B();
            AppCons.ORDERBEN.setTerminalID(AppCons.locationListBean.getTerminalID());
            Log.e("新增",AppCons.ORDERBEN.toString());
            }

            AppCons.ORDERBEN.setLowElectric(check);
            AppCons.ORDERBEN.setLowElectricType(pst);
            AppCons.ORDERBEN.setDeviceProtocol(AppCons.locationListBean.getLocation().getDeviceProtocol());

        Gson gson = new Gson();
        String obj = gson.toJson(AppCons.ORDERBEN);
        NewHttpUtils.saveCommand(obj, getApplicationContext(), new ResponseCallback() {
            @Override
            public void TaskCallBack(Object object) {
                Log.e("Object",object.toString());
                if ((object.toString().contains("true") || object.toString().contains("200")) && AppCons.ORDERBEN != null){


                }
            }

            @Override
            public void FailCallBack(Object object) {
                Log.e("Object",object.toString());
            }
        });

    }
}
