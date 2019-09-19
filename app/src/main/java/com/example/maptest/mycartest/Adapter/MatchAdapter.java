package com.example.maptest.mycartest.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maptest.mycartest.R;

import java.util.List;

/**
 * Created by ${Author} on 2017/3/1.
 * Use to 胎压列表
 */

public class MatchAdapter extends RecyclerView.Adapter implements View.OnClickListener{
        List<String>list;
        private Context context;
        private int checkPosition = -1;
    public MatchAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    //定义接口
    public interface OnRecyclerViewItemClickListener{
        void onItemclick(View view, String bean);
    }
    //设置接口方法
    private  OnRecyclerViewItemClickListener mListener = null;
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener){
        this.mListener = listener;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_equip,null);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    //数据绑定
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder vh = (ViewHolder) holder;
        vh.getTextView_carId.setText(list.get(position).toString());
        vh.itemView.setTag(list.get(position));
        if (position == checkPosition){
            vh.imageView_select.setBackgroundResource(R.drawable.btn_znsb_xz);
        }else {
            vh.imageView_select.setBackgroundResource(R.drawable.btn_znsb_wxz);
        }
    }
    
    @Override
    public void onClick(View v) {
        if (mListener != null){
            //使用getTag方法获取数据
            mListener.onItemclick(v, (String) v.getTag());
            checkPosition = list.indexOf(v.getTag());
            Log.e("position",checkPosition +"");
        }
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder{
        TextView getTextView_carId;
        ImageView imageView_select;
        public ViewHolder(View itemView) {
            super(itemView);
            getTextView_carId = (TextView) itemView.findViewById(R.id.item_text_mac);
            imageView_select = (ImageView) itemView.findViewById(R.id.item_image_select);
        }
    }
}
