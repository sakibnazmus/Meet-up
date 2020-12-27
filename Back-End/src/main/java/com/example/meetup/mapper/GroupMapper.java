package com.example.meetup.mapper;

import com.example.meetup.model.Group;
import com.example.meetup.model.User;
import com.example.meetup.payload.response.GroupDetailsResponse;
import com.example.meetup.payload.response.GroupSummaryResponse;
import com.example.meetup.payload.response.UserInGroupResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface GroupMapper {

    GroupMapper INSTANCE = Mappers.getMapper(GroupMapper.class);

    @Mappings({
            @Mapping(target = "members", source = "group", qualifiedByName = "getMembersInResponseList"),
            @Mapping(target = "admin", source = "admin", qualifiedByName = "getUserInGroupResponseFromUser")
    })
    GroupDetailsResponse groupToGroupDetailsResponse(Group group);

    @Mapping(target = "name", source = "groupName")
    GroupSummaryResponse groupToGroupSummaryResponse(Group group);

    @Named("getMembersInResponseList")
    default Set<UserInGroupResponse> getMembersInResponseList(Group group) {
        return group.getMembers().stream()
                .map(this::getUserInGroupResponseFromUser)
                .collect(Collectors.toSet());
    }

    @Named("getUserInGroupResponseFromUser")
    default UserInGroupResponse getUserInGroupResponseFromUser(User user) {
        return UserInGroupResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
