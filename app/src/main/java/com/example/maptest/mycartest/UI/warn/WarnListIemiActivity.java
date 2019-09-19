package com.example.maptest.mycartest.UI.warn;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.maptest.mycartest.Adapter.IemilListAdapter;
import com.example.maptest.mycartest.New.LocationListBean;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.SwipRecycleView.ListViewDecoration;
import com.example.maptest.mycartest.UI.EquipUi.SetWarnActivity;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.BaseActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by ${Author} on 2017/9/13.
 * Use to
 */

public class WarnListIemiActivity extends BaseActivity implements View.OnClickListener{

    private ImageView imageView_quit,imageView_set;
    private RecyclerView recyclerView;
    private IemilListAdapter adapter;
    private SearchView searchView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iemlist);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        initView();
        adapters();
        search();
    }

    private void initView() {
        imageView_quit = (ImageView) findViewById(R.id.iemi_image_quit);
        imageView_set = (ImageView) findViewById(R.id.iemi_image_set);
        imageView_set.setOnClickListener(this);
        imageView_quit.setOnClickListener(this);
        searchView = (SearchView) findViewById(R.id.iemi_searchView);
        recyclerView = (RecyclerView) findViewById(R.id.iemi_recycleview);
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) searchView.findViewById(id);
        textView.setTextSize(18);
        if (searchView != null){        //去掉下划线
            Class<?> argClass = searchView.getClass();
            //--指定某个私有属性,mSearchPlate是搜索框父布局的名字
            Field ownField = null;
            try {
                ownField = argClass.getDeclaredField("mSearchPlate");
                //--暴力反射,只有暴力反射才能拿到私有属性
                ownField.setAccessible(true);
                View mView = (View) ownField.get(searchView);
                //--设置背景
                mView.setBackgroundColor(Color.TRANSPARENT);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    /**
     * 初始化适配器级适配器点击
     */
    private void adapters() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new IemilListAdapter(AppCons.locationList,WarnListIemiActivity.this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new ListViewDecoration()); //添加分割线
        adapter.setOnItemClickListener(new IemilListAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemclick(View view, LocationListBean bean) {
                Bundle bundle = new Bundle();
                bundle.putString("TimID",bean.getTerminalID());
                bundle.putString("AgtID","-1");
                startActivity(new Intent(WarnListIemiActivity.this,WarnMationActivity.class).putExtras(bundle));
            }
        });
    }

    /**
     * 搜索事件
     */
    private void search() {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<LocationListBean> lists = filter(AppCons.locationList,newText);
                adapter.setFilter(lists);
                adapter.animateTo(lists);
                return false;
            }
        });
    }
    private List<LocationListBean> filter(List<LocationListBean> peoples, String query) {
        query = query.toLowerCase();

        final List<LocationListBean> filteredModelList = new ArrayList<>();
        for (LocationListBean people : peoples) {
            if (people.getCarNumber().equals("")){
                final String nameEn = people.getNickname().toLowerCase();
                final String desEn = people.getTerminalID().toLowerCase();
                final String name = people.getNickname();
                final String des = people.getTerminalID();
                if (name.contains(query) || des.contains(query) || nameEn.contains(query) || desEn.contains(query)) {
                    filteredModelList.add(people);
                }
//                final String desEn = people.getTerminalID().toLowerCase();
//                final String des = people.getTerminalID();
//                if (des.contains(query) || desEn.contains(query)) {
//                    filteredModelList.add(people);
//                }
            }else {
                final String nameEn = people.getCarNumber().toLowerCase();
                final String desEn = people.getTerminalID().toLowerCase();
                final String name = people.getCarNumber();
                final String des = people.getTerminalID();
                if (name.contains(query) || des.contains(query) || nameEn.contains(query) || desEn.contains(query)) {
                    filteredModelList.add(people);
                }
            }

        }
        return filteredModelList;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iemi_image_quit:
                quit();
                break;
            case R.id.iemi_image_set:
                Bundle bundle = new Bundle();
                bundle.putString("temid",AppCons.loginDataBean.getData().getTerminalID());
                bundle.putString("agtid",AppCons.loginDataBean.getData().getUser_id()+"");
                startActivity(new Intent(WarnListIemiActivity.this, SetWarnActivity.class).putExtras(bundle));
                break;
        }
    }

    private void quit() {
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
       if (KeyEvent.KEYCODE_BACK == keyCode){
           quit();
       }
        return super.onKeyDown(keyCode, event);
    }
}
