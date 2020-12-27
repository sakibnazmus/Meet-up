package com.example.meetup.payload.response;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInGroupResponse {

    private String id;
    private String name;
    private String email;
}
