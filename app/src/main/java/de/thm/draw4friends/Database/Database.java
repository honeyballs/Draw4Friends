package de.thm.draw4friends.Database;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import de.thm.draw4friends.Model.Friends;
import de.thm.draw4friends.Model.User;

/**
 * Created by Yannick Bals on 19.02.2018.
 */

@android.arch.persistence.room.Database(entities = {User.class, Friends.class}, version = 1)
public abstract class Database extends RoomDatabase {

    private static Database instance;

    public abstract UserDAO userDAO();
    public abstract FriendsDAO friendsDAO();

    public static Database getDatabaseInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), Database.class, "draw_db")
                    .build();
        }
        return instance;
    }

}
