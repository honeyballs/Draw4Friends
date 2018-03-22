package de.thm.draw4friends.Friendlist;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.thm.draw4friends.Database.Database;
import de.thm.draw4friends.Model.FriendWithFriendshipId;
import de.thm.draw4friends.Model.Friends;
import de.thm.draw4friends.R;
import de.thm.draw4friends.Model.User;
import de.thm.draw4friends.Server.FriendsService;
import de.thm.draw4friends.Server.ServiceFacade;

/**
 * Created by Yannick Bals on 19.02.2018.
 */

public class FriendlistActivity extends AppCompatActivity implements FriendlistCommunicator {

    private User user;
    private FriendListAdapter adapter;

    private List<FriendWithFriendshipId> friends = new ArrayList<>();

    private FriendsService service;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.service = new FriendsService(this);

        setContentView(R.layout.friendlist_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            user = bundle.getParcelable(getString(R.string.user_obj));
        }

        // Fill List with Data
        ListView friendListView = findViewById(R.id.listViewFriends);
        adapter = new FriendListAdapter(this, friends);
        friendListView.setAdapter(adapter);
        friendListView.setOnItemLongClickListener(new FriendDeleteListener());

        FloatingActionButton fab = findViewById(R.id.addFriendButton);
        fab.setOnClickListener(new FABListener());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        service.getFriends(user.getUId());
    }

    @Override
    public void setFriendList(List<FriendWithFriendshipId> friendList) {
        friends.clear();
        friends = friendList;
        adapter.addAll(friendList);
    }

    @Override
    public void refreshList(String msg) {
        if (msg != null) {
            Toast.makeText(FriendlistActivity.this, msg,Toast.LENGTH_SHORT).show();
            if (msg.equals("You are friends now")) {
                friends.clear();
                service.getFriends(user.getUId());
            }
        }
    }

    @Override
    public void refreshAfterDelete() {
        friends.clear();
        service.getFriends(user.getUId());
    }

    class FABListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Log.d("info", "add btn pressed");
            AlertDialog.Builder builder = new AlertDialog.Builder(FriendlistActivity.this);
            builder.setTitle("Add friend");
            View viewInflated = LayoutInflater.from(FriendlistActivity.this).inflate(R.layout.textfield_dialog_layout, null);

            final TextView message = viewInflated.findViewById(R.id.dialogTextView);
            message.setText(R.string.search_friend);
            final EditText input = viewInflated.findViewById(R.id.dialogEditText);
            input.setHint(R.string.username);
            builder.setView(viewInflated);

            // Set up the buttons
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                Database db = Database.getDatabaseInstance(FriendlistActivity.this);
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    User mixedUser = new User();
                    mixedUser.setUId(user.getUId());
                    mixedUser.setUsername(input.getText().toString());
                    service.addFriend(mixedUser);
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        }

    }



    class FriendDeleteListener implements AdapterView.OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

            AlertDialog.Builder builder = new AlertDialog.Builder(FriendlistActivity.this);
            builder.setTitle(R.string.delete_friend);
            builder.setMessage(getString(R.string.delete_friend_sure) + " " + friends.get(position).getUsername() + "?");
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Friends friendsToDelete = new Friends();
                    friendsToDelete.setFriendsid(friends.get(position).getFriendshipId());
                    service.deleteFriend(friendsToDelete);
                }
            });

            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.show();

            return true;
        }
    }
}