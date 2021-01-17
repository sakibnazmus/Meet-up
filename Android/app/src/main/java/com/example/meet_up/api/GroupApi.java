package com.example.meet_up.api;

import com.example.meet_up.payload.request.GroupCreateRequest;
import com.example.meet_up.payload.response.ApiResponse;
import com.example.meet_up.payload.response.GroupSummaryResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface GroupApi {

    @POST("new")
    Call<ApiResponse> createNewGroup(@Body GroupCreateRequest request);

    @GET(".")
    Call<List<GroupSummaryResponse>> getGroupList();
}