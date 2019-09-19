package com.example.maptest.mycartest.Utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.maptest.mycartest.Fragment.HistoryFragment;
import com.example.maptest.mycartest.Fragment.MessageFragment;
import com.example.maptest.mycartest.Fragment.SetFragment;
import com.example.maptest.mycartest.Fragment.TraceFragment;

import java.util.ArrayList;

/**
 * Created by ${Author} on 2017/3/20.
 * Use to  fragment控制类
 */

public class InforFragmentdisplayController {

    private int containerId;
    private FragmentManager manager;
    private ArrayList<Fragment> fragmentArrayList;

    private static InforFragmentdisplayController controller;

    public static InforFragmentdisplayController getInsance(FragmentActivity activity, int containerId){
        if (controller == null){
            controller = new InforFragmentdisplayController(activity,containerId);
        }
        return controller;
    }
    private InforFragmentdisplayController(FragmentActivity activity, int containerId){
        this.containerId = containerId;
        manager = activity.getSupportFragmentManager();
        initFragment();
    }

    private void initFragment() {
        fragmentArrayList = new ArrayList<Fragment>();
        fragmentArrayList.add(new MessageFragment());
        fragmentArrayList.add(new TraceFragment());
        fragmentArrayList.add(new HistoryFragment());
        FragmentTransaction transaction = manager.beginTransaction();
        for (Fragment fragment : fragmentArrayList){
            transaction.add(containerId,fragment);
        }
        transaction.commitAllowingStateLoss();
    }
    public void showFragment(int position){     //点击显示fragment
        hideFragment();
        Fragment fragment = fragmentArrayList.get(position);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.show(fragment);
        transaction.commitAllowingStateLoss();
    }

    private void hideFragment() {       //隐藏其他的fragment
        FragmentTransaction transaction = manager.beginTransaction();
        for (Fragment fragment : fragmentArrayList){
            if (fragment != null){
                transaction.hide(fragment);
            }
        }
        transaction.commitAllowingStateLoss();
    }
    public Fragment getFragment(int position){
        return fragmentArrayList.get(position);
    }
    public static void destoryController(){
        controller = null;
    }


}
