package com.example.maptest.mycartest.Utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.maptest.mycartest.Fragment.CapacityFragment;
import com.example.maptest.mycartest.Fragment.ListFragment;
import com.example.maptest.mycartest.Fragment.MapFragment;
import com.example.maptest.mycartest.Fragment.ProtectFragment;
import com.example.maptest.mycartest.Fragment.TyreFragment;

import java.util.ArrayList;

/**
 * Created by ${Author} on 2017/3/13.
 * Use to       主界面fragment的控制类
 */

public class FragmentController {
    private int containerId;
    private FragmentManager manager;
    private ArrayList<Fragment>fragmentArrayList;

    private static FragmentController controller;



    public static FragmentController getInstance(FragmentActivity activity,int containerId){
     if (controller == null){
         controller = new FragmentController(activity,containerId);
         }
        return controller;
    }
    private FragmentController(FragmentActivity activity,int containerId){
        this.containerId = containerId;
        manager = activity.getSupportFragmentManager();
        initFragment();
    }

    private void initFragment() {       //实例化
        fragmentArrayList =new ArrayList<Fragment>();
        fragmentArrayList.add(new MapFragment());
        fragmentArrayList.add(new TyreFragment());
        fragmentArrayList.add(new ListFragment());
        fragmentArrayList.add(new CapacityFragment());
        FragmentTransaction transaction = manager.beginTransaction();
        for (Fragment fragment : fragmentArrayList){
            transaction.add(containerId,fragment);
        }
        transaction.commitAllowingStateLoss();
    }
    public void showFragment(int position){     //点击显示fragment
        hideFragments();
        Fragment fragment = fragmentArrayList.get(position);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.show(fragment);
        transaction.commitAllowingStateLoss();
    }

    private void hideFragments() {      //点击隐藏其他的fragment
        FragmentTransaction transaction = manager.beginTransaction();
        for (Fragment fragment : fragmentArrayList){
            if (fragment != null){
                transaction.hide(fragment);
            }
        }
            transaction.commitAllowingStateLoss();
    }
    public  Fragment getFragment(int position){
        return fragmentArrayList.get(position);
    }
    public static void destoryController(){
        controller = null;
    }


}
