package com.example.meetup.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.TYPE})
@Documented
@AuthenticationPrincipal
public @interface CurrentUser {
}
