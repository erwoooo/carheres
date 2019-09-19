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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.example.maptest.mycartest.Bean.CommandResponse;
import com.example.maptest.mycartest.New.NewHttpUtils;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.UI.SetUi.service.K100B;
import com.example.maptest.mycartest.UI.SetUi.service.PostCommand;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.BaseActivity;
import com.example.maptest.mycartest.Utils.DateChangeUtil;
import com.example.maptest.mycartest.Utils.TcpSocketClient;
import com.example.maptest.mycartest.Utils.http.AlertWarnService;
import com.example.maptest.mycartest.Utils.http.ResponseCallback;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.maptest.mycartest.Utils.bluetoothmanager.BlueToothManager.handler;

/**
 * Created by ${Author} on 2017/3/23.
 * Use to油电控制报警指令发送
 */

public class RooaOilContralActivity extends BaseActivity implements View.OnClickListener {
    private ImageView imageView_quit;
    private Switch aSwitch;
    private TcpSocketClient socketClient;
    private boolean check;
    private String commId;
    public RequestQueue queue;
    private JSONObject objectorder;
    private JSONObject jsono;
    private JSONObject object;
    private JSON jsonr;
    private org.json.JSONObject objects = null;
    private org.json.JSONObject objectset = null;
    private int type = 13;
    private boolean first = false;
    private Activity activity = RooaOilContralActivity.this;
    private Map<String, Object> map = new HashMap<>();
    private String jsonOrder;
    CommandResponse commandResponse;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 11){
                if (commandResponse != null){
                    PostCommand command = new PostCommand(AppCons.locationListBean.getTerminalID(),commId,AppCons.loginDataBean.getData().getUsername(),commandResponse.getContent());
                    NewHttpUtils.postCommand(new String(new Gson().toJson(command)),RooaOilContralActivity.this,null);
                }else {
                    PostCommand command = new PostCommand(AppCons.locationListBean.getTerminalID(),commId,AppCons.loginDataBean.getData().getUsername());
                    NewHttpUtils.postCommand(new String(new Gson().toJson(command)),RooaOilContralActivity.this,null);
                }
                commandResponse = null;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oil);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        initView();
        reQuestCenter();
        initClick();
    }

    /**
     * 初始化点击事件
     */
    private void initClick() {
        imageView_quit.setOnClickListener(this);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        socketClient = TcpSocketClient.getInstance();
        imageView_quit = (ImageView) findViewById(R.id.image_quitoil);
        aSwitch = (Switch) findViewById(R.id.switch_oil);

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    check = true;
                    first = false;
                    try {
                        sendDate();         //关闭开关，发送指令
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } else {
                    check = false;
                    if (first){         //如果是第一次进，不做任何操作

                    }else {
                        try {
                            sendDate();             //打开开关，发送指令
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        first = false;
                    }
                }


            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_quitoil:        //退出
                quitSet();
                break;
        }
    }

    /**
     * @throws IOException
     * @throws InterruptedException 发送指令
     */
    private void sendDate() throws IOException, InterruptedException {
        if (check) {
            commId = "RELAY,0#";        //开油电Switch,连接油电
        } else {
            commId = "RELAY,1#";        //关油电Switch,断开油电
        }
        objectorder = new JSONObject();
        objectorder.put("terminalID", AppCons.locationListBean.getTerminalID());              //设备的IEMI号
        objectorder.put("deviceProtocol", AppCons.locationListBean.getLocation().getDeviceProtocol());      //设备协议号
        objectorder.put("content", commId);

        NewHttpUtils.sendOrder(objectorder.toJSONString(), RooaOilContralActivity.this, new ResponseCallback() {
            @Override
            public void TaskCallBack(Object object) {
                commandResponse = (CommandResponse) object;
                Toast.makeText(getApplicationContext(), "设置成功,机器下次连接系统时将再次发送该指令!", Toast.LENGTH_SHORT).show();
                postOrder();                //机器回应成功，保存指令到服务器
            }

            @Override
            public void FailCallBack(Object object) {
                Toast.makeText(getApplicationContext(), "设置成功,机器下次连接系统时将再次发送该指令!", Toast.LENGTH_SHORT).show();
                postOrder();                //机器回应成功，保存指令到服务器
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
        commId = null;
        jsonr = null;
        jsono = null;
        object = null;
        objectorder = null;
        objectset = null;
        objects = null;
        aSwitch = null;
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
     * 查询指令
     */
    private void reQuestCenter() {
        if (AppCons.ORDERBEN != null){
            if (AppCons.ORDERBEN.getOffline() != null){
                if (AppCons.ORDERBEN.getOffline().getCutoffState()){
                    aSwitch.setChecked(true);
                }else {
                    first = true;
                    aSwitch.setChecked(false);
                }
            }
        }
//        map.clear();
//        map.put("Setting.CutOffState", 1);
//        jsonOrder = JSON.toJSONString(map);
//            Retrofit retrofit = new Retrofit.Builder()
//                    .baseUrl("http://app.carhere.net/")
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//            GetCommIdStateService service = retrofit.create(GetCommIdStateService.class);
//            Call<ResponseBody>call = service.getOrder(useBean.getTerminalID(),jsonOrder);

//        queue = Volley.newRequestQueue(getApplicationContext());
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                object = new JSONObject();
//                object.put("Setting.CutOffState", "1");     //查询油电状态
//                jsonr = (JSON) JSONObject.toJSON(object);
//                Log.d("ssss", "666" + jsonr);
//                String URL = "http://app.carhere.net/appGetCommandInfo?TerminalID=" + AppCons.locationListBean.getTerminalID() + "&Field=" + jsonr;
//                JsonObjectRequest objectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, URL, null, new Response.Listener<org.json.JSONObject>() {
//                    @Override
//                    public void onResponse(org.json.JSONObject jsonObject) {
//                        Log.d("response1", jsonObject.toString());
//
//                        try {
//                            objects = (org.json.JSONObject) jsonObject.get("Data");
//                            Log.d("resss", objects.toString());
//                            objectset = (org.json.JSONObject) objects.get("Setting");
//                            Log.d("resss", objectset.toString());
//                            String check = String.valueOf(objectset.get("CutOffState").toString());
//                            if (activity!= null && CheckActivity.isForeground(activity)){
//                                if (check.equals("1") || check.contains("false")) {            //根据查询的内容，显示开关状态
//                                    aSwitch.setChecked(true);
//                                } else {
//                                    first = true;
//                                    aSwitch.setChecked(false);
//                                }
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        if (jsonObject != null) {
//
//                        }
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        Toast.makeText(getApplicationContext(), volleyError.toString(), Toast.LENGTH_SHORT).show();
//                        Log.d("response2", volleyError.toString());
//                    }
//                });
//                queue.add(objectRequest);
//
//            }
//        }).start();

    }

    /**
     * 保存指令
     */
    private void postOrder() {
        if (AppCons.ORDERBEN == null){
            AppCons.ORDERBEN = new K100B();
            AppCons.ORDERBEN.setTerminalID(AppCons.locationListBean.getTerminalID());
        }
        if (check) {
            AppCons.ORDERBEN.getOffline().setCutoffState(false);

        } else {
            AppCons.ORDERBEN.getOffline().setCutoffState(true);

        }

        AppCons.ORDERBEN.setDeviceProtocol(AppCons.locationListBean.getLocation().getDeviceProtocol());
        Gson gson = new Gson();
        String obj = gson.toJson(AppCons.ORDERBEN);
        NewHttpUtils.saveCommand(obj, getApplicationContext(), new ResponseCallback() {
            @Override
            public void TaskCallBack(Object object) {
                Log.e("离线断油电",object.toString());
            }

            @Override
            public void FailCallBack(Object object) {
                Log.e("离线断油电失败",object.toString());
            }
        });

    }

    /**
     * 提交失败的断油电
     */
    private void postDefault() throws UnsupportedEncodingException {

        if (AppCons.locationListBean.getLocation().getDeviceProtocol() == (2)) {    //如果是r001
         String time = DateChangeUtil.DateToStrs(new Date()).toString();
            if (check) {
                type = 13;
            } else {
                type = 14;
            }
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://app.carhere.net/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            AlertWarnService service = retrofit.create(AlertWarnService.class);
            Call<ResponseBody>call = service.getOrder(AppCons.locationListBean.getTerminalID(),time,"",type,commId);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try {
                        String string = new String(response.body().bytes());
                        if (string.contains("1") || string.contains("false")){
                            Toast.makeText(getApplicationContext(), "指令保存成功，可在查询指令里查询", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getApplicationContext(), "指令保存失败", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    String URL = "http://app.carhere.net/appInsertWireless?TerminalID=" + useBean.getTerminalID()
//                            + "&SendTime=" + time + "&commandType=" + type + "&OfflineContext=''" + "&commandContext=" + commId;
//                    JsonObjectRequest objectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, URL, null, new Response.Listener<org.json.JSONObject>() {
//                        @Override
//                        public void onResponse(org.json.JSONObject jsonObject) {
//                            Log.d("response3", jsonObject.toString());
//                            String result = jsonObject.toString();
//                            if (result.contains("1")) {
//                                Toast.makeText(getApplicationContext(), "指令保存成功，可在查询指令里查询", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(getApplicationContext(), "指令保存失败", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError volleyError) {
//                            Toast.makeText(getApplicationContext(), volleyError.toString(), Toast.LENGTH_SHORT).show();
//                            Log.d("response4", volleyError.toString());
//                        }
//                    });
//                    queue.add(objectRequest);
////                    OkHttpUtils.get()
////                            .url(AppCons.INSETORDER)
////                            .addParams("TerminalID",useBean.getTerminalID())
////                            .addParams("SendTime",time)
////                            .addParams("commandContext",commId)
////                            .addParams("OfflineContext","")
////                            .addParams("commandType", String.valueOf(type))
////                            .build()
////                            .execute(new StringCallback() {
////                                @Override
////                                public void onError(Request request, Exception e) {
////                                    Toast.makeText(getApplicationContext(),"失败指令保存失败",Toast.LENGTH_SHORT).show();
////                                }
////
////                                @Override
////                                public void onResponse(String response) {
////                                    if (response.contains("0")){
////                                        Toast.makeText(getApplicationContext(),"失败指令保存失败",Toast.LENGTH_SHORT).show();
////                                    }else if (response.contains("1")){
////                                        Toast.makeText(getApplicationContext(),"失败指令保存成功",Toast.LENGTH_SHORT).show();
////                                    }
////                                }
////                            });
//
//                }
//            }).start();
        }

    }

}
