package com.example.maptest.mycartest.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.maptest.mycartest.Adapter.TyreAdapter;
import com.example.maptest.mycartest.New.LocationListBean;
import com.example.maptest.mycartest.New.WeiboDialogUtils;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.SwipRecycleView.ListViewDecoration;
import com.example.maptest.mycartest.SwipRecycleView.listener.OnItemClickListener;
import com.example.maptest.mycartest.UI.TyreUi.TyreActivity;
import com.example.maptest.mycartest.Utils.AppCons;

/**
 * Created by ${Author} on 2017/3/2.
 * Use to   胎压fragment
 */

public class TyreFragment extends Fragment implements View.OnClickListener {
    private View view;

    private RecyclerView recyclerView;
    private TyreAdapter tyreAdapter;
    private OnItemClickListener onItemClickListener;
    private Dialog dialog;
    private LinearLayout linerlayout_tyre_none,linerlayout_tyre_show;
    public TyreFragment() {

    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if (AppCons.tyreList.size() == 0){
                        linerlayout_tyre_none.setVisibility(View.VISIBLE);
                        linerlayout_tyre_show.setVisibility(View.GONE);
                    }else {
                        linerlayout_tyre_none.setVisibility(View.GONE);
                        linerlayout_tyre_show.setVisibility(View.VISIBLE);
                        tyreAdapter.notifyDataSetChanged();
                    }
                    break;
                case 2:
                    if (dialog != null ){
                        WeiboDialogUtils.closeDialog(dialog);
                    }
                    break;
            }

        }
    };
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * @param activity 从宿主activity获取数据
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tyre, null);
        initView();
        return view;
    }

    /**
     * 初始化适配器
     */
    private void initView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview_tyre);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        tyreAdapter = new TyreAdapter(AppCons.tyreList, getContext());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(tyreAdapter);
        linerlayout_tyre_show = (LinearLayout) view.findViewById(R.id.linerlayout_tyre_show);
        linerlayout_tyre_none = (LinearLayout) view.findViewById(R.id.linerlayout_tyre_none);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new ListViewDecoration());

        tyreAdapter.setOnItemClickListener(new TyreAdapter.OnRecyclerViewItemClickListener() {
            /**
             * @param view
             * @param bean
             * 适配器的点击事件，点击进入胎压界面
             */
            @Override
            public void onItemclick(View view, LocationListBean bean) {

                LocationListBean beans = bean;
                Bundle bundle = new Bundle();
                bundle.putSerializable(AppCons.TEST_TYR, beans);
                startActivity(new Intent(getActivity(), TyreActivity.class).putExtras(bundle));
            }
        });

    }

    @Override
    public void onClick(View v) {

    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        view = null;
        recyclerView = null;
        tyreAdapter = null;
        onItemClickListener = null;

    }

    @Override
    public void onHiddenChanged(boolean hidd) {
        AppCons.tyreList.clear();
        if (hidd && getActivity() != null) {

            Log.e("tyrefragment","tyrefragment");
        }else if (!hidd && getActivity() != null){
            dialog = WeiboDialogUtils.createLoadingDialog(getActivity(),"加载中");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (AppCons.locationList.size() != 0){
//                            AppCons.tyreList.addAll(AppCons.locationList);
                            for (LocationListBean bean : AppCons.locationList){
                                if (bean.getLocation() != null && bean.getState() != 3 && bean.getState() != 4 && bean.getLocation().getDeviceProtocol() == 4 ){
                                    if (bean.getState() == 1){  //如果在线在最前面
                                        AppCons.tyreList.add(0,bean);
                                    }else {
                                        AppCons.tyreList.add(bean);
                                    }
                                }
                            }
                        }
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Message message = new Message();
                    message.what = 2;
                    handler.sendMessage(message);
                }
            }).start();
        }
    }
}
