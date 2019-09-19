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
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.maptest.mycartest.New.NewHttpUtils;
import com.example.maptest.mycartest.New.TrackBean;
import com.example.maptest.mycartest.New.TrickListBean;
import com.example.maptest.mycartest.New.WeiboDialogUtils;
import com.example.maptest.mycartest.R;

import com.example.maptest.mycartest.UI.PlayTeaceActivity;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.DateChangeUtil;
import com.example.maptest.mycartest.Utils.DatePickActivity;
import com.example.maptest.mycartest.Utils.UtcDateChang;
import com.example.maptest.mycartest.Utils.http.ResponseCallback;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.R.attr.type;
import static com.example.maptest.mycartest.Utils.UtcDateChang.UtcDatetoLocaTime;

/**
 * Created by ${Author} on 2017/3/2.
 * Use to  历史记录查询
 */

public class HistoryFragment extends Fragment implements View.OnClickListener {

    private RelativeLayout relativeLayout_auto, relativeLayout_today, relativeLayout_three;
    private ImageView imageView_today, imageView_three, imageView_auto, imageView_quit;
    private LinearLayout linearLayout_date;
    private TextView textView_start, textView_stop;
    private Button button_sure;
    private View view;
    private String TerminalID, startTime, endTime,comPareTime = "2018-7-24 00:30:00";
    private int select = 0;
    private Calendar calendar;
    private Date now;
    private SimpleDateFormat sdf, sdfnow;
    private Dialog dialog;
    private AlertDialog dialog_locationType;
    private View view_location;
    private Date sdate, edate,comPareDate;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (dialog != null){
                        WeiboDialogUtils.closeDialog(dialog);
                    }
                    Toast.makeText(getContext(),"没有轨迹信息", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    if (dialog != null){
                        WeiboDialogUtils.closeDialog(dialog);
                    }
                    break;
                case 5:
                    startActivity(new Intent(getActivity(),PlayTeaceActivity.class));
                    if (dialog != null){
                        WeiboDialogUtils.closeDialog(dialog);
                    }
                    break;
                case 6:
                    if (dialog != null){
                        WeiboDialogUtils.closeDialog(dialog);
                    }
                    Toast.makeText(getActivity().getApplicationContext(), "网络错误，请重新请求", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history, null);
        requestPermissions(new String[]{Manifest.permission.READ_CALENDAR}, 0);
        initView();
        initClick();
        selecttoday();
        return view;
    }

    private void initView() {
        calendar = Calendar.getInstance();
        now = new Date();
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        sdfnow = new SimpleDateFormat("yyyy-MM-dd ");

        imageView_quit = (ImageView) view.findViewById(R.id.image_quit_history);
        relativeLayout_auto = (RelativeLayout) view.findViewById(R.id.relative_auto);
        relativeLayout_today = (RelativeLayout) view.findViewById(R.id.relative_today);
        relativeLayout_three = (RelativeLayout) view.findViewById(R.id.relative_three);
        linearLayout_date = (LinearLayout) view.findViewById(R.id.linerlayout_data);
        imageView_today = (ImageView) view.findViewById(R.id.image_sign);
        imageView_three = (ImageView) view.findViewById(R.id.image_signthree);
        imageView_auto = (ImageView) view.findViewById(R.id.image_signauto);
        textView_start = (TextView) view.findViewById(R.id.text_start);
        textView_stop = (TextView) view.findViewById(R.id.text_stop);
        button_sure = (Button) view.findViewById(R.id.button_hisensure);
    }

    private void initClick() {
        imageView_quit.setOnClickListener(this);
        button_sure.setOnClickListener(this);
        relativeLayout_today.setOnClickListener(this);
        relativeLayout_three.setOnClickListener(this);

        relativeLayout_auto.setOnClickListener(this);
        textView_start.setOnClickListener(this);
        textView_stop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_quit_history:
                overHistory();
                break;
            case R.id.relative_today:
                select = 0;
                selecttoday();
                break;
            case R.id.relative_three:
                select = 0;
                selectthree();
                break;
            case R.id.relative_auto:
                select = 1;
                selectauto();
                break;
            case R.id.text_start:
                Intent intent = new Intent(getActivity(), DatePickActivity.class);
                intent.putExtra("date", textView_start.getText());
                startActivityForResult(intent, 1);
                break;
            case R.id.text_stop:
                Intent intent1 = new Intent(getActivity(), DatePickActivity.class);
                intent1.putExtra("date", textView_stop.getText());
                startActivityForResult(intent1, 2);
                break;
            case R.id.button_hisensure:
                showLocationDialog();
                break;
        }
    }

    private void overHistory() {
        if (dialog != null)
            dialog = null;
        getActivity().finish();
    }

    /**
     * 请求历史轨迹数据，将数据传入轨迹播放界面
     */
    private void requestDate(String type) {

        TerminalID = AppCons.locationListBean.getTerminalID();
        if (startTime == null || endTime == null || TerminalID == null) {
            Toast.makeText(getContext(), "输入信息有误", Toast.LENGTH_SHORT).show();
        } else {
            sdate = DateChangeUtil.StrToDateS(startTime);
            edate = DateChangeUtil.StrToDateS(endTime);
            comPareDate = DateChangeUtil.StrToDate(comPareTime);
            if (sdate.getTime() > comPareDate.getTime()){  //开始的时间，大于预定的时间
                Log.e("requestDate1",AppCons.trackType + "");
                AppCons.trackType = 0;
                AppCons.sTime = UtcDateChang.Local2UTC(startTime);
                AppCons.eTime = UtcDateChang.Local2UTC(endTime);
                long dates = (edate.getTime() - sdate.getTime());
                if (DateChangeUtil.getDay(dates) > 5) {
                    Toast.makeText(getContext(), "最多查询5天的轨迹!", Toast.LENGTH_SHORT).show();
                } else {
                    dialog = WeiboDialogUtils.createLoadingDialog(getActivity(),"加载中");
                    TrackBean bean = new TrackBean(AppCons.locationListBean.getTerminalID(),AppCons.sTime,AppCons.eTime,400,1,type);
                    Gson gson = new Gson();
                    String obj = gson.toJson(bean);
                    Log.e("bean",obj);
                    NewHttpUtils.querrOldTrack(obj,getContext(),trackCallback);
                }
            }else if (edate.getTime() < comPareDate.getTime()){ //结束的时间小于预定的时间
                AppCons.trackType = 1;
                AppCons.sTime = UtcDateChang.Local2UTC(startTime);
                AppCons.eTime = UtcDateChang.Local2UTC(endTime);
                long dates = (edate.getTime() - sdate.getTime());
                if (DateChangeUtil.getDay(dates) > 5) {
                    Toast.makeText(getContext(), "最多查询5天的轨迹!", Toast.LENGTH_SHORT).show();
                } else {
                    dialog = WeiboDialogUtils.createLoadingDialog(getActivity(),"加载中");
                    TrackBean bean = new TrackBean(AppCons.locationListBean.getTerminalID(),AppCons.sTime,AppCons.eTime,400,1,type);
                    Gson gson = new Gson();
                    String obj = gson.toJson(bean);
                    NewHttpUtils.querrTrack(obj,getContext(),trackCallback);
                }
            }else {  //包含了预定的时间
                Toast.makeText(getContext(), "由于数据库升级，不能查看包含2018-7-24 00:30:00的时刻的轨迹!", Toast.LENGTH_SHORT).show();
            }


        }
    }

    private ResponseCallback trackCallback = new ResponseCallback() {
        @Override
        public void TaskCallBack(Object object) {
            if (object != null){
               AppCons.trickList = (List<TrickListBean>) object;
                if (AppCons.trickList.size() < 2){
                    Message message = new Message();
                    message.what = 0 ;
                    handler.sendMessage(message);
                }else {
                    Message message = new Message();
                    message.what = 5;
                    handler.sendMessage(message);
                }

            }
        }

        @Override
        public void FailCallBack(Object object) {
            Message message = new Message();
            message.what = 0;
            handler.sendMessage(message);
        }
    };
    /**
     * 选择自定义时间
     */
    private void selectauto() {
        startTime = null;
        endTime = null;
        linearLayout_date.setVisibility(View.VISIBLE);
        imageView_auto.setVisibility(View.VISIBLE);
        imageView_three.setVisibility(View.GONE);
        imageView_today.setVisibility(View.GONE);
    }

    /**
     * 选择最近三天
     */
    private void selectthree() {
        startTime = null;
        endTime = null;
        calendar = Calendar.getInstance();
        linearLayout_date.setVisibility(View.GONE);
        imageView_auto.setVisibility(View.GONE);
        imageView_three.setVisibility(View.VISIBLE);
        imageView_today.setVisibility(View.GONE);
        String nowdate = sdf.format(now);
        calendar.add(Calendar.DATE, -3);
        String three = sdf.format(calendar.getTime());
        startTime = three;
        endTime = nowdate;
        Log.e("HISTORYttt", startTime + ":"  + endTime);
    }

    /**
     * 选择今天
     */
    private void selecttoday() {
        startTime = null;
        endTime = null;
        String nowdate = sdf.format(now);
        String b = sdfnow.format(now) + "00" + ":" + "00";

        startTime = b;
        endTime = nowdate;
        linearLayout_date.setVisibility(View.GONE);
        imageView_auto.setVisibility(View.GONE);
        imageView_three.setVisibility(View.GONE);
        imageView_today.setVisibility(View.VISIBLE);
        Log.e("HISTORYtttoday", startTime + "..." + endTime);
    }

    /**
     * @param requestCode
     * @param resultCode
     * @param data        时间选择器
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (resultCode == Activity.RESULT_OK && requestCode == 1) {
                // 选择预约时间的页面被关闭
                String date = data.getStringExtra("date");
                if (!textView_start.getText().toString().equals(date)) {
                    textView_start.setText(data.getStringExtra("date"));
                    startTime = textView_start.getText().toString();
                } else {
                    System.out.println("选择未变");
                }
            }
            if (resultCode == Activity.RESULT_OK && requestCode == 2) {
                // 选择预约时间的页面被关闭
                String date = data.getStringExtra("date");
                if (!textView_stop.getText().toString().equals(date)) {
                    textView_stop.setText(data.getStringExtra("date"));
                    endTime = textView_stop.getText().toString();
                } else {
                    System.out.println("选择未变");
                }
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("history","onStop");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("history","onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        startTime = null;
        endTime = null;
        TerminalID = null;
        view = null;
        calendar = null;
        now = null;
        sdf = null;
        sdfnow = null;
        if (dialog != null)
            dialog = null;
    }

    public void showLocationDialog(){
        if (dialog_locationType == null){
            dialog_locationType = new AlertDialog.Builder(getActivity()).create();
            view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_locationtype,null);
            final RadioGroup group_container = (RadioGroup) view.findViewById(R.id.radio_location);

            group_container.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    RadioButton radioButton = (RadioButton)view.findViewById(group_container.getCheckedRadioButtonId());
                    String type = radioButton.getText().toString();
                    switch (type){
                        case "LBS":
                            AppCons.locationType = "Lbs";
                            break;
                        case "WIFI":
                            AppCons.locationType = "Wifi";
                            break;
                        case "北斗":
                            AppCons.locationType = "Bds";
                            break;
                        case "卫星定位":
                            AppCons.locationType = "Gps";
                            break;


                    }
                    Log.e("type",AppCons.locationType + "type" + type);
                }
            });
            Button button_ensure = (Button) view.findViewById(R.id.button_loca_sure);
            Button button_cancel = (Button) view.findViewById(R.id.button_loca_cancel);
            button_ensure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog_locationType.dismiss();

                    requestDate(AppCons.locationType);
                }
            });
            button_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog_locationType.dismiss();
                }
            });
            dialog_locationType.setView(view);
            dialog_locationType.show();
        }else {
            dialog_locationType.show();
        }



    }



}
