package com.example.maptest.mycartest.UI.SetUi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.maptest.mycartest.New.NewHttpUtils;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.UI.SetUi.service.PostCommand;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.ButtonUtils;
import com.example.maptest.mycartest.Utils.TcpSocketClient;
import com.example.maptest.mycartest.Utils.http.GetCommIdService;
import com.example.maptest.mycartest.Utils.http.ResponseCallback;
import com.google.gson.Gson;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ${Author} on 2017/11/29.
 * Use to
 */

public class SgexOrderActivtiy extends AppCompatActivity implements View.OnClickListener{
    private ImageView imageView1, imageView2, imageView3;
    private RelativeLayout relativeLayout1, relativeLayout2, relativeLayout3;
    private int index = 0,i = 0;
    private Button button1;
    public static RequestQueue queue;
    private JSONObject object, jsonObject1, defaultJson;
    private org.json.JSONObject objects = null;
    private org.json.JSONObject objectset = null;
    private String times;
    private LinearLayout linearLayout_order;
    private Map<String,Object> map = new HashMap<>();
    private String jsonOrder;
    private String commId;
        private TcpSocketClient socketClient;
    private ImageView image_quit_set;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sagex);
        initView();
        reQuestCenter();
    }

    private void initView() {
        socketClient = TcpSocketClient.getInstance();
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

        button1 = (Button) findViewById(R.id.button_lixian);
        button1.setOnClickListener(this);
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
        index = 2;
            imageView1.setVisibility(View.GONE);
            imageView2.setVisibility(View.GONE);
            imageView3.setVisibility(View.VISIBLE);
    }

    private void lixianOrder2() {
        index = 1;

            imageView1.setVisibility(View.GONE);
            imageView3.setVisibility(View.GONE);
            imageView2.setVisibility(View.VISIBLE);


    }

    private void lixianOrder1() {
        index = 0;

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
                    commId = "#152#01301##";
                    break;
                case 1:
                    commId = "#152#01300#0033600##";
                    break;
                case 2:
                    commId = "#152#01300#00709300000#003604800##";
                    break;
            }


        Log.d("index", index + "");
        Log.d("index", commId);
        JSONObject object = new JSONObject();
        object.put("terminalID", AppCons.locationListBean.getTerminalID());              //设备的IEMI号
        object.put("deviceProtocol", AppCons.locationListBean.getLocation().getDeviceProtocol());      //设备协议号
        object.put("content", commId);                 //指令

        PostCommand command = new PostCommand(AppCons.locationListBean.getTerminalID(),commId,AppCons.loginDataBean.getData().getUsername());
        NewHttpUtils.postCommand(new String(new Gson().toJson(command)),SgexOrderActivtiy.this,null);
        NewHttpUtils.sendOrder(object.toJSONString(), SgexOrderActivtiy.this, new ResponseCallback() {
            @Override
            public void TaskCallBack(Object object) {
                Toast.makeText(getApplicationContext(), "设置成功,机器下次连接系统时将再次发送该指令!", Toast.LENGTH_SHORT).show();
                postOrder();                //机器回应成功，保存指令到服务器
//                String data = (String) object;
//                if (data != null){
//                    if (data.contains("OK") || data.contains("ok") || data.contains("Success") || data.contains("successfully") || data.contains("Already")) {
//                        Toast.makeText(getApplicationContext(), "设置成功", Toast.LENGTH_SHORT).show();
//                        postOrder();        //指令收到回应成功后，再保存指令到服务器
//                    } else if (data.contains("timeout")) {
//                        Toast.makeText(getApplicationContext(), "请求超时", Toast.LENGTH_SHORT).show();
//
//                    } else {
//                        Toast.makeText(getApplicationContext(), "设置失败", Toast.LENGTH_SHORT).show();
//
//                    }
//                }else {
//                    Toast.makeText(getApplicationContext(), "设置失败", Toast.LENGTH_SHORT).show();
//                }

            }

            @Override
            public void FailCallBack(Object object) {
                Toast.makeText(getApplicationContext(), "设置失败", Toast.LENGTH_SHORT).show();
            }
        });
//        Thread.sleep(200);
//        socketClient.socketSend(object.toJSONString(), this);
//        Log.d("teeee", "发送的数据" + object.toJSONString());
//        socketClient.setOnConnectLinstener(new TcpSocketClient.ConnectLinstener() {
//            /**
//             * @param data
//             * 根据返回值判断指令设置是否成功，如果成功，保存到服务器
//             */
//            @Override
//            public void onReceiveData(String data) {
//                Log.d("oooo", data.toString());
//                if (data.contains("OK") || data.contains("ok") || data.contains("Success") || data.contains("successfully") || data.contains("Already")) {
//                    Toast.makeText(getApplicationContext(), "设置成功", Toast.LENGTH_SHORT).show();
//                    postOrder();
//                } else if (data.contains("timeout")) {
//                    Toast.makeText(getApplicationContext(), "请求超时", Toast.LENGTH_SHORT).show();
//
//                } else {
//                    Toast.makeText(getApplicationContext(), "设置失败", Toast.LENGTH_SHORT).show();
//
//                }
//            }
//        });
    }


    /**
     * 查询离线指令
     */
    private void reQuestCenter() {

        queue = Volley.newRequestQueue(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                object = new JSONObject();
                object.put("Setting.ReportTimeType", "1");
                JSON json = (JSON) JSONObject.toJSON(object);
                Log.d("ssss", "666" + json);
                String URL = "http://app.carhere.net/appGetCommandInfo?TerminalID=" + AppCons.locationListBean.getTerminalID() + "&Field=" + json;
                JsonObjectRequest objectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, URL, null, new Response.Listener<org.json.JSONObject>() {
                    @Override
                    public void onResponse(org.json.JSONObject jsonObject) {
                        Log.d("response1", jsonObject.toString());
                        try {
                            objects = (org.json.JSONObject) jsonObject.get("Data");
                            Log.d("resss", objects.toString());
                            objectset = (org.json.JSONObject) objects.get("Setting");
                            Log.d("resss", objectset.toString());
                            i = Integer.parseInt(objectset.get("ReportTimeType").toString());

                            Log.d("index", i + "");
                            switch (i) {
                                case 0:
                                    lixianOrder1();
                                    break;
                                case 1:

                                    lixianOrder2();
                                    break;
                                case 2:
                                    lixianOrder3();
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (jsonObject != null) {

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("response2", volleyError.toString());
                    }
                });
                queue.add(objectRequest);

            }
        }).start();

    }

    /**
     * 将离线指令保存到服务器
     */
    private void postOrder() {
        map.clear();
        map.put("Setting.ReportTimeType", index);
        jsonOrder = JSON.toJSONString(map);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://app.carhere.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetCommIdService service = retrofit.create(GetCommIdService.class);
        Call<ResponseBody> call = service.postOrder(AppCons.locationListBean.getTerminalID(),jsonOrder);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    Log.e("saveSagex",response.body().bytes().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    private void quitSet() {
        if (socketClient != null)
            socketClient = null;
        if (queue != null)
            queue = null;

        objects = null;
        objectset = null;
            finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
