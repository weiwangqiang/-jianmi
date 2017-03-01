package com.example.xiaoxi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.control.UserControl;
import com.example.my.mySuggest;
import com.example.wangqiang.jianmi1.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by wangqiang on 2016/6/19.
 */
public class main_xiaoxi extends Fragment implements View.OnClickListener {
    private RelativeLayout rel;
    private TextView top;
    private View view;
    private GridView gridView;
    private SimpleAdapter adapter;
    private List<Map<String,Object>> listData = new ArrayList<Map<String,Object>>();
    private int[] pic = new int[]{R.drawable.talk,R.drawable.news,R.drawable.interest,
            R.drawable.travel,R.drawable.feedback};
    private String[] title = new String[]{"在线客服","新闻频道","兼职趣事","兼职旅游","反馈建议"};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_xiaoxi,container,false);
        init();
        return view;
    }

    private void init(){
        findid();
        addDate();
        setlister();
    }
    private void findid(){
        //下拉id
        top = (TextView)view.findViewById(R.id.main_xiaoxi_text);
        gridView = (GridView)view.findViewById(R.id.main_xiaoxi_gridView);
        rel = (RelativeLayout)view.findViewById(R.id.main_xiaoxi_RelNews);
    }
    private void addDate(){
        for(int i = 0 ;i< title.length;i++){
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("图片",pic[i]);
            map.put("标题",title[i]);
            listData.add(map);
        }
    }
    private void setlister(){
        adapter = new SimpleAdapter(getActivity(),listData,
                R.layout.main_xiaoxi_grid_item,
                new String[]{"图片","标题"}
                ,new int[]{ R.id.main_xiaoxi_image,
                R.id.main_xiaoxi_text,
          });
        gridView.setAdapter(adapter);
        //设置item 间距为10dp (由px转化为dp)
        final float scale = getActivity().getResources().getDisplayMetrics().density;
        int width =(int)(scale * 10 + 0.5f);
        //设置gridView 的item 间距
        gridView.setHorizontalSpacing(width);
        gridView.setVerticalSpacing(width);
        Log.e("gridView","gridView is "+width);
        gridView.setOnItemClickListener(new myItemlister());
        rel.setOnClickListener(this);
    }
    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_xiaoxi_RelNews:
                Intent intent = new Intent(getActivity(),companyNews.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private class myItemlister implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch(position){
                case 0:
                    toServer();
                    break;
                case 1:
                    toNews();
                    break;
                case 2:
                    toInterest();
                    break;
                case 3:
                    toTravel();
                    break;
                case 4:
                    toSuggest();
                    break;
                default:
                    break;
            }

        }
    }
    private void toServer(){
//        Intent intent = new Intent(getActivity(), LoginHuan.class);
//        startActivity(intent);
    }
    private void  toNews(){

    }
    private void toInterest(){

    }
    private void toTravel(){

    }
    //跳转到反馈建议
    private void toSuggest(){
        Log.e("niaho","tosuggest");
        Intent intent = new Intent(getActivity(), mySuggest.class);
        startActivity(intent);
    }
    /**
     *开始创建调用onStart,onResume
     */
    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        Log.e("HJJ", "main_xiaoxi **** onStart...");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.e("HJJ", "main_xiaoxi **** onResume...");
        // TODO Auto-generated method stub
        super.onResume();

    }

    /**
     * 打开另一个activity,退出程序，调用onPause,onStop
     */
    @Override
    public void onPause() {
        Log.e("HJJ", "main_xiaoxi **** onPause...");
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.e("HJJ", "main_xiaoxi **** onStop...");
        // TODO Auto-generated method stub
        super.onStop();

    }
    @Override
    public void onDestroyView() {
        Log.e("HJJ", "main_xiaoxi **** onDestroyView...");
        // TODO Auto-generated method stub
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        Log.e("HJJ", "main_xiaoxi **** onDestroy...");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.e("HJJ", "main_xiaoxi **** onDetach...");
        // TODO Auto-generated method stub
        super.onDetach();
    }
    //fragment 是否被隐藏，解决viewPager 有白画面的情况
    @Override
    public void onHiddenChanged (boolean hidden){
        Log.e("main_xiaoxi is hidded ",""+hidden);
        if( UserControl.getUserControl().getUser().getUsername().length()!=0){
            top.setText("消息");
        }
    }


}
