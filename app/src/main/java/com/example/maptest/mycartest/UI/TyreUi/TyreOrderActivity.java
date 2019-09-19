package com.example.maptest.mycartest.UI.TyreUi;

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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.maptest.mycartest.Bean.CommandResponse;
import com.example.maptest.mycartest.New.NewHttpUtils;
import com.example.maptest.mycartest.New.WeiboDialogUtils;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.UI.SetUi.service.K100B;
import com.example.maptest.mycartest.UI.SetUi.service.PostCommand;
import com.example.maptest.mycartest.UI.SetUi.service.TirmeredBean;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.BaseActivity;
import com.example.maptest.mycartest.Utils.http.ResponseCallback;
import com.google.gson.Gson;

/**
 * Created by ${Author} on 2017/3/7.
 * Use to 胎压指令
 */

public class TyreOrderActivity extends BaseActivity implements View.OnClickListener{
    private RelativeLayout relativeLayout_temp,relativeLayout_tyre,relativeLayout_learn,relativeLayout_qury,relativeLayout_change,relativeLayout_default,relativeLayout_cancel;
    private ImageView imageView_tyreorderquit;

    private Bundle bundle = new Bundle();
    private String commId;

    private String temId;

    private int order = 0;
    private Dialog dialog;
    CommandResponse commandResponse;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 11){
                if (commandResponse != null){
                    PostCommand command = new PostCommand(temId,commId,AppCons.loginDataBean.getData().getUsername(),commandResponse.getContent());
                    NewHttpUtils.postCommand(new String(new Gson().toJson(command)),TyreOrderActivity.this,null);
                }else {
                    PostCommand command = new PostCommand(temId,commId,AppCons.loginDataBean.getData().getUsername());
                    NewHttpUtils.postCommand(new String(new Gson().toJson(command)),TyreOrderActivity.this,null);
                }
                commandResponse = null;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tyreorder);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        temId = (String) getIntent().getExtras().getSerializable(AppCons.TEST_TYR);

        initView();
        initClick();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.relative_temps:
                startActivity(new Intent(TyreOrderActivity.this,TempsActivity.class).putExtras(bundle));    //去温度界面
                break;
            case R.id.relative_tyre:
                startActivity(new Intent(TyreOrderActivity.this,TyreValueActivity.class).putExtras(bundle));    //去胎压界面
                break;
            case R.id.relative_learn:
                startActivity(new Intent(TyreOrderActivity.this,LearnActivity.class).putExtras(bundle));    //学习指令
                break;
            case R.id.relative_qury:
                startActivity(new Intent(TyreOrderActivity.this,QuerryActivity.class).putExtras(bundle));   //检测硬件
                break;
            case R.id.relative_change:
                startActivity(new Intent(TyreOrderActivity.this,ChangeTireActivity.class).putExtras(bundle));   //轮胎交换
                break;
            case R.id.relative_default:
                order = 0;
                try {
                    sendTyreOrder();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.image_tyreorderquit:
               quitOrder();
                break;
            case R.id.relative_cancel:
                order = 1;
                try {
                    sendTyreOrder();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    private void initView() {
        bundle.putSerializable(AppCons.TEST_TYR,temId);
        relativeLayout_temp = (RelativeLayout) findViewById(R.id.relative_temps);
        relativeLayout_tyre = (RelativeLayout) findViewById(R.id.relative_tyre);
        relativeLayout_learn = (RelativeLayout) findViewById(R.id.relative_learn);
        relativeLayout_qury = (RelativeLayout) findViewById(R.id.relative_qury);
        relativeLayout_change = (RelativeLayout) findViewById(R.id.relative_change);
        relativeLayout_default = (RelativeLayout) findViewById(R.id.relative_default);
        relativeLayout_cancel = (RelativeLayout) findViewById(R.id.relative_cancel);
        imageView_tyreorderquit = (ImageView) findViewById(R.id.image_tyreorderquit);
        dialog = WeiboDialogUtils.createLoadingDialog(TyreOrderActivity.this,"正在加载");
        TirmeredBean bean = new TirmeredBean(temId,1);
        Gson gson = new Gson();
        String obj = gson.toJson(bean);
        NewHttpUtils.findCommand(obj, getApplicationContext(), new ResponseCallback() {
            @Override
            public void TaskCallBack(Object object) {
                if (object !=null){
                    AppCons.ORDERBEN = JSONObject.parseObject((String) object,K100B.class);
                    if (dialog != null)
                        WeiboDialogUtils.closeDialog(dialog);
                }else {
                    if (dialog != null)
                        WeiboDialogUtils.closeDialog(dialog);
                }

            }
            @Override
            public void FailCallBack(Object object) {
                if (dialog != null)
                    WeiboDialogUtils.closeDialog(dialog);
                Log.e("FailCallBack",object.toString());
            }
        });
    }

    private void initClick() {
        relativeLayout_temp.setOnClickListener(this);
        relativeLayout_tyre.setOnClickListener(this);
        relativeLayout_learn.setOnClickListener(this);
        relativeLayout_qury.setOnClickListener(this);
        relativeLayout_change.setOnClickListener(this);
        relativeLayout_default.setOnClickListener(this);
        imageView_tyreorderquit.setOnClickListener(this);
        relativeLayout_cancel.setOnClickListener(this);
    }

    /**
     * @throws InterruptedException
     * 恢复默认设置
     */
    private void sendTyreOrder() throws InterruptedException {
        switch (order){
            case 0:
                commId = "TPMS_F#";         //恢复默认设置
                break;
            case 1:
                commId = "TPMS_CONFIRM#";     //取消报警
                break;
        }

        JSONObject object = new JSONObject();
        object.put("terminalID", temId);
        object.put("deviceProtocol", 4);      //设备协议号
//        object.put("CommandType", true);
        object.put("content", commId);


        NewHttpUtils.sendOrder(object.toJSONString(), TyreOrderActivity.this, new ResponseCallback() {
            @Override
            public void TaskCallBack(Object object) {
                commandResponse = (CommandResponse) object;
                String data = (String) commandResponse.getContent();
                if (data != null){
                    if (data.contains("OK") || data.contains("ok") || data.contains("Success") || data.contains("successfully") || data.contains("Already")) {
                        Toast.makeText(getApplicationContext(), "设置成功", Toast.LENGTH_SHORT).show();
                    } else if (data.contains("timeout")) {
                        Toast.makeText(getApplicationContext(), "请求超时", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "设置失败", Toast.LENGTH_SHORT).show();

                    }
                }else {
                    Toast.makeText(getApplicationContext(), "设置失败", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void FailCallBack(Object object) {
                Toast.makeText(getApplicationContext(), "设置超时", Toast.LENGTH_SHORT).show();
            }
        });
       Message message = new Message();
        message.what = 11;
        handler.sendMessage(message);
    }

    private void quitOrder(){

        commId = null;
        bundle = null;

        temId = null;
        finish();
        System.gc();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.e("tyreorder","onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("tyreorder","onRestart");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.e("tyreorder","onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.e("tyreorder","onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        AppCons.ORDERBEN = null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode){
            quitOrder();
        }
        return super.onKeyDown(keyCode, event);
    }
}
