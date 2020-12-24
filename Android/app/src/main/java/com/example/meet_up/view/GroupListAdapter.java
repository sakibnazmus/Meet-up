package com.example.meet_up.view;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.meet_up.util.Constants;
import com.example.meet_up.R;

import java.util.ArrayList;

public class GroupListAdapter extends ArrayAdapter<String> {

    private class GroupNameHolder {
        Button groupNameBtn;

        public GroupNameHolder(Button button) {
            groupNameBtn = button;
            groupNameBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String group = groupNameBtn.getText().toString();
                    if(group != null) {
                        Intent mapsIntent = new Intent(mActivity, MapsActivity.class);
                        mapsIntent.putExtra(Constants.INTENT_EXTRA_GROUP_NAME, group);
                        mActivity.startActivity(mapsIntent);
                        mActivity.finishActivity(0);
                    }
                }
            });
        }
    }

    private Activity mActivity;
    private ArrayList<String> groupList;
    private LayoutInflater mInflater;

    public GroupListAdapter(@NonNull Activity activity, int resource, ArrayList<String> groups) {
        super(activity, resource, groups);
        Log.v("Group", "Created group list adapter");
        mActivity = activity;
        groupList = groups;
        mInflater = mActivity.getLayoutInflater();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Log.v("Group", "GetView is called");
        GroupNameHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.group_list_view, parent, false);
            holder = new GroupNameHolder((Button) convertView.findViewById(R.id.group_name));
            convertView.setTag(holder);
        } else {
            holder = (GroupNameHolder) convertView.getTag();
        }

        String groupName = groupList.get(position);
        holder.groupNameBtn.setText(groupName);

        return convertView;
    }
}
