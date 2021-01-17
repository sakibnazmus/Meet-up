package com.example.meet_up.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.meet_up.api.GroupApi;
import com.example.meet_up.api.UserApi;
import com.example.meet_up.model.AuthToken;
import com.example.meet_up.model.BasicUserInfo;
import com.example.meet_up.model.UserLocation;
import com.example.meet_up.payload.request.LocationUpdateRequest;
import com.example.meet_up.payload.response.ApiResponse;
import com.example.meet_up.util.Constants;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserService {

    private static final String TAG = UserService.class.getSimpleName();
    private static final String GROUP_RELATIVE_URL = "users/";
    private static final String url = Constants.BASE_URL + GROUP_RELATIVE_URL;
    private static UserService mInstance;
    private MutableLiveData<BasicUserInfo> mUserInfo;
    private UserApi api;
    private SharedPreferences preferences;
    private static String SHARED_PREF_KEY_USER_ID = "id";
    private static String SHARED_PREF_KEY_USER_NAME = "name";
    private static String SHARED_PREF_KEY_USER_EMAIL = "email";

    public UserService(Context context) {
        mUserInfo = new MutableLiveData<>();
        preferences = context.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(chain -> {
            Request.Builder requestBuilder = chain.request().newBuilder();
            AuthToken token = AuthService.getInstance(context).getAuthToken().getValue();
            requestBuilder.addHeader(Constants.HEADER_KEY_AUTHORIZATION, token.getAuthToken());
            return chain.proceed(requestBuilder.build());
        }).build();

        this.api = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build()
                .create(UserApi.class);
    }

    public void setUserInfo(BasicUserInfo userInfo) {
        Log.v(TAG, "Set user info called");
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SHARED_PREF_KEY_USER_ID, userInfo.getUserId());
        editor.putString(SHARED_PREF_KEY_USER_NAME, userInfo.getName());
        editor.putString(SHARED_PREF_KEY_USER_EMAIL, userInfo.getEmail());
        editor.apply();
        mUserInfo.setValue(userInfo);
    }

    public MutableLiveData<BasicUserInfo> getUserInfo() {
        Log.v(TAG, "Get User Info called");
        if (mUserInfo.getValue() == null) {
            String id = preferences.getString(SHARED_PREF_KEY_USER_ID, null);
            String name = preferences.getString(SHARED_PREF_KEY_USER_NAME, null);
            String email = preferences.getString(SHARED_PREF_KEY_USER_EMAIL, null);
            if (id != null && name != null && email != null) {
                BasicUserInfo userInfo = new BasicUserInfo(id, name, email);
                mUserInfo.setValue(userInfo);
            } else {
                Log.v(TAG, "No user saved");
            }
        }
        return mUserInfo;
    }

    public void updateUserLocation(UserLocation location) {
        LocationUpdateRequest request = new LocationUpdateRequest(location);
        api.updateUserLocation(request).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.v(TAG, response.message());
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e(TAG, t.getLocalizedMessage());
                t.printStackTrace();
            }
        });
    }

    public static UserService getInstance(Context context) {
        if (mInstance == null) mInstance = new UserService(context);
        return mInstance;
    }
}