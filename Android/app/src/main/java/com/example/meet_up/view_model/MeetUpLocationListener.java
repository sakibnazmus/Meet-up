package com.example.meet_up.view_model;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import com.example.MeetUpApplication;

public class MeetUpLocationListener implements LocationListener {

    private static String TAG = "LocationListener";

    public MeetUpLocationListener(String uid) {
        Log.v(TAG, "Creating location listener for: " + uid);
    }
    @Override
    public void onLocationChanged(Location location) {
        Log.v(TAG, "latitude: " + location.getLatitude() + " longitude: " + location.getLongitude());
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
