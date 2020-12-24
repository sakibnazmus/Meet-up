package com.example.meet_up.model;

public class Location {
    private double latitude = 91.0;
    private double longitude = 181.0;
    public Location(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location() {}

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isInvalid() {
        return this.latitude > 90 || this.longitude > 180;
    }
}