package com.example.meet_up.service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.meet_up.model.BasicUserInfo;
import com.example.meet_up.model.User;
import com.example.meet_up.model.UserLocation;

public class LocationService extends Service implements LocationListener {

    private static final String TAG = "LocationService";

    public MutableLiveData<UserLocation> userLocation;

    private Observer<BasicUserInfo> userInfoObserver;
    private Observer<UserLocation> userLocationObserver;
    private String currentUserId;

    public class LocationBinder extends Binder {
        public LocationService getService() {
            return LocationService.this;
        }
    }

    private final IBinder binder = new LocationBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "OnCreate()");

        userLocation = new MutableLiveData<>();
        userLocationObserver = userLocation -> UserService.getInstance(getApplicationContext()).updateUserLocation(userLocation);
        userLocation.observeForever(userLocationObserver);

//        if (Build.VERSION.SDK_INT >= 26) {
//            String CHANNEL_ID = "my_channel_01";
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
//                    "Meet Up",
//                    NotificationManager.IMPORTANCE_DEFAULT);
//
//            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);
//
//            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
//                    .setContentTitle("Meet Up")
//                    .setContentText("This app is uploading your location in background")
//                    .build();
//
//            startForeground(1, notification);
//        }

        userInfoObserver = userInfo -> currentUserId = userInfo.getUserId();
        UserService.getInstance(this).getUserInfo().observeForever(userInfoObserver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.v(TAG, "OnBind()");
        return binder;
    }

    public void stopService() {
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        UserService.getInstance(this).getUserInfo().removeObserver(userInfoObserver);
        userLocation.removeObserver(userLocationObserver);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.v(TAG, "onLocationChanged: " + location.getLatitude() + " " + location.getLongitude());
//        UserLocation current = new UserLocation(location.getLatitude(), location.getLongitude());
        if (userLocation.getValue() == null) {
            userLocation.setValue(new UserLocation(location.getLatitude(), location.getLongitude()));
        } else {
            userLocation.getValue().setLatitude(location.getLatitude());
            userLocation.getValue().setLongitude(location.getLongitude());
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.v(TAG, "onStatusChanged: " + s);
    }

    @Override
    public void onProviderEnabled(String s) {
        Log.v(TAG, "onProviderEnabled: " + s);
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.v(TAG, "onProviderDisabled");
    }
}
