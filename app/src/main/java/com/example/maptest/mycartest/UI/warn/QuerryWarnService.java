package com.example.maptest.mycartest.UI.warn;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by ${Author} on 2018/3/15.
 * Use to
 */

public interface QuerryWarnService {
    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("deviceAlarms/findByTerminalID")
    Call<ResponseBody> getMessage(@Body RequestBody info);   // 请求体味RequestBody 类型

    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("deviceAlarms/findPageByTerminalID")
    Call<ResponseBody> getMessageOnlyOne(@Body RequestBody info);   // 请求体味RequestBody 类型
}
