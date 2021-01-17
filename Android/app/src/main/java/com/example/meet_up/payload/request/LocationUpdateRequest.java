package com.example.meet_up.payload.request;

import com.example.meet_up.model.UserLocation;

public class LocationUpdateRequest {

    private double latitude;
    private double longitude;

    public LocationUpdateRequest(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LocationUpdateRequest(UserLocation location) {
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
