package com.example.maptest.mycartest.Utils.http;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ${Author} on 2017/11/21.
 * Use to
 */

public interface AddPhoneService {
    @GET("qxg/appAddPhone")
    Call<ResponseBody>getContral(@Query("imei")String imei,@Query("lat")Double lat,@Query("lon")Double lon,@Query("time")String time);



}
