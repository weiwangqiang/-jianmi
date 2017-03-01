package com.example.xiaoxi;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.adapter.xiaoxi_simpleAdapter;
import com.example.wangqiang.jianmi1.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangqiang on 2016/7/20.
 */
public class companyNews extends Activity implements View.OnClickListener{
    private Button back;
    private ListView list;
    private xiaoxi_simpleAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private String url = "http://apis.baidu.com/songshuxiansheng/news/news";
    private RequestQueue requestQueue;
    private List<Map<String,String>> date = new ArrayList<Map<String,String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_xiaoxi_companynew);
        requestQueue = Volley.newRequestQueue(companyNews.this);
        findid();
        initListView();
        pulltoRefrash();//初始化下拉刷新
        refreshLayout.setRefreshing(true);
        addDate();
    }
    private void findid(){
        back = (Button)findViewById(R.id.companyNews_back);
        list = (ListView)findViewById(R.id.company_list);
        refreshLayout = (SwipeRefreshLayout)findViewById(R.id.message_swipeRefreshLayout);
        back.setOnClickListener(this);
    }
  private void initListView(){
      adapter = new xiaoxi_simpleAdapter(companyNews.this,date,
              R.layout.main_xiaoxi_list_item,
              new String[]{"image","title","content"},
              new int[]{R.id.xiaoxi_list_image,R.id.xiaoxi_list_TextTitle,
                      R.id.xiaoxi_list_text});
      list.setAdapter(adapter);
  }
    private void pulltoRefrash(){
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
//              getMessage();
                addDate();
            }
        });
    }
    private void getMessage(){
        //RequestQueue是一个请求队列对象，它可以缓存所有的HTTP请求，
        JsonObjectRequest stringRequest = new JsonObjectRequest(url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("TAG", response.toString());
//                        addDate(response);//重新添加网络数据
                    }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }}){
            //重写请求头
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("dataType", "json");
//                headers.put("ContentType", "application/json; charset=UTF-8");
                headers.put("apikey","8950b4a1b6e7614474c337b2b2a7d894");
                return headers;
            }
        };
        requestQueue.add(stringRequest);
    }
    private void addDate(){
      String[]  image = new String[]{"http://192.168.1.103:9099/jianzhi/images/5.jpg"
              ,"http://image.zzd.sm.cn/3253914432259577073.jpg"
              ,"http://image.zzd.sm.cn/15109050605057229870.jpg"
              ,"http://image.zzd.sm.cn/13166736723100992252.jpg"
              ,"http://image.zzd.sm.cn/3659984098164869766.jpg"
              ,"http://image.zzd.sm.cn/15171054734706063164.jpg"
              ,"http://image.zzd.sm.cn/16037863086259064135.jpg"
              ,"http://image.zzd.sm.cn/13606122218248922375.jpg"};
        String[] title = new String[]{"放倒的衣柜你家是不是也应该做一个?",
                "放倒的衣柜你家是不是也应该做一个?"
                ,"刚刚发生, 太吓人了! 赶紧通知身边的朋友!"
                ,"你以为菠萝只是水果吗? 那你就错大了······"
                ,"伦敦最潮: 长八字胡的小马"
                ,"这可比“把梳子卖给和尚”牛逼多了!"
                ,"这才是中国说话礼仪, 一开口就看出教养!"
                ,"铁路最大规模调图开始 市场化进程进入快车道"};
        for(int i = 0;i<image.length;i++){
            Map<String,String> map = new HashMap<String,String>();
            map.put("image",image[i]);
            map.put("title",title[i]);
            map.put("content","韦王强出品");
            date.add(map);
        }
        Log.e("data"," companyNews data have been changed!");
        adapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(false);
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.companyNews_back:
                finish();
                break;
            default:
                break;
        }
    }
}
