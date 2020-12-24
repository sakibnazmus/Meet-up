package com.example;

import android.app.Application;

public class MeetUpApplication extends Application{
    private static MeetUpApplication mInstance;
    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        mInstance = this;
    }

    public static MeetUpApplication getInstance() {
        return mInstance;
    }
}
