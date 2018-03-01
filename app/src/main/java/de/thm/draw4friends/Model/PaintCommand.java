package de.thm.draw4friends.Model;

import android.graphics.Canvas;

/**
 * Created by yannikstenzel on 27.02.18.
 */

public abstract class PaintCommand {
    public static final float TOLERANCE = 5;

    public void draw(Canvas canvas){}

    // when ACTION_DOWN start touch according to the x,y values
    public void startTouch(float x, float y) {
    }

    // when ACTION_MOVE move touch according to the x,y values
    public void moveTouch(float x, float y) {
    }

    // when ACTION_UP stop touch
    public void upTouch() {
    }
}
