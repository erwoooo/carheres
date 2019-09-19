package com.example.maptest.mycartest.UI.SetUi;

import android.app.Activity;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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
import com.example.maptest.mycartest.Utils.CheckActivity;
import com.example.maptest.mycartest.Utils.TcpSocketClient;
import com.example.maptest.mycartest.Utils.http.ResponseCallback;
import com.google.gson.Gson;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ${Author} on 2017/3/23.
 * Use to震动报警
 */

public class ShkActivity extends BaseActivity implements View.OnClickListener {
    private ImageView imageView_quit;
    private Switch aSwitch;
    private LinearLayout linearLayout_show;
    private Spinner spinner;
    private Button button;
    private TcpSocketClient socketClient;
    private SpinnerAdapter adapter;
    private List<SpineBean> spList;
    private int pst;
    private String comId;
    private boolean check = true;
    public RequestQueue queue;
    private org.json.JSONObject objects,objectset;
    private JSONObject object;
    private JSON jsonr;
    private JSONObject objectorder;
    private JSONObject jsono;
    private Activity activity = ShkActivity.this;
    private Map<String,Object>map = new HashMap<>();
    private String jsonOrder;
    CommandResponse commandResponse;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 11:
                    if (commandResponse != null){
                        PostCommand command = new PostCommand(AppCons.locationListBean.getTerminalID(),comId,AppCons.loginDataBean.getData().getUsername(),commandResponse.getContent());
                        NewHttpUtils.postCommand(new String(new Gson().toJson(command)),ShkActivity.this,null);
                    }else {
                        PostCommand command = new PostCommand(AppCons.locationListBean.getTerminalID(),comId,AppCons.loginDataBean.getData().getUsername());
                        NewHttpUtils.postCommand(new String(new Gson().toJson(command)),ShkActivity.this,null);
                    }
                    commandResponse = null;
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shk);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        initView();
//        reQuestCenter();
        initClick();


    }

    @Override
    protected void onResume() {
        super.onResume();
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
        imageView_quit = (ImageView) findViewById(R.id.image_quitshk);
        aSwitch = (Switch) findViewById(R.id.switch_shk);
        linearLayout_show = (LinearLayout) findViewById(R.id.linerlayout_shk);
        spinner = (Spinner) findViewById(R.id.spinner_shk);
        dates();
        initSpdate();
        button = (Button) findViewById(R.id.button_shk);

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
        if (AppCons.ORDERBEN != null){
            aSwitch.setChecked(AppCons.ORDERBEN.getVibration());
            spinner.setSelection(AppCons.ORDERBEN.getVibrationType());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_shk:       //发送指令
                if (!ButtonUtils.isFastDoubleClick(R.id.button_shk)) {
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
            case R.id.image_quitshk:        //退出
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
        adapter = new SpinnerAdapter(spList, ShkActivity.this);
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
            switch (pst) {
                case 0:
                    comId = "SENALM,ON,0#"; //震动平台报警
                    break;
                case 1:
                    comId = "SENALM,ON,1#"; //震动平台+短信
                    break;
                case 2:
                    comId = "SENALM,ON,2#"; //震动平台+短信+电话
                    break;
                case 3:
                    comId = "SENALM,ON,3#"; //震动平台+电话
                    break;
            }
        } else {
            comId = "SENALM,OFF#";
        }

        objectorder = new JSONObject();
        objectorder.put("terminalID", AppCons.locationListBean.getTerminalID());              //设备的IEMI号
        objectorder.put("deviceProtocol", AppCons.locationListBean.getLocation().getDeviceProtocol());      //设备协议号
        objectorder.put("content", comId);



        NewHttpUtils.sendOrder(objectorder.toJSONString(), ShkActivity.this, new ResponseCallback() {
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
        activity = null;
        adapter = null;

        spList.clear();
        comId = null;
        jsono = null;
        objectorder = null;
        aSwitch = null;
        button = null;
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
     * 查询指令
     */
    private void reQuestCenter() {
//        map.clear();
//        map.put("Setting.VibrationAlarmState", 1);     //震动报警开关
//        map.put("Setting.VibrationType", 2);       //震动报警方式
//
//        jsonOrder = JSON.toJSONString(map);
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://app.carhere.net/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        GetCommIdStateService service = retrofit.create(GetCommIdStateService.class);
//        Call<ResponseBody> call = service.getOrder(useBean.getTerminalID(),jsonOrder);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
//                try {
//                    String string = new String(response.body().bytes());
//                    ShkNumBean bean = JSONObject.parseObject(string,ShkNumBean.class);
//                    switch (bean.getData().getSetting().getVibrationAlarmState()){
//                        case 0:
//                            aSwitch.setChecked(false);
//                            break;
//                        case 1:
//                            aSwitch.setChecked(true);
//                            break;
//                    }
//                    spinner.setSelection(bean.getData().getSetting().getVibrationType());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Toast.makeText(getApplicationContext(),t.toString(),Toast.LENGTH_SHORT).show();
//            }
//        });

        queue = Volley.newRequestQueue(getApplicationContext());

        new Thread(new Runnable() {
            @Override
            public void run() {

                object = new JSONObject();
                object.put("Setting.VibrationAlarmState", "1");     //震动报警开关
                object.put("Setting.VibrationType", "2");       //震动报警方式
                jsonr = (JSON) JSONObject.toJSON(object);
                Log.d("ssss", "666" + jsonr);
                String URL = "http://app.carhere.net/appGetCommandInfo?TerminalID=" + AppCons.locationListBean.getTerminalID() + "&Field=" + jsonr;
                JsonObjectRequest objectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, URL, null, new Response.Listener<org.json.JSONObject>() {
                    @Override
                    public void onResponse(org.json.JSONObject jsonObject) {
                        Log.d("response1", jsonObject.toString());

                        try {
                            objects = (org.json.JSONObject) jsonObject.get("Data");
                            Log.d("resss", objects.toString());
                            objectset = (org.json.JSONObject) objects.get("Setting");
                            Log.d("resss", objectset.toString());
                            String  check = String.valueOf(objectset.get("VibrationAlarmState").toString());
                            String type = String.valueOf(objectset.get("VibrationType"));
                            if (activity!= null && CheckActivity.isForeground(activity)){
                                if (check.equals("1") || check.contains("true")) {        //根据查询的指令，显示开关
                                    aSwitch.setChecked(true);
                                } else {
                                    aSwitch.setChecked(false);
                                }
                                spinner.setSelection(Integer.parseInt(type));       //显示报警方式
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (jsonObject != null) {

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("response2", volleyError.toString());
                        Toast.makeText(getApplicationContext(),volleyError.toString(),Toast.LENGTH_SHORT).show();
                    }
                });
                queue.add(objectRequest);


            }
        }).start();

    }

    /**
     * 提交指令
     */
    private void postOrder() {

        if (AppCons.ORDERBEN == null){
            AppCons.ORDERBEN = new K100B();
            AppCons.ORDERBEN.setTerminalID(AppCons.locationListBean.getTerminalID());
            Log.e("新增",AppCons.ORDERBEN.toString());
        }
            AppCons.ORDERBEN.setVibration(check);
            AppCons.ORDERBEN.setVibrationType(pst);
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
