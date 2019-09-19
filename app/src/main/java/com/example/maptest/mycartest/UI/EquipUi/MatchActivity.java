package com.example.maptest.mycartest.UI.EquipUi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maptest.mycartest.Adapter.MatchAdapter;
import com.example.maptest.mycartest.Bean.AMatchBean;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.Utils.BaseActivity;
import com.example.maptest.mycartest.Utils.bluetoothmanager.BlueToothManager;
import com.example.maptest.mycartest.Utils.bluetoothmanager.BlueToothState;

import java.util.ArrayList;
import java.util.List;


import static com.example.maptest.mycartest.Utils.bluetoothmanager.BlueToothManager.handler;

/**
 * Created by ${Author} on 2017/10/7.
 * Use to
 */

public class MatchActivity extends BaseActivity implements View.OnClickListener{
    private RecyclerView recyclerView;
    private MatchAdapter matchAdapter;
    private List<String>list;
    private ImageView imageView_ani;
    private TextView textView_research,textView_look;
    private RotateAnimation rotate;
    private TextView textView_tip;
    private ImageView imageView_quit;
    public BlueToothManager bluetooth = null;
    public  String address = null;
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_macth);
        initView();
        initClick();

        handler = new Handler()
        {
            public void handleMessage(Message msg)
            {
                super.handleMessage(msg);
                switch(msg.what)
                {
                    case 2:
                        break;
                    case 5:
                        textView_tip.setText("正在为您查找周边设备...");
                        break;
                    case 6:
                        textView_tip.setText("找到以下设备");
                        textView_research.setText("重新查找");
                        break;
                    case 7:
                        Toast.makeText(MatchActivity.this,"连接失败", Toast.LENGTH_LONG).show();
                        break;
                    case 8:
                        bluetooth.StopSearchDevice();
                        Bundle b = msg.getData();
                        list.add(b.getString("Address"));
                        matchAdapter.notifyDataSetChanged();
                        if (rotate != null)
                        rotate.cancel();
                        break;
                }
            }
        };
    }


    private void initClick() {
        textView_look.setOnClickListener(this);
        textView_research.setOnClickListener(this);
        imageView_ani.setOnClickListener(this);
        imageView_quit.setOnClickListener(this);
    }

    private void initView() {
        imageView_quit = (ImageView) findViewById(R.id.image_quitmacth);
        bluetooth = new BlueToothManager(MatchActivity.this);       //初始化
        bluetooth.RegisterBroadCastReceive();                       //注册
        bluetooth.TurnOnBluetooth();
        textView_tip = (TextView) findViewById(R.id.text_tip);
        imageView_ani = (ImageView) findViewById(R.id.image_activity);
        textView_research = (TextView) findViewById(R.id.match_text_research);
        textView_look = (TextView) findViewById(R.id.match_text_surebind);
        initAni();
        list = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycleview_match);
        matchAdapter = new MatchAdapter(list,this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(matchAdapter);

        matchAdapter.setOnItemClickListener(new MatchAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemclick(View view, String bean) {
                address = bean;
                Log.e("address",address);
                Toast.makeText(getApplicationContext(),bean,Toast.LENGTH_SHORT).show();
                matchAdapter.notifyDataSetChanged();
            }
        });
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.match_text_surebind:
                    if (address == null){
                        Toast.makeText(getApplicationContext(),"请先选择设备MAC地址",Toast.LENGTH_SHORT).show();
                    }else {
                        startActivity(new Intent(MatchActivity.this,BindSuccessActivity.class).putExtra("address",address));
                    }

                break;
            case R.id.match_text_research:
                    serachBlue();
                break;
            case R.id.image_quitmacth:
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        bluetooth.RegisterBroadCastReceive();
    }

    private void bindBlue() {
        if(bluetooth != null)
        {
            Log.e("bluetoothaddr1",address);
            bluetooth.BlueToothConnect(address);

        }
    }

    private void serachBlue() {
        if (rotate == null){
            initAni();
            rotate.startNow();
        }else {
            rotate.startNow();
        }
        if (bluetooth != null){
            list.clear();
            bluetooth.StartSearchDevice();
        }
    }

    private void initAni(){
        Log.e("ani","1");
        rotate = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        LinearInterpolator lin = new LinearInterpolator();
        rotate.setInterpolator(lin);
        rotate.setDuration(1500);//设置动画持续时间
        Log.e("ani","2");
        /** 常用方法 */
        rotate.setRepeatCount(-1);//设置重复次数
        rotate.setFillAfter(true);//动画执行完后是否停留在执行完的状态
//        rotate.setStartOffset(10);//执行前的等待时间
        imageView_ani.setAnimation(rotate);
        rotate.cancel();
        Log.e("ani","3");
    }

    @Override
    protected void onPause() {
        super.onPause();
//        bluetooth.UnRegisterBroadCastReceive();
        list.clear();
        matchAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bluetooth.UnRegisterBroadCastReceive();
        handler.removeCallbacksAndMessages(null);
        rotate = null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
