package com.example.maptest.mycartest.UI.TyreUi;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maptest.mycartest.New.LocationListBean;
import com.example.maptest.mycartest.New.NewHttpUtils;
import com.example.maptest.mycartest.New.TyrePost;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.BaseActivity;
import com.example.maptest.mycartest.Utils.BitmapUtil;
import com.example.maptest.mycartest.Utils.http.ResponseCallback;
import com.google.gson.Gson;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ${Author} on 2017/3/6.
 * Use to 显示胎压状态
 */

public class TyreActivity extends BaseActivity implements View.OnClickListener {
    private ImageView imageView_tires1, imageView_tires2, imageView_tires3, imageView_tires4;
    private ImageView imageView, imageView_tyrequit;
    private ImageView imageView_bk;
    private TextView textView_pres1, textView_pres2, textView_pres3, textView_pres4;
    private TextView textView_temps1, textView_temps2, textView_temps3, textView_temps4;
    private TextView textView_warns1, textView_warns2, textView_warns3, textView_warns4;
    private LocationListBean tyreListBean;
    private ImageView image_tyre_download;
    private Bundle bundle = new Bundle();
    private String temId;

    private boolean rh = true;
    private   Timer timer;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:

                    break;
                case 5:
                    Toast.makeText(TyreActivity.this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tyre_shows);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        try{
            tyreListBean = (LocationListBean) getIntent().getExtras().getSerializable(AppCons.TEST_TYR);
        }catch (Exception e){
            e.printStackTrace();
        }

        initView();
        initClick();
        // && tyreListBean.getState() == 1
        if (tyreListBean != null) {         //判断temid是正常获取还是推送获取
            temId = tyreListBean.getTerminalID();
            initTyreState();
            Log.e("TEMID",temId + "sss1");
            bundle.putSerializable(AppCons.TEST_TYR, temId);
        }else if (temId == null){
            if (temId == null){
                temId = (String) getIntent().getExtras().get("TimID");
                AppCons.WARN_TYPE = false;
            }
            bundle.putSerializable(AppCons.TEST_TYR, temId);
            Log.e("TEMID",temId + "sss");
            initTyreState();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_tyre_download:
                if (temId == null){
                    bundle.putSerializable(AppCons.TEST_TYR, tyreListBean.getTerminalID());
                }
                startActivity(new Intent(TyreActivity.this, TyreOrderActivity.class).putExtras(bundle));
                break;
            case R.id.image_tyre_quit:
                quitTyre();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (temId != null){
            rh = true;
            timer = new Timer();            //定时器10秒钟刷新一次
            timer.schedule(new MyTask(), 10000, 10000);
            Log.e("timer","创建");
        }else {
            Log.e("timer","没有");
        }

    }


    /**
     * 绑定点击监听
     */
    private void initClick() {
        image_tyre_download.setOnClickListener(this);
        imageView_tyrequit.setOnClickListener(this);
        imageView.setOnClickListener(this);
    }

    /**
     * 初始化控件
     */
    private void initView() {

        image_tyre_download = (ImageView) findViewById(R.id.image_tyre_download);
        imageView = (ImageView) findViewById(R.id.image_centercar);
        imageView_tyrequit = (ImageView) findViewById(R.id.image_tyre_quit);
        imageView_bk = (ImageView) findViewById(R.id.relative_bk);
        imageView_tires1 = (ImageView) findViewById(R.id.image_tire1);
        imageView_tires2 = (ImageView) findViewById(R.id.image_tire2);
        imageView_tires3 = (ImageView) findViewById(R.id.image_tire3);
        imageView_tires4 = (ImageView) findViewById(R.id.image_tire4);
        //气压
        textView_pres1 = (TextView) findViewById(R.id.text_pres1);
        textView_pres2 = (TextView) findViewById(R.id.text_pres2);
        textView_pres3 = (TextView) findViewById(R.id.text_pres3);
        textView_pres4 = (TextView) findViewById(R.id.text_pres4);
        //温度
        textView_temps1 = (TextView) findViewById(R.id.text_temps1);
        textView_temps2 = (TextView) findViewById(R.id.text_temps2);
        textView_temps3 = (TextView) findViewById(R.id.text_temps3);
        textView_temps4 = (TextView) findViewById(R.id.text_temps4);
        //警报
        textView_warns1 = (TextView) findViewById(R.id.text_warns1);
        textView_warns2 = (TextView) findViewById(R.id.text_warns2);
        textView_warns3 = (TextView) findViewById(R.id.text_warns3);
        textView_warns4 = (TextView) findViewById(R.id.text_warns4);
//        imageView.setImageBitmap(BitmapUtil.compressImage(BitmapUtil.readBitMap(this, R.drawable.bg_taiya_yellowcar)));
        imageView_bk.setImageBitmap(BitmapUtil.compressImage(BitmapUtil.readBitMap(this, R.drawable.tyre_bg)));
        imageView_bk.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    /**
     * 请求胎压状态数据
     */
    private void initTyreState() {


        new Thread(new Runnable() {
            @Override
            public void run() {
                TyrePost bean = new TyrePost(temId);
                Gson gson = new Gson();
                String obj = gson.toJson(bean);
                NewHttpUtils.querrTire(obj,TyreActivity.this,tireCallback);

            }
        }).start();
    }

    private ResponseCallback tireCallback = new ResponseCallback() {
        @Override
        public void TaskCallBack(Object object) {
            if (object != null){
                TyreListStatuBean bean = (TyreListStatuBean) object;
                initTyreDate(bean);
            }

        }

        @Override
        public void FailCallBack(Object object) {

        }
    };

    private void initTyreDate(TyreListStatuBean tyreListStatuBean){
        textView_pres1.setText(tyreListStatuBean.getOne().getPressure() + " Bar");
        textView_temps1.setText(tyreListStatuBean.getOne().getTemperature() + " ℃");
        textView_pres2.setText(tyreListStatuBean.getTwo().getPressure() + " Bar");
        textView_temps2.setText(tyreListStatuBean.getTwo().getTemperature() + " ℃");
        textView_pres4.setText(tyreListStatuBean.getThree().getPressure() + " Bar");
        textView_temps4.setText(tyreListStatuBean.getThree().getTemperature() + " ℃");
        textView_pres3.setText(tyreListStatuBean.getFour().getPressure() + " Bar");
        textView_temps3.setText(tyreListStatuBean.getFour().getTemperature() + " ℃");
        //第一个的状态
        initEveryTyre(tyreListStatuBean.getOne().getTireState(),tyreListStatuBean.getOne().getPressureState(),tyreListStatuBean.getOne().getTemperatureState(),tyreListStatuBean.getOne().getElectricState()
        ,textView_temps1,textView_pres1,textView_warns1,imageView_tires1,tyreListStatuBean.getOne().getPressure(),tyreListStatuBean.getOne().getTemperature());
        //第一个的状态
        initEveryTyre(tyreListStatuBean.getTwo().getTireState(),tyreListStatuBean.getTwo().getPressureState(),tyreListStatuBean.getTwo().getTemperatureState(),tyreListStatuBean.getTwo().getElectricState()
                ,textView_temps2,textView_pres2,textView_warns2,imageView_tires2,tyreListStatuBean.getTwo().getPressure(),tyreListStatuBean.getTwo().getTemperature());
        //第一个的状态
        initEveryTyre(tyreListStatuBean.getFour().getTireState(),tyreListStatuBean.getFour().getPressureState(),tyreListStatuBean.getFour().getTemperatureState(),tyreListStatuBean.getFour().getElectricState()
                ,textView_temps3,textView_pres3,textView_warns3,imageView_tires3,tyreListStatuBean.getFour().getPressure(),tyreListStatuBean.getFour().getTemperature());
        //第一个的状态
        initEveryTyre(tyreListStatuBean.getThree().getTireState(),tyreListStatuBean.getThree().getPressureState(),tyreListStatuBean.getThree().getTemperatureState(),tyreListStatuBean.getThree().getElectricState()
                ,textView_temps4,textView_pres4,textView_warns4,imageView_tires4,tyreListStatuBean.getThree().getPressure(),tyreListStatuBean.getThree().getTemperature());

    }


    /*退出*/
    private void quitTyre() {
        temId = null;
        tyreListBean = null;

        bundle = null;
        if (handler != null){
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        if (timer != null){
            timer.cancel();
            timer = null;
        }
        finish();
        System.gc();
//        textView_warns1.setText("");
//        textView_warns2.setText("");
//        textView_warns3.setText("");
//        textView_warns4.setText("");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            quitTyre();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 定时器，15秒后开始刷新，每隔15秒刷新一次
     */
    private void timerRefush() {


    }
    /**
     * 执行定时器的类
     */
    private class MyTask extends TimerTask {
        public void run() {
            if (rh) {
                initTyreState();
            } else {

            }

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        rh = false;
        if (timer != null){
            timer.cancel();
            timer = null;
        }
    }

    private void initEveryTyre(int tire,int press,int temp,int elec,TextView tt1,TextView tp1,TextView tw1,ImageView img,float nump,int numtem){
        tp1.setText(nump + " Bar");
        tt1.setText(numtem + " ℃");
        if (tire == 0 && press == 0 && temp == 0 && elec == 0) {
            //如果四个状态都正常
            img.setImageResource(R.drawable.bg_taiya_normal);
            tp1.setTextColor(Color.parseColor("#ffffff"));
            tt1.setTextColor(Color.parseColor("#ffffff"));
            //如果正常，就不显示
            tw1.setVisibility(View.GONE);
        } else {   //如果不正常
            tw1.setVisibility(View.VISIBLE);
            img.setImageResource(R.drawable.bg_taiya_abnormal);
            tw1.setTextColor(Color.parseColor("#ffffff"));
            if (tire == 1) {       //如果胎压不正常
                tw1.setText("漏气");
                tp1.setTextColor(Color.parseColor("#DC143C"));
                tt1.setTextColor(Color.parseColor("#DC143C"));
            } else {     //如果胎压正常
                if (press == 1) {    //如果气不压正常
                    tw1.setText("气压高");
                    tp1.setTextColor(Color.parseColor("#DC143C"));

                    if (temp == 0) {    //如果温度正常
                        tp1.setTextColor(Color.parseColor("#ffffff"));
                    } else {
                        tp1.setTextColor(Color.parseColor("#DC143C"));
                    }
                } else if (press == 2) {
                    tw1.setText("气压低");
                    tp1.setTextColor(Color.parseColor("#DC143C"));
                    if (temp == 0) {    //如果温度正常
                        tt1.setTextColor(Color.parseColor("#ffffff"));
                    } else {
                        tt1.setTextColor(Color.parseColor("#DC143C"));
                    }
                } else {          //如果气压正常
                    tp1.setTextColor(Color.parseColor("#ffffff"));
                    if (temp == 1) {    //如果温度不正常
                        tw1.setText("温度高");
                        tt1.setTextColor(Color.parseColor("#DC143C"));
                    } else if (temp == 2) {
                        tw1.setText("温度低");
                        tt1.setTextColor(Color.parseColor("#DC143C"));
                    } else {        //如果温度正常
                        tt1.setTextColor(Color.parseColor("#ffffff"));
                        if (elec == 1) {
                            tw1.setText("电量低");
                        }
                    }
                }

            }
        }

    }

}
