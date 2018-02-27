package de.thm.draw4friends.Server;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import de.thm.draw4friends.Database.Database;
import de.thm.draw4friends.Home.HomeActivity;
import de.thm.draw4friends.Home.HomeCommunicator;
import de.thm.draw4friends.Model.Challenge;
import de.thm.draw4friends.Model.Communicator;
import de.thm.draw4friends.Model.FriendWithFriendshipId;
import de.thm.draw4friends.Model.Friends;
import de.thm.draw4friends.Model.User;

/**
 * Created by Yannick Bals on 26.02.2018.
 */

public class HomeService {

    private HomeCommunicator communicator;
    private Context context;

    public HomeService(Communicator c) {
        this.communicator = (HomeCommunicator) c;
        this.context = (AppCompatActivity) c;
    }

    public void getChallenges(int id) {
        new GetChallengesTask().execute(id);
    }

    public void getFriends(int id) {
        new GetFriendsTask().execute(id);
    }

    public void createChallenge(User user) {
        new CreateChallengeTask().execute(user);
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

    class CreateChallengeTask extends AsyncTask<User, Void, Void> {

        @Override
        protected Void doInBackground(User... users) {
            Database db = Database.getDatabaseInstance(context);
            User opponent = db.userDAO().findUser(users[0].getUsername());
            Challenge challenge = new Challenge();
            challenge.setPlayer(users[0].getUId());
            challenge.setOpponent(opponent.getUId());
            challenge.setTurnOff(users[0].getUId());
            db.challengeDAO().insertChallenge(challenge);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //TODO: Start drawing activity
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

}
