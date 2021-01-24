package com.example.meet_up.view;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.util.Pair;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.meet_up.databinding.ActivityMapsBinding;
import com.example.meet_up.model.BasicUserInfo;
import com.example.meet_up.model.UserLocation;
import com.example.meet_up.util.Constants;
import com.example.meet_up.R;
import com.example.meet_up.view_model.MapsViewModel;
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

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {

    private static final String TAG = "MapsActivity";

    private MapsViewModel mViewModel;
    private GoogleMap mMap;
    private Handler mHandler;

    private final static int PERMISSION_FINE_LOCATION = 1;
    private final static int PERMISSION_COARSE_LOCATION = 2;

    private final static int TOTAL_NEEDED_PERMISSIONS = 2;

    private final static int TIME_DELAY_AFTER_MAP_READY = 1000;

    private String selectedGroupId;
    private String selectedGroupName;

    private int acquiredPermissions = 0;

    private HashMap<String, Marker> mMarkerHashMap;
    private UserLocation mUserUserLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMapsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_maps);
        mViewModel = new ViewModelProvider(this).get(MapsViewModel.class);
        binding.setViewModel(mViewModel);
        binding.setLifecycleOwner(this);

        checkPermissions();
        if (acquiredPermissions == 2) {
            mViewModel.startService();
        }

        selectedGroupId = getIntent().getStringExtra(Constants.INTENT_EXTRA_GROUP_ID);
        selectedGroupName = getIntent().getStringExtra(Constants.INTENT_EXTRA_GROUP_NAME);

        mHandler = new Handler();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
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
        Log.v(TAG, "onMapReady");
        mMap = googleMap;
        mMap.setOnMapLoadedCallback(this);
        mMarkerHashMap = new HashMap<>();
        mHandler.postDelayed(this::adjustMap, TIME_DELAY_AFTER_MAP_READY);
    }

    @Override
    public void onMapLoaded() {
        mViewModel.getUserLocation().observe(this, getCurrentLocationObserver());
    }

    public void updateMap(BasicUserInfo basicInfo, UserLocation userLocation) {
        Log.v(TAG, "User: " + basicInfo.getName() +"\n" + "userLocation: " + userLocation.toString());
        if (userLocation.isInvalid()) return;
        int hashMapPrevSize = mMarkerHashMap.size();
        LatLng latLng = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
        if(mMarkerHashMap.containsKey(basicInfo.getUserId())) {
            Marker prevMarker = mMarkerHashMap.get(basicInfo.getUserId());
            prevMarker.remove();
        }
        Marker newMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(basicInfo.getName()));
        mMarkerHashMap.put(basicInfo.getUserId(), newMarker);

        if(mMarkerHashMap.size() != hashMapPrevSize) {
            adjustMap();
        }
    }

    private void adjustMap() {
        if(mMarkerHashMap.size() < 1) return;

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker: mMarkerHashMap.values()) {
            builder.include(marker.getPosition());
        }
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(builder.build(), 10);
        mMap.animateCamera(cameraUpdate);
    }

    private Observer<Pair<BasicUserInfo, UserLocation>> getCurrentLocationObserver() {
        return pair -> {
            if (pair.second != null) {
                mUserUserLocation = pair.second;
                updateMap(pair.first, mUserUserLocation);
                adjustMap();
            }
        };
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean checkFineLocationPermission() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        acquiredPermissions++;
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean checkCoarseLocationPermission() {
        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        acquiredPermissions++;
        return true;
    }

    private void requestFineLocationPermission() {
        ActivityCompat.requestPermissions(MapsActivity.this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_FINE_LOCATION);
    }

    private void requestCoarseLocationPermission() {
        ActivityCompat.requestPermissions(MapsActivity.this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_COARSE_LOCATION);
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
                acquiredPermissions++;
                break;
            case PERMISSION_COARSE_LOCATION:
                acquiredPermissions++;
                Toast.makeText(this, "Got Permission COARSE LOCATION", Toast.LENGTH_LONG);
                break;
        }

        if (acquiredPermissions == TOTAL_NEEDED_PERMISSIONS) {
            mViewModel.startService();
        }
    }
}
