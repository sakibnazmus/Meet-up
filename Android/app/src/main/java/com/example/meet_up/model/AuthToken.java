package com.example.meet_up.model;

public class AuthToken {

    private String tokenType;
    private String accessToken;

    public AuthToken(String tokenType, String accessToken) {
        this.tokenType = tokenType;
        this.accessToken = accessToken;
    }

    public String getAuthToken() {
        return tokenType + " " + accessToken;
    }
}
