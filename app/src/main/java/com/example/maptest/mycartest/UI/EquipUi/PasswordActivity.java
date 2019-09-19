package com.example.maptest.mycartest.UI.EquipUi;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maptest.mycartest.Entity.EditPass;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.UI.CarContralActivity;
import com.example.maptest.mycartest.UI.MainActivity;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.BaseActivity;
import com.example.maptest.mycartest.Utils.CheckNumBer;
import com.example.maptest.mycartest.New.NewHttpUtils;
import com.example.maptest.mycartest.Utils.http.ResponseCallback;
import com.example.maptest.mycartest.test.LoginDataBean;
import com.google.gson.Gson;

import static com.example.maptest.mycartest.Utils.AppCons.EDIT;
import static com.example.maptest.mycartest.Utils.AppCons.loginDataBean;

/**
 * Created by ${Author} on 2017/3/21.
 * Use to修改密码
 */

public class PasswordActivity extends BaseActivity implements View.OnClickListener {
    private ImageView imageView_quit;
    private String psd1, psd2, pwd;
    private EditText editText1, editText2;
    private Button button;

    private String temId;
    private String agtId;
    private LinearLayout lin_pwd_sucs,lin_pwd_edit;
    private TextView text_re_login;
    private CarContralActivity activity = CarContralActivity.instance;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    Toast.makeText(PasswordActivity.this,"修改密码失败",Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(PasswordActivity.this,"修改密码成功",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(PasswordActivity.this,"旧密码错误",Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    lin_pwd_edit.setVisibility(View.GONE);
                    lin_pwd_sucs.setVisibility(View.VISIBLE);
                    break;
                case 5:
                    Toast.makeText(getApplicationContext(),msg.obj.toString(),Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏


        initView();
        initClick();
    }

    private void initClick() {
        imageView_quit.setOnClickListener(this);
        button.setOnClickListener(this);
        text_re_login.setOnClickListener(this);
    }

    private void initView() {
        lin_pwd_sucs = findViewById(R.id.lin_pwd_sucs);
        lin_pwd_edit = findViewById(R.id.lin_pwd_edit);
        imageView_quit = (ImageView) findViewById(R.id.image_quitpass);
        editText1 = (EditText) findViewById(R.id.edit_once);
        editText2 = (EditText) findViewById(R.id.edit_twice);
        button = (Button) findViewById(R.id.button_editpass);
        text_re_login = findViewById(R.id.text_re_login);
        psd1 = editText1.getText().toString();
        psd2 = editText2.getText().toString();
        editText1.setFilters(new InputFilter[]{filter});
        editText2.setFilters(new InputFilter[]{filter});
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_quitpass:
                if (EDIT){
                    exit();
                }else
                quitTofinsh();
                break;
            case R.id.button_editpass:      //正则表达式检测字符串格式
                if (editText1.getText().toString().isEmpty() || editText2.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"密码不能为空",Toast.LENGTH_SHORT).show();
                }
                else if (CheckNumBer.rexCheckPassword(editText2.getText().toString())){
                    postPsd();
                }else {
                    Toast.makeText(getApplicationContext(),"请输入6-20位规范密码",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.text_re_login:
                    exit();
                break;
        }
    }

    public void exit(){
        EDIT = false;
        activity.unLogin();
        activity = null;
        finish();

    }
    /**
     * 〈修改密码〉
     * 〈功能详细描述〉
     * 判断密码，修改密码
     */
    private void postPsd() {
        EditPass editPass = new EditPass(loginDataBean.getData().getId(),loginDataBean.getData().getRole(),editText2.getText().toString(),loginDataBean.getData().getUsername());
        Gson gson=new Gson();
        String obj = gson.toJson(editPass);
        NewHttpUtils.modifyMessage(obj,PasswordActivity.this,passwordCallback);

    }
    private ResponseCallback passwordCallback = new ResponseCallback() {
        @Override
        public void TaskCallBack(Object object) {
            if (object != null && object.toString().contains("200")){
                Toast.makeText(PasswordActivity.this,"修改密码成功",Toast.LENGTH_SHORT).show();
                Message message = new Message();
                message.what = 4;
                handler.sendMessage(message);
            }else {
                Toast.makeText(PasswordActivity.this,"修改密码失败",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void FailCallBack(Object object) {
            Toast.makeText(PasswordActivity.this,"修改密码失败",Toast.LENGTH_SHORT).show();
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void quitTofinsh(){
        psd1 = null;
        psd2 = null;
        pwd = null;
        temId = null;
        handler.removeCallbacksAndMessages(null);
        handler = null;
        finish();
        System.gc();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (EDIT){
                exit();
            }else
            quitTofinsh();
        }
        return false;

    }

    //禁止输入空格和回车
    public InputFilter filter=new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if(source.equals(" ")||source.toString().contentEquals("\n"))return "";
            else return null;
        }
    };
}
