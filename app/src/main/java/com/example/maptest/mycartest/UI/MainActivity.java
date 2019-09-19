package com.example.maptest.mycartest.UI;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maptest.mycartest.New.NewHttpUtils;
import com.example.maptest.mycartest.New.WeiboDialogUtils;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.BaseActivity;
import com.example.maptest.mycartest.Utils.DialogCreateUtil;
import com.example.maptest.mycartest.Utils.http.LoginService;
import com.example.maptest.mycartest.Utils.http.ResponseCallback;
import com.example.maptest.mycartest.test.LoginDataBean;
import com.example.maptest.mycartest.test.PostBean;
import com.example.maptest.mycartest.zxing.activity.CaptureActivity;
import com.google.gson.Gson;
import com.iflytek.autoupdate.IFlytekUpdate;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends BaseActivity implements View.OnClickListener {
    private EditText editText_user, editText_pass;
    private Button button_login, button_scan, button_experices;
    private CheckBox checkBox_rempass;
    private TextView textView_tip;
    private ImageView imageView_QRcode, imageView_tip;
    private boolean x = false;
    private String name, pwd;
    private Bundle bundle;
    private boolean tip = true;
    private Dialog dialog;
    private IFlytekUpdate updManager;
    private Toast mToast;
    public ImageView imageView_pass;
    private boolean status = true;
    private String  tempResponse;
    private String headers = null;
    @SuppressLint("ShowToast")
    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Toast.makeText(MainActivity.this,  msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    if (dialog != null ){
                        WeiboDialogUtils.closeDialog(dialog);
                    }
                    break;
                case 2:
                    Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    bundle = new Bundle();
                    bundle.putString("name", name);
                    bundle.putString("pwd", pwd);
                    Intent intent = new Intent(MainActivity.this, CarContralActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    AppCons.TEST_LG = false;
                    if (dialog != null ){
                        WeiboDialogUtils.closeDialog(dialog);
                    }
                    pwd = null;
                    name = null;
                    tempResponse = null;
                    if (!checkBox_rempass.isChecked()) {
                        editText_pass.setText("");
                        editText_user.setText("");
                    }
                    finish();
                    break;
                case 4:
                    Toast.makeText(MainActivity.this, "用户名错误", Toast.LENGTH_SHORT).show();
                    if (dialog != null ){
                        WeiboDialogUtils.closeDialog(dialog);
                    }
                    break;
                case 5:
                    Toast.makeText(MainActivity.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
                    if (dialog != null ){
                        WeiboDialogUtils.closeDialog(dialog);
                    }
                    break;
                case 6:
                    Toast.makeText(MainActivity.this, msg.obj + "", Toast.LENGTH_SHORT).show();
                    if (dialog != null ){
                        WeiboDialogUtils.closeDialog(dialog);
                    }
                    break;
                case 7:
                    Toast.makeText(MainActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                    if (dialog != null ){
                        WeiboDialogUtils.closeDialog(dialog);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.MODIFY_AUDIO_SETTINGS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_CALENDAR,Manifest.permission.REQUEST_INSTALL_PACKAGES
                    }, 0);
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        ZXingLibrary.initDisplayOpinion(this);
        initView();
        initClick();
//        install(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_pass_show:
                changeSatus();
                break;
            case R.id.textView_tips:
                if (tip) {
                    imageView_tip.setVisibility(View.VISIBLE);
                    tip = false;
                } else {
                    imageView_tip.setVisibility(View.GONE);
                    tip = true;
                }
                break;
            case R.id.button_login:
                hideTips();
                if (editText_user.getText().toString().isEmpty() || editText_pass.getText().toString().isEmpty()) { //登录判断用户名密码
                    DialogCreateUtil dialogCreateUtil = new DialogCreateUtil();
                    Dialog dialog = dialogCreateUtil.creatDialog(MainActivity.this);
                    dialog.show();
                } else {
                    name = editText_user.getText().toString();
                    pwd = editText_pass.getText().toString();
                    logins();
                    logincheck();
                }
//                upload();
                break;
            case R.id.button_scan:
                hideTips();
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},99);
                    startActivityForResult(new Intent(MainActivity.this, CaptureActivity.class).putExtra("Donde", "1"), 1);/*二维码*/
                    x = false;
                }else {
                    startActivityForResult(new Intent(MainActivity.this, CaptureActivity.class).putExtra("Donde", "1"), 1);/*二维码*/
                    x = false;
                }
                break;
            case R.id.image_QR_code:                //二维码扫描
                hideTips();
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},99);
                    startActivityForResult(new Intent(MainActivity.this, CaptureActivity.class).putExtra("Donde", "2"), 1);/*条形码*/
                    x = true;
                }else {
                    startActivityForResult(new Intent(MainActivity.this, CaptureActivity.class).putExtra("Donde", "2"), 1);/*条形码*/
                    x = true;
                }
                break;
            case R.id.button_experience:        //体验账号登录
                name = "体验账号";
                pwd = "123456";
                hideTips();
                logins();
//                editText_user.setText("体验账号");
//                editText_pass.setText("123456");
//                logincheck();
                break;
        }
    }
    private void changeSatus() {
        if (status){
            imageView_pass.setImageResource(R.drawable.icon_login_pass);
            status = false;
            editText_pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }else {
            status = true;
            imageView_pass.setImageResource(R.drawable.icon_login_passd);
            editText_pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }


    /**
     * @param requestCode
     * @param resultCode
     * @param data        根据请求码返回扫码结果，并解析
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {     /*扫描返回的值*/
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {

        } else {
            final String result = data.getExtras().getString("result");
            Log.e("result", result);
            if (x) {        //条形码
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject data = new JSONObject(result);
                            return;
                        } catch (JSONException e) {
                            e.printStackTrace();
                            editText_user.setText(result);
                        }

                    }
                });
            } else if (!x) {     //二维码
                if (result.contains("name") || result.contains("password")) {
                    com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(result);
                    name = (String) jsonObject.get("name");
                    pwd = (String) jsonObject.get("password");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject data = new JSONObject(result);
                                editText_user.setText(name);
                                editText_pass.setText(pwd);

                                logins();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                return;
                            }

                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "无法识别当前二维码", Toast.LENGTH_SHORT).show();  //如果不是车在哪二维码
                }
            }
        }
    }

    private void initClick() {
        imageView_pass.setOnClickListener(this);
        button_experices.setOnClickListener(this);
        button_scan.setOnClickListener(this);
        button_login.setOnClickListener(this);
        imageView_QRcode.setOnClickListener(this);
        textView_tip.setOnClickListener(this);

    }
    private void initView() {
//        NewHttpUtils.getAppName("carhere",MainActivity.this);
        imageView_pass = (ImageView) findViewById(R.id.image_pass_show);
        textView_tip = (TextView) findViewById(R.id.textView_tips);
        imageView_tip = (ImageView) findViewById(R.id.image_tips);
        editText_user = (EditText) findViewById(R.id.edit_username);
        editText_pass = (EditText) findViewById(R.id.edit_pass);
        button_login = (Button) findViewById(R.id.button_login);
        button_scan = (Button) findViewById(R.id.button_scan);
        button_experices = (Button) findViewById(R.id.button_experience);
        checkBox_rempass = (CheckBox) findViewById(R.id.checkbox_rempass);
        imageView_QRcode = (ImageView) findViewById(R.id.image_QR_code);
        setting(editText_user, editText_pass, checkBox_rempass);
        editText_pass.setFilters(new InputFilter[]{filter});
        editText_user.setFilters(new InputFilter[]{filter});
    }

    private void hideTips() {
        imageView_tip.setVisibility(View.GONE);
    }

    /**
     * 判断是否保存密码
     */
    private void logincheck() {
        if (checkBox_rempass.isChecked()) {
            saving(editText_user.getText().toString(), editText_pass.getText().toString(), true);
        } else {
            saving("", "", false);
        }
    }

    /**
     * @param s1
     * @param s2
     * @param x  输入的用户名和密码保存在本地
     */
    private void saving(String s1, String s2, boolean x) {     //保存用户名和密码
        SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        name = s1;
        pwd = s2;
        editor.putString("name", name);
        editor.putString("password", pwd);
        editor.putBoolean("check", x);
        editor.commit();
    }


    /**
     * @param e1
     * @param e2
     * @param checkBox 读取保存在本地的用户名，密码
     */
    private void setting(EditText e1, EditText e2, CheckBox checkBox) {
        SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        String name = preferences.getString("name", null);
        String password = preferences.getString("password", null);
        boolean x = preferences.getBoolean("check", false);
        if (x) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
        e1.setText(name);
        e2.setText(password);
    }
    /**
     * 登录方法，传入用户名，密码
     */
    private void logins() {
        AppCons.PWD = pwd;
        dialog = WeiboDialogUtils.createLoadingDialog(MainActivity.this,"正在登录");
        PostBean jsonObject = new PostBean(name,pwd);
        Gson gson=new Gson();
        String obj = gson.toJson(jsonObject);
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),obj);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppCons.COMMONURL)
                .client(NewHttpUtils.loginClient(MainActivity.this))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        LoginService service = retrofit.create(LoginService.class);
        retrofit2.Call<LoginDataBean>call = service.getMessage(body);
        call.enqueue(new retrofit2.Callback<LoginDataBean>() {
            @Override
            public void onResponse(retrofit2.Call<LoginDataBean> call, retrofit2.Response<LoginDataBean> response) {

                if (response.body() != null ){
                    Log.e("LoginDataBean",response.body().toString());
                    if ( response.body().getMeta().isSuccess() && (response.body().getMeta().getMessage().equals("ok") || response.body().getMeta().getMessage().equals("200"))){
                        Log.e("LoginDataBean",response.body().toString());
                        Message message2 = new Message();
                        message2.what = 2;
                        AppCons.loginDataBean = response.body();
                        handler.sendMessage(message2);
                    }else if (response.body().getMeta().getMessage().equals("201")){  //用户名错误
                        Message message = new Message();
                        message.what = 4;
                        handler.sendMessage(message);
                    }else if (response.body().getMeta().getMessage().equals("202")){  //密码错误
                        Message message = new Message();
                        message.what = 7;
                        handler.sendMessage(message);
                    }else if (response.body().getMeta().getMessage().equals("209")){  //用户名或密码错误
                        Message message = new Message();
                        message.what = 5;
                        handler.sendMessage(message);
                    }else if (response.body().getMeta().getMessage().equals("208")){  //用户到期
                        Message message2 = new Message();
                        message2.what = 2;
                        AppCons.loginDataBean = response.body();
                        handler.sendMessage(message2);
                    }
                }else {
                    Message message1 = new Message();
                    message1.what = 6;
                    message1.obj = "null";
                    handler.sendMessage(message1);
                }

            }
            @Override
            public void onFailure(retrofit2.Call<LoginDataBean> call, Throwable t) {
                Log.e("Throwable",t.toString());
                Message message1 = new Message();
                message1.what = 6;
                message1.obj = t;
                handler.sendMessage(message1);
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        name = null;
        pwd = null;
        bundle = null;
        if (dialog != null)
            dialog = null;
        updManager = null;
        mToast = null;
        handler.removeCallbacksAndMessages(null);
    }

    /*不能输入空格和回车*/
    public InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source.equals(" ") || source.toString().contentEquals("\n")) return "";
            else return null;
        }
    };



    private void upload(){
        File tempFile = new File(this.getFilesDir().getAbsoluteFile(), "car.apk");
        copyAssets(this, tempFile);
        if (tempFile.exists()){
            NewHttpUtils.uploadApk("49", "car.apk", "车在这儿版本更新:\n 更新修改服务商日期 \n更新车辆图片追踪", tempFile, MainActivity.this, new ResponseCallback() {
                @Override
                public void TaskCallBack(Object object) {

                }

                @Override
                public void FailCallBack(Object object) {

                }
            });
        }else {
            Log.e("文件","no exits");
        }

    }
    private void copyAssets(Context context, File tempFile) {   //读文件
        AssetManager assetManager = context.getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("TAG", "Failed to get asset file list.", e);
        }
        if (files != null) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open("car.apk");
                File outFile = tempFile;
                out = new FileOutputStream(outFile);
                copyFile(in, out);
            } catch(IOException e) {
                Log.e("tag", "Failed to copy asset file: " + tempFile, e);
            }
            finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        // NOOP
                        Log.e("in.close()",e.toString());
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        // NOOP
                        Log.e(" out.close()",e.toString());
                    }
                }

            }
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }
}
