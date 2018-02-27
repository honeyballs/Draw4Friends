package de.thm.draw4friends.Server;

import de.thm.draw4friends.Model.Communicator;
import de.thm.draw4friends.Model.Friends;
import de.thm.draw4friends.Model.User;

/**
 * Created by Yannick Bals on 26.02.2018.
 */

public class ServiceFacade {

    private Communicator communicator;

    public ServiceFacade(Communicator c) {
        this.communicator = c;
    }


    /**
     * Everything Account related.
     */

    public void registerUser(String username, String pw, String pwConfirm) {
        new AccountService(communicator).registerUser(username, pw, pwConfirm);
    }

    public void loginUser(String username, String pw) {
        new AccountService(communicator).loginUser(username, pw);
    }

    public void loginWithToken() {
        new AccountService(communicator).loginWithToken();
    }

    /**
     * Everything Challenge related.
     */

    public void getChallenges(int uId) {
        new HomeService(communicator).getChallenges(uId);
    }

    public void getFriendsForChallenges(int uId) {
        new HomeService(communicator).getFriends(uId);
    }

    public void createChallenge(User user) {
        new HomeService(communicator).createChallenge(user);
    }

    public void deleteAllChallengesOfUser(int uId) {
        new HomeService(communicator).deleteAllChallengesOfUser(uId);
    }

    /**
     * Everything Friend related.
     */

    public void getFriends(int uId) {
        new FriendsService(communicator).getFriends(uId);
    }

    public void addFriend(User user) {
        new FriendsService(communicator).addFriend(user);
    }

    public void deleteFriend(Friends friends) {
        new FriendsService(communicator).deleteFriend(friends);
    }

}
