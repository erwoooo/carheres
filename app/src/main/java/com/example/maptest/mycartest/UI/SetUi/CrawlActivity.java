package com.example.maptest.mycartest.UI.SetUi;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.maptest.mycartest.Bean.CommandResponse;
import com.example.maptest.mycartest.New.NewHttpUtils;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.UI.SetUi.service.K100B;
import com.example.maptest.mycartest.UI.SetUi.service.PostCommand;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.BaseActivity;
import com.example.maptest.mycartest.Utils.TcpSocketClient;
import com.example.maptest.mycartest.Utils.UtcDateChang;
import com.example.maptest.mycartest.Utils.http.ResponseCallback;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ${Author} on 2017/3/29.
 * Use to 设置电子围栏
 */

public class CrawlActivity extends BaseActivity implements View.OnClickListener {
    private ImageView imageView_set;
    private Button button_delete;
    private ImageView imageView_quit;
    private RelativeLayout relativeLayout;
    private Bundle bundle;
    private ImageView imageView_add;
    private TextView textView_name, textView_type, textView_time;
    private LinearLayout linearLayout_weilan;
    private TcpSocketClient socketClient;
    private String commId;
    private Activity activity = CrawlActivity.this;
    private JSONObject objectorder;
    private Map<String,Object> map = new HashMap<>();
    CommandResponse commandResponse;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    break;
                case 5:
                    linearLayout_weilan.setVisibility(View.GONE);
                    break;
                case 11:
                    if (commandResponse != null){
                        PostCommand command = new PostCommand(AppCons.locationListBean.getTerminalID(),commId,AppCons.loginDataBean.getData().getUsername(),commandResponse.getContent());
                        NewHttpUtils.postCommand(new String(new Gson().toJson(command)),CrawlActivity.this,null);
                    }else {
                        PostCommand command = new PostCommand(AppCons.locationListBean.getTerminalID(),commId,AppCons.loginDataBean.getData().getUsername());
                        NewHttpUtils.postCommand(new String(new Gson().toJson(command)),CrawlActivity.this,null);
                    }
                    commandResponse = null;
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elecrawl);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        initView();
        reQuestCenter();
        initClick();

    }

    /**
     * 初始化点击事件
     */
    private void initClick() {
        imageView_set.setOnClickListener(this);
        button_delete.setOnClickListener(this);
        imageView_quit.setOnClickListener(this);
        relativeLayout.setOnClickListener(this);
        imageView_add.setOnClickListener(this);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        socketClient = TcpSocketClient.getInstance();
        linearLayout_weilan = (LinearLayout) findViewById(R.id.linerlayout_wei);
        textView_name = (TextView) findViewById(R.id.text_weiname);
        textView_type = (TextView) findViewById(R.id.text_weitype);
        textView_time = (TextView) findViewById(R.id.text_weitime);
        imageView_add = (ImageView) findViewById(R.id.add_crawl);
        bundle = new Bundle();
        button_delete = (Button) findViewById(R.id.button_crawl_delete);
        imageView_set = (ImageView) findViewById(R.id.image_setcrawl);
        imageView_quit = (ImageView) findViewById(R.id.image_quitcrawl);
        relativeLayout = (RelativeLayout) findViewById(R.id.relative_editcraw);


    }

    /**
     * @param v
     * 每个view的点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_crawl:
                startActivity(new Intent(CrawlActivity.this, SetCrawlActivity.class));        //设置新的围栏
                break;
            case R.id.image_setcrawl:
                break;
            case R.id.image_quitcrawl:
                quitSet();                              //退出
                break;
            case R.id.button_crawl_delete:              //删除围栏
                try {
                    sendDate();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.relative_editcraw:
                startActivity(new Intent(CrawlActivity.this, ShowCrawlActivity.class));               //显示当前围栏
                break;
        }
    }

    private void quitSet() {
        if (socketClient != null)
            socketClient = null;
        commId = null;
        bundle = null;
        activity = null;
        objectorder = null;
        handler.removeCallbacksAndMessages(null);
        handler = null;
        button_delete = null;
        imageView_add = null;
        imageView_quit = null;
        imageView_set = null;
        linearLayout_weilan = null;
        relativeLayout = null;
        textView_name = null;
        textView_time = null;
        textView_type = null;
        finish();


    }

    /**
     * 〈发送指令〉
     * 〈功能详细描述〉
     *
     * @exception/throws [输入输出异常] [违例说明]
     * 关闭电子围栏指令
     */
    private void sendDate() throws IOException, InterruptedException {
        commId = "FENCE,OFF#";                      //删除围栏指令内容
        objectorder = new JSONObject();
        objectorder.put("terminalID", AppCons.locationListBean.getTerminalID());              //设备的IEMI号
        objectorder.put("deviceProtocol", AppCons.locationListBean.getLocation().getDeviceProtocol());      //设备协议号
        objectorder.put("content", commId);              //指令
        NewHttpUtils.sendOrder(objectorder.toJSONString(), CrawlActivity.this, new ResponseCallback() {
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
                }else {
                    Toast.makeText(getApplicationContext(), "设置失败", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void FailCallBack(Object object) {
                Toast.makeText(getApplicationContext(), "设置失败", Toast.LENGTH_SHORT).show();
            }
        });
        Message message = new Message();
        message.what = 11;
        handler.sendMessage(message);
    }

    /**
     * @param keyCode
     * @param event
     * @return 重写返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            quitSet();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 在重新获取焦点处，请求数据
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("actity", "onRestart");
        reQuestCenter();
    }

    /**
     * 〈获取电子围栏信息〉
     * 〈功能详细描述〉
     * 进入界面，请求电子围栏信息，展示给用户
     */
    private void reQuestCenter() {
      if (AppCons.ORDERBEN != null){
          Log.e("围栏状态",AppCons.ORDERBEN.isFenceState() + "");
          if (AppCons.ORDERBEN.isFenceState()){
              linearLayout_weilan.setVisibility(View.VISIBLE);
              switch (AppCons.ORDERBEN.getFenceModel()){
                  case 0:
                      textView_type.setText("围栏类型:进围栏");
                      break;
                  case 1:
                      textView_type.setText("围栏类型:出围栏");
                      break;
                  case 2:
                      textView_type.setText("围栏类型:进出围栏");
                      break;
              }
              textView_name.setText(AppCons.ORDERBEN.getFenceName());
              textView_time.setText(UtcDateChang.UtcDatetoLocaTime(AppCons.ORDERBEN.getFenceSetTime()));
          }else {
              linearLayout_weilan.setVisibility(View.GONE);
          }
      }

    }

    /**
     * 〈提交围栏信息到服务器〉
     * 〈功能详细描述〉
     * 删除电子围栏之后，提交空数据到服务器
     */
    private void postOrder() {
        if (AppCons.ORDERBEN == null){
            AppCons.ORDERBEN = new K100B();
            AppCons.ORDERBEN.setTerminalID(AppCons.locationListBean.getTerminalID());
            Log.e("新增",AppCons.ORDERBEN.toString());
        }
        AppCons.ORDERBEN.setFenceState(false);
        AppCons.ORDERBEN.setDeviceProtocol(AppCons.locationListBean.getLocation().getDeviceProtocol());
        Gson gson = new Gson();
        String obj = gson.toJson(AppCons.ORDERBEN);
        NewHttpUtils.saveCommand(obj, getApplicationContext(), new ResponseCallback() {
            @Override
            public void TaskCallBack(Object object) {
                Log.e("Object", object.toString());
                if ((object.toString().contains("true") || object.toString().contains("200")) ){
                    Message message = new Message();
                    message.what = 5;
                    handler.sendMessage(message);
                }
            }

            @Override
            public void FailCallBack(Object object) {

            }
        });

    }
}
