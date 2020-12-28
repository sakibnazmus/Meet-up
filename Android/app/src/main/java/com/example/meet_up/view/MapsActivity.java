package com.example.meet_up.view;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.meet_up.model.Location;
import com.example.meet_up.util.Constants;
import com.example.meet_up.view_model.GroupLocationListener;
import com.example.meet_up.service.LocationService;
import com.example.meet_up.R;
import com.example.meet_up.model.User;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MapsActivity";

    private Button mRefreshBtn;
    private Button mEnableBtn;
    private Button mSettingsBtn;

    private GoogleMap mMap;

    private LocationManager mLocationManager;

    private String selectedGroupName;

    private LocationService mService;
    private boolean mBound = false;

    private final static int PERMISSION_FINE_LOCATION = 1;
    private final static int PERMISSION_COARSE_LOCATION = 2;

    private LocationListener mLocationListener;
    GroupLocationListener mGroupLocationlistener;

    private Handler handler;

    private HashMap<String, Marker> markerHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        checkPermissions();

        selectedGroupName = getIntent().getStringExtra(Constants.INTENT_EXTRA_GROUP_NAME);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mRefreshBtn = findViewById(R.id.map_refresh_btn);
        mEnableBtn = findViewById(R.id.enable_btn);
        mSettingsBtn = findViewById(R.id.group_settings_btn);

        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mRefreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adjustMap();
            }
        });

        final SettingsFragment settingsFragment = (SettingsFragment) getSupportFragmentManager().findFragmentById(R.id.settingFrgmnt);
        settingsFragment.getView().setVisibility(View.GONE);

        mEnableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEnableBtn.setEnabled(false);
            }
        });

        mSettingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(settingsFragment.getView().getVisibility() == View.GONE) {
                    Log.v(TAG, "Showing Settings");
                    mapFragment.getView().setVisibility(View.GONE);
                    settingsFragment.getView().setVisibility(View.VISIBLE);
                    mSettingsBtn.setText("Map");
                } else {
                    Log.v(TAG, "Showing Map");
                    mapFragment.getView().setVisibility(View.VISIBLE);
                    settingsFragment.getView().setVisibility(View.GONE);
                    mSettingsBtn.setText("Settings");
                }
            }
        });

        handler = new Handler();

        mGroupLocationlistener = new GroupLocationListener(this);
    }

    private ServiceConnection connection = new ServiceConnection() {

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            LocationService.LocationBinder binder = (LocationService.LocationBinder) service;
            mService = binder.getService();

            if (!checkFineLocationPermission() || !checkCoarseLocationPermission()) {
                Log.v(TAG, "Did not get permission for location update");
                Toast.makeText(getApplicationContext(), "Sorry!!! You did not give permissions", Toast.LENGTH_LONG);
                mService.stopService();
            } else {
                Log.v(TAG, "Requesting for location update");
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, (float) 0.5, mLocationListener);
            }

            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mLocationManager.removeUpdates(mLocationListener);
            mLocationListener = null;
            mBound = false;
        }
    };


    @Override
    protected void onStart() {
        super.onStart();
    }

    private Intent getServiceIntent() {
        Intent serviceIntent = new Intent(MapsActivity.this, LocationService.class);
        return serviceIntent;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        markerHashMap = new HashMap<>();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adjustMap();
            }
        }, 1000);
    }

    public void adjustMap(View view) {
        adjustMap();
    }

    //need to adjust map
    private void adjustMap() {
        if(markerHashMap.size() < 1) return;

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker: markerHashMap.values()) {
            builder.include(marker.getPosition());
        }
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(builder.build(), 10);
        mMap.animateCamera(cameraUpdate);
    }

    public void updateMap(User user) {
        int hashMapPrevSize = markerHashMap.size();
        Location userLocation = user.getLocation();
        LatLng latLng = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
        if(markerHashMap.containsKey(user.getUserId())) {
            Marker prevMarker = markerHashMap.get(user.getUserId());
            prevMarker.remove();
        }
        Marker newMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(user.getUserId()));
        markerHashMap.put(user.getUserId(), newMarker);

        if(markerHashMap.size() != hashMapPrevSize) {
            adjustMap();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean checkFineLocationPermission() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean checkCoarseLocationPermission() {
        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    private void requestFineLocationPermission() {
        ActivityCompat.requestPermissions(MapsActivity.this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_FINE_LOCATION);
    }

    private void requestCoarseLocationPermission() {
        ActivityCompat.requestPermissions(MapsActivity.this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_FINE_LOCATION);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermissions() {
        if (!checkFineLocationPermission()) {
            requestFineLocationPermission();
        }

        if (!checkCoarseLocationPermission()) {
            requestCoarseLocationPermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_FINE_LOCATION:
                Toast.makeText(this, "Got Permission FINE LOCATION", Toast.LENGTH_LONG);
                break;
            case PERMISSION_COARSE_LOCATION:
                Toast.makeText(this, "Got Permission COARSE LOCATION", Toast.LENGTH_LONG);
                break;
        }
    }
}
