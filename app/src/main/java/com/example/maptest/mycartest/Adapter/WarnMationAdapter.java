package com.example.maptest.mycartest.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.UI.warn.WarnListBean;


import java.util.List;

/**
 * Created by ${Author} on 2017/3/28.
 * Use to   报警信息适配器
 */

public class WarnMationAdapter extends RecyclerView.Adapter<WarnMationAdapter.ViewHolder>
        implements View.OnClickListener, View.OnLongClickListener {
    //数据源
    private List<WarnListBean> list;
    private Context context;
    //是否显示单选框,默认false
    private boolean isshowBox = false;
    //接口实例
    private RecyclerViewOnItemClickListener onItemClickListener;

    public WarnMationAdapter(List<WarnListBean> list, Context context) {
        this.list = list;
        this.context = context;

    }



    //视图管理
    public class ViewHolder extends RecyclerView.ViewHolder {
        private View root;
        private TextView texttime;
        private TextView textView_info;
        private TextView textView_addr;
        private ImageView imageView_new;
        private TextView textView_itemname;
        private TextView textView_speed;
        public ViewHolder(View root) {
            super(root);
            this.root = root;
            texttime = (TextView) root.findViewById(R.id.item_texttime);
            textView_info = (TextView) root.findViewById(R.id.text_iteminfo);
            textView_addr = (TextView) root.findViewById(R.id.text_itemaddr);
            imageView_new = (ImageView) root.findViewById(R.id.image_itemnew);
            textView_itemname = (TextView) root.findViewById(R.id.text_itemname);
            textView_speed = (TextView) root.findViewById(R.id.text_speed);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    //绑定视图管理者
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.texttime.setText(list.get(position).getExpireTime());
        holder.textView_itemname.setText("设备:"+list.get(position).getTerminalID());
        holder.textView_addr.setText("地址:"+list.get(position).getAlarmAddress());
        holder.textView_speed.setText(list.get(position).getSpeed() + "km/h");
        if (!list.get(position).isRead()){     //未读
            holder.imageView_new.setImageResource(R.drawable.icon_baojing_weiduxiaoxi);
        }else {
            holder.imageView_new.setImageResource(R.drawable.icon_weiduxiaoxi_selected);
        }
        switch (list.get(position).getAlarmState()){
            case 0:
                holder.textView_info.setText("正常报警");
                break;
            case 1:
                holder.textView_info.setText("求救报警(SOS)");
                break;
            case 2:
                holder.textView_info.setText("断电报警");
                break;
            case 3:
                holder.textView_info.setText("震动报警");
                break;
            case 4:
                holder.textView_info.setText("围栏报警(进)");
                break;
            case 5:
                holder.textView_info.setText("围栏报警(出)");
                break;
            case 6:
                holder.textView_info.setText("超速报警");
                break;
            case 7:
                holder.textView_info.setText("拆除报警");
                break;
            case 9:
                holder.textView_info.setText("位移报警");
                break;
            case 10:
                holder.textView_info.setText("盲区报警(进)");
                break;
            case 11:
                holder.textView_info.setText("盲区报警(出)");
                break;
            case 12:
                holder.textView_info.setText("开机报警");
                break;
            case 14:
                holder.textView_info.setText("低电报警(外电)");
                break;
            case 15:
                holder.textView_info.setText("低电报警(外电)");
                break;
            case 18:
                holder.textView_info.setText("低电报警(电池)");
                break;
            case 19:
                holder.textView_info.setText("拆卸报警");
                break;
            case 20:
                holder.textView_info.setText("离线报警");
                break;
            case 21:
                holder.textView_info.setText("胎压漏气");
                break;
            case 22:
                holder.textView_info.setText("轮胎异常");
                break;
            case 23:
                holder.textView_info.setText("围栏报警");
                break;
            case 40:
                holder.textView_info.setText("急减速");
                break;
            case 41:
                holder.textView_info.setText("急加速");
                break;
            case 44:
                holder.textView_info.setText("碰撞报警");
                break;
            case 45:
                holder.textView_info.setText("翻转报警");
                break;
            case 46:
                holder.textView_info.setText("急转弯");
            case 48:
                holder.textView_info.setText("GSM干扰报警");
                break;
            default:
                holder.textView_info.setText("正常报警");
                break;
        }


        holder.root.setTag(position);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_warninfo, parent, false);
        ViewHolder vh = new ViewHolder(root);
        //为Item设置点击事件
        root.setOnClickListener(this);
        root.setOnLongClickListener(this);
        return vh;
    }

    //点击事件
    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            onItemClickListener.onItemClickListener(v, (Integer) v.getTag());
        }
    }

    //长按事件
    @Override
    public boolean onLongClick(View v) {
        return onItemClickListener != null && onItemClickListener.onItemLongClickListener(v, (Integer) v.getTag());
    }

    //设置点击事件
    public void setRecyclerViewOnItemClickListener(RecyclerViewOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //接口回调设置点击事件
    public interface RecyclerViewOnItemClickListener {
        //点击事件
        void onItemClickListener(View view, int position);

        //长按事件
        boolean onItemLongClickListener(View view, int position);
    }
}

