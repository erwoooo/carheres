package com.example.maptest.mycartest.UI.SetUi;


import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.maptest.mycartest.Utils.UtcDateChang;

/**
 * Created by ${Author} on 2017/3/30.
 * Use to 显示电子围栏
 */
public class ShowCrawlActivity extends BaseActivity implements View.OnClickListener {
    private ImageView imageView_quit, imageView_maplarg, imageView_mapsmall;
    private MapView mapView;
    private BaiduMap baiduMap;
    private LinearLayout linearLayout_crawl;
    private LocationClient locationClient;
    private BitmapDescriptor icon;//标注图标
    private Marker marker = null;
    //围栏半径
    private int cra;
    //圆心的经纬度
    private double mLatitude;
    private double mLongtitude;
    //添加覆盖物
    private static Overlay fenceOverlay = null;
    private static OverlayOptions fenceOverlayOption = null;
    private static OverlayOptions fenceOverlayOptionsTemp = null;
    private String name, time;
    private int type;
    private InfoWindow infoWindow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_showcrawl);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        initView();
        initClick();
        initLocation();
        cycle();

    }

    /**
     * 绑定点击监听
     */
    private void initClick() {
        imageView_quit.setOnClickListener(this);
        imageView_mapsmall.setOnClickListener(this);
        imageView_maplarg.setOnClickListener(this);

    }

    /**
     * 初始化控件
     */
    private void initView() {

        linearLayout_crawl = (LinearLayout) LayoutInflater.from(ShowCrawlActivity.this).inflate(R.layout.item_crawwindow, null);
        mapView = (MapView) findViewById(R.id.showcrawl_map);
        baiduMap = mapView.getMap();
        mapView.showZoomControls(false);        //隐藏地图自带的控件
        mapView.showScaleControl(false);        //隐藏比例尺
        MapStatusUpdate Msu = MapStatusUpdateFactory.zoomTo(15.0f);         //地图比例
        baiduMap.setMapStatus(Msu);
        imageView_quit = (ImageView) findViewById(R.id.image_quit_showmaps);
        imageView_mapsmall = (ImageView) findViewById(R.id.image_showcrawlmaplit);
        imageView_maplarg = (ImageView) findViewById(R.id.image_showcrawlmapbig);

        if (AppCons.ORDERBEN != null ){
            name = AppCons.ORDERBEN.getFenceName();
            type = AppCons.ORDERBEN.getFenceModel();
            time = UtcDateChang.UtcDatetoLocaTime(AppCons.ORDERBEN.getFenceSetTime());
            cra = AppCons.ORDERBEN.getFenceLimit();
            mLatitude = AppCons.ORDERBEN.getFenceLat();
            mLongtitude = AppCons.ORDERBEN.getFenceLon();
            setDis(new LatLng(mLatitude, mLongtitude));
        }else {
            mLatitude = AppCons.locationListBean.getLocation().getBdLat();
            mLongtitude = AppCons.locationListBean.getLocation().getBdLon();
            setDis(new LatLng(mLatitude, mLongtitude));

        }
        createWindow(linearLayout_crawl);
        infoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(linearLayout_crawl), new LatLng(mLatitude, mLongtitude), -30, new InfoWindow.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick() {
                baiduMap.hideInfoWindow();
            }
        });
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                baiduMap.showInfoWindow(infoWindow);
                return false;
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_showcrawlmaplit:
                smallMap();
                break;
            case R.id.image_showcrawlmapbig:
                largeMap();
                break;
            case R.id.image_quit_showmaps:
                quitShow();
                break;
        }

    }

    private void quitShow() {

        if (icon != null) {
            icon.recycle();
            icon = null;
        }
        mapView = null;
        marker = null;

        time = null;
        name = null;
        infoWindow = null;
        linearLayout_crawl = null;
        imageView_maplarg = null;
        imageView_mapsmall = null;
        imageView_quit = null;

        finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        baiduMap = null;
        locationClient = null;
    }

    private void smallMap() {
        float zoomLevel = baiduMap.getMapStatus().zoom;
        if (zoomLevel > 1) {
            baiduMap.setMapStatus(MapStatusUpdateFactory.zoomOut());
            imageView_mapsmall.setEnabled(true);
        } else {
            imageView_mapsmall.setEnabled(false);
            Toast.makeText(ShowCrawlActivity.this, "已经缩至最小！", Toast.LENGTH_SHORT).show();
        }
    }

    private void largeMap() {
        float zoomLevel = baiduMap.getMapStatus().zoom;

        if (zoomLevel <= 24) {
//					MapStatusUpdateFactory.zoomIn();
            baiduMap.setMapStatus(MapStatusUpdateFactory.zoomIn());
            imageView_maplarg.setEnabled(true);
        } else {
            Toast.makeText(ShowCrawlActivity.this, "已经放至最大！", Toast.LENGTH_SHORT).show();
            imageView_maplarg.setEnabled(false);
        }
    }


    private void initLocation() {
        locationClient = new LocationClient(ShowCrawlActivity.this);
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


//        baiduMap.showInfoWindow(infoWindow);
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
        baiduMap.setMyLocationEnabled(false);   //关闭
        locationClient.stop();              //停止定位
    }

    /**
     * 显示围栏在地图上
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
                LatLng latLng = new LatLng(mLatitude, mLongtitude);
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
                baiduMap.animateMapStatus(msu);
            }
        });
    }


    /**
     * @param latLng 画围栏
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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            quitShow();
        }
        return super.onKeyDown(keyCode, event);
    }


    private void createWindow(LinearLayout linearLayout_window) {
        CrawlViewHolder holder = null;
        if (linearLayout_window.getTag() == null) {
            holder = new CrawlViewHolder();
            holder.textView_name = (TextView) linearLayout_window.findViewById(R.id.text_crawltypes);
            holder.textView_type = (TextView) linearLayout_window.findViewById(R.id.text_crawlsss);
            holder.textView_limit = (TextView) linearLayout_window.findViewById(R.id.text_warnlocastlits);
            holder.textView_time = (TextView) linearLayout_window.findViewById(R.id.text_warnlocastime);
            holder.textView_fs = (TextView) linearLayout_window.findViewById(R.id.text_warnlocastlitwarn);
            holder.textView_zt = (TextView) linearLayout_window.findViewById(R.id.text_crawlboolen);
        }
        holder.textView_name.setText(name);
        switch (type){
            case 0:
                holder.textView_type.setText("进围栏");
                break;
            case 1:
                holder.textView_type.setText("出围栏");
                break;
            case 2:
                holder.textView_type.setText("进出围栏");
                break;
        }
        holder.textView_time.setText(time);
        holder.textView_limit.setText(cra + "米");
        switch (AppCons.ORDERBEN.getFenceAlarmType()){
            case 0:
                holder.textView_fs.setText("平台");
                break;
            case 1:
                holder.textView_fs.setText("平台 + 短信");
                break;
            case 2:
                holder.textView_fs.setText("平台 + 短信 + 电话");
                break;
            case 3:
                holder.textView_fs.setText("平台 + 电话");
                break;
        }
        if (AppCons.ORDERBEN.isFenceState()){
            holder.textView_zt.setText("打开");
        }else {
            holder.textView_zt.setText("关闭");
        }
    }

    static class CrawlViewHolder {
        private TextView textView_type;
        private TextView textView_name;
        private TextView textView_time;
        private TextView textView_limit;
        private TextView textView_fs;
        private TextView textView_zt;
    }

}
