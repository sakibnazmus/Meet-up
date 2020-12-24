package com.example.meet_up.model;

import com.example.meet_up.view_model.IndividualLocationListener;

public class User {

    private String userId;
    private String email;
    private Location location;
    private String username;
    private boolean enabled;
    private IndividualLocationListener locationUpdater;

    public User (String userId) {
        this.userId = userId;
        this.location = new Location();
    }

    public String getUserId() {
        return userId;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(float latitude, float longitude) {
        location.setLatitude(latitude);
        location.setLongitude(longitude);
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

    public void setLocationUpdater(IndividualLocationListener locationUpdater) {
        this.locationUpdater = locationUpdater;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public IndividualLocationListener getLocationUpdater() {
        return locationUpdater;
    }
}
