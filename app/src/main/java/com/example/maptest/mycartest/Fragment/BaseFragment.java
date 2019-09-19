package com.example.maptest.mycartest.Fragment;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.example.maptest.mycartest.New.NewHttpUtils;
import com.example.maptest.mycartest.UI.SetUi.service.TirmeredBean;
import com.example.maptest.mycartest.Utils.http.ResponseCallback;
import com.google.gson.Gson;

import static com.example.maptest.mycartest.Utils.AppCons.locationListBean;

/**
 * Created by ${Author} on 2018/3/31.
 * Use to
 */

public class BaseFragment extends Fragment {

//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        Log.e("执行",hidden ? "隐藏 " + hidden : "显示 "  +hidden  );
//        if (hidden){
//            Log.e("隐藏","设置的地方隐藏");
//        }else {
//            TirmeredBean bean = new TirmeredBean(locationListBean.getTerminalID(),locationListBean.getLocation().getDeviceProtocol());
//            Gson gson = new Gson();
//            String obj = gson.toJson(bean);
//            NewHttpUtils.findCommand(obj, getContext(), new ResponseCallback() {
//                @Override
//                public void TaskCallBack(Object object) {
//                    Log.e("TaskCallBack",object.toString());
//                }
//
//                @Override
//                public void FailCallBack(Object object) {
//                    Log.e("FailCallBack",object.toString());
//                }
//            });
//        }
//    }
}
