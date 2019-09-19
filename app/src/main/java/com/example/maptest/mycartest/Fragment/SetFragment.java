package com.example.maptest.mycartest.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.example.maptest.mycartest.Entity.NbDevice;
import com.example.maptest.mycartest.New.NewHttpUtils;
import com.example.maptest.mycartest.New.WeiboDialogUtils;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.UI.SetUi.CenterNubActivity;
import com.example.maptest.mycartest.UI.SetUi.CrawlActivity;
import com.example.maptest.mycartest.UI.SetUi.LowPowerActivity;
import com.example.maptest.mycartest.UI.SetUi.MoreSpeedActivity;
import com.example.maptest.mycartest.UI.SetUi.MoveActivity;
import com.example.maptest.mycartest.UI.SetUi.NoPowerActivity;
import com.example.maptest.mycartest.UI.SetUi.OilContralActivity;
import com.example.maptest.mycartest.UI.SetUi.QuerryOrderActivity;
import com.example.maptest.mycartest.UI.SetUi.ShkActivity;
import com.example.maptest.mycartest.UI.SetUi.SosActivity;
import com.example.maptest.mycartest.UI.SetUi.TimeOilActivity;
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

public class SetFragment extends BaseFragment implements View.OnClickListener {
    private View view,passwordView;
    private ImageView imageView_quit;
    private RelativeLayout relative_sos, relative_center, relative_crawl, relative_shk, relative_speed, relative_lowper,
            relative_nopower, relative_move, relative_oil,relative_time;
    private LinearLayout linearLayout1, linearLayout2, linearLayout3;
    private TextView  textView_qurry;
    public static RequestQueue queue;
    private Dialog dialog;
    private AlertDialog dialog_pass;
    private String passWord;
    private  EditText dialog_edit_pass;
    private boolean havenEnter = false;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            NewHttpUtils.checkPassword(passWord, locationListBean.getTerminalID(), getActivity(), new ResponseCallback() {
                @Override
                public void TaskCallBack(Object object) {
                String result = (String) object;
                    if (result.contains("true")){
                        startActivity(new Intent(getActivity(), OilContralActivity.class));
                        havenEnter = true;
                        if (dialog_pass != null && dialog_pass.isShowing()){
                            dialog_pass.dismiss();
                        }
                    }else {
                        Toast.makeText(getActivity(),"密码错误",Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void FailCallBack(Object object) {
                    Toast.makeText(getActivity(),"密码错误",Toast.LENGTH_SHORT).show();
                }
            });
        }
    };
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

        view = inflater.inflate(R.layout.fragment_set, null);
        initView();
        initClick();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("setfragment", "onResume");
        switch (AppCons.locationListBean.getDevice().getDeviceType()){
            case "G300S":
            case "G11":
            case "G11A":
                linearLayout3.setVisibility(View.GONE);
                relative_nopower.setVisibility(View.GONE);
                break;
            case "EVO2":
            case "G27":
            case "GM02D":
            case "BY-015":
                linearLayout3.setVisibility(View.GONE);
                break;
            case "K100":
            case "GM02":
            case "C200":
                linearLayout2.setVisibility(View.GONE);
                linearLayout3.setVisibility(View.GONE);
                break;
            case "R001":
            case "R002":
                linearLayout3.setVisibility(View.GONE);
                relative_shk.setVisibility(View.GONE);
                relative_move.setVisibility(View.GONE);
                relative_nopower.setVisibility(View.GONE);
                break;
            case "R001A":
                relative_move.setVisibility(View.GONE);
                relative_nopower.setVisibility(View.GONE);
                relative_shk.setVisibility(View.GONE);
                break;
            case "518T":
                relative_nopower.setVisibility(View.GONE);
                relative_lowper.setVisibility(View.GONE);
                break;
            case "G16":
                relative_move.setVisibility(View.GONE);
                relative_nopower.setVisibility(View.GONE);
                relative_lowper.setVisibility(View.GONE);
                relative_shk.setVisibility(View.GONE);
                linearLayout3.setVisibility(View.GONE);
                break;
            case "G17":
            case "GT06":
            case "GT09":
                relative_sos.setVisibility(View.GONE);
                relative_center.setVisibility(View.GONE);
                relative_move.setVisibility(View.GONE);
                relative_nopower.setVisibility(View.GONE);
                relative_lowper.setVisibility(View.GONE);
                relative_shk.setVisibility(View.GONE);
                linearLayout3.setVisibility(View.GONE);
                break;
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("setfragment", "onPause");
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
        relative_oil.setOnClickListener(this);
        relative_time.setOnClickListener(this);

    }

    private void initView() {
        textView_qurry = (TextView) view.findViewById(R.id.text_qyerry);
        linearLayout1 = (LinearLayout) view.findViewById(R.id.linerlayout_set1);
        linearLayout2 = (LinearLayout) view.findViewById(R.id.linerlayout_set2);
        linearLayout3 = (LinearLayout) view.findViewById(R.id.linerlayout_set3);
        imageView_quit = (ImageView) view.findViewById(R.id.image_quit_set);
        relative_sos = (RelativeLayout) view.findViewById(R.id.relative_sos);
        relative_center = (RelativeLayout) view.findViewById(R.id.relative_center);
        relative_crawl = (RelativeLayout) view.findViewById(R.id.relative_crawl);
        relative_shk = (RelativeLayout) view.findViewById(R.id.relative_shk);
        relative_speed = (RelativeLayout) view.findViewById(R.id.relative_speed);
        relative_lowper = (RelativeLayout) view.findViewById(R.id.relative_lowper);
        relative_nopower = (RelativeLayout) view.findViewById(R.id.relative_noper);
        relative_move = (RelativeLayout) view.findViewById(R.id.relative_movewarn);
        relative_oil = (RelativeLayout) view.findViewById(R.id.relative_oil);
        relative_time = (RelativeLayout) view.findViewById(R.id.relative_time);
        if(locationListBean.getLocation() != null && locationListBean.getLocation().getDeviceProtocol() == 11){
            relative_time.setVisibility(View.GONE);
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
            case R.id.relative_center:
                startActivity(new Intent(getActivity(), CenterNubActivity.class));
                break;
            case R.id.relative_crawl:
                startActivity(new Intent(getActivity(), CrawlActivity.class));
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
            case R.id.relative_noper:
                startActivity(new Intent(getActivity(), NoPowerActivity.class));
                break;
            case R.id.relative_movewarn:
                startActivity(new Intent(getActivity(), MoveActivity.class));
                break;
            case R.id.relative_oil:
                if (havenEnter){
                    startActivity(new Intent(getActivity(), OilContralActivity.class));
                }else {
                    showPassDialog();
                }
                break;
            case R.id.relative_time:
                startActivity(new Intent(getActivity(),TimeOilActivity.class));
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
        havenEnter = false;

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

    private void showPassDialog(){
        if (dialog_pass == null){
            dialog_pass = new AlertDialog.Builder(getActivity()).create();
            passwordView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_pass,null);
            dialog_edit_pass = (EditText) passwordView.findViewById(R.id.dialog_edit_pass);
            Button button_pass_cancel = (Button) passwordView.findViewById(R.id.button_pass_cancel);
            Button button_pass_sure = (Button) passwordView.findViewById(R.id.button_pass_sure);
            button_pass_sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    passWord = dialog_edit_pass.getText().toString();
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                }
            });
            button_pass_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog_pass.dismiss();
                }
            });
            dialog_pass.setView(passwordView);
            dialog_pass.show();
        }else {
            dialog_pass.show();
        }
    }

}
