package com.example.maptest.mycartest.Utils.http;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by ${Author} on 2018/3/10.
 * Use to
 */

public interface QuerryDeviceService {
    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("user/findAllUser")
    Call<ResponseBody> getMessage(@Body RequestBody info);   // 请求体味RequestBody 类型
}
//867004000000590