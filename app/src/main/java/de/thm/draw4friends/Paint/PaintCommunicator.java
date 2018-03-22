package de.thm.draw4friends.Paint;

import de.thm.draw4friends.Model.Painting;

/**
 * Created by yannikstenzel on 01.03.18.
 */

public interface PaintCommunicator {

//    void getPainting(Painting painting);

//    void setPainting();

    void setChallengeIdAndPainting(long id);

    void notifyChallengeStarted();
}
