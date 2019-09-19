package com.example.maptest.mycartest.UI.SetUi;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.maptest.mycartest.Adapter.SpinnergwAdapter;
import com.example.maptest.mycartest.Bean.CommandResponse;
import com.example.maptest.mycartest.Bean.SpineBean;
import com.example.maptest.mycartest.New.NewHttpUtils;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.SwipRecycleView.App;
import com.example.maptest.mycartest.UI.SetUi.service.K100B;
import com.example.maptest.mycartest.UI.SetUi.service.PostCommand;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.BaseActivity;
import com.example.maptest.mycartest.Utils.http.ResponseCallback;
import com.google.gson.Gson;


import java.lang.reflect.Field;
import java.util.ArrayList;

import java.util.List;

import static com.example.maptest.mycartest.R.id.view;
import static com.example.maptest.mycartest.UI.SetUi.RooaLixianActivity.dip2px;

/**
 * Created by ${Author} on 2017/11/22.
 * Use to
 */

public class WorkmodeActivity extends BaseActivity implements View.OnClickListener,NumberPicker.OnScrollListener,NumberPicker.OnValueChangeListener{
    private ImageView imageView_quit;
    private LinearLayout linearLayout_set,linearLayout_week,linner_gw_sleep;
    private Spinner spinner;
    private List<SpineBean>list;
    private SpinnergwAdapter adapter;
    String time;
    private Button button_send_gw;
    private EditText gw_edit_settime;
    private TextView text_sleep_hour1,text_sleep_min1;
    private RelativeLayout gw_relative_sleep;
    CommandResponse commandResponse;
    private int indexType = 0;
    private TextView textView_cancel,textView_do;
    private NumberPicker numberPicker_hour,numberPicker_min;
    private int hour,min;
    private String commId;
    private PopupWindow window;
    private  View view;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            PostCommand command = new PostCommand(AppCons.locationListBean.getTerminalID(),commId,AppCons.loginDataBean.getData().getUsername(),commandResponse.getContent());
            NewHttpUtils.postCommand(new String(new Gson().toJson(command)),WorkmodeActivity.this,null);
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workmode);
        initView();
        initClick();
    }

    private void initClick() {
        button_send_gw.setOnClickListener(this);
        imageView_quit.setOnClickListener(this);
        gw_relative_sleep.setOnClickListener(this);
    }
    private void initView() {
        text_sleep_hour1 = (TextView) findViewById(R.id.text_sleep_hour1);
        text_sleep_min1 = (TextView) findViewById(R.id.text_sleep_min1);
        gw_edit_settime = (EditText) findViewById(R.id.gw_edit_settime);
        gw_relative_sleep = (RelativeLayout) findViewById(R.id.gw_relative_sleep);
        button_send_gw = (Button) findViewById(R.id.button_send_gw);
        list = new ArrayList<>();
        imageView_quit = (ImageView) findViewById(R.id.image_quitgw);
        linearLayout_set = (LinearLayout) findViewById(R.id.linner_gw_order1);
        linearLayout_week = (LinearLayout) findViewById(R.id.linerlayout_gw_week);
        linner_gw_sleep = (LinearLayout) findViewById(R.id.linner_gw_sleep);
        spinner = (Spinner) findViewById(R.id.spinner_gw);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.gw_windowinfo, null);
        initDate();
        initSpiner();
        if (AppCons.ORDERBEN != null){
            spinner.setSelection(AppCons.ORDERBEN.getWorkPattern());
            if (AppCons.ORDERBEN.getWorkPattern() == 1){
                gw_edit_settime.setText(AppCons.ORDERBEN.getIntervalTime() + "");
                time = gw_edit_settime.getText().toString();
            }else if (AppCons.ORDERBEN.getWorkPattern() == 2){
                try {
                    String sleep = AppCons.ORDERBEN.getIntervalTime() + "";
                    if (sleep.length() == 4){
                        text_sleep_hour1.setText(sleep.substring(0,sleep.length() - 2));
                    }else {
                        text_sleep_hour1.setText("0" + sleep.substring(0,sleep.length() - 2));
                    }
                    text_sleep_min1.setText(sleep.substring(sleep.length() - 2));
                }catch (Exception e){
                    e.printStackTrace();
                }

            }


        }
    }


    private void initSpiner() {
        adapter = new SpinnergwAdapter(list,WorkmodeActivity.this);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        weekModle();
                        indexType = 0;
                        break;
                    case 1:
                        setTimeModle();
                        indexType = 1;
                        break;
                    case 2:
                        setSleepMode();
                        indexType = 2;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    private void setTimeModle() {
        linner_gw_sleep.setVisibility(View.GONE);
        linearLayout_set.setVisibility(View.VISIBLE);
        linearLayout_week.setVisibility(View.GONE);
    }
    private void setSleepMode(){
        linearLayout_set.setVisibility(View.GONE);
        linearLayout_week.setVisibility(View.GONE);
        linner_gw_sleep.setVisibility(View.VISIBLE);
    }
    private void weekModle() {
        linner_gw_sleep.setVisibility(View.GONE);
        linearLayout_set.setVisibility(View.GONE);
        linearLayout_week.setVisibility(View.VISIBLE);

    }
    private void initDate() {
        SpineBean bean1 = new SpineBean("智能模式");
        SpineBean bean2 = new SpineBean("间隔模式");
        SpineBean bean3 = new SpineBean("休眠模式");
        list.add(bean1);
        list.add(bean2);
        list.add(bean3);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_quitgw:
                finish();
                break;

            case R.id.button_send_gw:
                senUdpOrder(sendWorkMode());
                break;
            case R.id.gw_relative_sleep:
                showPopwindow(gw_relative_sleep,text_sleep_hour1,text_sleep_min1);
                break;
        }
    }

    private String sendWorkMode() {
        switch (indexType){
            case 0:
                commId = "SWMODE,0,0#";
                break;
            case 1:     //定时模式
                if (gw_edit_settime.getText().toString().isEmpty()){
                    Toast.makeText(WorkmodeActivity.this,"请选择时间",Toast.LENGTH_SHORT).show();
                }else {
                     time = gw_edit_settime.getText().toString();
                    commId = "SWMODE,1,"+ time + "#";
                }
                break;
            case 2:
                commId = "SWMODE,2,"+ text_sleep_hour1.getText() + text_sleep_min1.getText() + "#";
                break;
        }
        return commId;
    }


    @Override
    public void onScrollStateChange(NumberPicker view, int scrollState) {

    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        switch (picker.getId()){
            case R.id.number_pickhour:
                hour = newVal;
                break;
            case R.id.number_pickmin:
                min = newVal;
                break;
        }
    }

    /**
     * @param commd
     * UDP 发送指令
     */
    private void senUdpOrder(String commd){
        Log.e("commd",commd);
        JSONObject object = new JSONObject();
        object.put("terminalID", AppCons.locationListBean.getTerminalID());              //设备的IEMI号
        object.put("deviceProtocol", AppCons.locationListBean.getLocation().getDeviceProtocol());      //设备协议号
        object.put("content", commd);                 //指令

        NewHttpUtils.sendOrder(object.toJSONString(), WorkmodeActivity.this, new ResponseCallback() {
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
                    handler.sendEmptyMessage(0);
                }else {
                    Toast.makeText(getApplicationContext(), "设置失败", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void FailCallBack(Object object) {
                Toast.makeText(getApplicationContext(), "设置成功,机器下次连接系统时将再次发送该指令!", Toast.LENGTH_SHORT).show();
             //机器回应成功，保存指令到服务器
            }
        });

        if (commandResponse != null){
            PostCommand command = new PostCommand(AppCons.locationListBean.getTerminalID(),commd,AppCons.loginDataBean.getData().getUsername(),commandResponse.getContent());
            NewHttpUtils.postCommand(new String(new Gson().toJson(command)),WorkmodeActivity.this,null);
        }else {
            PostCommand command = new PostCommand(AppCons.locationListBean.getTerminalID(),commd,AppCons.loginDataBean.getData().getUsername());
            NewHttpUtils.postCommand(new String(new Gson().toJson(command)),WorkmodeActivity.this,null);
        }
        commandResponse = null;
    }


    private void postOrder() {
        if (AppCons.ORDERBEN == null){
            AppCons.ORDERBEN = new K100B();
            AppCons.ORDERBEN.setTerminalID(AppCons.locationListBean.getTerminalID());
        }
        AppCons.ORDERBEN.setWorkPattern(indexType);
        if (indexType == 2){
            AppCons.ORDERBEN.setIntervalTime(Integer.parseInt(hour + "" + min));
        }else if (indexType == 1){
            AppCons.ORDERBEN.setIntervalTime(Integer.parseInt(time));
        }
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

    private void showPopwindow(RelativeLayout relativeLayout, final TextView textView_hour, final TextView textView_min) {
        if (window == null){
            window = new PopupWindow(view, dip2px(this,150),
                    dip2px(this,260));
            window.setFocusable(false);
            ColorDrawable dw = new ColorDrawable(0x808080);
            window.setBackgroundDrawable(dw);
            textView_cancel = (TextView) view.findViewById(R.id.gw_text_cancel);
            textView_do = (TextView) view.findViewById(R.id.gw_text_do);
            numberPicker_hour = (NumberPicker) view.findViewById(R.id.number_pickhour);
            numberPicker_min = (NumberPicker) view.findViewById(R.id.number_pickmin);
            setNumberPickerDividerColor(numberPicker_hour);
            setNumberPickerDividerColor(numberPicker_min);
            numberPicker_hour.setMinValue(0);
            numberPicker_hour.setMaxValue(23);
            numberPicker_min.setMaxValue(59);
            numberPicker_min.setMinValue(0);
            numberPicker_hour.setOnValueChangedListener(this);
            numberPicker_hour.setOnScrollListener(this);
            numberPicker_min.setOnScrollListener(this);
            numberPicker_min.setOnValueChangedListener(this);
        }
        int[] location = new int[2];
        relativeLayout.getLocationOnScreen(location);
        window.showAtLocation(relativeLayout, Gravity.NO_GRAVITY, location[0]+relativeLayout.getWidth(), location[1] + relativeLayout.getHeight());
        textView_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
        textView_do.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (hour <10){
                            textView_hour.setText("0" + hour);
                        }else {
                            textView_hour.setText("" + hour);
                        }
                        if (min < 10){
                            textView_min.setText("0"+ min);
                        }else {
                            textView_min.setText(min + "");
                        }

                    }
                });

                window.dismiss();
            }
        });
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                System.out.print("消失");
            }
        });
    }
    private void setNumberPickerDividerColor(NumberPicker numberPicker) {
        NumberPicker picker = numberPicker;
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    //设置分割线的颜色值 透明
                    pf.set(picker, new ColorDrawable(this.getResources().getColor(R.color.bg_bar)));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}
