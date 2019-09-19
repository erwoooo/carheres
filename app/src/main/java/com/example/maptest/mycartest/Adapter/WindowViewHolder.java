package com.example.maptest.mycartest.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.maptest.mycartest.R;


/**
 * Created by ${Author} on 2018/7/12.
 * Use to
 */

public class WindowViewHolder extends RecyclerView.ViewHolder {
    public TextView text_audio_window;
    public WindowViewHolder(View itemView) {
        super(itemView);
        text_audio_window = (TextView) itemView.findViewById(R.id.text_audio_window);
    }
}
