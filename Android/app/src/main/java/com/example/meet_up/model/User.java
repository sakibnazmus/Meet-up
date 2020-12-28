package com.example.meet_up.model;

import com.example.meet_up.view_model.IndividualLocationListener;

public class User {

    private String userId;
    private String email;
    private UserLocation userLocation;
    private String username;
    private boolean enabled;

    public User (String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public UserLocation getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(float latitude, float longitude) {
        userLocation = new UserLocation(latitude, longitude);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
