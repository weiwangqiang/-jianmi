package com.example.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.control.UserControl;

/**
 * class description here
 *
 * @version 1.0.0
 * @outher wangqiang
 * @project jianmi1
 * @since 2017-02-19
 */

public abstract class BaseActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "BaseActivity";
    public UserControl userControl = UserControl.getUserControl();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    /**
     * Called when an activity you launched exits
     */
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}