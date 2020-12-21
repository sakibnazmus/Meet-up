package com.example.meet_up.ui;

import android.app.Activity;

import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.meet_up.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_GOOGLE_SIGN_IN = 0;
    private static final String TAG = "LoginActivity";
    private Activity mActivity;

    private EditText mEmail;
    private EditText mPassword;
    private Button mLoginBtn;
    private TextView mSignUpText;
    private ProgressBar mLoadingProgressBar;
    private SignInButton mGoogleSignInBtn;

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mActivity = this;

        mEmail = findViewById(R.id.login_email);
        mPassword = findViewById(R.id.login_password);
        mLoginBtn = findViewById(R.id.login);
        mSignUpText = findViewById(R.id.signup_text);
        mLoadingProgressBar = findViewById(R.id.login_loading);
        mGoogleSignInBtn = findViewById(R.id.google_sign_in_btn);

        mGoogleSignInBtn.setSize(SignInButton.SIZE_WIDE);

        mSignUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUpIntent = new Intent(mActivity, SignUpActivity.class);
                startActivity(signUpIntent);
                finishActivity(0);
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCredentials();
                mLoadingProgressBar.setVisibility(View.VISIBLE);
            }
        });

        mGoogleSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
            }
        });

        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            updateUI(account);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) updateUI(account);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    private void checkCredentials() {
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
    }

    private void updateUI(GoogleSignInAccount account) {
        Log.v(TAG, account.getEmail());
        Log.v(TAG, account.getDisplayName());
        Log.v(TAG, account.getId());
        Log.v(TAG, account.getIdToken());

        if(verifyWithServer(account.getIdToken())) {
            Intent homeIntent = new Intent(mActivity, HomeActivity.class);
            startActivity(homeIntent);
            finishActivity(0);
        }
    }

    private boolean verifyWithServer(String idToken) {
        return true;
    }
}
