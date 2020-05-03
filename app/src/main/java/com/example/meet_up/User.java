package com.example.meet_up;

public class User {

    class Location {
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
    private String email;
    private String uid;
    private Location location;
    private String username;
    private boolean enabled;
    private IndividualLocationListener locationUpdater;

    public User (String uid) {
        this.uid = uid;
        this.location = new Location();
    }

    public String getUid() {
        return uid;
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
