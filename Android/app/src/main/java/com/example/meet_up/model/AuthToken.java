package com.example.meet_up.model;

public class AuthToken {

    private String tokenType;
    private String accessToken;

    public AuthToken(String tokenType, String accessToken) {
        this.tokenType = tokenType;
        this.accessToken = accessToken;
    }

    public AuthToken(String authToken) {
        String[] tokens = authToken.split(" ");
        this.tokenType = tokens[0];
        this.accessToken = tokens[1];
    }

    public String getAuthToken() {
        return tokenType + " " + accessToken;
    }
}
