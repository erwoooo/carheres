package com.example.maptest.mycartest.Utils.http;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ${Author} on 2017/8/26.
 * Use to 插入报警信息
 */

public interface GetAgentService {
    @GET("appGetAgents")
    Call<ResponseBody> getOrder(@Query("AgentId") String AgentId);
}
