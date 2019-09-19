package com.example.maptest.mycartest.Utils;


import android.view.View;

/**
 * Created by ${Author} on 2018/4/2.
 * Use to
 */

public abstract class OnclickListeners implements View.OnClickListener {
    public static final int TIME = 500;
    private long lastTime = 0;
    @Override
    public void onClick(View v) {
        long clickTime = System.currentTimeMillis();
        if ((clickTime - lastTime) > TIME){
            lastTime = clickTime;
            onNoDoubleClick(v);
        }

    }
    protected abstract void onNoDoubleClick(View v);
}
