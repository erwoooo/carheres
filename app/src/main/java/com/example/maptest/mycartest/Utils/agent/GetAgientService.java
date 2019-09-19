package com.example.maptest.mycartest.Utils.agent;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ${Author} on 2017/10/24.
 * Use to
 */

public interface GetAgientService {
    @GET("appGetAgents")
    Call<ResponseBody> getAgent(@Query("AgentId") String AgentId);
}
