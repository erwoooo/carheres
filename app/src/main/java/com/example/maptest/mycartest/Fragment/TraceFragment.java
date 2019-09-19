package com.example.maptest.mycartest.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.lbsapi.model.BaiduPanoData;
import com.baidu.lbsapi.model.BaiduPoiPanoData;
import com.baidu.lbsapi.panoramaview.PanoramaRequest;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.maptest.mycartest.Bean.Gps;
import com.example.maptest.mycartest.BuildConfig;
import com.example.maptest.mycartest.Entity.EditLocation;
import com.example.maptest.mycartest.New.LocationListBean;
import com.example.maptest.mycartest.New.NewHttpUtils;
import com.example.maptest.mycartest.Permission.PermissionFail;
import com.example.maptest.mycartest.Permission.PermissionGen;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.UI.InformationActivity;
import com.example.maptest.mycartest.UI.PanoramaDemoActivityMain;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.BaiDuMap;
import com.example.maptest.mycartest.Utils.DateChangeUtil;
import com.example.maptest.mycartest.Utils.MyGlideUrl;
import com.example.maptest.mycartest.Utils.PositionUtil;
import com.example.maptest.mycartest.Utils.UtcDateChang;
import com.example.maptest.mycartest.Utils.http.ResponseCallback;
import com.google.gson.Gson;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.maptest.mycartest.Utils.AppCons.TELL;


/**
 * Created by ${Author} on 2017/3/13.
 * Use to  车辆追踪fragment
 */

public class TraceFragment extends Fragment implements View.OnClickListener {
    private View view,view_pic;
    private ImageView imageView_trace, imageView_locationcar, imageView_typemap, imageView_trafficmap, imageView_largecar, imageView_smallcar;
    private ImageView imageView_quit,imageView_street,image_picture;
    private ImageView text_bj;
    private TextureMapView mapView;
    private BaiduMap baiduMap;
    private boolean isFirstIn = true, traffic = true, type = true;
    private MyLocationListener myLocationListener;
    private BitmapDescriptor mIconLocation;
    private BitmapDescriptor mMaker;
    private double mLatitude;
    private double mLongtitude;
    private LocationClient locationClient;
    private LinearLayout baidumap_infowindow;
    private InfoWindowHolder holder = null;
    private InfoWindow infoWindow = null;
    private double dis = 0;
    private String distance;
    private Dialog dialog_picture;
    private String mPublicPhotoPath;
    private boolean rh = true;
    private Bundle bundle = new Bundle();
    private Timer timer;
    private Map<Object,Object> alarmsMap = new HashMap<>();
    private LocationListBean locationListBean;
    private ImageView image_trace_pt;
    private OnMarkerClickListeners onMarkerClickListener;
    private int index = 0;
    private boolean isShow,isResume;
    private String startAddress,endAddress;
    private AlertDialog navigationDialog,alarmsDialog;
    InformationActivity activity = InformationActivity.instance;
    private String LTAG = "街景";
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    disRide(new LatLng(mLatitude, mLongtitude), new LatLng(locationListBean.getLocation().getBdLat(),locationListBean.getLocation().getBdLon()));
                    createInfoWindow(baidumap_infowindow, locationListBean);
                    infoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(baidumap_infowindow), new LatLng(locationListBean.getLocation().getBdLat(),locationListBean.getLocation().getBdLon()), -30, null);
                    baiduMap.showInfoWindow(infoWindow);
                    break;
                case 9:
                    Toast.makeText(getActivity(),"没有查询到街景数据数据",Toast.LENGTH_SHORT).show();
                    break;
                case 10:
                    Intent intent = new Intent();
                    bundle.putSerializable("street",AppCons.locationListBean);
                    intent.putExtras(bundle);
                    startActivity(new Intent(getActivity(), PanoramaDemoActivityMain.class).putExtras(bundle));
                    break;
            }
        }
    };
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * @param activity
     * 获取宿主activity的车辆对象
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SDKInitializer.initialize(getActivity().getApplicationContext());
        view = inflater.inflate(R.layout.fragment_trace, null);
        PermissionGen.with(TraceFragment.this)
                .addRequestCode(100)
                .permissions(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                .request();
        initView();
        initClick();
        initLocation();
        locationListBean = AppCons.locationListBean;
        addInfo(AppCons.locationListBean);
        disPlay();
        return view;
    }
    private void initView() {
        text_bj = view.findViewById(R.id.text_bj);
        image_trace_pt = view.findViewById(R.id.image_trace_pt);
        imageView_quit = (ImageView) view.findViewById(R.id.image_quit_trace);
        imageView_street = (ImageView) view.findViewById(R.id.image_reftrace);
        mapView = (TextureMapView) view.findViewById(R.id.trace_bmapView);
        mapView.showZoomControls(false);        //隐藏地图自带的控件
        mapView.showScaleControl(false);        //隐藏比例尺
        baiduMap = mapView.getMap();
        MapStatusUpdate Msu = MapStatusUpdateFactory.zoomTo(15.0f);         //地图比例
        baiduMap.setMapStatus(Msu);
        baidumap_infowindow = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.item_inforwindowtrace, null); //实例化infowindow
        baidumap_infowindow.getBackground().setAlpha(220);
        onMarkerClickListener = new TraceFragment.OnMarkerClickListeners();
        imageView_trace = (ImageView) view.findViewById(R.id.image_tracecar);
        imageView_locationcar = (ImageView) view.findViewById(R.id.image_locationcar);
        imageView_typemap = (ImageView) view.findViewById(R.id.image_typecar);
        imageView_trafficmap = (ImageView) view.findViewById(R.id.image_trafficar);
        imageView_smallcar = (ImageView) view.findViewById(R.id.image_maps);
        imageView_largecar = (ImageView) view.findViewById(R.id.image_maple);
        endAddress = AppCons.locationListBean.getAddress();
        if (BuildConfig.app_tag.equals("lcaf")){
            text_bj.setVisibility(View.VISIBLE);
        }else {
            text_bj.setVisibility(View.GONE);
        }
    }

    private void initClick() {
        text_bj.setOnClickListener(this);
        image_trace_pt.setOnClickListener(this);
        imageView_street.setOnClickListener(this);
        imageView_quit.setOnClickListener(this);
        imageView_trace.setOnClickListener(this);
        imageView_locationcar.setOnClickListener(this);
        imageView_typemap.setOnClickListener(this);
        imageView_trafficmap.setOnClickListener(this);
        imageView_largecar.setOnClickListener(this);
        imageView_smallcar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_reftrace:
                if (locationListBean.getState() == 3 || (locationListBean.getLocation().getBdLat() == 0 && locationListBean.getLocation().getBdLon() == 0)){
                    Toast.makeText(getContext(),"当前设备未激活或未定位",Toast.LENGTH_SHORT).show();
                }else {
                    testPanoramaRequest();

//
                }
                break;
            case R.id.image_quit_trace:
                getActivity().finish();
                break;
            case R.id.image_tracecar:
                if (locationListBean.getState() == 3 || (locationListBean.getLocation().getBdLat() == 0 && locationListBean.getLocation().getBdLon() == 0)){
                    Toast.makeText(getContext(),"当前设备未激活或未定位",Toast.LENGTH_SHORT).show();
                }else {
                    showNavigationDialg();
//                    try{
//                        onBaidu(new LatLng(locationListBean.getLocation().getBdLat(),locationListBean.getLocation().getBdLon()),startAddress,endAddress);
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }

                }
                break;
            case R.id.text_bj:
                showAlarms();
                break;

            case R.id.image_locationcar:
                centerToMyLocation();
                break;
            case R.id.image_typecar:
                typemaps();
                break;
            case R.id.image_trafficar:
                maptraffic();
                break;
            case R.id.image_maps:
                largemaps();
                break;
            case R.id.image_maple:
                smallmaps();
                break;
            case R.id.image_trace_pt:
//                if (activity == null ){
//                    activity = InformationActivity.instance;
//                }
//                activity.showPicDialog();
                showPicDialog();
                break;
        }
    }

    /**
     * 检查是否存在街景
     */
    private void testPanoramaRequest() {

        new Thread(new Runnable() {

            @Override
            public void run() {
                PanoramaRequest panoramaRequest = PanoramaRequest.getInstance(getActivity());

                String pid = "01002200001307201550572285B";
                Log.e(LTAG, "PanoramaRecommendInfo");
                Log.i(LTAG, panoramaRequest.getPanoramaRecommendInfo(pid).toString());

                String iid = "978602fdf6c5856bddee8b62";
                Log.e(LTAG, "PanoramaByIIdWithJson");
                Log.i(LTAG, panoramaRequest.getPanoramaByIIdWithJson(iid).toString());

                // 通过百度经纬度坐标获取当前位置相关全景信息，包括是否有外景，外景PID，外景名称等
                double lat = locationListBean.getLocation().getBdLat();
                double lon = locationListBean.getLocation().getBdLon();
                BaiduPanoData mPanoDataWithLatLon = panoramaRequest.getPanoramaInfoByLatLon(lon, lat);
                Log.e(LTAG, "PanoDataWithLatLon");
                Log.i(LTAG, mPanoDataWithLatLon.getDescription());
                if (mPanoDataWithLatLon != null && mPanoDataWithLatLon.hasStreetPano()){
                    Message message = new Message();
                    message.what = 10;
                    handler.sendMessage(message);

                }else {
                    Message message = new Message();
                    message.what = 9;
                    handler.sendMessage(message);

                }
//                // 通过百度墨卡托坐标获取当前位置相关全景信息，包括是否有外景，外景PID，外景名称等
//                int x = 12948920;
//                int y = 4842480;
//                BaiduPanoData mPanoDataWithXy = panoramaRequest.getPanoramaInfoByMercator(x, y);
//
//                Log.e(LTAG, "PanoDataWithXy");
//                Log.i(LTAG, mPanoDataWithXy.getDescription());
//
//                // 通过百度地图uid获取该poi下的全景描述信息，以此来判断此UID下是否有内景及外景
//                String uid = "bff8fa7deabc06b9c9213da4";
//                BaiduPoiPanoData poiPanoData = panoramaRequest.getPanoramaInfoByUid(uid);
//                Log.e(LTAG, "poiPanoData");
//                Log.i(LTAG, poiPanoData.getDescription());
            }
        }).start();

    }


    private void showPicDialog(){
        if (locationListBean.getPicture().equals("")){
            Toast.makeText(getContext(),"当前设备没有车辆图片",Toast.LENGTH_SHORT).show();
            return;
        }
        if (dialog_picture == null){
            mPublicPhotoPath = locationListBean.getPicture();
            dialog_picture = new Dialog(getActivity(),R.style.ActionDialogStyle);
            view_pic = getActivity().getLayoutInflater().inflate(R.layout.dialog_picture,null);
            image_picture = view_pic.findViewById(R.id.image_picture);
            Glide.with(getActivity()).load(AppCons.locationListBean.getPicture()).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(image_picture);
            dialog_picture.setContentView(view_pic);
            dialog_picture.setCanceledOnTouchOutside(true);
            Window dialogWindow = dialog_picture.getWindow();
            dialogWindow.setGravity( Gravity.CENTER);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            WindowManager wm = (WindowManager) getActivity()
                    .getSystemService(Context.WINDOW_SERVICE);
            int width = wm.getDefaultDisplay().getWidth();
            int height = wm.getDefaultDisplay().getHeight();
            lp.height = (int) (height * 0.6);//设置Dialog距离底部的距离
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            Log.e("lp.height",lp.height + " ; lp.height: " + height);
            view_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog_picture.dismiss();
                }
            });
//       将属性设置给窗体
            dialogWindow.setAttributes(lp);
            dialog_picture.show();
        }else {
            if (mPublicPhotoPath.equals(locationListBean.getPicture())){
                if (dialog_picture != null && !dialog_picture.isShowing())
                dialog_picture.show();
            }else {
                mPublicPhotoPath = locationListBean.getPicture();
                if (dialog_picture != null && !dialog_picture.isShowing()){
                    Glide.with(this).load(new MyGlideUrl(locationListBean.getPicture())).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(image_picture);
                    dialog_picture.show();
                }

            }
        }


    }


    private void showAlarms(){
        if (alarmsDialog == null){
            alarmsDialog = new AlertDialog.Builder(getActivity())
                    .setIcon(R.drawable.iflytek_dialog_image)//设置标题的图片
                    .setTitle("一键报警")//设置对话框的标题
                    .setMessage("车辆一键报警")//设置对话框的内容
                    //设置对话框的按钮
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alarmsMap.clear();
                            alarmsMap.put("terminalID",locationListBean.getTerminalID());
                            alarmsMap.put("address",locationListBean.getAddress());
                            NewHttpUtils.querrAlarms(new Gson().toJson(alarmsMap), getActivity(), new ResponseCallback() {
                                @Override
                                public void TaskCallBack(Object object) {
                                    //成功了
                                    callNumber();

                                }

                                @Override
                                public void FailCallBack(Object object) {
                                    callNumber();
                                }
                            });
                            dialog.dismiss();
                        }
                    }).create();
            alarmsDialog.show();
        }else {
            alarmsDialog.show();
        }

    }


    private void callNumber() {
        if (TELL.equals("")){
            Toast.makeText(getContext(),"没有设置电话号码",Toast.LENGTH_SHORT).show();
            return;
            }
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + TELL));
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE},11);
                return;
            }else {
                startActivity(intent);
            }

    }


    /**
     * 地图缩放
     */
    private void smallmaps() {
        float zoomLevel = baiduMap.getMapStatus().zoom;
        if (zoomLevel > 4) {
            baiduMap.setMapStatus(MapStatusUpdateFactory.zoomOut());
            imageView_smallcar.setEnabled(true);
        } else {
            imageView_smallcar.setEnabled(false);
            Toast.makeText(getActivity(), "已经缩至最小！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 地图缩放
     */
    private void largemaps() {
        float zoomLevel = baiduMap.getMapStatus().zoom;
        if (zoomLevel <= 18) {
            baiduMap.setMapStatus(MapStatusUpdateFactory.zoomIn());
            imageView_largecar.setEnabled(true);
        } else {
            Toast.makeText(getActivity(), "已经放至最大！", Toast.LENGTH_SHORT).show();
            imageView_largecar.setEnabled(false);
        }
    }

    /**
     * 地图交通热图
     */
    private void maptraffic() {
        if (traffic) {
            baiduMap.setTrafficEnabled(true);
            imageView_trafficmap.setImageResource(R.drawable.icon_index_guanbishishilukuang);
            traffic = false;
        } else {
            baiduMap.setTrafficEnabled(false);
            imageView_trafficmap.setImageResource(R.drawable.icon_index_kaiqishishilukuang);
            traffic = true;
        }
    }

    /**
     * 地图类型
     */
    private void typemaps() {
        if (type) {
            baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
            imageView_typemap.setImageResource(R.drawable.icon_index_putongshitu_selected);
            type = false;
        } else {
            type = true;
            baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
            imageView_typemap.setImageResource(R.drawable.icon_index_putongshitu);

        }
    }

    @Override
    public void onStart() {
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
    public void onResume() {
        super.onResume();
        mapView.onResume();
        isResume = true;


        rh = true;

        Log.d("mapstate","onresume");
    }


    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        isResume = false;
        Log.d("mapstate","onpause");
        rh = false;
        if (navigationDialog != null){
            if (navigationDialog.isShowing()){
                navigationDialog.dismiss();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
        if (timer != null){
            timer.cancel();
            timer = null;
        }
        Log.d("mapstate","destory");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        index = 0;
        activity = null;
        baiduMap.removeMarkerClickListener(onMarkerClickListener);
        onMarkerClickListener = null;
        myLocationListener = null;
        locationClient = null;
        baidumap_infowindow = null;
        holder = null;
        infoWindow = null;
        if (mIconLocation != null)
            mIconLocation.recycle();
        mIconLocation = null;
        if (mMaker != null)
            mMaker.recycle();
        mMaker = null;
    }

    /**
     * 回到追踪者位置
     */
    private void centerToMyLocation() {         //回到我的位置
        LatLng latLng = new LatLng(mLatitude, mLongtitude);
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        baiduMap.animateMapStatus(msu);
    }

    /**
     * 初始化地图定位相关
     */
    private void initLocation() {
        locationClient = new LocationClient(getActivity().getApplicationContext());
        myLocationListener = new MyLocationListener();
        locationClient.registerLocationListener(myLocationListener);
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setOpenGps(true);    //开启GPS
        option.setScanSpan(10000);
        locationClient.setLocOption(option);
    }


    /**
     * 用户位置监听
     */
    private class MyLocationListener implements BDLocationListener {         //我的位置监听
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            startAddress = bdLocation.getAddrStr();
            MyLocationData data = new MyLocationData.Builder()
                    .accuracy(bdLocation.getRadius())
                    .latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude())
                    .build();

            baiduMap.setMyLocationData(data);
            MyLocationConfiguration configuration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, mIconLocation);
            baiduMap.setMyLocationConfigeration(configuration);
            mLatitude = bdLocation.getLatitude();       //更新经纬度
            mLongtitude = bdLocation.getLongitude();
            if (isFirstIn) {             //如果是第一次定位
                mLatitude = bdLocation.getLatitude();       //更新经纬度
                mLongtitude = bdLocation.getLongitude();
                LatLng latLng = new LatLng(AppCons.locationListBean.getLocation().getBdLat(), AppCons.locationListBean.getLocation().getBdLon());
                disRide(new LatLng(mLatitude,mLongtitude),latLng);
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
                baiduMap.animateMapStatus(msu);
                isFirstIn = false;
                createInfoWindow(baidumap_infowindow, AppCons.locationListBean);
                infoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(baidumap_infowindow), latLng, -30,null);
                baiduMap.showInfoWindow(infoWindow);
            }

        }



    }


    /**
     * 在被追踪的位置添加marker
     */
    private void addInfo(LocationListBean useBean) {
        baiduMap.clear();
        baiduMap.removeMarkerClickListener(onMarkerClickListener);  //每次刷新的时候移除对marker的监听
        if (useBean.getState() == 3 || (useBean.getLocation().getBdLat() == 0 && useBean.getLocation().getBdLon() == 0)){

        }else {
            LatLng latLng = null;
            Marker marker = null;
            OverlayOptions options;
            if (BuildConfig.app_tag.equals("lcaf")){
                initIconType(useBean.getState(),2);
            }else {
                initIconType(useBean.getState(),useBean.getDevice().getIconType());
            }
            Log.e("use",useBean.toString());
            latLng = new LatLng(useBean.getLocation().getBdLat(), useBean.getLocation().getBdLon());
            options = new MarkerOptions().position(latLng).icon(mMaker).zIndex(5);
            marker = (Marker) baiduMap.addOverlay(options);
            if (useBean.getLocation().getCourse() == 0){
                marker.setRotate(0);
            }else {
                marker.setRotate(-(useBean.getLocation().getCourse()));
            }
            MapStatus mapStatus = new MapStatus.Builder()
                    .target(latLng)
                    .build();
            MapStatusUpdate msu = MapStatusUpdateFactory.newMapStatus(mapStatus);
            baiduMap.setMapStatus(msu);
//        baiduMap.showInfoWindow(infoWindow);

            baiduMap.setOnMarkerClickListener(onMarkerClickListener);
        }

    }

    /**
     * @param baidumap_infowindow
     * @param bean
     * 创建显示被追踪车辆信息气泡框
     */
    private void createInfoWindow(LinearLayout baidumap_infowindow, LocationListBean bean) {     //创建弹出框，参数弹出框，当前点击的marker
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        Date aDate = DateChangeUtil.StrToDate(DateChangeUtil.timedateTotim(bean.getLocation().getUtcTime()));    /*定位时间    */
        Date bDate = DateChangeUtil.StrToDate(DateChangeUtil.timedateTotim(bean.getLocation().getStateTime()));     /*通讯时间    */
        Log.d("tttttt2",dis + "");
        if (baidumap_infowindow.getTag() == null) {
            holder = new InfoWindowHolder();
            holder.textView_tracename = (TextView) baidumap_infowindow.findViewById(R.id.text_tracecarname);
            holder.textView_tracenumber = (TextView) baidumap_infowindow.findViewById(R.id.text_tracecarimei);
            holder.textView_tracestoptime = (TextView) baidumap_infowindow.findViewById(R.id.text_traceltime);
            holder.textView_tracenowtime = (TextView) baidumap_infowindow.findViewById(R.id.text_tracentimes);
            holder.textView_tracelocation = (TextView) baidumap_infowindow.findViewById(R.id.text_tracenlocas);
            holder.textView_tracestatus = (TextView) baidumap_infowindow.findViewById(R.id.text_tracestate);
            holder.textView_tracegps = (TextView) baidumap_infowindow.findViewById(R.id.text_tracegps);
            holder.textView_tracefar = (TextView) baidumap_infowindow.findViewById(R.id.text_tracefar);
            holder.imageView_elec = (TextView) baidumap_infowindow.findViewById(R.id.image_electr);
            holder.imageView_gsm = (ImageView) baidumap_infowindow.findViewById(R.id.image_gsmtr);
            holder.textView_spr = (TextView) baidumap_infowindow.findViewById(R.id.text_spr);
            holder.text_vr = baidumap_infowindow.findViewById(R.id.text_vr);
            baidumap_infowindow.setTag(holder);
        }
        holder = (InfoWindowHolder) baidumap_infowindow.getTag();
        if (bean.getCarNumber() == null || bean.getCarNumber().equals("")){
            holder.textView_tracename.setText("IMEI:"+bean.getTerminalID());
        }else {
            holder.textView_tracename.setText("车牌号:"+bean.getCarNumber());
        }
        holder.textView_tracenumber.setText("设备名:"+bean.getNickname());
        if (bean.getState() == 1) {  //在线

            long dates = (bDate.getTime() - aDate.getTime());

            String mydatas = DateChangeUtil.getdata(dates);
            if (DateChangeUtil.getSec(dates) < 60){
                if (bean.getLocation().getSpeed() <= 10){
                    holder.textView_spr.setText("0km/h");
                    if (dates <= 100000){
                        holder.textView_tracestoptime.setText("静止");       //通讯时间
                    }else {
                        holder.textView_tracestoptime.setText("静止:" + mydatas);       //通讯时间
                    }
                }else {
                    holder.textView_tracestoptime.setText("行驶");       //通讯时间
                    holder.textView_spr.setText(bean.getLocation().getSpeed() + "km/h");
                }
            }else {       //补传
                holder.textView_spr.setText("0km/h");
                holder.textView_tracestoptime.setText("静止:" + mydatas);       //通讯时间
            }
        } else {
            long dates = (UtcDateChang.Local2UTC(curDate) - bDate.getTime());
            String mydatas = DateChangeUtil.getdata(dates);
            holder.textView_tracestoptime.setText("离线" + mydatas);       //通讯时间
        }
        holder.textView_tracenowtime.setText(UtcDateChang.UtcDatetoLocaTime(bean.getLocation().getStateTime()));
        holder.textView_tracelocation.setText(AppCons.ADDRESS);            //地理位置显示
//        if (bean.getLocation().getSpeed() > 10){
//            holder.textView_spr.setText(bean.getLocation().getSpeed() + "km/h");
//        }else {
//            holder.textView_spr.setText("0km/h");
//        }
        if (bean.getLocation().getVoltage() != -1){
            holder.text_vr.setText("车电压: " + bean.getLocation().getVoltage() + "V");
        }

        switch (bean.getDevice().getDeviceType()){
            case "G17":
            case "GT06":
            case "G16":
                holder.textView_tracestatus.setText("ACC : 无");
                holder.imageView_elec.setVisibility(View.GONE);
                break;
            case "518":
            case "GT06H":
            case "518T":
            case "GT09":
            case "GT19":
            case "K100":
            case "GM02":
            case "G16H":
            case "G17H":
            case "K100B":
            case "GM02D":
            case "BY015":
                if (bean.getLocation().isAcc()) {
                    holder.textView_tracestatus.setText("ACC : 开");
                } else {
                    holder.textView_tracestatus.setText("ACC : 关");
                }
                holder.imageView_elec.setVisibility(View.GONE);
                break;
            default:
                if (bean.getLocation().isAcc()) {
                    holder.textView_tracestatus.setText("ACC : 开");
                } else {
                    holder.textView_tracestatus.setText("ACC : 关");
                }
                holder.imageView_elec.setVisibility(View.VISIBLE);
                holder.imageView_elec.setText(BaiDuMap.getElect(bean.getLocation().getElectric() * 100) + "%");
                break;
        }
        
        
//        if (bean.getLocation().isAcc()){
//            holder.textView_tracestatus.setText("ACC: 开");
//        }else {
//            holder.textView_tracestatus.setText("ACC: 关");
//        }
        if (bean.getLocation().getLocationType().contains("Gps")){
            holder.textView_tracegps.setText("定位: 卫星定位");  //:bean.getLocation().getLocationType())
        }else if (bean.getLocation().getLocationType().contains("Bds")){
            holder.textView_tracegps.setText("定位: 北斗");  //:bean.getLocation().getLocationType())
        }else {
            holder.textView_tracegps.setText("定位: " + bean.getLocation().getLocationType());  //:bean.getLocation().getLocationType())
        }
        holder.textView_tracefar.setText("距离:"+distance+"km");
        holder.imageView_elec.setText(BaiDuMap.getElect(bean.getLocation().getElectric() * 100) + "%");
        switch (bean.getLocation().getGsm()){
            case 1:
                holder.imageView_gsm.setImageResource(R.drawable.icon_chache_xh1);
                break;
            case 2:
                holder.imageView_gsm.setImageResource(R.drawable.icon_chache_xh2);
                break;
            case 3:
                holder.imageView_gsm.setImageResource(R.drawable.icon_chache_xh3);
                break;
            case 4:
                holder.imageView_gsm.setImageResource(R.drawable.icon_chache_xh4);
                break;
        }
    }
    static class InfoWindowHolder{
        private TextView textView_tracename;
        private TextView textView_tracenumber;
        private TextView textView_tracestoptime;
        private TextView textView_tracenowtime;
        private TextView textView_tracelocation;
        private TextView textView_tracestatus;
        private TextView textView_tracegps;
        private TextView textView_tracefar;
        private TextView imageView_elec;
        private ImageView imageView_gsm;
        private TextView textView_spr;
        private TextView text_vr;

    }

    /**
     * 地图状态监听，地图状态改变，隐藏气泡框
     */
    private void disPlay(){
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
     * 百度marker点击事件
     *
     */
    private final class OnMarkerClickListeners implements BaiduMap.OnMarkerClickListener {

        @Override
        public boolean onMarkerClick(Marker marker) {
            Log.e("MARKER", "点击事件开始");
            getAdd(locationListBean);
            Log.e("MARKER", "点击事件结束");
            return false;
        }
    }

    /**
     * 获取地址
     * @param useBean
     */
    public void getAdd( final LocationListBean useBean) {       //将marker对象传入
        GeoCoder geoCoder = GeoCoder.newInstance();
        //设置反地理编码位置坐标
        ReverseGeoCodeOption op = new ReverseGeoCodeOption();
        LatLng latLng = new LatLng(useBean.getLocation().getBdLat(), useBean.getLocation().getBdLon());
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
                AppCons.ADDRESS = addrs;
                endAddress = reverseGeoCodeResult.getAddress();
                Message message = new Message();
                message.what = 0;
                handler.sendMessage(message);
            }
        });
        geoCoder.reverseGeoCode(op);
        Log.e("ADD",useBean.toString());


    }


    /**
     * @param packageName
     * @return
     * 根据报名检查百度地图客户端是否安装
     */
    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    /**
     * 6.0权限申请
     */
    @PermissionFail(requestCode = 100)
    public void failContact() {
        Toast.makeText(getActivity(), "未授予相关权限", Toast.LENGTH_SHORT).show();
    }
    @Override public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                                     int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        if (requestCode == 11 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + TELL));
            startActivity(intent);
        }

    }


    /**
     * @param latLng1
     * @param latLng2
     * @return
     * 计算两个经纬度之间的直线距离
     */
    private String disRide(LatLng latLng1, LatLng latLng2) {
        distance =  BaiDuMap.getRelDistance(latLng1.longitude, latLng1.latitude, latLng2.longitude, latLng2.latitude);
        Log.d("distance", dis + "");
//        distance = new DecimalFormat("###,###,###.#").format((dis) / 1000);
        Log.d("distance", dis + "");
        Log.d("distance",distance);
        return distance;
    }


    public void refush() {
        if (AppCons.locationListBean != null && AppCons.locationListBean.getId()+"" != null){
            EditLocation bean = new EditLocation(AppCons.locationListBean.getId());
            Gson gson = new Gson();
            String obj = gson.toJson(bean);
            NewHttpUtils.querrLoocation(obj,getContext(),locationCallback);
        }

    }

    private ResponseCallback locationCallback = new ResponseCallback() {
        @Override
        public void TaskCallBack(Object object) {
            if (object != null){
                List<LocationListBean> list = (List<LocationListBean>) object;
                locationListBean = list.get(0);
                if (!isShow && isResume){
                    addInfo(locationListBean);
                }

            }
        }

        @Override
        public void FailCallBack(Object object) {

        }
    };

    private class MyTask extends TimerTask {
        public void run() {
            refush();
            if (rh) {
                Log.e("timer", "create");
            } else {
                Log.e("timer", "cancel");
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isShow = hidden;
        if (hidden){
            Log.e("hiddle",hidden + "");
            rh = false;
            if (timer != null){
                timer.cancel();
                timer = null;
            }

        }else {
            Log.e("show",hidden + "");
            if (isResume){   //只有当前页面
                rh = true;
                if (timer == null){
                    timer = new Timer();
                    timer.schedule(new MyTask(),5000,10000);
                }
            }else {
                if (timer != null){
                    timer.cancel();
                }
            }

        }
    }
    private void initIconType(int state,int types){
        if (state == 1){
            switch (types){
                case 1:
                    mMaker = BitmapDescriptorFactory.fromResource(R.drawable.online_car_1);
                    break;
                case 2:
                    mMaker = BitmapDescriptorFactory.fromResource(R.drawable.online_car_2);
                    break;
                case 3:
                    mMaker = BitmapDescriptorFactory.fromResource(R.drawable.online_car_3);
                    break;
                case 4:
                    mMaker = BitmapDescriptorFactory.fromResource(R.drawable.online_car_4);
                    break;
                case 5:
                    mMaker = BitmapDescriptorFactory.fromResource(R.drawable.online_car_5);
                    break;
                case 6:
                    mMaker = BitmapDescriptorFactory.fromResource(R.drawable.online_car_6);
                    break;
                case 7:
                    mMaker = BitmapDescriptorFactory.fromResource(R.drawable.online_people);
                    break;
                case 8:
                    mMaker = BitmapDescriptorFactory.fromResource(R.drawable.online_dog);
                    break;
            }
        }else {
            switch (types){
                case 1:
                    mMaker = BitmapDescriptorFactory.fromResource(R.drawable.offline_car_1);
                    break;
                case 2:
                    mMaker = BitmapDescriptorFactory.fromResource(R.drawable.offline_car_2);
                    break;
                case 3:
                    mMaker = BitmapDescriptorFactory.fromResource(R.drawable.offline_car_3);
                    break;
                case 4:
                    mMaker = BitmapDescriptorFactory.fromResource(R.drawable.offline_car_4);
                    break;
                case 5:
                    mMaker = BitmapDescriptorFactory.fromResource(R.drawable.offline_car_5);
                    break;
                case 6:
                    mMaker = BitmapDescriptorFactory.fromResource(R.drawable.offline_car_6);
                    break;
                case 7:
                    mMaker = BitmapDescriptorFactory.fromResource(R.drawable.offline_people);
                    break;
                case 8:
                    mMaker = BitmapDescriptorFactory.fromResource(R.drawable.offline_dog);
                    break;
            }
        }
    }

    private void showNavigationDialg(){
        if (navigationDialog == null){
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.navigation_layout, null);
            TextView textView_baidu = (TextView) view.findViewById(R.id.textView_baidu);
            TextView textView_gaode = (TextView) view.findViewById(R.id.textView_gaode);
            navigationDialog = new AlertDialog.Builder(getActivity()).create();
            textView_gaode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isInstallByread("com.baidu.BaiduMap")){
                        Log.e("sssss","安装百度");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = null;
                                try {
                                    intent = Intent.getIntent("intent://map/direction?destination=latlng:"+ locationListBean.getLocation().getBdLat() + "," + locationListBean.getLocation().getBdLon() + "|name:&origin=" + "我的位置" + "&mode=driving&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                                    startActivity(intent);
                                } catch (URISyntaxException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                        navigationDialog.dismiss();
                    }else {
                        Toast.makeText(getContext(),"未安装百度地图",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            textView_baidu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isInstallByread("com.autonavi.minimap")){
                        Log.e("sssss","安装高德");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Gps gpsend = PositionUtil.bd09_To_Gcj02(locationListBean.getLocation().getBdLat(),locationListBean.getLocation().getBdLon());
                                try {
                                    Intent intent = Intent.getIntent("androidamap://route?sourceApplication=softname&sname=我的位置&dlat="+gpsend.getWgLat()+"&dlon="+gpsend.getWgLon()+"&dname="+endAddress+"&dev=0&m=0&t=1");
                                    startActivity(intent);
                                } catch (URISyntaxException e) {
                                    e.printStackTrace();
                                }

                            }
                        }).start();
                        navigationDialog.dismiss();
                    }else {
                        Toast.makeText(getContext(),"未安装高德地图",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            navigationDialog.setView(view);
            navigationDialog.show();
        }else {
            navigationDialog.show();
        }
    }

}
