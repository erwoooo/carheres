package com.example.maptest.mycartest.Utils.http;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ${Author} on 2017/8/26.
 * Use to  获取指令
 */

public interface GetCommIdStateService {
    @GET("appGetCommandInfo")
    Call<ResponseBody>getOrder(@Query("TerminalID") String TerminalID, @Query("Field") String Field);

}
