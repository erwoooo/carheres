package com.example.maptest.mycartest.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.example.maptest.mycartest.Entity.NbDevice;
import com.example.maptest.mycartest.New.NewHttpUtils;
import com.example.maptest.mycartest.New.WeiboDialogUtils;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.UI.SetUi.AudioOrderActivity;
import com.example.maptest.mycartest.UI.SetUi.CrawlActivity;
import com.example.maptest.mycartest.UI.SetUi.LowPowerActivity;
import com.example.maptest.mycartest.UI.SetUi.MoreSpeedActivity;
import com.example.maptest.mycartest.UI.SetUi.QuerryOrderActivity;
import com.example.maptest.mycartest.UI.SetUi.RemoveOrderActivtiy;
import com.example.maptest.mycartest.UI.SetUi.SensorActivity;
import com.example.maptest.mycartest.UI.SetUi.SetandCanActivity;
import com.example.maptest.mycartest.UI.SetUi.ShkActivity;
import com.example.maptest.mycartest.UI.SetUi.SosActivity;
import com.example.maptest.mycartest.UI.SetUi.WorkmodeActivity;
import com.example.maptest.mycartest.UI.SetUi.service.K100B;
import com.example.maptest.mycartest.UI.SetUi.service.TirmeredBean;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.http.ResponseCallback;
import com.google.gson.Gson;


import static com.example.maptest.mycartest.Utils.AppCons.locationListBean;


/**
 * Created by ${Author} on 2017/3/13.
 * Use to
 */

public class GwaFragment extends BaseFragment implements View.OnClickListener {
    private View view;
    private ImageView imageView_quit;
    private RelativeLayout relative_sos, relative_center, relative_crawl, relative_shk, relative_speed, relative_lowper, relative_nopower, relative_move;
    private RelativeLayout relative_audio,relative_craws;
    private TextView  textView_qurry;
    public static RequestQueue queue;
    private Dialog dialog;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * @param activity 获取到车辆信息
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_gwa, null);
        initView();
        initClick();
        return view;
    }

    /**
     * 判断是否是离线指令显示界面
     */
    @Override
    public void onStart() {
        super.onStart();

    }

    /**
     * 判断协议号，显示离线指令界面
     */
    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    private void initClick() {
        textView_qurry.setOnClickListener(this);
        imageView_quit.setOnClickListener(this);
        relative_sos.setOnClickListener(this);
        relative_center.setOnClickListener(this);
        relative_crawl.setOnClickListener(this);
        relative_shk.setOnClickListener(this);
        relative_speed.setOnClickListener(this);
        relative_lowper.setOnClickListener(this);
        relative_nopower.setOnClickListener(this);
        relative_move.setOnClickListener(this);
        relative_audio.setOnClickListener(this);
        relative_craws.setOnClickListener(this);
    }

    private void initView() {
        relative_audio = (RelativeLayout) view.findViewById(R.id.relative_audio);
        textView_qurry = (TextView) view.findViewById(R.id.text_qyerry);
        imageView_quit = (ImageView) view.findViewById(R.id.image_quit_set);
        relative_sos = (RelativeLayout) view.findViewById(R.id.relative_sos);
        relative_center = (RelativeLayout) view.findViewById(R.id.relative_center);
        relative_crawl = (RelativeLayout) view.findViewById(R.id.relative_crawl);
        relative_shk = (RelativeLayout) view.findViewById(R.id.relative_shk);
        relative_speed = (RelativeLayout) view.findViewById(R.id.relative_speed);
        relative_lowper = (RelativeLayout) view.findViewById(R.id.relative_lowper);
        relative_nopower = (RelativeLayout) view.findViewById(R.id.relative_noper);
        relative_move = (RelativeLayout) view.findViewById(R.id.relative_movewarn);
        relative_craws = view.findViewById(R.id.relative_craws);
        if (locationListBean.getDevice().getDeviceType().equals("GW005A")){
            relative_craws.setVisibility(View.VISIBLE);
        }else {
            relative_craws.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_qyerry:
                startActivity(new Intent(getActivity(), QuerryOrderActivity.class));
                break;
            case R.id.image_quit_set:
                quitSet();
                break;
            case R.id.relative_sos:
                startActivity(new Intent(getActivity(), SosActivity.class));
                break;
            case R.id.relative_center:   //拨打号码
                startActivity(new Intent(getActivity(), SetandCanActivity.class));
                break;
            case R.id.relative_crawl:   ////工作模式
                startActivity(new Intent(getActivity(), WorkmodeActivity.class));
                break;
            case R.id.relative_shk:
                startActivity(new Intent(getActivity(), ShkActivity.class));
                break;
            case R.id.relative_speed:
                startActivity(new Intent(getActivity(), MoreSpeedActivity.class));
                break;
            case R.id.relative_lowper:
                startActivity(new Intent(getActivity(), LowPowerActivity.class));
                break;
            case R.id.relative_noper:   //拆出报警
                startActivity(new Intent(getActivity(), RemoveOrderActivtiy.class));
                break;
            case R.id.relative_movewarn:  //灵敏度
                startActivity(new Intent(getActivity(), SensorActivity.class));
                break;
            case R.id.relative_audio:
                startActivity(new Intent(getActivity(),AudioOrderActivity.class));
                break;
            case R.id.relative_craws:
                startActivity(new Intent(getActivity(), CrawlActivity.class));
                break;
        }
    }



    private void quitSet() {
        if (queue != null)
            queue = null;


        getActivity().finish();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        AppCons.ORDERBEN = null;

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (hidden){
        }else {
            dialog = WeiboDialogUtils.createLoadingDialog(getActivity(),"正在加载");
            TirmeredBean bean = new TirmeredBean(locationListBean.getTerminalID(),locationListBean.getLocation().getDeviceProtocol());
            Gson gson = new Gson();
             String obj = gson.toJson(bean);
            NewHttpUtils.findCommand(obj, getContext(), new ResponseCallback() {
                @Override
                public void TaskCallBack(Object object) {
                    if (object !=null){
                        K100B setbean = JSONObject.parseObject(object.toString(),K100B.class);
                        AppCons.ORDERBEN = setbean;
                        if (dialog != null)
                            WeiboDialogUtils.closeDialog(dialog);

                    }else {
                        if (dialog != null)
                            WeiboDialogUtils.closeDialog(dialog);
                    }
                }
                @Override
                public void FailCallBack(Object object) {
                        if (dialog != null)
                            WeiboDialogUtils.closeDialog(dialog);
                }
            });

            NewHttpUtils.findDeviceId(locationListBean.getTerminalID(), getContext(), new ResponseCallback() {
                @Override
                public void TaskCallBack(Object object) {
                    if (object != null){
                        AppCons.mNbDevice = (NbDevice) object;
                        Log.e("返回值",AppCons.mNbDevice.toString());
                    }
                }

                @Override
                public void FailCallBack(Object object) {

                }
            });
        }
    }


}
