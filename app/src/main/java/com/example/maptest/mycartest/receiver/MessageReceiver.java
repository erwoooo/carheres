package com.example.maptest.mycartest.receiver;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.maptest.mycartest.UI.TyreUi.TyreActivity;
import com.example.maptest.mycartest.UI.warn.WarnMationActivity;
import com.example.maptest.mycartest.Utils.AppCons;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import static com.tencent.android.tpush.service.channel.b.o;

public class MessageReceiver extends XGPushBaseReceiver {
    private Intent intent = new Intent("com.qq.xgdemo.activity.UPDATE_LISTVIEW");
    public static final String LogTag = "TPushReceiver";

    private void show(Context context, String text) {
//       Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    // 通知展示
    @Override
    public void onNotifactionShowedResult(Context context,
                                          XGPushShowedResult notifiShowedRlt) {
        if (context == null || notifiShowedRlt == null) {
            return;
        }

        Log.e(LogTag,notifiShowedRlt.getContent());
        Log.e(LogTag,AppCons.xgAccount);

        if (!notifiShowedRlt.getContent().equals(AppCons.xgAccount)){
            Log.e(LogTag,"不做推送");
            return;

        }else {
            XGNotification notific = new XGNotification();
            notific.setMsg_id(notifiShowedRlt.getMsgId());
            notific.setTitle(notifiShowedRlt.getTitle());
            notific.setContent(notifiShowedRlt.getContent());
            // notificationActionType==1为Activity，2为url，3为intent
            notific.setNotificationActionType(notifiShowedRlt
                    .getNotificationActionType());
            //Activity,url,intent都可以通过getActivity()获得

            notific.setActivity(notifiShowedRlt.getActivity());
            notific.setUpdate_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .format(Calendar.getInstance().getTime()));
            NotificationService.getInstance(context).save(notific);
            context.sendBroadcast(intent);
            show(context, "您有1条新消息, " + "通知被展示 ， " + notifiShowedRlt.toString());
            Log.d("LC", "+++++++++++++++++++++++++++++展示通知的回调");
        }

    }

    //反注册的回调
    @Override
    public void onUnregisterResult(Context context, int errorCode) {
        if (context == null) {
            return;
        }
        String text = "";
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = "反注册成功";
        } else {
            text = "反注册失败" + errorCode;
        }
        Log.d(LogTag, text);
        show(context, text);

    }

    //设置tag的回调
    @Override
    public void onSetTagResult(Context context, int errorCode, String tagName) {
        if (context == null) {
            return;
        }
        String text = "";
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = "\"" + tagName + "\"设置成功";
        } else {
            text = "\"" + tagName + "\"设置失败,错误码：" + errorCode;
        }
        Log.d(LogTag, text);
        show(context, text);

    }

    //删除tag的回调
    @Override
    public void onDeleteTagResult(Context context, int errorCode, String tagName) {
        if (context == null) {
            return;
        }
        String text = "";
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = "\"" + tagName + "\"删除成功";
        } else {
            text = "\"" + tagName + "\"删除失败,错误码：" + errorCode;
        }
        Log.d(LogTag, text);
        show(context, text);

    }

    // 通知点击回调 actionType=1为该消息被清除，actionType=0为该消息被点击。此处不能做点击消息跳转，详细方法请参照官网的Android常见问题文档
    @Override
    public void onNotifactionClickedResult(Context context,
                                           XGPushClickedResult message) {
        Log.e("LC", "+++++++++++++++ 通知被点击 跳转到指定页面。");
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        if (context == null || message == null) {
            return;
        }
        String text = "";
        if (message.getActionType() == XGPushClickedResult.NOTIFACTION_CLICKED_TYPE) {

            // 通知在通知栏被点击啦。。。。。
            // APP自己处理点击的相关动作
            // 这个动作可以在activity的onResume也能监听，请看第3点相关内容
            text = "通知被打开 :" + message;
        } else if (message.getActionType() == XGPushClickedResult.NOTIFACTION_DELETED_TYPE) {
            // 通知被清除啦。。。。
            // APP自己处理通知被清除后的相关动作
            text = "通知被清除 :" + message;
        }

        // 获取自定义key-value
        Log.e("通知被点击",message.getCustomContent() + " ; " + message.getContent() + " ; " + message.toString());
        String customContent = message.getCustomContent();
        if (customContent != null && customContent.length() != 0) {
            Bundle bundle = new Bundle();

            int alram = 0;
            String TimID = "";
            try {
                JSONArray obj = JSONArray.parseArray(customContent);
                for (int i = 0 ; i < obj.size() ; i ++){
                    Log.e("参数",obj.get(i).toString());
                }
                com.alibaba.fastjson.JSONObject jsonObject0 = obj.getJSONObject(0);
                alram = Integer.parseInt(jsonObject0.get("alarm").toString());
                com.alibaba.fastjson.JSONObject jsonObject1 = obj.getJSONObject(1);
                TimID = String.valueOf(jsonObject1.get("id")) ;
                bundle.putString("TimID",TimID);
                Log.e("收到的参数",TimID + " ; " + alram);
                if (alram == 1){
                    Intent intent = new Intent(context,TyreActivity.class).putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent );
                }else {
                    Intent intent = new Intent(context,WarnMationActivity.class).putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent );
                }
                // ...
            } catch (Exception e) {
                e.printStackTrace();

                com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(customContent);
                alram = Integer.parseInt(jsonObject.get("alarm").toString());
                TimID = String.valueOf(jsonObject.get("id")) ;
                bundle.putString("TimID",TimID);
                if (alram == 1){
                    Intent intent = new Intent(context,TyreActivity.class).putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent );
                }else {
                    Intent intent = new Intent(context,WarnMationActivity.class).putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent );
                }


            }
        }
        // APP自主处理的过程。。。
        Log.e(LogTag, text);
        show(context, text);
    }

    //注册的回调
    @Override
    public void onRegisterResult(Context context, int errorCode,
                                 XGPushRegisterResult message) {
        // TODO Auto-generated method stub
        if (context == null || message == null) {
            return;
        }
        String text = "";
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = message + "注册成功";
            // 在这里拿token
            String token = message.getToken();
        } else {
            text = message + "注册失败错误码：" + errorCode;
        }
        Log.d(LogTag, text);
        show(context, text);
    }

    // 消息透传的回调
    @Override
    public void onTextMessage(Context context, XGPushTextMessage message) {
        // TODO Auto-generated method stub
        String text = "收到消息:" + message.toString();
        // 获取自定义key-value
        String customContent = message.getCustomContent();
        if (customContent != null && customContent.length() != 0) {
            try {
                JSONObject obj = new JSONObject(customContent);
                // key1为前台配置的key
                if (!obj.isNull("key")) {
                    String value = obj.getString("key");
                    Log.d(LogTag, "get custom value:" + value);
                }
                // ...
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.d("LC", "++++++++++++++++透传消息");
        // APP自主处理消息的过程...
        Log.d(LogTag, text);
        show(context, text);
    }

}
