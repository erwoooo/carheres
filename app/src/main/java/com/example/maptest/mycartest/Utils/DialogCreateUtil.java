package com.example.maptest.mycartest.Utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import com.example.maptest.mycartest.R;
/**
 * Created by ${Author} on 2017/3/4.
 * Use to 创建对话框
 */

public class DialogCreateUtil  {
    private AlertDialog dialog;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1){
                dialog.dismiss();
            }
        }
    };
    private WindowManager windowManager;

    public Dialog creatDialog(Context context) {
        dialog = new AlertDialog.Builder(context).create();     //创建对话框
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_dialog, null);
        Button button = (Button) view.findViewById(R.id.button_unlogins);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        });
        dialog.setCancelable(false);
        dialog.setView(view);
        return dialog;
    }
    public WindowManager getWindowManager() {
        return windowManager;
    }
}
