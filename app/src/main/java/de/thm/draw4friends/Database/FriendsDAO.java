package de.thm.draw4friends.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import de.thm.draw4friends.Model.Friends;

/**
 * Created by Yannick Bals on 19.02.2018.
 */

@Dao
public interface FriendsDAO {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public long insertFriendsRow(Friends friends);

    @Update(onConflict = OnConflictStrategy.ABORT)
    public int updateFriendsRow(Friends friends);

    @Delete
    public int deleteFriends(Friends friends);

    @Query("SELECT * FROM friends WHERE user_one_id LIKE :userId OR user_two_id LIKE :userId")
    public List<Friends> getAllFriendsOfUser(int userId);

}
