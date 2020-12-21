package com.example.meetup.repository;

import com.example.meetup.model.Role;
import com.example.meetup.model.RoleName;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {

    Optional<Role> findByName(RoleName roleName);
}
