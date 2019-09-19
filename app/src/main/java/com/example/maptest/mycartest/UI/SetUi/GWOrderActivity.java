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
import com.example.maptest.mycartest.UI.SetUi.service.PostCommand;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.BaseActivity;
import com.example.maptest.mycartest.Utils.UdpClientConnector;
import com.example.maptest.mycartest.Utils.http.ResponseCallback;
import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.maptest.mycartest.UI.SetUi.RooaLixianActivity.dip2px;
import static com.example.maptest.mycartest.Utils.bluetoothmanager.BlueToothManager.handler;

/**
 * Created by ${Author} on 2017/11/22.
 * Use to
 */

public class GWOrderActivity extends BaseActivity implements View.OnClickListener,NumberPicker.OnScrollListener,NumberPicker.OnValueChangeListener{
    private ImageView imageView_quit;
    private LinearLayout linearLayout_set,linearLayout_clock,linearLayout_week;
    private Spinner spinner;
    private List<SpineBean>list;
    private SpinnergwAdapter adapter;
    private RelativeLayout relative_clock_time1,relative_clock_time2,relative_clock_time3,relative_clock_time4,relative_week_time;
    private  View view;
    private PopupWindow window;
    private TextView textView_cancel,textView_do;
    private NumberPicker numberPicker_hour,numberPicker_min;
    private int hour,min;
    private TextView text_week_hour1,text_week_hour2,text_week_hour3,text_week_hour4,text_week_hour;
    private TextView text_week_min1,text_week_min2,text_week_min3,text_week_min4,text_week_min;
    private Button button_send_gw;
    private UdpClientConnector udpClientConnector;
    private String commd;
    private StringBuilder sb = new StringBuilder(":");
    private JSONObject objectorder;
    private int indexType = 0;
    private EditText gw_edit_settime;
    private CheckBox checkbox_gw_clock1,checkbox_gw_clock2,checkbox_gw_clock3,checkbox_gw_clock4;
    private CheckBox checkbox_gw_week1,checkbox_gw_week2,checkbox_gw_week3,checkbox_gw_week4,checkbox_gw_week5,checkbox_gw_week6,checkbox_gw_week7;
    private String clock1 = null,clock2 = null,clock3 = null,clock4 = null;
    private String week1 = "",week2 = "",week3 = "",week4 = "",week5 = "",week6 = "",week7 = "";
    private Map<Object,Object>map = new HashMap<>();
    private Map<Object,Object>mapBsj = new HashMap<>();
    private String jsonOrder,jsonBsjOrder;
    CommandResponse commandResponse;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 11){
                if (commandResponse != null){
                    PostCommand command = new PostCommand(AppCons.locationListBean.getTerminalID(),commd,AppCons.loginDataBean.getData().getUsername(),commandResponse.getContent());
                    NewHttpUtils.postCommand(new String(new Gson().toJson(command)),GWOrderActivity.this,null);
                }else {
                    PostCommand command = new PostCommand(AppCons.locationListBean.getTerminalID(),commd,AppCons.loginDataBean.getData().getUsername());
                    NewHttpUtils.postCommand(new String(new Gson().toJson(command)),GWOrderActivity.this,null);
                }
                commandResponse = null;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gw);
        initView();
        initClick();
    }

    private void initClick() {
        relative_clock_time1.setOnClickListener(this);
        relative_clock_time2.setOnClickListener(this);
        relative_clock_time3.setOnClickListener(this);
        relative_clock_time4.setOnClickListener(this);
        relative_week_time.setOnClickListener(this);
        button_send_gw.setOnClickListener(this);
        imageView_quit.setOnClickListener(this);
    }


    private void initView() {
        gw_edit_settime = (EditText) findViewById(R.id.gw_edit_settime);
        udpClientConnector = UdpClientConnector.getInstance();
        button_send_gw = (Button) findViewById(R.id.button_send_gw);
        list = new ArrayList<>();
        imageView_quit = (ImageView) findViewById(R.id.image_quitgw);
        linearLayout_set = (LinearLayout) findViewById(R.id.linner_gw_order1);
        linearLayout_clock = (LinearLayout) findViewById(R.id.linerlayout_gw_clock);
        linearLayout_week = (LinearLayout) findViewById(R.id.linerlayout_gw_week);
        relative_clock_time1 = (RelativeLayout) findViewById(R.id.relative_clock_time1);
        relative_clock_time2 = (RelativeLayout) findViewById(R.id.relative_clock_time2);
        relative_clock_time3 = (RelativeLayout) findViewById(R.id.relative_clock_time3);
        relative_clock_time4 = (RelativeLayout) findViewById(R.id.relative_clock_time4);
        relative_week_time = (RelativeLayout) findViewById(R.id.relative_week_time);
        text_week_hour1 = (TextView) findViewById(R.id.text_week_hour1);
        text_week_hour2 = (TextView) findViewById(R.id.text_week_hour2);
        text_week_hour3 = (TextView) findViewById(R.id.text_week_hour3);
        text_week_hour4 = (TextView) findViewById(R.id.text_week_hour4);
        text_week_hour = (TextView) findViewById(R.id.text_week_hour);
        text_week_min = (TextView) findViewById(R.id.text_week_min);
        text_week_min1 = (TextView) findViewById(R.id.text_week_min1);
        text_week_min2 = (TextView) findViewById(R.id.text_week_min2);
        text_week_min3 = (TextView) findViewById(R.id.text_week_min3);
        text_week_min4 = (TextView) findViewById(R.id.text_week_min4);
        checkbox_gw_clock1 = (CheckBox) findViewById(R.id.checkbox_gw_clock1);
        checkbox_gw_clock2 = (CheckBox) findViewById(R.id.checkbox_gw_clock2);
        checkbox_gw_clock3 = (CheckBox) findViewById(R.id.checkbox_gw_clock3);
        checkbox_gw_clock4 = (CheckBox) findViewById(R.id.checkbox_gw_clock4);
        checkbox_gw_week1 = (CheckBox) findViewById(R.id.checkbox_gw_week1);
        checkbox_gw_week2 = (CheckBox) findViewById(R.id.checkbox_gw_week2);
        checkbox_gw_week3 = (CheckBox) findViewById(R.id.checkbox_gw_week3);
        checkbox_gw_week4 = (CheckBox) findViewById(R.id.checkbox_gw_week4);
        checkbox_gw_week5 = (CheckBox) findViewById(R.id.checkbox_gw_week5);
        checkbox_gw_week6 = (CheckBox) findViewById(R.id.checkbox_gw_week6);
        checkbox_gw_week7 = (CheckBox) findViewById(R.id.checkbox_gw_week7);
        spinner = (Spinner) findViewById(R.id.spinner_gw);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.gw_windowinfo, null);
        initDate();
        initSpiner();
        initGwUi();
    }

    private void initGwUi() {
        if (AppCons.GWORDER != null){
            if (AppCons.GWORDER.getTimingState()){  //定时模式
                spinner.setSelection(0);
                setTimeModle();
                gw_edit_settime.setText(AppCons.GWORDER.getTimingValue() + "");
            }else if (AppCons.GWORDER.getClockState()){     //闹钟模式
                spinner.setSelection(1);
                clockModle();
                gw_edit_settime.setText(AppCons.GWORDER.getTimingValue() +"");
            }else{                                          //星期模式
                spinner.setSelection(2);
                weekModle();
            }
            gw_edit_settime.setText(AppCons.GWORDER.getTimingValue() + "");
            initWeekAndTime();
        }else {
            spinner.setSelection(0);
            setTimeModle();
        }
    }

    private void initSpiner() {
        adapter = new SpinnergwAdapter(list,GWOrderActivity.this);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        setTimeModle();
                        indexType = 0;
                        break;
                    case 1:
                        clockModle();
                        indexType = 1;
                        break;
                    case 2:
                        weekModle();
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
        linearLayout_set.setVisibility(View.VISIBLE);
        linearLayout_week.setVisibility(View.GONE);
        linearLayout_clock.setVisibility(View.GONE);
    }

    private void clockModle() {
        linearLayout_set.setVisibility(View.GONE);
        linearLayout_week.setVisibility(View.GONE);
        linearLayout_clock.setVisibility(View.VISIBLE);
    }
    private void weekModle() {
        linearLayout_set.setVisibility(View.GONE);
        linearLayout_week.setVisibility(View.VISIBLE);
        linearLayout_clock.setVisibility(View.GONE);
    }
    private void initDate() {
        SpineBean bean1 = new SpineBean("定时模式");
        SpineBean bean2 = new SpineBean("闹钟模式");
        SpineBean bean3 = new SpineBean("星期模式");
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
            case R.id.relative_clock_time1:
                showPopwindow(relative_clock_time1,text_week_hour1,text_week_min1);
                break;
            case R.id.relative_clock_time2:
                showPopwindow(relative_clock_time2,text_week_hour2,text_week_min2);
                break;
            case R.id.relative_clock_time3:
                showPopwindow(relative_clock_time3,text_week_hour3,text_week_min3);
                break;
            case R.id.relative_clock_time4:
                showPopwindow(relative_clock_time4,text_week_hour4,text_week_min4);
                break;
            case R.id.relative_week_time:
                showPopwindow(relative_week_time,text_week_hour,text_week_min);
                break;
            case R.id.button_send_gw:
                    switch (indexType){
                        case 0:
                            if (gw_edit_settime.getText().toString().isEmpty()){
                                Toast.makeText(getApplicationContext(),"时间不能为空",Toast.LENGTH_SHORT).show();
                            }else {
                                senUdpOrder(createOrder());
                            }
                            break;
                        case 1:
                            if (!checkbox_gw_clock1.isChecked() && !checkbox_gw_clock2.isChecked() && !checkbox_gw_clock3.isChecked() && !checkbox_gw_clock4.isChecked()){
                                Toast.makeText(getApplicationContext(),"参数不能为空",Toast.LENGTH_SHORT).show();
                            }else {
                                senUdpOrder(createOrder());

                            }
                            break;
                        case 2:
                            if (!checkbox_gw_week1.isChecked() && !checkbox_gw_week2.isChecked() && !checkbox_gw_week3.isChecked() && !checkbox_gw_week4.isChecked()
                                    && !checkbox_gw_week5.isChecked() && !checkbox_gw_week6.isChecked() && !checkbox_gw_week7.isChecked()){
                                Toast.makeText(getApplicationContext(),"参数不能为空",Toast.LENGTH_SHORT).show();
                            }else {
                                senUdpOrder(createOrder());
                            }
                            break;
                    }
                break;
        }
    }


    /**
     * @param relativeLayout
     * @param textView_hour
     * @param textView_min
     * 选择时间
     */
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
     * @param commdsend
     * UDP 发送指令
     */
    private void senUdpOrder(String commdsend){
        NewHttpUtils.sendOrder(commdsend, GWOrderActivity.this, new ResponseCallback() {
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

    /**
     * @param numberPicker
     * 修改数字选择器样式
     */
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

    /**
     * 创建指令
     * @return 指令内容
     */
    private String createOrder(){
            commd = null;
//        //三种模式
        switch (indexType){
            case 0:                         //定时模式
                commd = "<YG*D:"+gw_edit_settime.getText() +">";
                clockTime();
                weekTime();
                break;
            case 1:                         //闹钟模式
                clockTime();
                weekTime();
                if (clock1 != null){
                    sb.append(clock1+",");
                }
                if (clock2 != null){
                    sb.append( clock2 + "," );
                }
                if (clock3 != null){
                    sb.append( clock3 + "," );
                }
                if (clock4 != null){
                    sb.append( clock4);
                }
                commd = "<YG*R:"+sb.toString()+">";
                sb.delete(0,sb.length());
//                sb.append(":");
                break;
            case 2:                         //星期模式
                clockTime();
                weekTime();
                commd = "<YG*W:1,"+week1+week2+week3+week4+week5+week6+week7+","+text_week_hour.getText()+text_week_min.getText()+">";
                break;
        }
        objectorder = new JSONObject();
        objectorder.put("terminalID", AppCons.locationListBean.getTerminalID());              //设备的IEMI号
        objectorder.put("deviceProtocol", AppCons.locationListBean.getLocation().getDeviceProtocol());      //设备协议号
        objectorder.put("content", commd);                   //指令
        return objectorder.toJSONString();
    }

    /**
     * 闹钟模式
     */
    private void clockTime(){
        if (checkbox_gw_clock1.isChecked()){
            clock1 = text_week_hour1.getText().toString() + text_week_min1.getText().toString();
        }else {
            clock1 = null;
        }
        if (checkbox_gw_clock2.isChecked()){
            clock2 = text_week_hour2.getText().toString() + text_week_min2.getText().toString();
        }else {
            clock2 = null;
        }
        if (checkbox_gw_clock3.isChecked()){
            clock3 = text_week_hour3.getText().toString() + text_week_min3.getText().toString();
        }else {
            clock3 = null;
        }
        if (checkbox_gw_clock4.isChecked()){
            clock4 = text_week_hour4.getText().toString() + text_week_min4.getText().toString();
        }else {
            clock4 = null;
        }
    }

    /**
     * 星期模式
     */
    private void weekTime(){
        if (checkbox_gw_week1.isChecked()){
            week1 = "1";
        }else {
            week1 = "";
        }
        if (checkbox_gw_week2.isChecked()){
            week2 = "2";
        }else {
            week2 = "";
        }
        if (checkbox_gw_week3.isChecked()){
            week3 = "3";
        }else {
            week3 = "";
        }
        if (checkbox_gw_week4.isChecked()){
            week4 = "4";
        }else {
            week4 = "";
        }
        if (checkbox_gw_week5.isChecked()){
            week5 = "5";
        }else {
            week5 = "";
        }
        if (checkbox_gw_week6.isChecked()){
            week6 = "6";
        }else {
            week6 = "";
        }
        if (checkbox_gw_week7.isChecked()){
            week7 = "7";
        }else {
            week7 = "";
        }
    }

    /**
     * 指令保存
     */
    private void postOrder(){
        if (AppCons.GWORDER != null){
            AppCons.GWORDER.setDeviceProtocol(AppCons.locationListBean.getLocation().getDeviceProtocol());
            switch (indexType){
                case 0:         //定时模式
                        AppCons.GWORDER.setTimingState(true);
                        AppCons.GWORDER.setClockState(false);
                        AppCons.GWORDER.setTimingValue(gw_edit_settime.getText().toString().isEmpty()?0:Integer.parseInt(gw_edit_settime.getText().toString()));
                    break;
                case 1:         //闹钟模式
                        AppCons.GWORDER.setTimingState(false);
                        AppCons.GWORDER.setClockState(true);
                        AppCons.GWORDER.setGroupState(new String[]{clock1 == null?"":new StringBuilder(clock1).insert(2,":").toString(),
                                clock2 == null?"":new StringBuilder(clock2).insert(2,":").toString(),
                                clock3 == null?"":new StringBuilder(clock3).insert(2,":").toString(),
                                clock4 == null?"":new StringBuilder(clock4).insert(2,":").toString()});
                    break;
                case 2:         //星期模式
                        AppCons.GWORDER.setTimingState(false);
                        AppCons.GWORDER.setClockState(false);
                        AppCons.GWORDER.setWeekState(new Boolean[]{checkbox_gw_week1.isChecked(),checkbox_gw_week2.isChecked(),
                                checkbox_gw_week3.isChecked(),checkbox_gw_week4.isChecked(),checkbox_gw_week5.isChecked(),
                                checkbox_gw_week6.isChecked(),checkbox_gw_week7.isChecked()});
                    String weekvalue = text_week_hour.getText().toString() + ":"+text_week_min.getText().toString();
                    Log.e("weekvalue",weekvalue );
                        AppCons.GWORDER.setWeekValue(weekvalue + "");
                    break;
            }
            Gson gson = new Gson();
            String obj = gson.toJson(AppCons.GWORDER);
            Log.e("保存指令",obj);
            NewHttpUtils.saveCommand(obj, GWOrderActivity.this, new ResponseCallback() {
                @Override
                public void TaskCallBack(Object object) {

                }

                @Override
                public void FailCallBack(Object object) {

                }
            });
        }



    }
    private void initWeekAndTime(){
        if (AppCons.GWORDER != null){
            //星期模式
            checkbox_gw_week1.setChecked(AppCons.GWORDER.getWeekState()[0]);
            checkbox_gw_week2.setChecked(AppCons.GWORDER.getWeekState()[1]);
            checkbox_gw_week3.setChecked(AppCons.GWORDER.getWeekState()[2]);
            checkbox_gw_week4.setChecked(AppCons.GWORDER.getWeekState()[3]);
            checkbox_gw_week5.setChecked(AppCons.GWORDER.getWeekState()[4]);
            checkbox_gw_week6.setChecked(AppCons.GWORDER.getWeekState()[5]);
            checkbox_gw_week7.setChecked(AppCons.GWORDER.getWeekState()[6]);
            if (AppCons.GWORDER.getWeekValue() != null && AppCons.GWORDER.getWeekValue().length() >= 3){
                Log.e("WEEK",AppCons.GWORDER.getWeekValue());
                try {
                    String cl11 = AppCons.GWORDER.getWeekValue().substring(0,2);
                    String cl12 = AppCons.GWORDER.getWeekValue().substring(3);
                    text_week_hour.setText(cl11);
                    text_week_min.setText(cl12);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
            //闹钟模式
            try{
                checkbox_gw_clock1.setChecked(!AppCons.GWORDER.getGroupState()[0].isEmpty());
                checkbox_gw_clock2.setChecked(!AppCons.GWORDER.getGroupState()[1].isEmpty());
                checkbox_gw_clock3.setChecked(!AppCons.GWORDER.getGroupState()[2].isEmpty());
                checkbox_gw_clock4.setChecked(!AppCons.GWORDER.getGroupState()[3].isEmpty());
                text_week_hour1.setText(AppCons.GWORDER.getGroupState()[0].isEmpty()?"":AppCons.GWORDER.getGroupState()[0].substring(0,2));
                text_week_min1.setText(AppCons.GWORDER.getGroupState()[0].isEmpty()?"":AppCons.GWORDER.getGroupState()[0].substring(3));
                text_week_hour2.setText(AppCons.GWORDER.getGroupState()[1].isEmpty()?"":AppCons.GWORDER.getGroupState()[1].substring(0,2));
                text_week_min2.setText(AppCons.GWORDER.getGroupState()[1].isEmpty()?"":AppCons.GWORDER.getGroupState()[1].substring(3));
                text_week_hour3.setText(AppCons.GWORDER.getGroupState()[2].isEmpty()?"":AppCons.GWORDER.getGroupState()[2].substring(0,2));
                text_week_min3.setText(AppCons.GWORDER.getGroupState()[2].isEmpty()?"":AppCons.GWORDER.getGroupState()[2].substring(3));
                text_week_hour4.setText(AppCons.GWORDER.getGroupState()[3].isEmpty()?"":AppCons.GWORDER.getGroupState()[3].substring(0,2));
                text_week_min4.setText(AppCons.GWORDER.getGroupState()[3].isEmpty()?"":AppCons.GWORDER.getGroupState()[3].substring(3));

            }catch (Exception e){

            }

        }
    }
}
