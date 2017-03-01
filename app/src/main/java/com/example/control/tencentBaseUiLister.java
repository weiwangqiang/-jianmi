package com.example.control;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

/**QQ分享的回调监听
 * Created by wangqiang on 2016/12/31.
 */

public class tencentBaseUiLister implements IUiListener {
    private Context context;
    public tencentBaseUiLister(Context context){
        this.context = context;
    }
    @Override
    public void onComplete(Object o) {
        Log.e("success","jianzhi share back");
        Toast.makeText(context,"分享成功！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(UiError uiError) {
        Log.e("success","jianzhi share onerror"+uiError);
    }

    @Override
    public void onCancel() {
        Log.e("success","jianzhi share have been canceled ");
        Toast.makeText(context,"取消分享！", Toast.LENGTH_SHORT).show();
    }
}
