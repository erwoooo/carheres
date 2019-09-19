package com.example.maptest.mycartest.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.UI.SetUi.service.AudioMenuBean;

import java.util.List;


/**
 * Created by ${Author} on 2018/7/10.
 * Use to
 */

public class AudioWindowAdapter extends RecyclerView.Adapter implements View.OnClickListener{
    private List<AudioMenuBean> list;
    private Context mContext;


    public AudioWindowAdapter(List<AudioMenuBean> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_audio_window,parent,false);
        view.setOnClickListener(this);
        return new WindowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof WindowViewHolder){
            WindowViewHolder audioViewHolder = (WindowViewHolder) holder;
            audioViewHolder.text_audio_window.setText(list.get(position).getMenuName());
            if (list.get(position).isMenuSelect()){
                audioViewHolder.text_audio_window.setTextColor(Color.parseColor("#ff9913"));
            }else {
                audioViewHolder.text_audio_window.setTextColor(Color.parseColor("#666666"));
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
        if (mListener != null){
            mListener.onItemclick(v, (int) v.getTag());
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
}
