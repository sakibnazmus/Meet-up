package com.example.meet_up.view_model;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.meet_up.model.AuthToken;
import com.example.meet_up.payload.request.EmailSignInRequest;
import com.example.meet_up.payload.request.GoogleSignInRequest;
import com.example.meet_up.service.AuthService;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class LoginViewModel extends ViewModel {

    private static final String TAG = LoginViewModel.class.getSimpleName();
    public String email;
    public String password;

    public void onLoginButtonClicked(View view) {
        Log.d(TAG, "onLoginButtonClicked: " + email);

        EmailSignInRequest signInRequest = new EmailSignInRequest(email, password);
        AuthService.getInstance(view.getContext()).emailSignIn(signInRequest);
    }

    public LiveData<Boolean> isLoginSuccess(Context context) {
        return AuthService.getInstance(context).mIsLoginSuccess;
    }

    public LiveData<AuthToken> getAuthToken(Context context) {
        return AuthService.getInstance(context).getAuthToken();
    }

    public void googleSignIn(Context context, GoogleSignInAccount googleAccount) {
        GoogleSignInRequest request = new GoogleSignInRequest(googleAccount.getIdToken());
        AuthService.getInstance(context).googleSignIn(request);
    }
}
