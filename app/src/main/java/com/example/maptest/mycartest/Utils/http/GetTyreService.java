package com.example.maptest.mycartest.Utils.http;

import com.alibaba.fastjson.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ${Author} on 2017/8/25.
 * Use to  更新胎压指令
 */

public interface GetTyreService {
    @GET("appUpdateTireInfo")
    Call<ResponseBody>getOrder(@Query("TerminalID") String TerminalID,@Query("Field") String Field);
}
