package com.example.meetup.model;

import com.example.meetup.model.audit.UserDateAudit;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.util.Set;
import java.util.HashSet;

@Document(collection = "groups")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Group extends UserDateAudit {

    @Id
    private String id;

    @NotBlank
    private String groupName;

    @DBRef(lazy = true)
    @Setter(AccessLevel.PRIVATE)
    private Set<User> members;

    @DBRef(lazy = true)
    private User admin;

    public void addMember(User member) {
        members.add(member);
    }
}
