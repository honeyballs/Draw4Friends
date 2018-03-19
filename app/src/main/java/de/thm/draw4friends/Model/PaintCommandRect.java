package de.thm.draw4friends.Model;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by yannikstenzel on 27.02.18.
 */

public class PaintCommandRect extends PaintCommand {

    private Paint paint;
    private float initX, initY, mX, mY;
    private int color;

    public PaintCommandRect(Paint paint, int color){
        super("PaintCommandRect");
        this.paint = paint;
        this.color = color;
    }

    @Override
    public void draw(Canvas canvas){
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(initX,initY,mX,mY,paint);
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
