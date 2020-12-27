package com.example.meetup.model.audit;

import com.example.meetup.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Getter @Setter
public class UserDateAudit extends DateAudit {

    @CreatedBy
    @DBRef(lazy = true)
    private User createdBy;
}
