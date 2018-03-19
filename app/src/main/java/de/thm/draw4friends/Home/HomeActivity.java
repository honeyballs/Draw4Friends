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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.thm.draw4friends.Database.Database;
import de.thm.draw4friends.Friendlist.FriendlistActivity;
import de.thm.draw4friends.Guess.GuessActivity;
import de.thm.draw4friends.Login.LoginActivity;
import de.thm.draw4friends.Model.Challenge;
import de.thm.draw4friends.Model.FriendWithFriendshipId;
import de.thm.draw4friends.Model.Friends;
import de.thm.draw4friends.Model.User;
import de.thm.draw4friends.Paint.PaintCanvasActivity;
import de.thm.draw4friends.R;
import de.thm.draw4friends.Server.ServiceFacade;


/**
 * Created by Yannick Bals on 19.02.2018.
 */

public class HomeActivity extends AppCompatActivity implements HomeCommunicator {

    private User user;
    private List<FriendWithFriendshipId> friends = new ArrayList<>();
    private ChallengeAdapter adapter;

    private Button startChallengeButton;
    private ListView challengeListView;

    private ServiceFacade serviceFacade;

    private Challenge newChallenge;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        serviceFacade = new ServiceFacade(this);
        setContentView(R.layout.home_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            user = bundle.getParcelable(getString(R.string.user_obj));
            Log.e("USER: ", user.getUsername());
        }

        //TODO: Remove this, only for testing
        //serviceFacade.deleteAllChallengesOfUser(user.getUId());

        this.startChallengeButton = findViewById(R.id.startChallengeButton);
        this.startChallengeButton.setOnClickListener(new ChallengeButtonListener());
        this.challengeListView = findViewById(R.id.challengeList);
        adapter = new ChallengeAdapter(this, new ArrayList<Challenge>(), user.getUId());
        this.challengeListView.setAdapter(adapter);
        this.challengeListView.setOnItemClickListener(new ListItemListener());

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
        serviceFacade.getFriendsForChallenges(user.getUId());
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

    @Override
    public void setChallenges(List<Challenge> challenges) {
        adapter.clear();
        adapter.addAll(challenges);
    }

    @Override
    public void setFriends(List<FriendWithFriendshipId> friends) {
        this.friends = friends;
        serviceFacade.getChallenges(user.getUId());
    }

    @Override
    public void setOpponentId(Integer id) {
        newChallenge.setOpponent(id);
        Intent intent = new Intent(HomeActivity.this, PaintCanvasActivity.class);
        intent.putExtra(getString(R.string.challenge_obj), newChallenge);
        startActivity(intent);
    }

    @Override
    public void setData(Object obj) {

    }

    class ChallengeButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            if (friends != null && friends.size() > 0) {
                //Show a dialog to pick a friend
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle(R.string.pick_opp);
                final ArrayAdapter<String> friendsAdapter = new ArrayAdapter<String>(HomeActivity.this, android.R.layout.simple_list_item_1);
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
                        String oppnentName = friendsAdapter.getItem(which);
                        newChallenge = new Challenge();
                        newChallenge.setPlayer(user.getUId());
                        newChallenge.setOpponentName(oppnentName);
                        newChallenge.setTurnOff(user.getUId());
                        serviceFacade.getIdOfOpponent(oppnentName);
                    }
                });
                builder.show();
            } else {
                Toast.makeText(HomeActivity.this, R.string.no_friends, Toast.LENGTH_LONG).show();
            }

        }
    }

    class ListItemListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Challenge challenge = adapter.getItem(position);
            if (challenge.getTurnOff() == user.getUId()) {
                Intent intent = new Intent(HomeActivity.this, PaintCanvasActivity.class);
                intent.putExtra(getString(R.string.challenge_obj), challenge);
                startActivity(intent);
                //TODO: Check if painted already
            } else {
                Intent intent = new Intent(HomeActivity.this, GuessActivity.class);
                intent.putExtra(getString(R.string.challenge_obj), challenge);
                startActivity(intent);
            }
        }
    }
}
