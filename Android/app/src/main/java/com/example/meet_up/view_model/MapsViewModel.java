package com.example.meet_up.view_model;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.LocationManager;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.meet_up.model.BasicUserInfo;
import com.example.meet_up.model.UserLocation;
import com.example.meet_up.service.LocationService;
import com.example.meet_up.service.UserService;

import java.util.Objects;

public class MapsViewModel extends AndroidViewModel {

    private static final String SETTINGS_BUTTON_TEXT_SETTINGS = "Settings";
    private static final String SETTINGS_BUTTON_TEXT_MAP = "Map";

    private Context mContext;
    private LocationManager mLocationManager;

    private LocationService mService;
    private ServiceConnection mServiceConnection;
    private boolean mBound;

    private MutableLiveData<Pair<BasicUserInfo, UserLocation>> locationWithId;

    public boolean isSettingsVisible;
    public String settingsButtonText;

    public MapsViewModel(@NonNull Application application) {
        super(application);
        mContext = application.getApplicationContext();
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        isSettingsVisible = false;
        settingsButtonText = SETTINGS_BUTTON_TEXT_SETTINGS;

        locationWithId = new MutableLiveData<>();

        mServiceConnection = new ServiceConnection() {
            @SuppressLint("MissingPermission")
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                LocationService.LocationBinder binder = (LocationService.LocationBinder) iBinder;
                mService = binder.getService();
                mService.userLocation.observeForever(userLocation ->
                        locationWithId.setValue(Pair.create(Objects.requireNonNull(locationWithId.getValue()).first, userLocation)));

                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, (float) 0.5, mService);
                mBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                mLocationManager.removeUpdates(mService);
                mService = null;
                mBound = false;
            }
        };

        UserService.getInstance(mContext).getUserInfo().observeForever(basicUserInfo -> {
            locationWithId.setValue(Pair.create(basicUserInfo, null));
        });
    }

    public void startService() {
        Intent intent = new Intent(mContext, LocationService.class);
        mContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public LiveData<Pair<BasicUserInfo, UserLocation>> getUserLocation() {
        return locationWithId;
    }

    public boolean isServiceRunning() {
        return mBound;
    }
}