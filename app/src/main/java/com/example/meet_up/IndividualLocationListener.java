package com.example.meet_up;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class IndividualLocationListener implements ChildEventListener {

    private final static String TAG = "LocationUpdater";
    private User user;
    private MapsActivity mapsActivity;
    public IndividualLocationListener(User user, MapsActivity mapsActivity) {
        this.user = user;
        this.mapsActivity = mapsActivity;
    }

    private void setLocation(DataSnapshot dataSnapshot) {
        if(dataSnapshot.getKey().equals("location")) {
            User.Location userLocation = user.getLocation();
            for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                if(snapshot.getKey().equals("latitude")) {
                    userLocation.setLatitude((Double) snapshot.getValue());
                } else if(snapshot.getKey().equals("longitude")) {
                    userLocation.setLongitude((Double) snapshot.getValue());
                }
            }
            if(!userLocation.isInvalid())
                mapsActivity.updateMap(user);
        }
    }
    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Log.v(TAG,  "Added user: " + user.getUid() + " : " + dataSnapshot + " s: " + s);
        setLocation(dataSnapshot);
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Log.v(TAG,  "changed: " + user.getUid() + " : " +  dataSnapshot + " s: " + s);
        setLocation(dataSnapshot);
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
        Log.v(TAG,  "removed: " + user.getUid() + " : " + dataSnapshot);
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Log.v(TAG,  "moved: "+ dataSnapshot + " s: " + s);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        Log.v(TAG,  "Error: " + databaseError);
    }
}
