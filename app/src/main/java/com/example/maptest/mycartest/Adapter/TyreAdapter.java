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

import java.util.List;

/**
 * Created by ${Author} on 2017/3/1.
 * Use to 胎压列表
 */

public class TyreAdapter extends RecyclerView.Adapter implements View.OnClickListener{
        List<LocationListBean>list;
        private Context context;

    public TyreAdapter(List<LocationListBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

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
        View view = LayoutInflater.from(context).inflate(R.layout.item_tyre,null);
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
        }else{
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
            textView_carName = (TextView) itemView.findViewById(R.id.item_textname_trye);
            getTextView_carId = (TextView) itemView.findViewById(R.id.item_textno_trye);
            textView_state = (TextView) itemView.findViewById(R.id.text_caron);
            imageView = (ImageView) itemView.findViewById(R.id.image_item_tyreshow);
        }
    }
}
