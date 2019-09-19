package com.example.maptest.mycartest.Utils.http;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ${Author} on 2017/11/25.
 * Use to
 */

public interface QuerryGwFailService {
    @GET("appBsjInsert")
    Call<ResponseBody>postFailOrder(@Query("TerminalID")String TerminalID,@Query("Date")String Data,@Query("Field_M")String Field_M,@Query("Field_R")String Field_R
    ,@Query("command")String Command,@Query("commandLogo")String commandLogo);
}
