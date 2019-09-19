package com.example.maptest.mycartest.UI;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.maptest.mycartest.New.NewHttpUtils;
import com.example.maptest.mycartest.New.UpAgentBean;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.BaseActivity;
import com.example.maptest.mycartest.Utils.GwaFragmentController;
import com.example.maptest.mycartest.Utils.InforFragmentControlleRooa;
import com.example.maptest.mycartest.Utils.InforFragmentController;
import com.example.maptest.mycartest.Utils.InforFragmentdisplayController;
import com.example.maptest.mycartest.Utils.MyGlideUrl;
import com.example.maptest.mycartest.Utils.http.ResponseCallback;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import static com.example.maptest.mycartest.Utils.AppCons.TELL;
import static com.example.maptest.mycartest.Utils.AppCons.locationListBean;

/**
 * Created by ${Author} on 2017/3/20.
 * Use to   车辆信息的宿主activity
 */

public class InformationActivity extends BaseActivity implements View.OnClickListener {

    private TextView textView_msg,textView_trace,textView_history,textView_set;
    private ImageView imageView_msg,imageView_trace,imageView_history,imageView_set,image_picture;
    private InforFragmentController controller;
    private InforFragmentdisplayController discontroller;
    private InforFragmentControlleRooa controlleRooa;
    private GwaFragmentController gwaFragmentController;
    private LinearLayout linearLayout_msg,linearLayout_trace,linearLayout_history,linearLayout_set;
    private int i = 0;
    private Dialog dialog_picture;
    private View view_pic;
    public static InformationActivity instance;
    private String mPublicPhotoPath;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_informations);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        i = getIntent().getIntExtra("position",0);

        initView();
        initClick();
        instance = this;
    }

    private void initClick() {
        linearLayout_msg.setOnClickListener(this);
        linearLayout_trace.setOnClickListener(this);
        linearLayout_history.setOnClickListener(this);
        linearLayout_set.setOnClickListener(this);

    }
    private void initView() {
        try{
            AppCons.ADDRESS = locationListBean.getAddress();
        }catch (Exception e){}
        if (!locationListBean.getPicture().equals("")){
            mPublicPhotoPath = locationListBean.getPicture();
        }
        findUpAgent();
        dialog_picture = null;
        linearLayout_msg = (LinearLayout) findViewById(R.id.linerlayout_msg);
        linearLayout_trace = (LinearLayout) findViewById(R.id.linerlayout_trace);
        linearLayout_history = (LinearLayout) findViewById(R.id.linerlayout_history);
        linearLayout_set = (LinearLayout) findViewById(R.id.linerlayout_set);
        switch (locationListBean.getState()){
            case 1:    //设备在线
                switch (locationListBean.getLocation().getDeviceProtocol()){  //根据协议
                    case 0:   //k100
                    case 1:   //k100b
                        if (locationListBean.getDevice().getDeviceType().equals("GW005A") || locationListBean.getDevice().getDeviceType().equals("GT750A")
                                || locationListBean.getDevice().getDeviceType().equals("V703D")){   //V703D没有设防撤防
                                gwaFragmentController = GwaFragmentController.getInsance(this,R.id.linerlayout_container);
                                gwaFragmentController.showFragment(i);
                            }else {
                                controller = InforFragmentController.getInsance(this,R.id.linerlayout_container);
                                controller.showFragment(i);
                        }
                        break;
                    case 2:  //r001
                    case 3:  //r002
                    case 4:   //tire
                    case 5:   //obd
                        controller = InforFragmentController.getInsance(this,R.id.linerlayout_container);
                        controller.showFragment(i);
                        break;
                    case 6:  //bsj
                        controlleRooa = InforFragmentControlleRooa.getInsance(this,R.id.linerlayout_container);
                        controlleRooa.showFragment(i);
                        break;
                    case 8:     //808协议
//                        controlleRooa = InforFragmentControlleRooa.getInsance(this,R.id.linerlayout_container);
//                        controlleRooa.showFragment(i);
//                        linearLayout_set.setVisibility(View.VISIBLE);
//                        break;
                    default:
                        controller = InforFragmentController.getInsance(this,R.id.linerlayout_container);
                        controller.showFragment(i);
                        break;
                }
                break;
            case 2:
                Log.e("设备离线", locationListBean.toString());
                switch (locationListBean.getLocation().getDeviceProtocol()){  //根据协议  离线的时候
                    case 0:
                    case 1:
                        if (locationListBean.getDevice().getDeviceType().equals("GW005A") || locationListBean.getDevice().getDeviceType().equals("GT750A")
                                || locationListBean.getDevice().getDeviceType().equals("V703D")){   //V703D没有设防撤防
                            gwaFragmentController = GwaFragmentController.getInsance(this,R.id.linerlayout_container);
                            gwaFragmentController.showFragment(i);
                        }else {
                            discontroller = InforFragmentdisplayController.getInsance(this,R.id.linerlayout_container);
                            discontroller.showFragment(i);
                            linearLayout_set.setVisibility(View.GONE);
                        }
                        break;
                    case 4:
                    case 5:
                        discontroller = InforFragmentdisplayController.getInsance(this,R.id.linerlayout_container);
                        discontroller.showFragment(i);
                        linearLayout_set.setVisibility(View.GONE);
                        break;

                    case 2:
                    case 3:
                    case 6:
                        controlleRooa = InforFragmentControlleRooa.getInsance(this,R.id.linerlayout_container);
                        controlleRooa.showFragment(i);
                        linearLayout_set.setVisibility(View.VISIBLE);
                        break;
                    case 8:
//                        controlleRooa = InforFragmentControlleRooa.getInsance(this,R.id.linerlayout_container);
//                        controlleRooa.showFragment(i);
//                        linearLayout_set.setVisibility(View.VISIBLE);
//                        break;
                    default:
                        discontroller = InforFragmentdisplayController.getInsance(this,R.id.linerlayout_container);
                        discontroller.showFragment(i);
                        linearLayout_set.setVisibility(View.GONE);
                        break;
                }

                break;
        }
        textView_msg = (TextView) findViewById(R.id.text_message);
        textView_trace = (TextView) findViewById(R.id.text_trace);
        textView_history = (TextView) findViewById(R.id.text_history);
        textView_set = (TextView) findViewById(R.id.text_setinfo);
        imageView_msg = (ImageView) findViewById(R.id.image_message);
        imageView_trace = (ImageView) findViewById(R.id.image_trace);
        imageView_history = (ImageView) findViewById(R.id.image_history);
        imageView_set = (ImageView) findViewById(R.id.image_setinfo);

        switch (i){
            case 0:
                showmessage();
                break;
            case 1:
                showtrace();
                break;
            case 2:
                showhistory();
                break;
            case 3:
                showset();
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linerlayout_msg:
                if (controller != null){
                    controller.showFragment(0);
                }
                if (discontroller != null){
                discontroller.showFragment(0);
                 }
                if (controlleRooa != null){
                controlleRooa.showFragment(0);
                }
                if (gwaFragmentController != null){
                    gwaFragmentController.showFragment(0);
                }
                showmessage();
                break;
            case R.id.linerlayout_trace:
                if (controller != null){
                    controller.showFragment(1);
                }
                if (discontroller != null){
                discontroller.showFragment(1);
                }
                if (controlleRooa != null){
                controlleRooa.showFragment(1);
                 }
                if (gwaFragmentController != null){
                    gwaFragmentController.showFragment(1);
                }
                showtrace();
                break;
            case R.id.linerlayout_history:
                if (controller != null){
                    controller.showFragment(2);
                }
                if (discontroller != null){
                discontroller.showFragment(2);
                }
                if (controlleRooa != null){
                controlleRooa.showFragment(2);
                }
                if (gwaFragmentController != null){
                    gwaFragmentController.showFragment(2);
                }
                showhistory();
                break;
            case R.id.linerlayout_set:
                if (controller != null){
                    controller.showFragment(3);
                }
                if (controlleRooa != null){
                controlleRooa.showFragment(3);
                }
                if (gwaFragmentController != null){
                    gwaFragmentController.showFragment(3);
                }
                showset();
                break;
        }

    }

    //查询代理商电话号码


    private void findUpAgent(){
        Map<Object,Object> map = new HashMap<>();
        map.put("id",AppCons.loginDataBean.getData().getUser_id());
        Gson gson = new Gson();
        String obj = gson.toJson(map);
        NewHttpUtils.querrUpAgent(obj, this, new ResponseCallback() {
            @Override
            public void TaskCallBack(Object object) {
                if (object != null){
                    UpAgentBean bean = JSONObject.parseObject((String) object,UpAgentBean.class);
                    if (bean != null && bean.getData() != null){
                        TELL = bean.getData().getTelephone();
                    }
                }
            }

            @Override
            public void FailCallBack(Object object) {

            }
        });
    }




    public void showPicDialog(){
        if (locationListBean.getPicture().equals("")){
            Toast.makeText(this,"当前设备没有车辆图片",Toast.LENGTH_SHORT).show();
            return;
        }
        dialog_picture = null;
        if (dialog_picture == null){
        Log.e("上传的图片",locationListBean.getPicture() + "——————————");
        dialog_picture = new Dialog(InformationActivity.this,R.style.ActionDialogStyle);
        view_pic =getLayoutInflater().inflate(R.layout.dialog_picture,null);
            image_picture = view_pic.findViewById(R.id.image_picture);
        Glide.with(InformationActivity.this).load(new MyGlideUrl(locationListBean.getPicture())).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(image_picture);
        dialog_picture.setContentView(view_pic);
        dialog_picture.setCanceledOnTouchOutside(true);
        Window dialogWindow = dialog_picture.getWindow();
        dialogWindow.setGravity( Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        WindowManager wm = (WindowManager)
                getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        lp.height = (int) (height * 0.6);//设置Dialog距离底部的距离
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        view_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_picture.dismiss();
            }
        });
        dialogWindow.setAttributes(lp);
        dialog_picture.show();
        }
//        else {
//            if (mPublicPhotoPath.equals(locationListBean.getPicture())){
//                if (dialog_picture != null && !dialog_picture.isShowing())
//                dialog_picture.show();
//            }else {
//                mPublicPhotoPath = locationListBean.getPicture();
//                if (dialog_picture != null && !dialog_picture.isShowing()){
//                    Glide.with(this).load(new MyGlideUrl(locationListBean.getPicture())).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(image_picture);
//                    dialog_picture.show();
//                }
//
//            }
//
//
//        }


    }

    private void showset() {
        imageView_set.setImageResource(R.drawable.icon_chache_shezhi_selected);
        textView_set.setTextColor(Color.parseColor("#ff7f00"));
        imageView_history.setImageResource(R.drawable.icon_chache_huifang);
        textView_history.setTextColor(Color.parseColor("#999999"));
        imageView_trace.setImageResource(R.drawable.icon_chache_zhuizong);
        textView_trace.setTextColor(Color.parseColor("#999999"));
        imageView_msg.setImageResource(R.drawable.icon_index_chache);
        textView_msg.setTextColor(Color.parseColor("#999999"));
    }

    private void showhistory() {
        imageView_set.setImageResource(R.drawable.icon_chache_shezhi);
        textView_set.setTextColor(Color.parseColor("#999999"));
        imageView_history.setImageResource(R.drawable.icon_chache_huifang_selected);
        textView_history.setTextColor(Color.parseColor("#ff7f00"));
        imageView_trace.setImageResource(R.drawable.icon_chache_zhuizong);
        textView_trace.setTextColor(Color.parseColor("#999999"));
        imageView_msg.setImageResource(R.drawable.icon_index_chache);
        textView_msg.setTextColor(Color.parseColor("#999999"));
    }

    private void showtrace() {
        imageView_set.setImageResource(R.drawable.icon_chache_shezhi);
        textView_set.setTextColor(Color.parseColor("#999999"));
        imageView_history.setImageResource(R.drawable.icon_chache_huifang);
        textView_history.setTextColor(Color.parseColor("#999999"));
        imageView_trace.setImageResource(R.drawable.icon_chache_zhuizong_selected);
        textView_trace.setTextColor(Color.parseColor("#ff7f00"));
        imageView_msg.setImageResource(R.drawable.icon_index_chache);
        textView_msg.setTextColor(Color.parseColor("#999999"));
    }

    private void showmessage() {
        imageView_set.setImageResource(R.drawable.icon_chache_shezhi);
        textView_set.setTextColor(Color.parseColor("#999999"));
        imageView_history.setImageResource(R.drawable.icon_chache_huifang);
        textView_history.setTextColor(Color.parseColor("#999999"));
        imageView_trace.setImageResource(R.drawable.icon_chache_zhuizong);
        textView_trace.setTextColor(Color.parseColor("#999999"));
        imageView_msg.setImageResource(R.drawable.icon_index_chache_selected);
        textView_msg.setTextColor(Color.parseColor("#ff7f00"));
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e("mation","onResume");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.e("mation","onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("mation","onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("mation","onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("mation","onStart");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (gwaFragmentController != null){
            gwaFragmentController.destoryController();
            gwaFragmentController = null;
        }
        if (controller != null){
            controller.destoryController();
            controller = null;
        }
        if (discontroller != null){
            discontroller.destoryController();
            discontroller = null;
        }
        if (controlleRooa != null){
            controlleRooa.destoryController();
            controlleRooa = null;
        }
        imageView_msg = null;
        imageView_set = null;
        imageView_trace = null;
        imageView_history = null;
        textView_msg = null;
        textView_trace = null;
        textView_history = null;
        textView_set = null;
        linearLayout_history = null;
        linearLayout_msg = null;
        linearLayout_set = null;
        linearLayout_trace = null;

        Log.e("mation","onDestory");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        // mCurrentPosition表示当前切换的fragment的数组下标

        //super.onSaveInstanceState(outState); 总是执行这句代码来调用父类去保存视图层的状态，会导致fragmen重影，不能正常恢复状态
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        // 获取保存的数组下
        // 回复视图状态，恢复为fragmen的切换状态
//        controller.showFragment(savedInstanceState.getInt(POSITIONKEY));
        super.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Log.e("leveli","----------"+level);
    }
}
