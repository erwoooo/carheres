package com.example.maptest.mycartest.New;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;

/**
 * Created by ${Author} on 2018/3/29.
 * Use to
 */

public interface LoginOutService {
    @POST("logout")
    Call<ResponseBody>loginOut();
}
