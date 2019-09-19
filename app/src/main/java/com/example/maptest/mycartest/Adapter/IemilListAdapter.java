package com.example.maptest.mycartest.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maptest.mycartest.New.LocationListBean;
import com.example.maptest.mycartest.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${Author} on 2017/3/1.
 * Use to 车辆列表适配器
 */

public class IemilListAdapter extends RecyclerView.Adapter implements View.OnClickListener{
        List<LocationListBean>list;
        private Context context;

    public IemilListAdapter(List<LocationListBean> list, Context context) {
        this.list = list;
        this.context = context;
    }
    /*搜索功能*/
    public void setFilter(List<LocationListBean>lists){
        list = new ArrayList<>();
        list.addAll(lists);
        notifyDataSetChanged();
    }
    public void animateTo(List<LocationListBean>lists){
        notifyDataSetChanged();
    }
    private void applyAndAnimateRemovals(List<LocationListBean>lists){      //关键字不符合，移除的bean对象
        for (int i = list.size() -1 ;i >= 0;i ++){
            LocationListBean reccarbeans = list.get(i);
            if (! lists.contains(reccarbeans)){
                removewItem(i);
            }
        }
    }
    private void applyAndAnimateAdditions(List<LocationListBean>lists){     //有关键字符合，添加的bean对象
        for (int i = 0, count = lists.size();i< count; i++){
            LocationListBean reccarbeans = list.get(i);
            if (!list.contains(reccarbeans)){
                addItem(i,reccarbeans);
            }
        }
    }
    private void applyAndAnimateMovedItems(List<LocationListBean>lists){
        for (int toPosition = lists.size() -1 ; toPosition >= 0;toPosition--){
            LocationListBean reccarbeans = list.get(toPosition);
             int fromPosition = list.indexOf(reccarbeans);
            if (fromPosition >= 0 && fromPosition != toPosition){
                moveTtem(fromPosition,toPosition);
            }
        }
    }
    public LocationListBean removewItem(int position){
        final LocationListBean reccarbeans = list.remove(position);
        notifyItemRemoved(position);
        return reccarbeans;
    }

    public void addItem(int position, LocationListBean reccarbeans){
        list.add(position,reccarbeans);
        notifyItemInserted(position);
    }

    public void moveTtem(int fromPosition,int toPosition){
        LocationListBean reccarbeans = list.remove(fromPosition);
        list.add(toPosition,reccarbeans);
        notifyItemMoved(fromPosition,toPosition);
    }
    /*搜索功能*/


    //定义接口
    public interface OnRecyclerViewItemClickListener{
        void onItemclick(View view, LocationListBean bean);
    }
    //设置接口方法
    private  OnRecyclerViewItemClickListener mListener = null;
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener){
        this.mListener = listener;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_iemi,null);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    //数据绑定
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolder vh = (ViewHolder) holder;
            vh.getTextView_carId.setText(list.get(position).getTerminalID());

            if (list.get(position).getCarNumber().equals("")){
                vh.getGetTextView_name.setText(list.get(position).getNickname());
            }else {
                vh.getGetTextView_name.setText(list.get(position).getCarNumber());
            }
            vh.itemView.setTag(list.get(position));
    }
    @Override
    public void onClick(View v) {
        if (mListener != null){
            //使用getTag方法获取数据
            mListener.onItemclick(v, (LocationListBean) v.getTag());
        }
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder{
        TextView getTextView_carId,getGetTextView_name;
        ImageView image_item;
        public ViewHolder(View itemView) {
            super(itemView);
            getGetTextView_name = (TextView) itemView.findViewById(R.id.item_text_name);
            getTextView_carId = (TextView) itemView.findViewById(R.id.iemi_item_text);
            image_item = (ImageView) itemView.findViewById(R.id.iemi_item_image);
        }
    }
}
