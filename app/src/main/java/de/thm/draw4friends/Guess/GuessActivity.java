package de.thm.draw4friends.Guess;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

import de.thm.draw4friends.Model.Challenge;
import de.thm.draw4friends.Model.PaintCommand;
import de.thm.draw4friends.Model.PaintCommandCirc;
import de.thm.draw4friends.Model.PaintCommandRect;
import de.thm.draw4friends.Model.Painting;
import de.thm.draw4friends.Model.User;
import de.thm.draw4friends.Paint.CanvasView;
import de.thm.draw4friends.R;
import de.thm.draw4friends.Server.GuessService;
import de.thm.draw4friends.Stats.StatsActivity;

/**
 * Created by Yannick Bals on 19.03.2018.
 */

public class GuessActivity extends AppCompatActivity implements GuessCommunicator{

    private User user;
    private Challenge currentChallenge = null;
    private Painting painting;
    private List<PaintCommand> commandList = null;
    private GuessService service;

    private CanvasView canvasView;
    private TextView pauseButton;
    private EditText guessAnswerEdit;

    private Handler handler;
    private Runnable paintRunnable;
    private int iterationCounter = 0;
    private long waitPerCommand = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.guess_layout);

        this.service = new GuessService(this);

        this.canvasView = findViewById(R.id.signature_canvas);
        this.pauseButton = findViewById(R.id.pauseButton);
        this.guessAnswerEdit = findViewById(R.id.answerGuessEdit);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            user = bundle.getParcelable(getString(R.string.user_obj));
            currentChallenge = bundle.getParcelable(getString(R.string.challenge_obj));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (currentChallenge != null) {
            service.getPainting(currentChallenge.getId());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.guess_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.send_answer:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.send_title);
                builder.setMessage(R.string.send_answer_msg);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (guessAnswerEdit.getText().toString() != null && !guessAnswerEdit.getText().toString().equals("")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(GuessActivity.this);
                            if (guessAnswerEdit.getText().toString().equals(painting.getDescription())) {
                                builder.setTitle(getString(R.string.answer_right_title));
                                builder.setMessage(getString(R.string.answer_right_msg) + " " + calculatePoints() + " Points");
                            } else {
                                builder.setTitle(getString(R.string.answer_wrong_title));
                                builder.setMessage(getString(R.string.answer_wrong_msg));
                            }
                            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    user.setScore(user.getScore() + calculatePoints());
                                    service.updateScore(user);
                                    currentChallenge.setTurnOff(user.getUId());
                                    service.updateChallengeTurn(currentChallenge);
                                    service.deletePainting(painting);
                                    Intent intent = new Intent(GuessActivity.this, StatsActivity.class);
                                    intent.putExtra(getString(R.string.user_obj), user);
                                    startActivity(intent);
                                }
                            });
                            builder.show();
                        } else {
                            Toast.makeText(GuessActivity.this, "Please enter an answer.", Toast.LENGTH_SHORT).show();
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
    public void setPainting(Painting painting) {
        this.painting = painting;
        //Create a dialog to start the timer
        if (this.painting != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.start_timer);
            builder.setMessage(R.string.start_timer_msg);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startTimer();
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    onBackPressed();
                }
            });
            builder.show();
        }
    }

    private int calculatePoints() {
        return 534;
    }

    private void startTimer() {
        iterationCounter = 0;
        handler = new Handler();
        Log.e("Paint commands", painting.getPaintCommandListString());
        commandList = painting.getPaintCommandLists();
        canvasView.clearCanvas();
        canvasView.clearCommands();
        if (commandList != null && commandList.size() > 0) {

            /*for (PaintCommand command : commandList) {
                Log.e("Command: ", command.toString());
                canvasView.addCommand(commandList.get(iterationCounter));
                canvasView.repaintCommandList();
            }*/
            //canvasView.addCommands(commandList);
            //canvasView.repaintCommandList();
/*
            waitPerCommand = 60000/commandList.size();
            paintRunnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        addCommandAndPaint();
                        iterationCounter += 1;
                    } finally {
                        handler.postDelayed(paintRunnable, waitPerCommand);
                    }
                }
            };
            paintRunnable.run();*/
        }
    }

    private void pauseTimer() {
        handler.removeCallbacks(paintRunnable);
    }

    private void continueTimer() {
        handler = new Handler();
        paintRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    addCommandAndPaint();
                    handler.postDelayed(paintRunnable, waitPerCommand);
                } finally {
                    Log.e("Finally", "");

                }
            }
        };
        paintRunnable.run();
    }


    private void addCommandAndPaint() {
        if (iterationCounter < commandList.size()) {
            Log.e("Command: ", commandList.get(iterationCounter).toString());
            Log.e("iteration", ""+iterationCounter);
            canvasView.addCommand(commandList.get(iterationCounter));
            canvasView.repaintCommandList();
            iterationCounter++;
        } else {
            handler.removeCallbacks(paintRunnable);
            //TODO 30 second timer
        }
    }



}
