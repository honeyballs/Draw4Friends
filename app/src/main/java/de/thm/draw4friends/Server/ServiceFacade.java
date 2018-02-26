package de.thm.draw4friends.Server;

import de.thm.draw4friends.Model.Communicator;
import de.thm.draw4friends.Model.User;

/**
 * Created by Yannick Bals on 26.02.2018.
 */

public class ServiceFacade {

    /**
     * Everything Account related.
     */

    public static void registerUser(Communicator c, String username, String pw, String pwConfirm) {
        new AccountService(c).registerUser(username, pw, pwConfirm);
    }

    public static void loginUser(Communicator c, String username, String pw) {
        new AccountService(c).loginUser(username, pw);
    }

    public static void loginWithToken(Communicator c) {
        new AccountService(c).loginWithToken();
    }

    /**
     * Everything Challenge related.
     */

    public static void getChallenges(Communicator c, int uId) {
        new HomeService(c).getChallenges(uId);
    }

    public static void getFriendsForChallenges(Communicator c, int uId) {
        new HomeService(c).getFriends(uId);
    }

    public static void createChallenge(Communicator c, User user) {
        new HomeService(c).createChallenge(user);
    }

    /**
     * Everything Friend related.
     */

    public static void getFriends(Communicator c, int uId) {
        new FriendsService(c).getFriends(uId);
    }

    public static void addFriend(Communicator c, User user) {
        new FriendsService(c).addFriend(user);
    }

}
