package com.example.maptest.mycartest.UI.SetUi;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.BaseActivity;

/**
 * Created by ${Author} on 2017/3/30.
 * Use to 在地图上添加围栏
 */
public class SetCrawlActivity extends BaseActivity implements View.OnClickListener {
    private ImageView imageView_quit, imageView_maplarg, imageView_mapsmall, imageView_crawlcount, imageView_crawlless;
    private SeekBar seekBar;
    private MapView mapView;
    private BaiduMap baiduMap;
    private LinearLayout linearLayout_text;
    private LocationClient locationClient;
    private TextView textView_add;
    private BitmapDescriptor icon;//标注图标
    private Marker marker = null;
    private Bundle bundle = new Bundle();
    private InfoWindow infoWindow = null;
    //围栏半径
    private int cra = 0,decra = 50;
    //圆心的经纬度
    private double mLatitude;
    private double mLongtitude;

    //添加覆盖物
    private static Overlay fenceOverlay = null;
    private static OverlayOptions fenceOverlayOption = null;
    private static OverlayOptions fenceOverlayOptionsTemp = null;
    private TextView textView_cra;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:

                    seekBar.setProgress(msg.arg1);
//                    baiduMap.showInfoWindow(infoWindow);

                    break;
                case 1:
                    seekBar.setProgress(msg.arg1);
//                    baiduMap.showInfoWindow(infoWindow);
                    break;
                case 4:

                  infoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(linearLayout_text), (LatLng) msg.obj, -47, null);
                    baiduMap.showInfoWindow(infoWindow);
                    break;
                case 5:

                    infoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(linearLayout_text), new LatLng(mLatitude,mLongtitude), -47, null);
                    baiduMap.showInfoWindow(infoWindow);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_setcrawl);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        initView();
        initClick();
        setCrawl();
        initLocation();
        cycle();

    }

    /**
     * 初始化点击监听
     */
    private void initClick() {
        imageView_quit.setOnClickListener(this);
        imageView_crawlless.setOnClickListener(this);
        imageView_mapsmall.setOnClickListener(this);
        imageView_crawlcount.setOnClickListener(this);
        imageView_maplarg.setOnClickListener(this);
        textView_add.setOnClickListener(this);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        linearLayout_text = (LinearLayout) LayoutInflater.from(SetCrawlActivity.this).inflate(R.layout.item_text, null);
        textView_cra = (TextView) linearLayout_text.findViewById(R.id.text_infospd);
        textView_add = (TextView) findViewById(R.id.text_addcrawl);
        mapView = (MapView) findViewById(R.id.crawl_map);
        baiduMap = mapView.getMap();
        mapView.showZoomControls(false);        //隐藏地图自带的控件
        mapView.showScaleControl(false);        //隐藏比例尺
        MapStatusUpdate Msu = MapStatusUpdateFactory.zoomTo(15.0f);         //地图比例
        baiduMap.setMapStatus(Msu);
        imageView_quit = (ImageView) findViewById(R.id.image_quit_setmaps);
        imageView_mapsmall = (ImageView) findViewById(R.id.image_crawlmaplit);
        imageView_maplarg = (ImageView) findViewById(R.id.image_crawlmapbig);
        imageView_crawlcount = (ImageView) findViewById(R.id.image_largecrawl);
        imageView_crawlless = (ImageView) findViewById(R.id.image_smallcrawl);
        seekBar = (SeekBar) findViewById(R.id.seekbar_crawl);

        mLatitude = AppCons.locationListBean.getLocation().getBdLat();
        mLongtitude = AppCons.locationListBean.getLocation().getBdLon();
        seekBar.setProgress(50);
        textView_cra.setText("50m");            //初始围栏半径100
        setDis(new LatLng(mLatitude,mLongtitude));

    }

    /**
     * @param v
     * view点击时间
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_addcrawl:        //设置电子围栏，把围栏信息传到下一个界面
                bundle.putInt("dis", cra);
                bundle.putDouble("la", mLatitude);
                bundle.putDouble("lo", mLongtitude);
                startActivity(new Intent(SetCrawlActivity.this, EditCrawlActivity.class).putExtras(bundle));
                break;
            case R.id.image_quit_setmaps:
               canCle();
                break;
            case R.id.image_crawlmaplit:
                smallMap();
                break;
            case R.id.image_crawlmapbig:
                largeMap();
                break;
            case R.id.image_largecrawl:
                crawlCount();
                break;
            case R.id.image_smallcrawl:
                crawlLess();
                break;
        }

    }

    private void smallMap() {
        float zoomLevel = baiduMap.getMapStatus().zoom;
        if (zoomLevel > 1) {
            baiduMap.setMapStatus(MapStatusUpdateFactory.zoomOut());
            imageView_mapsmall.setEnabled(true);
        } else {
            imageView_mapsmall.setEnabled(false);
            Toast.makeText(SetCrawlActivity.this, "已经缩至最小！", Toast.LENGTH_SHORT).show();
        }
    }

    private void largeMap() {
        float zoomLevel = baiduMap.getMapStatus().zoom;
        if (zoomLevel <= 24) {
            baiduMap.setMapStatus(MapStatusUpdateFactory.zoomIn());
            imageView_maplarg.setEnabled(true);
        } else {
            Toast.makeText(SetCrawlActivity.this, "已经放至最大！", Toast.LENGTH_SHORT).show();
            imageView_maplarg.setEnabled(false);
        }
    }

    /**
     * 半径大于0，可减
     */
    private void crawlLess() {
        if (cra > 0) {
            cra--;
        }
        Message message = new Message();
        message.what = 0;
        message.arg1 = cra;
        handler.sendMessage(message);

    }

    /**
     * 半径小于1000，可加
     */
    private void crawlCount() {
        if (cra < 10000) {
            cra++;
        }
        Message message = new Message();
        message.what = 1;
        message.arg1 = cra;
        handler.sendMessage(message);
    }

    /**
     * seekbar设置围栏半径
     */
    private void setCrawl() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cra = progress + decra;
                setDis(new LatLng(mLatitude, mLongtitude));
                Message message = new Message();
                message.what = 5;
                message.arg1 = cra;
                handler.sendMessage(message);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView_cra.setText(cra+"m");
                    }
                });
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /**
     * 初始化地图相关
     */
    private void initLocation() {
        locationClient = new LocationClient(SetCrawlActivity.this);
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setOpenGps(true);    //开启GPS
        option.setScanSpan(1000);
        locationClient.setLocOption(option);
        LatLng latLng = new LatLng(mLatitude, mLongtitude);
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        baiduMap.animateMapStatus(msu);
        icon = BitmapDescriptorFactory.fromResource(R.drawable.icon_huifang_location);
        OverlayOptions ooA = new MarkerOptions().position(new LatLng(mLatitude, mLongtitude))
                .icon(icon).zIndex(9).draggable(true);
        marker = (Marker) (baiduMap.addOverlay(ooA));
        infoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(linearLayout_text), new LatLng(mLatitude, mLongtitude), -47, null);
        baiduMap.showInfoWindow(infoWindow);
    }

    @Override
    protected void onStart() {
        super.onStart();
        baiduMap.setMyLocationEnabled(true);   //开启定位
        if (!locationClient.isStarted()) {
            locationClient.start();

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        baiduMap.setMyLocationEnabled(false);   //开启定位
        locationClient.stop();              //停止定位
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        infoWindow = null;
        mapView.onDestroy();
        if (icon != null){
            icon.recycle();
            icon = null;
        }
        linearLayout_text = null;
        bundle = null;
        marker = null;
        handler.removeCallbacksAndMessages(null);
        handler = null;
    }

    /**
     * 围栏随地图移动
     */
    private void cycle() {
        baiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {
            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

            }


            @Override
            public void onMapStatusChange(MapStatus mapStatus) {
            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                mLatitude = mapStatus.target.latitude;
                mLongtitude = mapStatus.target.longitude;
                setDis(mapStatus.target);
                marker.setPosition(mapStatus.target);
                Message message = new Message();
                message.what = 4;
                message.obj = mapStatus.target;
                handler.sendMessage(message);
            }
        });
    }

    /**
     * @param latLng
     * 画围栏
     */
    private void setDis(LatLng latLng) {
        if (fenceOverlayOption != null) {
            fenceOverlayOptionsTemp = fenceOverlayOption;
        }
        if (fenceOverlay != null) {
            fenceOverlay.remove();
        }
        fenceOverlayOption = new CircleOptions().fillColor(0x000000FF).center(latLng)
                .stroke(new Stroke(5, Color.rgb(0xff, 0x00, 0x33)))
                .radius(cra);
        if (fenceOverlayOption != null) {
            fenceOverlay = baiduMap.addOverlay(fenceOverlayOption);
        }

    }

    /**
     * 退出
     */
    private void canCle(){
        if (icon != null){
            icon.recycle();
            icon = null;
        }
        marker = null;
        infoWindow = null;
        fenceOverlayOption = null;
        fenceOverlay = null;
        fenceOverlayOptionsTemp = null;

        bundle = null;
        finish();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode){
            canCle();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }
}
