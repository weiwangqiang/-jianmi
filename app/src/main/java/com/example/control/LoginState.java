package com.example.control;

import android.app.Activity;
import android.content.Intent;

import com.example.dao.UserState;
import com.example.my.myApply;
import com.example.my.myCollect;
import com.example.my.myHealth;
import com.example.my.myShare;
import com.example.my.myShixi;
import com.example.my.mySuggest;
import com.example.my.myWorked;
import com.example.my.partTime;
import com.example.my.realName;
import com.example.wangqiang.jianmi1.R;
import com.example.wangqiang.jianmi1.apply;

/**登录状态
 * Created by wangqiang on 2016/12/31.
 */

public class LoginState implements UserState {
    /**
     *申请兼职
     */
    @Override
    public void apply(Activity activity, int jid, int bid) {
        Intent intent = new Intent(activity,apply.class);
        intent.putExtra("jid",jid);
        intent.putExtra("bid",bid);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    /**
     * 申请记录
     */
    @Override
    public void applyRecord(Activity activity) {
        Intent intent = new Intent(activity,myApply.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    /**
     * 分享兼职
     */
    @Override
    public void shareJz(Activity activity) {

    }

    @Override
    public void shareRecord(Activity activity) {

        Intent intent = new Intent(activity,myShare.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }


    @Override
    public void collectRecord(Activity activity) {
        Intent intent = new Intent(activity,myCollect.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void feedBack(Activity activity) {
        Intent intent = new Intent(activity,mySuggest.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void realName(Activity activity) {
        Intent intent = new Intent(activity,realName.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    /**
     * 简历
     * @param activity
     */
    @Override
    public void resume(Activity activity) {
        Intent intent = new Intent(activity,myWorked.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    /**
     * 健康证
     * @param activity
     */
    @Override
    public void health(Activity activity) {
        Intent intent = new Intent(activity,myHealth.class);

        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void practice(Activity activity) {
        Intent intent = new Intent(activity,myShixi.class);

        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    /**
     * 求职意向
     * @param activity
     */
    @Override
    public void jobWant(Activity activity) {
        Intent intent = new Intent(activity,partTime.class);

        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void jianZhiTui(Activity activity) {
        Intent intent = new Intent(activity,myApply.class);

        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

}
