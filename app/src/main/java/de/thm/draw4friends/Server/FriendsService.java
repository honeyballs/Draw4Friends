package de.thm.draw4friends.Server;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.thm.draw4friends.Database.Database;
import de.thm.draw4friends.Friendlist.FriendlistActivity;
import de.thm.draw4friends.Friendlist.FriendlistCommunicator;
import de.thm.draw4friends.Model.Communicator;
import de.thm.draw4friends.Model.FriendWithFriendshipId;
import de.thm.draw4friends.Model.Friends;
import de.thm.draw4friends.Model.User;

/**
 * Created by Yannick Bals on 26.02.2018.
 */

public class FriendsService {

    private FriendlistCommunicator communicator;
    private Context context;

    public FriendsService(Communicator c) {
        this.communicator = (FriendlistCommunicator) c;
        this.context = (AppCompatActivity) c;
    }

    public void getFriends(int uId) {
        new GetFriendsTask().execute(uId);
    }

    public void addFriend(User user) {
        new AddFriendTask().execute(user);
    }

    class GetFriendsTask extends AsyncTask<Integer, Void, List<FriendWithFriendshipId>> {

        @Override
        protected List<FriendWithFriendshipId> doInBackground(Integer... integers) {
            Database db = Database.getDatabaseInstance(context);
            List<Friends> lf = db.friendsDAO().getAllFriendsOfUser(integers[0]);

            List<FriendWithFriendshipId> list = new ArrayList<>();

            for (Friends lfObj : lf) {
                User u;
                if (lfObj.getUserOneId() == integers[0]) {
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
            communicator.setFriendList(arr);
        }
    }

    class AddFriendTask extends AsyncTask<User, Void, String> {

        @Override
        protected String doInBackground(User... users) {
            User friend;
            Database db = Database.getDatabaseInstance(context);
            friend = db.userDAO().findUser(users[0].getUsername());

            String msg;

            if (friend != null) {
                Friends friends = new Friends();
                friends.setUserOneId(users[0].getUId());
                friends.setUserTwoId(friend.getUId());
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
            communicator.refreshList(msg);
        }
    }
}
