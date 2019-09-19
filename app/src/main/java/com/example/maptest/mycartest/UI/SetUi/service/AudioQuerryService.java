package com.example.maptest.mycartest.UI.SetUi.service;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by ${Author} on 2018/7/14.
 * Use to
 */

public interface AudioQuerryService {
    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("video/findByTerminalID")
    Call<ResponseBody> getMessage(@Body RequestBody info);   // 请求体味RequestBody 类型

    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("video/update")
    Call<ResponseBody> postRead(@Body RequestBody info);   // 请求体味RequestBody 类型

    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("video/deleteAll")
    Call<ResponseBody> deleteAudio(@Body RequestBody info);   // 请求体味RequestBody 类型
}
