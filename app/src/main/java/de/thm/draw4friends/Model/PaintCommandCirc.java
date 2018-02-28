package de.thm.draw4friends.Model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

/**
 * Created by yannikstenzel on 27.02.18.
 */

public class PaintCommandCirc extends PaintCommand {

    private Paint paint;
    private float initX, initY, mX, mY;
    private int color;

    public PaintCommandCirc(Paint paint, int color){
        this.paint = paint;
        this.color = color;
    }

    @Override
    public void draw(Canvas canvas){
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setColor(color);

        float diffX, diffY, radius;
        diffX = mX - initX;
        diffY = mY - initY;
        if (Math.abs(diffX) > Math.abs(diffY)) {
            radius = Math.abs(diffX) /2;
        } else {
            radius = Math.abs(diffY) /2;
        }

        canvas.drawCircle(
                Math.signum(diffX) * radius + initX,
                Math.signum(diffY) * radius + initY,
                radius,
                paint);
    }

    @Override
    public void startTouch(float x, float y) {
        initX = mX = x;
        initY = mY = y;
    }

    @Override
    public void moveTouch(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOLERANCE || dy >= TOLERANCE) {
            mX = x;
            mY = y;
        }
    }

    @Override
    public void upTouch() {

    }

}
