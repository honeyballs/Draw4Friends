package de.thm.draw4friends.Server;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import de.thm.draw4friends.Database.Database;
import de.thm.draw4friends.Home.HomeCommunicator;
import de.thm.draw4friends.Model.Challenge;
import de.thm.draw4friends.Model.Communicator;
import de.thm.draw4friends.Model.Painting;
import de.thm.draw4friends.Paint.PaintCommunicator;

/**
 * Created by yannikstenzel on 01.03.18.
 */

public class PaintingService {

    private PaintCommunicator communicator;
    private Context context;

    public PaintingService(Communicator c) {
        communicator = (PaintCommunicator) c;
        context = (AppCompatActivity) c;
    }

    public void createChallenge(Challenge challenge) {
        new CreateChallengeTask().execute(challenge);
    }

    public void setPainting(Painting painting) {
        new SetPaintingsTask().execute(painting);
    }

    class SetPaintingsTask extends AsyncTask<Painting, Void, Void> {

        @Override
        protected Void doInBackground(Painting... paintings) {
            Database db = Database.getDatabaseInstance(context);
            db.paintingDAO().insertPainting(paintings[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            communicator.notifyChallengeStarted();
        }
    }

    class CreateChallengeTask extends AsyncTask<Challenge, Void, Long> {

        @Override
        protected Long doInBackground(Challenge... challenges) {
            Database db = Database.getDatabaseInstance(context);
            //User opponent = db.userDAO().findUser(users[0].getUsername());
//            Challenge challenge = new Challenge();
//            challenge.setPlayer(users[0].getUId());
//            challenge.setOpponent(opponent.getUId());
//            challenge.setTurnOff(users[0].getUId());
            long id = db.challengeDAO().insertChallenge(challenges[0]);
            return id;
        }

        @Override
        protected void onPostExecute(Long id) {
            communicator.setChallengeIdAndPainting(id);
            //communicator.setPainting();
        }
    }
}
