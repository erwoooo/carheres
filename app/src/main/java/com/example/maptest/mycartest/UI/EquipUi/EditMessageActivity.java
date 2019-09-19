package com.example.maptest.mycartest.UI.EquipUi;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maptest.mycartest.Entity.Editequip;
import com.example.maptest.mycartest.New.EditResultBean;
import com.example.maptest.mycartest.New.LocationListBean;
import com.example.maptest.mycartest.New.NewHttpUtils;
import com.example.maptest.mycartest.New.WeiboDialogUtils;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.BaseActivity;
import com.example.maptest.mycartest.Utils.CheckNumBer;
import com.example.maptest.mycartest.Utils.OnclickListeners;
import com.example.maptest.mycartest.Utils.http.ResponseCallback;
import com.google.gson.Gson;

/**
 * Created by ${Author} on 2017/3/21.
 * Use to 修改用户资料
 */

public class EditMessageActivity extends BaseActivity implements View.OnClickListener {
    private EditText textView_name, textView_carnumber;
    private TextView textView_complete,textView_tell;
    private ImageView imageView_quit;
    private LocationListBean useBean;
    private String name, number;
    Editequip editequip;
    private String temId;
    private Dialog dialog;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    useBean.setNickname(name);
                    useBean.setCarNumber(number);
                    reDate();
                    Toast.makeText(getApplicationContext(),"修改成功",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(),"修改失败，车牌号重复",Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(),"修改失败",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desinequip);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        initView();
        initData();
        initClick();
    }

    private void initClick() {
        textView_complete.setOnClickListener(this);
        imageView_quit.setOnClickListener(this);
        textView_complete.setOnClickListener(new OnclickListeners() {
            @Override
            protected void onNoDoubleClick(View v) {
                if (textView_name.getText().toString().isEmpty() && (textView_carnumber.getText().toString().isEmpty() || CheckNumBer.carNumber(textView_carnumber.getText().toString()) )){
                    Toast.makeText(getApplicationContext(),"请检查修改的内容是否合法或者为空",Toast.LENGTH_SHORT).show();
                }else {
                    dialog = WeiboDialogUtils.createLoadingDialog(EditMessageActivity.this,"修改中");
                    updateDate();
                }
            }
        });
    }


    private void initView() {
        imageView_quit = (ImageView) findViewById(R.id.image_desinequipquits);
        useBean = (LocationListBean) getIntent().getExtras().getSerializable(AppCons.TEST_INT);
        textView_complete = (TextView) findViewById(R.id.text_complete);
        textView_carnumber = (EditText) findViewById(R.id.text_plateno);
        textView_name = (EditText) findViewById(R.id.text_desinequipname);
        textView_tell = (TextView) findViewById(R.id.text_desinequiptell);
        temId = useBean.getTerminalID();
        Log.d("mmmmmm",temId);
    }

    /**
     * 显示用户信息
     */
    private void initData() {
        textView_name.setText(useBean.getNickname());
        textView_carnumber.setText(useBean.getCarNumber() + "");
        textView_tell.setText(useBean.getDevice().getDeviceNumber() + "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.text_complete:        //正则表达式判断字符串格式
//                if (textView_name.getText().toString().isEmpty() && (textView_carnumber.getText().toString().isEmpty() || CheckNumBer.carNumber(textView_carnumber.getText().toString()) )){
//                    Toast.makeText(getApplicationContext(),"请检查修改的内容是否合法或者为空",Toast.LENGTH_SHORT).show();
//                }else {
//                    updateDate();
//                }
//                break;
            case R.id.image_desinequipquits:
                directurn();        //直接返回
                break;
        }
    }
    /**
     * 〈更新用户资料〉
     * 〈将修改的用户信息发回到上一个界面显示〉
     */
    private void updateDate() {
        name = textView_name.getText().toString();
        number = textView_carnumber.getText().toString();
        if (name.equals(useBean.getNickname()) && number.equals(useBean.getCarNumber())){
            Toast.makeText(getApplicationContext(),"没有修改资料",Toast.LENGTH_SHORT).show();
        }else {
            if (!name.equals(useBean.getNickname()) && !number.equals(useBean.getCarNumber())){
                editequip = new Editequip(useBean.getId(),name,number);
            }else if (!name.equals(useBean.getNickname()) && number.equals(useBean.getCarNumber())){
                editequip = new Editequip(useBean.getId(),name);
            }else if (name.equals(useBean.getNickname()) && !number.equals(useBean.getCarNumber())){
                editequip = new Editequip(number,useBean.getId());
            }
            Gson gson = new Gson();
            String obj = gson.toJson(editequip);
            NewHttpUtils.modifyMessage(obj,EditMessageActivity.this,messageCallback);
        }


    }

    private ResponseCallback messageCallback = new ResponseCallback() {
        @Override
        public void TaskCallBack(Object object) {
            if (object != null){
                EditResultBean resultBean = (EditResultBean) object;
                Log.e("Object",resultBean.toString());
                if (resultBean.getMeta().getMessage().equals("200")){
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                }else if (resultBean.getMeta().getMessage().equals("252")){
                    Toast.makeText(getApplicationContext(),"用户昵称重复",Toast.LENGTH_SHORT).show();
                }else if (resultBean.getMeta().getMessage().equals("251")){
                    Toast.makeText(getApplicationContext(),"用户名重复",Toast.LENGTH_SHORT).show();
                }
                else if (resultBean.getMeta().getMessage().equals("253")){
                    Toast.makeText(getApplicationContext(),"车牌号重复",Toast.LENGTH_SHORT).show();
                }
                else {
                    Message message = new Message();
                    message.what = 3;
                    handler.sendMessage(message);
                }
            }else {
                Message message = new Message();
                message.what = 3;
                handler.sendMessage(message);
            }
            if (dialog != null)
                WeiboDialogUtils.closeDialog(dialog);
        }

        @Override
        public void FailCallBack(Object object) {
            Message message = new Message();
            message.what = 3;
            handler.sendMessage(message);
            if (dialog != null)
                WeiboDialogUtils.closeDialog(dialog);
        }
    };

    private void reDate(){
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppCons.TEST_INT, useBean);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(0, intent);
        finish();
    }

    /**
     * 〈没有修改资料〉
     * 〈直接返回〉
     */
    private void directurn() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppCons.TEST_INT, useBean);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(0, intent);
        temId = null;
        number = null;
        name = null;
        handler.removeCallbacksAndMessages(null);
        finish();
        System.gc();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            directurn();
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        temId = null;
        number = null;
        name = null;

    }

}
