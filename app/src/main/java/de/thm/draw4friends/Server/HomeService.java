package de.thm.draw4friends.Server;

import android.content.Context;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import de.thm.draw4friends.Database.Database;
import de.thm.draw4friends.Home.HomeActivity;
import de.thm.draw4friends.Home.HomeCommunicator;
import de.thm.draw4friends.Model.Challenge;
import de.thm.draw4friends.Model.FriendWithFriendshipId;
import de.thm.draw4friends.Model.Friends;
import de.thm.draw4friends.Model.User;

/**
 * Created by Yannick Bals on 26.02.2018.
 */

public class HomeService {

    private HomeCommunicator communicator;
    private Context context;

    public HomeService(HomeCommunicator c) {
        this.communicator =  c;
        this.context = (AppCompatActivity) c;
    }

    public void getChallenges(int id) {
        new GetChallengesTask().execute(id);
    }

    public void getFriends(int id) {
        new GetFriendsTask().execute(id);
    }

    public void getIdOfOpponent(String username){
        new GetIdOfOpponentTask().execute(username);
    }

    public void deleteAllChallengesOfUser(int uId) {
        new DeleteAllChallengesOfUserTask().execute(uId);
    }

    class GetFriendsTask extends AsyncTask<Integer, Void, List<FriendWithFriendshipId>> {

        @Override
        protected List<FriendWithFriendshipId> doInBackground(Integer... integers) {
            List<FriendWithFriendshipId> friends = new ArrayList<>();
            Database db = Database.getDatabaseInstance(context);
            List<Friends> friendsTemp = db.friendsDAO().getAllFriendsOfUser(integers[0]);
            for (Friends f : friendsTemp) {
                User u;
                if (f.getUserOneId() == integers[0]) {
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
            communicator.setFriends(friendsList);
        }
    }


    class GetChallengesTask extends AsyncTask<Integer, Void, List<Challenge>> {

        @Override
        protected List<Challenge> doInBackground(Integer... integers) {
            int uId = integers[0];
            Database db = Database.getDatabaseInstance(context);
            List<Challenge> challenges = db.challengeDAO().getChallengesForPlayer(uId);
            List<User> opponents = new ArrayList<>();
            for (Challenge c : challenges) {
                User tempUser;
                if(c.getPlayer() == uId) {
                    tempUser = db.userDAO().getUserById(c.getOpponent());
                } else {
                    tempUser = db.userDAO().getUserById(c.getPlayer());
                }
                c.setOpponentName(tempUser.getUsername());
            }
            return challenges;
        }

        @Override
        protected void onPostExecute(List<Challenge> challenges) {
            communicator.setChallenges(challenges);
        }
    }

    class DeleteAllChallengesOfUserTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... integers) {
            Database db = Database.getDatabaseInstance(context);
            List<Challenge> challenges = db.challengeDAO().getChallengesForPlayer(integers[0]);
            db.challengeDAO().deleteChallenges(challenges);
            return null;
        }

    }

    class GetIdOfOpponentTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... strings) {
            Database db = Database.getDatabaseInstance(context);
            User user = db.userDAO().findUser(strings[0]);
            return user.getUId();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            communicator.setOpponentId(integer);
        }
    }

}
