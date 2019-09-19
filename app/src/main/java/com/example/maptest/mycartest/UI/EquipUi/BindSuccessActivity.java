package com.example.maptest.mycartest.UI.EquipUi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maptest.mycartest.Bean.BlueOrderBean;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.BaseActivity;
import com.example.maptest.mycartest.Utils.bluetoothmanager.BlueToothManager;

/**
 * Created by ${Author} on 2017/10/9.
 * Use to
 */

public class BindSuccessActivity extends BaseActivity implements View.OnClickListener{
    private String address;
    private TextView textView_rs,textView_look;
    private BlueOrderBean orderBean;
    private ImageView imageView_quit;
//    public BlueToothManager bluetooth = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bindequip);
        address = getIntent().getStringExtra("address");
        Log.e("address",address);
        orderBean = new BlueOrderBean(address,true);
        initView();
    }

    private void initView() {
//        bluetooth = new BlueToothManager(BindSuccessActivity.this);
//        bluetooth.RegisterBroadCastReceive();
        imageView_quit = (ImageView) findViewById(R.id.image_quitbind);
        textView_rs = (TextView) findViewById(R.id.bind_text_research);
        textView_look = (TextView) findViewById(R.id.bind_text_checkequip);
        imageView_quit.setOnClickListener(this);
        textView_rs.setOnClickListener(this);
        textView_look.setOnClickListener(this);
        AppCons.orderBean = orderBean;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bind_text_research:
//                if(bluetooth != null)
//                {
//                    bluetooth.BlueToothDisconnect(address);
//                }
                AppCons.orderBean = null;
                finish();
                break;
            case R.id.bind_text_checkequip:
                startActivity(new Intent(BindSuccessActivity.this,CapacityEquipActivity.class));
                finish();
                break;
            case R.id.image_quitbind:
//                if(bluetooth != null)
//                {
//                    bluetooth.BlueToothDisconnect(address);
//                }
                AppCons.orderBean = null;
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        bluetooth.UnRegisterBroadCastReceive();
    }
}
