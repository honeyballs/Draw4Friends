package de.thm.draw4friends.Stats;

import java.util.List;

import de.thm.draw4friends.Model.User;

/**
 * Created by Yannick Bals on 22.03.2018.
 */

public interface StatsCommunicator {

    void setFriendScores(List<User> friends);

}
