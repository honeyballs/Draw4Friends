package de.thm.draw4friends.Server;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import de.thm.draw4friends.Database.Database;
import de.thm.draw4friends.Model.FriendWithFriendshipId;
import de.thm.draw4friends.Model.Friends;
import de.thm.draw4friends.Model.User;
import de.thm.draw4friends.Stats.StatsCommunicator;

/**
 * Created by Yannick Bals on 22.03.2018.
 */

public class StatsService {

    private StatsCommunicator communicator;
    private Context context;

    public StatsService(StatsCommunicator c) {
        this.communicator = c;
        this.context = (AppCompatActivity) c;
    }

    public void getScoresOfFrieds(int uId) {
        new GetScoresTask().execute(uId);
    }

    class GetScoresTask extends AsyncTask<Integer, Void, List<User>> {

        @Override
        protected List<User> doInBackground(Integer... integers) {
            Database db = Database.getDatabaseInstance(context);
            List<Friends> friends = db.friendsDAO().getAllFriendsOfUser(integers[0]);
            List<User> users = new ArrayList<>();
            for (Friends friendship : friends) {
                User u;
                if (friendship.getUserOneId() == integers[0]) {
                    u = db.userDAO().getUserById(friendship.getUserTwoId());
                } else {
                    u = db.userDAO().getUserById(friendship.getUserOneId());
                }
                users.add(u);
            }
            return users;
        }

        @Override
        protected void onPostExecute(List<User> users) {
            communicator.setFriendScores(users);
        }
    }

}
