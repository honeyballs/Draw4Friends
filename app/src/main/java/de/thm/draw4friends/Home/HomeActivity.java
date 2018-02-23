package de.thm.draw4friends.Home;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.thm.draw4friends.Database.Database;
import de.thm.draw4friends.Friendlist.FriendlistActivity;
import de.thm.draw4friends.Login.LoginActivity;
import de.thm.draw4friends.Model.Challenge;
import de.thm.draw4friends.Model.FriendWithFriendshipId;
import de.thm.draw4friends.Model.Friends;
import de.thm.draw4friends.Model.User;
import de.thm.draw4friends.R;


/**
 * Created by Yannick Bals on 19.02.2018.
 */

public class HomeActivity extends AppCompatActivity {

    private User user;
    private List<FriendWithFriendshipId> friends = new ArrayList<>();
    private ChallengeAdapter adapter;

    private Button startChallengeButton;
    private ListView challengeListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            user = bundle.getParcelable(getString(R.string.user_obj));
            Log.e("USER: ", user.getUsername());
        }

        this.startChallengeButton = findViewById(R.id.startChallengeButton);
        this.startChallengeButton.setOnClickListener(new ChallengeButtonListener());
        this.challengeListView = findViewById(R.id.challengeList);
        adapter = new ChallengeAdapter(this, new ArrayList<Challenge>(), user.getUId());
        this.challengeListView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.friends_menu:
                Intent intent = new Intent(this, FriendlistActivity.class);
                intent.putExtra(getString(R.string.user_obj), user);
                startActivity(intent);
                return true;
            case R.id.logout_menu:
                logoutDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        friends.clear();
        new getChallengesTask().execute(user.getUId());
        new getFriendsTask().execute(user.getUId());
    }

    private void logoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.logout));
        builder.setMessage(getString(R.string.confirm_logout));
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(getString(R.string.login_token), "");
                editor.commit();
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    class ChallengeButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            if (friends != null && friends.size() > 0) {
                //Show a dialog to pick a friend
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle(R.string.pick_opp);
                final ArrayAdapter<String> friendsAdapter = new ArrayAdapter<String>(HomeActivity.this, android.R.layout.select_dialog_singlechoice);
                for (FriendWithFriendshipId f : friends) {
                    friendsAdapter.add(f.getUsername());
                }
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setAdapter(friendsAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String username = friendsAdapter.getItem(which);
                        new CreateChallengeTask().execute(username);
                    }
                });
                builder.show();
            } else {
                Toast.makeText(HomeActivity.this, R.string.no_friends, Toast.LENGTH_LONG).show();
            }

        }
    }

    class getChallengesTask extends AsyncTask<Integer, Void, List<Challenge>> {

        @Override
        protected List<Challenge> doInBackground(Integer... integers) {
            List<Challenge> challenges = null;
            Database db = Database.getDatabaseInstance(HomeActivity.this);
            challenges = db.challengeDAO().getChallengesForPlayer(integers[0]);
            return challenges;
        }

        @Override
        protected void onPostExecute(List<Challenge> challenges) {
            if (challenges != null) {
                adapter.clear();
                adapter.addAll(challenges);
            }
        }
    }

    class getFriendsTask extends AsyncTask<Integer, Void, List<FriendWithFriendshipId>> {

        @Override
        protected List<FriendWithFriendshipId> doInBackground(Integer... integers) {
            List<FriendWithFriendshipId> friends = new ArrayList<>();
            Database db = Database.getDatabaseInstance(HomeActivity.this);
            List<Friends> friendsTemp = db.friendsDAO().getAllFriendsOfUser(integers[0]);
            for (Friends f : friendsTemp) {
                User u;
                if (f.getUserOneId() == user.getUId()) {
                    u = db.userDAO().getUserById(f.getUserTwoId());
                } else {
                    u = db.userDAO().getUserById(f.getUserOneId());
                }
                friends.add(new FriendWithFriendshipId(u.getUsername(), f.getFriendsid()));
            }
            return friends;
        }

        @Override
        protected void onPostExecute(List<FriendWithFriendshipId> friendsList) {
            friends = friendsList;
        }
    }

    class CreateChallengeTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            Database db = Database.getDatabaseInstance(HomeActivity.this);
            User opponent = db.userDAO().findUser(strings[0]);
            Challenge challenge = new Challenge();
            challenge.setPlayer(user.getUId());
            challenge.setOpponent(opponent.getUId());
            challenge.setTurnOff(user.getUId());
            db.challengeDAO().insertChallenge(challenge);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //TODO: Start drawing activity
        }
    }

}
