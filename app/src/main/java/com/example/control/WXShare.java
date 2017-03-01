package com.example.control;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.dao.share;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import static com.example.mytools.staticTools.url;

/**
 * Created by wangqiang on 2016/12/31.
 */

public class WXShare implements share {
    //wx
    private static final String WEI_ID = "wx62e1081cd468f5e2";
    private IWXAPI api;
    private Context context;
    private String TAG = "WXShare";
    private WXWebpageObject webpager ;
    private WXMediaMessage message;
    private SendMessageToWX.Req req;

    public WXShare(Context context){
        this.context = context;
        api = WXAPIFactory.createWXAPI(context,WEI_ID,true);
        api.registerApp(WEI_ID);
    }
    @Override
    public void shareToZone() {
        req = new SendMessageToWX.Req();
        req.scene = SendMessageToWX.Req.WXSceneSession ;
        share();
    }

    @Override
    public void shareToPY() {
        req = new SendMessageToWX.Req();
        req.scene = SendMessageToWX.Req.WXSceneTimeline ;
        share();
    }
    private void share(){
        Toast.makeText(context,"正在跳转",Toast.LENGTH_SHORT).show();
        Log.e(TAG,"share to weixin");
        webpager = new WXWebpageObject();
        webpager.webpageUrl = url;
        message = new WXMediaMessage(webpager);
        message.title = "兼米";
        message.description = "我正在兼米上看兼职信息";
        req.transaction = "webPager";
        req.message = message;
        Boolean get = api.sendReq(req);
//        api.handleIntent(getIntent(),this);
    }
}
