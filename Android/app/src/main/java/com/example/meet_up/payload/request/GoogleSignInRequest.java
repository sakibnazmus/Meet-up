package com.example.meet_up.payload.request;

public class GoogleSignInRequest {

    public String tokenId;
    public Long clientTime;

    public GoogleSignInRequest(String tokenId) {
        this.tokenId = tokenId;
        clientTime = System.currentTimeMillis();
    }

    public String getTokenId() {
        return tokenId;
    }

    public Long getClientTime() {
        return clientTime;
    }
}
