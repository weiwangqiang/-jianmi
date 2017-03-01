package com.example.jpush;

import android.app.Application;
import android.util.Log;
import com.example.mytools.staticTools;
import com.tencent.mm.sdk.openapi.IWXAPI;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by wangqiang on 2016/8/3.
 */
public class ExampleApplication extends Application {
    private static final String TAG = "JPush";
    private static final String WEI_ID = "wx62e1081cd468f5e2";
    private IWXAPI api;
    @Override
    public void onCreate() {
        Log.d(TAG, "[ExampleApplication] onCreate");
        super.onCreate();
        JPushInterface.setDebugMode(false); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
        /**
         * 环信初始化
         * */
        //代码中设置环信IM的Appkey
//        EMChat.getInstance().setAppkey("18852852276#jianmi");
//        CustomerHelper.getInstance().init(this);
//    //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
//        EMChat.getInstance().setDebugMode(false);
        staticTools.getStaticTools().setDebug(true);
        }

    /**
     * 测试单元
     * */
    @Override
    public void onTerminate()
    {
        // TODO Auto-generated method stub
        super.onTerminate();
    }
}
