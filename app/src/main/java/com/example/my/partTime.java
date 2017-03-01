package com.example.my;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.adapter.StaggeredRecyclerAdapter;
import com.example.adapter.recyclerAdapter;
import com.example.wangqiang.jianmi1.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangqiang on 2016/7/7.
 */
public class partTime extends Activity {
    private String TAG = "partTime";
    private Button back,post;
    private static List<Map<String,Object>> list ;//兼职类型
    private static List<Object> list2;//兼职时间
    private recyclerAdapter adapter;
    private StaggeredRecyclerAdapter adapter2;
    private RecyclerView recyclerView,recyclerView2;
    private String[] kinds = new String[]{
            "不限","调研","送餐员","促销","礼仪",
            "安保","销售","服务员","零时工","校内",
            "设计","文员","派单","模特", "实习",
            "家教","演出","客服","翻译","其他"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_my_parttime);
        addDate();
        findId();
        setlister();
        initRecyclerView();
        init();
    }
    private void addDate(){
        //兼职类型
        list = new ArrayList<>();
        for(int i = 0;i<kinds.length;i++){
            HashMap<String,Object> map = new HashMap<>();
            map.put("kind",kinds[i]);
            map.put("checked",false);
            list.add(map);
        }
        String[] time = new String[]{"周","一","二","三","四","五","六","日"};
        //兼职时间
        list2 = new ArrayList<>();
        for(int i = 0;i<8;i++){
            list2.add(time[i]);
        }
        for(int i = 0;i<24;i++){
            if(i==0){
                list2.add("上午");
            }
            else if(i==8){
                list2.add("下午");
            }
            else if(i==16){
                list2.add("晚上");
            }
            else if(i%2==0){
                list2.add(false);
            }
            else {
                list2.add(true);
            }
        }

    }
    private void findId(){
            back = (Button)findViewById(R.id.partTime_back);
            post = (Button)findViewById(R.id.partTime_post);
            recyclerView = (RecyclerView)findViewById(R.id.partTime_recyclerView);//兼职类型
            recyclerView2 = (RecyclerView)findViewById(R.id.partTime_recyclerView2);//兼职时间
    }
    private void setlister(){
            back.setOnClickListener(new mylister());
            post.setOnClickListener(new mylister());
    }
    private void initRecyclerView(){
        adapter = new recyclerAdapter(partTime.this,R.layout.main_my_parttime_kinditem,list);
        recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        recyclerView.setAdapter(adapter);

        adapter2  = new StaggeredRecyclerAdapter(partTime.this,
                R.layout.main_my_parttime_timeitem,list2);
        recyclerView2.setLayoutManager(new StaggeredGridLayoutManager
                (8,StaggeredGridLayoutManager.VERTICAL));
         ViewGroup.LayoutParams lp = recyclerView2.getLayoutParams();
        lp.height = 100+180 * 3;
//        recyclerView2.setLayoutParams(lp);
         recyclerView2.setAdapter(adapter2);
    }
    private class mylister implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.partTime_back:
                    finish();
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                    break;
                case R.id.partTime_post:
//                     list = adapter.getList();
                    for(int a = 0 ;a<list.size();a++){
                        Log.e("list",""+list.get(a).get("kind")+" is "+list.get(a).get("checked"));
                    }
//                    list2 = adapter2.getList();
                    for(int i = 8;i<32;i++){
                        Log.e("list22","position is "+i+"  is "+list2.get(i).toString());
                    }
                    break;
                default:
                    break;
            }
        }
    }
    private void init(){

    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
