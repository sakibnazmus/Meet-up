package com.example.meet_up;

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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

public class GroupListAdapter extends ArrayAdapter<String> implements ChildEventListener {

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
                        mapsIntent.putExtra(Const.INTENT_EXTRA_GROUP_NAME, group);
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

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        String groupName = dataSnapshot.getValue().toString();
        if(!groupList.contains(groupName)){
            Log.v("Group", "Child added " + groupName);
            groupList.add(groupName);
            notifyDataSetChanged();
        }
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
        String removedGroup = dataSnapshot.getValue().toString();
        Log.v("Group", "Child removed " + removedGroup);
        for(String groupName: groupList) {
            if(groupName.equals(removedGroup)) {
                Log.v("Group", "Removing child " + groupName);
                groupList.remove(groupName);
                notifyDataSetChanged();
                break;
            }
        }
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

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
