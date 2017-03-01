package com.example.my;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.adapter.baomingAdapter;
import com.example.object.Orders;
import com.example.wangqiang.jianmi1.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangqiang on 2016/7/10.\
 * 结算
 */
public class fragmentJS extends Fragment{
    private ListView listview;
    private List<Orders> list = new ArrayList<>();
    private baomingAdapter adapter;
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("onCreateView","fragmentJS  is onCreateView");
        view = inflater.inflate(R.layout.main_my_apply_list,container,false);
        init();
        return view;
    }
    public void init(){
        listview = (ListView)view.findViewById(R.id.my_apply_list);
        adapter = new baomingAdapter(getActivity(),list,
                R.layout.main_my_apply_paylistitem,
                new String[]{"image","title","time","money","location"},
                new int[]{R.id.my_apply_listItem_image,
                        R.id.my_apply_listItem_title,
                        R.id.my_apply_listItem_time,
                        R.id.my_apply_listItem_money,
                        R.id.my_apply_listItem_location,
                        R.id.my_apply_listItem_connect,
                        R.id.my_apply_listItem_cancel
                });
        listview.setAdapter(adapter);

    }
    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        Log.e("HJJ", "fragment 结算 **** onStart...");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.e("HJJ", "fragment 结算JS **** onResume...");
        // TODO Auto-generated method stub
        super.onResume();
    }

    /**
     * 打开另一个activity,退出程序，调用onPause,onStop
     */
    @Override
    public void onPause() {
        Log.e("HJJ", "fragmentJS  结算**** onPause...");
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.e("HJJ", "fragmentJS 结算**** onStop...");
        // TODO Auto-generated method stub
        super.onStop();
    }
    @Override
    public void onDestroyView() {
        Log.e("HJJ", "fragmentJS 结算**** onDestroyView...");
        // TODO Auto-generated method stub
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        Log.e("HJJ", "fragmentJS结算**** onDestroy...");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.e("HJJ", "fragmentJS 结算**** onDetach...");
        // TODO Auto-generated method stub
        super.onDetach();
    }
    //fragment 是否被隐藏，解决viewPager 有白画面的情况
    @Override
    public void onHiddenChanged (boolean hidden){
        Log.e("fragmentJS 结算 is ",""+hidden);
    }

}
