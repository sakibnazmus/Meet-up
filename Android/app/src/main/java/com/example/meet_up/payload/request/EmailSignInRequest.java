package com.example.meet_up.payload.request;

public class EmailSignInRequest {

    private String email;
    private String password;

    public EmailSignInRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
