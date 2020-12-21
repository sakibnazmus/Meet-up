package com.example.meetup.service;

import com.example.meetup.model.User;
import com.example.meetup.payload.request.OAuth2LoginRequest;
import com.example.meetup.payload.request.SignUpRequest;
import com.example.meetup.payload.response.AuthenticationResponse;
import com.example.meetup.payload.request.EmailLoginRequest;
import org.springframework.security.authentication.AuthenticationManager;

public interface AuthenticationService {
    AuthenticationResponse authenticateUser(EmailLoginRequest loginRequest);

    AuthenticationManager authenticateUser(OAuth2LoginRequest loginRequest);
    boolean checkEmailAvailable(String email);
    User registerUser(SignUpRequest signUpRequest);
}
