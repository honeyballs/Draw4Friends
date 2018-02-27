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

    private SubjectColor currentColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paint_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        customCanvas = (CanvasView) findViewById(R.id.signature_canvas);

        currentColor = new SubjectColor(getColorString(ContextCompat.getColor(this, R.color.red)));

        this.brushButton = findViewById(R.id.brushTool);
        this.circleButton = findViewById(R.id.circleTool);
        this.squareButton = findViewById(R.id.squareTool);
        this.undoButton = findViewById(R.id.undoButton);

        currentColor.registerObserver(brushButton);
        currentColor.registerObserver(circleButton);
        currentColor.registerObserver(squareButton);
        currentColor.notifyObservers();

        this.blackButton = findViewById(R.id.colorBlack);
        this.whiteButton = findViewById(R.id.colorWhite);
        this.redButton = findViewById(R.id.colorRed);
        this.yellowButton = findViewById(R.id.colorYellow);
        this.greenButton = findViewById(R.id.colorGreen);
        this.blueButton = findViewById(R.id.colorBlue);
        this.brownButton = findViewById(R.id.colorBrown);
        this.skinButton = findViewById(R.id.colorSkin);
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
}
