package com.example.meetup.service;

import com.example.meetup.exception.ResourceNotFoundException;
import com.example.meetup.mapper.GroupMapper;
import com.example.meetup.model.Group;
import com.example.meetup.model.User;
import com.example.meetup.payload.request.GroupCreateRequest;
import com.example.meetup.payload.response.ApiResponse;
import com.example.meetup.payload.response.GroupDetailsResponse;
import com.example.meetup.payload.response.GroupSummaryResponse;
import com.example.meetup.repository.GroupRepository;
import com.example.meetup.repository.UserRepository;
import com.example.meetup.security.UserPrincipal;
import org.mapstruct.ap.internal.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    private static final Logger logger = LoggerFactory.getLogger(GroupServiceImpl.class);

    @Override
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    @Override
    public List<GroupSummaryResponse> getGroupsUserIn(UserPrincipal currentUser) {
        List<Group> groups = groupRepository.findAllByMembers(currentUser.getId());
        return groups.stream()
                .map(GroupMapper.INSTANCE::groupToGroupSummaryResponse)
                .collect(Collectors.toList());
    }

    @Override
    public GroupDetailsResponse getGroupById(UserPrincipal currentUser, String groupId) {
        Group receivedGroup = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group", "groupId", groupId));
        return GroupMapper.INSTANCE.groupToGroupDetailsResponse(receivedGroup);
    }

    @Override
    @Transactional
    public ApiResponse createGroup(UserPrincipal creatorPrincipal, GroupCreateRequest request) {
        if (groupRepository.existsByGroupName(request.getName())) {
            return new ApiResponse(false, "Sorry! Please try another name");
        }

        User creator = userRepository.findById(creatorPrincipal.getId()).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", creatorPrincipal.getId())
        );

        Group newGroup = Group.builder()
                .groupName(request.getName())
                .members(Collections.asSet(creator))
                .admin(creator)
                .build();

        groupRepository.save(newGroup);
        return new ApiResponse(true, "Group created successfully!");
    }
}
