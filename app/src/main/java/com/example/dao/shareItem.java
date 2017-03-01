package com.example.dao;

import android.app.Activity;
import android.content.Context;

import com.example.control.tencentBaseUiLister;

/**
 * Created by wangqiang on 2016/12/31.
 */

public interface shareItem {
    //分享到QQ空间
    void shareToQzone(Activity activity, tencentBaseUiLister lister);
    //分享到QQ好友
    void shareToQQ(Activity activity, tencentBaseUiLister lister);
    //分享到微信
    void shareTowei(Context context);
    //分享到微信朋友圈
    void shareTopyq(Context context);
}
