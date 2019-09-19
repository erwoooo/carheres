package com.example.maptest.mycartest.UI.SetUi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.Utils.BaseActivity;

import OcrCode.Ocr;


/**
 * Created by ${Author} on 2017/5/15.
 * Use to
 */
public class PayAcitivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_n);
        ImageView imageView_quit = (ImageView) findViewById(R.id.image_quitpay);
        imageView_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                String deviceId =
//                        Ocr.getValue("YET1wMjrymbUGORlNJSJMqmtDeD92I3OI4SdK8w2lNnc0eS9cDjOQAL7PFKzVnEiWx858gvqVBU=");
//                System.err.print(deviceId);
//
//                Log.e("imei",deviceId + " ; " + "测试");
            }
        });



    }
}
