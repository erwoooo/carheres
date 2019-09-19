package com.example.maptest.mycartest.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.UI.SetUi.service.Command;
import com.example.maptest.mycartest.Utils.DateChangeUtil;
import com.example.maptest.mycartest.Utils.UtcDateChang;

import java.util.List;

/**
 * Created by ${Author} on 2017/3/28.
 * Use to   查询指令适配器
 */

public class QueOrderAdapter extends RecyclerView.Adapter<QueOrderAdapter.ViewHolder>
        implements View.OnClickListener, View.OnLongClickListener {
    //数据源
    private List<Command> list;
    private Context context;
    //是否显示单选框,默认false
    private boolean isshowBox = false;
    //接口实例
    private RecyclerViewOnItemClickListener onItemClickListener;

    public QueOrderAdapter(List<Command> list, Context context) {
        this.list = list;
        this.context = context;

    }



    //视图管理
    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout title;
        private View root;
        private TextView textView_id;
        private TextView textView_type;
        private TextView textView_states;
        private TextView textView_return;
        private TextView textView_send;
        private TextView textView_answer;

        public ViewHolder(View root) {
            super(root);
            this.root = root;
            textView_id = (TextView) root.findViewById(R.id.item_quryno);
            textView_type = (TextView) root.findViewById(R.id.text_itemtype);
            textView_states = (TextView) root.findViewById(R.id.text_itemstate);
            textView_return = (TextView) root.findViewById(R.id.text_itemanser);
            textView_send = (TextView) root.findViewById(R.id.text_itemsendtime);
            textView_answer = (TextView) root.findViewById(R.id.text_itemansertime);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    //绑定视图管理者
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.textView_id.setText("编号"+(position + 1));
        holder.textView_type.setText(list.get(position).getTerminalID());
        holder.textView_states.setText(list.get(position).getContent());
        holder.textView_return.setText(list.get(position).getBackContent());
        holder.textView_answer.setText(UtcDateChang.UtcDatetoLocaTime(list.get(position).getExcuteTime()));
        holder.textView_send.setText(UtcDateChang.UtcDatetoLocaTime(list.get(position).getIssTime()));
        holder.root.setTag(position);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_qurry_order, parent, false);
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

