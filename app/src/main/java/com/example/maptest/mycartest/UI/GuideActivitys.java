package com.example.maptest.mycartest.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.Utils.BitmapUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 引导页
 *
 * @author
 */
public class GuideActivitys extends AppCompatActivity {


    ViewPager mViewPager;
    ImageView imageView;

    private Context TAG;
    private static final String SHAREDPREFERENCES_NAME = "first_pref";
    // 图片资源
    private List<Integer> picResource = new ArrayList<Integer>();
    // ViewPager的ImageView个数
    private List<ImageView> viewPagerIvList = new ArrayList<ImageView>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉信息栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_guide);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        imageView = (ImageView) findViewById(R.id.iv_start_weibo);
        TAG = this;
        initViewPagerData();
    }

    /**
     * 初始化viewpager
     */
    private void initViewPagerData() {
        picResource.add(R.drawable.a1);
        picResource.add(R.drawable.a2);
        picResource.add(R.drawable.a3);
        picResource.add(R.drawable.a4);
        // 根据图片个数设置imageview
        ImageView imageView = null;
        for (int i = 0; i < picResource.size(); i++) {
            imageView = new ImageView(TAG);
            imageView.setImageBitmap(BitmapUtil.compressImage(BitmapUtil.readBitMap(TAG, picResource.get(i))));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewPagerIvList.add(imageView);
        }
        // 添加完毕后设置viewpager的适配器
        mViewPager.setAdapter(new mViewPagerAdapter());
        // 设置viewpager滑动监听
        mViewPager.setOnPageChangeListener(new mOnPageChangeListener());
    }

    /**
     * 跳转到首页
     */
    private void toTabActivity() {
        startActivity(new Intent(TAG, MainActivity.class));
        finish();
    }

    /**
     * 设置viewpager适配器
     *
     * @author wuxuhua
     */
    private class mViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return viewPagerIvList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView(viewPagerIvList.get(position));
        }

        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager) container).addView(viewPagerIvList.get(position));
            return viewPagerIvList.get(position);
        }

    }

    /**
     * 监听viewpager滑动
     *
     * @author wuxuhua
     */
    private class mOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int position) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int position) {
            // 判断如果是最后一张图片，就跳转到首页
            if (position == viewPagerIvList.size() - 1) {
                imageView.setVisibility(View.VISIBLE);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toTabActivity();
                        setGuided();
                    }
                });
            } else {
                imageView.setVisibility(View.GONE);
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        junActivity();
    }

    private void junActivity(){
        imageView = null;
        viewPagerIvList.clear();
        viewPagerIvList = null;
        picResource.clear();
        picResource = null;
        mViewPager = null;

    }
    /**
     *
     * method desc：设置已经引导过了，下次启动不用再次引导
     */
    private void setGuided() {
        SharedPreferences preferences = TAG.getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        // 存入数据
        editor.putBoolean("isFirstIn", false);
        // 提交修改
        editor.commit();
    }

}
