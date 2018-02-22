package de.thm.draw4friends.Friendlist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by yannikstenzel on 22.02.18.
 */

public class FABBehavior extends FloatingActionButton.Behavior {
    //Standard Konstruktor
    public FABBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    //Da wir auf Scrollen reagieren wird die onNestedScroll Methode überschrieben
    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                               @NonNull FloatingActionButton child,
                               @NonNull View target,
                               int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed, int type)
    {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed,
                dyConsumed, dxUnconsumed, dyUnconsumed, type);
        //Child ist hier der Floating Action Button
        //Beim nach oben scrollen (y>0) wird der Button angezeigt
        //(nach oben geschoben)
        if (dyConsumed > 0) {
            CoordinatorLayout.LayoutParams layoutParams =
                    (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            //Der Wert, um den der Button nach oben geschoben werden muss
            //ergibt sich aus der Höhe des Buttons + bottom margin
            int bottomMargin = layoutParams.bottomMargin;
            //Die tatsächliche Animation
            child.animate().translationY(child.getHeight() + bottomMargin)
                    .setInterpolator(new LinearInterpolator()).start();
        } else if (dyConsumed < 0) {
            //Zieht den Button nach unten aus der View
            child.animate().translationY(0)
                    .setInterpolator(new LinearInterpolator()).start();
        } }
    //Diese Methode entscheidet, ob wir auf scrolling reagieren oder nicht.
    //Wir reagieren nur, falls vertikal gescrollt wird
    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                       @NonNull FloatingActionButton child,
                                       @NonNull View directTargetChild,
                                       @NonNull View target, int axes, int type)
    {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }
}
