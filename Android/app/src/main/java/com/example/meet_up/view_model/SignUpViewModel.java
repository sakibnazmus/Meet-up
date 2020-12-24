package com.example.meet_up.view_model;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.meet_up.payload.request.SignUpRequest;
import com.example.meet_up.service.AuthService;

public class SignUpViewModel extends ViewModel {

    public String name;
    public String email;
    public String password1;
    public String password2;

    private AuthService mAuthService;

    public SignUpViewModel() {
        mAuthService = AuthService.getInstance();
    }

    public void onSignUpButtonClicked(View view) {
        if (!password1.equals(password2)) {
            mAuthService.mResponseMessage.setValue("Passwords do not match");
            return;
        }

        SignUpRequest request = new SignUpRequest(email, name, password1);
        mAuthService.signUp(request);
    }

    public LiveData<String> getResponseMessage() {
        return mAuthService.mResponseMessage;
    }

    public LiveData<Boolean> getSignUpSuccess() {
        return mAuthService.mIsSignUpSuccess;
    }
}
