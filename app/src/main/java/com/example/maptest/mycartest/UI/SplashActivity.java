package com.example.maptest.mycartest.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.maptest.mycartest.New.NewHttpUtils;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.UI.TyreUi.TyreActivity;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.BitmapUtil;
import com.example.maptest.mycartest.Utils.http.LoginService;
import com.example.maptest.mycartest.test.LoginDataBean;
import com.example.maptest.mycartest.test.PostBean;
import com.google.gson.Gson;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushManager;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ${Author} on 2017/3/21.
 * Use to 欢迎页面
 */

public class SplashActivity extends AppCompatActivity {
    boolean isFirstIn = false;

    private static final int GO_HOME = 1000;
    private static final int GO_GUIDE = 1001;
    // 延迟3秒
    private long SPLASH_DELAY_MILLIS = 1000;
    private String SHAREDPREFERENCES_NAME = "first_pref";
    private String pwd, name;
    private OkHttpClient client;
    private RequestBody body1;
    private ImageView imageView;
    private Bitmap bm;
    /**
     * Handler:跳转到不同界面
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_HOME:
                    reQuerry();
                    break;
                case GO_GUIDE:
                    goGuide();
                    break;
                case 1:
                    goHome();
                    break;
                case 2:
                    LoginDataBean loginDataBean = (LoginDataBean) msg.obj;
                    Bundle bundle = new Bundle();
                    bundle.putString("pwd", pwd);
                    bundle.putSerializable(AppCons.TEST_DATA, loginDataBean);
                    Intent intent = new Intent(SplashActivity.this, CarContralActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    loginJump();
                    break;
                case 4:
                    goHome();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
//        XGPushClickedResult click = XGPushManager.onActivityStarted(this);
//        if (click != null) {
//            //从推送通知栏打开-Service打开Activity会重新执行Laucher流程
//            //查看是不是全新打开的面板
//            if (isTaskRoot()) {
//                return;
//            }
//            //如果有面板存在则关闭当前的面板
//            finish();
//        }else {
//
//        }
        Log.e("AppCons.WARN_TYPE",AppCons.WARN_TYPE + "");

//        unRegister();
        if (AppCons.WARN_TYPE){
            if (isTaskRoot()) {
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putString("TimD","T");
            startActivity(new Intent(SplashActivity.this, TyreActivity.class));
            //如果有面板存在则关闭当前的面板
            finish();
        }else {
            initView();
            init();
        }


    }

    /**
     *初始化欢迎图片
     */
    private void initView() {
        imageView = (ImageView) findViewById(R.id.imageView8);
        bm = BitmapUtil.compressImage(BitmapUtil.readBitMap(this, R.mipmap.start));
        imageView.setImageBitmap(bm);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    /**
     * 判断是否第一次登录
     */
    private void init() {
        // 读取SharedPreferences中需要的数据
        // 使用SharedPreferences来记录程序的使用次数
        SharedPreferences preferences = getSharedPreferences(
                SHAREDPREFERENCES_NAME, MODE_PRIVATE);

        // 取得相应的值，如果没有该值，说明还未写入，用true作为默认值
        isFirstIn = preferences.getBoolean("isFirstIn", true);

        // 判断程序与第几次运行，如果是第一次运行则跳转到引导界面，否则跳转到主界面
        if (!isFirstIn) {
            // 使用Handler的postDelayed方法，3秒后执行跳转到MainActivity
            mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
        } else {
            mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
        }

    }

    /**
     * 去登录界面
     */
    private void goHome() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        loginJump();
    }

    /**
     * 引导界面
     */
    private void goGuide() {
        Intent intent = new Intent(SplashActivity.this, GuideActivitys.class);
        startActivity(intent);
        loginJump();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Uri uri = getIntent().getData();
        if (uri != null){
            startActivity(new Intent(SplashActivity.this,TyreActivity.class));
        }
    }

    /**
     * 获取存在本地的用户名和密码，如果为空，跳转到登录页，如果不为空，直接登录
     */
    private void reQuerry() {
        SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        name = preferences.getString("name", null);
        pwd = preferences.getString("password", null);
        if (name != null && pwd != null) {

            PostBean jsonObject = new PostBean(name,pwd);
            Gson gson=new Gson();
            String obj = gson.toJson(jsonObject);
            RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),obj);
            Retrofit retrofit = new Retrofit.Builder()
                  .baseUrl(AppCons.COMMONURL)
                    .client(NewHttpUtils.loginClient(SplashActivity.this))
                  .addConverterFactory(GsonConverterFactory.create())
                  .build();
            LoginService service = retrofit.create(LoginService.class);
            retrofit2.Call<LoginDataBean>call = service.getMessage(body);
            call.enqueue(new retrofit2.Callback<LoginDataBean>() {
                @Override
                public void onResponse(retrofit2.Call<LoginDataBean> call, retrofit2.Response<LoginDataBean> response) {
//
                    if (response.body() != null && response.body().getMeta().isSuccess() && (response.body().getMeta().getMessage().equals("ok") || response.body().getMeta().getMessage().equals("200"))){
                        Log.e("LoginDataBean",response.body().toString());
                        Message message2 = new Message();
                        message2.what = 2;
                        message2.obj = response.body();
                        AppCons.loginDataBean = response.body();
                        mHandler.sendMessage(message2);

                    }else {
                        Message message1 = new Message();
                        message1.what = 1;
                        mHandler.sendMessage(message1);
                    }

                }

                @Override
                public void onFailure(retrofit2.Call<LoginDataBean> call, Throwable t) {
                    Log.e("Throwable",t.toString());
                            Message message1 = new Message();
                            message1.what = 1;
                            mHandler.sendMessage(message1);
                }
            });

        } else {
            goHome();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("splactivity", "onStop");

    }

    private void loginJump() {
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
        pwd = null;
        name = null;
        client = null;
        body1 = null;
        if (bm != null){
            bm.recycle();
            bm = null;
        }
        finish();
        System.gc();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Log.e("levels", "----------" + level);
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            if (mHandler != null)
                mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
            Log.d("recycle", "recycle");
        }
    }


    private void unRegister(){
        XGPushManager.unregisterPush(getApplicationContext(), new XGIOperateCallback() {
            @Override
            public void onSuccess(Object o, int i) {
                Log.e("acacac", "注销成功");
//                Toast.makeText(getApplicationContext(),"关闭报警推送成功",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFail(Object o, int i, String s) {
                Log.e("acacac", "注销失败");
//                Toast.makeText(getApplicationContext(),"关闭报警推送失败",Toast.LENGTH_LONG).show();
            }
        });
    }
}
