package com.example.maptest.mycartest.UI.SetUi;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
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
import com.example.maptest.mycartest.Bean.CommandResponse;
import com.example.maptest.mycartest.Entity.EightOrderBean;
import com.example.maptest.mycartest.New.NewHttpUtils;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.UI.SetUi.service.PostCommand;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.BaseActivity;
import com.example.maptest.mycartest.Utils.ButtonUtils;
import com.example.maptest.mycartest.Utils.http.ResponseCallback;
import com.google.gson.Gson;

import java.io.IOException;

/**
 * Created by ${Author} on 2019/5/24.
 * Use to
 */

public class EightSpeedActivity extends BaseActivity implements View.OnClickListener{
    private Switch aSwitch_open;
    private LinearLayout linearLayout_dis;
    private SeekBar seekBar_time, seekBar_dis;
    private ImageView imageView_timeless, imageView_timecount, imageView_disless, imageView_discount, imageView_quit;
    private TextView textView_time, textView_dis;
    private Spinner spinner;
    private int dis, times;
    private Button button_srue;
    private boolean check = true;
    private String commId;
    private JSONObject object;
    private org.json.JSONObject objects = null;
    private org.json.JSONObject objectset = null;
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
                        NewHttpUtils.postCommand(new String(new Gson().toJson(command)),EightSpeedActivity.this,null);
                    }else {
                        PostCommand command = new PostCommand(AppCons.locationListBean.getTerminalID(),commId,AppCons.loginDataBean.getData().getUsername());
                        NewHttpUtils.postCommand(new String(new Gson().toJson(command)),EightSpeedActivity.this,null);
                    }
                    commandResponse = null;
                    break;

            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_espeed);
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
        if (AppCons.ETORDER != null){
            aSwitch_open.setChecked(AppCons.ETORDER.isSpeedLimit());
            spinner.setSelection(AppCons.ETORDER.getSpeedLimitType());       //报警方式
            seekBar_time.setProgress(AppCons.ETORDER.getLimitTime());    //时间
            seekBar_dis.setProgress(AppCons.ETORDER.getLimitSpeed());      //距离范围
            textView_dis.setText("范围:" +AppCons.ETORDER.getLimitSpeed() +"km/h");
            textView_time.setText("范围:" + AppCons.ETORDER.getLimitTime() + "s");
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
     * @throws IOException
     * @throws InterruptedException
     * 发送指令，抛出异常
     */
    private void sendDate() throws IOException, InterruptedException {
        if (check) {
            commId = "AT+LIMITSPEED="+ dis+","+times+";";
        } else {
            commId = "AT+LIMITSPEED=0,0;";
        }
        object = new JSONObject();
        object.put("terminalID", AppCons.locationListBean.getTerminalID());              //设备的IEMI号
        object.put("deviceProtocol", AppCons.locationListBean.getLocation().getDeviceProtocol());      //设备协议号
        object.put("content", commId);                    //指令
        NewHttpUtils.sendOrder(object.toJSONString(), EightSpeedActivity.this, new ResponseCallback() {
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

        commId = null;

        objects = null;
        objectset = null;

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
     * 提交指令
     */
    private void postOrder() {
        if (AppCons.ETORDER == null){
            AppCons.ETORDER = new EightOrderBean();
            AppCons.ETORDER.setTerminalID(AppCons.locationListBean.getTerminalID());
            Log.e("新增",AppCons.ETORDER.toString());
        }

        AppCons.ETORDER.setSpeedLimit(check);
        AppCons.ETORDER.setLimitSpeed(dis);
        AppCons.ETORDER.setLimitTime(times);
        AppCons.ETORDER.setDeviceProtocol(AppCons.locationListBean.getLocation().getDeviceProtocol());

        Gson gson = new Gson();
        String obj = gson.toJson(AppCons.ETORDER);
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
