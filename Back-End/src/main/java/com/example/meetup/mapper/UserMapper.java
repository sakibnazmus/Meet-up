package com.example.meetup.mapper;

import com.example.meetup.model.Role;
import com.example.meetup.model.User;
import com.example.meetup.payload.request.SignUpRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Set;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "roles", source = "roles")
    User signUpRequestToUser(SignUpRequest signUpRequest, Set<Role> roles);
}
