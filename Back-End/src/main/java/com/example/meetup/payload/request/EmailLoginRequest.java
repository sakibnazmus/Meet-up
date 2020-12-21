package com.example.meetup.payload.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter @Setter
public class EmailLoginRequest {

    @NotBlank
    @Email
    @JsonAlias("email")
    private String email;

    @NotBlank
    private String password;
}
