package com.example.meetup.controller;

import com.example.meetup.model.User;
import com.example.meetup.payload.request.EmailLoginRequest;
import com.example.meetup.payload.request.OAuth2LoginRequest;
import com.example.meetup.payload.request.SignUpRequest;
import com.example.meetup.payload.response.ApiResponse;
import com.example.meetup.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationService authenticationService;
    private final static Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login/email")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody EmailLoginRequest loginRequest) {
        return ResponseEntity.ok(authenticationService.authenticateUser(loginRequest));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if(!authenticationService.checkEmailAvailable(signUpRequest.getEmail()))
            return ResponseEntity.badRequest().body(
                    new ApiResponse(false, "Email is already taken"));

        User registeredUser = authenticationService.registerUser(signUpRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{id}")
                .buildAndExpand(registeredUser.getId()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(
                true,
                "User Registered Successfully!!!"
        ));
    }

    @PostMapping("/oauth2/google")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody OAuth2LoginRequest loginRequest) {
        return ResponseEntity.ok(authenticationService.authenticateUser(loginRequest));
    }

}
