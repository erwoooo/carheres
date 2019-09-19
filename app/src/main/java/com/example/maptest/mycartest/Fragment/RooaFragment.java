package com.example.maptest.mycartest.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.example.maptest.mycartest.Entity.EightOrderBean;
import com.example.maptest.mycartest.Entity.NbDevice;
import com.example.maptest.mycartest.New.NewHttpUtils;
import com.example.maptest.mycartest.New.WeiboDialogUtils;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.UI.SetUi.AudioOrderActivity;
import com.example.maptest.mycartest.UI.SetUi.EightCenterActivity;
import com.example.maptest.mycartest.UI.SetUi.EightMoreSpeedActivity;
import com.example.maptest.mycartest.UI.SetUi.EightOrderActivity;
import com.example.maptest.mycartest.UI.SetUi.EightSpeedActivity;
import com.example.maptest.mycartest.UI.SetUi.GWOrderActivity;
import com.example.maptest.mycartest.UI.SetUi.QuerryOrderActivity;
import com.example.maptest.mycartest.UI.SetUi.RooaLixianActivity;
import com.example.maptest.mycartest.UI.SetUi.RooaOilContralActivity;
import com.example.maptest.mycartest.UI.SetUi.SgexOrderActivtiy;
import com.example.maptest.mycartest.UI.SetUi.service.BSJ;
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

public class RooaFragment extends Fragment implements View.OnClickListener {
    private View view,passwordView;
    private ImageView imageView_quit;
    private RelativeLayout  relative_oil ,relative_rooa,relative_audio,relative_center,relative_shk;
    private TextView textView_order;
    private LinearLayout linerlayout_roo_show,linerlayout_gw,linner_eight,linner_rooa_mode;
    private RelativeLayout relative_lixingw,relative_eight_morespeed,relative_eight_km,relative_eight_reset;
    private Dialog dialog;
    private AlertDialog dialog_pass;
    private String passWord;
    private EditText dialog_edit_pass;
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
                        startActivity(new Intent(getActivity(), RooaOilContralActivity.class));
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
        view = inflater.inflate(R.layout.fragment_setr, null);
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
        if (AppCons.locationListBean.getLocation().getDeviceProtocol() == 8){
            linner_eight.setVisibility(View.VISIBLE);
            linner_rooa_mode.setVisibility(View.GONE);
        }else {
            linner_eight.setVisibility(View.GONE);
            linner_rooa_mode.setVisibility(View.VISIBLE);
            if (AppCons.locationListBean.getLocation().getDeviceProtocol() == 6 ){
                linerlayout_gw.setVisibility(View.VISIBLE);
                linerlayout_roo_show.setVisibility(View.GONE);
            }else{
                linerlayout_gw.setVisibility(View.GONE);
                linerlayout_roo_show.setVisibility(View.VISIBLE);
                if ( AppCons.locationListBean.getLocation().getDeviceProtocol() == 2 || AppCons.locationListBean.getLocation().getDeviceProtocol() == 7){
                    relative_oil.setVisibility(View.GONE);
                }else if (AppCons.locationListBean.getLocation().getDeviceProtocol() == 3){
                    relative_oil.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void initClick() {
        imageView_quit.setOnClickListener(this);
        relative_audio.setOnClickListener(this);
        relative_oil.setOnClickListener(this);
        relative_rooa.setOnClickListener(this);
        textView_order.setOnClickListener(this);
        relative_lixingw.setOnClickListener(this);
        relative_eight_morespeed.setOnClickListener(this);
        relative_eight_km.setOnClickListener(this);
        relative_eight_reset.setOnClickListener(this);
        relative_shk.setOnClickListener(this);
        relative_center.setOnClickListener(this);
    }

    private void initView() {
        relative_eight_morespeed = (RelativeLayout) view.findViewById(R.id.relative_eight_morespeed);
        relative_center = (RelativeLayout) view.findViewById(R.id.relative_center);
        relative_shk = (RelativeLayout) view.findViewById(R.id.relative_shk);
        relative_eight_km = (RelativeLayout) view.findViewById(R.id.relative_eight_km);
        relative_eight_reset = (RelativeLayout) view.findViewById(R.id.relative_eight_reset);
        textView_order = (TextView) view.findViewById(R.id.text_qyerryr);
        imageView_quit = (ImageView) view.findViewById(R.id.image_quit_setr);
        relative_oil = (RelativeLayout) view.findViewById(R.id.relative_oilr);
        relative_rooa = (RelativeLayout) view.findViewById(R.id.relative_lixinrooa);
        linerlayout_roo_show = (LinearLayout) view.findViewById(R.id.linerlayout_roo_show);
        linerlayout_gw = (LinearLayout) view.findViewById(R.id.linner_gw_show);
        relative_lixingw = (RelativeLayout) view.findViewById(R.id.relative_lixingw);
        relative_audio = (RelativeLayout) view.findViewById(R.id.relative_audio);
        linner_eight = (LinearLayout) view.findViewById(R.id.linner_eight);
        linner_rooa_mode = (LinearLayout) view.findViewById(R.id.linner_rooa_mode);
        if(locationListBean.getDevice().getDeviceType().equals("Q6H")){
            relative_eight_km.setVisibility(View.GONE);
        }else {
            relative_shk.setVisibility(View.GONE);
            relative_center.setVisibility(View.GONE);
            relative_audio.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relative_eight_reset:
                AppCons.ETSELECT = 2;
                startActivity(new Intent(getActivity(), EightMoreSpeedActivity.class));
                break;
            case R.id.relative_eight_km:
                AppCons.ETSELECT = 1;
                startActivity(new Intent(getActivity(), EightMoreSpeedActivity.class));
                break;
            case R.id.relative_eight_morespeed:
                AppCons.ETSELECT = 0;
                if(locationListBean.getDevice().getDeviceType().equals("Q6H")){
                    startActivity(new Intent(getActivity(), EightSpeedActivity.class));
                }else {
                    startActivity(new Intent(getActivity(), EightMoreSpeedActivity.class));
                }
                break;
            case R.id.text_qyerryr:
                startActivity(new Intent(getActivity(), QuerryOrderActivity.class));
                break;
            case R.id.image_quit_setr:
                quitSet();
                break;
            case R.id.relative_oilr:
                if (havenEnter){
                    startActivity(new Intent(getActivity(), RooaOilContralActivity.class));
                }else {
                    showPassDialog();
                }
                break;
            case R.id.relative_lixinrooa:
                if (AppCons.locationListBean.getLocation().getDeviceProtocol() == 7){
                    startActivity(new Intent(getActivity(),SgexOrderActivtiy.class));
                }else {
                    startActivity(new Intent(getActivity(),RooaLixianActivity.class));
                }
                break;
            case R.id.relative_lixingw:
                startActivity(new Intent(getActivity(),GWOrderActivity.class));
                break;
            case R.id.relative_audio:
                startActivity(new Intent(getActivity(),AudioOrderActivity.class));
                break;
            case R.id.relative_shk:
                startActivity(new Intent(getActivity(),EightOrderActivity.class));
                break;
            case R.id.relative_center:
                startActivity(new Intent(getActivity(),EightCenterActivity.class));
                break;
        }
    }

    private void quitSet() {

        getActivity().finish();
    }


    /**
     * 查询离线指令
     */



    @Override
    public void onDestroy() {
        super.onDestroy();
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
                        String string = (String) object;
                        if (locationListBean.getLocation().getDeviceProtocol() == 8){
                            EightOrderBean eightOrderBean = JSONObject.parseObject(string,EightOrderBean.class);
                            AppCons.ETORDER = eightOrderBean;
                        }
                        if (locationListBean.getLocation().getDeviceProtocol() == 6){
                            BSJ bsj = JSONObject.parseObject(string,BSJ.class);
                            AppCons.GWORDER = bsj;
                        }else if (locationListBean.getLocation().getDeviceProtocol() == 2 || locationListBean.getLocation().getDeviceProtocol() ==3){
                            K100B k100B = JSONObject.parseObject(string,K100B.class);
                            AppCons.ORDERBEN = k100B;
                        }
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
