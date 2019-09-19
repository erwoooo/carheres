package com.example.maptest.mycartest.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.maptest.mycartest.Bean.SpineBean;
import com.example.maptest.mycartest.R;

import java.util.List;

/**
 * Created by ${Author} on 2017/3/30.
 * Use to 测试Spiner适配器
 */

public class SpinnerAdapter extends BaseAdapter {
    private List<SpineBean> list;
    private Context context;

    public SpinnerAdapter(List<SpineBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(R.layout.item_spinner,null);
        if (convertView !=null){
            TextView textView = (TextView) convertView.findViewById(R.id.text_sp);
            textView.setText(list.get(position).getName());
        }
        return convertView;
    }
}
