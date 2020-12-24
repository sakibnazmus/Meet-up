package com.example.meet_up.api;

import com.example.meet_up.payload.request.EmailSignInRequest;
import com.example.meet_up.payload.request.GoogleSignInRequest;
import com.example.meet_up.payload.request.SignUpRequest;
import com.example.meet_up.payload.response.ApiResponse;
import com.example.meet_up.payload.response.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {

    @POST("login/oauth2/google")
    Call<LoginResponse> googleLogIn(@Body GoogleSignInRequest request);

    @POST("login/email")
    Call<LoginResponse> emailLogIn(@Body EmailSignInRequest request);

    @POST("signup")
    Call<ApiResponse> signUp(@Body SignUpRequest request);
}
