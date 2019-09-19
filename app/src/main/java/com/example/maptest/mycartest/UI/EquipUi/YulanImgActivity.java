package com.example.maptest.mycartest.UI.EquipUi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.BaseActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by ${Author} on 2019/4/2.
 * Use to
 */

public class YulanImgActivity extends BaseActivity {
    private ImageView image_yl_show;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            image_yl_show.setImageBitmap((Bitmap) msg.obj);
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yl);
        image_yl_show = (ImageView) findViewById(R.id.image_yl_show);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = getURLimage(AppCons.locationListBean.getPicture());
                Message message = new Message();
                message.what = 1;
                message.obj = bitmap;
                handler.sendMessage(message);
            }
        }).start();


//        Glide.with(this).load(AppCons.locationListBean.getPicture()).placeholder(R.drawable.a1).into(image_yl_show);


    }

    //加载图片
    public Bitmap getURLimage(String url) {
        Bitmap bmp = null;
        try {
            URL myurl = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();
            InputStream is = conn.getInputStream();//获得图片的数据流
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }
}
