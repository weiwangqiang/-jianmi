package com.example.mytools;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import com.example.control.shareItemLister;
import com.example.control.tencentBaseUiLister;
import com.example.wangqiang.jianmi1.R;
import com.tencent.tauth.Tencent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangqiang on 2016/7/19.
 */
public class shareDialog extends AlertDialog{
    private Context context;
    public Button cancel;
    private   int ItemHeight = 250;
    private GridView shareGride;
    private SimpleAdapter gridAdapter ;
    private List<Map<String,String>> Gdate = new ArrayList<>();
    private int[] shareIcn = new int[]{R.drawable.share_qkong,
            R.drawable.share_qq, R.drawable.share_wx, R.drawable.share_pyq};
    private String[] shareText = new String[]{"QQ空间","QQ","微信","朋友圈"};
    private Tencent tencet;
    private String APP_ID = "1105550774";
    private shareItemLister itemlister;
    private tencentBaseUiLister UiLister;
    private Activity activity;

    public static shareDialog newInstance(Activity activity,tencentBaseUiLister UiLister) {
        shareDialog s = new shareDialog(activity,UiLister);
        return s;
    }
    /**
     * Creates an alert dialog that uses the default alert dialog theme.
     * <p>
     * The default alert dialog theme is defined by
     * {@link android.R.attr#alertDialogTheme} within the parent
     * {@code context}'s theme.
     *
     * @param context the parent context
     * @see android.R.styleable#Theme_alertDialogTheme
     */
    protected shareDialog(Activity activity,tencentBaseUiLister UiLister) {
        super(activity);
        itemlister = new shareItemLister();
        this.UiLister = UiLister;
        this.activity = activity;
        this.context = activity.getBaseContext();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); // must be called before set content
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share);
        findid();
        initGridView();
        setCanceledOnTouchOutside(true);
        // 设置宽度为屏宽、靠近屏幕底部。
        Window window = getWindow();
        window.setBackgroundDrawableResource(R.color.transparent);
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        ListAdapter listAdapter = shareGride.getAdapter();
        if (listAdapter != null) {
            View listItem = listAdapter.getView(1, null, shareGride);
            listItem.measure(0, 0);
            ItemHeight = listItem.getMeasuredHeight();
            Log.e("shareDialog","listAdapter is not null and get the height of adapterItem is "+ItemHeight);
        }
        ViewGroup.LayoutParams params = shareGride.getLayoutParams();
        params.height = ItemHeight;
        shareGride.setLayoutParams(params);
//        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(WindowManager.
//                LayoutParams.MATCH_PARENT,ItemHeight );
//        shareGride.setLayoutParams(param);
        window.setAttributes(wlp);
        window.setWindowAnimations(R.style.popWindow_animation);
    }

    private void findid(){
        cancel = (Button)findViewById(R.id.jianzhi_share_cancel);
        shareGride = (GridView)findViewById(R.id.share_popGrid);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }
    public void initGridView(){
        //分享资源界面
        for(int i = 0;i<shareIcn.length;i++){
            Map<String,String> map = new HashMap<String,String>();
            map.put("icn",shareIcn[i]+"");
            map.put("text",shareText[i]+"");
            Gdate.add(map);
        }
        gridAdapter = new SimpleAdapter(context,
                Gdate, R.layout.share_item,
                new String[]{"icn","text"},
                new int[]{R.id.share_item_image, R.id.share_item_text});
        shareGride.setAdapter(gridAdapter);
        shareGride.setOnItemClickListener(new myItemlister());
    }
    private class myItemlister implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position){
                case 0:
                    //qq空间
                    itemlister.shareToQzone(activity,UiLister);
                    cancel();
                    break;
                case 1:
                    //QQ
                    itemlister.shareToQQ(activity,UiLister);
                    cancel();

                    break;
                case 2:
                    //微信
                    itemlister.shareTowei(context);
                    cancel();
                    break;
                case 3:
                    //朋友圈
                    itemlister.shareTopyq(context);
                    cancel();
                    break;
                default:
                    break;
            }
        }
    }
}
