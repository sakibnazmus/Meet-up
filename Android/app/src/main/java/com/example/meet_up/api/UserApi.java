package com.example.meet_up.api;

import com.example.meet_up.payload.request.LocationUpdateRequest;
import com.example.meet_up.payload.response.ApiResponse;

import retrofit2.Call;
import retrofit2.http.POST;

public interface UserApi {

    @POST("/location/update")
    Call<ApiResponse> updateUserLocation(LocationUpdateRequest request);
}
