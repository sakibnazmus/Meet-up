package com.example.meetup.mapper;

import com.example.meetup.model.Group;
import com.example.meetup.model.User;
import com.example.meetup.model.UserLocation;
import com.example.meetup.payload.request.UserLocationRequest;
import com.example.meetup.payload.response.GroupLocationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.HashMap;
import java.util.Set;
import java.util.Map;

@Mapper
public interface LocationMapper {

    LocationMapper INSTANCE = Mappers.getMapper(LocationMapper.class);

    UserLocation locationRequestToLocation(UserLocationRequest request);

    @Mapping(source = "members", target = "locationMap", qualifiedByName = "getUserLocationFromMembers")
    GroupLocationResponse groupToGroupLocationResponse(Group group);

    @Named("getUserLocationFromMembers")
    default Map<String, UserLocation> getUserLocationFromMembers(Set<User> members) {
        Map<String, UserLocation> userLocationMap = new HashMap<>();
        members.stream().map(user -> userLocationMap.put(user.getId(), user.getLocation()));
        return userLocationMap;
    }
}
