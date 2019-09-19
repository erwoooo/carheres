package com.example.maptest.mycartest.UI;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.maptest.mycartest.Entity.PriceBean;
import com.example.maptest.mycartest.New.NewHttpUtils;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.BaseActivity;
import com.example.maptest.mycartest.Utils.DateChangeUtil;
import com.example.maptest.mycartest.Utils.PayDatePickActivity;
import com.example.maptest.mycartest.Utils.http.GetCommIdService;
import com.example.maptest.mycartest.Utils.http.ResponseCallback;
import com.google.gson.Gson;


import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.maptest.mycartest.Utils.AppCons.locationListBean;


/**
 * Created by ${Author} on 2017/5/15.
 * Use to
 */
public class CarPayAcitivity extends BaseActivity implements View.OnClickListener{
    private RelativeLayout relativeLayout_one,relativeLayout_two,relativeLayout_foever,relativeLayout_auto;
    private ImageView imageView_one,imageView_two,imageView_foever,imageView_auto,imageView_quit;
    private LinearLayout linearLayout_auto;

    private TextView textView_endtimes,textView_num;
    private float payNum  ;
    private SimpleDateFormat sdf;
    private Map<String,Object>map = new HashMap<>();
    private Map<String,Object>maprec = new HashMap<>();
    private String jsonOrder,jsonRec;
    private DatePickerDialog  dialog_choseTime ;
    private View view_time;
    private DatePicker datePicker;
    private String datestrold;
    private String datestr;
    private Button button_cancel;
    private Button button_sureTime;
    private String updateTime;
    private Calendar calendar;
    private boolean isEmput = true;
    private  PriceBean bean;
    private   DecimalFormat df;
    private TextView pay_zfb_sure,pay_wx_sure;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        initView();
        initClick();
        requestPrice();
    }

    private void initClick() {
        relativeLayout_one.setOnClickListener(this);
        relativeLayout_two.setOnClickListener(this);
        relativeLayout_auto.setOnClickListener(this);
        relativeLayout_foever.setOnClickListener(this);
        linearLayout_auto.setOnClickListener(this);
        imageView_quit.setOnClickListener(this);

        textView_endtimes.setOnClickListener(this);
        pay_zfb_sure.setOnClickListener(this);
        pay_wx_sure.setOnClickListener(this);

    }

    private void initView() {
        df   = new DecimalFormat("######0.0");
        sdf =  new SimpleDateFormat("yyyy-MM-dd");
        calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, +1);
        updateTime = sdf.format(calendar.getTime());
        Log.d("一年",updateTime);
        pay_zfb_sure = findViewById(R.id.pay_zfb_sure);
        pay_wx_sure = findViewById(R.id.pay_wx_sure);
        relativeLayout_one = (RelativeLayout) findViewById(R.id.pay_rel_one);
        relativeLayout_two = (RelativeLayout) findViewById(R.id.pay_rel_two);
        relativeLayout_auto = (RelativeLayout) findViewById(R.id.pay_rel_auto);
        relativeLayout_foever = (RelativeLayout) findViewById(R.id.pay_rel_forever);
        linearLayout_auto = (LinearLayout) findViewById(R.id.pay_liner_data);
        imageView_one = (ImageView) findViewById(R.id.pay_image_one);
        imageView_two = (ImageView) findViewById(R.id.pay_image_two);
        imageView_foever = (ImageView) findViewById(R.id.pay_image_foever);
        imageView_auto = (ImageView) findViewById(R.id.pay_image_auto);
        imageView_quit = (ImageView) findViewById(R.id.image_payservice);
        textView_endtimes = (TextView) findViewById(R.id.pay_text_start);
        textView_num = (TextView) findViewById(R.id.pay_text_num);
        if (bean != null)
            payNum = bean.getOneYearPrice();
        textView_num.setText(payNum + "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pay_rel_one:
                setUiOne();
                break;
            case R.id.pay_rel_two:
                setUiTwo();
                break;
            case R.id.pay_rel_forever:
                setUiForever();
                break;
            case R.id.pay_rel_auto:
                setUiAuto();
                break;
            case R.id.image_payservice:
                quit();
                break;

            case R.id.pay_text_start:
                Intent intent = new Intent(this, PayDatePickActivity.class);
                intent.putExtra("date", textView_endtimes.getText());
                startActivityForResult(intent, 3);
                break;
            case R.id.pay_zfb_sure:
                pay_zfb();
                break;
            case R.id.pay_wx_sure:

                pay_wx();
                break;
        }
    }

    private void pay_wx() {
        updateTime(updateTime);
    }

    private void pay_zfb() {

    }

    private void setUiAuto() {
        isEmput = true;
        updateTime = "";
        Log.d("自定义",updateTime);
        textView_num.setText(payNum + "");
        imageView_one.setVisibility(View.GONE);
        imageView_two.setVisibility(View.GONE);
        imageView_foever.setVisibility(View.GONE);
        imageView_auto.setVisibility(View.VISIBLE);
        linearLayout_auto.setVisibility(View.VISIBLE);
    }

    private void setUiForever() {
        isEmput = false;
        updateTime = "";
        Log.d("永久",updateTime + "永久");
        if (bean != null)
            payNum = bean.getForeverPrice() ;
        textView_num.setText(payNum + "");
        imageView_one.setVisibility(View.GONE);
        imageView_two.setVisibility(View.GONE);
        imageView_foever.setVisibility(View.VISIBLE);
        imageView_auto.setVisibility(View.GONE);
        linearLayout_auto.setVisibility(View.GONE);
    }

    private void setUiTwo() {
        isEmput = true;
        calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, +2);
        updateTime = sdf.format(calendar.getTime());
        Log.d("两年",updateTime);
        if (bean != null)
            payNum = bean.getTwoYearPrice() ;
        textView_num.setText(payNum + "");
        imageView_one.setVisibility(View.GONE);
        imageView_two.setVisibility(View.VISIBLE);
        imageView_foever.setVisibility(View.GONE);
        imageView_auto.setVisibility(View.GONE);
        linearLayout_auto.setVisibility(View.GONE);

    }

    private void setUiOne() {
        isEmput = true;
        calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, +1);
        updateTime = sdf.format(calendar.getTime());
        Log.d("一年",updateTime);
        if (bean != null)
            payNum = bean.getOneYearPrice() ;
        textView_num.setText(payNum + "");
        imageView_one.setVisibility(View.VISIBLE);
        imageView_two.setVisibility(View.GONE);
        imageView_foever.setVisibility(View.GONE);
        imageView_auto.setVisibility(View.GONE);
        linearLayout_auto.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (resultCode == Activity.RESULT_OK && requestCode == 3) {
                // 选择预约时间的页面被关闭
                String date = data.getStringExtra("date");
                if (!textView_endtimes.getText().toString().equals(date)) {
                    textView_endtimes.setText(data.getStringExtra("date"));
                    updateTime = data.getStringExtra("date");    //选择自定义
                    Log.d("自定义选择之后",updateTime);
                    countSum(data.getStringExtra("date"));
                } else {
                    System.out.println("选择未变");
                }
            }

        }
    }

    /**
     * @param dates
     */
    private void countSum(String dates){
        Date nowDate = DateChangeUtil.StrToDateYear(sdf.format(new Date()));//获取当前时间
        Date endDate =  DateChangeUtil.StrToDateYear(dates);       //设置的自定义时间
        long currentTime= (endDate.getTime() - nowDate.getTime());
        long day = DateChangeUtil.getDay(currentTime);
        payNum =  Float.parseFloat(df.format((day * bean.getOneDayPrice())));
        Log.e("自定义", ":" + payNum);
//        payNum = (float) 0.01;
        textView_num.setText(payNum + "");
    }
    private void  requestPrice(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppCons.COMMONURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetCommIdService service = retrofit.create(GetCommIdService.class);
        Map<Object,Object> map = new HashMap<>();
        map.put("user_id",1);
        Gson gson=new Gson();
        String obj = gson.toJson(map);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj);
        Call<ResponseBody> call = service.postCom(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String string = new String(response.body().bytes());
                    bean = JSONObject.parseObject(string,PriceBean.class);
                    Log.e("price",string);
                    Log.e("price",bean.toString());
                    payNum = bean.getOneYearPrice();
                    textView_num.setText(payNum + "");
                } catch (IOException e) {
                    Log.e("exception",e.toString());
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Throwable",t.toString());
            }
        });
    }




    private void updateTime(String time){

        locationListBean.getDevice().setHireExpirationTime(time);
        Map<Object,Object>map = new HashMap<Object, Object>();
        map.put("hireExpirationTime",time);
        map.put("list",new String[]{locationListBean.getTerminalID()});
        NewHttpUtils.sethireExpirationTime(new Gson().toJson(map), this, new ResponseCallback() {
            @Override
            public void TaskCallBack(Object object) {

            }

            @Override
            public void FailCallBack(Object object) {

            }
        });

//        map.clear();
//        Log.e("times",time);
//        map.put("ExpirationTime",time);   //平台设备都一样
//        map.put("HireExpirationTime",time);
//        jsonOrder = JSON.toJSONString(map);
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(AppCons.COMMONURL)
//                .build();
//        GetCommIdService service = retrofit.create(GetCommIdService.class);
//        Call<ResponseBody> call = service.updateTime(locationListBean.getTerminalID(),jsonOrder);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                try {
//                    String string = new String(response.body().bytes());
//                    if (string.contains("1")){
//                        JumpActivity();
//                    }
//                    Log.e("update",string);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//            }
//        });
    }

    private void quit(){
        payNum  = 0;
        updateTime = null;
        finish();
    }


    private void JumpActivity(){
        startActivity(new Intent(CarPayAcitivity.this, CarContralActivity.class));
        finish();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode){
            quit();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void recCharge(){
        Log.e("postTime",DateChangeUtil.DateToStrs(new Date()));
        maprec.clear();
        maprec.put("TerminalID",locationListBean.getTerminalID());       //设备号
        maprec.put("PayPrice",payNum);                              //支付的价格
        maprec.put("ServiceTime",updateTime);                       //充值到的时间
        maprec.put("RechargeTime",DateChangeUtil.DateToStrs(new Date()));       //当前的时间
        jsonRec = JSON.toJSONString(maprec);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://app.carhere.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetCommIdService service = retrofit.create(GetCommIdService.class);
        Call<ResponseBody> call = service.postReord(jsonRec);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response != null){
                    try {
                        String string = new String(response.body().bytes());
                        Log.e("record",string);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }else {
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("record",t.toString());
            }
        });
    }
}
