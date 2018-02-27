package de.thm.draw4friends.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Yannick Bals on 22.02.2018.
 */

@Entity(tableName = "challenges",
        foreignKeys = {
                @ForeignKey(entity = User.class,
                        parentColumns = "uId",
                        childColumns = "player",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = User.class,
                        parentColumns = "uId",
                        childColumns = "opponent",
                        onDelete = ForeignKey.CASCADE)})
public class Challenge implements Parcelable{

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int player;
    private int opponent;

    @ColumnInfo(name = "turn_off")
    private int turnOff;

    @Ignore
    private String opponentName;

    public Challenge() {
        super();
    }

    @Ignore
    public Challenge(Parcel in) {
        id = in.readInt();
        player = in.readInt();
        opponent = in.readInt();
        turnOff = in.readInt();
        opponentName = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public int getOpponent() {
        return opponent;
    }

    public void setOpponent(int opponent) {
        this.opponent = opponent;
    }

    public int getTurnOff() {
        return turnOff;
    }

    public void setTurnOff(int turnOff) {
        this.turnOff = turnOff;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public void setOpponentName(String opponentName) {
        this.opponentName = opponentName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(player);
        dest.writeInt(opponent);
        dest.writeInt(turnOff);
        dest.writeString(opponentName);
    }

    public static final Parcelable.Creator<Challenge> CREATOR = new Parcelable.Creator<Challenge>() {

        @Override
        public Challenge createFromParcel(Parcel source) {
            return new Challenge(source);
        }

        @Override
        public Challenge[] newArray(int size) {
            return new Challenge[size];
        }
    };
}
