package com.example.meetup.service;

import com.example.meetup.exception.ResourceNotFoundException;
import com.example.meetup.mapper.LocationMapper;
import com.example.meetup.model.Group;
import com.example.meetup.model.User;
import com.example.meetup.model.UserLocation;
import com.example.meetup.payload.request.UserLocationRequest;
import com.example.meetup.payload.response.ApiResponse;
import com.example.meetup.payload.response.GroupLocationResponse;
import com.example.meetup.repository.GroupRepository;
import com.example.meetup.repository.UserRepository;
import com.example.meetup.security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationUpdaterServiceImpl implements LocationUpdaterService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    private static final Logger logger = LoggerFactory.getLogger(LocationUpdaterServiceImpl.class);

    @Override
    public ApiResponse updateUserLocation(UserPrincipal currentUser, UserLocationRequest locationRequest) {
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", currentUser.getId()));

        logger.info("Updating location for user: " + user.getName());

        UserLocation location = LocationMapper.INSTANCE.locationRequestToLocation(locationRequest);
        user.setLocation(location);
        userRepository.save(user);

        return new ApiResponse(true, "Location updated successfully");
    }

    @Override
    public GroupLocationResponse getLocationsForGroup(String groupId) {
        Group group = groupRepository.findUsersById(groupId);
        group.getMembers().forEach(user -> logger.info(user.getName()));
        return LocationMapper.INSTANCE.groupToGroupLocationResponse(group);
    }
}
