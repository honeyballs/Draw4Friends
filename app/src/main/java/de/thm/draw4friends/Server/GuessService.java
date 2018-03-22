package de.thm.draw4friends.Server;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import de.thm.draw4friends.Database.Database;
import de.thm.draw4friends.Guess.GuessCommunicator;
import de.thm.draw4friends.Model.Challenge;
import de.thm.draw4friends.Model.Painting;
import de.thm.draw4friends.Model.User;

/**
 * Created by Yannick Bals on 19.03.2018.
 */

public class GuessService {

    private GuessCommunicator communicator;
    private Context context;

    public GuessService(GuessCommunicator c) {
        this.communicator = c;
        this.context = (AppCompatActivity) c;
    }

    public void getPainting(int callengeid) {
        new GetPaintingsTask().execute(callengeid);
    }

    public void updateScore(User user) {
        new UpdateScoreTask().execute(user);
    }

    public void updateChallengeTurn(Challenge challenge) {
        new UpdateChallengeTurnTask().execute(challenge);
    }

    public void deletePainting(Painting painting) {
        new DeletePaintingTask().execute(painting);
    }

    class GetPaintingsTask extends AsyncTask<Integer, Void, List<Painting>> {

        @Override
        protected List<Painting> doInBackground(Integer... integers) {
            List<Painting> paintings = new ArrayList<>();
            Database db = Database.getDatabaseInstance(context);
            paintings = db.paintingDAO().getPaintingForChallenge(integers[0]);
            return paintings;
        }

        @Override
        protected void onPostExecute(List<Painting> paintingList) {
            communicator.setPainting(paintingList.get(0));
        }
    }

    class UpdateScoreTask extends AsyncTask<User, Void, Void> {

        @Override
        protected Void doInBackground(User... users) {
            Database db = Database.getDatabaseInstance(context);
            db.userDAO().updateUser(users[0]);
            return null;
        }
    }

    class UpdateChallengeTurnTask extends AsyncTask<Challenge, Void, Void> {

        @Override
        protected Void doInBackground(Challenge... challenges) {
            Database db = Database.getDatabaseInstance(context);
            db.challengeDAO().updateChallenge(challenges[0]);
            return null;
        }
    }

    class DeletePaintingTask extends AsyncTask<Painting, Void, Void> {

        @Override
        protected Void doInBackground(Painting... paintings) {
            Database db = Database.getDatabaseInstance(context);
            db.paintingDAO().deletePainting(paintings[0]);
            return null;
        }
    }

}
