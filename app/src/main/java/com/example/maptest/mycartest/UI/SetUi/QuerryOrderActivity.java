package com.example.maptest.mycartest.UI.SetUi;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maptest.mycartest.Adapter.QueOrderAdapter;
import com.example.maptest.mycartest.Bean.QuerryOrderBean;
import com.example.maptest.mycartest.New.NewHttpUtils;
import com.example.maptest.mycartest.New.WeiboDialogUtils;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.UI.SetUi.service.Command;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.BaseActivity;
import com.example.maptest.mycartest.Utils.DateChangeUtil;
import com.example.maptest.mycartest.Utils.DatePickActivity;
import com.example.maptest.mycartest.Utils.http.ResponseCallback;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by ${Author} on 2017/7/24.
 * Use to
 */

public class QuerryOrderActivity extends BaseActivity implements View.OnClickListener{
    private RecyclerView recyclerView;
    private QueOrderAdapter queOrderAdapter;
    private List<Command>list;
    private ImageView imageView_quit;
    private TextView text_order_start,text_order_stop;
    private Button button_querry_order;
    private Map<Object,Object>map = new HashMap<>();
    private String beginTime,endTime;
    private long bgTime,edTime;
    private Dialog dialog;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    queOrderAdapter.notifyDataSetChanged();
                    break;
                case 4:
//                    delete(list.get(msg.arg1));
//                    list.remove(msg.arg1);
//                    queOrderAdapter.notifyDataSetChanged();
                    break;
                case 5:
                    Toast.makeText(getApplicationContext(),msg.obj.toString(),Toast.LENGTH_SHORT).show();
                    break;
                case 11:
                    if (queOrderAdapter == null){
                        queOrderAdapter = new QueOrderAdapter(list,QuerryOrderActivity.this);
                        recyclerView.setAdapter(queOrderAdapter);
                        queOrderAdapter.setRecyclerViewOnItemClickListener(new QueOrderAdapter.RecyclerViewOnItemClickListener() {
                            @Override
                            public void onItemClickListener(View view, int position) {

                            }

                            @Override
                            public boolean onItemLongClickListener(View view, int position) {  //长按删除
                                showPopwindow(position);
                                return false;
                            }
                        });
                    }else {
                        queOrderAdapter.notifyDataSetChanged();
                    }
                    if (dialog != null){
                        WeiboDialogUtils.closeDialog(dialog);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_querry);
        initView();
        initClick();


    }



    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycleview_querry);
        imageView_quit = (ImageView) findViewById(R.id.image_quitquerry);
        text_order_start = (TextView) findViewById(R.id.text_order_start);
        text_order_stop = (TextView) findViewById(R.id.text_order_stop);
        button_querry_order = (Button) findViewById(R.id.button_querry_order);

        list = new ArrayList<>();
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }


    private void initClick() {
        imageView_quit.setOnClickListener(this);
        button_querry_order.setOnClickListener(this);
        text_order_start.setOnClickListener(this);
        text_order_stop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_quitquerry:
                quit();
                break;
            case R.id.text_order_start:
                Intent intent = new Intent(this, DatePickActivity.class);
                intent.putExtra("date", text_order_start.getText());
                startActivityForResult(intent, 1);
                break;
            case R.id.text_order_stop:
                Intent intent1 = new Intent(this, DatePickActivity.class);
                intent1.putExtra("date", text_order_stop.getText());
                startActivityForResult(intent1, 2);
                break;
            case R.id.button_querry_order:
                initOrders(beginTime,endTime);
                if (dialog == null){
                    dialog = WeiboDialogUtils.createLoadingDialog(this,"查询中...");
                }else {
                    dialog.show();
                }
                break;
        }
    }

    private void initOrders(String start,String end) {
        if (start.equals("") || end.equals("")){
            Toast.makeText(this,"选择的时间段为空!",Toast.LENGTH_SHORT).show();
        }else {
            Log.e("选择的时间","start: " + start + " ,end: " + end);
            bgTime = DateChangeUtil.StrToDateHi(start).getTime();
            edTime = DateChangeUtil.StrToDateHi(end).getTime();
            if (bgTime > edTime){
                Toast.makeText(this,"开始时间比结束时间大!",Toast.LENGTH_SHORT).show();
            }else {
                map.put("terminalID", AppCons.locationListBean.getTerminalID());
                map.put("beginTime", bgTime);
                map.put("endTime",edTime);
                Gson gson = new Gson();
                String obj = gson.toJson(map);
                Log.e("查询",obj);
                NewHttpUtils.querryCommand(obj, QuerryOrderActivity.this, new ResponseCallback() {
                    @Override
                    public void TaskCallBack(Object object) {
                        List<Command>commandList = (List<Command>) object;
                        for (Command command : commandList){
                            Log.e("查询的",command.toString());
                        }
                        list.clear();
                        list.addAll(commandList);
                        Message message = new Message();
                        message.what = 11;
                        handler.sendMessage(message);

                    }

                    @Override
                    public void FailCallBack(Object object) {
                        if (dialog != null){
                            WeiboDialogUtils.closeDialog(dialog);
                        }
                    }
                });
            }
        }



    }

    private void showPopwindow(final int position) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pop_delorder, null);
        final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0xb000000);
        window.setBackgroundDrawable(dw);
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        window.showAtLocation(QuerryOrderActivity.this.findViewById(R.id.recycleview_querry), Gravity.BOTTOM, 0, 0);
        TextView textView_remove = (TextView) view.findViewById(R.id.text_remorder);
        TextView textView_cancel = (TextView) view.findViewById(R.id.text_cancelremorder);

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
     * @param bean
     * 删除对应的指令
     */
    private void delete(QuerryOrderBean bean) {
        Log.e("id",bean.toString());
        OkHttpUtils.get()
                .url("http://app.carhere.net/appRemoveWireless")
                .addParams("_id",bean.get_id())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Log.d("eeeee5", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("rrrr5", response);
                    }
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            quit();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void quit() {
        list.clear();
        list = null;
        finish();
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
                if (!text_order_start.getText().toString().equals(date)) {
                    text_order_start.setText(data.getStringExtra("date"));
                    beginTime = text_order_start.getText().toString();
                } else {
                    System.out.println("选择未变");
                }
            }
            if (resultCode == Activity.RESULT_OK && requestCode == 2) {
                // 选择预约时间的页面被关闭
                String date = data.getStringExtra("date");
                if (!text_order_stop.getText().toString().equals(date)) {
                    text_order_stop.setText(data.getStringExtra("date"));
                    endTime = text_order_stop.getText().toString();
                } else {
                    System.out.println("选择未变");
                }
            }
        }
    }
}
