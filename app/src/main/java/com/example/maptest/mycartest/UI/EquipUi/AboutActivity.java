package com.example.maptest.mycartest.UI.EquipUi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.maptest.mycartest.BuildConfig;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.BaseActivity;
import com.example.maptest.mycartest.Utils.DownloadService;
import com.iflytek.autoupdate.IFlytekUpdate;
import com.iflytek.autoupdate.IFlytekUpdateListener;
import com.iflytek.autoupdate.UpdateConstants;
import com.iflytek.autoupdate.UpdateErrorCode;
import com.iflytek.autoupdate.UpdateInfo;
import com.iflytek.autoupdate.UpdateType;

import java.io.File;
import java.io.IOException;

import static com.example.maptest.mycartest.Utils.AppCons.versionNum;

/**
 * Created by ${Author} on 2017/3/21.
 * Use to 关于车在这儿
 */

public class AboutActivity extends BaseActivity implements View.OnClickListener{
    private ImageView imageView_quit;
    private RelativeLayout relativeLayout_use;
    private TextView textView_update,text_weixin,text_weburl,text_varsionumber;
    private IFlytekUpdate updManager;
    private TextView title_text;
    private Uri fileUri;
    MyReceiver myReceiver = new MyReceiver();
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    textView_update.setText("已是最新版本");
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carhere);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        initView();
        initClick();
        regist();
    }

    private void initClick() {
        textView_update.setOnClickListener(this);
        relativeLayout_use.setOnClickListener(this);
        imageView_quit.setOnClickListener(this);
    }

    private void initView() {
        text_weixin = (TextView) findViewById(R.id.text_weixin);
        text_weburl = (TextView) findViewById(R.id.text_weburl);
        text_weburl.setText(BuildConfig.app_net);
        text_weixin.setText(BuildConfig.app_pub);
        textView_update = (TextView) findViewById(R.id.text_varsion_update);
        text_varsionumber = (TextView) findViewById(R.id.text_varsionumber);
        text_varsionumber.setText(versionNum);
        relativeLayout_use = (RelativeLayout) findViewById(R.id.relative_use);
        imageView_quit = (ImageView) findViewById(R.id.image_quitabout);
        title_text = (TextView) findViewById(R.id.title_text);
        title_text.setText(BuildConfig.app_b);
        if (AppCons.TEST_UPDATE){
            textView_update.setText("已是最新版本");
        }else {
            textView_update.setText("更新");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_quitabout:
                quit();
                break;
            case R.id.relative_use:
                startActivity(new Intent(AboutActivity.this,UseGuideActivity.class));
                break;
            case R.id.text_varsion_update:
                initUpdate();
                break;
        }
    }

    private void quit() {
        updManager = null;
        handler.removeCallbacks(null);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
    }

    /**
     * 讯飞版本更新控制
     */
    public void initUpdate() {
        try {
            PackageManager manager = getApplicationContext().getPackageManager();
            PackageInfo info = manager.getPackageInfo(getApplicationContext().getPackageName(), 0);//getPackageName()是你当前类的包名，0代表是获取版本信息String name = pi.versionName;int code = pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        updManager = IFlytekUpdate.getInstance(getApplicationContext());
        updManager.setDebugMode(true);
      /*  updManager.setParameter(UpdateConstants.EXTRA_WIFIONLY, "false");*/
        // 设置通知栏icon，默认使用SDK默认
        updManager.setParameter(UpdateConstants.EXTRA_NOTI_ICON, "false");
        updManager.setParameter(UpdateConstants.EXTRA_STYLE, UpdateConstants.UPDATE_UI_DIALOG);
        updManager.forceUpdate(getApplicationContext(), updateListener);
    }

    /**
     * 〈讯飞SDK更新监听〉
     * 〈功能详细描述〉
     * @param [errorcode]     [返回码]
     * @param [result]     [返回的内容]
     * @return  [返回类型说明]
     */
    private IFlytekUpdateListener updateListener = new IFlytekUpdateListener() {

        @Override
        public void onResult(int errorcode, final UpdateInfo result) {
            if (errorcode == UpdateErrorCode.OK && result != null) {
                if (result.getUpdateType() == UpdateType.NoNeed) {
//                    showTip("已经是最新版本！");
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                    return;
                }
                if(Build.VERSION.SDK_INT>=24) {   /*-------7.0以上------*/
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showDialog(result.getUpdateInfo(),result.getUpdateDetail(),result.getDownloadUrl());
                        }
                    });

                }else {
                    updManager.showUpdateInfo(getApplicationContext(), result);     /*内存泄露*/
                }
            } else {
                updManager.showUpdateInfo(getApplicationContext(), new UpdateInfo());     /*内存泄露*/
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            quit();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void showDialog(String s1, String s2, final String s3){
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        View view = LayoutInflater.from(AboutActivity.this).inflate(R.layout.autoupdate_dialog_layout,null);
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
        Intent serviceIntent = new Intent(AboutActivity.this,DownloadService.class);
        //将下载地址url放入intent中
        serviceIntent.setData(Uri.parse(url));
        startService(serviceIntent);
    }

    private void regist() {
        IntentFilter intentFilter = new IntentFilter(DownloadService.BROADCAST_ACTION);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver, intentFilter);
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getStringExtra(DownloadService.EXTENDED_DATA_STATUS);
            Log.e("test", data);
            File file = new File(Environment.getExternalStorageDirectory().getPath() + "/download/AutoUpdate/carhere.apk");
            Log.e("file",file.getName());
            if (file.exists()){
                Log.e("file",file.getName()    +"            地址:" + Environment.getExternalStorageDirectory().getPath());
            }else {
                Log.e("file","没有文件" + ";" + Environment.getExternalStorageDirectory().getPath());
            }
            String[] command = {"chmod", "777", file.getPath() };
            ProcessBuilder builder = new ProcessBuilder(command);
            try {
                builder.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            fileUri = android.support.v4.content.FileProvider.getUriForFile(AboutActivity.this, getPackageName()+".fileprovider", file);
            Intent intents = new Intent(Intent.ACTION_VIEW);
            // 由于没有在Activity环境下启动Activity,设置下面的标签
            intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intents.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intents.setDataAndType(fileUri, "application/vnd.android.package-archive");
            startActivity(intents);

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
}
