package de.thm.draw4friends.Paint;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import de.thm.draw4friends.Model.Observer;
import de.thm.draw4friends.R;

/**
 * Created by Yannick Bals on 27.02.2018.
 */

public class ObserverTextView extends android.support.v7.widget.AppCompatTextView implements Observer {


    public ObserverTextView(Context context) {
        super(context);
    }

    public ObserverTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObserverTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void update(String color) {
        String part1 = getResources().getString(R.string.current_color);
        switch(color.toLowerCase()) {
            case "#000000":
                setText(part1 + " " + getResources().getString(R.string.black));
                break;
            case "#000":
                setText(part1 + " " + getResources().getString(R.string.black));
                break;
            case "#ffffff":
                setText(part1 + " " + getResources().getString(R.string.white));
                break;
            case "#fff":
                setText(part1 + " " + getResources().getString(R.string.white));
                break;
            case "#ff0000":
                setText(part1 + " " + getResources().getString(R.string.red));
                break;
            case "#0000ff":
                setText(part1 + " " + getResources().getString(R.string.blue));
                break;
            case "#ffff00":
                setText(part1 + " " + getResources().getString(R.string.yellow));
                break;
            case "#7cfc00":
                setText(part1 + " " + getResources().getString(R.string.green));
                break;
            case "#8b4513":
                setText(part1 + " " + getResources().getString(R.string.brown));
                break;
            case "#ffe4c4":
                setText(part1 + " " + getResources().getString(R.string.pink));
                break;
            default:
                break;
        }
    }
}
