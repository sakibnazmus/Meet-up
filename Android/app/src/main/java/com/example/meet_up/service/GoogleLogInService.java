package com.example.meet_up.service;

import android.content.Context;

import com.example.meet_up.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class GoogleLogInService {

    private Context mContext;
    private static GoogleLogInService logInService;
    private GoogleSignInOptions mSignInOptions;

    private GoogleLogInService(Context context) {
        mContext = context;
        mSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(context.getString(R.string.server_client_id))
                .build();    }

    public static GoogleLogInService getInstance(Context context) {
        if(logInService == null) {
            logInService = new GoogleLogInService(context);
        }
        return logInService;
    }

    public GoogleSignInClient getClient() {
        return GoogleSignIn.getClient(mContext, mSignInOptions);
    }

    public boolean isSignedIn() {
        return GoogleSignIn.getLastSignedInAccount(mContext) != null;
    }
}
