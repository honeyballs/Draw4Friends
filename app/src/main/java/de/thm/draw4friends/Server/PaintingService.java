package de.thm.draw4friends.Server;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import de.thm.draw4friends.Database.Database;
import de.thm.draw4friends.Home.HomeCommunicator;
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

    public void getPainting(int callengeid) {

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
            communicator.loadPainting(paintingList.get(0));
        }
    }
}
