package com.example.maptest.mycartest.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.maptest.mycartest.UI.TyreUi.TyreActivity;

/**
 * Created by ${Author} on 2018/4/11.
 * Use to
 */

public class IntentBoradcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //接受到广播
        Intent activityIntent = new Intent(context, TyreActivity.class);
        //  要想在Service中启动Activity，必须设置如下标志（网上搜的才知道这点）
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(activityIntent);
    }
}
