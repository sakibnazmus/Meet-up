package com.example.meet_up.view_model;

import android.app.Application;
import android.content.Context;
import android.view.View;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.meet_up.model.AuthToken;
import com.example.meet_up.model.BasicUserInfo;
import com.example.meet_up.payload.request.GroupCreateRequest;
import com.example.meet_up.payload.response.GroupSummaryResponse;
import com.example.meet_up.service.AuthService;
import com.example.meet_up.service.GroupService;
import com.example.meet_up.service.UserService;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private Context mContext;

    public String welcomeText;

    public HomeViewModel(Application application) {
        super(application);

        mContext = getApplication().getApplicationContext();

        UserService.getInstance(mContext).getUserInfo().observeForever(basicUserInfo -> {
           if (basicUserInfo != null) {
               welcomeText = "Welcome " + basicUserInfo.getName();
           }
        });
    }

    private LiveData<BasicUserInfo> getCurrentUserInfo() {
        return UserService.getInstance(mContext).getUserInfo();
    }

    public LiveData<AuthToken> getAuthToken(Context context) {
        return AuthService.getInstance(context).getAuthToken();
    }

    public void signOut(View view) {
        AuthService.getInstance(view.getContext()).signOut();
    }

    public LiveData<String> createNewGroup(String groupName) {
        return GroupService.getInstance(mContext).createNewGroup(new GroupCreateRequest(groupName));
    }

    public LiveData<List<GroupSummaryResponse>> getGroupList() {
        return GroupService.getInstance(mContext).getGroupList();
    }
}