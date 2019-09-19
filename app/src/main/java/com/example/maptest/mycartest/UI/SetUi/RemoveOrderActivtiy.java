package com.example.maptest.mycartest.UI.SetUi;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.maptest.mycartest.Adapter.SpinnerAdapter;
import com.example.maptest.mycartest.Bean.CommandResponse;
import com.example.maptest.mycartest.Bean.SpineBean;
import com.example.maptest.mycartest.New.NewHttpUtils;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.UI.SetUi.service.K100B;
import com.example.maptest.mycartest.UI.SetUi.service.PostCommand;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.ButtonUtils;
import com.example.maptest.mycartest.Utils.TcpSocketClient;
import com.example.maptest.mycartest.Utils.http.GetCommIdService;
import com.example.maptest.mycartest.Utils.http.ResponseCallback;
import com.google.gson.Gson;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * Created by ${Author} on 2017/11/29.
 * Use to
 */

public class RemoveOrderActivtiy extends AppCompatActivity implements View.OnClickListener{
    private ImageView imageView1, imageView2, imageView3;
    private RelativeLayout relativeLayout1, relativeLayout2, relativeLayout3;
    private int index = 0;
    private Button button1;
    private String commId;
    private ImageView image_quit_set;
    private Spinner spinner_remove;
    private List<SpineBean>list;
    private SpinnerAdapter adapter;
    private int type = 0;
    private boolean check = true;
    private CommandResponse commandResponse;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            PostCommand command = new PostCommand(AppCons.locationListBean.getTerminalID(),commId,AppCons.loginDataBean.getData().getUsername(),commandResponse.getContent());
            NewHttpUtils.postCommand(new String(new Gson().toJson(command)),RemoveOrderActivtiy.this,null);
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove);
        initView();
    }

    private void initView() {

        image_quit_set = (ImageView) findViewById(R.id.image_quit_set);
        image_quit_set.setOnClickListener(this);
        imageView1 = (ImageView) findViewById(R.id.image_lixian1);
        imageView2 = (ImageView) findViewById(R.id.image_lixian2);
        imageView3 = (ImageView) findViewById(R.id.image_lixian3);

        relativeLayout1 = (RelativeLayout) findViewById(R.id.relative_lixian1);
        relativeLayout2 = (RelativeLayout) findViewById(R.id.relative_lixian2);
        relativeLayout3 = (RelativeLayout) findViewById(R.id.relative_lixian3);

        relativeLayout1.setOnClickListener(this);
        relativeLayout2.setOnClickListener(this);
        relativeLayout3.setOnClickListener(this);

        spinner_remove = (Spinner) findViewById(R.id.spinner_remove);
        list = new ArrayList<>();
        SpineBean bean1 = new SpineBean("平台");
        SpineBean bean2 = new SpineBean("平台 + 短信");
        SpineBean bean3 = new SpineBean("平台 + 短信 + 电话");
        SpineBean bean4 = new SpineBean("平台 + 电话");
        list.add(bean1);
        list.add(bean2);
        list.add(bean3);
        list.add(bean4);
        adapter = new SpinnerAdapter(list,this);
        button1 = (Button) findViewById(R.id.button_lixian);
        button1.setOnClickListener(this);
        spinner_remove.setAdapter(adapter);
        spinner_remove.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (AppCons.ORDERBEN != null){
            if (AppCons.ORDERBEN.isDismantle()){
                lixianOrder1();
                check = true;
            }else {
                lixianOrder2();
                check = false;
            }
            spinner_remove.setSelection(AppCons.ORDERBEN.getDisplacementType());
            type = AppCons.ORDERBEN.getDisplacementType();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.relative_lixian1:
                lixianOrder1();
                break;
            case R.id.relative_lixian2:
                lixianOrder2();
                break;
            case R.id.relative_lixian3:
                lixianOrder3();
                break;
            case R.id.button_lixian:
                Log.e("INDEX", index + "");
                if (!ButtonUtils.isFastDoubleClick(R.id.button_lixian)) {
                    //写你相关操作即可
                        Log.e("INDEX1", index + "");
                        try {
                            sendDate();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                }

                break;
            case R.id.image_quit_set:
                quitSet();
                break;
        }
    }

    private void lixianOrder3() {
        check = false;
        index = 2;
        spinner_remove.setVisibility(View.GONE);
        imageView1.setVisibility(View.GONE);
        imageView2.setVisibility(View.GONE);
        imageView3.setVisibility(View.VISIBLE);
    }

    private void lixianOrder2() {
        check = false;
        index = 1;
        spinner_remove.setVisibility(View.GONE);
        imageView1.setVisibility(View.GONE);
        imageView3.setVisibility(View.GONE);
        imageView2.setVisibility(View.VISIBLE);


    }

    private void lixianOrder1() {
        check = true;
        index = 0;
        spinner_remove.setVisibility(View.VISIBLE);
        imageView1.setVisibility(View.VISIBLE);
        imageView2.setVisibility(View.GONE);
        imageView3.setVisibility(View.GONE);


    }


        /**
     * @throws IOException
     * @throws InterruptedException 发送离线指令
     */
    private void sendDate() throws IOException, InterruptedException {

            switch (index) {
                case 0:
                    switch (type){
                        case 0:
                            commId = "RMV,ON,0#";
                            break;
                        case 1:
                            commId = "RMV,ON,1#";
                            break;
                        case 2:
                            commId = "RMV,ON,2#";
                            break;
                        case 3:
                            commId = "RMV,ON,3#";
                            break;
                    }
                    break;
                case 1:
                    commId = "RMV,OFF#";
                    break;
                case 2:
                    commId = "RMV,CLR#";
                    break;
            }

        JSONObject object = new JSONObject();
        object.put("terminalID", AppCons.locationListBean.getTerminalID());              //设备的IEMI号
        object.put("deviceProtocol", AppCons.locationListBean.getLocation().getDeviceProtocol());      //设备协议号
        object.put("content", commId);                 //指令
        NewHttpUtils.sendOrder(object.toJSONString(), RemoveOrderActivtiy.this, new ResponseCallback() {
            @Override
            public void TaskCallBack(Object object) {
                commandResponse = (CommandResponse) object;
                String data = commandResponse.getContent();
                if (data != null){
                    if (data.contains("OK") || data.contains("ok") || data.contains("Success") || data.contains("successfully") || data.contains("Already")) {
                        Toast.makeText(getApplicationContext(), "设置成功", Toast.LENGTH_SHORT).show();
                        postOrder();        //指令收到回应成功后，再保存指令到服务器

                    } else if (data.contains("timeout")) {
                        Toast.makeText(getApplicationContext(), "请求超时", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "设置失败", Toast.LENGTH_SHORT).show();

                    }
                    handler.sendEmptyMessage(0);
                }else {
                    Toast.makeText(getApplicationContext(), "设置失败", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void FailCallBack(Object object) {
                Toast.makeText(getApplicationContext(), "设置失败", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void postOrder() {
        if (AppCons.ORDERBEN == null){
            AppCons.ORDERBEN = new K100B();
            AppCons.ORDERBEN.setTerminalID(AppCons.locationListBean.getTerminalID());
        }
        AppCons.ORDERBEN.setDismantle(check);
        AppCons.ORDERBEN.setDisplacementType(type);
        AppCons.ORDERBEN.setDeviceProtocol(AppCons.locationListBean.getLocation().getDeviceProtocol());

        Gson gson = new Gson();
        String obj = gson.toJson(AppCons.ORDERBEN);
        NewHttpUtils.saveCommand(obj, getApplicationContext(), new ResponseCallback() {
            @Override
            public void TaskCallBack(Object object) {
                Log.e("Object",object.toString());
                if ((object.toString().contains("true") || object.toString().contains("200")) && AppCons.ORDERBEN != null){


                }
            }

            @Override
            public void FailCallBack(Object object) {
                Log.e("Object",object.toString());
            }
        });

    }

    private void quitSet() {
            finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
