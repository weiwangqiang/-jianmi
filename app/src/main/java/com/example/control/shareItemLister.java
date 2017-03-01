package com.example.control;

import android.app.Activity;
import android.content.Context;

import com.example.dao.shareItem;

/**
 * Created by wangqiang on 2016/12/31.
 */

public class shareItemLister implements shareItem {

    @Override
    public void shareToQzone(Activity activity, tencentBaseUiLister lister) {
        QQShare qq = new QQShare(activity,lister);
        qq.shareToZone();
    }

    @Override
    public void shareToQQ(Activity activity, tencentBaseUiLister lister) {
        QQShare qq = new QQShare(activity,lister);
        qq.shareToPY();
    }

    @Override
    public void shareTowei(Context context) {
        WXShare wx = new WXShare(context);
        wx.shareToZone();
    }

    @Override
    public void shareTopyq(Context context) {
        WXShare wx = new WXShare(context);
        wx.shareToPY();
    }
}
