package com.example.maptest.mycartest.Utils;

import android.app.DownloadManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.maptest.mycartest.UI.CarContralActivity;

/**
 * Created by ${Author} on 2018/4/12.
 * Use to Android N及以上下载服务
 */

public class DownloadService extends IntentService {
    private String TAG = "DownloadService";
    public static final String BROADCAST_ACTION =
            "com.example.maptest.mycartest.BROADCAST";
    public static final String EXTENDED_DATA_STATUS =
            "com.example.maptest.mycartest.STATUS";

    private LocalBroadcastManager mLocalBroadcastManager;

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //获取下载地址
        String url = intent.getDataString();
        Log.i(TAG,url);
        //获取DownloadManager对象
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        //指定APK缓存路径和应用名称，可在SD卡/Android/data/包名/file/Download文件夹中查看
        request.setDestinationInExternalPublicDir("download/AutoUpdate", "carhere.apk");
        Log.e("下载所在的位置",Environment.getExternalStorageDirectory().getPath());
//        request.setDestinationInExternalFilesDir(this, Environment.getExternalStorageDirectory().getPath() + "/download", "ceshi.apk");
        //设置网络下载环境为wifi
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        //设置显示通知栏，下载完成后通知栏自动消失
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED );
        //设置通知栏标题
        request.setTitle("下载");
        request.setDescription("应用正在下载");
        request.setAllowedOverRoaming(false);
        request.setVisibleInDownloadsUi(true);
        //获得唯一下载id
        long requestId = downloadManager.enqueue(request);
        Log.e("sendID",requestId + "  有吗");
        //将id放进Intent
        Intent localIntent = new Intent(BROADCAST_ACTION);
        localIntent.putExtra(EXTENDED_DATA_STATUS,"OK");
        //查询下载信息
        DownloadManager.Query query=new DownloadManager.Query();
        query.setFilterById(requestId);
        try{
            boolean isGoging=true;
            while(isGoging){
                Cursor cursor = downloadManager.query(query);
                if (cursor != null && cursor.moveToFirst()) {
                    int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    int bytes_downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    Log.e("status",status + "" + "bytes_downloaded: " + bytes_downloaded);
                    switch(status){
                        //如果下载状态为成功
                        case DownloadManager.STATUS_SUCCESSFUL:
                            isGoging=false;
                            Log.e("status",status + " " + isGoging);
                            //调用LocalBroadcastManager.sendBroadcast将intent传递回去
                            mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
                            Log.e("status",status + " this " + isGoging);
                            mLocalBroadcastManager.sendBroadcast(localIntent);
                            Log.e("status",status + " localIntent " + isGoging);
                            CarContralActivity activity = CarContralActivity.instance;
                            activity.installApk();
                            Log.e("status",status + " activity " + isGoging);
                            break;
                    }
                }

                if(cursor!=null){
                    cursor.close();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            Log.e("error",e.toString());
        }


    }
}