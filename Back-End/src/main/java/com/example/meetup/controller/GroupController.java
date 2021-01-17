package com.example.meetup.controller;

import com.example.meetup.payload.request.GroupCreateRequest;
import com.example.meetup.security.CurrentUser;
import com.example.meetup.security.UserPrincipal;
import com.example.meetup.service.GroupService;
import com.example.meetup.service.LocationUpdaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private LocationUpdaterService locationService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllGroups() {
        return ResponseEntity.ok(groupService.getAllGroups());
    }

    @GetMapping({"", "/"})
    public ResponseEntity<?> getGroupsForUser(@CurrentUser UserPrincipal currentUser) {
        return ResponseEntity.ok(groupService.getGroupsUserIn(currentUser));
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<?> getGroupById(@CurrentUser UserPrincipal currentUser,
                                          @PathVariable String groupId) {
        return ResponseEntity.ok(groupService.getGroupById(currentUser, groupId));
    }

    @PostMapping("/new")
    public ResponseEntity<?> createNewGroup(@CurrentUser UserPrincipal currentUser,
                                            @RequestBody GroupCreateRequest request) {
        return ResponseEntity.ok(groupService.createGroup(currentUser, request));
    }

    @GetMapping("/{groupId}/locations")
    public ResponseEntity<?> getLocationsForGroup(@CurrentUser UserPrincipal currentUser,
                                                  @PathVariable String groupId) {
        return ResponseEntity.ok(locationService.getLocationsForGroup(groupId));
    }
}
