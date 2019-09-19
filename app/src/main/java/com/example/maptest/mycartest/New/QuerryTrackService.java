package com.example.maptest.mycartest.New;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


/**
 * Created by ${Author} on 2018/3/13.
 * Use to请求历史轨迹
 */

public interface QuerryTrackService {
    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("track/find")
    Call<ResponseBody> findNewTrack(@Body RequestBody info);   // 请求体味RequestBody 类型

    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("track/findOld")
    Call<ResponseBody> findOldTrack(@Body RequestBody info);   // 请求体味RequestBody 类型

    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("alarms")
    Call<ResponseBody> findAlarms(@Body RequestBody info);   // 请求体味RequestBody 类型

    @Multipart
    @POST("user/exportPic")
    Call<ResponseBody>exportPic(@Part("terminalID") RequestBody description, @Part MultipartBody.Part file);

    @Multipart
    @POST("version/importApp")
    Call<ResponseBody>uploadApk(@Part("version") RequestBody version,@Part("description") RequestBody description,@Part("appName") RequestBody appName, @Part MultipartBody.Part file);

}
