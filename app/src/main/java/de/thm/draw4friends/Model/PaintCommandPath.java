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
    private int color;
    private float mX, mY;

    public PaintCommandPath(Paint paint, int color){
        super("PaintCommandPath");
        path = new Path();
        this.paint = paint;
        this.color = color;
    }

    @Override
    public void draw(Canvas canvas){
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
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
