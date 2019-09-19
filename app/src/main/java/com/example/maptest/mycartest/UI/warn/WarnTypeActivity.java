package com.example.maptest.mycartest.UI.warn;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.Utils.BaseActivity;

/**
 * Created by ${Author} on 2017/3/19.
 * Use to
 */

public class WarnTypeActivity  extends BaseActivity implements View.OnClickListener{
    private ImageView imageView_quit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warntype);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        initView();
        initClick();
    }

    private void initClick() {
        imageView_quit.setOnClickListener(this);
    }

    private void initView() {
        imageView_quit = (ImageView) findViewById(R.id.image_waninfoquits);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_waninfoquits:
                finish();
                break;
        }
    }
}
