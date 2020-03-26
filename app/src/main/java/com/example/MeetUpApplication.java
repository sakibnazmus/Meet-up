package com.example;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;

public class MeetUpApplication extends Application{
    private static MeetUpApplication mInstance;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        mAuth = FirebaseAuth.getInstance();
    }

    public static MeetUpApplication getInstance() {
        return mInstance;
    }

    public FirebaseAuth getAuth() {
        return mAuth;
    }
}
