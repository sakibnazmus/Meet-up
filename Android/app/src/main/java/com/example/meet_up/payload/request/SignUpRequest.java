package com.example.meet_up.payload.request;

public class SignUpRequest {

    private String email;
    private String name;
    private String password;

    public SignUpRequest(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
