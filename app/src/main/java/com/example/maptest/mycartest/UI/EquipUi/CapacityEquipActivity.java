package com.example.maptest.mycartest.UI.EquipUi;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.maptest.mycartest.Adapter.CapacityEquipAdapter;
import com.example.maptest.mycartest.Bean.ABtbean;
import com.example.maptest.mycartest.Bean.BlueOrderBean;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.UI.CarContralActivity;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.BaseActivity;
import com.example.maptest.mycartest.Utils.SaveList;
import com.example.maptest.mycartest.Utils.bluetoothmanager.BlueToothManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.maptest.mycartest.Utils.bluetoothmanager.BlueToothManager.handler;

/**
 * Created by ${Author} on 2017/10/7.
 * Use to
 */

public class CapacityEquipActivity extends BaseActivity implements View.OnClickListener{
    private RecyclerView recyclerView;
    private List<BlueOrderBean>list;
    private List<String>listScan;
    private CapacityEquipAdapter equipAdapter;
    private ImageView imageView_add;
    private RelativeLayout relativeLayout_none;
    private LinearLayout linearLayout_equip;
    private BlueOrderBean blueOrderBean;
    private boolean isFirst = true;
    private ImageView imageView_quit;
    private boolean isExit = false;
    public BlueToothManager bluetooth = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capacity);
//        ActivityCompat.requestPermissions(CapacityEquipActivity.this,
//                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                100);
        initView();

        BlueToothManager.handler = new Handler()
        {
            public void handleMessage(Message msg)
            {
                super.handleMessage(msg);
                switch(msg.what)
                {

                    case 5:

                        break;
                    case 6:

                        break;

                    case 8:
                        if (bluetooth != null){
                            bluetooth.StopSearchDevice();
                            Bundle b = msg.getData();
                            Log.e("sraechresult",b.getString("Address"));
                            listScan.add(b.getString("Address"));

                            comPlate();
                        }

                        break;
                }
            }


        };
    }

    private void initView() {
        bluetooth = new BlueToothManager(CapacityEquipActivity.this);
        bluetooth.RegisterBroadCastReceive();
        bluetooth.TurnOnBluetooth();
        relativeLayout_none = (RelativeLayout) findViewById(R.id.relative_capactiy_none);
        linearLayout_equip = (LinearLayout) findViewById(R.id.linerlayout_equip);
        imageView_add = (ImageView) findViewById(R.id.image_capacity_add);
        imageView_add.setOnClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview_capacity_equip);
        imageView_quit = (ImageView) findViewById(R.id.image_quitcapacity);
        imageView_quit.setOnClickListener(this);
        list = new ArrayList<>();
        listScan = new ArrayList<>();
    }

    private void initLayout() {
            if (list.size() != 0 || AppCons.orderBean != null){
            Log.e("layout","show");
            relativeLayout_none.setVisibility(View.GONE);
            linearLayout_equip.setVisibility(View.VISIBLE);
            if (equipAdapter == null){
                Log.e("layout","create");
                equipAdapter = new CapacityEquipAdapter(list,this);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(equipAdapter);
                equipAdapter.setOnItemClickListener(new CapacityEquipAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemclick(View view, BlueOrderBean bean) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("bean",bean);
                        startActivity(new Intent(CapacityEquipActivity.this,BlueOrderActivity.class).putExtras(bundle));
                    }
                });
            }else {
                Log.e("layout","refush");
                equipAdapter.notifyDataSetChanged();
            }

        }else{
                Log.e("layout","display");
                relativeLayout_none.setVisibility(View.VISIBLE);
                linearLayout_equip.setVisibility(View.GONE);
            }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFirst){
            sacnBlue();
            isFirst = false;
        }else {
            if (AppCons.orderBean != null){
                initLayout();
                blueOrderBean = AppCons.orderBean;
                if (blueOrderBean != null && list.size() > 0){
                    for (int i = 0 ;i < list.size(); i ++){
                        if (blueOrderBean.getAddress().equals(list.get(i).getAddress())){
                            isExit = true;
                            break;
                        }
                    }
                    if (!isExit){
                        list.add(0,blueOrderBean);
                        equipAdapter.notifyDataSetChanged();
                    }
                }else if (list.size() == 0){
                    list.add(0,blueOrderBean);
                    equipAdapter.notifyDataSetChanged();
                }
                AppCons.orderBean = null;
            }

        }

        Log.e("onresume","caresume");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_capacity_add:
                startActivity(new Intent(CapacityEquipActivity.this,MatchActivity.class));
                break;
            case R.id.image_quitcapacity:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveList();
    }

    private void saveList(){
        for (int i = 0; i < list.size(); i ++){
            list.get(i).setStatus(false);
        }
        SharedPreferences sp = this.getSharedPreferences(AppCons.AGENTID_SAVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        try {
            edit.putString("equiplist", SaveList.SceneList2String(list));
            edit.commit();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void sacnBlue(){

        if (bluetooth != null){
            bluetooth.StartSearchDevice();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isExit = false;
        bluetooth.UnRegisterBroadCastReceive();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        bluetooth = null;
    }
    private void comPlate() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sp = getSharedPreferences(AppCons.AGENTID_SAVE, Context.MODE_PRIVATE);
                String liststr = sp.getString("equiplist", "");
                if (!TextUtils.isEmpty(liststr)) {
                    try {
                        list = SaveList.String2SceneList(liststr);
                        Log.e("length",list.size() + "," + listScan.size());
                        if (list.size() > 0 && listScan.size() > 0){
                            for (int i = 0 ; i < listScan.size(); i ++){
                                for (int j = 0; j < list.size() ; j ++){
                                    Log.e("getSave",list.get(j).getAddress());
                                    Log.e("scan",listScan.get(i).toString());
                                    if (listScan.get(i).equals(list.get(j).getAddress())){
                                        list.get(j).setStatus(true);
                                        Log.e("getSave",list.get(j).getAddress());
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (list != null){

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initLayout();
                        }
                    });
                }
            }

        }).start();
    }
}
