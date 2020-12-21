package com.example.meetup.service;

import com.example.meetup.exception.AppException;
import com.example.meetup.mapper.UserMapper;
import com.example.meetup.model.RoleName;
import com.example.meetup.model.User;
import com.example.meetup.payload.request.EmailLoginRequest;
import com.example.meetup.payload.request.OAuth2LoginRequest;
import com.example.meetup.payload.request.SignUpRequest;
import com.example.meetup.payload.response.AuthenticationResponse;
import com.example.meetup.repository.RoleRepository;
import com.example.meetup.repository.UserRepository;
import com.example.meetup.security.JwtTokenProvider;
import com.example.meetup.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public AuthenticationResponse authenticateUser(EmailLoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();

        String jwt = tokenProvider.generateToken(authentication);
        return new AuthenticationResponse(jwt, user.getName());
    }

    @Override
    public AuthenticationManager authenticateUser(OAuth2LoginRequest loginRequest) {
        return null;
    }

    @Override
    public boolean checkEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }

    @Override
    public User registerUser(SignUpRequest signUpRequest) {
        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());
        signUpRequest.setPassword(encodedPassword);

        User user = UserMapper.INSTANCE.signUpRequestToUser(signUpRequest,
                Collections.singleton(roleRepository.findByName(RoleName.ROLE_USER)
                        .orElseThrow(() -> new AppException("User role not set"))));

        return userRepository.save(user);
    }
}
