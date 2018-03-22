package de.thm.draw4friends.Stats;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import de.thm.draw4friends.Model.User;
import de.thm.draw4friends.R;
import de.thm.draw4friends.Server.StatsService;

/**
 * Created by Yannick Bals on 22.03.2018.
 */

public class StatsActivity extends AppCompatActivity implements StatsCommunicator {

    private User user;
    private List<User> friends = null;
    private StatsService service;

    private TextView pointsView;
    private TableLayout highscoreTable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.stats_layout);

        service = new StatsService(this);

        this.pointsView = findViewById(R.id.ownPointsView);
        this.highscoreTable = findViewById(R.id.highscore_table);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            user = bundle.getParcelable(getString(R.string.user_obj));
            this.pointsView.setText(""+user.getScore());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        service.getScoresOfFrieds(user.getUId());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void setFriendScores(List<User> friends) {
        if (friends != null && friends.size() > 0) {
            TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
            TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            for (User friend : friends) {
                TableRow row = new TableRow(this);
                row.setLayoutParams(tableParams);
                TextView nameView = new TextView(this);
                nameView.setLayoutParams(rowParams);
                nameView.setText(friend.getUsername());
                TextView scoreView = new TextView(this);
                scoreView.setLayoutParams(rowParams);
                scoreView.setText(""+user.getScore());
                row.addView(nameView);
                row.addView(scoreView);
                highscoreTable.addView(row);

            }
        }
    }
}
