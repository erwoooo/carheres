package com.example.maptest.mycartest.Utils;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;

/**
 * Created by ${Author} on 2017/4/20.
 * Use to 基础fragmentactivity类
 */

public class BaseFragmentActivity extends FragmentActivity {

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config=new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config,res.getDisplayMetrics() );
        return res;
    }
}
