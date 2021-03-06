package de.thm.draw4friends.Guess;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    private TextView pauseButton, timerView;
    private EditText guessAnswerEdit;

    private Handler handler;
    private Runnable paintRunnable;
    private Runnable timerRunnable;
    private int iterationCounter = 0;
    private int timer;
    private long waitPerCommand = 0;
    private boolean answerCorrect = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.guess_layout);

        this.service = new GuessService(this);

        this.canvasView = findViewById(R.id.signature_canvas);
        this.pauseButton = findViewById(R.id.pauseButton);
        this.guessAnswerEdit = findViewById(R.id.answerGuessEdit);
        this.timerView = findViewById(R.id.timerView);

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
                                answerCorrect = true;
                                builder.setTitle(getString(R.string.answer_right_title));
                                builder.setMessage(getString(R.string.answer_right_msg) + " " + calculatePoints() + " Points");
                            } else {
                                answerCorrect = false;
                                builder.setTitle(getString(R.string.answer_wrong_title));
                                builder.setMessage(getString(R.string.answer_wrong_msg));
                            }
                            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    if (answerCorrect) {
                                        user.setScore(user.getScore() + calculatePoints());
                                        service.updateScore(user);
                                    }
                                    currentChallenge.setTurnOff(user.getUId());
                                    handler.removeCallbacks(paintRunnable, timerRunnable);
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
        return timer * 100;
    }

    private void startTimer() {
        iterationCounter = 0;
        handler = new Handler();
        Log.e("Paint commands", painting.getPaintCommandListString());
        commandList = painting.getPaintCommandLists();
        canvasView.clearCanvas();
        canvasView.clearCommands();
        byte[] imageBytes = painting.getPainting();
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        canvasView.setBitmap(bitmap);
        if (commandList != null && commandList.size() > 0) {

            timer = commandList.size() * 3 + 30;
            timerView.setText(Integer.toString(timer));
            paintRunnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        addCommandAndPaint();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        handler.postDelayed(paintRunnable, 3000);
                    }
                }
            };
            timerRunnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        timer--;
                        timerView.setText(Integer.toString(timer));
                    } finally {
                        if (timer > 0) {
                            handler.postDelayed(timerRunnable, 1000);
                        } else {
                            handler.removeCallbacks(timerRunnable);
                        }
                    }
                }
            };
            handler.post(timerRunnable);
            handler.post(paintRunnable);
        }
    }

    private void pauseTimer() {
        handler.removeCallbacks(paintRunnable, timerRunnable);
    }

    private void continueTimer() {
        handler = new Handler();
        handler.postDelayed(paintRunnable, 3000);
        handler.postDelayed(timerRunnable, 1000);

    }


    private void addCommandAndPaint() {
        if (iterationCounter < commandList.size()) {
            /*Log.e("Command: ", commandList.get(iterationCounter).toString());
            Log.e("iteration", ""+iterationCounter);
            canvasView.addCommand(commandList.get(iterationCounter));
            canvasView.repaintCommandList();*/
            Log.e("Iteration nr: ", ""+iterationCounter);
            iterationCounter++;
        } else {
            handler.removeCallbacks(paintRunnable);
        }
    }



}
