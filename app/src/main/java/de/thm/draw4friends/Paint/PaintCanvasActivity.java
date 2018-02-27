package de.thm.draw4friends.Paint;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;

import de.thm.draw4friends.R;

/**
 * Created by yannikstenzel on 27.02.18.
 */

public class PaintCanvasActivity extends AppCompatActivity {
    private CanvasView customCanvas;

    //Tools
    private ObserverImageButton brushButton, circleButton, squareButton, undoButton;

    //Colors
    private ImageButton blackButton, whiteButton, redButton, yellowButton, greenButton, blueButton, brownButton, skinButton;

    private ObserverTextView colorText;

    private SubjectColor currentColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paint_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        customCanvas = (CanvasView) findViewById(R.id.signature_canvas);
        currentColor = new SubjectColor(getColorString(ContextCompat.getColor(this, R.color.black)));

        ToolButtonListener toolButtonListener = new ToolButtonListener();
        this.brushButton = findViewById(R.id.brushTool);
        this.brushButton.setOnClickListener(toolButtonListener);
        this.circleButton = findViewById(R.id.circleTool);
        this.circleButton.setOnClickListener(toolButtonListener);
        this.squareButton = findViewById(R.id.squareTool);
        this.squareButton.setOnClickListener(toolButtonListener);
        this.undoButton = findViewById(R.id.undoButton);
        this.undoButton.setOnClickListener(toolButtonListener);

        this.colorText = findViewById(R.id.colorTextView);

        currentColor.registerObserver(brushButton);
        currentColor.registerObserver(circleButton);
        currentColor.registerObserver(squareButton);
        currentColor.registerObserver(colorText);
        currentColor.notifyObservers();

        ColorButtonListener colorButtonListener = new ColorButtonListener();
        this.blackButton = findViewById(R.id.colorBlack);
        this.blackButton.setOnClickListener(colorButtonListener);
        this.whiteButton = findViewById(R.id.colorWhite);
        this.whiteButton.setOnClickListener(colorButtonListener);
        this.redButton = findViewById(R.id.colorRed);
        this.redButton.setOnClickListener(colorButtonListener);
        this.yellowButton = findViewById(R.id.colorYellow);
        this.yellowButton.setOnClickListener(colorButtonListener);
        this.greenButton = findViewById(R.id.colorGreen);
        this.greenButton.setOnClickListener(colorButtonListener);
        this.blueButton = findViewById(R.id.colorBlue);
        this.blueButton.setOnClickListener(colorButtonListener);
        this.brownButton = findViewById(R.id.colorBrown);
        this.brownButton.setOnClickListener(colorButtonListener);
        this.skinButton = findViewById(R.id.colorSkin);
        this.skinButton.setOnClickListener(colorButtonListener);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.paint_menu, menu);
        return true;
    }

    private String getColorString(Integer color) {
        String colorString = Integer.toHexString(color);
        return "#" + colorString.substring(2);
    }

    public void clearCanvas(View v) {
        customCanvas.clearCanvas();
    }

    class ColorButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int color;
            switch (v.getId()) {
                case R.id.colorBlack:
                    color = ContextCompat.getColor(PaintCanvasActivity.this, R.color.black);
                    break;
                case R.id.colorWhite:
                    color = ContextCompat.getColor(PaintCanvasActivity.this, R.color.white);
                    break;
                case R.id.colorBlue:
                    color = ContextCompat.getColor(PaintCanvasActivity.this, R.color.blue);
                    break;
                case R.id.colorRed:
                    color = ContextCompat.getColor(PaintCanvasActivity.this, R.color.red);
                    break;
                case R.id.colorYellow:
                    color = ContextCompat.getColor(PaintCanvasActivity.this, R.color.yellow);
                    break;
                case R.id.colorGreen:
                    color = ContextCompat.getColor(PaintCanvasActivity.this, R.color.green);
                    break;
                case R.id.colorBrown:
                    color = ContextCompat.getColor(PaintCanvasActivity.this, R.color.brown);
                    break;
                case R.id.colorSkin:
                    color = ContextCompat.getColor(PaintCanvasActivity.this, R.color.skin);
                    break;
                default:
                    color = ContextCompat.getColor(PaintCanvasActivity.this, R.color.black);
                    Log.e("DEFAULT ", "what");
            }
            currentColor.setColor(getColorString(color));
            customCanvas.setColor(color);
        }
    }

    class ToolButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v){
            switch (v.getId()) {
                case R.id.brushTool:
                    customCanvas.changeTool(0);
                    break;
                case R.id.circleTool:
                    customCanvas.changeTool(1);
                    break;
                case R.id.squareTool:
                    customCanvas.changeTool(2);
                    break;
                case R.id.undoButton:
                    break;
                default:
                    break;
            }
        }
    }
}
