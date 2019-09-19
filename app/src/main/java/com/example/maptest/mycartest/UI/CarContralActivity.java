package com.example.maptest.mycartest.UI;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maptest.mycartest.BuildConfig;
import com.example.maptest.mycartest.Entity.AppVersion;
import com.example.maptest.mycartest.Fragment.MapFragment;
import com.example.maptest.mycartest.New.NewHttpUtils;
import com.example.maptest.mycartest.New.WeiboDialogUtils;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.SwipRecycleView.SocketService;
import com.example.maptest.mycartest.UI.EquipUi.AboutActivity;
import com.example.maptest.mycartest.UI.EquipUi.EditAccountActivity;
import com.example.maptest.mycartest.UI.EquipUi.MyMessageActivity;
import com.example.maptest.mycartest.UI.EquipUi.PasswordActivity;
import com.example.maptest.mycartest.UI.EquipUi.ServiceActivity;
import com.example.maptest.mycartest.UI.warn.WarnListIemiActivity;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.BaseActivity;
import com.example.maptest.mycartest.Utils.ButtonUtils;
import com.example.maptest.mycartest.Utils.DownloadService;
import com.example.maptest.mycartest.Utils.FragmentController;
import com.example.maptest.mycartest.Utils.http.ResponseCallback;
import com.example.maptest.mycartest.test.LoginDataBean;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.common.Constants;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static com.example.maptest.mycartest.R.id.text_aboutcar;
import static com.example.maptest.mycartest.Utils.AppCons.DOWLOADURL;
import static com.example.maptest.mycartest.Utils.AppCons.PWD;
import static com.example.maptest.mycartest.Utils.AppCons.SOCKET_CUTOFF;
import static com.example.maptest.mycartest.Utils.AppCons.xgAccount;

/**
 * Created by Administrator on 2017/2/27.
 * 查车，胎压，列表宿主acttivty
 */

public class CarContralActivity extends BaseActivity implements View.OnClickListener {
    private Context context;
    private TextView textView_title, textView_service, textView_about;
    private TextView textView_look, textView_tyre, textView_carlists,textView_capacity;
    private TextView textView_account, textView_username, textView_phone, textView_addr;
    private LinearLayout linearLayout_look, linearLayout_tyre, linearLayout_carlist,linearLayout_capacity;
    private RelativeLayout relative_account;
    private ImageView imageView_lookcars, imageView_tyre, imageView_carlists, imageView_photots,imageView_capacity;
    private ImageView imageView_pass, imageView_changes, imageView_head, imageView_unlogin;
    public int changes = 1;
    private FragmentController controller;
    private DrawerLayout drawerLayout;
    private int agentId;
    private String terminalID;
    private Dialog dialog_pro;
    private Button button_edit;
    private String user, tell, addr,account;
    LoginDataBean loginDataBean;
    private MapFragment mapFragment;
    private Bitmap bm = null;
    private Drawable drawable;
    private BitmapDrawable bd;
    private boolean check;
    private long mExitTime;
    private Uri fileUri;
    MyReceiver myReceiver = new MyReceiver();
    public static CarContralActivity instance = null;
    private AlertDialog dialog;
    private View view_pwd;
    SocketService socketService = new SocketService();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            this.obtainMessage();
            switch (msg.what) {
                case 0:
                    break;
                case 1:
                    textView_title.setText("查车");
                    imageView_changes.setImageResource(R.drawable.icon_index_baojin);
                    break;
                case 2:
                    textView_title.setText("胎压");
                    imageView_changes.setImageResource(R.drawable.icon_taiya_search);
                    break;
                case 5:
                    textView_username.setText(user);
                    textView_phone.setText(tell);
                    textView_addr.setText(addr);
                    if (bm != null) {
                        imageView_photots.setImageBitmap(bm);
                    }
                    break;
                case 9:
                    textView_title.setText("车辆列表");
                    imageView_changes.setImageResource(R.drawable.icon_weiduxiaoxi_selected);
                    break;
                case 10:
                    textView_title.setText("智能设备");
                    imageView_changes.setImageResource(R.drawable.icon_weiduxiaoxi_selected);
                    break;
                case 11:
                    SharedPreferences preferences = getSharedPreferences("push",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("act",xgAccount);
                    editor.commit();
                    break;

            }
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_contralcars);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        instance = this;
        loginDataBean = AppCons.loginDataBean;

        initView();
        getState();
        initClick();
        userMation();
        initUpdate();
        getDevel();
        regist();
    }


    /**
     * @return responseBean作为数据源使用，包含登录信息
     */
    public LoginDataBean receiveData() {        //将传递过来的值存为集合,传给fragment使用
        return loginDataBean;
    }

    private void initView() {
        Log.e("登录",loginDataBean.toString());
        SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        if (loginDataBean.getData().getId() != 6 && (PWD.equals("123456") || preferences.getString("password",null).equals("123456"))){
            view_pwd = LayoutInflater.from(this).inflate(R.layout.dialog_pwd,null);
            dialog = new AlertDialog.Builder(this).create();
            dialog.setView(view_pwd);
            dialog.show();
            TextView text_qd = (TextView) view_pwd.findViewById(R.id.text_qd);
            TextView text_qx = (TextView) view_pwd.findViewById(R.id.text_qx);
            text_qd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(CarContralActivity.this,PasswordActivity.class));
                    dialog.dismiss();

                }
            });
            text_qx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        }
        imageView_photots = (ImageView) findViewById(R.id.image_photos);
        this.context = this;
        textView_phone = (TextView) findViewById(R.id.text_phone);
        textView_username = (TextView) findViewById(R.id.text_username);
        textView_account = (TextView) findViewById(R.id.text_account);
        textView_addr = (TextView) findViewById(R.id.text_addr);
        agentId = AppCons.loginDataBean.getData().getId();
        terminalID = AppCons.loginDataBean.getData().getTerminalID();
        AppCons.AGENTID_USED = AppCons.loginDataBean.getData().getId();   //当前登录的代理商ID
        AppCons.AGENTID_SELECT = AppCons.loginDataBean.getData().getId();
        Log.e(" AppCons", "AGENTID_USED: " +AppCons.AGENTID_USED + " ,AGENTID_SELECT: " + AppCons.AGENTID_SELECT); //当前选择的代理商ID
        imageView_head = (ImageView) findViewById(R.id.image_head);
        relative_account = (RelativeLayout) findViewById(R.id.relative_account);
        linearLayout_carlist = (LinearLayout) findViewById(R.id.linerlayout_carlist);
        linearLayout_tyre = (LinearLayout) findViewById(R.id.linerlayout_tyre);
        linearLayout_look = (LinearLayout) findViewById(R.id.linerlayout_look);
        linearLayout_capacity = (LinearLayout) findViewById(R.id.linerlayout_capacity);
        drawerLayout = (DrawerLayout) findViewById(R.id.carlayout);
        button_edit = (Button) findViewById(R.id.button_edit);
        textView_capacity = (TextView) findViewById(R.id.text_capacity);
        textView_service = (TextView) findViewById(R.id.text_servicemsg);
        textView_about = (TextView) findViewById(text_aboutcar);
        textView_about.setText(BuildConfig.app_a);
        textView_look = (TextView) findViewById(R.id.text_look);
        textView_tyre = (TextView) findViewById(R.id.text_tyre);
        textView_carlists = (TextView) findViewById(R.id.text_carlists);
        textView_title = (TextView) findViewById(R.id.text_title);
        imageView_lookcars = (ImageView) findViewById(R.id.image_look);
        imageView_tyre = (ImageView) findViewById(R.id.image_tyre);
        imageView_capacity = (ImageView) findViewById(R.id.image_capacity);
        imageView_carlists = (ImageView) findViewById(R.id.image_carlists);
        imageView_changes = (ImageView) findViewById(R.id.image_changes);
        imageView_pass = (ImageView) findViewById(R.id.image_changepass);
        imageView_unlogin = (ImageView) findViewById(R.id.image_unlogin);
        if (loadDrawable() != null) {
            drawable = loadDrawable();
            bd = (BitmapDrawable) drawable;
            bm = bd.getBitmap();
            imageView_photots.setImageBitmap(bm);
        }

    }

    /**
     * 〈用户信息赋值〉
     * 〈功能详细描述〉
     * drawerlayout，侧滑显示登录的信息
     */
    private void userMation() {
        AppCons.ACCOUNT = loginDataBean.getData().getUsername();
        textView_account.setText(loginDataBean.getData().getUsername());
        account = loginDataBean.getData().getUsername();
        textView_username.setText(loginDataBean.getData().getNickname());
        textView_phone.setText(loginDataBean.getData().getTelephone());
        textView_addr.setText(loginDataBean.getData().getAddress());
    }

    private void initClick() {
        relative_account.setOnClickListener(this);
        linearLayout_tyre.setOnClickListener(this);
        linearLayout_look.setOnClickListener(this);
        linearLayout_carlist.setOnClickListener(this);
        linearLayout_capacity.setOnClickListener(this);
        imageView_head.setOnClickListener(this);
        imageView_changes.setOnClickListener(this);
        button_edit.setOnClickListener(this);
        textView_service.setOnClickListener(this);
        textView_about.setOnClickListener(this);
        imageView_photots.setOnClickListener(this);
        imageView_pass.setOnClickListener(this);
        imageView_unlogin.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("activity", "onResume");

        SOCKET_CUTOFF = false;
        if (controller == null){
            controller = FragmentController.getInstance(instance, R.id.fragment_continers);
            if (CarContralActivity.this != null)
            controller.showFragment(0);
            mapFragment = (MapFragment) controller.getFragment(0);
        }


    }



    @Override
    protected void onPause() {
        super.onPause();
        Log.e("activity", "onPause");

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
        Log.e("CarConactivity", "onDestory");
        SOCKET_CUTOFF = true;
        socketService.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("activity", "onStart");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("CarConactivity", "onStop");
        drawable = null;

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("activity", "onRestart");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_head:
                if (drawerLayout.isDrawerOpen(GravityCompat.START)         /*打开侧滑*/
                        ) {
                    drawerLayout.closeDrawers();
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
                break;
            case R.id.linerlayout_look:
                if (!ButtonUtils.isFastDoubleClick(R.id.linerlayout_look)){
                    looks();
//                    refush();
                }

                break;
            case R.id.linerlayout_tyre:
                changes = 2;
                tyre();
                controller.showFragment(1);
                break;
            case R.id.linerlayout_carlist:
                if (!ButtonUtils.isFastDoubleClick(R.id.linerlayout_carlist)) {
                    //写你相关操作即可
                    changes = 3;
                    carlists();
                    controller.showFragment(2);
                }
                break;
            case R.id.linerlayout_capacity:
                changes = 4;
                capacity();
                controller.showFragment(3);
                break;
            case R.id.image_changes:
                changes();
                break;
            case R.id.button_edit:
                editMsg();
                break;
            case R.id.text_servicemsg:
                serviceMation();
                break;
            case text_aboutcar:
                startActivity(new Intent(CarContralActivity.this, AboutActivity.class));
                break;
            case R.id.image_changepass:
                password();
                break;
            case R.id.image_unlogin:
                unLogins();
                break;
            case R.id.image_photos:
                editMsg();
                break;
            case R.id.relative_account:
                startActivityForResult(new Intent(CarContralActivity.this, EditAccountActivity.class),99);
                break;
        }
    }

    private void password() {
        if (agentId == 6){
            Toast.makeText(getApplicationContext(),"体验账号不能修改密码",Toast.LENGTH_SHORT).show();
        }else {
            Bundle bundle = new Bundle();
            bundle.putSerializable("pass",loginDataBean);
            startActivity(new Intent(CarContralActivity.this, PasswordActivity.class).putExtras(bundle));
        }
    }

    private void unLogins() {
            if (dialog_pro == null){
                dialog_pro = WeiboDialogUtils.createLoadingDialog(context, "注销中");
            }else {
                dialog_pro.show();
            }

        XGPushManager.delAccount(this,xgAccount, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object o, int i) {
                dialog_pro.dismiss();
                Log.e("删除账号成功",o.toString() );
                NewHttpUtils.loginOut(CarContralActivity.this);
                startActivity(new Intent(CarContralActivity.this, MainActivity.class));
                reLayse();
            }

            @Override
            public void onFail(Object o, int i, String s) {
                dialog_pro.dismiss();
                Log.e("删除账号失败",o.toString() + " ; " + s);
                NewHttpUtils.loginOut(CarContralActivity.this);
                startActivity(new Intent(CarContralActivity.this, MainActivity.class));
                reLayse();
            }
        });

    }
    /**
     * 〈bundle封装数据到服务商信息〉
     * 〈功能详细描述〉
     *
     * @see [类、类#方法、类#成员]
     */
    private void serviceMation() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppCons.TEST_SEV, loginDataBean);
        startActivity(new Intent(CarContralActivity.this, ServiceActivity.class).putExtras(bundle));
    }
    /**
     * 〈bundle对象〉
     * 〈功能详细描述〉
     * 跳转到编辑资料界面
     *
     * @see [类、类#方法、类#成员]
     */
    private void editMsg() {
//        if (agentId.equals("6")){
//            Toast.makeText(getApplicationContext(),"体验账号不能修改资料",Toast.LENGTH_SHORT).show();
//        }else {
            Intent intent = new Intent(CarContralActivity.this, MyMessageActivity.class);
            Bundle bundle = new Bundle();
//            bundle.putSerializable("info",loginDataBean);
////            bundle.putString("teid", terminalID);
////            bundle.putString("agtId", agentId);
////            bundle.putString("pwd",pwd);
////            bundle.putString("acct",account);
////            bundle.putString("id", textView_account.getText().toString());
////            bundle.putString("user", textView_username.getText().toString());
////            bundle.putString("tell", textView_phone.getText().toString());
////            bundle.putString("addr", textView_addr.getText().toString());
            bundle.putParcelable("photos", bm);
            intent.putExtras(bundle);
            startActivityForResult(intent, 0);
//        }

    }

    /**
     * 〈根据标志为，执行不同的点击事件〉
     * 〈功能详细描述〉
     */
    private void changes() {
        if (changes == 1) {
//            mapList = mapFragment.reList();
//            Bundle bundle = new Bundle();
//            bundle.putString("TimID", terminalID);
//            bundle.putString("AgtID", agentId);
//            bundle.putSerializable(AppCons.TEST_IEMI,(Serializable)mapList);
            AppCons.WARNTYPE = 0;
            startActivity(new Intent(context, WarnListIemiActivity.class));
        }
        if (changes == 2) {
//            tyrList = tyreFragment.reList();
//            Bundle bundle = new Bundle();
//            bundle.putParcelableArrayList(AppCons.TEST_PRA, (ArrayList<? extends Parcelable>) tyrList);
            startActivity(new Intent(context, SearchActivity.class));
//            startActivity(new Intent(context, TyreActivity.class));
        }
        if (changes == 3) {

        }
    }

    public void looks() {
        changes = 1;
        controller.showFragment(0);
        Message message1 = new Message();
        message1.what = 1;
        handler.dispatchMessage(message1);
        imageView_lookcars.setImageResource(R.drawable.icon_index_chache_selected);
        textView_look.setTextColor(Color.parseColor("#ff7f00"));
        imageView_tyre.setImageResource(R.drawable.icon_index_taiya);
        textView_tyre.setTextColor(Color.parseColor("#999999"));
        imageView_carlists.setImageResource(R.drawable.icon_index_liebiao);
        textView_carlists.setTextColor(Color.parseColor("#999999"));
        imageView_capacity.setImageResource(R.drawable.btn_znsb_normal);
        textView_capacity.setTextColor(Color.parseColor("#999999"));

    }

    private void tyre() {
        Message message2 = new Message();
        message2.what = 2;
        handler.dispatchMessage(message2);
        imageView_lookcars.setImageResource(R.drawable.icon_index_chache);
        textView_look.setTextColor(Color.parseColor("#999999"));
        imageView_tyre.setImageResource(R.drawable.icon_index_taiya_selected);
        textView_tyre.setTextColor(Color.parseColor("#ff7f00"));
        imageView_carlists.setImageResource(R.drawable.icon_index_liebiao);
        textView_carlists.setTextColor(Color.parseColor("#999999"));
        imageView_capacity.setImageResource(R.drawable.btn_znsb_normal);
        textView_capacity.setTextColor(Color.parseColor("#999999"));

    }


    private void carlists() {
        imageView_lookcars.setImageResource(R.drawable.icon_index_chache);
        textView_look.setTextColor(Color.parseColor("#999999"));
        imageView_tyre.setImageResource(R.drawable.icon_index_taiya);
        textView_tyre.setTextColor(Color.parseColor("#999999"));
        imageView_carlists.setImageResource(R.drawable.icon_index_liebiao_selected);
        textView_carlists.setTextColor(Color.parseColor("#ff7f00"));
        imageView_capacity.setImageResource(R.drawable.btn_znsb_normal);
        textView_capacity.setTextColor(Color.parseColor("#999999"));
        Message message = new Message();
        message.what = 9;
        handler.sendMessage(message);
    }
    private void capacity(){
        imageView_lookcars.setImageResource(R.drawable.icon_index_chache);
        textView_look.setTextColor(Color.parseColor("#999999"));
        imageView_tyre.setImageResource(R.drawable.icon_index_taiya);
        textView_tyre.setTextColor(Color.parseColor("#999999"));
        imageView_carlists.setImageResource(R.drawable.icon_index_liebiao);
        textView_carlists.setTextColor(Color.parseColor("#999999"));
        imageView_capacity.setImageResource(R.drawable.btn_znsb);
        textView_capacity.setTextColor(Color.parseColor("#ff7f00"));
        Message message = new Message();
        message.what = 10;
        handler.sendMessage(message);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)         /*打开侧滑*/
                    ) {
                drawerLayout.closeDrawers();
            } else {
                exit();
                return false;
            }
        }
        return false;
    }


    /**
     * 根据不同的请求码返回不同的data
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (data.getExtras() != null) {
                    user = (String) data.getExtras().get("user");
                    tell = (String) data.getExtras().get("tell");
                    addr = (String) data.getExtras().get("addr");
                    bm = (Bitmap) data.getExtras().get("bitmap");
                    if (bm != null) {
                        saveDrawable(bm);
                    }
                    Message message = new Message();
                    message.what = 5;
                    handler.sendMessage(message);
                }
                break;
            case 99:
                if (data.getExtras() != null){
                    account = (String) data.getExtras().get("acct");
                    loginDataBean.getData().setUsername(account);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView_account.setText(account);
                        }
                    });
                }
                break;

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }

    /**
     * 头像保存到本地
     *
     * @param bm
     */
    private void saveDrawable(Bitmap bm) {
        if (bm != null) {
            SharedPreferences preferences = getSharedPreferences(terminalID + agentId, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            String imageBase64 = new String(Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT));
            editor.putString("P", imageBase64);
            editor.commit();
        }

    }

    /**
     * 读取sharedpreferences保存在本地的drawable对象
     *
     * @return
     */
    private Drawable loadDrawable() {
        SharedPreferences preferences = getSharedPreferences(terminalID + agentId, Context.MODE_PRIVATE);
        String temp = preferences.getString("P", "");
        ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decode(temp.getBytes(), Base64.DEFAULT));
        return Drawable.createFromStream(bais, "");
    }


    /**
     * 读取标志位
     */
    private void getState() {
        SharedPreferences preferences = getSharedPreferences(agentId + terminalID, Context.MODE_PRIVATE);
        check = preferences.getBoolean("check", true);
        if (check) {
            Log.e("terminalID",terminalID + " agentId" + agentId);
            initXgPush(terminalID,agentId);
        }
    }

    /**
     * 根据terminalID和agentId注册信鸽推送
     */

    public void initXgPush(String terminalID, int agentId) {
        XGPushConfig.enableOtherPush(getApplicationContext(),true);
        XGPushConfig.setHuaweiDebug(true);
        if (!terminalID.isEmpty()){
            xgAccount = terminalID;
        }else{
            xgAccount = agentId+"";
        }
        SharedPreferences preferences = getSharedPreferences("push",MODE_PRIVATE);
        final String act = preferences.getString("act",null);

        if (act != null && !act.equals(xgAccount)){
            Log.e("act","另一个"+act);
            XGPushManager.delAccount(context, act,new XGIOperateCallback() {
                @Override
                public void onSuccess(Object o, int i) {
                    Log.e("acacac", "注销成功");
                    XGPushManager.bindAccount(getApplicationContext(),xgAccount,
                            new XGIOperateCallback() {
                                @Override
                                public void onSuccess(Object data, int flag) {
                                    Toast.makeText(getApplicationContext(),"报警推送注册成功",Toast.LENGTH_SHORT).show();
                                    Log.w(Constants.LogTag, "+++ register push sucess. token:" + data + "flag" + flag);
                                    Message message = new Message();
                                    message.what = 11 ;
                                    handler.sendMessage(message);
                                }
                                @Override
                                public void onFail(Object data, int errCode, String msg) {
                                    Log.w(Constants.LogTag,
                                            "+++ register push fail. token:" + data
                                                    + ", errCode:" + errCode + ",msg:"
                                                    + msg);
                                    Toast.makeText(getApplicationContext(),"报警推送注册失败",Toast.LENGTH_SHORT).show();

                                }
                            });

                }

                @Override
                public void onFail(Object o, int i, String s) {
                    Log.e("acacac", "注销失败");
                    XGPushManager.delAccount(context, act);

                    XGPushManager.bindAccount(getApplicationContext(),xgAccount,
                            new XGIOperateCallback() {
                                @Override
                                public void onSuccess(Object data, int flag) {
                                    Toast.makeText(getApplicationContext(),"报警推送注册成功",Toast.LENGTH_SHORT).show();
                                    Log.w(Constants.LogTag, "+++ register push sucess. token:" + data + "flag" + flag);
                                    Message message = new Message();
                                    message.what = 11 ;
                                    handler.sendMessage(message);
                                }
                                @Override
                                public void onFail(Object data, int errCode, String msg) {
                                    Log.w(Constants.LogTag,
                                            "+++ register push fail. token:" + data
                                                    + ", errCode:" + errCode + ",msg:"
                                                    + msg);
                                    Toast.makeText(getApplicationContext(),"报警推送注册失败",Toast.LENGTH_SHORT).show();

                                }
                            });
                }
            });
        }else {
            Log.e("act","同一个"+act);
            XGPushManager.bindAccount(getApplicationContext(),xgAccount,
                    new XGIOperateCallback() {
                        @Override
                        public void onSuccess(Object data, int flag) {
                            Toast.makeText(getApplicationContext(),"报警推送注册成功",Toast.LENGTH_SHORT).show();
                            Log.w(Constants.LogTag, "+++ register push sucess. token:" + data + "flag" + flag);
                            Message message = new Message();
                            message.what = 11 ;
                            handler.sendMessage(message);

                        }
                        @Override
                        public void onFail(Object data, int errCode, String msg) {
                            Log.w(Constants.LogTag,
                                    "+++ register push fail. token:" + data
                                            + ", errCode:" + errCode + ",msg:"
                                            + msg);
                            Toast.makeText(getApplicationContext(),"报警推送注册失败",Toast.LENGTH_SHORT).show();

                        }
                    });

        }




    }
    /**
     * 反注册信鸽
     */
    public void unLogin() {
        Log.e("反注销测试","开关注销");
        XGPushManager.delAccount(context, xgAccount,new XGIOperateCallback() {
            @Override
            public void onSuccess(Object o, int i) {
                Log.e("acacac", "注销成功");
            }

            @Override
            public void onFail(Object o, int i, String s) {
                Log.e("acacac", "注销失败");
            }
        });
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);// 必须要调用这句
    }

    /**
     * 讯飞版本更新控制
     */
    public void initUpdate() {   //版本控制更新
        Log.e("BuildConfig.app_tag",BuildConfig.app_tag);
        NewHttpUtils.getAppVersion(BuildConfig.app_tag +".apk", getApplicationContext(), new ResponseCallback() {
            @Override
            public void TaskCallBack(Object object) {
                final AppVersion version = (AppVersion) object;
                if (version != null && version.getMeta() != null && version.getMeta().isSuccess()){
                    Log.e("version",version.toString());
                    int code = Integer.parseInt(version.getData().getVersion());
                    if (code > AppCons.versionCode){   //当前更新之后，所有的版本都是48版本的
                        final String url =  DOWLOADURL + BuildConfig.app_tag + ".apk";
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showDialog("版本更新",version.getData().getDescription(),url);
                            }
                        });
                    }

                }
            }

            @Override
            public void FailCallBack(Object object) {

            }
        });
    }

    public void getDevel() {
        ActivityManager manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        int heapSize = manager.getMemoryClass();
        Log.d("heapSize","当前手机的内存" + heapSize);
    }

    private void reLayse(){
        if ( dialog_pro != null){
            dialog_pro.dismiss();
            dialog_pro = null;
        }
        if (controller != null) {
            controller.destoryController();
            controller = null;
        }
        terminalID = null;
        user = null;
        tell = null;
        addr = null;
        if (bd != null) {
            bd = null;
        }
        handler.removeCallbacksAndMessages(null);
        handler = null;
        if (bm != null && !bm.isRecycled()) {
            bm.recycle();
            bm = null;
        }
        AppCons.locationList.clear();
        AppCons.locationListBean = null;
        AppCons.tyreList.clear();
        AppCons.loginDataBean = null;
        AppCons.ACCOUNT = null;
        instance = null;
        finish();
        System.gc();
    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(getApplicationContext(), BuildConfig.ENVIRONMENT, Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            reLayse();
            finish();
        }
    }

    public void refush(){
        Log.e("调用","0");
        try{
            mapFragment.listRefush();
            Log.e("调用","1");
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    /**
     * 显示更新对话框
     * @param s1 title
     * @param s2 content
     * @param s3 url
     */
    public void showDialog(String s1, String s2, final String s3){
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        View view = LayoutInflater.from(CarContralActivity.this).inflate(R.layout.autoupdate_dialog_layout,null);
        TextView textView1 = (TextView) view.findViewById(R.id.iflytek_update_title);
        TextView textView2 = (TextView) view.findViewById(R.id.iflytek_update_content);
        RelativeLayout buttonOk = (RelativeLayout) view.findViewById(R.id.ifltek_sure);
        RelativeLayout buttonNo = (RelativeLayout) view.findViewById(R.id.ifltek_cancel);
        textView1.setText(s1);
        textView2.setText(s2);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(Environment.getExternalStorageDirectory().getPath() + "/download/AutoUpdate/carhere.apk");
                Log.e("file",file.getName());
                delFile(file);
                dialog.dismiss();
                beginUpdate(s3);
            }
        });
        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                AppCons.TEST_UPDATE = true;
            }
        });
        dialog.setView(view);
        dialog.show();
    }

    public void beginUpdate(String url){
        Log.e("URL",url);
        Intent serviceIntent = new Intent(CarContralActivity.this,DownloadService.class);
        //将下载地址url放入intent中
        serviceIntent.setData(Uri.parse(url));
        startService(serviceIntent);
    }
    private void regist() {

        IntentFilter intentFilter = new IntentFilter(DownloadService.BROADCAST_ACTION);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        intentFilter.addCategory(SocketService.HEART_BEAT_ACTION);
        intentFilter.addCategory(SocketService.HEART_BEAT_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver, intentFilter);
    }
    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getExtras().getString(SocketService.HEART_BEAT_ACTION);

            String data1 = intent.getStringExtra(SocketService.HEART_BEAT_ACTION);

        }
    }

    private void delFile(File deleteFile) {
        Log.e("files",deleteFile.getName());
        if(!deleteFile.exists()) {
            Log.e("files","return");
            return;
        }
        if(!deleteFile.isDirectory()) {
            Log.e("files","delete");
            deleteFile.delete();
        } else {
            File[] fileList = deleteFile.listFiles();
            if(null == fileList || fileList.length<=0) {
                deleteFile.delete();
                return;
            }
            for(File file : fileList) {
                delFile(file);
            }
            deleteFile.delete();
        }
    }

    public void installApk(){
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/download/AutoUpdate/carhere.apk");
        Log.e("file",file.getName());
        if (file.exists()){
            Log.e("file",file.getName()    +"            地址:" + Environment.getExternalStorageDirectory().getPath());
        }else {
            Log.e("file","没有文件" + ";" + Environment.getExternalStorageDirectory().getPath());
            return;
        }
        String[] command = {"chmod", "777", file.getPath() };
        ProcessBuilder builder = new ProcessBuilder(command);
        try {
            builder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("包名",getPackageName()+".fileprovider");
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            fileUri = android.support.v4.content.FileProvider.getUriForFile(CarContralActivity.this, getPackageName()+".fileprovider", file);
        }else {
            fileUri = Uri.fromFile(file);
        }

        Log.e("fileUri",fileUri + "");
        Intent intents = new Intent(Intent.ACTION_VIEW);
        Log.e("fileUri",  "1");
        // 由于没有在Activity环境下启动Activity,设置下面的标签
        intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Log.e("fileUri",  "2");
        intents.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Log.e("fileUri",  "3");
        intents.setDataAndType(fileUri, "application/vnd.android.package-archive");
        Log.e("fileUri",  "4");
        CarContralActivity.this.startActivity(intents);
        Log.e("fileUri",  "5");

    }
}
