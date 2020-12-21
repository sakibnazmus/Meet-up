package com.example.meet_up;

import com.example.meet_up.model.User;
import com.example.meet_up.ui.MapsActivity;

public class IndividualLocationListener {

    private final static String TAG = "LocationUpdater";
    private User user;
    private MapsActivity mapsActivity;
    public IndividualLocationListener(User user, MapsActivity mapsActivity) {
        this.user = user;
        this.mapsActivity = mapsActivity;
    }
}
