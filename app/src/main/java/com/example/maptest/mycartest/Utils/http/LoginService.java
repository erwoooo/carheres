package com.example.maptest.mycartest.Utils.http;

import com.example.maptest.mycartest.test.LoginDataBean;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by ${Author} on 2018/3/8.
 * Use to
 */

public interface LoginService {
    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("login")
    Call<LoginDataBean>getMessage(@Body RequestBody info);   // 请求体味RequestBody 类型
}
