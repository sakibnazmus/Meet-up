package com.example.meet_up.view_model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.meet_up.model.BasicUserInfo;
import com.example.meet_up.service.UserService;

public class UserProfileViewModel extends ViewModel {

    private UserService userService;

    public UserProfileViewModel() {
        this.userService = UserService.getInstance();
    }

    private LiveData<BasicUserInfo> getCurrentUserInfo() {
        return userService.getUserInfo();
    }
}