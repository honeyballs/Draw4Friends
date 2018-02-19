package de.thm.draw4friends.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Yannick Bals on 19.02.2018.
 */

@Entity(tableName = "friends",
        foreignKeys = {
                @ForeignKey(entity = User.class,
                            parentColumns = "uId",
                            childColumns = "user_one_id",
                            onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = User.class,
                            parentColumns = "uId",
                            childColumns = "user_two_id",
                            onDelete = ForeignKey.CASCADE)})
public class Friends {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "friends_id")
    private int friendsid;

    @ColumnInfo(name = "user_one_id")
    private int userOneId;

    @ColumnInfo(name = "user_two_id")
    private int userTwoId;

    public int getFriendsid() {
        return friendsid;
    }

    public void setFriendsid(int friendsid) {
        this.friendsid = friendsid;
    }

    public int getUserOneId() {
        return userOneId;
    }

    public void setUserOneId(int userOneId) {
        this.userOneId = userOneId;
    }

    public int getUserTwoId() {
        return userTwoId;
    }

    public void setUserTwoId(int userTwoId) {
        this.userTwoId = userTwoId;
    }
}
