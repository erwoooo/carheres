package com.example.maptest.mycartest.UI.EquipUi;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.maptest.mycartest.Bean.MyBean;
import com.example.maptest.mycartest.New.NewHttpUtils;
import com.example.maptest.mycartest.New.UpAgentBean;
import com.example.maptest.mycartest.Permission.PermissionFail;
import com.example.maptest.mycartest.Permission.PermissionGen;
import com.example.maptest.mycartest.Permission.PermissionSuccess;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.BaseActivity;
import com.example.maptest.mycartest.Utils.http.ResponseCallback;
import com.example.maptest.mycartest.test.LoginDataBean;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by ${Author} on 2017/3/21.
 * Use to 服务商
 */

public class ServiceActivity extends BaseActivity implements View.OnClickListener {
    private ImageView imageView_quit, imageView_call;
    private TextView textView_ser, textView_name, textView_tell, textView_addr;
    private UpAgentBean bean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_imforamtion);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏

        initView();
        findUpAgent();
        initClick();
        final List<Integer>listRrc = new ArrayList<>();
        List<Integer>list = new ArrayList<>();
        list.add(0);
        list.add(0);
        list.add(1);
        list.add(1);
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(4);
        list.add(4);
        list.add(5);
        list.add(6);
        list.add(7);
        list.add(8);
        list.add(9);
        list.add(0);
        list.add(0);
        list.add(0);
        list.add(0);
        list.add(0);
        list.add(11);
        list.add(11);
        list.add(11);
        list.add(11);
        list.add(12);
        list.add(12);
        list.add(12);
        list.add(13);
        list.add(14);
        list.add(14);
        list.add(14);
        list.add(14);
        list.add(15);
        for (int i = 0; i < list.size() ; i ++){
            if (i <  list.size() - 1){
                if (list.get(i) == list.get(i + 1)){

                }else {
                    listRrc.add(list.get(i));
                }
            }else {
                listRrc.add(list.get(list.size() - 1));
            }

        }

        for (Integer integer : listRrc){
            Log.e("最后的结果", integer + "");
        }
    }





    private void initClick() {
        imageView_quit.setOnClickListener(this);
        imageView_call.setOnClickListener(this);
    }

    private void initView() {
        textView_ser = (TextView) findViewById(R.id.text_servicewname);
        textView_name = (TextView) findViewById(R.id.text_conpeople);
        textView_tell = (TextView) findViewById(R.id.text_tellnumber);
        textView_addr = (TextView) findViewById(R.id.text_servicelocation);
        imageView_quit = (ImageView) findViewById(R.id.image_quitservice);
        imageView_call = (ImageView) findViewById(R.id.image_callnumber);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_quitservice:
                finish();
                break;
            case R.id.image_callnumber:
                PermissionGen.with(ServiceActivity.this)
                        .addRequestCode(100)
                        .permissions(
                                Manifest.permission.CALL_PHONE,
                                Manifest.permission.READ_CONTACTS,
                                Manifest.permission.RECEIVE_SMS,
                                Manifest.permission.WRITE_CONTACTS)
                        .request();

                break;
        }
    }

    private void findUpAgent(){
        Map<Object,Object>map = new HashMap<>();
        map.put("id",AppCons.loginDataBean.getData().getUser_id());
        Gson gson = new Gson();
        String obj = gson.toJson(map);
        NewHttpUtils.querrUpAgent(obj, this, new ResponseCallback() {
            @Override
            public void TaskCallBack(Object object) {
            if (object != null){
                bean = JSONObject.parseObject((String) object,UpAgentBean.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (bean != null){
                            textView_ser.setText(bean.getData().getUsername());
                            textView_name.setText(bean.getData().getLinkman());
                            textView_tell.setText(bean.getData().getTelephone());
                            textView_addr.setText(bean.getData().getAddress());
                        }
                    }
                });
            }
            }

            @Override
            public void FailCallBack(Object object) {

            }
        });
    }

    /**
     * 〈调用手机打电话功能〉
     * 〈功能详细描述〉
     */
    private void callNumber() {
        if (textView_tell.getText().length() < 8){

        }else {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + textView_tell.getText().toString()));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }else {
                startActivity(intent);
            }
        }
    }

    @PermissionSuccess(requestCode = 100)
    public void test(){
        callNumber();
    }

    @PermissionFail(requestCode = 100)
    private void test2() {

    }

    @PermissionSuccess(requestCode = 200)
    public void openCamera(){

    }

    @PermissionFail(requestCode = 200)
    public void failOpenCamera(){
        Toast.makeText(this, "Camera permission is not granted", Toast.LENGTH_SHORT).show();
    }

    @Override public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                                     int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
