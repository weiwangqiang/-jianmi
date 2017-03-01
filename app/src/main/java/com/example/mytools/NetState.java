package com.example.mytools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**网络接口
 * Created by wangqiang on 2016/7/14.
 */
public class NetState extends BroadcastReceiver {
    private NetLister myNetLister ;
    public interface NetLister{
        void OutInternet();
        void GetInternet();
    }
    public void setNetLister(NetLister myNetLister){
        this.myNetLister = myNetLister;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if(null==networkInfo)
        {
            myNetLister.OutInternet();
        }
        else {
            myNetLister.GetInternet();
        }
    }

}
