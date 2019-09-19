package com.example.maptest.mycartest.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

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
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapViewLayoutParams;
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
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.maptest.mycartest.Bean.Addrbean;
import com.example.maptest.mycartest.BuildConfig;
import com.example.maptest.mycartest.Entity.EditLocation;
import com.example.maptest.mycartest.New.LocationListBean;
import com.example.maptest.mycartest.New.NewHttpUtils;
import com.example.maptest.mycartest.New.WeiboDialogUtils;
import com.example.maptest.mycartest.Permission.PermissionFail;
import com.example.maptest.mycartest.Permission.PermissionGen;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.UI.InformationActivity;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.BaiDuMap;
import com.example.maptest.mycartest.Utils.DateChangeUtil;
import com.example.maptest.mycartest.Utils.EventIntentUtil;
import com.example.maptest.mycartest.Utils.Eventbean;
import com.example.maptest.mycartest.Utils.OnclickListeners;
import com.example.maptest.mycartest.Utils.UtcDateChang;
import com.example.maptest.mycartest.Utils.http.ResponseCallback;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.example.maptest.mycartest.Utils.AppCons.TEST_USE;

/**
 * Created by ${Author} on 2017/3/2.
 * Use to 查车信息的fragment
 */

public class MapFragment extends Fragment implements View.OnClickListener {
    private TextureMapView mapView;
    private BaiduMap baiduMap;
    private ImageView imageView_refush, imageView_location, imageView_type, imageView_traffic;
    private ImageView imageView_large, imageView_small, imageView_up, imageView_down;

    private LocationClient locationClient;
    private MyLocationListener myLocationListener;
    private double mLatitude;
    private double mLongtitude;
    private boolean isFirstIn = true, traffic = true, type = true,firstClick =true;
    private BitmapDescriptor mIconLocation;
    //    private MyOrientationListener orientationListener;  //方向传感器
//    private float mCurrentX;
    private PopupWindow popupWindow;
    private View view;
    private List<LocationListBean> list = new CopyOnWriteArrayList<>();
    private List<LocationListBean> indexList = new CopyOnWriteArrayList<>();
    private int moves = 0,index = 0;
    private Dialog dialog;
    //弹出框

    private View baidumap_infowindow;
    private View view_layout;
    InfoWindow infoWindow;
    private MarkerOnInfoWindowClickListener markerListener;
    private BitmapDescriptor mMaker;
    private MyLocationConfiguration configuration;
    private Addrbean addrbean;
    private String addrss;
    private InfoWindowHolder holder = null;
    private int position;
    private boolean firstRequest = true;

    private String distance;
    private Timer timer;
    private boolean isFirstRush = true,isFirstEnter = true,showMap;
    private OkHttpUtils okHttpUtils = OkHttpUtils.getInstance();
    private OnMarkerClickListeners onMarkerClickListener;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            this.obtainMessage();
            switch (msg.what) {
                case 0:
                    if (dialog != null ){
                        WeiboDialogUtils.closeDialog(dialog);
                    }
                    mapView.removeView(baidumap_infowindow);
                    break;
                case 1:
                    if (moves < indexList.size() - 1) {
                        moves++;
                        if (indexList != null && indexList.size() !=0){
                            getAdd(moves, indexList.get(moves));
                        }
                    } else {
                        moves = 0;
                        if (indexList != null && indexList.size() !=0){
                            getAdd(moves, indexList.get(moves));
                        }

                    }
                    break;
                case 2:
                    if (moves == 0) {
                        moves = (indexList.size() - 1);
                        if (indexList != null && indexList.size() !=0){
                            getAdd(moves, indexList.get(moves));
                        }else {
                            moves = 0;
                        }

                    } else {
                        moves--;
                        if (indexList != null && indexList.size() !=0 && moves <indexList.size()){
                            getAdd(moves, indexList.get(moves));
                        }

                    }
                    break;
                case 9:
                    imageView_up.setClickable(true);
                    imageView_down.setClickable(true);
                    break;
            }
        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * @param activity 获取宿主activity里面的方法和数据
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        SDKInitializer.initialize(getActivity().getApplicationContext());
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        view = inflater.inflate(R.layout.fragment_maps, container, false);
        PermissionGen.with(MapFragment.this)
                .addRequestCode(100)
                .permissions(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                .request();


        initView();
        refush();
        initClick();
        initLocation();
//        clickMarker();
        disPlay();

        return view;
    }

    private void initView() {
//        view_layout =  LayoutInflater.from(getActivity()).inflate(R.layout.item_infowindow, null);

        markerListener = new MarkerOnInfoWindowClickListener();
        onMarkerClickListener = new OnMarkerClickListeners();
        mapView = (TextureMapView) view.findViewById(R.id.bmapView);
        mapView.showZoomControls(false);        //隐藏地图自带的控件
        mapView.showScaleControl(false);        //隐藏比例尺
        baiduMap = mapView.getMap();
        imageView_refush = (ImageView) view.findViewById(R.id.image_refush);
        imageView_location = (ImageView) view.findViewById(R.id.image_location);
        imageView_type = (ImageView) view.findViewById(R.id.image_type);
        imageView_traffic = (ImageView) view.findViewById(R.id.image_traffic);
        imageView_large = (ImageView) view.findViewById(R.id.image_large);
        imageView_small = (ImageView) view.findViewById(R.id.image_small);
        imageView_up = (ImageView) view.findViewById(R.id.image_up);
        imageView_down = (ImageView) view.findViewById(R.id.image_down);
        MapStatusUpdate Msu = MapStatusUpdateFactory.zoomTo(15.0f);         //地图比例
        baiduMap.setMapStatus(Msu);
        baidumap_infowindow = (View) LayoutInflater.from(getActivity()).inflate(R.layout.item_infowindow, null); //实例化infowindow

    }

    private void initClick() {
        imageView_refush.setOnClickListener(this);
        imageView_location.setOnClickListener(this);
        imageView_type.setOnClickListener(this);
        imageView_traffic.setOnClickListener(this);
        imageView_large.setOnClickListener(this);
        imageView_small.setOnClickListener(this);
//        imageView_up.setOnClickListener(this);
//        imageView_down.setOnClickListener(this);
        baidumap_infowindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    index = 0;
                intentToMsg();
            }
        });
        imageView_down.setOnClickListener(new OnclickListeners() {
            @Override
            protected void onNoDoubleClick(View v) {
                Message message2 = new Message();
                message2.what = 2;
                handler.sendMessage(message2);

            }
        });

        imageView_up.setOnClickListener(new OnclickListeners() {
            @Override
            protected void onNoDoubleClick(View v) {
                Message message1 = new Message();
                message1.what = 1;
                handler.sendMessage(message1);
            }
        });
    }

    /**
     * 初始化地图相关
     *
     */
    private void initLocation() {
        locationClient = new LocationClient(getActivity().getApplicationContext());
        myLocationListener = new MyLocationListener();
        locationClient.registerLocationListener(myLocationListener);
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setOpenGps(true);    //开启GPS
        option.setScanSpan(1000);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        locationClient.setLocOption(option);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.image_refush:
                dialog = WeiboDialogUtils.createLoadingDialog(getActivity(),"正在刷新");
                NewHttpUtils.cancelCall();
                refush();
                break;
            case R.id.image_location:
                centerToMyLocation();
                break;
            case R.id.image_type:
                type();
                break;
            case R.id.image_traffic:
                traffic();
                break;
            case R.id.image_large:
                largeMaps();
                break;
            case R.id.image_small:
                smallMaps();
                break;
//            case R.id.image_up:
//
//                break;
//            case R.id.image_down:
//
//                break;
        }
    }


    /**
     * 地图交通热图
     *
     */
    private void traffic() {
        if (traffic) {
            baiduMap.setTrafficEnabled(true);
            imageView_traffic.setImageResource(R.drawable.icon_index_guanbishishilukuang);
            traffic = false;
        } else {
            baiduMap.setTrafficEnabled(false);
            imageView_traffic.setImageResource(R.drawable.icon_index_kaiqishishilukuang);
            traffic = true;
        }

    }

    /**
     * 地图显示类型
     *
     */
    private void type() {
        if (type) {
            baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
            imageView_type.setImageResource(R.drawable.icon_index_putongshitu_selected);
            type = false;
        } else {
            type = true;
            baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
            imageView_type.setImageResource(R.drawable.icon_index_putongshitu);
        }
    }

    /**
     * 地图中心位置设置为用户当前位置
     *
     */
    private void centerToMyLocation() {         //回到我的位置
        LatLng latLng = new LatLng(mLatitude, mLongtitude);
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        baiduMap.animateMapStatus(msu);
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.e("mapfragment","onstart");
        baiduMap.setMyLocationEnabled(true);   //开启定位
        if (!locationClient.isStarted()) {
            locationClient.start();
        }
        //开启方向传感器
//        orientationListener.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("mapfragment","onstop");
        baiduMap.setMyLocationEnabled(false);   //开启定位
        locationClient.stop();              //停止定位
        //停止方向传感器
//        orientationListener.stop();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("mapfragment","onResume");
        mapView.onResume();
//        showMap = false;
        if (firstRequest){   //如果第一次进来
            firstRequest = false;
            dialog = WeiboDialogUtils.createLoadingDialog(getActivity(),"正在刷新");
            refush();
        }
        Log.e("showMap",showMap +"");
        if (!showMap && timer == null){
            timer = new Timer();
            timer.schedule(new MyTask(), 10000, 10000);

        }

    }

    @Override
    public void onPause() {
        super.onPause();
        NewHttpUtils.cancelCall();
        mapView.onPause();
//        showMap = true;
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("mapfragment","onDestory");
        firstRequest = true;
        mapView.onDestroy();
        isFirstIn = true;
        isFirstRush = true;
        view = null;
        okHttpUtils.cancelTag("map");
        NewHttpUtils.cancelCall();
        baidumap_infowindow = null;
        if (mMaker != null)
            mMaker.recycle();
        mMaker = null;
        addrbean = null;
        addrss = null;
        holder = null;
        if (mIconLocation != null)
            mIconLocation.recycle();
        mIconLocation = null;
        locationClient = null;
        myLocationListener = null;
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (onMarkerClickListener != null) {
            onMarkerClickListener = null;
        }
        okHttpUtils = null;
        list.clear();
        list = null;
    }


    /**
     * 手机当前位置监听
     *
     */
    private class MyLocationListener implements BDLocationListener {         //我的位置监听

        /**
         * @param bdLocation 用户位置监听
         */
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            MyLocationData data = new MyLocationData.Builder()
                    .accuracy(bdLocation.getRadius())
                    .latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude())
                    .build();
            baiduMap.setMyLocationData(data);
            configuration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, mIconLocation);
            baiduMap.setMyLocationConfigeration(configuration);
            mLatitude = bdLocation.getLatitude();       //更新经纬度
            mLongtitude = bdLocation.getLongitude();
            if (isFirstIn) {             //第一次定位
                LatLng latLng = null;
                if (list.size() != 0 && list != null && list.get(0).getLocation() != null) {            /*数组有数据，就以数组第一个为中心*/
                    latLng = new LatLng(list.get(0).getLocation().getBdLat(), list.get(0).getLocation().getBdLon());
                } else {
                    latLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());   /*集合没有数据，就以用户位置为中心*/
                }
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
                baiduMap.animateMapStatus(msu);
                isFirstIn = false;
            }
        }

    }
    /**
     * 地图缩放
     */
    private void largeMaps() {
        float zoomLevel = baiduMap.getMapStatus().zoom;

        if (zoomLevel <= 24) {
//					MapStatusUpdateFactory.zoomIn();
            baiduMap.setMapStatus(MapStatusUpdateFactory.zoomIn());
            imageView_up.setEnabled(true);
        } else {
            Toast.makeText(getActivity(), "已经放至最大！", Toast.LENGTH_SHORT).show();
            imageView_up.setEnabled(false);
        }
    }
    /**
     * 地图缩放
     */
    private void smallMaps() {
        float zoomLevel = baiduMap.getMapStatus().zoom;

        if (zoomLevel > 1) {
            baiduMap.setMapStatus(MapStatusUpdateFactory.zoomOut());
            imageView_down.setEnabled(true);
        } else {
            imageView_down.setEnabled(false);
            Toast.makeText(getActivity(), "已经缩至最小！", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 添加大量marker
     *
     */
    private void addInfo() {
        indexList.clear();
        baiduMap.clear();
        baiduMap.removeMarkerClickListener(onMarkerClickListener);  //每次刷新的时候移除对marker的监听
        try{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    baiduMap.clear();
                    LatLng latLng = null;
                    InfoWindow infoWindow = null;
                    Marker marker = null;
                    OverlayOptions options;
                    indexList.clear();
                    if (list.size() < 500 ){
                        AppCons.addType = "all";
                    }else {
                        if (isFirstEnter){
                            isFirstEnter = false;
                            AppCons.addType = "online";
                        }

                    }
                    switch (AppCons.addType){
                        case "all":
                            if (list.size() > 0 && list != null){
                                for (int i = 0; i < list.size(); i++) {
                                    if (list.get(i).getState() == 3 || list.get(i).getState() == 4 || list.get(i).getLocation() == null  || list.get(i).getLocation().getBdLon() == 0 && list.get(i).getLocation().getBdLat() == 0|| list.get(i).isExpire()){
                                    }else{
                                        if (list.get(i).getState() == 1) {
                                            indexList.add(0,list.get(i));
                                        } else if (list.get(i).getState() == 2){
                                            indexList.add(list.get(i));
                                        }
                                        if (BuildConfig.app_tag.equals("lcaf")){
                                            initIconType(list.get(i).getState(),2);
                                        }else {
                                            initIconType(list.get(i).getState(),list.get(i).getDevice().getIconType());
                                        }

                                        latLng = new LatLng(list.get(i).getLocation().getBdLat(), list.get(i).getLocation().getBdLon());
                                        options = new MarkerOptions().position(latLng).icon(mMaker).zIndex(5);
                                        marker = (Marker) baiduMap.addOverlay(options);
                                        if (list.get(i).getLocation().getCourse() == 0) {
                                            marker.setRotate(0);
                                        } else {
                                            marker.setRotate(-(list.get(i).getLocation().getCourse()));
                                        }
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable(TEST_USE, list.get(i));         //把当前位置的bean对象存到bundle里面
                                        bundle.putInt("position", i);
                                        marker.setExtraInfo(bundle);
                                    }
                                }
                            }
                            break;
                        case "online":
                            if (list.size() > 0 && list != null){
                                for (int i = 0; i < list.size(); i++) {
                                    if (list.get(i).getState() == 3 || list.get(i).getState() == 4 || list.get(i).getLocation() == null  || list.get(i).getLocation().getBdLon() == 0 && list.get(i).getLocation().getBdLat() == 0|| list.get(i).isExpire()){
                                    }else{
                                        if (list.get(i).getState() == 1) {
                                            indexList.add(0,list.get(i));

                                            if (BuildConfig.app_tag.equals("lcaf")){
                                                initIconType(1,2);
                                            }else {
                                                initIconType(1,list.get(i).getDevice().getIconType());
                                            }

                                            latLng = new LatLng(list.get(i).getLocation().getBdLat(), list.get(i).getLocation().getBdLon());
                                            options = new MarkerOptions().position(latLng).icon(mMaker).zIndex(5);
                                            marker = (Marker) baiduMap.addOverlay(options);
                                            if (list.get(i).getLocation().getCourse() == 0) {
                                                marker.setRotate(0);
                                            } else {
                                                marker.setRotate(-(list.get(i).getLocation().getCourse()));
                                            }
                                            Bundle bundle = new Bundle();
                                            bundle.putSerializable(TEST_USE, list.get(i));         //把当前位置的bean对象存到bundle里面
                                            bundle.putInt("position", i);
                                            marker.setExtraInfo(bundle);
                                        }

                                    }
                                }
                            }
                            break;
                        case "off":
                            if (list.size() > 0 && list != null){
                                for (int i = 0; i < list.size(); i++) {
                                    if (list.get(i).getState() == 3 || list.get(i).getState() == 4 || list.get(i).getLocation() == null  || list.get(i).getLocation().getBdLon() == 0 && list.get(i).getLocation().getBdLat() == 0|| list.get(i).isExpire()){
                                    }else{
                                        if (list.get(i).getState() == 2){
                                            indexList.add(list.get(i));
                                            if (BuildConfig.app_tag.equals("lcaf")){
                                                initIconType(2,2);
                                            }else {
                                                initIconType(2,list.get(i).getDevice().getIconType());
                                            }

//                                            mMaker = BitmapDescriptorFactory.fromResource(R.drawable.chache_lixianxiancheliang);
                                            latLng = new LatLng(list.get(i).getLocation().getBdLat(), list.get(i).getLocation().getBdLon());
                                            options = new MarkerOptions().position(latLng).icon(mMaker).zIndex(5);
                                            marker = (Marker) baiduMap.addOverlay(options);
                                            if (list.get(i).getLocation().getCourse() == 0) {
                                                marker.setRotate(0);
                                            } else {
                                                marker.setRotate(-(list.get(i).getLocation().getCourse()));

                                            }
                                            Bundle bundle = new Bundle();
                                            bundle.putSerializable(TEST_USE, list.get(i));         //把当前位置的bean对象存到bundle里面
                                            bundle.putInt("position", i);
                                            marker.setExtraInfo(bundle);
                                        }

                                    }
                                }
                            }
                            break;
                    }


                }


            }).start();
        }catch (Exception e){
            e.printStackTrace();
        }
        Message message = new Message();
        message.what = 0;
        handler.sendMessage(message);
        clickMarker();
    }
    /**
     * marker的点击事件，读去当前点击的对象里面的数据，反地理编码获得地址信息
     */
    private void clickMarker() {
        baiduMap.setOnMarkerClickListener(onMarkerClickListener);
    }
    /**
     * 创建弹出框，和弹出窗显示的数据
     *
     * @param bean
     */
    private void createInfoWindow(LocationListBean bean) {     //创建弹出框，参数弹出框，当前点击的marker
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        Date aDate = DateChangeUtil.StrToDate(DateChangeUtil.timedateTotim(bean.getLocation().getUtcTime()));    /*定位时间    */
        Date bDate = DateChangeUtil.StrToDate(DateChangeUtil.timedateTotim(bean.getLocation().getStateTime()));     /*通讯时间    */
        if (baidumap_infowindow.getTag() == null) {
            holder = new InfoWindowHolder();
            holder.textView_carno = (TextView) baidumap_infowindow.findViewById(R.id.text_carunmb);
            holder.textView_imei = (TextView) baidumap_infowindow.findViewById(R.id.text_carimei);
            holder.textView_statues = (TextView) baidumap_infowindow.findViewById(R.id.text_ltime);
            holder.textView_nowtime = (TextView) baidumap_infowindow.findViewById(R.id.text_ntimes);
            holder.textView_location = (TextView) baidumap_infowindow.findViewById(R.id.text_nlocas);
            holder.textView_mapfar = (TextView) baidumap_infowindow.findViewById(R.id.text_mapfar);
            holder.imageView_elec = (TextView) baidumap_infowindow.findViewById(R.id.image_elec);
            holder.imageView_gsm = (ImageView) baidumap_infowindow.findViewById(R.id.image_gsm);
            holder.textView_acc = (TextView) baidumap_infowindow.findViewById(R.id.text_mapstate);
            holder.textView_gps = (TextView) baidumap_infowindow.findViewById(R.id.text_mapgps);
            holder.textView_sp = (TextView) baidumap_infowindow.findViewById(R.id.text_sp);
            holder.button_msg = (Button) baidumap_infowindow.findViewById(R.id.win_btn_msg);
            holder.button_trace = (Button) baidumap_infowindow.findViewById(R.id.win_btn_trace);
            holder.button_back = (Button) baidumap_infowindow.findViewById(R.id.win_btn_back);
            holder.button_set = (Button) baidumap_infowindow.findViewById(R.id.win_btn_set);
            holder.text_v = baidumap_infowindow.findViewById(R.id.text_v);
            baidumap_infowindow.setTag(holder);
        }
        holder = (InfoWindowHolder) baidumap_infowindow.getTag();
        //点击事件
        holder.button_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = 0;
                intentToMsg();

            }
        });
        holder.button_trace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = 1;
                intentToMsg();

            }
        });
        holder.button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = 2;
                intentToMsg();

            }
        });
        holder.button_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = 3;
                intentToMsg();

            }
        });
        //显示事件
        if (bean.getCarNumber() == null || bean.getCarNumber().equals("")) {
            holder.textView_carno.setText("IMEI:" + bean.getTerminalID());
        } else {
            holder.textView_carno.setText("车牌号:" + bean.getCarNumber());
        }
        holder.textView_imei.setText("设备名:" + bean.getNickname());

        if (bean.getState() == 1) {
            holder.button_set.setVisibility(View.VISIBLE);
            long dates = (bDate.getTime() - aDate.getTime());
            String mydatas = DateChangeUtil.getdata(dates);
            Log.e("相减的时间",dates +" 转换后的时间 " + mydatas + "miaoshu :" + DateChangeUtil.getSec(dates));
            if (DateChangeUtil.getSec(dates) < 60){
                if (bean.getLocation().getSpeed() <= 10){
                    holder.textView_sp.setText("0km/h");
                    if (dates <= 100000){
                        holder.textView_statues.setText("静止");       //通讯时间
                    }else {
                        holder.textView_statues.setText("静止:" + mydatas);       //通讯时间
                    }
                }else {
                    holder.textView_statues.setText("行驶");       //通讯时间
                    holder.textView_sp.setText(bean.getLocation().getSpeed() + "km/h");
                }
            }else {       //补传
                    holder.textView_sp.setText("0km/h");
                    holder.textView_statues.setText("静止:" + mydatas);       //通讯时间
            }
        } else {                //如果是离线
            long dateunline = (UtcDateChang.Local2UTC(curDate) - bean.getLocation().getStateTime());
            String mydatanuline = DateChangeUtil.getdata(dateunline);
            holder.textView_sp.setText(bean.getLocation().getSpeed() + "km/h");
            holder.textView_statues.setText("离线" + mydatanuline);       //通讯时间
            switch (bean.getLocation().getDeviceProtocol()){
                case 2:  //r001
                case 3:  //r002
                case 6:  //bsj
                case 7:  //sagex
                case 8:
                    holder.button_set.setVisibility(View.VISIBLE);   //拥有离线指令
                    break;
                case 0:  //k100
                case 1:  //k100b
                    if (bean.getDevice().getDeviceType().equals("GW005A") || bean.getDevice().getDeviceType().equals("GT750A")
                            || bean.getDevice().getDeviceType().equals("V703D")){   //V703D没有设防撤防
                        holder.button_set.setVisibility(View.VISIBLE);   //拥有离线指令
                    }else{
                        holder.button_set.setVisibility(View.GONE);
                    }
                    break;
                case 4:  //tire
                case 5:  //0bd
                    holder.button_set.setVisibility(View.GONE);
                    break;
                default:
                    holder.button_set.setVisibility(View.GONE);
                    break;
            }
        }
        holder.textView_nowtime.setText(UtcDateChang.UtcDatetoLocaTime(bean.getLocation().getStateTime()));
        if (DistanceUtil.getDistance(new LatLng(bean.getLocation().getBdLat(),bean.getLocation().getBdLon()),new LatLng(30.235319,104.052502)) < 500){
            holder.textView_location.setText("四川省眉山市仁寿县/四川玉骑铃科技有限公司附近");            //地理位置显示
        }else {
            holder.textView_location.setText(bean.getAddress());            //地理位置显示
        }
        if (bean.getLocation().getVoltage() != -1){
            holder.text_v.setText("车电压: " + bean.getLocation().getVoltage() +"V");
        }

        if (bean.getLocation().getLocationType().contains("Gps")){
            holder.textView_gps.setText("定位: 卫星定位");  //:bean.getLocation().getLocationType())
        }else if (bean.getLocation().getLocationType().contains("Bds")){
            holder.textView_gps.setText("定位: 北斗");  //:bean.getLocation().getLocationType())
        }else {
            holder.textView_gps.setText("定位: " + bean.getLocation().getLocationType());  //:bean.getLocation().getLocationType())
        }

        holder.textView_mapfar.setText("距离:" + distance + "km");
        switch (bean.getDevice().getDeviceType()){
            case "G17":
            case "GT06":
            case "G16":
                holder.textView_acc.setText("ACC : 无");
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
                    holder.textView_acc.setText("ACC : 开");
                } else {
                    holder.textView_acc.setText("ACC : 关");
                }
                holder.imageView_elec.setVisibility(View.GONE);
                break;
            default:
                if (bean.getLocation().isAcc()) {
                    holder.textView_acc.setText("ACC : 开");
                } else {
                    holder.textView_acc.setText("ACC : 关");
                }
                holder.imageView_elec.setVisibility(View.VISIBLE);
                holder.imageView_elec.setText(BaiDuMap.getElect(bean.getLocation().getElectric() * 100) + "%");
                break;
        }

/*
        if (bean.getDevice().getDeviceType().equals("G17")){
            holder.textView_acc.setText("ACC : 无");
            holder.imageView_elec.setVisibility(View.GONE);
        }else {
            if (bean.getLocation().isAcc()) {
                holder.textView_acc.setText("ACC : 开");
            } else {
                holder.textView_acc.setText("ACC : 关");
            }
            holder.imageView_elec.setVisibility(View.VISIBLE);
            holder.imageView_elec.setText(BaiDuMap.getElect(bean.getLocation().getElectric() * 100) + "%");
        }*/
        switch (bean.getLocation().getGsm()) {
            case 0:
                holder.imageView_gsm.setImageResource(R.drawable.icon_chache_wxh);
                break;
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
            default:
                holder.imageView_gsm.setImageResource(R.drawable.icon_chache_wxh);
                break;
        }

    }
    /**
     * 气泡框的点击事件
     *
     */
    private final class MarkerOnInfoWindowClickListener implements InfoWindow.OnInfoWindowClickListener {

        @Override
        public void onInfoWindowClick() {



//
//            Intent intent = new Intent(getActivity(), InformationActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putInt("position",index);
//            bundle.putSerializable(AppCons.TEST_FRG, list.get(position));
//            intent.putExtras(bundle);
//            startActivity(intent);
//            intentToMsg();
        }

    }
    /**
     * 百度marker点击事件
     *
     */
    private final class OnMarkerClickListeners implements BaiduMap.OnMarkerClickListener {

        @Override
        public boolean onMarkerClick(Marker marker) {
            Log.e("MARKER", "点击事件开始");
            LocationListBean useBean = (LocationListBean) marker.getExtraInfo().getSerializable(AppCons.TEST_USE);   //读取存到marker里面的数据
            position = marker.getExtraInfo().getInt("position");
            getAdd(position, useBean);
            Log.e("MARKER", "点击事件结束");
            return false;
        }
    }
    /**
     * 定义气泡框显示静态类
     *
     */
    static class InfoWindowHolder {

        private TextView textView_carno;
        private TextView textView_imei;
        private TextView textView_statues;
        private TextView textView_nowtime;
        private TextView textView_location;
        private TextView textView_mapfar;
        private TextView imageView_elec;
        private ImageView imageView_gsm;
        private TextView textView_acc;
        private TextView textView_gps;
        private TextView textView_sp;
        private TextView text_v;
        private Button button_msg;
        private Button button_trace;
        private Button button_back;
        private Button button_set;
    }
    /**
     * 获取地址
     *
     * @param i
     * @param useBean
     */
    public void getAdd(final int i, final LocationListBean useBean) {       //将marker对象传入

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
                Log.e("onGetGeoCodeResult",addrs);
                addrbean = new Addrbean(addrs, useBean, i);
                EventBus.getDefault().post(new Eventbean(addrbean));

            }
        });
        geoCoder.reverseGeoCode(op);

    }
    /**
     * 接受到含有地理位置的对象，更新气泡窗上的数据
     *
     * @param eventbean
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void recaddrs(Eventbean eventbean) {

        Addrbean addrbean = eventbean.getAddrbean();
        addrss = addrbean.getAddrs();
        position = addrbean.getI();
        Log.e("执行",addrbean.toString());
        LocationListBean useBean = addrbean.getUseBean();
        LatLng latLng = new LatLng(useBean.getLocation().getBdLat(), useBean.getLocation().getBdLon());
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        baiduMap.animateMapStatus(msu);
        useBean.setAddress(addrss);
        disRide(new LatLng(mLatitude, mLongtitude), latLng);
        createInfoWindow(useBean);
        mapView.removeView(baidumap_infowindow);
        ViewGroup.LayoutParams params = new MapViewLayoutParams.Builder()
                .layoutMode(MapViewLayoutParams.ELayoutMode.mapMode)// 按照经纬度设置位置
                .position(latLng)// 不能传null
                .width(MapViewLayoutParams.WRAP_CONTENT)
                .height(MapViewLayoutParams.WRAP_CONTENT)
                .yOffset(-30)// 距离position的像素 向下是正值 向上是负值
                .build();
        mapView.addView(baidumap_infowindow, params);
        AppCons.locationListBean = useBean;
    }
    /**
     * 接受车辆列表传过来的数据对象，定位到对象的位置，显示气泡框
     *
     * @param eventIntentUtil
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void recIntent(EventIntentUtil eventIntentUtil){
        LocationListBean recCarBean = eventIntentUtil.getRecCarBean();
        String terminalID = recCarBean.getTerminalID();
        Log.e("recIntent",terminalID);
        for (int i = 0; i < indexList.size(); i++) {   //判断传过来的值是哪个
            if (indexList.get(i).getTerminalID().equals(terminalID)) {
                Log.e("indexList",indexList.get(i).getTerminalID());
                getAdd(i, indexList.get(i));
            }
        }
    }
    /**
     * 多车监控数据源的请求方法，刷新方法
     *
     */
    public void refush() {
        Log.e("mapfragment","刷新实时位置");
        if (!showMap){
            EditLocation bean = new EditLocation(AppCons.AGENTID_SELECT);
            Gson gson = new Gson();
            String obj = gson.toJson(bean);
            NewHttpUtils.querrLoocation(obj,getContext(),locationCallback);
        }
//        NewHttpUtils.querrAlarms();
    }



    private ResponseCallback locationCallback = new ResponseCallback() {
        @Override
        public void TaskCallBack(Object object) {
            if (object != null && !showMap){
                List<LocationListBean> listr = (List<LocationListBean>) object;
                list.clear();
                list.addAll(listr);
                AppCons.locationList.clear();
                AppCons.locationList.addAll(listr);
                addInfo();
            }
        }

        @Override
        public void FailCallBack(Object object) {
            Message message = new Message();
            message.what = 0;
            handler.sendMessage(message);
        }
    };

    public  void listRefush(){
        Log.e("执行刷新marker","来自车辆列表的刷新");
        synchronized (list){
            list.clear();
            list.addAll(AppCons.locationList);
        }
        synchronized (indexList){
            indexList.clear();
        }
        addInfo();

    }



    /**
     * 执行定时器的类
     *
     */
    private class MyTask extends TimerTask {
        public void run() {
            refush();

        }
    }
    /**
     * 6.0权限
     */
    @PermissionFail(requestCode = 100)
    public void failContact() {
        Toast.makeText(getActivity(), "未授予相关权限", Toast.LENGTH_SHORT).show();
    }
    /**
     * @param requestCode
     * @param permissions
     * @param grantResults 6.0权限申请结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }
    /**
     * 监听地图的状态，当地图状态改变，隐藏气泡框
     *
     */
    private void disPlay() {
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
     * 两个经纬之间的直线距离
     *
     * @param latLng1
     * @param latLng2
     * @return
     */
    private String disRide(LatLng latLng1, LatLng latLng2) {
        distance = BaiDuMap.getRelDistance(latLng1.longitude, latLng1.latitude, latLng2.longitude, latLng2.latitude);
        return distance;
    }
    @Override
    public void onHiddenChanged(boolean hidd) {
        showMap = hidd;
        if (hidd) {
            System.out.println("不可见");
            NewHttpUtils.cancelCall();

            if (timer != null) {
                timer.cancel();
                timer = null;
            }

        } else {
            System.out.println("当前可见");
            if (isFirstRush){
                isFirstRush = false;
                if (timer == null){
                    Log.e("create","timer2");
                    timer = new Timer();
                    timer.schedule(new MyTask(), 10000, 15000);

                }
            }else {
                if (timer == null){
                    Log.e("create","timer2");
                    timer = new Timer();
                    timer.schedule(new MyTask(), 10000, 15000);

                }
            }


        }
    }

    private void intentToMsg(){
        Intent intent = new Intent(getActivity(), InformationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("position",index);
        intent.putExtras(bundle);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.in_anim,R.anim.out_anim);
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
}