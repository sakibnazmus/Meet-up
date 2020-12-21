package com.example.meetup.payload.response;

import lombok.Getter;

@Getter
public class AuthenticationResponse {

    private String accessToken;
    private String tokenType;
    private String name;

    public AuthenticationResponse(String accessToken, String name) {
        this.accessToken = accessToken;
        this.name = name;
        this.tokenType = "Bearer";
    }
}
