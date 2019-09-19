package com.example.maptest.mycartest.UI.EquipUi;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.UI.warn.WarnListBean;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.BaseActivity;

/**
 * Created by ${Author} on 2017/3/29.
 * Use to展示用户报警信息和报警位置
 */

public class ShowWarnActivity extends BaseActivity implements View.OnClickListener {
    private ImageView imageView_quit, imageView_lrage, imageView_small, imageView_type;
    private TextureMapView mapView;
    private BaiduMap baiduMap;
    private WarnListBean bean;
    private LinearLayout linearLayout_window;
    private BitmapDescriptor icon;//标注图标
    private InfoWindow infoWindow = null;
    private MarkerOnInfoWindowClickListener markerListener;
    private boolean type = true;
    private Marker marker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showwarn);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        bean = (WarnListBean) getIntent().getExtras().getSerializable(AppCons.TEST_WARN);
        initView();
        initClick();
    }

    private void initClick() {
        imageView_quit.setOnClickListener(this);
        imageView_lrage.setOnClickListener(this);
        imageView_small.setOnClickListener(this);
        imageView_type.setOnClickListener(this);
    }

    private void initView() {
        imageView_lrage = (ImageView) findViewById(R.id.image_largeshow);
        imageView_small = (ImageView) findViewById(R.id.image_smallshow);
        imageView_type = (ImageView) findViewById(R.id.image_typeshow);
        imageView_quit = (ImageView) findViewById(R.id.image_quit_show);
        mapView = (TextureMapView) findViewById(R.id.showwarn_bmapView);
        baiduMap = mapView.getMap();
        mapView.showZoomControls(false);        //隐藏地图自带的控件
        markerListener = new MarkerOnInfoWindowClickListener();
        linearLayout_window = (LinearLayout) LayoutInflater.from(ShowWarnActivity.this).inflate(R.layout.item_warnwindow, null);
        showWarn();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_largeshow:
                largemaps();
                break;
            case R.id.image_smallshow:
                smallmaps();
                break;
            case R.id.image_typeshow:
                maptype();
                break;
            case R.id.image_quit_show:
                quitTofinish();
                break;
        }
    }

    private void largemaps() {
        float zoomLevel = baiduMap.getMapStatus().zoom;

        if (zoomLevel <= 24) {
//					MapStatusUpdateFactory.zoomIn();
            baiduMap.setMapStatus(MapStatusUpdateFactory.zoomIn());
            imageView_lrage.setEnabled(true);

        } else {
            Toast.makeText(ShowWarnActivity.this, "已经放至最大！", Toast.LENGTH_SHORT).show();
            imageView_lrage.setEnabled(false);

        }
    }

    private void smallmaps() {
        float zoomLevel = baiduMap.getMapStatus().zoom;
        if (zoomLevel > 1) {
            baiduMap.setMapStatus(MapStatusUpdateFactory.zoomOut());
            imageView_small.setEnabled(true);
        } else {
            imageView_small.setEnabled(false);
            Toast.makeText(ShowWarnActivity.this, "已经缩至最小！", Toast.LENGTH_SHORT).show();
        }
    }

    private void maptype() {
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
     * 车辆报警信息的展示
     */
    private void showWarn() {
        baiduMap.clear();
        LatLng latLng = new LatLng(bean.getBdLat(), bean.getBdLon());
        MapStatusUpdate center = MapStatusUpdateFactory.newLatLng(latLng);
        baiduMap.setMapStatus(center);
        icon = BitmapDescriptorFactory.fromResource(R.drawable.chache_zaixiancheliang);
        OverlayOptions ooA = new MarkerOptions().position(latLng)
                .icon(icon).zIndex(9).draggable(true);
        marker = (Marker) (baiduMap.addOverlay(ooA));
        createWindow(linearLayout_window, bean);
        infoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(linearLayout_window), latLng, -47, markerListener);
        baiduMap.showInfoWindow(infoWindow);
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            /**
             * @param marker
             * @return
             * 点击地图上的覆盖物，弹出车辆信息
             */
            @Override
            public boolean onMarkerClick(Marker marker) {
                baiduMap.showInfoWindow(infoWindow);
                return false;
            }
        });


    }

    private final class MarkerOnInfoWindowClickListener implements InfoWindow.OnInfoWindowClickListener {

        /**
         * 地图状态改变，隐藏车辆信息
         */
        @Override
        public void onInfoWindowClick() {
            baiduMap.hideInfoWindow();
        }

    }

    /**
     * @param linearLayout_window
     * @param bean
     * 报警信息展示和当前车辆信息
     */
    private void createWindow(LinearLayout linearLayout_window, WarnListBean bean) {
        warnViewHolder holder = null;
        if (linearLayout_window.getTag() == null) {
            holder = new warnViewHolder();
            holder.textView_type = (TextView) linearLayout_window.findViewById(R.id.text_warntypes);
            holder.textView_timid = (TextView) linearLayout_window.findViewById(R.id.text_cartemid);
            holder.textView_time = (TextView) linearLayout_window.findViewById(R.id.text_warntimes);
            holder.textView_locas = (TextView) linearLayout_window.findViewById(R.id.text_warnlocas);
            holder.textView_loctype = (TextView) linearLayout_window.findViewById(R.id.text_warnloctype);
            holder.textView_sp = (TextView) linearLayout_window.findViewById(R.id.text_warnsp);
            linearLayout_window.setTag(holder);
        }
        //根据请求到的数据，显示相应类型的报警类型
        switch (bean.getAlarmState()){
            case 0:
                holder.textView_type.setText("正常报警");
                break;
            case 1:
                holder.textView_type.setText("求救报警(SOS)");
                break;
            case 2:
                holder.textView_type.setText("断电报警");
                break;
            case 3:
                holder.textView_type.setText("震动报警");
                break;
            case 4:
                holder.textView_type.setText("围栏报警(进)");
                break;
            case 5:
                holder.textView_type.setText("围栏报警(出)");
                break;
            case 6:
                holder.textView_type.setText("超速报警");
                break;
            case 7:
                holder.textView_type.setText("拆除报警");
                break;
            case 9:
                holder.textView_type.setText("位移报警");
                break;
            case 10:
                holder.textView_type.setText("盲区报警(进)");
                break;
            case 11:
                holder.textView_type.setText("盲区报警(出)");
                break;
            case 12:
                holder.textView_type.setText("开机报警");
                break;
            case 14:
                holder.textView_type.setText("低电报警(外电)");
                break;
            case 15:
                holder.textView_type.setText("低电报警(外电)");
                break;
            case 18:
                holder.textView_type.setText("低电报警(电池)");
                break;
            case 19:
                holder.textView_type.setText("拆卸报警");
                break;
            case 20:
                holder.textView_type.setText("离线报警");
                break;
            case 21:
                holder.textView_type.setText("胎压漏气");
                break;
            case 22:
                holder.textView_type.setText("轮胎异常");
                break;
            case 23:
                holder.textView_type.setText("围栏报警");
                break;
            case 40:
                holder.textView_type.setText("急减速");
                break;
            case 41:
                holder.textView_type.setText("急加速");
                break;
            case 44:
                holder.textView_type.setText("碰撞报警");
                break;
            case 45:
                holder.textView_type.setText("翻转报警");
                break;
            case 46:
                holder.textView_type.setText("急转弯");
                break;
            case 48:
                holder.textView_type.setText("GSM干扰报警");
                break;
            default:
                holder.textView_type.setText("正常报警");
                break;
        }
        holder.textView_sp.setText(bean.getSpeed() +"km/h");
        holder.textView_timid.setText("IMEI:" + bean.getTerminalID());
        holder.textView_time.setText(bean.getExpireTime());
        holder.textView_locas.setText(bean.getAlarmAddress());
        holder.textView_loctype.setText(bean.getLocationType());
    }

    static class warnViewHolder {
        private TextView textView_type;
        private TextView textView_timid;
        private TextView textView_time;
        private TextView textView_locas;
        private TextView textView_loctype;
        private TextView textView_sp;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();

    }

    /**
     * 退出事件
     */
    private void quitTofinish() {
        bean = null;
        baiduMap = null;
        if (icon != null) {
            icon.recycle();
            icon = null;
        }
        linearLayout_window = null;
        infoWindow = null;
        marker.remove();
        marker = null;
        finish();
        System.gc();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            quitTofinish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
