package com.example.meet_up.view_model;

import android.util.Log;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.meet_up.payload.request.EmailSignInRequest;
import com.example.meet_up.service.AuthService;

public class LoginViewModel extends ViewModel {

    private static final String TAG = LoginViewModel.class.getSimpleName();
    public String email;
    public String password;
    private AuthService mAuthService;

    public LoginViewModel() {
        mAuthService = AuthService.getInstance();
    }

    public void onLoginButtonClicked(View view) {
        Log.d(TAG, "onLoginButtonClicked: " + email);

        EmailSignInRequest signInRequest = new EmailSignInRequest(email, password);
        mAuthService.emailLogIn(signInRequest);
    }

    public LiveData<Boolean> isLoginSuccess() {
        return mAuthService.mIsLoginSuccess;
    }
}
