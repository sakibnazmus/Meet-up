package com.example.meetup.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;
import java.util.HashSet;

@Document(collection = "roles")
@Getter @Setter
@NoArgsConstructor
public class Role {

    @Id
    private String id;

    @Indexed(unique = true)
    private RoleName name;

    private Set<User> users = new HashSet<>();

    public Role(RoleName name) {
        this.name = name;
    }
}
