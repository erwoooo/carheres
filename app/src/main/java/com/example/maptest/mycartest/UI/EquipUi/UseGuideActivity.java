package com.example.maptest.mycartest.UI.EquipUi;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.Utils.BaseActivity;

/**
 * Created by ${Author} on 2017/8/15.
 * Use to 指导页面
 */

public class UseGuideActivity extends BaseActivity implements View.OnClickListener{
    private ImageView imageView_quit,imageView_use;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use);
        initView();
    }

    private void initView() {

        imageView_quit = (ImageView) findViewById(R.id.image_quit_use);
        imageView_quit.setOnClickListener(this);
        imageView_use = (ImageView) findViewById(R.id.image_use);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_quit_use:
                quit();
                break;
        }
    }

    private void quit() {
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            quit();
        }
        return super.onKeyDown(keyCode, event);
    }
}
