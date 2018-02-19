package de.thm.draw4friends.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import de.thm.draw4friends.Model.User;

/**
 * Created by Yannick Bals on 19.02.2018.
 */

@Dao
public interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public long insertUser(User user);

    @Update(onConflict = OnConflictStrategy.ABORT)
    public int updateUser(User user);

    @Delete
    public int deleteUser(User user);

    @Query("SELECT uId, username FROM users WHERE username LIKE :username AND password LIKE :password")
    public User loginWithInfo(String username, String password);

    @Query("SELECT uId, username FROM users WHERE token LIKE :token")
    public User loginWithToken(String token);

    @Query("SELECT uId, username FROM users")
    public List<User> getUsers();

    @Query("SELECT uId, username FROM users WHERE uId LIKE :uId")
    public User getUserById(int uId);

    @Query("SELECT uId, username FROM users WHERE username LIKE :username")
    public User findUser(String username);

}
