package com.example.meet_up;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.MeetUpApplication;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private Button mSeeCurrentBtn;
    private Button mAddGroupBtn;
    private ListView mGroupsListView;
    private Button mSignOutBtn;

    private ProgressDialog loadingDialog;

    private FirebaseAuth mAuth;
    private DatabaseReference mGroupsReference;
    private DatabaseReference mUsersReference;

    private GroupListAdapter mGroupListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mSeeCurrentBtn = findViewById(R.id.see_current_btn);
        mAddGroupBtn = findViewById(R.id.add_group_btn);
        mGroupsListView = findViewById(R.id.groups_lv);
        mSignOutBtn = findViewById(R.id.sign_out_btn);

        mGroupsReference = MeetUpApplication.getInstance().getGroupsReference();
        mUsersReference = MeetUpApplication.getInstance().getUsersReference();
        mAuth = MeetUpApplication.getInstance().getAuth();

        mSeeCurrentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapIntent = new Intent(HomeActivity.this, MapsActivity.class);
                startActivity(mapIntent);
                finishActivity(0);
            }
        });

        mGroupListAdapter = new GroupListAdapter(this, R.layout.group_list_view, new ArrayList<String>());
        mGroupsListView.setAdapter(mGroupListAdapter);

        mUsersReference.child(mAuth.getUid()).child("groups").addChildEventListener(mGroupListAdapter);

        mAddGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptForGroupName();
            }
        });

        mSignOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MeetUpApplication.getInstance().getAuth().signOut();

                Intent loginIntent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finishActivity(0);
            }
        });
    }

    private OnCompleteListener<Void> addGroupCompletionListener = new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if(task.isSuccessful()) {
                Log.v("Group addition", "added group to user successfully");
            } else {
                Log.v("Group addition", "adding user to group failed");
            }
            loadingDialog.cancel();
        }
    };

    private void promptForGroupName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Title");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String groupName = input.getText().toString();
                Log.v("Group Name",  input.getText().toString());
                loadingDialog = ProgressDialog.show(HomeActivity.this,
                        "Creating Group", "Loading, Please wait...", true);
                Group group = new Group(MeetUpApplication.getInstance().getAuth().getCurrentUser().getUid());
                mGroupsReference.child(groupName).
                        setValue(group)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    Log.v("Group Creation", "Successfully created group");
                                    //TODO: add group to user table
                                    DatabaseReference reference = mUsersReference.child(mAuth.getUid()).child("groups").push();
                                    reference.setValue(groupName).addOnCompleteListener(addGroupCompletionListener);
                                } else {
                                    Log.v("Group Creation", "Group creation failed");
                                }
                            }
                        });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}