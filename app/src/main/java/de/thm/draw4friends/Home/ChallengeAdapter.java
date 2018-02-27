package de.thm.draw4friends.Home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import de.thm.draw4friends.Model.Challenge;
import de.thm.draw4friends.R;

/**
 * Created by Yannick Bals on 22.02.2018.
 */

public class ChallengeAdapter extends ArrayAdapter<Challenge> {

    private int userId;

    public ChallengeAdapter(Context context, List<Challenge> challenges, int userId) {
        super(context, 0, challenges);
        this.userId = userId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Challenge challenge = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.challenge_listitem, parent, false);
        }

        TextView opponentName = convertView.findViewById(R.id.opponentNameView);
        opponentName.setText(challenge.getOpponentName());
        TextView turn = convertView.findViewById(R.id.turnView);
        if (challenge.getTurnOff() == userId) {
            turn.setText(R.string.turn_draw);
        } else {
            turn.setText(R.string.turn_guess);
        }

        return convertView;
    }
}
