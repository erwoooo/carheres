package com.example.maptest.mycartest.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maptest.mycartest.R;


/**
 * Created by ${Author} on 2018/7/10.
 * Use to
 */

public class AudioViewHolder extends RecyclerView.ViewHolder {
    public TextView textViewtime;
    public ImageView image_play_audio,image_red_audio,audio_play_img;
    public CheckBox audio_delete_check;
    public AudioViewHolder(View itemView) {
        super(itemView);
        textViewtime = (TextView) itemView.findViewById(R.id.text_audio_time);
        image_red_audio = (ImageView) itemView.findViewById(R.id.image_red_audio);
        image_play_audio = (ImageView) itemView.findViewById(R.id.image_play_audio);
        audio_delete_check = (CheckBox) itemView.findViewById(R.id.audio_delete_check);
        audio_play_img = (ImageView) itemView.findViewById(R.id.audio_play_img);
    }
}
