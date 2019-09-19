package com.example.maptest.mycartest.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.maptest.mycartest.R;
import com.example.maptest.mycartest.UI.EquipUi.CapacityEquipActivity;


/**
 * Created by ${Author} on 2017/10/7.
 * Use to
 */

public class CapacityFragment extends Fragment implements View.OnClickListener {
    private View view;
    private ImageView imageView_begin;
    private RelativeLayout relativeLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_capacity, null);
        initView();
        return view;
    }

    private void initView() {
        imageView_begin = (ImageView) view.findViewById(R.id.image_capacity_begin);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.relative_equip);
        relativeLayout.setOnClickListener(this);
        imageView_begin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_capacity_begin:
                startActivity(new Intent(getActivity(), CapacityEquipActivity.class).putExtra("address","1"));
                break;
            case R.id.relative_equip:
                startActivity(new Intent(getActivity(), CapacityEquipActivity.class).putExtra("address","1"));
                break;
        }
    }
}
