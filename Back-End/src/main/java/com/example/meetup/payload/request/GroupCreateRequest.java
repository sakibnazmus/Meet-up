package com.example.meetup.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class GroupCreateRequest {

    @NotBlank
    private String name;
}
