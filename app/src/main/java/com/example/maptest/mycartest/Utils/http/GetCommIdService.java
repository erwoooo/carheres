package com.example.maptest.mycartest.Utils.http;

import com.alibaba.fastjson.JSONObject;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by ${Author} on 2017/8/26.
 * Use to  更新指令
 */

public interface GetCommIdService {
    @GET("appUpdateCommandInfo")
    Call<ResponseBody>postOrder(@Query("TerminalID") String TerminalID, @Query("Field") String Field);
    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("user/findprice")
    Call<ResponseBody>postCom(@Body RequestBody info);

    @GET("appUpdateUserInfo")
    Call<ResponseBody>updateTime(@Query("TerminalID") String TerminalID, @Query("Field") String Field);

    @GET("appRechargeRecord")
    Call<ResponseBody> postReord(@Query("Field") String Field);
}



