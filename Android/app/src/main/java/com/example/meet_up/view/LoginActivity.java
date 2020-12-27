package com.example.meet_up.view;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meet_up.R;
import com.example.meet_up.databinding.ActivityLoginBinding;
import com.example.meet_up.service.GoogleLogInService;
import com.example.meet_up.view_model.LoginViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_GOOGLE_SIGN_IN = 0;
    private static final String TAG = LoginActivity.class.getSimpleName();

    private Activity mActivity;
    private LoginViewModel mLoginViewModel;

    private TextView mSignUpText;
    private ProgressBar mLoadingProgressBar;
    private SignInButton mGoogleSignInBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        mLoginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        binding.setLoginViewModel(mLoginViewModel);
        binding.setLifecycleOwner(this);

        mActivity = this;
        mSignUpText = findViewById(R.id.signup_text);
        mLoadingProgressBar = findViewById(R.id.login_loading);
        mGoogleSignInBtn = findViewById(R.id.google_sign_in_btn);

        mGoogleSignInBtn.setSize(SignInButton.SIZE_WIDE);

        mLoginViewModel.isLoginSuccess(this).observe(this, isSuccess -> {
            Log.i(TAG, "Changed Login SuccessValue: " + isSuccess);
            if (isSuccess) {
                Log.i(TAG, "Logged in successfully");
                updateUI();
            }
        });

        mLoginViewModel.getAuthToken(this).observe(this, authToken -> {
            if (authToken != null) {
                Toast.makeText(this, "Logged in successfully!", Toast.LENGTH_LONG).show();
                updateUI();
            }
        });

        mGoogleSignInBtn.setOnClickListener(this::googleSignIn);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void googleSignIn(View view) {
        Log.v(TAG, "Google login button clicked");
        Intent signInIntent = GoogleLogInService.getInstance(this).getClient().getSignInIntent();
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
    }

    public void signUp(View view) {
        Intent signUpIntent = new Intent(mActivity, SignUpActivity.class);
        startActivity(signUpIntent);
        finishActivity(0);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) mLoginViewModel.googleSignIn(mActivity, account);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    private void updateUI() {
        Intent homeIntent = new Intent(mActivity, HomeActivity.class);
        startActivity(homeIntent);
        finishAfterTransition();
    }
}
