package com.example.meetup.repository;

import com.example.meetup.model.Group;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends MongoRepository<Group, String> {

    Boolean existsByGroupName(String name);
    List<Group> findAll();
    List<Group> findAllByMembers(String memberId);
    List<Group> findByAdmin(String adminId);
}
