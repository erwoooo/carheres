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
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.maptest.mycartest.Entity.EditName;
import com.example.maptest.mycartest.New.EditResultBean;
import com.example.maptest.mycartest.New.NewHttpUtils;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.BaseActivity;
import com.example.maptest.mycartest.Utils.http.ResponseCallback;
import com.example.maptest.mycartest.test.LoginDataBean;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;

import static com.example.maptest.mycartest.Utils.AppCons.loginDataBean;

/**
 * Created by ${Author} on 2017/6/26.
 * Use to
 */

public class EditAccountActivity extends BaseActivity implements View.OnClickListener{
    private ImageView imageView_quit;
    private Button button_sure;
    private EditText editText_account;
    public static RequestQueue queue;
    private String account,temId,agenId,URL;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Toast.makeText(getApplicationContext(),"修改成功",Toast.LENGTH_SHORT).show();
                    reMation();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(),"修改失败",Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(),"修改失败,用户名已有人使用",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        setContentView(R.layout.activity_account);
        initView();
        initClick();
    }

    private void initClick() {
        imageView_quit.setOnClickListener(this);
        button_sure.setOnClickListener(this);
    }

    private void initView() {
        imageView_quit = (ImageView) findViewById(R.id.image_quitaccout);
        button_sure = (Button) findViewById(R.id.button_editcount);
        editText_account = (EditText) findViewById(R.id.edit_account);
        editText_account.setFilters(new InputFilter[]{filter});  //不能输入空格和回车
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_editcount:
                if (editText_account.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"账号不能为空",Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        editAccout();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.image_quitaccout:

                quitSet();
                break;
        }
    }

    /**
     * 修改资料成功
     */
    private void editAccout() throws UnsupportedEncodingException {

        account = editText_account.getText().toString();
        if (account.equals(AppCons.loginDataBean.getData().getUsername())){
            Toast.makeText(getApplicationContext(),"用户名未发送修改",Toast.LENGTH_SHORT).show();
        }else {
            EditName jsonObject = new EditName(AppCons.loginDataBean.getData().getId(),AppCons.loginDataBean.getData().getRole(),account);
            Gson gson=new Gson();
            String obj = gson.toJson(jsonObject);
            NewHttpUtils.modifyMessage(obj,EditAccountActivity.this,accountCallback);
        }

    }

    private ResponseCallback accountCallback = new ResponseCallback() {
        @Override
        public void TaskCallBack(Object object) {
            if (object != null){
                EditResultBean bean = (EditResultBean) object;
                if (bean.getMeta().getMessage().equals("200")){ //成功
                    AppCons.loginDataBean.getData().setUsername(account);
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                }else if (bean.getMeta().getMessage().equals("251")){ //重复
                    Message message = new Message();
                    message.what = 3;
                    handler.sendMessage(message);
                }else {
                    Message message = new Message();
                    message.what = 2;
                    handler.sendMessage(message);
                }
            }else {
                Message message = new Message();
                message.what = 2;
                handler.sendMessage(message);
            }
        }

        @Override
        public void FailCallBack(Object object) {
            Message message = new Message();
            message.what = 2;
            handler.sendMessage(message);
        }
    };
    /**
     * 直接返回
     */
    private void quitSet() {
        Intent intent = new Intent();
        setResult(99, intent);
        account = null;
        agenId = null;
        temId = null;
        queue = null;
        if (handler != null){
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        finish();
    }

    /**
     * 返回给前一个界面显示
     */
    private void reMation(){
        account = editText_account.getText().toString();
        Bundle bundle = new Bundle();
        bundle.putString("acct",account);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(99,intent);
        finish();
    }



    /*不能输入空格和回车*/
    public InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source.equals(" ") || source.toString().contentEquals("\n")) return "";
            else return null;
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
                quitSet();
         }
        return false;

    }
}
