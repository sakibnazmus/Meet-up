package com.example.meet_up.service;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.meet_up.api.GroupApi;
import com.example.meet_up.model.AuthToken;
import com.example.meet_up.payload.request.GroupCreateRequest;
import com.example.meet_up.payload.response.ApiResponse;
import com.example.meet_up.util.Constants;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GroupService {

    private static final String TAG = GroupService.class.getSimpleName();
    private static final String GROUP_RELATIVE_URL = "groups/";
    private static final String url = Constants.BASE_URL + GROUP_RELATIVE_URL;

    private GroupApi api;
    public static GroupService mInstance;

    private GroupService(Context context) {
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(chain -> {
            Request.Builder requestBuilder = chain.request().newBuilder();
//            requestBuilder.addHeader(Constants.HEADER_CONTENT_TYPE_JSON, Constants.HEADER_CONTENT_TYPE_JSON);
            AuthToken token = AuthService.getInstance(context).getAuthToken().getValue();
            requestBuilder.addHeader(Constants.HEADER_KEY_AUTHORIZATION, token.getAuthToken());
            Log.v(TAG, token.getAuthToken());
            return chain.proceed(requestBuilder.build());
        }).build();

        this.api = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build()
                .create(GroupApi.class);
    }

    public static GroupService getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new GroupService(context);
        }
        return mInstance;
    }

    public LiveData<String> createNewGroup(GroupCreateRequest request) {
        MutableLiveData<String> responseData = new MutableLiveData<>();
        api.createNewGroup(request).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, retrofit2.Response<ApiResponse> response) {
                if (response.body() != null) {
                    responseData.setValue(response.body().getMessage());
                } else {
                    try {
                        responseData.setValue(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                responseData.setValue(t.getLocalizedMessage());
            }
        });
        return responseData;
    }
}
