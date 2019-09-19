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

public interface PostReadService {
    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("deviceAlarms/save")
    Call<ResponseBody> getMessage(@Body RequestBody info);   // 请求体味RequestBody 类型
}
