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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.thm.draw4friends.Database.Database;
import de.thm.draw4friends.Model.FriendWithFriendshipId;
import de.thm.draw4friends.Model.Friends;
import de.thm.draw4friends.R;
import de.thm.draw4friends.Model.User;

/**
 * Created by Yannick Bals on 19.02.2018.
 */

public class FriendlistActivity extends AppCompatActivity {

    private User user;
    private ArrayAdapter<String> adapter;

    private List<FriendWithFriendshipId> fwfidArr = new ArrayList<>();
    private List<String> friends = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, friends);
        friendListView.setAdapter(adapter);
        new GetFriendsTask().execute(user);

        FloatingActionButton fab = findViewById(R.id.addFriendButton);
        fab.setOnClickListener(new FABListener());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    class GetFriendsTask extends AsyncTask<User, Void, List<FriendWithFriendshipId>> {

        @Override
        protected List<FriendWithFriendshipId> doInBackground(User... users) {
            User user = users[0];

            Database db = Database.getDatabaseInstance(FriendlistActivity.this);
            List<Friends> lf = db.friendsDAO().getAllFriendsOfUser(user.getUId());

            List<FriendWithFriendshipId> list = new ArrayList<>();

            for (Friends lfObj : lf) {
                User u;
                if (lfObj.getUserOneId() == user.getUId()) {
                    u = db.userDAO().getUserById(lfObj.getUserTwoId());
                } else {
                    u = db.userDAO().getUserById(lfObj.getUserOneId());
                }
                list.add(new FriendWithFriendshipId(u.getUsername(), lfObj.getFriendsid()));
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<FriendWithFriendshipId> arr) {
            friends.clear();
            for (FriendWithFriendshipId obj : arr) {
                friends.add(obj.getUsername());
            }
            adapter.notifyDataSetChanged();
        }
    }

    class FABListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Log.d("info", "add btn pressed");
            AlertDialog.Builder builder = new AlertDialog.Builder(FriendlistActivity.this);
            builder.setTitle("Add friend");
            View viewInflated = LayoutInflater.from(FriendlistActivity.this).inflate(R.layout.friend_add_frame_layout, (ViewGroup) findViewById(android.R.id.content), false);

            final EditText input = viewInflated.findViewById(R.id.input);
            builder.setView(viewInflated);

            // Set up the buttons
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                Database db = Database.getDatabaseInstance(FriendlistActivity.this);
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new AddFriendTask().execute(input.getText().toString());
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

        class AddFriendTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... strings) {
                String username = strings[0];
                User user;
                Database db = Database.getDatabaseInstance(FriendlistActivity.this);
                user = db.userDAO().findUser(username);

                String msg;

                if (user != null) {
                    Friends friends = new Friends();
                    friends.setUserOneId(FriendlistActivity.this.user.getUId());
                    friends.setUserTwoId(user.getUId());
                    if (friends.getUserOneId() == friends.getUserTwoId()){
                        msg = "You can't be your own friend";
                    } else {
                        long success = db.friendsDAO().insertFriendsRow(friends);
                        // see if friendship was inserted successfully
                        if (success != 0) {
                            msg = "You are friends now";
                        } else {
                            msg = "An error accured while adding";
                        }
                    }
                } else {
                    msg = "No user was found";
                }

                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                if (msg != null) {
                    Toast.makeText(FriendlistActivity.this, msg,Toast.LENGTH_SHORT).show();
                    if (msg.equals("You are friends now")) {
                        new GetFriendsTask().execute(user);
                    }
                }
            }
        }

    }
}