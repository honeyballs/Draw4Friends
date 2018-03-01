package de.thm.draw4friends.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import de.thm.draw4friends.Model.Challenge;
import de.thm.draw4friends.Model.PaintCommand;
import de.thm.draw4friends.Model.Painting;
import de.thm.draw4friends.Model.User;

/**
 * Created by yannikstenzel on 01.03.2018.
 */

@Dao
public interface PaintingDAO {

    @Insert
    public long insertPainting(Painting painting);

    @Update
    public int updatePainting(Painting painting);

    @Delete
    public int deletePainting(Painting painting);

    @Delete
    public int deletePaintings(List<Painting> paintings);

   @Query("SELECT * FROM paintings WHERE challengeId LIKE :challengeId")
    public List<Painting> getPaintingForChallenge(int challengeId);

}
