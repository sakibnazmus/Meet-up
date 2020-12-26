package com.example.meetup.security.oauth2.user;

import com.example.meetup.security.UserPrincipal;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class OAuth2Authentication extends AbstractAuthenticationToken {

    private UserPrincipal principal;

    public OAuth2Authentication(UserPrincipal principal) {
        super(principal.getAuthorities());
        this.principal = principal;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
