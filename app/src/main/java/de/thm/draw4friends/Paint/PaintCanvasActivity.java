package de.thm.draw4friends.Paint;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import de.thm.draw4friends.Model.Challenge;
import de.thm.draw4friends.Model.Painting;
import de.thm.draw4friends.R;
import de.thm.draw4friends.Server.PaintingService;
import de.thm.draw4friends.Server.ServiceFacade;

import static java.lang.Math.toIntExact;

/**
 * Created by yannikstenzel on 27.02.18.
 */

public class PaintCanvasActivity extends AppCompatActivity implements PaintCommunicator {

    private Challenge challenge;
    private CanvasView customCanvas;

    //Tools
    private ObserverImageButton brushButton, circleButton, squareButton, undoButton;

    //Colors
    private ImageButton blackButton, whiteButton, redButton, yellowButton, greenButton, blueButton, brownButton, skinButton;

    private ObserverTextView colorText;

    private SubjectColor currentColor;

    private Painting currentPainting;
    private PaintingService service;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.service = new PaintingService(this);

        setContentView(R.layout.paint_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        customCanvas = (CanvasView) findViewById(R.id.signature_canvas);
        CanvasListener canvasListener = new CanvasListener();
        customCanvas.setOnTouchListener(canvasListener);
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
        this.undoButton.setEnabled(false);

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

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            challenge = bundle.getParcelable(getString(R.string.challenge_obj));
        }

        currentPainting = new Painting();
    }

    @Override
    public boolean onSupportNavigateUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.cancel_drawing);
        builder.setMessage(R.string.cancel_drawing_msg);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onBackPressed();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.paint_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.start_challenge:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.startChallenge);
                builder.setMessage(R.string.startChallengeMsg);
                builder.setPositiveButton(R.string.startChallengeSubmit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        currentPainting.setPaintCommandListString(customCanvas.getCommandList());
                        if (challenge.getId() <= 0) {
                            service.createChallenge(challenge);
                        } else {
                            currentPainting.setChallengeId(challenge.getId());
                            service.setPainting(currentPainting);
                        }
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //TODO: Check if word is already set
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.enter_word);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.textfield_dialog_layout, null);
        final TextView textView = dialogView.findViewById(R.id.dialogTextView);
        textView.setText(R.string.enter_word_hint);
        final EditText wordText = dialogView.findViewById(R.id.dialogEditText);
        wordText.setHint(R.string.word);
        builder.setView(dialogView);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO: Save word in drawing instance
                currentPainting.setDescription(wordText.getText().toString());
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                onSupportNavigateUp();
            }
        });
        builder.show();

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
                    customCanvas.changeTool(CanvasView.PAINTCOMMAND_PATH);
                    break;
                case R.id.circleTool:
                    customCanvas.changeTool(CanvasView.PAINTCOMMAND_CIRC);
                    break;
                case R.id.squareTool:
                    customCanvas.changeTool(CanvasView.PAINTCOMMAND_RECT);
                    break;
                case R.id.undoButton:
                    customCanvas.undoPaintCommand();
                    undoButton.setEnabled(customCanvas.undoAvailable());
                    break;
                default:
                    break;
            }
        }
    }

    class CanvasListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent me) {
            if (me.getAction() == MotionEvent.ACTION_UP) {
                undoButton.setEnabled(true);
            }
            return false;
        }

    }

//    @Override
//    public void getPainting(Painting painting) {
//
//    }

    @Override
    public void setChallengeIdAndPainting(long id) {
        challenge.setId(toIntExact(id));
        currentPainting.setChallengeId(challenge.getId());
        service.setPainting(currentPainting);
    }
//
//    @Override
//    public void setPainting() {
//        currentPainting.setChallengeId(challenge.getId());
//        serviceFacade.setPainting(currentPainting);
//    }

    @Override
    public void notifyChallengeStarted() {
        Toast.makeText(PaintCanvasActivity.this, R.string.challengeStarted, Toast.LENGTH_LONG).show();
        onBackPressed();
    }
}
