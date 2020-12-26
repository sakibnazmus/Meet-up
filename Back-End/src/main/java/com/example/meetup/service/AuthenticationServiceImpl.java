package com.example.meetup.service;

import com.example.meetup.exception.AppException;
import com.example.meetup.mapper.UserMapper;
import com.example.meetup.model.AuthProvider;
import com.example.meetup.model.RoleName;
import com.example.meetup.model.User;
import com.example.meetup.payload.request.EmailLoginRequest;
import com.example.meetup.payload.request.GoogleLoginRequest;
import com.example.meetup.payload.request.SignUpRequest;
import com.example.meetup.payload.response.AuthenticationResponse;
import com.example.meetup.repository.RoleRepository;
import com.example.meetup.repository.UserRepository;
import com.example.meetup.security.JwtTokenProvider;
import com.example.meetup.security.UserPrincipal;
import com.example.meetup.security.oauth2.user.OAuth2Authentication;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Clock;
import com.google.api.client.util.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Optional;

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

    private final HttpTransport transport = new NetHttpTransport();
    private final JsonFactory jsonFactory = new JacksonFactory();

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String CLIENT_ID;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

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
        return new AuthenticationResponse(jwt, user.getId(), user.getUsername(), user.getName());
    }

    @Override
    public AuthenticationResponse authenticateUser(GoogleLoginRequest loginRequest) {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singleton(CLIENT_ID))
                .setIssuer("https://accounts.google.com")
                .build();
        GoogleIdToken idToken = null;
        try {
            String tokenId = loginRequest.getTokenId();

            idToken = verifier.verify(tokenId);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                String email = payload.getEmail();
                Optional<User> userOptional = userRepository.findByEmail(email);
                User user = null;
                if (!userOptional.isPresent()) {
                    String name = (String) payload.get("name");
                    user = User.builder()
                            .email(email)
                            .name(name)
                            .provider(AuthProvider.GOOGLE)
                            .providerId(payload.getJwtId())
                            .build();
                    user = createNewUser(user);
                } else {
                    user = userOptional.get();
                }
                UserPrincipal userPrincipal = UserPrincipal.create(user);
                Authentication authentication = new OAuth2Authentication(userPrincipal);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                String jwt = tokenProvider.generateToken(authentication);
                return new AuthenticationResponse(jwt, user.getId(), user.getEmail(), user.getName());
            } else {
                logger.error("Verification failed: idToken is null");
            }
        } catch (GeneralSecurityException | IOException e) {
            logger.error(e.getLocalizedMessage());
            e.printStackTrace();
        }
        throw new BadCredentialsException("Invalid auth token");
    }

    @Override
    public boolean checkEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }

    @Override
    public User registerUser(SignUpRequest signUpRequest) {
        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());
        signUpRequest.setPassword(encodedPassword);

        User user = UserMapper.INSTANCE.signUpRequestToUser(signUpRequest);
        user.setProvider(AuthProvider.NATIVE);

        return createNewUser(user);
    }

    private User createNewUser(User user) {
        user.setRoles(Collections.singleton(roleRepository.findByName(RoleName.ROLE_USER).
                orElseThrow(() -> new AppException("User role not set"))));
        return userRepository.save(user);
    }
}