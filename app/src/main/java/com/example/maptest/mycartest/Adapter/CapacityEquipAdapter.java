package com.example.maptest.mycartest.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.maptest.mycartest.Bean.ABtbean;
import com.example.maptest.mycartest.Bean.AMatchBean;
import com.example.maptest.mycartest.Bean.BlueOrderBean;
import com.example.maptest.mycartest.R;

import java.util.List;

/**
 * Created by ${Author} on 2017/3/1.
 * Use to 胎压列表
 */

public class CapacityEquipAdapter extends RecyclerView.Adapter implements View.OnClickListener{
        List<BlueOrderBean>list;
        private Context context;

    public CapacityEquipAdapter(List<BlueOrderBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    //定义接口
    public interface OnRecyclerViewItemClickListener{
        void onItemclick(View view, BlueOrderBean bean);
    }
    //设置接口方法
    private  OnRecyclerViewItemClickListener mListener = null;
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener){
        this.mListener = listener;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_capacity_equip,null);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    //数据绑定
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder vh = (ViewHolder) holder;

        vh.getTextView_carId.setText(list.get(position).getAddress());
        Log.e("show",list.get(position).getAddress());
        if (list.get(position).isStatus()){
            vh.getTextView_statues.setText("设备在线");
            vh.getTextView_statues.setTextColor(Color.parseColor("#ff9900"));
        }else {
            vh.getTextView_statues.setTextColor(Color.parseColor("#666666"));
            vh.getTextView_statues.setText("设备离线");
        }
        vh.itemView.setTag(list.get(position));
    }
    
    @Override
    public void onClick(View v) {
        if (mListener != null){
            //使用getTag方法获取数据
            mListener.onItemclick(v, (BlueOrderBean) v.getTag());
        }
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView_carName,getTextView_carId,getTextView_statues;
        public ViewHolder(View itemView) {
            super(itemView);
            textView_carName = (TextView) itemView.findViewById(R.id.item_capacity_name);
            getTextView_carId = (TextView) itemView.findViewById(R.id.item_capacity_mac);
            getTextView_statues = (TextView) itemView.findViewById(R.id.item_capacity_statue);
        }
    }
}
