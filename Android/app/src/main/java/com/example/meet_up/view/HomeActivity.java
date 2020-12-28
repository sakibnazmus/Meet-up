package com.example.meet_up.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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
import android.widget.Toast;

import com.example.meet_up.R;
import com.example.meet_up.databinding.ActivityHomeBinding;
import com.example.meet_up.model.AuthToken;
import com.example.meet_up.service.GroupService;
import com.example.meet_up.view_model.HomeViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private Button mSeeCurrentBtn;
    private ListView mGroupsListView;
    private Button mSignOutBtn;

    private HomeActivity mActivity;
    private ProgressDialog loadingDialog;

    private GroupListAdapter mGroupListAdapter;
    private GoogleSignInClient mGoogleSignInClient;

    private HomeViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHomeBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding.setViewModel(mViewModel);
        binding.setLifecycleOwner(this);

        mActivity = this;

        mSeeCurrentBtn = findViewById(R.id.see_current_btn);
        mGroupsListView = findViewById(R.id.groups_lv);
        mSignOutBtn = findViewById(R.id.sign_out_btn);

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

        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mViewModel.getAuthToken(this).observe(this, getAuthenticationListener());
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

    private Observer<AuthToken> getAuthenticationListener() {
        return authToken -> {
            if (authToken == null) {
                goToLoginPage();
            }
        };
    }

    private void goToLoginPage() {
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finishAfterTransition();
    }

    public void promptForGroupName(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Title");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Confirm", (dialog, which) -> {
            final String groupName = input.getText().toString();
            loadingDialog = ProgressDialog.show(HomeActivity.this,
                    "Creating Group", "Loading, Please wait...", true);
            mViewModel.createNewGroup(groupName).observe(mActivity , response -> {
                Toast.makeText(mActivity, response, Toast.LENGTH_LONG).show();
                loadingDialog.cancel();
            });
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}