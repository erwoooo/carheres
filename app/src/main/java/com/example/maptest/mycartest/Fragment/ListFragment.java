package com.example.maptest.mycartest.Fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maptest.mycartest.Adapter.CarlistAdapter;
import com.example.maptest.mycartest.BuildConfig;
import com.example.maptest.mycartest.Entity.PostAgent;
import com.example.maptest.mycartest.New.AgentlistBean;
import com.example.maptest.mycartest.New.LocationListBean;
import com.example.maptest.mycartest.New.NewHttpUtils;
import com.example.maptest.mycartest.New.WeiboDialogUtils;
import com.example.maptest.mycartest.SwipRecycleView.ListViewDecoration;
import com.example.maptest.mycartest.UI.CarContralActivity;
import com.example.maptest.mycartest.R;

import com.example.maptest.mycartest.UI.CarPayAcitivity;
import com.example.maptest.mycartest.UI.SetUi.PayAcitivity;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.ButtonUtils;
import com.example.maptest.mycartest.Utils.EventIntentUtil;
import com.example.maptest.mycartest.Utils.SaveList;
import com.example.maptest.mycartest.Utils.agent.Nodes;
import com.example.maptest.mycartest.Utils.agent.SimpleTreeAdapter;
import com.example.maptest.mycartest.Utils.agent.TreeListViewAdapter;
import com.example.maptest.mycartest.Utils.http.QurryAgent;
import com.example.maptest.mycartest.Utils.http.ResponseCallback;
import com.example.maptest.mycartest.test.LoginDataBean;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


import static com.example.maptest.mycartest.Utils.AppCons.AGENTID_SELECT;
import static com.example.maptest.mycartest.Utils.AppCons.AGENTID_USED;

/**
 * Created by ${Author} on 2017/3/2.
 * Use to
 */

public class ListFragment extends Fragment implements View.OnClickListener {
    private View view;
    private TextView textView_all, textView_online, textView_off, textView_unlive;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private LinearLayout linearLayout_search;
    private CarlistAdapter carlistAdapter;
    private LinearLayout linerlayout_list_agent, linearLayout_list_car;
    private RelativeLayout relativeLayout_scoll;
    private TextView textView_agent;
    private ImageView imageView_scoll;
    private Dialog dialog;
    private List<LocationListBean> alllist;
    private List<LocationListBean> reclist;
    private List<LocationListBean> unlivelist;
    private List<LocationListBean> onlist;
    private List<LocationListBean> offlist;
    private PostAgent postAgent[] = new PostAgent[]{};
    private List<PostAgent>postAgentList = new ArrayList<>();
    private List<AgentlistBean> agentList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView mTree;
    private TreeListViewAdapter mAdapter;
    float width, height, viewHeight1, viewHeight2;
    ObjectAnimator animator1, animator2;
    private float downY, upY;
    private boolean isFirst = true,firstScoll = false;
    private boolean isScoll = true, isExit = false;
    private CarContralActivity activity = CarContralActivity.instance;
    private LinearLayout linerlayout_list_show, linerlayout_list_none;
    private boolean isShow;

    public ListFragment() {

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    try {
                        Log.e("AppCons.AGENTID_USED", AGENTID_USED + " AppCons.AGENTID_SELECT :" + AGENTID_SELECT);
                        if (AGENTID_USED == (AGENTID_SELECT)) {     //登录的代理商和选择的代理商相同
                            if (AppCons.locationList != null && firstScoll){
                                alllist.addAll(AppCons.locationList);
                                firstShow();   //登录的代理商第一次进入车辆列表界面，将列表刷上去，不请求数据，直接添加数据
                                firstScoll = false;
                            }
                        } else {                //上一个代理商和选择的代理商不同
                            AGENTID_USED = AGENTID_SELECT;
                            Set set = AppCons.ACCOUNT_MAP.keySet();
                            Iterator iter = set.iterator();
                            while (iter.hasNext()) {
                                int key = (int) iter.next();
                                if (AGENTID_USED == (key)) {   //如果选择的代理商在已经请求过数据的代理商集合里面
                                    Log.e("存在key",key + "");
                                    isExit = true;
                                    break;
                                } else {
                                    Log.e("不存在key", AGENTID_USED + ""); //选择的代理商没有在保存的代理商集合里面
                                }
                            }
                            initRush();
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    if (dialog != null){
                        WeiboDialogUtils.closeDialog(dialog);
                    }
                    break;
                case 1:
                    reclist.clear();
                    reclist.addAll(alllist);
                    carlistAdapter.setFilter(alllist);
                    carlistAdapter.notifyDataSetChanged();
                    AppCons.addType = "all";
//                    AppCons.locationList.clear();
//                    AppCons.locationList.addAll(alllist);
//                    activity.refush();
                    break;
                case 2:
                    reclist.clear();
                    reclist.addAll(onlist);
                    carlistAdapter.setFilter(onlist);
                    carlistAdapter.notifyDataSetChanged();
                    AppCons.addType = "online";
//                    AppCons.locationList.clear();
//                    AppCons.locationList.addAll(alllist);
//                    activity.refush();
                    break;
                case 3:
                    reclist.clear();
                    reclist.addAll(offlist);
                    carlistAdapter.setFilter(offlist);
                    carlistAdapter.notifyDataSetChanged();
                    AppCons.addType = "off";
//                    AppCons.locationList.clear();
//                    AppCons.locationList.addAll(alllist);
//                    activity.refush();
                    break;
                case 4:
                    reclist.clear();
                    reclist.addAll(unlivelist);
                    carlistAdapter.setFilter(unlivelist);
                    carlistAdapter.notifyDataSetChanged();

                    break;
                case 5:
                    List<LocationListBean> listt = filter(reclist, (String) msg.obj);
                    carlistAdapter.setFilter(listt);
                    carlistAdapter.animateTo(listt);
                    carlistAdapter.notifyDataSetChanged();
                    break;
                case 6:
                    if (AGENTID_USED == (AGENTID_SELECT)) {
                        if (AppCons.locationList != null && firstScoll){
                            alllist.addAll(AppCons.locationList);
                            firstShow();
                            firstScoll = false;
                        }
                    } else {
                        AGENTID_USED = AGENTID_SELECT;
                        Set set = AppCons.ACCOUNT_MAP.keySet();
                        Iterator iter = set.iterator();
                        while (iter.hasNext()) {
                            int key = (int) iter.next();
                            if (AGENTID_USED == (key)) {
                                isExit = true;
                                break;
                            }
                        }
                        initRush();
                    }

                    if (dialog != null){
                        WeiboDialogUtils.closeDialog(dialog);
                    }
                    break;
                case 8:  //代理商
                    if (dialog != null){
                        WeiboDialogUtils.closeDialog(dialog);}
                    if (mAdapter == null) {
                        initAgentAdapter();
                    } else {
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
                case 9:   //请求数据失败
                    if (dialog != null){
                        WeiboDialogUtils.closeDialog(dialog);
                        swipeRefreshLayout.setRefreshing(false);
                    }
//                    activity.refush();
                    initNumber();
                    AppCons.addType = "online";
                    swipeRefreshLayout.setRefreshing(false);
                    carlistAdapter.notifyDataSetChanged();
                    reclist.clear();
                    reclist.addAll(onlist);
                    carlistAdapter.setFilter(onlist);
                    online();
                    linerlayout_list_show.setVisibility(View.GONE);
                    linerlayout_list_none.setVisibility(View.VISIBLE);
                    break;
                case 11:   //数据为空
                    if (dialog != null){
                        WeiboDialogUtils.closeDialog(dialog);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    activity.refush();
                    initNumber();
                    AppCons.addType = "online";
                    swipeRefreshLayout.setRefreshing(false);
                    carlistAdapter.notifyDataSetChanged();
                    reclist.clear();
                    reclist.addAll(onlist);
                    carlistAdapter.setFilter(onlist);
                    online();
                    linerlayout_list_show.setVisibility(View.GONE);
                    linerlayout_list_none.setVisibility(View.VISIBLE);
                    break;
                case 13:
                    if (dialog != null){
                        WeiboDialogUtils.closeDialog(dialog);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    initNumber();
                    AppCons.addType = "online";
                    swipeRefreshLayout.setRefreshing(false);
                    carlistAdapter.notifyDataSetChanged();
                    reclist.clear();
                    reclist.addAll(onlist);
                    carlistAdapter.setFilter(onlist);
                    AppCons.locationList.clear();
                    AppCons.locationList.addAll(alllist);
                    linerlayout_list_show.setVisibility(View.VISIBLE);
                    linerlayout_list_none.setVisibility(View.GONE);
                    activity.refush();
                    online();
                    break;
                case 18:  //右下角点击刷新
                    if (dialog != null){
                        WeiboDialogUtils.closeDialog(dialog);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    initNumber();
                    reclist.clear();
                    reclist.addAll(onlist);
                    carlistAdapter.setFilter(onlist);
                    carlistAdapter.notifyDataSetChanged();
                    AppCons.addType = "online";
                    online();
//                    AppCons.locationList.clear();
//                    AppCons.locationList.addAll(onlist);
//                    activity.refush();
                    break;

            }
        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * @param activity 获取宿主activity的数据
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        LoginDataBean responseBean = ((CarContralActivity) activity).receiveData();      //获取宿主activity的方法


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_carlist, null);
        initView();
        initClick();
        initAdapter();
        initSearch();
        refush();

        return view;
    }

    /**
     * 车辆列表
     */
    private void initNumber() {
        textView_all.setText("全部" + "(" + alllist.size() + ")");
        textView_online.setText("在线" + "(" + onlist.size() + ")");
        textView_off.setText("离线" + "(" + offlist.size() + "" + ")");
        textView_unlive.setText("未激活" + "(" + unlivelist.size() + ")");
        Log.d("eeee", alllist.size() + " 在线" + onlist.size() + " 离线" + offlist.size() + "未激活" + unlivelist.size());

    }

    /**
     * 初始化搜索框，实现搜索功能
     */
    private void initSearch() {
        if (searchView != null) {        //去掉下划线
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
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Message message = new Message();
                message.what = 5;
                message.obj = newText;
                handler.sendMessage(message);
                return false;
            }
        });
        searchView.clearFocus();
    }

    /**
     * 初始化适配器，给适配器赋值
     */
    private void initAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Log.e("LENGTH", reclist.size() + "");
        carlistAdapter = new CarlistAdapter(reclist, getContext());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(carlistAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new ListViewDecoration());

        carlistAdapter.setOnItemClickListener(new CarlistAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemclick(View view, LocationListBean bean) {
//                AppCons.locationListBean = bean;
//                startActivity(new Intent(getActivity(), CarPayAcitivity.class));
                if (bean.isExpire()){
                    if (BuildConfig.app_tag.equals("car")){
                        startActivity(new Intent(getActivity(), PayAcitivity.class));
                    }else {
                        startActivity(new Intent(getActivity(), PayAcitivity.class));
                    }

                }else {
                    if (bean.getState() != 3){
                     if (bean.getLocation().getBdLat() == 0 && bean.getLocation().getBdLon() == 0){
                            Toast.makeText(getContext(),"设备未定位",Toast.LENGTH_SHORT).show();
                        }else {  //未到期
                            AppCons.devicelistBean = bean;
                            Log.e("传送的设备",bean.getTerminalID());
                            EventBus.getDefault().post(new EventIntentUtil(bean));
                            CarContralActivity carContralActivity = (CarContralActivity) getActivity();
                            carContralActivity.looks();
                        }
                    }else {
                        Toast.makeText(getContext(),"设备未激活",Toast.LENGTH_SHORT).show();
                    }
                }



            }
        });


    }

    private void initClick() {
        relativeLayout_scoll.setOnClickListener(this);
        textView_all.setOnClickListener(this);
        textView_online.setOnClickListener(this);
        textView_off.setOnClickListener(this);
        textView_unlive.setOnClickListener(this);
    }

    private void initView() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        width = dm.widthPixels;
        height = dm.heightPixels;
        viewHeight1 = dip2px(getActivity(), 48);
        viewHeight2 = dip2px(getActivity(), 160);
        mTree = (ListView) view.findViewById(R.id.id_tree);
        alllist = new ArrayList<>();
        reclist = new ArrayList<>();
        unlivelist = new ArrayList<>();
        onlist = new ArrayList<>();
        offlist = new ArrayList<>();
        linerlayout_list_show = (LinearLayout) view.findViewById(R.id.linerlayout_list_show);
        linerlayout_list_none = (LinearLayout) view.findViewById(R.id.linerlayout_list_none);
        linerlayout_list_agent = (LinearLayout) view.findViewById(R.id.linerlayout_list_agent);
        linearLayout_list_car = (LinearLayout) view.findViewById(R.id.linerlayout_list_car);
        relativeLayout_scoll = (RelativeLayout) view.findViewById(R.id.linerlayout_scoll);
        imageView_scoll = (ImageView) view.findViewById(R.id.image_turn);
        textView_agent = (TextView) view.findViewById(R.id.text_agent_select);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.id_swipe_ly);
        linearLayout_search = (LinearLayout) view.findViewById(R.id.linner_search);
        searchView = (SearchView) view.findViewById(R.id.search_cars);
        textView_all = (TextView) view.findViewById(R.id.text_all);
        textView_online = (TextView) view.findViewById(R.id.text_online);
        textView_off = (TextView) view.findViewById(R.id.text_off);
        textView_unlive = (TextView) view.findViewById(R.id.text_unlive);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview_allcars);
        searchView.clearFocus();
        if (AppCons.ACCOUNT != null) {
            textView_agent.setText(AppCons.ACCOUNT);
        }
        if (AppCons.loginDataBean.getData().getRole() == 3) {
            Log.e("个人用户登录","ddd");
            linerlayout_list_agent.setVisibility(View.GONE);
            relativeLayout_scoll.setVisibility(View.GONE);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) linearLayout_list_car.getLayoutParams();
            lp.setMargins(0, 0, 0, 0);
            linearLayout_list_car.setLayoutParams(lp);
            AppCons.AGENT_SCOLL = false;
        }
        scollLayout();
    }

    private void scollLayout() {
        relativeLayout_scoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    downY = event.getY();
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    upY = event.getY();
                    if (downY - upY > 50 && AppCons.AGENT_SCOLL == true && isScoll == true) {
                        scollToTop();
                    } else if (upY - downY > 50 && AppCons.AGENT_SCOLL == false && isScoll == true) {
                        scollBtmAni();
                    }
                }
                return false;
            }

        });
    }



    private void scollToTop() {
        imageView_scoll.setImageResource(R.drawable.icon_down);
        solllTopAni();
    }

    private void solllTopAni(){
        Log.d("onclick", "收起");
        if (dialog == null){
            dialog = WeiboDialogUtils.createLoadingDialog(getActivity(),"加载中");
        }else {
            dialog.show();
        }
        imageView_scoll.setImageResource(R.drawable.icon_down);
        animator1 = ObjectAnimator.ofFloat(linerlayout_list_agent, "translationY", 0, -(height - viewHeight1));
        animator2 = ObjectAnimator.ofFloat(relativeLayout_scoll, "translationY", 0, -(height - viewHeight2));
        animator1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isScoll = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.e("ani1", "over");
                isScoll = true;
                AppCons.AGENT_SCOLL = false;
                Message message = new Message();
                message.what = 0;
                handler.sendMessage(message);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator1.setDuration(1000);
        animator2.setDuration(1000);
        if (isScoll){
            animator1.start();
            animator2.start();
        }
    }

    private void clickTopAni(){
        Log.d("onclick", "收起");
        if (dialog == null){
            dialog = WeiboDialogUtils.createLoadingDialog(getActivity(),"加载中");
        }else {
            dialog.show();
        }
        imageView_scoll.setImageResource(R.drawable.icon_down);
        animator1 = ObjectAnimator.ofFloat(linerlayout_list_agent, "translationY", 0, -(height - viewHeight1));
        animator2 = ObjectAnimator.ofFloat(relativeLayout_scoll, "translationY", 0, -(height - viewHeight2));
        animator1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isScoll = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.e("ani1", "over");
                isScoll = true;
                AppCons.AGENT_SCOLL = false;
                Message message = new Message();
                message.what = 6;
                handler.sendMessage(message);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator1.setDuration(1000);
        animator2.setDuration(1000);
        if (isScoll){
            animator1.start();
            animator2.start();
        }
    }

    private void scollBtmAni(){
        animator1 = ObjectAnimator.ofFloat(linerlayout_list_agent, "translationY", -(height * (9.0f / 10.0f)), 0);
        animator2 = ObjectAnimator.ofFloat(relativeLayout_scoll, "translationY", -(height * (3.0f / 4.0f)), 0);
        imageView_scoll.setImageResource(R.drawable.icon_up);
        animator2.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isScoll = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.e("ani2", "over");
                isScoll = true;
                AppCons.AGENT_SCOLL = true;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator1.setDuration(1000);
        animator2.setDuration(1000);
        if (isScoll){
            animator1.start();
            animator2.start();
        }
    }


    private void initRush() {
        if (isExit) {        //如果已存在
            Log.e("EXIT_Yes", "AppCon.UseD: " + AGENTID_USED);
            alllist.clear();
            reclist.clear();
            unlivelist.clear();
            onlist.clear();
            offlist.clear();
            AppCons.locationList.clear();
            String rec = AppCons.ACCOUNT_MAP.get(AGENTID_USED);
            try {
                alllist.addAll(SaveList.String2SceneList(rec));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            seconShow();
        } else {  //如果不存在
            Log.e("EXIT_No", "AppCon.UseD: " + AGENTID_USED);
            alllist.clear();
            reclist.clear();
            unlivelist.clear();
            onlist.clear();
            offlist.clear();
            AppCons.locationList.clear();
            getnetDateSave(AGENTID_SELECT);
        }
        isExit = false;
    }


    /**
     * 下拉刷新
     */
    public void refush() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        getnetDates(AppCons.AGENTID_SELECT);
                        getnetNewDateData(AGENTID_SELECT);
                    }
                }, 1500);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linerlayout_scoll:
                if (!ButtonUtils.isFastDoubleClick(R.id.linerlayout_scoll)) {
                    scollView();
                }

                break;
            case R.id.text_all:
                Message message1 = new Message();
                message1.what = 1;
                handler.sendMessage(message1);
                all();
                break;
            case R.id.text_online:
                Message message2 = new Message();
                message2.what = 2;
                handler.sendMessage(message2);
                online();
                break;
            case R.id.text_off:
                Message message3 = new Message();
                message3.what = 3;
                handler.sendMessage(message3);
                off();
                break;
            case R.id.text_unlive:
                Message message4 = new Message();
                message4.what = 4;
                handler.sendMessage(message4);
                unlive();
                break;
        }
    }

    private void scollView() {
        if (AppCons.AGENT_SCOLL) {
            Log.e("AppCome","AGENTID_USED: " + AGENTID_USED + " ,AGENTID_SELECT: " + AGENTID_SELECT);
            imageView_scoll.setImageResource(R.drawable.icon_down);
            clickTopAni();          //点击事件
        } else {
           scollBtmAni();

        }



    }

    private void unlive() {
        textView_all.setTextColor(Color.parseColor("#999999"));
        textView_unlive.setTextColor(Color.parseColor("#ff7f00"));
        textView_off.setTextColor(Color.parseColor("#999999"));
        textView_online.setTextColor(Color.parseColor("#999999"));
    }

    private void off() {
        textView_all.setTextColor(Color.parseColor("#999999"));
        textView_unlive.setTextColor(Color.parseColor("#999999"));
        textView_off.setTextColor(Color.parseColor("#ff7f00"));
        textView_online.setTextColor(Color.parseColor("#999999"));
    }

    private void online() {
        textView_all.setTextColor(Color.parseColor("#999999"));
        textView_unlive.setTextColor(Color.parseColor("#999999"));
        textView_off.setTextColor(Color.parseColor("#999999"));
        textView_online.setTextColor(Color.parseColor("#ff7f00"));
    }

    private void all() {
        textView_all.setTextColor(Color.parseColor("#ff7f00"));
        textView_unlive.setTextColor(Color.parseColor("#999999"));
        textView_off.setTextColor(Color.parseColor("#999999"));
        textView_online.setTextColor(Color.parseColor("#999999"));
    }

    @Override
    public void onResume() {
        super.onResume();
        linearLayout_search.setFocusable(true);
        linearLayout_search.setFocusableInTouchMode(true);
        linearLayout_search.requestFocus();
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.e("listfragment", "onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppCons.AGENT_SCOLL = true;
        alllist.clear();
        alllist = null;
        reclist.clear();
        reclist = null;
        unlivelist.clear();
        unlivelist = null;
        onlist.clear();
        onlist = null;
        offlist.clear();
        offlist = null;
        carlistAdapter = null;
        AppCons.ACCOUNT_MAP.clear();


    }

    /**
     * @param peoples
     * @param query
     * @return 筛选逻辑，query筛选条件，peoples筛选的数据源
     */
    private List<LocationListBean> filter(List<LocationListBean> peoples, String query) {
        query = query.toLowerCase();
        final List<LocationListBean> filteredModelList = new ArrayList<>();
        for (LocationListBean people : peoples) {
            if (people.getCarNumber().equals("")) {
                final String nameEn = people.getNickname().toLowerCase();
                final String desEn = people.getTerminalID().toLowerCase();
                final String name = people.getNickname();
                final String des = people.getTerminalID();
                if (name.contains(query) || des.contains(query) || nameEn.contains(query) || desEn.contains(query)) {
                    filteredModelList.add(people);
                }
            } else {
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

    /**
     * 请求车辆列表数据，并初始化适配器
     */
    public void getnetDateSave(int id) {  //请求新的车辆列表数据
        QurryAgent qurryAgent = new QurryAgent(id);
        Gson gson = new Gson();
        String obj = gson.toJson(qurryAgent);
        NewHttpUtils.querrDevice(obj,getContext(),getDeviceCallback);
    }
    private ResponseCallback getDeviceCallback = new ResponseCallback() {
        @Override
        public void TaskCallBack(Object object) {
            if (alllist != null ) {
                            alllist.clear();
                            reclist.clear();
                            unlivelist.clear();
                            onlist.clear();
                            offlist.clear();

                        }
            if (object != null ){
                List<LocationListBean>list = (List<LocationListBean>) object;
                if (list != null && list.size() != 0){
                    alllist.addAll(list);
                    firstShow();   //请求新的
                        } else {
                            Message message = new Message();
                            message.what = 11;
                            handler.sendMessage(message);
                }
            }else {
                Message message = new Message();
                message.what = 11;
                handler.sendMessage(message);
            }

        }

        @Override
        public void FailCallBack(Object object) {
            Message message = new Message();
            message.what = 9;
            handler.sendMessage(message);
        }
    };

    /*
    * 每次点击车辆列表下方按钮刷新的请求
    * */
    public void getnetNewDateData(int id) {  //请求新的车辆列表数据,每次刷新点击当前界面的时候刷新
        QurryAgent qurryAgent = new QurryAgent(id);
        Gson gson = new Gson();
        String obj = gson.toJson(qurryAgent);
        NewHttpUtils.querrDevice(obj, getContext(), new ResponseCallback() {
            @Override
            public void TaskCallBack(Object object) {
                if (alllist != null ) {
                    alllist.clear();
                    reclist.clear();
                    unlivelist.clear();
                    onlist.clear();
                    offlist.clear();

                }
                if (object != null ){
                    List<LocationListBean>list = (List<LocationListBean>) object;
                    if (list != null && list.size() != 0){
                        alllist.addAll(list);
                        thirdShow();   //请求新的
                    } else {
                        Message message = new Message();
                        message.what = 11;
                        handler.sendMessage(message);
                    }
                }else {
                    Message message = new Message();
                    message.what = 11;
                    handler.sendMessage(message);
                }
            }

            @Override
            public void FailCallBack(Object object) {
                Message message = new Message();
                message.what = 9;
                handler.sendMessage(message);
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidd) {
        isShow = hidd;
        if (hidd && getActivity() != null) {
            alllist.clear();
            offlist.clear();
            unlivelist.clear();
            onlist.clear();
            reclist.clear();
        } else if (!hidd && getActivity() != null) {
            if (AppCons.AGENT_SCOLL) {
                if (isFirst) {
                    if (AppCons.loginDataBean.getData().getRole() == 3) {  //普通用户
                        initListDatas();
                    } else {        //代理商，先请求所有下级代理商
                        initData();
                        firstScoll = true;
                    }
                    isFirst = false;
                }
            } else {
                initListDatas();
            }


        }
    }

    private void initListDatas() {
        if (dialog == null){
            dialog = WeiboDialogUtils.createLoadingDialog(getActivity(),"加载中");
        }else {
            dialog.show();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (isFirst){
                        getnetDateSave(AGENTID_SELECT);
                    }else {
                        getnetNewDateData(AGENTID_SELECT);
                    }

                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 请求代理商数据
     */
    private void initData() {  //请求代理商数据
        if (dialog == null){
            dialog = WeiboDialogUtils.createLoadingDialog(getActivity(),"加载中");
        }else {
            dialog.show();
        }
        QurryAgent qurryAgent = new QurryAgent(AppCons.loginDataBean.getData().getId());
        Gson gson = new Gson();
        String obj = gson.toJson(qurryAgent);
        NewHttpUtils.querrAgent(obj,getContext(),getAgentCaback);


    }
    private ResponseCallback getAgentCaback = new ResponseCallback() {
        @Override
        public void TaskCallBack(Object object) {
            try {
                if (object != null){
                    agentList = (List<AgentlistBean>) object;
                    Message message = new Message();
                    message.what = 8;
                    handler.sendMessage(message);

                }
            }catch (Exception e){
                e.printStackTrace();
            }


        }

        @Override
        public void FailCallBack(Object object) {
            Message message = new Message();
            message.what = 8;
            handler.sendMessage(message);
        }
    };

    private void initAgentAdapter() {
        try {
            mAdapter = new SimpleTreeAdapter<AgentlistBean>(mTree, getActivity(), agentList, 10);
            mAdapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener() {
                @Override
                public void onClick(Nodes node, int position) {
                    textView_agent.setText(node.getUsername());
                    AGENTID_SELECT = node.getId() ;
                    if (node.isLeaf()) {
                        autoShow();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        mTree.setAdapter(mAdapter);

    }


    private void autoShow() {
        alllist.clear();
        reclist.clear();
        unlivelist.clear();
        onlist.clear();
        offlist.clear();
        AppCons.locationList.clear();
        scollToTop();
        AppCons.AGENT_SCOLL = false;
    }
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        Log.e("dp to px", (dpValue * scale + 0.5f) + "");
        return (int) (dpValue * scale + 0.5f);
    }
    /**
     * 代理商第一次进入的时候
     */
    private void firstShow(){   //刷新车辆列表，并且判断是否已经请求过
            reclist.addAll(alllist);
            postAgentList.clear();
//            PostAgent[] postAgent = new PostAgent[]{};
            for (int i = 0; i < alllist.size(); i++) {

                PostAgent agent = new PostAgent(alllist.get(i).getTerminalID(),alllist.get(i).getUser_id(),alllist.get(i).getNickname(),
                        alllist.get(i).getCarNumber());

                postAgentList.add(agent);

                if (alllist.get(i).getState() != 3) {     //如果激活了
                    if (alllist.get(i).getState() == 1) {
                        LocationListBean bean = alllist.get(i);
                        onlist.add(bean); //存储在线车辆
                        alllist.remove(i);
                        alllist.add(0, bean);
                    } else {
                        offlist.add(alllist.get(i));    //存储离线车辆
                    }
                } else {
                    unlivelist.add(alllist.get(i));     //存wei
                }
            }
        if (postAgentList.size() ==  alllist.size()){
            postAgentList.toArray();
            Map<Object,Object>map = new HashMap<>();
            map.put("usr_list",postAgentList);
            Log.e("数组关系集合",new String(new Gson().toJson(map)));
            NewHttpUtils.psotAgent(new String(new Gson().toJson(map)),getContext(),null);
            }
            try {
                AppCons.ACCOUNT_MAP.put(AGENTID_USED , SaveList.SceneList2String(alllist));      //存
            } catch (IOException e) {
                e.printStackTrace();
            }
            Message message = new Message();
            message.what = 13;  
            handler.sendMessage(message);
        }
    /**
     * 代理商第一次进入的时候
     */
    private void seconShow(){
        reclist.addAll(alllist);
        for (int i = 0; i < alllist.size(); i++) {
            if (alllist.get(i).getState() != 3) {     //如果激活了
                if (alllist.get(i).getState() == 1) {
                    LocationListBean bean = alllist.get(i);
                    onlist.add(bean); //存储在线车辆
                    alllist.remove(i);
                    alllist.add(0, bean);
                } else {
                    offlist.add(alllist.get(i));    //存储离线车辆
                }
            } else {
                unlivelist.add(alllist.get(i));     //存wei
            }
        }
        try {
            AppCons.ACCOUNT_MAP.put(AGENTID_USED , SaveList.SceneList2String(alllist));      //存
        } catch (IOException e) {
            e.printStackTrace();
        }
        Message message = new Message();
        message.what = 13;
        handler.sendMessage(message);
    }

    private void thirdShow(){  //右下角点击刷新
        reclist.addAll(alllist);
        for (int i = 0; i < alllist.size(); i++) {
            if (alllist.get(i).getState() != 3) {     //如果激活了
                if (alllist.get(i).getState() == 1) {
                    LocationListBean bean = alllist.get(i);
                    onlist.add(bean); //存储在线车辆
                    alllist.remove(i);
                    alllist.add(0, bean);
                } else {
                    offlist.add(alllist.get(i));    //存储离线车辆
                }
            } else {
                unlivelist.add(alllist.get(i));     //存wei
            }
        }
        try {
            AppCons.ACCOUNT_MAP.put(AGENTID_USED , SaveList.SceneList2String(alllist));      //存
        } catch (IOException e) {
            e.printStackTrace();
        }
        Message message = new Message();
        message.what = 18;
        handler.sendMessage(message);
    }
}