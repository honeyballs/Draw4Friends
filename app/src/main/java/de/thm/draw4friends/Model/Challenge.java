package de.thm.draw4friends.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

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
public class Challenge {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int player;
    private int opponent;

    @ColumnInfo(name = "turn_off")
    private int turnOff;

    @Ignore
    private String opponentName;

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
}
