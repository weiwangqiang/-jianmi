package com.example.main;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Created by wangqiang on 2016/9/23.
 */
public class myScroller extends Scroller {
    private int mduration = 2000;
    public myScroller(Context context) {
        super(context);
    }

    public myScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }
    /**
     * Create a Scroller with the specified interpolator. If the interpolator is
     * null, the default (viscous) interpolator will be used. Specify whether or
     * not to support progressive "flywheel" behavior in flinging.
     *
     * @param context
     * @param interpolator
     * @param flywheel
     */
    public myScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }
    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
// Ignore received duration, use fixed one instead
        super.startScroll(startX, startY, dx, dy, mduration);
    }
    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
// Ignore received duration, use fixed one instead
        super.startScroll(startX, startY, dx, dy, mduration);
    }
}
