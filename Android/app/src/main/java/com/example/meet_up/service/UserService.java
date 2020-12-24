package com.example.meet_up.service;

import androidx.lifecycle.MutableLiveData;

import com.example.meet_up.model.BasicUserInfo;

public class UserService {

    private static UserService mInstance;
    private MutableLiveData<BasicUserInfo> mUserInfo;

    public UserService() {
        mUserInfo = new MutableLiveData<>();
    }

    public void setUserInfo(BasicUserInfo userInfo) {
        mUserInfo.setValue(userInfo);
    }

    public MutableLiveData<BasicUserInfo> getUserInfo() {
        return mUserInfo;
    }

    public static UserService getInstance() {
        if (mInstance == null) mInstance = new UserService();
        return mInstance;
    }
}
