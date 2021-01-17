package com.example.meetup.service;

import com.example.meetup.payload.request.UserLocationRequest;
import com.example.meetup.payload.response.ApiResponse;
import com.example.meetup.payload.response.GroupLocationResponse;
import com.example.meetup.security.UserPrincipal;

public interface LocationUpdaterService {

    ApiResponse updateUserLocation(UserPrincipal currentUser, UserLocationRequest locationRequest);
    GroupLocationResponse getLocationsForGroup(String groupId);
}
