package com.example.meet_up.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.meet_up.api.AuthApi;
import com.example.meet_up.model.AuthToken;
import com.example.meet_up.model.BasicUserInfo;
import com.example.meet_up.payload.request.EmailSignInRequest;
import com.example.meet_up.payload.request.GoogleSignInRequest;
import com.example.meet_up.payload.request.SignUpRequest;
import com.example.meet_up.payload.response.ApiResponse;
import com.example.meet_up.payload.response.LoginResponse;
import com.example.meet_up.util.Constants;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthService {

    private static final String TAG = AuthService.class.getSimpleName();
    private static final String AUTH_RELATIVE_URL = "auth/";
    private static final String url = Constants.BASE_URL + AUTH_RELATIVE_URL;
    private static final String SHARED_PREF_JWT_TOKEN_KEY = "jwt_token";
    private AuthApi api;

    private SharedPreferences mPreference;
    private static AuthService mInstance;

    public MutableLiveData<Boolean> mIsLoginSuccess;
    public MutableLiveData<Boolean> mIsSignUpSuccess;
    public MutableLiveData<String> mResponseMessage;
    private MutableLiveData<AuthToken> mAuthToken;

    private AuthService(Context context) {
        mIsLoginSuccess = new MutableLiveData<>();
        mIsSignUpSuccess = new MutableLiveData<>();
        mAuthToken = new MutableLiveData<>();
        mResponseMessage = new MutableLiveData<>();
        mIsLoginSuccess.setValue(Boolean.FALSE);
        mIsSignUpSuccess.setValue(Boolean.FALSE);

        mPreference = context.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        api = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AuthApi.class);
    }

    public static AuthService getInstance(Context context) {
        if(mInstance == null) mInstance = new AuthService(context.getApplicationContext());
        return mInstance;
    }

    public void emailSignIn(EmailSignInRequest request) {
        Call<LoginResponse> call = api.emailLogIn(request);

    }

   public void googleSignIn(GoogleSignInRequest request) {
        Call<LoginResponse> call = api.googleLogIn(request);
        makeSignInRequest(call);
   }

   private void makeSignInRequest(Call<LoginResponse> call) {
       call.enqueue(new Callback<LoginResponse>() {
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

    public void signUp(SignUpRequest signUpRequest) {
        api.signUp(signUpRequest).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.v(TAG, response.toString());
                ApiResponse apiResponse = null;
                if (response.isSuccessful()) {
                    apiResponse = response.body();
                } else if (response.errorBody() != null) {
                    apiResponse = new Gson().fromJson(response.errorBody().charStream(), ApiResponse.class);
                }
                if (apiResponse != null) {
                    handleSignUpResponse(apiResponse);
                } else {
                    Log.e(TAG, "Api Response is null");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e(TAG, t.getLocalizedMessage());
            }
        });
    }

    private void handleSignUpResponse(ApiResponse apiResponse) {
        Log.v(TAG, apiResponse.toString());
        Boolean success = apiResponse.getSuccess();
        String responseMessage = apiResponse.getMessage();
        mIsSignUpSuccess.setValue(success);
        mResponseMessage.setValue(responseMessage);
    }

    private void handleLoginResponse(LoginResponse loginResponse) {
        if (loginResponse != null) {
            Log.i(TAG, loginResponse.toString());
            mIsLoginSuccess.setValue(Boolean.TRUE);
            AuthToken authToken = new AuthToken(loginResponse.getTokenType(), loginResponse.getAccessToken());
            setJWTToken(authToken);
            mAuthToken.setValue(authToken);
            BasicUserInfo userInfo = new BasicUserInfo(loginResponse.getUserId(),
                    loginResponse.getName(), loginResponse.getEmail());
            UserService.getInstance().setUserInfo(userInfo);
        } else {
            Log.e(TAG, "Login response is null");
        }
    }

    private void setJWTToken(AuthToken token) {
        SharedPreferences.Editor sharedEditor = mPreference.edit();
        sharedEditor.putString(SHARED_PREF_JWT_TOKEN_KEY, token.getAuthToken());
        sharedEditor.apply();
    }

    public LiveData<AuthToken> getAuthToken() {
        if (mAuthToken.getValue() == null) {
            String token = mPreference.getString(SHARED_PREF_JWT_TOKEN_KEY, null);
            if (token != null) {
                AuthToken authToken = new AuthToken(token);
                mAuthToken.setValue(authToken);
            }
        }
        return mAuthToken;
    }

    public void signOut() {
        SharedPreferences.Editor sharedEditor = mPreference.edit();
        sharedEditor.remove(SHARED_PREF_JWT_TOKEN_KEY);
        sharedEditor.apply();
        mAuthToken.setValue(null);
    }
}
