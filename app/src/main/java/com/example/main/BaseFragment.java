package com.example.main;

import android.support.v4.app.Fragment;

import com.example.control.UserControl;

/**
 * class description here
 *
 * @version 1.0.0
 * @outher wangqiang
 * @project jianmi1
 * @since 2017-02-19
 */
public abstract class BaseFragment extends Fragment {
    private String TAG = "BaseFragment";
    public UserControl userControl = UserControl.getUserControl();

}
