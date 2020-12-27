package com.example.meetup.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter @Setter
public class GroupDetailsResponse {

    private String id;

    @JsonProperty("group_name")
    private String groupName;

    private Set<UserInGroupResponse> members;

    private UserInGroupResponse admin;
}
