package com.example.meetup.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserLocationRequest {

    private Double latitude;
    private Double longitude;
}
