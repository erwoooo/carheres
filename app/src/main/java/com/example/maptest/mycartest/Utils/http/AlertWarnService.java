package com.example.maptest.mycartest.Utils.http;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ${Author} on 2017/8/26.
 * Use to 插入报警信息
 */

public interface AlertWarnService {
    @GET("appInsertWireless")
    Call<ResponseBody> getOrder(@Query("TerminalID") String TerminalID, @Query("SendTime") String SendTime,@Query("OfflineContext") String OfflineContext,@Query("commandType") int commandType,@Query("commandContext") String commandContext);
}
