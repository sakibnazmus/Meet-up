package com.example.meet_up.view_model;

import android.content.Context;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.meet_up.model.AuthToken;
import com.example.meet_up.payload.request.SignUpRequest;
import com.example.meet_up.service.AuthService;

public class SignUpViewModel extends ViewModel {

    public String name;
    public String email;
    public String password1;
    public String password2;

    public void onSignUpButtonClicked(View view) {
        if (!password1.equals(password2)) {
            AuthService.getInstance(view.getContext()).mResponseMessage.setValue("Passwords do not match");
            return;
        }

        SignUpRequest request = new SignUpRequest(email, name, password1);
        AuthService.getInstance(view.getContext()).signUp(request);
    }

    public LiveData<String> getResponseMessage(Context context) {
        return AuthService.getInstance(context).mResponseMessage;
    }

    public LiveData<Boolean> getSignUpSuccess(Context context) {
        return AuthService.getInstance(context).mIsSignUpSuccess;
    }
}
