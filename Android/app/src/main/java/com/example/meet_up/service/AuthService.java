package com.example.meet_up.service;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.meet_up.api.AuthApi;
import com.example.meet_up.model.AuthToken;
import com.example.meet_up.model.BasicUserInfo;
import com.example.meet_up.payload.request.EmailSignInRequest;
import com.example.meet_up.payload.response.LoginResponse;
import com.example.meet_up.util.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthService {

    private static final String TAG = AuthService.class.getSimpleName();
    private static final String AUTH_RELATIVE_URL = "auth/";
    private static final String url = Constants.BASE_URL + AUTH_RELATIVE_URL;
    private AuthApi api;

    private static AuthService mInstance;

    public MutableLiveData<Boolean> mIsLoginSuccess;
    public MutableLiveData<AuthToken> mAuthToken;

    private AuthService() {
        mIsLoginSuccess = new MutableLiveData<>();
        mIsLoginSuccess.setValue(Boolean.FALSE);
        mAuthToken = new MutableLiveData<>();

        api = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AuthApi.class);
    }

    public static AuthService getInstance() {
        if(mInstance == null) mInstance = new AuthService();
        return mInstance;
    }

    public void emailLogIn(EmailSignInRequest request) {
        api.emailLogIn(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Log.i(TAG, "onResponse: " + response.toString());
                if (response.isSuccessful()) {
                    handleLoginResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                mIsLoginSuccess.setValue(Boolean.FALSE);
            }
        });
    }

    private void handleLoginResponse(LoginResponse loginResponse) {
        if (loginResponse != null) {
            Log.i(TAG, loginResponse.toString());
            mIsLoginSuccess.setValue(Boolean.TRUE);
            mAuthToken.setValue(new AuthToken(loginResponse.getTokenType(), loginResponse.getAccessToken()));
            BasicUserInfo userInfo = new BasicUserInfo(loginResponse.getUserId(),
                    loginResponse.getName(), loginResponse.getEmail());
            UserService.getInstance().setUserInfo(userInfo);
        } else {
            Log.e(TAG, "Login response is null");
        }
    }
}
