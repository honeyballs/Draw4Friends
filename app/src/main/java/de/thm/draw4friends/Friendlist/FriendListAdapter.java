package de.thm.draw4friends.Friendlist;

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
import de.thm.draw4friends.Model.FriendWithFriendshipId;
import de.thm.draw4friends.R;

/**
 * Created by Farea on 27.02.2018.
 */

public class FriendListAdapter extends ArrayAdapter<FriendWithFriendshipId> {

    public FriendListAdapter(Context context, List<FriendWithFriendshipId> friends) {
        super(context, 0, friends);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        FriendWithFriendshipId friend = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.challenge_listitem, parent, false);
        }

        TextView opponent = convertView.findViewById(R.id.opponentNameView);
        opponent.setVisibility(View.GONE);

        TextView friendName = convertView.findViewById(R.id.turnView);
        friendName.setText(friend.getUsername());

        return convertView;
    }
}
