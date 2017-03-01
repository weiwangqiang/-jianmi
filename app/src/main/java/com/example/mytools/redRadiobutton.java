package com.example.mytools;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;

/**
 * Created by wangqiang on 2016/7/5.
 */
public class redRadiobutton extends RadioButton {
    public redRadiobutton(Context context) {
        super(context);
    }

    public redRadiobutton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public redRadiobutton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    public boolean isChecked() {
        return super.isChecked();
    }

    @Override
    public void setButtonDrawable(int resid) {
        super.setButtonDrawable(resid);
    }
}
