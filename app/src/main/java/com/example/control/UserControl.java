package com.example.control;

import android.app.Activity;

import com.example.dao.UserState;
import com.example.object.User;

/**用户状态管理
 * Created by wangqiang on 2016/12/31.
 */

public class UserControl implements UserState {
    static UserControl userControl = new UserControl();//单列
    static User user = new User();

    UserState userState = new LogoutState();//默认为未登录的状态
    private UserControl(){

    }
    public static  UserControl getUserControl(){
        return userControl;
    }

    public void setState(UserState userState){
        this.userState = userState;
    }
    public boolean isLogin(){
        return this.userState.getClass() ==LoginState.class;
    }
    public void setUser(User user){
        this.user = user;
    }
    public User getUser(){
        return user;
    }
    @Override
    public void apply(Activity activity, int jid, int bid) {
        userState.apply(activity,jid,bid);
    }

    @Override
    public void applyRecord(Activity activity) {
        userState.applyRecord(activity);
    }

    @Override
    public void shareJz(Activity activity) {
        userState.shareJz(activity);
    }

    @Override
    public void shareRecord(Activity activity) {
        userState.shareRecord(activity);
    }

    @Override
    public void collectRecord(Activity activity) {
        userState.collectRecord(activity);
    }

    @Override
    public void feedBack(Activity activity) {
            userState.feedBack(activity);
    }

    @Override
    public void realName(Activity activity) {
            userState.realName(activity);
    }

    @Override
    public void resume(Activity activity) {
            userState.resume(activity);
    }

    @Override
    public void health(Activity activity) {
            userState.health(activity);
    }

    @Override
    public void practice(Activity activity) {
            userState.practice(activity);
    }

    @Override
    public void jobWant(Activity activity) {
            userState.jobWant(activity);
    }

    @Override
    public void jianZhiTui(Activity activity) {
            userState.jianZhiTui(activity);
    }
}
