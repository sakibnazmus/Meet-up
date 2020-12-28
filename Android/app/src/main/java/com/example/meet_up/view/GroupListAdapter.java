package com.example.meet_up.view;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.example.meet_up.payload.response.GroupSummaryResponse;
import com.example.meet_up.util.Constants;
import com.example.meet_up.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupListAdapter extends BaseAdapter implements Observer<List<GroupSummaryResponse>> {

    private class GroupNameHolder {
        String groupId;
        String groupName;
        Button groupNameBtn;

        public GroupNameHolder(Button button) {
            groupNameBtn = button;
            groupNameBtn.setOnClickListener(view -> {
                Intent mapsIntent = new Intent(mActivity, MapsActivity.class);
                mapsIntent.putExtra(Constants.INTENT_EXTRA_GROUP_ID, groupId);
                mapsIntent.putExtra(Constants.INTENT_EXTRA_GROUP_NAME, groupName);
                mActivity.startActivity(mapsIntent);
                mActivity.finishAfterTransition();
            });
        }

        public void setGroup(String groupId, String groupName) {
            this.groupId = groupId;
            this.groupName = groupName;
            groupNameBtn.setText(groupName);
        }
    }

    private static final String TAG = GroupListAdapter.class.getSimpleName();

    private Activity mActivity;
    private List<String> idList;
    private Map<String, String> groupMap;
    private LayoutInflater mInflater;

    public GroupListAdapter(@NonNull Activity activity, int resource) {
        super();
        mActivity = activity;
        groupMap = new HashMap<>();
        idList = new ArrayList<>();
        mInflater = mActivity.getLayoutInflater();
        mInflater.inflate(resource, null);
    }

    @Override
    public void onChanged(List<GroupSummaryResponse> groupSummaryResponses) {
        if (groupSummaryResponses != null && groupSummaryResponses.size() > 0) {
            for(GroupSummaryResponse response: groupSummaryResponses) {
                if (!groupMap.containsKey(response.getId())) {
                    idList.add(response.getId());
                    groupMap.put(response.getId(), response.getName());
                }
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return idList.size();
    }

    @Override
    public String getItem(int i) {
        return groupMap.get(idList.get(i));
    }

    @Override
    public long getItemId(int i) {
        return -1;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        GroupNameHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.group_list_view, parent, false);
            holder = new GroupNameHolder((Button) convertView.findViewById(R.id.group_name));
            convertView.setTag(holder);
        } else {
            holder = (GroupNameHolder) convertView.getTag();
        }

        String id = idList.get(position);
        holder.setGroup(id, groupMap.get(id));

        return convertView;
    }
}
