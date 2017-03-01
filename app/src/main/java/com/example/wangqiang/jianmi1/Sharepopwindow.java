package com.example.wangqiang.jianmi1;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;

import com.tencent.tauth.Tencent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangqiang on 2016/7/18.
 */
public class Sharepopwindow extends PopupWindow {
    private Context context;
    public  Button cancel;
    private GridView shareGride;
    private SimpleAdapter gridAdapter ;
    private List<Map<String,String>> Gdate = new ArrayList<Map<String,String>>();
    private int[] shareIcn = new int[]{R.drawable.share_qkong,
            R.drawable.share_qq,R.drawable.share_wx,R.drawable.share_pyq};
    private String[] shareText = new String[]{"QQ空间","QQ","微信","朋友圈"};
    private View view;
    private Tencent tencet;
    private String APP_ID = "1105550774";
    private Itemlister itemlister;
    interface Itemlister{
        //分享到QQ空间
        void shareToQzone();
        //分享到QQ好友
        void shareToQQ();
        //分享到微信
        void shareTowei();
        //分享到微信朋友圈
        void shareTopyq();
    }
    public void setItemlister(Itemlister itemlister){
        this.itemlister = itemlister;
    }
    public Sharepopwindow(Context context){
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view  = inflater.inflate(R.layout.share, null);
        findid();
        initGridView();
        initPop();
    }
    private void findid(){
        cancel = (Button)view.findViewById(R.id.jianzhi_share_cancel);
        shareGride = (GridView)view.findViewById(R.id.share_popGrid);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
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
              Gdate,R.layout.share_item,
              new String[]{"icn","text"},
              new int[]{R.id.share_item_image,R.id.share_item_text});
      shareGride.setAdapter(gridAdapter);
      shareGride.setOnItemClickListener(new myItemlister());
  }
    private class myItemlister implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position){
                case 0:
                    //qq空间
                    itemlister.shareToQzone();
                    break;
                case 1:
                    //QQ
                    itemlister.shareToQQ();
                    break;
                case 2:
                    //微信
                    itemlister.shareTowei();
                    break;
                case 3:
                    //朋友圈
                    itemlister.shareTopyq();
                    break;
                default:
                    break;
            }
        }
    }
    private void initPop(){
        //设置PopupWindow的View
        this.setContentView(view);
        //设置PopupWindow弹出窗体的宽
        this.setWidth(ActionBar.LayoutParams.MATCH_PARENT);
        //设置PopupWindow弹出窗体的高
        this.setHeight(ActionBar.LayoutParams.WRAP_CONTENT);
        //设置PopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.popWindow_animation);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
//        设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
                this.setTouchable(true);
//        ColorDrawable cd = new ColorDrawable(66000000);
//        this.setBackgroundDrawable(cd);//设置背景代码，和设置可以聚焦，点击外面和Back键才会消失。
        this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        this.setOutsideTouchable(true); // 设置popupwindow外部可点击
    }
}
