package com.example.meetup.service;

import com.example.meetup.model.Group;
import com.example.meetup.payload.request.GroupCreateRequest;
import com.example.meetup.payload.response.ApiResponse;
import com.example.meetup.payload.response.GroupDetailsResponse;
import com.example.meetup.payload.response.GroupSummaryResponse;
import com.example.meetup.security.UserPrincipal;

import java.util.List;

public interface GroupService {

    List<Group> getAllGroups();
    List<GroupSummaryResponse> getGroupsUserIn(UserPrincipal currentUser);
    GroupDetailsResponse getGroupById(UserPrincipal currentUser, String groupId);
    ApiResponse createGroup(UserPrincipal creatorPrincipal, GroupCreateRequest request);
}
