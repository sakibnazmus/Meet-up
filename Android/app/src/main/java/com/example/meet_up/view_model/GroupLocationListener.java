package com.example.meet_up;

import com.example.meet_up.model.User;
import com.example.meet_up.ui.MapsActivity;

import java.util.HashMap;

public class GroupLocationListener {

    private final static String TAG = "GroupLocationListener";
    private MapsActivity mapsActivity;

    private HashMap<String, User> users;

    public GroupLocationListener(MapsActivity activity) {
        mapsActivity = activity;

        users = new HashMap<>();
    }

    public HashMap<String, User> getUsers() {
        return users;
    }
}
