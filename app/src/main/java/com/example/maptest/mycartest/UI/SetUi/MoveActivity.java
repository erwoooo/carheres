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
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
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
 * Use to 位移报警指令
 */

public class MoveActivity extends BaseActivity implements View.OnClickListener {
    private ImageView imageView_quit;
    private Switch aSwitch;
    private SeekBar seekBar_dis;
    private ImageView imageView_less, imageView_count;
    private TextView textView_dis;
    private Button button_sure;
    private LinearLayout linearLayout_show;
    private Spinner spinner;
    private int dis = 0, pst = 0,baseLong = 100;
    private Boolean check = true;
    private SpinnerAdapter adapter;
    private List<SpineBean> spList;
    private String commId = null;
    public RequestQueue queue;
    private JSONObject objectorder;
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
                case 4:
                    spinner.setSelection(pst, true);
                    break;
                case 11:
                    if (commandResponse != null){
                        PostCommand command = new PostCommand(AppCons.locationListBean.getTerminalID(),commId,AppCons.loginDataBean.getData().getUsername(),commandResponse.getContent());
                        NewHttpUtils.postCommand(new String(new Gson().toJson(command)),MoveActivity.this,null);
                    }else {
                        PostCommand command = new PostCommand(AppCons.locationListBean.getTerminalID(),commId,AppCons.loginDataBean.getData().getUsername());
                        NewHttpUtils.postCommand(new String(new Gson().toJson(command)),MoveActivity.this,null);
                    }
                    commandResponse = null;
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        initView();
//        reQuestCenter();
        initClick();
        initDis();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    private void initClick() {
        imageView_count.setOnClickListener(this);
        imageView_less.setOnClickListener(this);
        imageView_quit.setOnClickListener(this);
        button_sure.setOnClickListener(this);
    }

    private void initView() {
        spinner = (Spinner) findViewById(R.id.spinner_move);
        seekBar_dis = (SeekBar) findViewById(R.id.seekbar_move);
        linearLayout_show = (LinearLayout) findViewById(R.id.linerlayout_move);
        imageView_quit = (ImageView) findViewById(R.id.image_quitmove);
        aSwitch = (Switch) findViewById(R.id.switch_move);
        imageView_less = (ImageView) findViewById(R.id.image_moveless);
        imageView_count = (ImageView) findViewById(R.id.image_movecount);
        textView_dis = (TextView) findViewById(R.id.text_movedis);
        button_sure = (Button) findViewById(R.id.button_move);
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
        if (AppCons.ORDERBEN != null){
            aSwitch.setChecked(AppCons.ORDERBEN.getDisplacement());
            spinner.setSelection(AppCons.ORDERBEN.getDisplacementType());
            seekBar_dis.setProgress(AppCons.ORDERBEN.getDisplacementLimit());
            textView_dis.setText("范围:"+AppCons.ORDERBEN.getDisplacementLimit()+" 米" );
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_movecount:
                disCount();
                break;
            case R.id.image_moveless:
                disLess();
                break;
            case R.id.button_move:

                if (!ButtonUtils.isFastDoubleClick(R.id.button_move)) {
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
            case R.id.image_quitmove:
                quitSet();
                break;
        }
    }

    private void sendDate() throws IOException, InterruptedException {
        if (check) {
            Log.d("teeee", pst + "");
            switch (pst) {
                case 0:
                    commId = "MOVING,ON," + dis + ",0#";
                    break;
                case 1:
                    commId = "MOVING,ON," + dis + ",1#";
                    break;
                case 2:
                    commId = "MOVING,ON," + dis + ",2#";
                    break;
                case 3:
                    commId = "MOVING,ON," + dis + ",3#";
                    break;
            }
        } else {
            commId = "MOVING,OFF#";
        }
        objectorder = new JSONObject();
        objectorder.put("terminalID", AppCons.locationListBean.getTerminalID());              //设备的IEMI号
        objectorder.put("deviceProtocol", AppCons.locationListBean.getLocation().getDeviceProtocol());      //设备协议号
        objectorder.put("content", commId);


        NewHttpUtils.sendOrder(objectorder.toJSONString(), MoveActivity.this, new ResponseCallback() {
            @Override
            public void TaskCallBack(Object object) {
                commandResponse = (CommandResponse) object;
                String data = (commandResponse.getContent());
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
        adapter = null;
        spList.clear();
        spList = null;
        objectorder = null;
        handler.removeCallbacksAndMessages(null);
        handler = null;
        aSwitch = null;
        button_sure = null;
        check = false;
        commId = null;
        imageView_count = null;
        imageView_less = null;
        imageView_quit = null;
        linearLayout_show = null;
        seekBar_dis = null;
        spinner = null;
        textView_dis = null;
        finish();


    }

    private void disLess() {
        if (dis > 0) {
            dis--;
        }
        Message message = new Message();
        message.what = 0;
        message.arg1 = dis;
        handler.sendMessage(message);
    }

    private void disCount() {
        if (dis < 1000) {
            dis++;
        }
        Message message = new Message();
        message.what = 1;
        message.arg1 = dis;
        handler.sendMessage(message);
    }

    private void initDis() {
        seekBar_dis.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                dis = progress + baseLong;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView_dis.setText("范围: " + dis + "米");
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

    private void initSpdate() {
        adapter = new SpinnerAdapter(spList, MoveActivity.this);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                pst = position;
                Log.d("teeee", pst + "spinner位置");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            quitSet();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void postOrder() {
        if (AppCons.ORDERBEN == null){
            AppCons.ORDERBEN = new K100B();
            AppCons.ORDERBEN.setTerminalID(AppCons.locationListBean.getTerminalID());
            Log.e("新增",AppCons.ORDERBEN.toString());
        }

            AppCons.ORDERBEN.setDisplacement(check);
            AppCons.ORDERBEN.setDisplacementType(pst);
            AppCons.ORDERBEN.setDisplacementLimit(dis);
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
