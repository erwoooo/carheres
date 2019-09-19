package com.example.maptest.mycartest.Adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.UI.SetUi.service.AvideoBean;
import com.example.maptest.mycartest.Utils.UtcDateChang;

import java.util.List;

import static com.example.maptest.mycartest.Utils.AppCons.integerList;


/**
 * Created by ${Author} on 2018/7/10.
 * Use to
 */

public class AudioListAdapter extends RecyclerView.Adapter implements View.OnClickListener{
    private List<AvideoBean> list;
    private Context mContext;
    public boolean isShow = false;
    AudioViewHolder audioViewHolder;
    AnimationDrawable animationDrawable;
    private int click_position = -1;
    private ImageView oldImageView;
    public AudioListAdapter(List<AvideoBean> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_audio_list,parent,false);
        view.setOnClickListener(this);
        return new AudioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof AudioViewHolder){
            audioViewHolder = (AudioViewHolder) holder;
            audioViewHolder.textViewtime.setText(UtcDateChang.UtcDatetoLocaTime(list.get(position).getUtcTime()));
            if (list.get(position).getIsRead()){
                audioViewHolder.image_red_audio.setVisibility(View.GONE);
            }else {
                audioViewHolder.image_red_audio.setVisibility(View.VISIBLE);
            }
            if (isShow){
                audioViewHolder.audio_delete_check.setVisibility(View.VISIBLE);
            }else {
                list.get(position).setSelect(false);
                audioViewHolder.audio_delete_check.setChecked(false);
                audioViewHolder.audio_delete_check.setVisibility(View.GONE);
            }
            audioViewHolder.audio_delete_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        list.get(position).setSelect(true);
                        Log.e("当前点击的位置",position + "");
                        integerList.add(list.get(position));
                    }else {
                        list.get(position).setSelect(false);
                        if (integerList.contains(list.get(position))){
                            integerList.remove(list.get(position));
                        }
                    }
                }
            });
            if (list.get(position).isClick()){
                audioViewHolder.audio_play_img.setImageResource(R.drawable.anim_audio_play);
                animationDrawable = (AnimationDrawable) ((ImageView) audioViewHolder.audio_play_img).getDrawable();
                animationDrawable.start();
            }else {
                audioViewHolder.audio_play_img.setImageResource(R.drawable.voice_icon_3);
            }

            audioViewHolder.itemView.setTag(position);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View v) {
        ImageView imageView = (ImageView) v.findViewById(R.id.audio_play_img);
        if (mListener != null){
            mListener.onItemclick(v, (int) v.getTag());
            if (click_position != -1 && click_position != (int)v.getTag()){
                oldImageView.setImageResource(R.drawable.voice_icon_3);
            }
            oldImageView = imageView;
            click_position = (int) v.getTag();
        }
    }

    //定义接口
    public interface OnRecycleItemClickListener{
        void onItemclick(View view, int position);
    }
    //设置接口方法
    private  OnRecycleItemClickListener mListener = null;
    public void setOnItemClickListener(OnRecycleItemClickListener listener){
        this.mListener = listener;
    }

    public void setImageResouse(){
        if (animationDrawable != null){
            animationDrawable.stop();
        }
        audioViewHolder.audio_play_img.setImageResource(R.drawable.voice_icon_3);
    }

}
