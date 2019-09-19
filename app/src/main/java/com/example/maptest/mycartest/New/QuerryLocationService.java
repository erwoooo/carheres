package com.example.maptest.mycartest.New;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by ${Author} on 2018/3/13.
 * Use to
 */

public interface QuerryLocationService {
    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("user/findAllUser")
    Call<ResponseBody> getMessage(@Body RequestBody info);   // 请求体味RequestBody 类型

    @GET("device/enableTime")  //电池激活日期
    Call<ResponseBody> getEnableTime(@Query("terminalID") String terminalID);



}
