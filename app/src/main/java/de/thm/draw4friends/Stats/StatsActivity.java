package de.thm.draw4friends.Stats;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TableLayout;
import android.widget.TextView;

import de.thm.draw4friends.Model.User;
import de.thm.draw4friends.R;

/**
 * Created by Yannick Bals on 22.03.2018.
 */

public class StatsActivity extends AppCompatActivity {

    private User user;

    private TextView pointsView;
    private TableLayout highscoreTable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.stats_layout);

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
}
