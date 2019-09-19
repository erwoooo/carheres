package com.example.maptest.mycartest.UI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


import com.baidu.lbsapi.BMapManager;
import com.baidu.lbsapi.panoramaview.PanoramaView;
import com.baidu.lbsapi.panoramaview.PanoramaViewListener;
import com.example.maptest.mycartest.New.LocationListBean;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.SwipRecycleView.App;


/**
 * Created by ${Author} on 2017/5/22.
 * Use to
 */

public class PanoramaDemoActivityMain extends AppCompatActivity {
    private PanoramaView mPanoView;
    private LocationListBean useBean;
    private ImageView image_quit_set;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_street);
        Log.e("create","街景");
        mPanoView = (PanoramaView) findViewById(R.id.panorama);
        useBean = (LocationListBean) getIntent().getExtras().getSerializable("street");
        Log.e("user",useBean.toString());
        image_quit_set = (ImageView) findViewById(R.id.image_quit_set);

        mPanoView = (PanoramaView) findViewById(R.id.panorama);
        mPanoView.setPanoramaImageLevel(PanoramaView.ImageDefinition.ImageDefinitionLow);
        App app = (App) this.getApplication();
        if (app.mBMapManager == null) {
            app.mBMapManager = new BMapManager(app);
            app.mBMapManager.init(new App.MyGeneralListener());
        }
        mPanoView.setPanoramaViewListener(new PanoramaViewListener() {

            @Override
            public void onLoadPanoramaBegin() {
                Log.i("onLoadPanoramaBegin", "onLoadPanoramaStart...");
            }

            @Override
            public void onLoadPanoramaEnd(String json) {
                Log.i("onLoadPanoramaEnd", "onLoadPanoramaEnd : " + json);
            }

            @Override
            public void onLoadPanoramaError(String error) {
                Log.i("onLoadPanoramaError", "onLoadPanoramaError : " + error);
            }

            @Override
            public void onDescriptionLoadEnd(String json) {

            }

            @Override
            public void onMessage(String msgName, int msgType) {

            }

            @Override
            public void onCustomMarkerClick(String key) {

            }

            @Override
            public void onMoveStart() {

            }

            @Override
            public void onMoveEnd() {

            }
        });
        mPanoView.setPanorama(useBean.getLocation().getBdLon(),useBean.getLocation().getBdLat(),PanoramaView.COORDTYPE_BD09LL);


        image_quit_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




    }
    @Override
    protected void onPause() {
        super.onPause();
        mPanoView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPanoView.onResume();
    }

    @Override
    protected void onDestroy() {
        mPanoView.destroy();
        useBean = null;
        super.onDestroy();
        mPanoView = null;

    }
}
