package com.example.control;

import android.app.Activity;
import android.content.Intent;

import com.example.dao.UserState;
import com.example.my.login;
import com.example.wangqiang.jianmi1.R;

/**
 * Created by wangqiang on 2016/12/31.
 */

public class LogoutState implements UserState {
    @Override
    public void apply(Activity activity, int jid, int bid) {
        Login(activity);

    }

    @Override
    public void applyRecord(Activity activity) {
        Login(activity);
    }

    @Override
    public void shareJz(Activity activity) {
        Login(activity);
    }

    @Override
    public void shareRecord(Activity activity) {
        Login(activity);

    }

    @Override
    public void collectRecord(Activity activity) {
        Login(activity);

    }

    @Override
    public void feedBack(Activity activity) {
        Login(activity);

    }

    @Override
    public void realName(Activity activity) {
        Login(activity);

    }

    @Override
    public void resume(Activity activity) {
        Login(activity);

    }

    @Override
    public void health(Activity activity) {
        Login(activity);

    }

    @Override
    public void practice(Activity activity) {
        Login(activity);

    }

    @Override
    public void jobWant(Activity activity) {
        Login(activity);

    }

    @Override
    public void jianZhiTui(Activity activity) {
        Login(activity);
    }
    public void Login(Activity activity){
        Intent intentJian = new Intent(activity,login.class);
        activity.startActivity(intentJian);
        activity.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }
}
