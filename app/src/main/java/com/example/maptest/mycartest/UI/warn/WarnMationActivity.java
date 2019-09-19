package com.example.maptest.mycartest.UI.warn;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maptest.mycartest.Adapter.WarnMationAdapter;
import com.example.maptest.mycartest.New.NewHttpUtils;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.UI.EquipUi.SetWarnActivity;
import com.example.maptest.mycartest.UI.EquipUi.ShowWarnActivity;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.BaseActivity;
import com.example.maptest.mycartest.Utils.CheckActivity;
import com.example.maptest.mycartest.Utils.http.ResponseCallback;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.iflytek.autoupdate.a.b.k;


/**
 * Created by ${Author} on 2017/3/19.
 * Use to 显示所有/单个用户的报警信息
 */

public class WarnMationActivity extends BaseActivity implements View.OnClickListener {
    private ImageView imageView_set, imageView_quit;
    private WarnMationAdapter adapter;
    private List<WarnListBean> warnList;
    private List<WarnListBean>unReadList;
    private List<WarnListBean>readList;
    private String agentId;
    private String terminalID;
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private TextView textView_unread,textView_read;
    private Bundle bundle = new Bundle();
    private boolean unRead = true;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    showPopwindow(msg.arg1);
                    break;
                case 2:
                    WarnListBean bean = warnList.get(msg.arg2);
                    if (!bean.isRead()) {
                        isRead(bean);
                        bean.setRead(true);
                    } else {
                    }
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(AppCons.TEST_WARN, bean);
                    startActivity(new Intent(WarnMationActivity.this, ShowWarnActivity.class).putExtras(bundle));
                    break;
                case 4:
                    delete(warnList.get(msg.arg1));
                    warnList.remove(msg.arg1);
                    adapter.notifyDataSetChanged();
                    break;
                case 5:
                    deleteAll();
                    warnList.clear();
                    adapter.notifyDataSetChanged();
                    break;
                case 6:
                    Toast.makeText(getApplicationContext(),msg.obj.toString(),Toast.LENGTH_SHORT).show();
                    break;
                case 7:
                    if (CheckActivity.isForeground(WarnMationActivity.this)){
                    try{
                        Log.e("CheckActivity","true");
                        warnList.clear();
                        warnList.addAll(unReadList);
                        initAdapter();
                        unRead = true;
                        textView_read.setBackgroundResource(R.drawable.bg_plug_rightpress);
                        textView_unread.setBackgroundResource(R.drawable.bg_plug_leftnormal);
                        textView_read.setTextColor(Color.parseColor("#ffffff"));
                        textView_unread.setTextColor(Color.parseColor("#333333"));
                    }catch (Exception e){}

                }
                    break;
                case 8:
                    warnList.clear();
                    warnList.addAll(readList);
                    adapter.notifyDataSetChanged();
                    break;
                case 9:
                    if (unRead){            //如果是未读
                        unReadList.remove(msg.obj);
                    }else {
                        readList.remove(msg.obj);
                    }
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informationwa);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
        initView();
        initClick();
        try{
            terminalID = (String) getIntent().getExtras().get("TimID");
            agentId = (String) getIntent().getExtras().get("AgtID");
            Log.d("rrrr3", agentId + "；" + terminalID);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private void initAdapter() {
        Log.e("initAdapter","initAdapter");
            recyclerView.setLayoutManager(manager);
            recyclerView.setHasFixedSize(true);
            adapter = new WarnMationAdapter(warnList, this);
            recyclerView.setAdapter(adapter);
            adapter.setRecyclerViewOnItemClickListener(new WarnMationAdapter.RecyclerViewOnItemClickListener() {
                @Override
                public void onItemClickListener(View view, int position) {
//                    isRead(warnList.get(position));
                    Message message = new Message();
                    message.what = 2;
                    message.arg2 = position;
                    handler.sendMessage(message);

                }

                @Override
                public boolean onItemLongClickListener(View view, int position) {
                    Message message = new Message();
                    message.what = 1;
                    message.arg1 = position;
                    handler.sendMessage(message);
                    adapter.notifyDataSetChanged();
                    return true;
                }
            });


    }

    private void initClick() {
        imageView_set.setOnClickListener(this);
        imageView_quit.setOnClickListener(this);
        textView_read.setOnClickListener(this);
        textView_unread.setOnClickListener(this);
    }

    private void initView() {
        textView_unread = (TextView) findViewById(R.id.warn_text_unread);
        textView_read = (TextView) findViewById(R.id.warn_text_read);
        imageView_set = (ImageView) findViewById(R.id.image_setmwarn);
        imageView_quit = (ImageView) findViewById(R.id.image_ifmwquits);
        recyclerView = (RecyclerView) findViewById(R.id.info_recyclerview);
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        warnList = new ArrayList<>();
        readList = new ArrayList<>();
        unReadList = new ArrayList<>();
        initAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (terminalID == null){
            terminalID = (String) getIntent().getExtras().get("TimID");
            Log.e("AppCons.WARN_TYPE",AppCons.WARN_TYPE + " ,terminalID: " + terminalID);
        }
        initData();

    }

    @Override
    public void onClick(View v) {
        //获取你选中的item
        switch (v.getId()) {
            case R.id.image_setmwarn:
                bundle.putString("temid",terminalID);
                bundle.putString("agtid",agentId);
                startActivity(new Intent(WarnMationActivity.this, SetWarnActivity.class).putExtras(bundle));
                break;
            case R.id.image_ifmwquits:
                quit();
                break;
            case R.id.warn_text_read:
                read();
                break;
            case R.id.warn_text_unread:
                unRead();
                break;


        }
    }
    private void read() {
        unRead = false;
        Message message = new Message();
        message.what = 8;
        handler.sendMessage(message);
        textView_read.setBackgroundResource(R.drawable.bg_plug_rightnormal);
        textView_unread.setBackgroundResource(R.drawable.bg_plug_leftpress);
        textView_unread.setTextColor(Color.parseColor("#ffffff"));
        textView_read.setTextColor(Color.parseColor("#333333"));
    }
    private void unRead() {
        Message message = new Message();
        message.what = 7;
        handler.sendMessage(message);

    }

    /**
     * 请求报警信息数据
     */
    private void initData() {
        TmidBean bean = new TmidBean(terminalID);
        Gson gson = new Gson();
         String obj = gson.toJson(bean);
        NewHttpUtils.querrWarn(obj, WarnMationActivity.this, new ResponseCallback() {
            @Override
            public void TaskCallBack(Object object) {
                if (object != null){
                    warnList.clear();
                    readList.clear();
                    unReadList.clear();
                    warnList = (List<WarnListBean>) object;
                    for (WarnListBean bean1 : warnList){
                        if (bean1.isRead()){
                            readList.add(bean1);
                        }else {
                            unReadList.add(bean1);
                        }
                    }
                    Message message = new Message();
                    message.what = 7;
                    handler.sendMessage(message);
                }
                }
            @Override
            public void FailCallBack(Object object) {

            }
        });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
    /**
     * 〈一句话功能简述〉
     * 〈功能详细描述〉
     * @param position    [点击的位置]
     * 长按position，弹出提示框，删除，全删，取消
     */
    private void showPopwindow(final int position) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_popdelete, null);
        final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0xb000000);
        window.setBackgroundDrawable(dw);
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        window.showAtLocation(WarnMationActivity.this.findViewById(R.id.info_recyclerview), Gravity.BOTTOM, 0, 0);
        LinearLayout linearLayoutall = (LinearLayout) view.findViewById(R.id.linnerall);
        TextView textView_all = (TextView) view.findViewById(R.id.text_removeinfoall);
        TextView textView_remove = (TextView) view.findViewById(R.id.text_removeinfo);
        TextView textView_cancel = (TextView) view.findViewById(R.id.text_cancelremove);
        if (terminalID == null || terminalID.equals("")){
            linearLayoutall.setVisibility(View.GONE);
        }
        textView_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.what = 5;
                message.arg1 = position;
                handler.sendMessage(message);
                window.dismiss();
            }
        });
        textView_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.what = 4;
                message.arg1 = position;
                handler.sendMessage(message);
                window.dismiss();
            }
        });
        textView_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                System.out.print("消失");
            }
        });
    }

    /**
     * 〈一句话功能简述〉
     * 〈功能详细描述〉
     * @param bean     [已读对象]
     *  将已读的报警信息提交
     *
     */

    private void isRead(WarnListBean bean) {
        SaveAlarmBean saveAlarmBean = new SaveAlarmBean(bean.getId());
        Gson gson = new Gson();
        String obj = gson.toJson(saveAlarmBean);
        NewHttpUtils.saveRead(obj,WarnMationActivity.this);
    }
    /**
     * 〈一句话功能简述〉
     * 〈功能详细描述〉
     * @param bean    [删除的对象]
     *  向服务器提交删除的目标对象
     */
    private void delete(final WarnListBean bean) {
        DeleteID deleteID = new DeleteID(bean.getId());
        Gson gson = new Gson();
        String obj = gson.toJson(deleteID);
        NewHttpUtils.deleteId(obj, WarnMationActivity.this, new ResponseCallback() {
            @Override
            public void TaskCallBack(Object object) {
                Message message = new Message();
                message.what = 9;
                message.obj = bean;
                handler.sendMessage(message);
            }

            @Override
            public void FailCallBack(Object object) {

            }
        });

    }

    /**
     * 全部删除，只有个人用户有全部删除功能，代理商不能全部删除
     */
    private void deleteAll(){
        TmidBean tmidBean = new TmidBean(terminalID);
        Gson gson = new Gson();
        String obj = gson.toJson(tmidBean);
        NewHttpUtils.deleteAll(obj,WarnMationActivity.this);
//        OkHttpUtils.get()
//                .url("http://app.carhere.net/appAllDeleteAlarm")
//                .addParams("TerminalID",terminalID)
//                .build()
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Request request, Exception e) {
//                        Log.d("rrrr6","error" + e);
//                    }
//
//                    @Override
//                    public void onResponse(String response) {
//                    Log.d("rrrr6",response);
//                    }
//                });
    }

    private void quit(){
        agentId = null;
        adapter = null;
        if (warnList != null){
            warnList.clear();
        }
        if (unReadList != null){
            unReadList.clear();
        }
        if (readList != null){
            readList.clear();
        }
        agentId = null;
        terminalID = null;
        manager = null;
        recyclerView = null;
        handler.removeCallbacksAndMessages(null);
        finish();
        System.gc();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode){
            quit();
        }
        return super.onKeyDown(keyCode, event);
    }
}
