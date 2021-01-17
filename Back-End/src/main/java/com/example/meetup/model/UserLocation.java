package com.example.meetup.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserLocation {

    private Double latitude;
    private Double longitude;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserLocation location = (UserLocation) o;

        if (!latitude.equals(location.latitude)) return false;
        return longitude.equals(location.longitude);
    }
}
