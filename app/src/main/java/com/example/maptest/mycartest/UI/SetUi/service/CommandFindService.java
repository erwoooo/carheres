package com.example.maptest.mycartest.UI.SetUi.service;

import com.example.maptest.mycartest.Entity.AppVersion;
import com.example.maptest.mycartest.Entity.NbDevice;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by ${Author} on 2018/3/29.
 * Use to
 */

public interface CommandFindService {
    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("order/find")
    Call<ResponseBody> getMessage(@Body RequestBody info);   // 请求体味RequestBody 类型

    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("order/save")
    Call<ResponseBody> saveMessage(@Body RequestBody info);   // 请求体味RequestBody 类型

    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("tracker_cmd")
    Call<ResponseBody> saveCommands(@Body RequestBody info);   // 请求体味RequestBody 类型

    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("command/insert")
    Call<ResponseBody> saveLixianCommands(@Body RequestBody info);   // 请求体味RequestBody 类型

    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("command/find")
    Call<ResponseBody> findCommands(@Body RequestBody info);   // 请求体味RequestBody 类型

    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("tracker_del")
    Call<ResponseBody> delCommands(@Body RequestBody info);   // 请求体味RequestBody 类型

    @GET("user/checkPassword")
    Call<ResponseBody>checkPassword(@Query("password")String password , @Query("terminalID") String terminalID);

    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("device/updateTime")
    Call<ResponseBody>hireExpirationTime(@Body RequestBody info);

    @GET("version")
    Call<AppVersion>getAppVersion(@Query("appName") String appName);

    @GET("nb/findDeviceId")
    Call<NbDevice>findDeviceId(@Query("terminalID") String terminalId);

}
