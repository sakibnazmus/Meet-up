package com.example.meetup.payload.response;

import lombok.Getter;

@Getter
public class AuthenticationResponse {

    private String accessToken;
    private String tokenType;
    private String userId;
    private String email;
    private String name;

    public AuthenticationResponse(String accessToken, String userId, String email, String name) {
        this.accessToken = accessToken;
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.tokenType = "Bearer";
    }
}
