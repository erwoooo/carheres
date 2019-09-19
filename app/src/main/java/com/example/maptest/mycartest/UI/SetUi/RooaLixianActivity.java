package com.example.maptest.mycartest.UI.SetUi;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.example.maptest.mycartest.Bean.CommandResponse;
import com.example.maptest.mycartest.New.NewHttpUtils;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.UI.SetUi.service.K100B;
import com.example.maptest.mycartest.UI.SetUi.service.PostCommand;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.CheckNumBer;
import com.example.maptest.mycartest.Utils.DateChangeUtil;
import com.example.maptest.mycartest.Utils.TcpSocketClient;
import com.example.maptest.mycartest.Utils.http.AlertWarnService;
import com.example.maptest.mycartest.Utils.http.ResponseCallback;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Field;
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
 * Created by ${Author} on 2017/7/6.
 * Use to
 */

public class RooaLixianActivity extends AppCompatActivity implements View.OnClickListener,NumberPicker.OnScrollListener,NumberPicker.OnValueChangeListener{

    private RelativeLayout relativeLayoutr1, relativeLayoutr2, relativeLayoutr3;
    private ImageView imageViewr1, imageViewr2, imageViewr3;
    private ImageView imageView_quit;
    private TextView textView_forma;
    private String commId;
    private TcpSocketClient socketClient;
    private int index = 0,type = 17;
    private TextView editText_jiange;
    private Button button1;
    private PopupWindow window,windowTime;
    private  View view,viewtime;
    private TextView textView_cancel,textView_do;
    private NumberPicker numberPicker_hour,numberPicker_min,numberPicker_time;
    private int hour,min,time;
    private int i = 0;
    public static RequestQueue queue;
    private JSONObject object;
    private org.json.JSONObject objects = null;
    private org.json.JSONObject objectset = null;
    private String times,settime;
    private LinearLayout linearLayout_order;
    private Activity activity = RooaLixianActivity.this;
    private Map<String,Object>map = new HashMap<>();
    private String jsonOrder;
    private RelativeLayout relative_select;
    CommandResponse commandResponse;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 11){
                if (commandResponse != null){
                    PostCommand command = new PostCommand(AppCons.locationListBean.getTerminalID(),commId,AppCons.loginDataBean.getData().getUsername(),commandResponse.getContent());
                    NewHttpUtils.postCommand(new String(new Gson().toJson(command)),RooaLixianActivity.this,null);
                }else {
                    PostCommand command = new PostCommand(AppCons.locationListBean.getTerminalID(),commId,AppCons.loginDataBean.getData().getUsername());
                    NewHttpUtils.postCommand(new String(new Gson().toJson(command)),RooaLixianActivity.this,null);
                }
                commandResponse = null;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooa);
        initView();
        reQuestCenter();
        ininClick();
    }

    private void ininClick() {
        button1.setOnClickListener(this);
        relativeLayoutr1.setOnClickListener(this);
        relativeLayoutr2.setOnClickListener(this);
        relativeLayoutr3.setOnClickListener(this);
        imageView_quit.setOnClickListener(this);
        relative_select.setOnClickListener(this);
    }

    private void initView() {

        socketClient = TcpSocketClient.getInstance();
        relativeLayoutr1 = (RelativeLayout) findViewById(R.id.relative_rooa1);
        relativeLayoutr2 = (RelativeLayout) findViewById(R.id.relative_rooa2);
        relativeLayoutr3 = (RelativeLayout) findViewById(R.id.relative_rooa3);
        relative_select = (RelativeLayout) findViewById(R.id.relative_select);
        imageViewr1 = (ImageView) findViewById(R.id.image_rooa1);
        imageViewr2 = (ImageView) findViewById(R.id.image_rooa2);
        imageViewr3 = (ImageView) findViewById(R.id.image_rooa3);
        imageView_quit = (ImageView) findViewById(R.id.image_quitrooa);

        textView_forma = (TextView)findViewById(R.id.text_forma_rooa);
        editText_jiange = (TextView) findViewById(R.id.edit_interval_rooa);

        button1 = (Button) findViewById(R.id.button_lixian_rooa);

        linearLayout_order = (LinearLayout) findViewById(R.id.linner_order_rooa);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.gw_windowinfo, null);
        viewtime = inflater.inflate(R.layout.gw_windowinfotime,null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.relative_rooa1:
                root1();
                break;
            case R.id.relative_rooa2:
                root2();
                break;
            case R.id.relative_rooa3:
                root3();
                break;
            case R.id.image_quitrooa:
                quitSet();
                break;
            case R.id.button_lixian_rooa:
                Log.e("INDEX",index +"");
                if (index == 0){
                    Log.e("INDEX1",index +"");
                    try {
                        sendDate();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else if (index == 1 && editText_jiange.getText().toString().contains(":")){
                    Log.e("INDEX2",index +"");
                    times = editText_jiange.getText().toString();
                    settime = times.replace(":",",");
                    try {
                        sendDate();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else if (index == 2 && CheckNumBer.numBers(editText_jiange.getText().toString())){
                    times = editText_jiange.getText().toString();
                    Log.e("INDEX3",index +"");
                    try {
                        sendDate();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"请确认格式",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.relative_select:
                    switch (index){
                        case 1:
                            showPopwindow(linearLayout_order,editText_jiange);
                            break;
                        case 2:
                            showPopwindowTime(linearLayout_order,editText_jiange);
                            break;
                    }
                break;
        }
    }
    private void root1() {
        index = 0;
        linearLayout_order.setVisibility(View.GONE);
        imageViewr1.setVisibility(View.VISIBLE);
        imageViewr2.setVisibility(View.GONE);
        imageViewr3.setVisibility(View.GONE);
        if (window != null && window.isShowing()){
            window.dismiss();
        }
        if (windowTime != null && windowTime.isShowing()){
            windowTime.dismiss();
        }
    }
    private void root2() {
        index = 1;
        textView_forma.setText("格式:18:30(设置成每天18:30开机定位一次)");
        imageViewr1.setVisibility(View.GONE);
        imageViewr2.setVisibility(View.VISIBLE);
        linearLayout_order.setVisibility(View.VISIBLE);
        imageViewr3.setVisibility(View.GONE);
        if (windowTime != null && windowTime.isShowing()){
            windowTime.dismiss();
        }

    }

    private void root3() {
        index = 2;
        textView_forma.setText("格式:2(设置成间隔2小时设备开机定位一次)");
        linearLayout_order.setVisibility(View.VISIBLE);
        imageViewr1.setVisibility(View.GONE);
        imageViewr2.setVisibility(View.GONE);
        imageViewr3.setVisibility(View.VISIBLE);
        if (window != null && window.isShowing()){
            window.dismiss();
        }
    }

    private void quitSet() {
        if (socketClient != null)
            socketClient = null;
        if (queue != null)
            queue = null;
        activity = null;
        commId = null;
        object = null;
        objectset = null;
        objects = null;
        imageView_quit = null;
        finish();

    }


    /**
     * @throws IOException
     * @throws InterruptedException 发送离线指令
     */
    private void sendDate() throws IOException, InterruptedException {
        switch (index) {
            case 0:
                commId = "WORKMODE,0,0,0#";     //常开
                break;
            case 1:
                commId = "WORKMODE,1,"+settime+"#";   //指定时间
                break;
            case 2:
                times = editText_jiange.getText().toString();
                commId = "WORKMODE,2,"+times+"#";       //间隔
                break;
        }
        Log.d("index", index + "");
        Log.d("index", commId);
        JSONObject object = new JSONObject();
        object.put("terminalID", AppCons.locationListBean.getTerminalID());              //设备的IEMI号
        object.put("deviceProtocol", AppCons.locationListBean.getLocation().getDeviceProtocol());      //设备协议号
        object.put("content", commId);

        NewHttpUtils.sendOrder(object.toJSONString(), RooaLixianActivity.this, new ResponseCallback() {
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
     * 将离线指令保存到服务器
     */
    private void postOrder() {
        if (AppCons.ORDERBEN == null){
            AppCons.ORDERBEN = new K100B();
            AppCons.ORDERBEN.setTerminalID(AppCons.locationListBean.getTerminalID());
        }
        switch (index){
            case 0:
                AppCons.ORDERBEN.getOffline().setModelState(0);
                break;
            case 1:
                AppCons.ORDERBEN.getOffline().setModelState(1);
                AppCons.ORDERBEN.getOffline().setInterval(times);
                break;
            case 2:
                try {
                    AppCons.ORDERBEN.getOffline().setModelState(2);
                    AppCons.ORDERBEN.getOffline().setTime(Integer.getInteger(times));
                }catch (Exception e){}
                break;
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
     * 查询离线指令
     */
    private void reQuestCenter() {
        if (AppCons.ORDERBEN != null){
            if (AppCons.ORDERBEN.getOffline() != null){
                try{
                    switch (AppCons.ORDERBEN.getOffline().getModelState()){  //模式选择
                        case 0:                             //定时模式
                            lixianOrder1();
                            break;
                        case 1:                             //定时时间
                            times = AppCons.ORDERBEN.getOffline().getInterval();
                            lixianOrder2();
                            break;
                        case 2:                             //间隔时间
                            times = AppCons.ORDERBEN.getOffline().getTime() + "";
                            lixianOrder3();
                            break;
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }


    }


    /**
     * 提交失败的断油电
     */
    private void postDefault()  {
            String time = DateChangeUtil.DateToStrs(new Date()).toString();
            switch (index){
                case 0:
                    type = 17;
                    break;
                case 1:
                    type = 18;
                    break;
                case 2:
                    type = 19;
                    break;
            }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://app.carhere.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        AlertWarnService service = retrofit.create(AlertWarnService.class);
        Call<ResponseBody>call = service.getOrder(AppCons.locationListBean.getTerminalID(),time,times,type,commId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    String string = new String(response.body().bytes());
                    Log.e("postdefault",string.isEmpty()?"返回为空":string);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("postdefault",t.toString());
            }
        });

    }


    private void lixianOrder1() {
        index = 0;
        root1();
    }
    private void lixianOrder3() {
        index = 2;
        editText_jiange.setText(times);
        root3();


    }

    private void lixianOrder2() {
        index = 1;
        editText_jiange.setText(times);
        root2();


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            quitSet();
        }
        return super.onKeyDown(keyCode, event);
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
            case R.id.number_picktime:
                time = newVal;
                break;
        }
    }


    /**
     * @param relativeLayout
     * @param textView_hour
     * 选择时间
     */
    private void showPopwindow(LinearLayout relativeLayout, final TextView textView_hour) {
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
        window.showAtLocation(relativeLayout, Gravity.NO_GRAVITY, location[0], location[1] + relativeLayout.getHeight());
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
                        if (min < 10){
                            textView_hour.setText(hour +":0" + min);
                        }else {
                            textView_hour.setText(hour +":" + min);
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

    /**
     * @param relativeLayout
     * @param textView_hour
     * 选择时间
     */
    private void showPopwindowTime(LinearLayout relativeLayout, final TextView textView_hour) {
        if (windowTime == null){
            windowTime = new PopupWindow(viewtime, dip2px(this,150),
                    dip2px(this,260));
            windowTime.setFocusable(false);
            ColorDrawable dw = new ColorDrawable(0x808080);
            windowTime.setBackgroundDrawable(dw);
            textView_cancel = (TextView) viewtime.findViewById(R.id.gw_text_cancel);
            textView_do = (TextView) viewtime.findViewById(R.id.gw_text_do);
            numberPicker_time = (NumberPicker) viewtime.findViewById(R.id.number_picktime);
            setNumberPickerDividerColor(numberPicker_time);
            numberPicker_time.setMaxValue(9999);
            numberPicker_time.setMinValue(1);
            numberPicker_time.setOnValueChangedListener(this);
            numberPicker_time.setOnScrollListener(this);

        }
        int[] location = new int[2];
        relativeLayout.getLocationOnScreen(location);
        windowTime.showAtLocation(relativeLayout, Gravity.NO_GRAVITY, location[0], location[1] + relativeLayout.getHeight());
        textView_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                windowTime.dismiss();
            }
        });
        textView_do.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView_hour.setText(time + "");

                    }
                });

                windowTime.dismiss();
            }
        });
        windowTime.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                System.out.print("消失");
            }
        });
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

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        Log.e("dp to px",(dpValue * scale + 0.5f) + "");
        return (int) (dpValue * scale + 0.5f);
    }
}
