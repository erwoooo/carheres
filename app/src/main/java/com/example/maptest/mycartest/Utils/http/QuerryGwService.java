package com.example.maptest.mycartest.Utils.http;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ${Author} on 2017/11/23.
 * Use toGW005保存指令
 */

public interface QuerryGwService {
    @GET("appGetCommandInfo")
    Call<ResponseBody>postOrder(@Query("TerminalID") String TerminalID, @Query("Field") String Field);
}
