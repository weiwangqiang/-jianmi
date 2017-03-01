package com.example.control;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.dao.share;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.Tencent;

import java.util.ArrayList;

/**
 * Created by wangqiang on 2016/12/31.
 */

public class QQShare implements share {
    //QQ
    private String APP_ID = "1105550774";
    private Tencent tencent;
    private Context context;
    private Activity activity;
    private QQShare qq;
    private tencentBaseUiLister lister;
    public QQShare(Activity activity,tencentBaseUiLister lister) {
        this.activity = activity;
        this.context = activity.getBaseContext();
        this.lister = lister;
        tencent = Tencent.createInstance(APP_ID, activity);
    }
    @Override
    public void shareToZone() {
        context= activity.getBaseContext();
        Toast.makeText(context,"正在跳转",Toast.LENGTH_SHORT).show();
        Log.e("share To QZone"," share to QZone is qq空间");
        Bundle params = new Bundle();
        ArrayList<String> list = new ArrayList<>();
        list.add("http://jzme.cn/images/job/7.png");
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "QQ空间兼职");//必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "我正在使用兼米兼职平台，你也快来加入吧");//选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, "http://jzme.cn/ListJobServlet?id=3&pNum=1");//必填
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL,list);
        tencent.shareToQzone(activity, params,new tencentBaseUiLister(context));
    }

    @Override
    public void shareToPY() {
        Toast.makeText(context,"正在跳转",Toast.LENGTH_SHORT).show();
        Log.e("share To QZone"," share to QZone is qq好友");
        Bundle params = new Bundle();
        params.putInt(com.tencent.connect.share.QQShare.SHARE_TO_QQ_KEY_TYPE, com.tencent.connect.share.QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_TITLE, "QQ好友兼职");
        params.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_SUMMARY,  "我正在使用兼米兼职平台，你也快来加入吧");
        params.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_TARGET_URL,  "http://jzme.cn/ListJobServlet?id=3&pNum=1");
        params.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_IMAGE_URL,"http://img3.cache.netease.com/photo/0005/2013-03-07/8PBKS8G400BV0005.jpg");
        params.putString(com.tencent.connect.share.QQShare.SHARE_TO_QQ_APP_NAME,  "兼米");
        tencent.shareToQQ(activity, params,new tencentBaseUiLister(context));
    }
}
