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
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
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
import com.example.maptest.mycartest.Utils.http.ResponseCallback;
import com.google.gson.Gson;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${Author} on 2017/3/29.
 * Use to 超速报警显示
 */

public class MoreSpeedActivity extends BaseActivity implements View.OnClickListener {
    private Switch aSwitch_open;
    private LinearLayout linearLayout_dis;
    private SeekBar seekBar_time, seekBar_dis;
    private ImageView imageView_timeless, imageView_timecount, imageView_disless, imageView_discount, imageView_quit;
    private TextView textView_time, textView_dis;
    private Spinner spinner;
    private int dis, times, pst;
    private Button button_srue;
    private SpinnerAdapter adapter;
    private List<SpineBean> spList;
    private boolean check = true;
    private String commId;
    public RequestQueue queue;
    private JSONObject object;
    private JSONObject object1;
    private JSON json;
    private org.json.JSONObject objects = null;
    private org.json.JSONObject objectset = null;
    private Activity activity = MoreSpeedActivity.this;
    CommandResponse commandResponse;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    seekBar_dis.setProgress(msg.arg1);

                    break;
                case 1:
                    seekBar_dis.setProgress(msg.arg1);

                    break;
                case 2:
                    seekBar_time.setProgress(msg.arg1);

                    break;
                case 3:
                    seekBar_time.setProgress(msg.arg1);

                    break;
                case 11:
                    if (commandResponse != null){
                        PostCommand command = new PostCommand(AppCons.locationListBean.getTerminalID(),commId,AppCons.loginDataBean.getData().getUsername(),commandResponse.getContent());
                        NewHttpUtils.postCommand(new String(new Gson().toJson(command)),MoreSpeedActivity.this,null);
                    }else {
                        PostCommand command = new PostCommand(AppCons.locationListBean.getTerminalID(),commId,AppCons.loginDataBean.getData().getUsername());
                        NewHttpUtils.postCommand(new String(new Gson().toJson(command)),MoreSpeedActivity.this,null);
                    }
                    commandResponse = null;
                    break;

            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morespeed);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏

        initView();
//        reQuestCenter();
        initClick();
        selectTime();
        selectDis();


    }

    /**
     * 初始化点击事件
     */
    private void initClick() {
        imageView_discount.setOnClickListener(this);
        imageView_disless.setOnClickListener(this);
        imageView_timeless.setOnClickListener(this);
        imageView_timecount.setOnClickListener(this);
        imageView_quit.setOnClickListener(this);
        button_srue.setOnClickListener(this);
    }

    /**
     * 初始化控件
     */
    private void initView() {

        button_srue = (Button) findViewById(R.id.button_more);
        spinner = (Spinner) findViewById(R.id.spinner_more);
        textView_dis = (TextView) findViewById(R.id.text_disabove);
        textView_time = (TextView) findViewById(R.id.text_above);
        seekBar_dis = (SeekBar) findViewById(R.id.seekbar_disabove);
        seekBar_time = (SeekBar) findViewById(R.id.seekbar_moreabove);
        imageView_timeless = (ImageView) findViewById(R.id.image_moreaboveless);
        imageView_timecount = (ImageView) findViewById(R.id.image_moreabovecount);
        imageView_disless = (ImageView) findViewById(R.id.image_disaboveless);
        imageView_discount = (ImageView) findViewById(R.id.image_disabovecount);
        imageView_quit = (ImageView) findViewById(R.id.image_quitmore);
        aSwitch_open = (Switch) findViewById(R.id.switch_more);
        dates();
        initSpdate();


        linearLayout_dis = (LinearLayout) findViewById(R.id.linerlayout_morespeed);
        aSwitch_open.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    check = true;
                    linearLayout_dis.setVisibility(View.VISIBLE);
                } else {
                    check = false;
                    linearLayout_dis.setVisibility(View.GONE);
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
            aSwitch_open.setChecked(AppCons.ORDERBEN.getSpeedLimit());
            spinner.setSelection(AppCons.ORDERBEN.getSpeedLimitType());       //报警方式
            seekBar_time.setProgress(AppCons.ORDERBEN.getLimitTime());    //时间
            seekBar_dis.setProgress(AppCons.ORDERBEN.getLimitSpeed());      //距离范围
            textView_dis.setText("范围:" +AppCons.ORDERBEN.getLimitSpeed() +"km/h");
            textView_time.setText("范围:" + AppCons.ORDERBEN.getLimitTime() + "s");
        }

    }


    /**
     * @param v
     * view点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_quitmore:
                quitSet();  //退出
                break;
            case R.id.image_moreabovecount:
                timeCount();   //时间加
                break;
            case R.id.image_moreaboveless:
                timeLess();   //时间减
                break;
            case R.id.image_disabovecount:
                disCount();     //距离加
                break;
            case R.id.image_disaboveless:
                disLess();     //距离减
                break;
            case R.id.button_more:      //发送指令
                if (!ButtonUtils.isFastDoubleClick(R.id.button_more)) {
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
     * 距离小于1000，可以加
     */
    private void disCount() {
        if (dis < 1000) {
            dis++;
        }
        Message message = new Message();
        message.what = 0;
        message.arg1 = dis;
        handler.sendMessage(message);
    }

    /**
     * 距离大于0，可以减
     */
    private void disLess() {
        if (dis > 0) {
            dis--;
        }
        Message message = new Message();
        message.what = 1;
        message.arg1 = dis;
        handler.sendMessage(message);

    }

    /**
     * 时间大于0可以减
     */
    private void timeLess() {
        if (times > 0) {
            times--;
        }
        Message message = new Message();
        message.what = 2;
        message.arg1 = times;
        handler.sendMessage(message);
    }

    /**
     * 时间小于100，可以加
     */
    private void timeCount() {
        if (times < 100) {
            times++;
        }
        Message message = new Message();
        message.what = 3;
        message.arg1 = times;
        handler.sendMessage(message);

    }

    /**
     * 时间seekbra
     */
    private void selectTime() {
        seekBar_time.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {
                times = progress;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView_time.setText("范围: " + progress + "s");
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
     * 距离seekbar
     */
    private void selectDis() {
        seekBar_dis.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {
                dis = progress;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView_dis.setText("范围: " + progress + "km/h");
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
     * 下拉框报警方式
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
        adapter = new SpinnerAdapter(spList, MoreSpeedActivity.this);
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
     * 发送指令，抛出异常
     */
    private void sendDate() throws IOException, InterruptedException {
        if (check) {
            Log.d("teeee", pst + "");
            switch (pst) {
                case 0:
                    commId = "SPEED,ON," + times + "," + dis + ",0#";           //超速：开，时间，距离，平台报警
                    break;
                case 1:
                    commId = "SPEED,ON," + times + "," + dis + ",1#";           //超速：开，时间，距离，平台+短信报警
                    break;
                case 2:
                    commId = "SPEED,ON," + times + "," + dis + ",2#";           //超速：开，时间，距离，平台+短信+电话报警
                    break;
                case 3:
                    commId = "SPEED,ON," + times + "," + dis + ",3#";           //超速：开，时间，距离，平台+电话报警
                    break;
            }
        } else {
            commId = "SPEED,OFF#";                          //关闭超速报警
        }
        object = new JSONObject();
        object.put("terminalID", AppCons.locationListBean.getTerminalID());              //设备的IEMI号
        object.put("deviceProtocol", AppCons.locationListBean.getLocation().getDeviceProtocol());      //设备协议号
        object.put("content", commId);                    //指令
        NewHttpUtils.sendOrder(object.toJSONString(), MoreSpeedActivity.this, new ResponseCallback() {
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
        if (queue != null)
            queue = null;
        activity = null;
        commId = null;

        spList.clear();
        spList = null;
        adapter = null;
        objects = null;
        objectset = null;
        object1 = null;
        json = null;
        handler.removeCallbacksAndMessages(null);
        handler = null;
        aSwitch_open = null;
        button_srue = null;
        imageView_discount = null;
        imageView_disless = null;
        imageView_quit = null;
        imageView_timecount = null;
        imageView_timeless = null;
        linearLayout_dis = null;
        object = null;
        seekBar_dis = null;
        seekBar_time = null;
        spinner = null;
        textView_dis = null;
        textView_time = null;
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
     * 获取超速报警指令内容
     */
    private void reQuestCenter() {
//        map.clear();
//        map.put("Setting.SpeedLimitState", "1");            //报警状态
//        map.put("Setting.SpeedLimitType", "2");             //报警方式
//        map.put("Setting.LimitTime", "3");                  //时间
//        map.put("Setting.LimitSpeed", "4");                 //距离
//        jsonOrder = JSON.toJSONString(map);
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
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//            }
//        });
        queue = Volley.newRequestQueue(getApplicationContext());

        new Thread(new Runnable() {
            @Override
            public void run() {
                object1 = new JSONObject();
                object1.put("Setting.SpeedLimitState", "1");            //报警状态
                object1.put("Setting.SpeedLimitType", "2");             //报警方式
                object1.put("Setting.LimitTime", "3");                  //时间
                object1.put("Setting.LimitSpeed", "4");                 //距离
                json = (JSON) JSONObject.toJSON(object1);
                Log.d("ssss", "666" + json);
                String URL = "http://app.carhere.net/appGetCommandInfo?TerminalID=" + AppCons.locationListBean.getTerminalID() + "&Field=" + json;
                JsonObjectRequest objectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, URL, null, new Response.Listener<org.json.JSONObject>() {
                    @Override
                    public void onResponse(org.json.JSONObject jsonObject) {
                        Log.d("response1", jsonObject.toString());      //根据获取的指令，显示
                        try {
                            objects = (org.json.JSONObject) jsonObject.get("Data");
                            Log.d("resss", objects.toString());
                            objectset = (org.json.JSONObject) objects.get("Setting");
                            Log.d("resss", objectset.toString());
                            String check = String.valueOf(objectset.get("SpeedLimitState").toString());
                            String type = String.valueOf(objectset.get("SpeedLimitType"));
                            String time = String.valueOf(objectset.get("LimitTime"));
                            String speed = String.valueOf(objectset.get("LimitSpeed"));
                            if (activity!= null && CheckActivity.isForeground(activity)){
                                if (check.equals("1") || check.contains("true")) {                //显示开关
                                    aSwitch_open.setChecked(true);
                                } else {
                                    aSwitch_open.setChecked(false);
                                }
                                spinner.setSelection(Integer.parseInt(type));       //报警方式
                                seekBar_time.setProgress(Integer.parseInt(time));    //时间
                                seekBar_dis.setProgress(Integer.parseInt(speed));      //距离范围
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

            AppCons.ORDERBEN.setSpeedLimit(check);
            AppCons.ORDERBEN.setSpeedLimitType(pst);
            AppCons.ORDERBEN.setLimitSpeed(dis);
            AppCons.ORDERBEN.setLimitTime(times);
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
