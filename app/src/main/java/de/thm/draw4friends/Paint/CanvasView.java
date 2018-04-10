package de.thm.draw4friends.Paint;

import android.view.MotionEvent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import de.thm.draw4friends.Model.PaintCommand;
import de.thm.draw4friends.Model.PaintCommandCirc;
import de.thm.draw4friends.Model.PaintCommandPath;
import de.thm.draw4friends.Model.PaintCommandRect;

/**
 * Created by yannikstenzel on 27.02.18.
 */

public class CanvasView extends View {

    public static final int PAINTCOMMAND_PATH = 0;
    public static final int PAINTCOMMAND_CIRC = 1;
    public static final int PAINTCOMMAND_RECT = 2;

    private Bitmap mBitmap;
    private Canvas mCanvas;
    Context context;
    private Paint paint;
    private int color;
    private PaintCommand pc;
    private List<PaintCommand> commandList;
    private int activeTool;


    public CanvasView(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(4f);

        color = Color.BLACK;

        pc = new PaintCommandPath(paint, color);

        commandList = new ArrayList<>();

    }

    public void setColor(int color) {
        this.color = color;
        changeTool(activeTool);
    }

    public List<PaintCommand> getCommandList() {
        return commandList;
    }

    public void changeTool(int tool) {
        activeTool = tool;
        switch (tool){
            case PAINTCOMMAND_PATH:
                pc = new PaintCommandPath(paint, color);
                break;
            case PAINTCOMMAND_CIRC:
                pc = new PaintCommandCirc(paint, color);
                break;
            case PAINTCOMMAND_RECT:
                pc = new PaintCommandRect(paint, color);
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
        repaintCommandList();
        pc.draw(mCanvas);
        canvas.drawBitmap(mBitmap, 0, 0, null);
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
                commandList.add(pc);
                invalidate();
                changeTool(activeTool);
                break;
        }
        return true;
    }

    public boolean undoPaintCommand() {
        if (undoAvailable()){
            commandList.remove(commandList.size() - 1);
            repaintCommandList();
            return true;
        }
        return false;
    }

    public boolean undoAvailable() {
        if (commandList.size() > 0) return true;
        return false;
    }

    public void repaintCommandList() {
        mCanvas.drawColor(Color.WHITE);
        for (PaintCommand command: commandList) {
            command.draw(mCanvas);
        }
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bmp) {
        mBitmap = bmp;
        mCanvas.drawBitmap(bmp, 0, 0, null);
    }

    public void addCommands(List<PaintCommand> commands) {
        this.commandList.addAll(commands);
    }

    public void addCommand(PaintCommand command) {
        this.commandList.add(command);
    }

    public void clearCommands() {
        this.commandList.clear();
    }

}
