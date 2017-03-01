package com.example.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.SqlHelper.Table_jz;
import com.example.SqlHelper.jz_helpe;
import com.example.adapter.CollectAdapter;
import com.example.main.BaseActivity;
import com.example.mytools.staticTools;
import com.example.object.User;
import com.example.wangqiang.jianmi1.R;
import com.example.wangqiang.jianmi1.jianzhi_mes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 我的收藏
 * Created by wangqiang on 2016/7/7.
 */
public class myCollect extends BaseActivity implements AdapterView.OnItemClickListener{

    private AlertDialog.Builder alertDialogBuilder;
    private    AlertDialog alertDialog;
    private User user;
    private Button back;
    private ListView listView;
    private CollectAdapter adapter ;
    private SwipeRefreshLayout refreshLayout;
    private jz_helpe helpe;
    private String url = staticTools.url+"/getShouChang?uid=";
    private RequestQueue requestQueue;
    private List<Map<String,String>> data = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_my_collect);
        requestQueue = Volley.newRequestQueue(myCollect.this);
        helpe = new jz_helpe(this);
        user = userControl.getUser();
        url = url + user.getUid()+"&&currentIndex="+0;
        findId();
        setlister();
        initView();
        initRefresh();
        initList();
        getMess();
    }
    private void findId(){
            back = (Button)findViewById(R.id.collect_back);
        listView = (ListView)findViewById(R.id.main_my_collectList);
        refreshLayout = (SwipeRefreshLayout)findViewById(R.id.main_my_collect_refresh);
    }
    private void setlister(){
        back.setOnClickListener(new mylister());
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        int Jid  = Integer.parseInt(data.get(position).get(Table_jz.Jid));
//        int bid = (int)data.get(position).get("Bid");
        Intent intent = new Intent(this, jianzhi_mes.class);
        Bundle bundle = new Bundle();
        bundle.putInt("jid",Jid);
//        bundle.putInt("Bid",bid);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }

    private void initView(){

        alertDialogBuilder =new AlertDialog.Builder(myCollect.this);
        View view =  getLayoutInflater().from(this).inflate(R.layout.jianzhi_message_dialog,null);
        alertDialog = alertDialogBuilder
                .setView(view)
                .create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

    }

    private class mylister implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.collect_back:
                    finish();
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                    break;
                default:
                    break;
            }
        }
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
                getMess();
            }
        });
    }
    private void getMess(){
        StringRequest stringRequest = new StringRequest(url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("mainFragment ","mainFragment have response is "+ response);
                    }}, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }});
        requestQueue.add(stringRequest);
    }

    private void initList(){
        addData();
        adapter = new CollectAdapter(myCollect.this,data,
                R.layout.main_my_collect_listitem,
                new String[]{Table_jz.ImageUrl,
                        Table_jz.Title,Table_jz.Salary,
                        Table_jz.Time,Table_jz.Place,
                        },
                new int[]{R.id.my_collect_listItem_image,
                        R.id.my_collect_listItem_title,
                        R.id.my_apply_listItem_money,
                        R.id.my_apply_listItem_time,
                        R.id.my_apply_listItem_location
                      });
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        alertDialog.cancel();
    }
    private void addData(){
        ArrayList<HashMap<String,String>> list = helpe.selectAll(user.getUid()+"");
        data.clear();
        if(list.size()!=0){
            for(int i = 0;i<list.size();i++){
                Map<String,String> map = new HashMap<>();
                map.put(Table_jz.Jid,list.get(i).get(Table_jz.Jid));
                map.put(Table_jz.ImageUrl,list.get(i).get(Table_jz.ImageUrl));
                map.put(Table_jz.Title,list.get(i).get(Table_jz.Title));
                map.put(Table_jz.Salary,list.get(i).get(Table_jz.Salary));
                map.put(Table_jz.Time,list.get(i).get(Table_jz.Time));
                map.put(Table_jz.Place,list.get(i).get(Table_jz.Place));
                data.add(map);
            }
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        if(null!=adapter){
            addData();
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
