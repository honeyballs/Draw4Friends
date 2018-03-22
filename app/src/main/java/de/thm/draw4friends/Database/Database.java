package de.thm.draw4friends.Database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import de.thm.draw4friends.Model.Challenge;
import de.thm.draw4friends.Model.Friends;
import de.thm.draw4friends.Model.Painting;
import de.thm.draw4friends.Model.User;

/**
 * Created by Yannick Bals on 19.02.2018.
 */

@android.arch.persistence.room.Database(entities = {User.class, Friends.class, Challenge.class, Painting.class}, version = 4)
public abstract class Database extends RoomDatabase {

    private static Database instance;

    public abstract UserDAO userDAO();
    public abstract FriendsDAO friendsDAO();
    public abstract ChallengeDAO challengeDAO();
    public abstract PaintingDAO paintingDAO();

    public static Database getDatabaseInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), Database.class, "draw_db")
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                    .build();
        }
        return instance;
    }

    static final Migration MIGRATION_1_2 = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            String migrationQuery = "CREATE TABLE 'challenges' (" +
                    "'id' INTEGER NOT NULL, " +
                    "'player' INTEGER NOT NULL, " +
                    "'opponent' INTEGER NOT NULL, " +
                    "'turn_off' INTEGER NOT NULL, " +
                    "PRIMARY KEY('id'), " +
                    "FOREIGN KEY('player') REFERENCES users('uId') ON DELETE CASCADE, " +
                    "FOREIGN KEY('opponent') REFERENCES users('uId') ON DELETE CASCADE)";
            database.execSQL(migrationQuery);
        }
    };

    static final Migration MIGRATION_2_3 = new Migration(2,3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            String migrationQuery = "CREATE TABLE 'paintings' (" +
                    "'id' INTEGER NOT NULL, " +
                    "'challengeId' INTEGER NOT NULL, " +
                    "'description' TEXT, " +
                    "'paint_command_list' TEXT, " +
                    "PRIMARY KEY('id'), " +
                    "FOREIGN KEY('challengeId') REFERENCES challenges('id') ON DELETE CASCADE )";
            database.execSQL(migrationQuery);
        }
    };

    static final Migration MIGRATION_3_4 = new Migration(3,4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            String migrationQuery = "ALTER TABLE users ADD COLUMN score INTEGER NOT NULL DEFAULT 0";
            database.execSQL(migrationQuery);
        }
    };
}
