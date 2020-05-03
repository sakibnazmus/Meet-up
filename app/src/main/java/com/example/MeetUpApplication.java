package com.example;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MeetUpApplication extends Application{
    private static MeetUpApplication mInstance;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mDatabaseReference;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public static MeetUpApplication getInstance() {
        return mInstance;
    }

    public FirebaseAuth getAuth() {
        return mAuth;
    }

    public FirebaseUser getUser() {
        if(mUser == null) {
            mUser = mAuth.getCurrentUser();
        }
        return mUser;
    }

    public DatabaseReference getUsersReference() {
        return mDatabaseReference.child("users");
    }

    public DatabaseReference getGroupsReference() {
        return mDatabaseReference.child("groups");
    }
}
