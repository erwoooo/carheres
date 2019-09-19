package com.example.maptest.mycartest.Adapter;

import android.content.Context;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.maptest.mycartest.New.LocationListBean;
import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.Utils.DateChangeUtil;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;




/**
 * Created by ${Author} on 2017/3/1.
 * Use to 车辆列表适配器
 */

public class CarlistAdapter extends RecyclerView.Adapter implements View.OnClickListener{
        List<LocationListBean>list;
        private Context context;

    public CarlistAdapter(List<LocationListBean> list, Context context) {
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
            final LocationListBean reccarbeans = list.get(i);
            if (! lists.contains(reccarbeans)){
                removewItem(i);
            }
        }
    }
    private void applyAndAnimateAdditions(List<LocationListBean>lists){     //有关键字符合，添加的bean对象
        for (int i = 0, count = lists.size();i< count; i++){
            final LocationListBean reccarbeans = list.get(i);
            if (!list.contains(reccarbeans)){
                addItem(i,reccarbeans);
            }
        }
    }
    private void applyAndAnimateMovedItems(List<LocationListBean>lists){
        for (int toPosition = lists.size() -1 ; toPosition >= 0;toPosition--){
            final LocationListBean reccarbeans = list.get(toPosition);
            final int fromPosition = list.indexOf(reccarbeans);
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
        final LocationListBean reccarbeans = list.remove(fromPosition);
        list.add(toPosition,reccarbeans);
        notifyItemMoved(fromPosition,toPosition);
    }
    /*搜索功能*/


    //定义接口
    public interface OnRecyclerViewItemClickListener{
        void onItemclick(View view,LocationListBean bean);
    }
    //设置接口方法
    private  OnRecyclerViewItemClickListener mListener = null;
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener){
        this.mListener = listener;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_allcars,null);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    //数据绑定
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder vh = (ViewHolder) holder;
            if (list.get(position).getCarNumber().isEmpty()){
                vh.textView_carName.setText(list.get(position).getNickname().isEmpty()?list.get(position).getUsername():list.get(position).getNickname());
            }else {
                vh.textView_carName.setText(list.get(position).getCarNumber());
            }

            switch (list.get(position).getState()){
                case 1:     //在线
                    if (list.get(position).getLocation() == null || (list.get(position).getLocation().getBdLat() == 0) && list.get(position).getLocation().getBdLon() == 0){
                        vh.textViewSate.setText("在线");
                        vh.image_item.setImageResource(R.drawable.icon_cllb_jinzhi);
                    }else {
                        Date aDate = DateChangeUtil.StrToDate(DateChangeUtil.timedateTotim(list.get(position).getLocation().getUtcTime()));    /*定位时间    */
                        Date bDate = DateChangeUtil.StrToDate(DateChangeUtil.timedateTotim(list.get(position).getLocation().getStateTime()));     /*通讯时间    */
                        long dates = (bDate.getTime() - aDate.getTime());
                        if (DateChangeUtil.getSec(dates) < 60){
                            if (list.get(position).getLocation().getSpeed() <= 10) {
                                vh.textViewSate.setText("静止");
                                vh.image_item.setImageResource(R.drawable.icon_cllb_jinzhi);
                            }else {
                                vh.textViewSate.setText("行驶");
                                vh.image_item.setImageResource(R.drawable.icon_cllb_xingshi);
                            }
                        }else {
                            vh.textViewSate.setText("静止");
                            vh.image_item.setImageResource(R.drawable.icon_cllb_jinzhi);
                        }
                    }


                    break;
                case 2:
                    vh.image_item.setImageResource(R.drawable.icon_cllb_lixian);
                    vh.textViewSate.setText("离线");
                    break;
                case 3:     //未激活
                    vh.image_item.setImageResource(R.drawable.icon_cllb_guoqi);
                    vh.textViewSate.setText("未激活");
                    break;
                case 4:     //设备到期
                    vh.image_item.setImageResource(R.drawable.icon_cllb_lixian);
                    vh.textViewSate.setText("离线");
                    break;
            }
            vh.getTextView_carId.setText(list.get(position).getTerminalID());
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
        TextView textViewSate;
        ImageView image_item;
        public ViewHolder(View itemView) {
            super(itemView);
            textView_carName = (TextView) itemView.findViewById(R.id.text_carname);
            getTextView_carId = (TextView) itemView.findViewById(R.id.text_carid);
            textViewSate = (TextView) itemView.findViewById(R.id.text_carstate);
            image_item = (ImageView) itemView.findViewById(R.id.image_item);
        }
    }
}
