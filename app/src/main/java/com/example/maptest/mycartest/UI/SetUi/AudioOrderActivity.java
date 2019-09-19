package com.example.maptest.mycartest.UI.SetUi;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.maptest.mycartest.Adapter.AudioListAdapter;
import com.example.maptest.mycartest.Adapter.AudioWindowAdapter;
import com.example.maptest.mycartest.Bean.CommandResponse;
import com.example.maptest.mycartest.New.NewHttpUtils;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.UI.SetUi.service.AudioMenuBean;
import com.example.maptest.mycartest.UI.SetUi.service.AvideoBean;
import com.example.maptest.mycartest.Utils.AppCons;
import com.example.maptest.mycartest.Utils.BaseActivity;
import com.example.maptest.mycartest.Utils.DownloadUtil;
import com.example.maptest.mycartest.Utils.http.ResponseCallback;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.maptest.mycartest.UI.SetUi.RooaLixianActivity.dip2px;
import static com.example.maptest.mycartest.Utils.AppCons.integerList;
import static com.example.maptest.mycartest.Utils.AppCons.locationListBean;


/**
 * Created by ${Author} on 2018/7/10.
 * Use to
 */

public class AudioOrderActivity extends BaseActivity implements View.OnClickListener{
    private ImageView image_audio_delete,image_quit_audio;
    private AudioListAdapter audioListAdapter;
    private AudioWindowAdapter audioWindowAdapter;
    private RecyclerView recycle_audio,audio_layout_rec;
    private LinearLayout audio_liner_select;
    private RelativeLayout audio_buttom_liner;
    private Button audio_button_send;
    boolean isDeleteMode = false;
    private PopupWindow window;
    private List<AudioMenuBean>listWindow;
    private TextView text_audio_cancel,text_audio_ensure;
    List<AvideoBean>avideoBeanList = new ArrayList<>();
    List<String>listName = new ArrayList<>();
    List<String>listId = new ArrayList<>();
    private TextView text_audio_clear,text_audio_del,text_audio_diss;
    private String command = null;
    private JSONObject jsonObject;
    private CommandResponse commandResponse;
    Map<Object,Object>map = new HashMap<>();
    MediaPlayer mediaPlayer;
    private int oldPosition = -1,oldMenuPosition = -1;
    private boolean isDw = false;
    String audioUrl = null;
    private AvideoBean myAvideoBean;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    initAdapter();
                    break;
                case 1:
                    int position = (int) msg.obj;
                    isReadMedia(position);
                    avideoBeanList.get(position).setIsRead(true);
                    audioListAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    if (avideoBeanList != null && avideoBeanList.size() != 0){
                        for (int i = 0 ; i < avideoBeanList.size() ; i++){
                            avideoBeanList.get(i).setSelect(false);
                        }
                    }
                    Log.e("avideoBeanList",avideoBeanList.size() + "");
                    integerList.clear();
                    audioListAdapter.isShow = false;
                    audioListAdapter.notifyDataSetChanged();
                    audioListAdapter.isShow = true;
                    audioListAdapter.notifyDataSetChanged();

                    break;
                case 3:
                    int menuPos = (int) msg.obj;
                    if (oldMenuPosition != -1 && oldMenuPosition != menuPos){
                        listWindow.get(oldMenuPosition).setMenuSelect(false);
                    }
                    oldMenuPosition = menuPos;
                    listWindow.get(menuPos).setMenuSelect(true);
                    audioWindowAdapter.notifyDataSetChanged();
                    break;
                case 4:
                    int pos = (int) msg.obj;
                    if (oldPosition != -1 && oldPosition != pos){
                        avideoBeanList.get(oldPosition).setClick(false);
                    }
                    oldPosition = pos;
                    avideoBeanList.get(pos).setClick(true);
                    audioListAdapter.notifyDataSetChanged();
                    break;
                case 11:
                    String url = Environment.getExternalStorageDirectory().getPath() + "/download/" + myAvideoBean.getFileName() +".amr" ;
                    Log.e("url",url);
                        if (mediaPlayer == null) {
                            mediaPlayer = new MediaPlayer();
                        }
                        if (mediaPlayer.isPlaying()){
                            mediaPlayer.stop();
                            mediaPlayer.reset();
                        }
                        try {
                            mediaPlayer.setDataSource(url);
                            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    audioListAdapter.setImageResouse();
                                    mediaPlayer.reset();
                                    Toast.makeText(getApplicationContext(), "播放完成", Toast.LENGTH_SHORT).show();
                                }
                            });
                    } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        initView();
    }

    private void initView() {
        image_quit_audio = (ImageView) findViewById(R.id.image_quit_audio);
        image_audio_delete = (ImageView) findViewById(R.id.image_audio_delete);
        audio_button_send = (Button) findViewById(R.id.audio_button_send);
        audio_liner_select = (LinearLayout) findViewById(R.id.audio_liner_select);
        audio_buttom_liner = (RelativeLayout) findViewById(R.id.audio_buttom_liner);
        text_audio_clear = (TextView) findViewById(R.id.text_audio_clear);
        text_audio_del = (TextView) findViewById(R.id.text_audio_del);
        text_audio_diss = (TextView) findViewById(R.id.text_audio_diss);
        text_audio_clear.setOnClickListener(this);
        text_audio_diss.setOnClickListener(this);
        text_audio_del.setOnClickListener(this);
        audio_button_send.setOnClickListener(this);
        image_quit_audio.setOnClickListener(this);
        image_audio_delete.setOnClickListener(this);
        recycle_audio = (RecyclerView) findViewById(R.id.recycle_audio);
        recycle_audio.setLayoutManager(new LinearLayoutManager(this));
        recycle_audio.setHasFixedSize(false);
        recycle_audio.setItemAnimator(new DefaultItemAnimator());

        listWindow = new ArrayList<>();
        if(locationListBean.getDevice().getDeviceType().equals("Q6H")){
            listWindow.add(new AudioMenuBean("关闭持续录音",false));
            listWindow.add(new AudioMenuBean("10秒",false));
            listWindow.add(new AudioMenuBean("30秒",false));
            listWindow.add(new AudioMenuBean("60秒",false));
        }else{
            listWindow.add(new AudioMenuBean("打开持续录音",false));
            listWindow.add(new AudioMenuBean("关闭持续录音",false));
            listWindow.add(new AudioMenuBean("30秒",false));
            listWindow.add(new AudioMenuBean("1分钟",false));
            listWindow.add(new AudioMenuBean("2分钟",false));
            listWindow.add(new AudioMenuBean("3分钟",false));
            listWindow.add(new AudioMenuBean("4分钟",false));
            listWindow.add(new AudioMenuBean("5分钟",false));
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_quit_audio:
                quitAudio();
                break;
            case R.id.image_audio_delete:
                if (mediaPlayer != null  ){
                    if (mediaPlayer.isPlaying()){
                        mediaPlayer.stop();
                    }
                    audioListAdapter.setImageResouse();
                }
                enterDelete();
                break;
            case R.id.audio_button_send:
                if (mediaPlayer != null ){
                    if ( mediaPlayer.isPlaying()){
                        mediaPlayer.stop();
                    }
                    audioListAdapter.setImageResouse();
                }
                showPopwindow();
                audio_buttom_liner.setVisibility(View.GONE);
                break;
            case R.id.text_audio_cancel:
                if (window != null){
                    window.dismiss();
                }
                break;
            case R.id.text_audio_ensure:
                if (command != null){
                    sendCommand(command);
                    if (window != null){
                        window.dismiss();
                    }
                }else {
                    Toast.makeText(AudioOrderActivity.this,"未选择时间",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.text_audio_clear:
                deleAllAudio();
                break;
            case R.id.text_audio_del:
                deleSelectAudio();
                break;
            case R.id.text_audio_diss:
                audio_button_send.setVisibility(View.VISIBLE);
                audio_liner_select.setVisibility(View.GONE);
                isDeleteMode = false;
                audioListAdapter.isShow = false;
                audioListAdapter.notifyDataSetChanged();
                break;
        }
    }
    private void deleSelectAudio() {
        listId.clear();
        listName.clear();
        if (integerList.size() != 0){
            for (AvideoBean position : integerList){
                listName.add(position.getFileName());
                listId.add(position.getId());
                avideoBeanList.remove(position);
            }
            deleAudio(listId);
            deleMedio(listName);
        }


    }
    @Override
    protected void onResume() {
        super.onResume();
        map.put("terminalID", AppCons.locationListBean.getTerminalID());
        Gson gson = new Gson();
        String obj = gson.toJson(map);
        NewHttpUtils.querryAudio(obj, new ResponseCallback() {
            @Override
            public void TaskCallBack(Object object) {
                avideoBeanList = (List<AvideoBean>) object;
                Message message = new Message();
                message.what = 0;
                handler.sendMessage(message);
            }
            @Override
            public void FailCallBack(Object object) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null){
            if (mediaPlayer.isPlaying()){
                mediaPlayer.stop();
                mediaPlayer.reset();
            }

        }
    }

    /**
     * @param commId
     * 发送指令
     */
    private void sendCommand(String commId) {
        jsonObject= new JSONObject();
        jsonObject.put("terminalID", AppCons.locationListBean.getTerminalID());              //设备的IEMI号
        jsonObject.put("deviceProtocol", AppCons.locationListBean.getLocation().getDeviceProtocol());      //设备协议号
        jsonObject.put("content", commId);
        Log.e("(jsonObject",jsonObject.toJSONString());
        NewHttpUtils.sendOrder(jsonObject.toJSONString(), AudioOrderActivity.this, new ResponseCallback() {
            @Override
            public void TaskCallBack(Object object) {
                commandResponse = (CommandResponse) object;
                String data = commandResponse.getContent();
                if (data != null){
                    if (data.contains("OK") || data.contains("Free")  || data.contains("ok") || data.contains("Success") || data.contains("successfully") || data.contains("Already")) {
                        Toast.makeText(getApplicationContext(), "设置成功", Toast.LENGTH_SHORT).show();
//                        postOrder();        //指令收到回应成功后，再保存指令到服务器
                    } else if (data.contains("timeout")) {
                        Toast.makeText(getApplicationContext(), "请求超时", Toast.LENGTH_SHORT).show();

                    }else if(data.contains("Recording")){
                        Toast.makeText(getApplicationContext(), "已经开始录音", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "设置失败", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(getApplicationContext(), "设置失败", Toast.LENGTH_SHORT).show();
                }
                commandResponse = null;
            }

            @Override
            public void FailCallBack(Object object) {
                Toast.makeText(getApplicationContext(), "设置失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 进入删除模式
     */
    private void enterDelete() {
        if (isDeleteMode){
            audio_button_send.setVisibility(View.VISIBLE);
            audio_liner_select.setVisibility(View.GONE);
            isDeleteMode = false;
            audioListAdapter.isShow = false;
            audioListAdapter.notifyDataSetChanged();
            integerList.clear();
        }else {
            audio_button_send.setVisibility(View.GONE);
            audio_liner_select.setVisibility(View.VISIBLE);
            isDeleteMode = true;
            audioListAdapter.isShow = true;
            audioListAdapter.notifyDataSetChanged();

        }
    }


    /**
     * 显示发送指令
     */
    private void showPopwindow() {
        if (window == null){
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.audio_time_layout, null);
            window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
                    dip2px(this,260));
            window.setFocusable(true);
            ColorDrawable dw = new ColorDrawable(getResources().getColor(R.color.bgunpres));
            window.setBackgroundDrawable(dw);
            window.setAnimationStyle(R.style.mypopwindow_anim_style);
            window.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    audio_buttom_liner.setVisibility(View.VISIBLE);
                    command = null;
                }
            });
            window.showAtLocation(AudioOrderActivity.this.findViewById(R.id.recycle_audio), Gravity.BOTTOM, 0, 0);
            audio_layout_rec = (RecyclerView) view.findViewById(R.id.audio_layout_rec);
            text_audio_cancel = (TextView) view.findViewById(R.id.text_audio_cancel);
            text_audio_ensure = (TextView) view.findViewById(R.id.text_audio_ensure);
            audioWindowAdapter = new AudioWindowAdapter(listWindow,AudioOrderActivity.this);
            audio_layout_rec.setHasFixedSize(false);
            audio_layout_rec.setItemAnimator(new DefaultItemAnimator());
            audio_layout_rec.setLayoutManager(new LinearLayoutManager(this));
            audio_layout_rec.setAdapter(audioWindowAdapter);
            audioWindowAdapter.setOnItemClickListener(new AudioWindowAdapter.OnRecycleItemClickListener() {
                @Override
                public void onItemclick(View view, int position) {
                    Message message = new Message();
                    message.what = 3;
                    message.obj = position;
                    handler.sendMessage(message);
                    if(locationListBean.getDevice().getDeviceType().equals("Q6H")){
                        switch (position) {
                            case 0:
                                command = "Ly,0#";
                                break;
                            case 1:
                                command = "Ly,10#";
                                break;
                            case 2:
                                command = "Ly,30#";
                                break;
                            case 3:
                                command = "Ly,60#";
                                break;
                        }
                    }else {
                        switch (position) {
                            case 0:
                                command = "LY,999#";
                                break;
                            case 1:
                                command = "LY,0#";
                                break;
                            case 2:
                                command = "LY,30#";
                                break;
                            case 3:
                                command = "LY,60#";
                                break;
                            case 4:
                                command = "LY,120#";
                                break;
                            case 5:
                                command = "LY,180#";
                                break;
                            case 6:
                                command = "LY,240#";
                                break;
                            case 7:
                                command = "LY,300#";
                                break;
                        }
                    }
                }
            });
            text_audio_cancel.setOnClickListener(this);
            text_audio_ensure.setOnClickListener(this);
        }else {
            window.showAtLocation(AudioOrderActivity.this.findViewById(R.id.recycle_audio), Gravity.BOTTOM, 0, 0);
        }
    }

    private void initAdapter(){
        if (audioListAdapter == null){
            audioListAdapter = new AudioListAdapter(avideoBeanList,this);
            recycle_audio.setAdapter(audioListAdapter);
            audioListAdapter.setOnItemClickListener(new AudioListAdapter.OnRecycleItemClickListener() {
                @Override
                public void onItemclick(View view, int position) {

                    if (audioListAdapter.isShow){

                    }else {
                        audioListAdapter.setImageResouse();
                        Message message1 = new Message();
                        message1.what = 4;
                        message1.obj = position;
                        handler.sendMessage(message1);
                        playMedia(avideoBeanList.get(position));
                        if (!avideoBeanList.get(position).getIsRead()){
                            Message message = new Message();
                            message.what = 1;
                            message.obj = position;
                            handler.sendMessage(message);
                        }
                    }
                }
            });
        }else {
            audioListAdapter.notifyDataSetChanged();
        }
    }

    /**
     * @param avideoBean
     * 播放一个文件
     */
    private void playMedia(final AvideoBean avideoBean) {
        myAvideoBean = avideoBean;
        DownloadUtil.get().download(avideoBean.getSource(), "download", new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess() {
                Log.e("onDownloading", "success");
                Message message = new Message();
                message.what = 11;
                handler.sendMessage(message);
            }
            @Override
            public void onDownloading(int progress) {
                Log.e("onDownloading",progress +"");
            }
            @Override
            public void onDownloadFailed() {
                Log.e("onDownloadFailed", "fail");
                Message message = new Message();
                message.what = 11;
                handler.sendMessage(message);
            }
        });
    }
    /**
     * @param position
     * 标记为已读
     */
    private void isReadMedia(int position){
        map.clear();
        map.put("id",avideoBeanList.get(position).getId());
        map.put("isRead",true);
        Gson gson = new Gson();
        String obj = gson.toJson(map);
        NewHttpUtils.readAudio(obj);
    }
    /**
     * @param listName
     * 删除文件
     */
    private void deleMedio(List<String>listName){
        map.clear();
        map.put("terminalID",AppCons.locationListBean.getTerminalID());
        map.put("fileName",listName);
        Gson gson = new Gson();
        String obj = gson.toJson(map);
        NewHttpUtils.deleteAudioOrder(obj);
    }
    /**
     * @param listId
     * 删除数据库里的记录
     */
    private void deleAudio(List<String>listId){
        Gson gson = new Gson();
        String obj = gson.toJson(listId);
        NewHttpUtils.deleteAudio(obj, new ResponseCallback() {
            @Override
            public void TaskCallBack(Object object) {
                if (object.toString().contains("200")){
                    Message message = new Message();
                    message.what = 2;
                    handler.sendMessageDelayed(message,3000);
                }
            }

            @Override
            public void FailCallBack(Object object) {

            }
        });
    }
    /**
     * 全部删除
     */
    private void deleAllAudio(){
        listId.clear();
        listName.clear();
        for (int i = 0 ; i < avideoBeanList.size() ; i ++){
                listName.add(avideoBeanList.get(i).getFileName());
                listId.add(avideoBeanList.get(i).getId());
        }
        avideoBeanList.clear();
        deleAudio(listId);
        deleMedio(listName);
    }
    private void quitAudio(){
        if (mediaPlayer != null){
            if (mediaPlayer.isPlaying()){
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
        listName.clear();
        listName = null;
        listId.clear();
        listId = null;
        listWindow.clear();
        listWindow = null;
        avideoBeanList.clear();
        avideoBeanList = null;
        finish();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            quitAudio();
        }
        return super.onKeyDown(keyCode, event);
    }

}
