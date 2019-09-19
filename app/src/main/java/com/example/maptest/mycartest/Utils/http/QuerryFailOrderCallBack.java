package com.example.maptest.mycartest.Utils.http;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baidu.mapapi.map.MapViewLayoutParams;
import com.example.maptest.mycartest.Bean.QuerryOrderBean;
import com.squareup.okhttp.Response;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${Author} on 2017/11/25.
 * Use to
 */

public abstract class QuerryFailOrderCallBack extends Callback<List<QuerryOrderBean>> {
    @Override
    public List<QuerryOrderBean> parseNetworkResponse(Response response) throws IOException {
        String string = new String(response.body().bytes());
        Log.e("报警信息",string);
        List<QuerryOrderBean>list = new ArrayList<>();
        if (string.length() > 20){
            list = JSON.parseObject(string,new TypeReference<List<QuerryOrderBean>>(){});
        }else {

        }

        return list;
    }
}
