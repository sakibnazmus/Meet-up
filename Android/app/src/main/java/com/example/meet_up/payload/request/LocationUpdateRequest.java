package com.example.meet_up.payload.request;

public class LocationUpdateRequest {

    private double latitude;
    private double longitude;

    public LocationUpdateRequest(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
