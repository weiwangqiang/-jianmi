package com.example.my;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.main.BaseActivity;
import com.example.object.User;
import com.example.wangqiang.jianmi1.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我收到的评价
 * Created by wangqiang on 2016/7/30.
 */
public class evaluate extends BaseActivity{
    private TextView level,number;
    private Button back;
    private User user;
    private ListView listView ;
    private SwipeRefreshLayout refreshLayout;
    private SimpleAdapter adapter;
    private RequestQueue requestQueue;
    private String url = "";
    private List<Map<String,Object>> date = new ArrayList<Map<String, Object>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_my_evaluate);
        requestQueue = Volley.newRequestQueue(evaluate.this);
        user = userControl.getUser();
        findid();
        initRefresh();
        initListView();
        initView();
    }
    private void findid(){
        back = (Button)findViewById(R.id.evaluate_back);
        level = (TextView)findViewById(R.id.evaluate_level);
        number = (TextView)findViewById(R.id.evaluate_number);
        listView = (ListView)findViewById(R.id.evaluateList);
        refreshLayout = (SwipeRefreshLayout)findViewById(R.id.Evaluate_swipeRefreshLayout);
    }
    private void initView(){
        level.setText("当前累计信用评价  为"+user.getGoodas());
        number.setText(date.size()+"  次评价");
        back.setOnClickListener(this);
    }
    private void initRefresh(){
        refreshLayout.setSize(SwipeRefreshLayout.MEASURED_STATE_TOO_SMALL);
        // 设置下拉多少距离之后开始刷新数据
        refreshLayout.setDistanceToTriggerSync(200);
        // 设置进度条背景颜色
        refreshLayout.setOverScrollMode( View.OVER_SCROLL_NEVER );
        refreshLayout.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(this,R.color.white));
        // 设置刷新动画的颜色，可以设置1或者更多.
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(this,R.color.base_color),
                ContextCompat.getColor(this,R.color.base_color));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /**
                 * 注意：AsyncTask是单线程顺序执行的线程任务。
                 * 如果涉及到大量、耗时的线程加载任务，可以考虑自己利用Thread(ExecutorService线程池)+Handler+Message实现。
                 * */
                Log.e("refrash"," refrash is running");
                //把列表上的数据清除
                getMessage();
            }
        });
    }
    private void initListView(){
        addDate();
        adapter = new SimpleAdapter(evaluate.this,date,R.layout.main_my_evaluate_item,
                new String[]{"image","company","evaluate","kind","time"},
                new int[]{R.id.evaluate_image,
                        R.id.evaluate_companyName,
                        R.id.evaluate_myEvaluate,
                        R.id.evaluate_kind,
                        R.id.evaluate_time});
        listView.setAdapter(adapter);
    }
    private void addDate(){
        for(int i = 0;i<10;i++){
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("image",R.drawable.job1);
            map.put("company","巨和责任有限公司");
            map.put("evaluate","评价");
            map.put("kind","兼职类型");
            map.put("time","评价时间");
            date.add(map);
        }
    }
    private void getMessage(){
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("TAG", response);
                        refreshLayout.setRefreshing(false);
                    }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                refreshLayout.setRefreshing(false);
            }});
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.evaluate_back:
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                break;
            default:
                break;
        }
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

    }
}
