package com.example.meet_up;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import com.example.MeetUpApplication;
import com.google.firebase.database.DatabaseReference;

public class MeetUpLocationListener implements LocationListener {

    private static String TAG = "LocationListener";
    private DatabaseReference mUserReference;

    public MeetUpLocationListener(String uid) {
        Log.v(TAG, "Creating location listener for: " + uid);
        mUserReference = MeetUpApplication.getInstance().getUsersReference().child(uid);
    }
    @Override
    public void onLocationChanged(Location location) {
        Log.v(TAG, "latitude: " + location.getLatitude() + " longitude: " + location.getLongitude());
        mUserReference.child("location").child("latitude").setValue(location.getLatitude());
        mUserReference.child("location").child("longitude").setValue(location.getLongitude());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
