package com.example.maptest.mycartest.UI;

import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;

import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.example.maptest.mycartest.Bean.DisBean;
import com.example.maptest.mycartest.New.NewHttpUtils;
import com.example.maptest.mycartest.New.TrackBean;
import com.example.maptest.mycartest.New.TrickListBean;
import com.example.maptest.mycartest.New.WeiboDialogUtils;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.BaiDuMap;
import com.example.maptest.mycartest.Utils.BaseActivity;
import com.example.maptest.mycartest.Utils.DateChangeUtil;
import com.example.maptest.mycartest.Utils.EventHistory;
import com.example.maptest.mycartest.Utils.UtcDateChang;
import com.example.maptest.mycartest.Utils.VerticalSeekBar;
import com.example.maptest.mycartest.Utils.http.ResponseCallback;
import com.google.gson.Gson;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Timer;


/**
 * Created by ${Author} on 2017/3/24.
 * Use to  播放历史轨迹
 */

public class PlayTeaceActivity extends BaseActivity implements View.OnClickListener {
    private MapView mapView;
    private BaiduMap baiduMap;
    private ImageView imageView_quit, imageView_play;
    private TextView textView_replay, textView_speed, textView_distance, textView_direct, textView_location;
    private SeekBar seekBar_play;
    private int index = 0,addPage = 2;
    private boolean flag;
    private List<LatLng> points = new ArrayList<>();// 所有路线点集合
    private List<LatLng> addpoints = new ArrayList<>();// 所有路线点集合
    private List<LatLng> speeds = new ArrayList<>();
    private Marker posMarker = null, sMarker,startMarker,endMarker;
    private BitmapDescriptor icon, icons, iconb, icone;//标注图标
    private List<TrickListBean> trackList = new ArrayList<>();
    private List<TrickListBean> pageList = new ArrayList<>();
    private List<DisBean> disList = new ArrayList<>();
    private List<Marker> markerList = new ArrayList<>();
    private List<Overlay> overlayList = new ArrayList<>();
    private LocationClient locationClient;
    private int i = 4;
    private float dis = 0;
    private Timer timer = null;
    private VerticalSeekBar seekBar;
    private OverlayOptions ooPolyline, ooPolylineSpeed;
    //添加覆盖物
    private static Overlay fenceOverlay = null;
    private boolean isFirst = true;
    private MyMarkerClickListener markerClickListener;
    private InfoHolder holder = null;
    private LinearLayout baidumap_infowindow;
    private InfoWindow infoWindow;
    private String stopTime,peroidTime;
    private int indexs;
    private Dialog dialog;
    Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                if (dialog != null){
                    WeiboDialogUtils.closeDialog(dialog);
                }
                imageView_play.setImageResource(R.drawable.icon_lishihuifang_bofang);
                flag = true;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        start();        //开始刻画
                    }
                }).start();
            }
            if (msg.what == 2) {
                flag = false;       //暂停
                imageView_play.setImageResource(R.drawable.icon_lishihuifang_zanting);
            }
            if (msg.what == 3) {
                OverlayOptions ooA = new MarkerOptions().position(new LatLng(trackList.get(trackList.size() - 1).getBdLat(), trackList.get(trackList.size() - 1).getBdLon()))
                        .icon(icone).zIndex(9).draggable(true);
                endMarker = (Marker) baiduMap.addOverlay(ooA);
                seekBar_play.setProgress(points.size() - 1);
                posMarker.setPosition(new LatLng(trackList.get(trackList.size() - 1).getBdLat(), trackList.get(trackList.size() - 1).getBdLon()));
                textView_speed.setText(trackList.get(trackList.size() - 1).getSpeed() + "km/h");
                textView_distance.setText(disList.get(disList.size() - 1).getDis() + "km");
                textView_location.setText(UtcDateChang.UtcDatetoLocaTime(trackList.get(trackList.size() - 1).getUtcTime()));
                textView_direct.setText(corNers(trackList.get(trackList.size() - 1).getCourse()));
                imageView_play.setImageResource(R.drawable.icon_lishihuifang_zanting);
                try {
                    flag = false;
                    Thread.sleep(200);
                    index = 0;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (msg.what == 4) {
                if (overlayList != null && overlayList.size() > 0) {
                    for (Overlay overlay : overlayList) {
                        overlay.remove();
                    }
                }
                baiduMap.removeMarkerClickListener(markerClickListener);
                if (markerList != null && markerList.size() != 0) {
                    for (Marker marker : markerList) {
                        marker.remove();
                    }
                }

                dis = 0;
                try {
                    flag = false;
                    index = 0;
                    posMarker.setPosition(points.get(index));
                    Thread.sleep(500);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                imageView_play.setImageResource(R.drawable.icon_lishihuifang_bofang);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        flag = true;
                        start();        //开始刻画
                    }
                }).start();
            }
            if (msg.what == 6) {
                flag = false;       //暂停
                imageView_play.setImageResource(R.drawable.icon_lishihuifang_zanting);

                requestByPage(addPage);
            }

            return false;
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SDKInitializer.initialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playtrace);
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        initView();
        initLocation();
        initClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFirst) {
            trackList.addAll(AppCons.trickList);
//            SharedPreferences sp = getSharedPreferences("mylist", Context.MODE_PRIVATE);
//            String liststr = sp.getString("mylistStr", "");
//            if (!TextUtils.isEmpty(liststr)) {
//                try {
//                    trackList = SaveList.String2SceneList(liststr);
            removeLowSpead();
////                    initTrack();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
            isFirst = false;
        } else {

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (index != 0 && index != trackList.size()) {
            Message message = new Message();
            message.what = 2;
            handler.sendMessage(message);
        }
    }

    private void initClick() {
        textView_replay.setOnClickListener(this);
        imageView_quit.setOnClickListener(this);
        imageView_play.setOnClickListener(this);
    }

    private void initView() {
        markerClickListener = new MyMarkerClickListener();
        baidumap_infowindow = (LinearLayout) LayoutInflater.from(PlayTeaceActivity.this).inflate(R.layout.item_history, null);
        seekBar = (VerticalSeekBar) findViewById(R.id.seekbar_playspeed);
        mapView = (MapView) findViewById(R.id.player_mapview);
        mapView.showZoomControls(false);        //隐藏地图自带的控件
        mapView.showScaleControl(false);        //隐藏比例尺
        icons = BitmapDescriptorFactory.fromResource(R.drawable.icon_stop);
        iconb = BitmapDescriptorFactory.fromResource(R.drawable.icon_star);
        icone = BitmapDescriptorFactory.fromResource(R.drawable.icon_end);
        baiduMap = mapView.getMap();
        baiduMap.setTrafficEnabled(false);
        textView_replay = (TextView) findViewById(R.id.text_replay);
        textView_speed = (TextView) findViewById(R.id.text_movespeed);
        textView_distance = (TextView) findViewById(R.id.text_movedistace);
        textView_direct = (TextView) findViewById(R.id.text_movedirection);
        textView_location = (TextView) findViewById(R.id.text_movetime);
        imageView_play = (ImageView) findViewById(R.id.image_players);

        imageView_quit = (ImageView) findViewById(R.id.image_playerquits);
        seekBar_play = (SeekBar) findViewById(R.id.play_seekBar);
        MapStatusUpdate Msu = MapStatusUpdateFactory.zoomTo(14.0f);         //地图比例
        baiduMap.setMapStatus(Msu);
        baiduMap.setTrafficEnabled(false);
        seekBar_play.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (progress == points.size() - 1) {
                    if (flag) {
                        index = points.size() - 1;
                        Message message = new Message();        //如果相等，执行完毕
                        message.what = 3;
                        handler.sendMessage(message);
                    } else {

                    }
                } else {
                    index = progress;
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                i = progress;
                Log.e("sppedd", i + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        baiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                baiduMap.hideInfoWindow();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
    }

    /**
     * 初始化地图相关，定位等
     */
    private void initLocation() {
        locationClient = new LocationClient(PlayTeaceActivity.this);
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd0911");
        option.setIsNeedAddress(true);
        option.setOpenGps(true);    //开启GPS
        option.setScanSpan(1000);
        locationClient.setLocOption(option);
    }


    /**
     * 剔除速度小于5的点
     */
    private void removeLowSpead() {
        points.add(0, new LatLng(trackList.get(0).getBdLat(), trackList.get(0).getBdLon()));
        if (trackList != null && trackList.size() > 2) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < trackList.size() ; i++) {
                        if(i < trackList.size() - 1){
                            if ((trackList.get(i).getBdLat() == trackList.get(i + 1).getBdLat()) && trackList.get(i).getBdLon() == trackList.get(i + 1).getBdLon()){

                            } else {
                                points.add(new LatLng(trackList.get(i).getBdLat(), trackList.get(i).getBdLon()));
                                dis += disRides(trackList.get(i), trackList.get(i +  1));
                                DisBean bean = new DisBean(new DecimalFormat("###,###,###.##").format((dis) / 1000));
                                disList.add(bean);
                            }
                        }else{
                                points.add(new LatLng(trackList.get(i -1).getBdLat(), trackList.get(i - 1).getBdLon()));
                                dis += disRides(trackList.get(i - 2), trackList.get(i - 1));
                                DisBean bean = new DisBean(new DecimalFormat("###,###,###.##").format((dis) / 1000));
                                disList.add(bean);
                        }

                    }
                    initOverlay();
                    LatLng l = new LatLng(points.get(0).latitude, points.get(0).longitude);
                    OverlayOptions ooA = new MarkerOptions().position(l)
                            .icon(iconb).zIndex(9).draggable(true);
                    startMarker = (Marker) baiduMap.addOverlay(ooA);
                    MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(l);
                    baiduMap.setMapStatus(u);//移动地图中心到此点
                    seekBar_play.setMax(points.size() - 1);
                }
            }).start();
            Log.e("size","points : " + points.size() + "  trackList :" + trackList.size() );
        }

    }



    /**
     * 画出播放的路径
     */
    public void initOverlay() {

        if (posMarker == null) {
            addmarker(points.get(0));
        }
        if (points.size() > 1) {
            ooPolyline = new PolylineOptions().width(10)
                    .color(Color.parseColor("#00BFFF")).points(points);
            baiduMap.addOverlay(ooPolyline);
        }
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(points.get(0));
        baiduMap.setMapStatus(u);
    }

    public void addOverlay() {
        ooPolyline = new PolylineOptions().width(10)
                .color(Color.parseColor("#00BFFF")).points(addpoints);
        baiduMap.addOverlay(ooPolyline);

    }

    /**
     * @param view imageview点击事件
     */
    public void resetOverlay(View view) {
        ImageView imageView = (ImageView) view;
        if (!flag) {
            if (index == points.size()) {
                baiduMap.clear();
                index = 0;
            } else {
//                imageView.setImageResource(R.drawable.icon_lishihuifang_bofang);
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }

        } else {

            Message message = new Message();
            message.what = 2;
            handler.sendMessage(message);
        }
    }

    /**
     * 轨迹回放
     */
    public void start() { // 画轨迹

        if (flag) {
            if (points.size() < 2) {
            } else {
                posMarker.setPosition(points.get(index));
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(points.get(index));
                baiduMap.setMapStatus(u);
                if (trackList.get(index).getSpeed() > 100 && (index + 1 <= points.size() - 1)) {
                    speedPoly(points.get(index), points.get(index + 1));
                }
                    if (index + 1 <= trackList.size() - 1) {
                        long dates = (trackList.get(index + 1).getUtcTime() - trackList.get(index).getUtcTime());  //判断是否停止
                        if (dates >= 600000) {               //如果停止时间超过100分钟
                            OverlayOptions ooA = new MarkerOptions().position(new LatLng(trackList.get(index).getBdLat(), trackList.get(index).getBdLon()))
                                    .icon(icons).zIndex(9).draggable(true);
                            sMarker = (Marker) baiduMap.addOverlay(ooA);
                            String mydatas = DateChangeUtil.getdata(dates);
                            Bundle bundle = new Bundle();
                            bundle.putString("period",UtcDateChang.UtcDatetoLocaTime(trackList.get(index).getUtcTime()) + "\n - " + UtcDateChang.UtcDatetoLocaTime(trackList.get(index + 1).getUtcTime()));
                            bundle.putInt("position", index);
                            bundle.putString("time", mydatas);
                            sMarker.setExtraInfo(bundle);
                            markerList.add(sMarker);
                            baiduMap.setOnMarkerClickListener(markerClickListener);
                        }
                    }
                try {
                    Thread.sleep((i + 1) * 100); //控制播放速度
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (flag) {

                            if (index > 1) {
                                if (index >= disList.size()){
                                    textView_speed.setText(trackList.get(disList.size() - 1).getSpeed() + "km/h");
                                    textView_distance.setText((disList.get(disList.size() - 1).getDis()) + "km");
                                    textView_location.setText(UtcDateChang.UtcDatetoLocaTime(trackList.get(disList.size() - 1).getUtcTime()));
                                    textView_direct.setText(corNers(trackList.get(disList.size() - 1).getCourse()));
                                }else {
                                    textView_speed.setText(trackList.get(index-1).getSpeed() + "km/h");
                                    textView_distance.setText((disList.get(index - 1).getDis()) + "km");
                                    textView_location.setText(UtcDateChang.UtcDatetoLocaTime(trackList.get(index - 1).getUtcTime()));
                                    textView_direct.setText(corNers(trackList.get(index - 1).getCourse()));
                                }

                            }

                        } else {
                        }
                    }
                });
                seekBar_play.setProgress(index);
                index++;        //执行一次index加一
                if (points.size() != 0 && points != null && index != ((points.size()) - 1)) {
                    if (index >= (float)trackList.size() * (4f/5f) && (addPage <= AppCons.trickCountBean.getTotalPages())){
                        Message message = new Message();    //先暂停
                        message.what = 6;
                        handler.sendMessage(message);
                    }else {
                        start(); //如果index不等于数据源的长度，继续执行
                    }
                } else {
                    flag = false;
                    if (points.size() != 0 && points != null) {
                        Message message = new Message();        //如果相等，执行完毕
                        message.what = 3;
                        handler.sendMessage(message);
                    } else {
                    }
                }
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_players:
                if (points != null && points.size() > 2) {
                    resetOverlay(imageView_play);
                }
                break;
            case R.id.image_playerquits:
                try {
                    quit();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.text_replay:
                if (points != null && points.size() > 2) {
                    replay();
                } else {

                }
                break;
        }
    }

    /**
     * 重播事件
     */
    private void replay() {
        Message message = new Message();        //重播
        message.what = 4;
        handler.sendMessage(message);

    }

    /**
     * @param latLng 在latlng处添加Marker
     */
    private void addmarker(LatLng latLng) {
        icon = BitmapDescriptorFactory.fromResource(R.drawable.icon_lshf_circle);
        OverlayOptions ooA = new MarkerOptions().position(latLng)
                .icon(icon).zIndex(9).draggable(true);
        posMarker = (Marker) (baiduMap.addOverlay(ooA));

        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        flag = false;
        stopTime = null;
        infoWindow = null;
        baidumap_infowindow = null;
        holder = null;
        if (icon != null) {
            icon.recycle();
            icon = null;
        }
        if (icone != null) {
            icone.recycle();
            icone = null;
        }
        if (iconb != null) {
            iconb.recycle();
            iconb = null;
        }
        if (icons != null) {
            icons.recycle();
            icons = null;
        }

        timer = null;
        if (posMarker != null) {
            posMarker.remove();
        }
        if (ooPolyline != null) {
            ooPolyline = null;
        }
        mapView.onDestroy();
        disList = null;
        points = null;
        trackList = null;
    }

    private void quit() throws InterruptedException {

        flag = false;
        synchronized (handler){
            if (index != 0 && index != trackList.size()) {
                Message message = new Message();
                message.what = 2;
                handler.sendMessage(message);
            }
        }
        index = 0;
        dis = 0;

        trackList.clear();
        disList.clear();
        if (posMarker != null) {
            posMarker = null;
        }
        points.clear();
        speeds.clear();
        overlayList.clear();
        AppCons.trickCountBean = null;
        addPage = 2;
        AppCons.trickList.clear();
        speeds = null;
        fenceOverlay = null;
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        handler.removeCallbacksAndMessages(null);
        handler = null;
        isFirst = true;
        seekBar = null;
        baiduMap = null;

        imageView_play = null;
        imageView_quit = null;
        locationClient = null;

        textView_location = null;
        textView_speed = null;
        textView_distance = null;
        textView_direct = null;
        textView_replay = null;

        finish();
        System.gc();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            synchronized (handler){
                if (index != 0 && index != trackList.size()) {
                    Message message = new Message();
                    message.what = 2;
                    handler.sendMessage(message);
                }
            }

            try {
                quit();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * @param bean1
     * @param bean2
     * @return 计算两点之间的直线距离
     */
    private float disRides(TrickListBean bean1, TrickListBean bean2) {
        return (float) BaiDuMap.GetShortDistance(bean1.getBdLon(), bean1.getBdLat(), bean2.getBdLon(), bean2.getBdLat());
    }


    /**
     * @param x
     * @return 计算方向
     */
    private String corNers(int x) {
        String s = null;
        if (x == 0) {
            s = "正北";
        } else if (0 < x && x < 90) {
            s = "东北";
        } else if (x == 90) {
            s = "正东";
        } else if (90 < x && x < 180) {
            s = "东南";
        } else if (x == 180) {
            s = "正南";
        } else if (180 < x && x < 270) {
            s = "西南";
        } else if (x == 270) {
            s = "正西";
        } else {
            s = "西北";
        }
        return s;
    }

    private Bitmap getBitmapFromView(View view) {
        view.destroyDrawingCache();
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    private class MyMarkerClickListener implements BaiduMap.OnMarkerClickListener {

        @Override
        public boolean onMarkerClick(Marker marker) {
            if((startMarker != null && marker.equals(startMarker) )|| (endMarker != null && marker.equals(endMarker)) ||(startMarker != null && startMarker.equals(posMarker))){

            }else {
                try{
                    stopTime = marker.getExtraInfo().getString("time");
                    peroidTime = marker.getExtraInfo().getString("period");
                    indexs = marker.getExtraInfo().getInt("position");
                    getAdd(trackList.get(indexs));
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
            return false;
        }
    }


    private void createInfo(String add) {

        if (baidumap_infowindow.getTag() == null) {
            holder = new InfoHolder();
            holder.textView_stopTime = (TextView) baidumap_infowindow.findViewById(R.id.text_ltimet);
            holder.textView_stopLoc = (TextView) baidumap_infowindow.findViewById(R.id.text_locas);
            holder.text_ltimeperoid = (TextView) baidumap_infowindow.findViewById(R.id.text_ltimeperoid);
            baidumap_infowindow.setTag(holder);
        }
        holder = (InfoHolder) baidumap_infowindow.getTag();
        holder.textView_stopTime.setText(stopTime);
        holder.textView_stopLoc.setText(add);
        holder.text_ltimeperoid.setText(peroidTime);
    }


    public class InfoHolder {
        private TextView textView_stopTime;
        private TextView textView_stopLoc;
        private TextView text_ltimeperoid;
    }


    public void getAdd(TrickListBean waysBean) {       //将marker对象传入

        GeoCoder geoCoder = GeoCoder.newInstance();
        //设置反地理编码位置坐标
        ReverseGeoCodeOption op = new ReverseGeoCodeOption();
        LatLng latLng = new LatLng(waysBean.getBdLat(), waysBean.getBdLon());
        op.location(latLng);
        geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
            }

            /**
             * @param reverseGeoCodeResult
             * EventBus抛出有地理位置信息的对象
             */
            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                String addrs = reverseGeoCodeResult.getAddress() + "/" + reverseGeoCodeResult.getSematicDescription();
                EventBus.getDefault().post(new EventHistory(addrs));

            }
        });
        geoCoder.reverseGeoCode(op);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getLoca(EventHistory eventHistory) {
        String add = eventHistory.getLocation();
        createInfo(add);
        infoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(baidumap_infowindow), new LatLng(trackList.get(indexs).getBdLat(), trackList.get(indexs).getBdLon()), -30, null);
        baiduMap.showInfoWindow(infoWindow);

    }


    public void speedPoly(LatLng latLng1, LatLng latLng2) {
        speeds.clear();
        speeds.add(latLng1);
        speeds.add(latLng2);
        ooPolylineSpeed = new PolylineOptions().width(10)
                .color(Color.parseColor("#ff0000")).points(speeds);
        fenceOverlay = baiduMap.addOverlay(ooPolylineSpeed);
        overlayList.add(fenceOverlay);
    }

    private void requestByPage(final int i) {
        dialog = WeiboDialogUtils.createLoadingDialog(PlayTeaceActivity.this,"加载中");
        new Thread(new Runnable() {
            @Override
            public void run() {
                TrackBean bean = new TrackBean(AppCons.locationListBean.getTerminalID(), AppCons.sTime,AppCons.eTime, 400, i,AppCons.locationType);
                Gson gson = new Gson();
                String obj = gson.toJson(bean);
                if (AppCons.trackType == 1){
                    NewHttpUtils.querrTrack(obj, PlayTeaceActivity.this, new ResponseCallback() {
                        @Override
                        public void TaskCallBack(Object object) {

                            pageList.clear();
                            pageList.addAll((Collection<? extends TrickListBean>) object);  //请求的新的集合
                            if (pageList.size() > 0){
                                trackList.addAll(pageList);
                                addNewList(pageList);
                                addPage ++ ;
                            }

                        }

                        @Override
                        public void FailCallBack(Object object) {   //失败

                        }
                    });
                }else {
                    NewHttpUtils.querrOldTrack(obj, PlayTeaceActivity.this, new ResponseCallback() {
                        @Override
                        public void TaskCallBack(Object object) {
                            pageList.clear();
                            pageList.addAll((Collection<? extends TrickListBean>) object);  //请求的新的集合
                            if (pageList.size() > 0){
                                trackList.addAll(pageList);
                                addNewList(pageList);
                                addPage ++ ;
                            }
                        }

                        @Override
                        public void FailCallBack(Object object) {   //失败

                        }
                    });
                }

            }
        }).start();

    }

    private void addNewList(final List<TrickListBean> list) {
        addpoints.clear();
        if (list != null && list.size() > 2) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 1; i < list.size(); i++) {
                        if (i < list.size() - 1){
                            if ((list.get(i).getBdLat() == list.get(i + 1).getBdLat()) && list.get(i).getBdLon() == list.get(i + 1).getBdLon()){  //如果当前点的速度小于5，后一个点的速度也小于5，剔除后面那个点

                            } else {     //如果速度都大于5
                                addpoints.add(new LatLng(list.get(i).getBdLat(), list.get(i).getBdLon()));
                                dis += disRides(list.get(i), list.get(i + 1));
                                DisBean bean = new DisBean(new DecimalFormat("###,###,###.##").format((dis) / 1000));
                                disList.add(bean);
                            }
                        }else{

                                addpoints.add(new LatLng(list.get(i - 1).getBdLat(), list.get(i - 1).getBdLon()));
                                dis += disRides(list.get(i - 2), list.get(i - 1));
                                DisBean bean = new DisBean(new DecimalFormat("###,###,###.##").format((dis) / 1000));
                                disList.add(bean);

                        }

                    }
                    addpoints.add(0,points.get(points.size() -1));
                    points.addAll(addpoints);
                    Log.e("添加了数据源之后","disList :" + disList.size() + " points : " + points.size() + " trackList : " + trackList.size());
                    addOverlay();
                    seekBar_play.setMax(points.size() - 1);
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                }
            }).start();

        }
    }
}
