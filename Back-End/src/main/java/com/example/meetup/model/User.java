package com.example.meetup.model;

import com.example.meetup.model.audit.DateAudit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.HashSet;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "users")
public class User extends DateAudit {

    @Id
    private String id;

    @NotBlank
    private String name;

    @NotBlank
    @Email
    @Indexed(unique = true)
    private String email;

    @JsonIgnore
    private String password;

    @DBRef
    private Set<Role> roles = new HashSet<>();

    private UserLocation location;

    @NotNull
    private AuthProvider provider;

    private String providerId;
}
