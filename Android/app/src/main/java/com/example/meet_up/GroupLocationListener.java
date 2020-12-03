package com.example.meet_up;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.MeetUpApplication;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

public class GroupLocationListener implements ChildEventListener {

    private final static String TAG = "GroupLocationListener";
    private MapsActivity mapsActivity;
    private DatabaseReference usersReference;

    private HashMap<String, User> users;

    public GroupLocationListener(MapsActivity activity) {
        mapsActivity = activity;
        usersReference = MeetUpApplication.getInstance().getUsersReference();

        users = new HashMap<>();
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Log.v(TAG, dataSnapshot.toString());
        boolean isEnabled = (boolean) dataSnapshot.getValue();
        Log.v(TAG, "enabled: " + isEnabled);
        String uid = dataSnapshot.getKey();
        User user;
        if(!users.containsKey(uid)) {
            user = new User(uid);
            users.put(uid, user);
            Log.v(TAG, "Adding childEventListener for: " + user.getUid());
            IndividualLocationListener userEventListener = new IndividualLocationListener(user, mapsActivity);
            user.setLocationUpdater(userEventListener);
            usersReference.child(uid).addChildEventListener(userEventListener);
        } else {
            Log.v(TAG, "Contains key: " + uid);
            user = users.get(uid);
        }
        user.setEnabled(isEnabled);
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Log.v(TAG, dataSnapshot.toString());
        String uid = dataSnapshot.getKey();
        User user = users.get(uid);
        Boolean isEnabled = (Boolean) dataSnapshot.getValue();
        user.setEnabled(isEnabled);
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
        Log.v(TAG, dataSnapshot.toString());
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Log.v(TAG, dataSnapshot.toString());
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        Log.v(TAG, databaseError.toString());
    }

    public HashMap<String, User> getUsers() {
        return users;
    }
}
