package de.thm.draw4friends.Friendlist;

import java.util.List;

import de.thm.draw4friends.Model.Communicator;
import de.thm.draw4friends.Model.FriendWithFriendshipId;

/**
 * Created by Yannick Bals on 26.02.2018.
 */

public interface FriendlistCommunicator extends Communicator {

    void setFriendList(List<FriendWithFriendshipId> friendList);

    void refreshList(String msg);

    void refreshAfterDelete();

}
