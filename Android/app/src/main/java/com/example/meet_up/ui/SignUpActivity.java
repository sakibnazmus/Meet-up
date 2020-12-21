package com.example.meet_up;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.MeetUpApplication;

public class SignupActivity extends AppCompatActivity {
    private Activity mActivity;

    private EditText mEmail;
    private EditText mPass;
    private EditText mConfirm;
    private Button mSignUpBtn;
    private ProgressBar mLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mActivity = this;

        mEmail = findViewById(R.id.signup_email);
        mPass = findViewById(R.id.signup_password);
        mConfirm = findViewById(R.id.signup_password_confirm);
        mSignUpBtn = findViewById(R.id.signup);
        mLoading = findViewById(R.id.signup_loading);

        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass1 = mPass.getText().toString();
                String pass2 = mConfirm.getText().toString();
                if (pass1.equals(pass2)) {
                    String email = mEmail.getText().toString();
                    mLoading.setVisibility(View.VISIBLE);
                    createNewUser(email, pass1);
                } else {
                    Toast.makeText(mActivity, "Authentication Failed. Passwords do not match",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createNewUser(String email, String pass) {

    }

    private void updateUI() {
        Intent homeActivity = new Intent(mActivity, HomeActivity.class);
        startActivity(homeActivity);
        finishActivity(0);
    }
}
