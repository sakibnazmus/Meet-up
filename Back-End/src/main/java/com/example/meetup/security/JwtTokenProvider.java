package com.example.meetup.security;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    public String generateToken(Authentication authentication) {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date expiryTime = new Date(now.getTime()+jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(userPrincipal.getId())
                .setIssuedAt(new Date())
                .setExpiration(expiryTime)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validate(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT Token");
            ex.printStackTrace();
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT Token");
            ex.printStackTrace();
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
            ex.printStackTrace();
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT Token");
            ex.printStackTrace();
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty");
            ex.printStackTrace();
        }

        return false;
    }
}
