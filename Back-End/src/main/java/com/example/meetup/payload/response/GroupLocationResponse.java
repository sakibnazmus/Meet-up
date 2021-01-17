package com.example.meetup.payload.response;

import com.example.meetup.model.UserLocation;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter @Setter
public class GroupLocationResponse {

    private UserLocation destination;

    @JsonProperty("location_map")
    private Map<String, UserLocation> locationMap;
}
