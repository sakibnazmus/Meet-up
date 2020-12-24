package com.example.meet_up.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.meet_up.R;
import com.example.meet_up.databinding.ActivitySignupBinding;
import com.example.meet_up.view.login.LoginActivity;
import com.example.meet_up.view_model.SignUpViewModel;

public class SignUpActivity extends AppCompatActivity {

    private Activity mActivity;
    private SignUpViewModel mSignUpViewModel;

    private ProgressBar mLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySignupBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);
        mSignUpViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);
        binding.setSignUpViewModel(mSignUpViewModel);
        binding.setLifecycleOwner(this);

        mActivity = this;
        mLoading = findViewById(R.id.signup_loading);

        mSignUpViewModel.getResponseMessage().observe(this, this::showToast);
        mSignUpViewModel.getSignUpSuccess().observe(this, success -> {
            if (success) {
                updateUI();
            }
        });
    }

    private void updateUI() {
        Intent loginActivity = new Intent(mActivity, LoginActivity.class);
        startActivity(loginActivity);
        finishActivity(0);
    }

    private void showToast(String text) {
        Toast.makeText(mActivity, text, Toast.LENGTH_LONG).show();
    }
}
