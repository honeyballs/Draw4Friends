package de.thm.draw4friends.Home;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import de.thm.draw4friends.Database.Database;
import de.thm.draw4friends.Friendlist.FriendlistActivity;
import de.thm.draw4friends.Login.LoginActivity;
import de.thm.draw4friends.Model.Challenge;
import de.thm.draw4friends.Model.User;
import de.thm.draw4friends.R;


/**
 * Created by Yannick Bals on 19.02.2018.
 */

public class HomeActivity extends AppCompatActivity {

    private User user;
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
        new getChallengesTask().execute(user.getUId());
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
                adapter.addAll(challenges);
            }
        }
    }

}
