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
 * Use to 胎压列表
 */

public class TyreSearchAdapter extends RecyclerView.Adapter implements View.OnClickListener{
        List<LocationListBean>list;
        private Context context;

    public TyreSearchAdapter(List<LocationListBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    /*
    * 搜索功能
    * */
    public void setFilter(List<LocationListBean>lists){
        list = new ArrayList<>();
        list.addAll(lists);
        notifyDataSetChanged();
    }
    public void animateTo(List<LocationListBean>lists){

    }
    private void applyAndAnimateRemovals(List<LocationListBean>lists){
        for (int i = list.size() -1; i >= 0; i++){
            final LocationListBean dataBeans = list.get(i);
            if (! lists.contains(dataBeans)){
                removeItem(i);
            }
        }
    }
    private void applyAndAnimateAdditions(List<LocationListBean> lists) {
        for (int i = 0, count = lists.size(); i < count; i++) {
            final LocationListBean dataBeans = list.get(i);
            if (!list.contains(dataBeans)) {
                addItem(i, dataBeans);
            }
        }
    }
    private void applyAndAnimateMovedItems(List<LocationListBean> lists) {
        for (int toPosition = lists.size() - 1; toPosition >= 0; toPosition--) {
            final LocationListBean dataBeans = list.get(toPosition);
            final int fromPosition = list.indexOf(dataBeans);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }
    public LocationListBean removeItem(int position) {
        final LocationListBean dataBeans = list.remove(position);
        notifyItemRemoved(position);
        return dataBeans;
    }


    public void addItem(int position, LocationListBean dataBeans) {
        list.add(position, dataBeans);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final LocationListBean dataBeans = list.remove(fromPosition);
        list.add(toPosition, dataBeans);
        notifyItemMoved(fromPosition, toPosition);
    }
    //*******************************************

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
        View view = LayoutInflater.from(context).inflate(R.layout.item_rec,null);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    //数据绑定
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder vh = (ViewHolder) holder;
        if (list.get(position).getCarNumber().equals("")){
            vh.textView_carName.setText(list.get(position).getNickname());
        }else {
            vh.textView_carName.setText(list.get(position).getCarNumber());
        }
        vh.getTextView_carId.setText(list.get(position).getTerminalID());

        if (list.get(position).getState() == 1){
            vh.textView_state.setText("在线");
            vh.imageView.setImageResource(R.drawable.icon_cllb_jinzhi);
        }else {
            vh.imageView.setImageResource(R.drawable.icon_cllb_lixian);
            vh.textView_state.setText("离线");
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
        TextView textView_carName,getTextView_carId;
        TextView textView_state;
        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            textView_carName = (TextView) itemView.findViewById(R.id.text_recname);
            getTextView_carId = (TextView) itemView.findViewById(R.id.text_recid);
            textView_state = (TextView) itemView.findViewById(R.id.text_search);
            imageView = (ImageView) itemView.findViewById(R.id.image_item_recsss);
        }
    }
}
