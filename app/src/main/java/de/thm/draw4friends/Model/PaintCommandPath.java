package de.thm.draw4friends.Model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by yannikstenzel on 27.02.18.
 */

public class PaintCommandPath extends PaintCommand {

    private Path path;
    private Paint paint;
    private float mX, mY;
    private static final float TOLERANCE = 5;

    public PaintCommandPath(Paint paint){
        path = new Path();
        this.paint = paint;
    }

    @Override
    public void draw(Canvas canvas){
        canvas.drawPath(path, paint);
    }

    @Override
    public void startTouch(float x, float y) {
        path.moveTo(x, y);
        mX = x;
        mY = y;
    }

    @Override
    public void moveTouch(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOLERANCE || dy >= TOLERANCE) {
            path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    @Override
    public void upTouch() {
        path.lineTo(mX, mY);
    }

}
