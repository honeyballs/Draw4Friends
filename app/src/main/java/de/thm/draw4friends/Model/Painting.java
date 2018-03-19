package de.thm.draw4friends.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yannikstenzel on 01.03.18.
 */

@Entity(tableName = "paintings",
        foreignKeys = {
                @ForeignKey(entity = Challenge.class,
                            parentColumns = "id",
                            childColumns = "challengeId",
                            onDelete = ForeignKey.CASCADE)
        })

public class Painting implements Parcelable{

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int challengeId;
    private String description;

    @ColumnInfo(name = "paint_command_list")
    private String paintCommandListString;

    public Painting() { super(); }

    public Painting(Parcel in) {
        id = in.readInt();
        challengeId = in.readInt();
        description = in.readString();
        paintCommandListString = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(int challengeId) {
        this.challengeId = challengeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPaintCommandListString() {
        return paintCommandListString;
    }

    public void setPaintCommandListString(String paintCommandListString) {
        this.paintCommandListString = paintCommandListString;
    }

    public List<PaintCommand> getPaintCommandLists() {
        //return GithubTypeConverters.stringToSomeObjectList(paintCommandListString);
        RuntimeTypeAdapterFactory<PaintCommand> adapter = RuntimeTypeAdapterFactory
                .of(PaintCommand.class)
                .registerSubtype(PaintCommandPath.class)
                .registerSubtype(PaintCommandCirc.class)
                .registerSubtype(PaintCommandRect.class);
        Type listType = new TypeToken<List<PaintCommand>>(){}.getType();
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(adapter).create();
        return gson.fromJson(paintCommandListString, listType);
    }

    public void setPaintCommandListString(List<PaintCommand> paintCommandListString) {
        //this.paintCommandListString = GithubTypeConverters.someObjectListToString(paintCommandListString);
        RuntimeTypeAdapterFactory<PaintCommand> adapter = RuntimeTypeAdapterFactory
                .of(PaintCommand.class)
                .registerSubtype(PaintCommandPath.class)
                .registerSubtype(PaintCommandCirc.class)
                .registerSubtype(PaintCommandRect.class);
        Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapterFactory(adapter).create();
        this.paintCommandListString = gson.toJson(paintCommandListString);
        Log.e("Command Strings", this.paintCommandListString);
    }


    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(challengeId);
        dest.writeString(description);
        dest.writeString(paintCommandListString);
    }

    public static final Parcelable.Creator<Painting> CREATOR = new Parcelable.Creator<Painting>() {

        @Override
        public Painting createFromParcel(Parcel source) {
            return new Painting(source);
        }

        @Override
        public Painting[] newArray(int size) {
            return new Painting[size];
        }
    };
}
