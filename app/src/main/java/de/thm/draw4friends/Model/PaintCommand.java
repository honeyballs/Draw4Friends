package de.thm.draw4friends.Model;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by yannikstenzel on 27.02.18.
 */

public abstract class PaintCommand {

    public void draw(Canvas canvas){}

    public Paint newPaint(int color){
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(4f);
        return paint;
    }

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
