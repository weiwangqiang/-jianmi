package com.example.mytools;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * class description here
 *
 * @version 1.0.0
 * @outher wangqiang
 * @project jianmi1
 * @since 2017-02-17
 */
public class MycarView extends CardView {
    private String TAG = "MycarView";

    public MycarView(Context context) {
        super(context);
    }

    public MycarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MycarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return true;
    }
}
