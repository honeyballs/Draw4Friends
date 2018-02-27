package de.thm.draw4friends.Paint;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;

import de.thm.draw4friends.Model.Observer;

/**
 * Created by Yannick Bals on 27.02.2018.
 */

public class ObserverImageButton extends android.support.v7.widget.AppCompatImageButton implements Observer {

    public ObserverImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ObserverImageButton(Context context) {
        super(context);
    }

    public ObserverImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void update(final String color) {
        int[][] state = new int[][] {
                new int[] {android.R.attr.state_enabled}
        };
        int[] colorArr = new int[] {Color.parseColor(color)};
        this.setSupportImageTintList(new ColorStateList(state, colorArr));
    }
}
