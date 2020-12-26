package com.example.meetup.service;

import com.example.meetup.model.User;
import com.example.meetup.payload.request.GoogleLoginRequest;
import com.example.meetup.payload.request.SignUpRequest;
import com.example.meetup.payload.response.AuthenticationResponse;
import com.example.meetup.payload.request.EmailLoginRequest;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface AuthenticationService {
    AuthenticationResponse authenticateUser(EmailLoginRequest loginRequest);

    AuthenticationResponse authenticateUser(GoogleLoginRequest loginRequest);
    boolean checkEmailAvailable(String email);
    User registerUser(SignUpRequest signUpRequest);
}
