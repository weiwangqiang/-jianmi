package com.example.dao;

import android.app.Activity;

/**
 * Created by wangqiang on 2016/12/31.
 */

public interface UserState {
    public void apply(Activity activity, int jid, int bid);
    public void applyRecord(Activity activity);
    public void shareJz(Activity activity);//分享兼职
    public void shareRecord(Activity activity);//分享兼职
    public void collectRecord(Activity activity);
    public void feedBack(Activity activity);
    public void realName(Activity activity);
    public void resume(Activity activity);
    public void health(Activity activity);
    public void practice(Activity activity);
    public void jobWant(Activity activity);
    public void jianZhiTui(Activity activity);
}
