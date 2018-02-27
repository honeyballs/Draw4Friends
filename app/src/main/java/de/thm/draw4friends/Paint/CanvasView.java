package de.thm.draw4friends.Paint;

import android.view.MotionEvent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import de.thm.draw4friends.Model.PaintCommand;
import de.thm.draw4friends.Model.PaintCommandCirc;
import de.thm.draw4friends.Model.PaintCommandPath;
import de.thm.draw4friends.Model.PaintCommandRect;

/**
 * Created by yannikstenzel on 27.02.18.
 */

public class CanvasView extends View {

    private Bitmap mBitmap;
    private Canvas mCanvas;
    Context context;
    private Paint paint;

    private PaintCommand pc;

    public CanvasView(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(4f);

        pc = new PaintCommandPath(paint);
    }

    public void setColor(int color) {
        paint.setColor(color);
    }

    public void changeTool(int tool) {
        switch (tool){
            case 0:
                pc = new PaintCommandPath(paint);
                break;
            case 1:
                pc = new PaintCommandCirc(paint);
                break;
            case 2:
                pc = new PaintCommandRect(paint);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Canvas will draw onto the defined Bitmap
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        pc.draw(canvas);
    }

    public void clearCanvas() {
        //mPath.reset();
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pc.startTouch(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                pc.moveTouch(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                pc.upTouch();
                invalidate();
                break;
        }
        return true;
    }
}
