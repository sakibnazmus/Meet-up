package com.example.meetup.controller;

import com.example.meetup.model.UserLocation;
import com.example.meetup.payload.request.UserLocationRequest;
import com.example.meetup.security.CurrentUser;
import com.example.meetup.security.UserPrincipal;
import com.example.meetup.service.LocationUpdaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/user")
@RestController
public class UserController {

    @Autowired
    private LocationUpdaterService locationUpdaterService;

    @PostMapping("/location/update")
    public ResponseEntity<?> updateUserLocation(@CurrentUser UserPrincipal currentUser,
                                                @RequestBody UserLocationRequest locationUpdateRequest) {

        return ResponseEntity.ok(locationUpdaterService.updateUserLocation(currentUser, locationUpdateRequest));
    }
}
