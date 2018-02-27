package de.thm.draw4friends.Home;

import java.util.List;

import de.thm.draw4friends.Model.Challenge;
import de.thm.draw4friends.Model.Communicator;
import de.thm.draw4friends.Model.FriendWithFriendshipId;
import de.thm.draw4friends.Model.User;

/**
 * Created by Yannick Bals on 26.02.2018.
 */

public interface HomeCommunicator extends Communicator {

    void setChallenges(List<Challenge> challenges);

    void setFriends(List<FriendWithFriendshipId> friends);

    void setOpponentId(Integer id);

}
