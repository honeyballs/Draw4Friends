package de.thm.draw4friends.Model;

/**
 * Created by Farea on 23.02.2018.
 */

public class FriendWithFriendshipId {
    private String username;
    private int friendshipId;

    public FriendWithFriendshipId(String username, int friendshipId) {
        this.username = username;
        this.friendshipId = friendshipId;
    }

    public String getUsername() { return username; }
    public int getFriendshipId() { return friendshipId; }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFriendshipId(int friendshipId) {
        this.friendshipId = friendshipId;
    }
}
