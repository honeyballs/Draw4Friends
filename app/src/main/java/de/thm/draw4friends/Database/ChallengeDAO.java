package de.thm.draw4friends.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import de.thm.draw4friends.Model.Challenge;
import de.thm.draw4friends.Model.User;

/**
 * Created by Yannick Bals on 22.02.2018.
 */

@Dao
public interface ChallengeDAO {

    @Insert
    public long insertChallenge(Challenge challenge);

    @Update
    public int updateChallenge(Challenge challenge);

    @Delete
    public int deleteChallenge(Challenge challenge);

    @Delete
    public int deleteChallenges(List<Challenge> challenges);

    @Query("SELECT * FROM challenges WHERE player LIKE :userId OR opponent LIKE :userId")
    public List<Challenge> getChallengesForPlayer(int userId);

    @Query("SELECT username, uId from users INNER JOIN challenges ON users.uId = challenges.opponent WHERE challenges.player = :userId")
    public User getOpponent(int userId);

    //TODO: Drawing Gelöt

}
