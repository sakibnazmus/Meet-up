package com.example.meet_up.payload.request;

public class GoogleSignInRequest {

    public String registrationId;

    public GoogleSignInRequest(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getRegistrationId() {
        return registrationId;
    }
}
