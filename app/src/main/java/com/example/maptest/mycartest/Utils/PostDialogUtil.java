package com.example.maptest.mycartest.Utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.maptest.mycartest.R;


/**
 * Created by ${Author} on 2017/4/11.
 * Use to
 */

public class PostDialogUtil {
    public static Dialog creatDialog(Context context){
        LayoutInflater inflater1 = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater1.inflate(R.layout.item_mydialog,null);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.dialogviews);
        ImageView imageView = (ImageView) view.findViewById(R.id.img);
        Animation animation = AnimationUtils.loadAnimation(context,R.anim.loading_animation);
        imageView.startAnimation(animation);
        Dialog lodingdialog = new Dialog(context,R.style.loading_dialogs);
        lodingdialog.setCancelable(false);
        lodingdialog.setContentView(linearLayout,new LinearLayout.LayoutParams( 300,
                200));
        return lodingdialog;
    }

    public  Dialog creatDialogs(Context context){
        LayoutInflater inflater1 = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater1.inflate(R.layout.item_mydialog,null);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.dialogviews);
        ImageView imageView = (ImageView) view.findViewById(R.id.img);
        Animation animation = AnimationUtils.loadAnimation(context,R.anim.loading_animation);
        imageView.startAnimation(animation);
        Dialog lodingdialog = new Dialog(context,R.style.loading_dialogs);
        lodingdialog.setCancelable(false);
        lodingdialog.setContentView(linearLayout,new LinearLayout.LayoutParams( 300,
                200));
        return lodingdialog;
    }
}
