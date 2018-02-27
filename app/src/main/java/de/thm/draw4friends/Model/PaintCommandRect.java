package de.thm.draw4friends.Model;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by yannikstenzel on 27.02.18.
 */

public class PaintCommandRect extends PaintCommand {

    private Paint paint;
    private float initX;
    private float initY;
    private float mX, mY;
    private static final float TOLERANCE = 5;

    public PaintCommandRect(Paint paint){
        this.paint = paint;
    }

    @Override
    public void draw(Canvas canvas){
        canvas.drawRect(initX,initY,mX,mY,paint);
    }

    @Override
    public void startTouch(float x, float y) {
        initX = x;
        initY = y;
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
