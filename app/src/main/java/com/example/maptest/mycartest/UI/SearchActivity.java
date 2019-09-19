package com.example.maptest.mycartest.UI;

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
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.maptest.mycartest.Adapter.TyreSearchAdapter;
import com.example.maptest.mycartest.New.LocationListBean;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.SwipRecycleView.ListViewDecoration;
import com.example.maptest.mycartest.UI.TyreUi.TyreActivity;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.BaseActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${Author} on 2017/3/6.
 * Use to 搜索页面
 */

public class SearchActivity extends BaseActivity implements View.OnClickListener{
    private SearchView searchView;
    private TextView textView_cacle;
    private RecyclerView recyclerView_search;
    private TyreSearchAdapter searchAdapter;
    private LinearLayout linearLayout_sh;
    private LinearLayout linerlayout_search_none,linerlayout_search_show;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏

        initView();
        adapters();
        search();
        initClick();
    }

    private void initClick() {
        textView_cacle.setOnClickListener(this);
    }


    private void initView() {
        linerlayout_search_none = (LinearLayout) findViewById(R.id.linerlayout_search_none);
        linerlayout_search_show = (LinearLayout) findViewById(R.id.linerlayout_search_show);
        if (AppCons.tyreList.size() == 0){
            linerlayout_search_none.setVisibility(View.VISIBLE);
            linerlayout_search_show.setVisibility(View.GONE);
        }else {
            linerlayout_search_none.setVisibility(View.GONE);
            linerlayout_search_show.setVisibility(View.VISIBLE);
        }
        textView_cacle = (TextView) findViewById(R.id.text_cancels);
        linearLayout_sh = (LinearLayout) findViewById(R.id.liiner_sh);
        recyclerView_search = (RecyclerView) findViewById(R.id.recycleview_search);

        searchView = (SearchView) findViewById(R.id.search_information);
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) searchView.findViewById(id);
        textView.setTextSize(16);
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
        recyclerView_search.setLayoutManager(new LinearLayoutManager(this));
        searchAdapter = new TyreSearchAdapter(AppCons.tyreList,SearchActivity.this);
        recyclerView_search.setItemAnimator(new DefaultItemAnimator());
        recyclerView_search.setAdapter(searchAdapter);
        recyclerView_search.setHasFixedSize(true);
        recyclerView_search.addItemDecoration(new ListViewDecoration()); //添加分割线
        searchAdapter.setOnItemClickListener(new TyreSearchAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemclick(View view, LocationListBean bean) {
                LocationListBean beans = bean;
                Bundle bundle = new Bundle();
                bundle.putSerializable(AppCons.TEST_TYR,beans);
                startActivity(new Intent(SearchActivity.this,TyreActivity.class).putExtras(bundle));

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
                final  List<LocationListBean> lists = filter(AppCons.tyreList,newText);
                searchAdapter.setFilter(lists);
                searchAdapter.animateTo(lists);
                //reset

                return true;
            }

        });


    }

    /**
     * @param peoples
     * @param query
     * @return
     * 搜索筛选字段逻辑
     */
    private List<LocationListBean> filter(List<LocationListBean> peoples, String query) {
        query = query.toLowerCase();

        final List<LocationListBean> filteredModelList = new ArrayList<>();
        for (LocationListBean people : peoples) {
            if (people.getCarNumber().equals("")){
                final String desEn = people.getTerminalID().toLowerCase();
                final String des = people.getTerminalID();
                final String nameEn = people.getNickname().toLowerCase();
                final String name = people.getNickname();

                if (name.contains(query) || des.contains(query) || nameEn.contains(query) || desEn.contains(query)) {
                    filteredModelList.add(people);
                }
            }else {
                final String desEn = people.getTerminalID().toLowerCase();
                final String des = people.getTerminalID();
                final String nameEn = people.getCarNumber().toLowerCase();
                final String name = people.getCarNumber();
                if (name.contains(query) || des.contains(query) || nameEn.contains(query) || desEn.contains(query)) {
                    filteredModelList.add(people);
                }
            }


        }
        return filteredModelList;
    }

    @Override
    protected void onResume() {
        super.onResume();
        linearLayout_sh.setFocusable(true);
        linearLayout_sh.setFocusableInTouchMode(true);
        linearLayout_sh.requestFocus();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.text_cancels:
               quitTofinish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
    private void quitTofinish(){
        recyclerView_search = null;
        searchAdapter = null;
        finish();
        System.gc();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            quitTofinish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
