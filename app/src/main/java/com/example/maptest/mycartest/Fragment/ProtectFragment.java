package com.example.maptest.mycartest.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.maptest.mycartest.R;

/**
 * Copyright (C)
 * <p>
 * FileName: ProtectFragment
 * <p>
 * Author: ${Erwoo}
 * <p>
 * Date: 2019/8/14 14:40
 * <p>
 * Description: ${报案信息}
 */
public class ProtectFragment extends BaseFragment {

    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pro,null);
        return view;
    }
}
