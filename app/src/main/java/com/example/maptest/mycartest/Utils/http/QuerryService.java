package com.example.maptest.mycartest.Utils.http;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by ${Author} on 2018/3/10.
 * Use to
 */

public interface QuerryService {
    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("user/findAllAgentApp")
    Call<ResponseBody> getMessage(@Body RequestBody info);   // 请求体味RequestBody 类型

    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("user/findById")
    Call<ResponseBody> findAgent(@Body RequestBody info);   // 请求体味RequestBody 类型

    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("tracker_usr")
    Call<ResponseBody> psotAgent(@Body RequestBody info);   // 请求体味RequestBody 类型

    @GET("battery")
    Call<ResponseBody>getBattery(@Query("terminalID") String terminalID);

    @GET("version")
    Call<ResponseBody>getAppNmae(@Query("appName") String appName);
}
