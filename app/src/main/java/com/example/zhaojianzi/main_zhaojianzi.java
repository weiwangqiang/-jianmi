package com.example.zhaojianzi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.adapter.main_simpleAdapter;
import com.example.adapter.zhaojianzhi_dialogAdapter;
import com.example.adapter.zhaojianzhi_dialogAdapter2;
import com.example.main.location;
import com.example.mytools.staticTools;
import com.example.object.Job;
import com.example.wangqiang.jianmi1.R;
import com.example.wangqiang.jianmi1.jianzhi_mes;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * Created by wangqiang on 2016/6/19.
 */
public class main_zhaojianzi extends Fragment
{
    private MyReceiver myreceiver;//广播事件
    private IntentFilter filter;//广播的过滤条件
    private View view,footer;
    private RadioButton button1,button2,button3,location;
    private ListView listView;//加载兼职信息的listView
    private  int width,height;//屏幕的宽和高
    private  int popHeight;
    private ListView DialogListvew;//加载兼职类型的listView
    private Button Dialogbutton;//dialog的确认button
    private RelativeLayout relativeLayout;//包含pop View里面的listView和button
    private RelativeLayout  relativeTop;
    private View RadioGroup;//包裹radioButton 的Linearlayout
    //加载兼职类型的listDate
    private List<Map<String,Object>> KindDate = new ArrayList<>();
    private List<Map<String,Object>> AreaDate = new ArrayList<>();
    private List<Map<String,Object>> OrderDate = new ArrayList<>();
    private List<Map<String,Object>> list = new ArrayList<>();
    private int state;
    //加载兼职信息的list
    private List<Map<String,Object>> listDate = new ArrayList<>();
    private List<Job> jobList = new ArrayList<>();

    private main_simpleAdapter adapter;
    private zhaojianzhi_dialogAdapter2 adapter2;
    private zhaojianzhi_dialogAdapter adapterDL;
    //下拉刷新
    private SwipeRefreshLayout swipeRefreshLayout;
    //是否在刷新，是否加载数据
    private Boolean isRefrashing = false,isAdded = false,removeFV = false;
    private Boolean ispull = false;//判断是否是下拉刷新
    private Boolean isinitPop = false;//判断是否加载完成popwindow
    private int currentIndex = 0 ;
    //网络资源
    private PopupWindow pop;
    private View popview,dialogView;
    private OkHttpClient mOkHttpClient ;

    private String url = staticTools.url+"/getJobPage";//获取兼职列表
//    private String url = staticTools.url+"/getManyJob";
    private String TAG = "main_zhaojianzhi";
    private String[] jobtype = new String[]{"不限","家教","调研","送餐员","促销",
            "礼仪","派单","安保","服务员"
            ,"临时工","设计","文员","模特","客服","翻译","其他"};
    private String[] workplace = new String[]{"全镇江","京口区","润州区","北固区","丹阳区"};
    private String[] pubtime = new String[]{"默认排序","最新发布","离我最近","结算效率","只看日结"};
    private Handler handler = new Handler(){
        public void handleMessage(Message mes){
            switch(mes.what){
                case 0x1:
                    //下拉发送网络请求
                    getHttp();
                    break;
                case 0x2:
                    //异常时改变刷新状态
                    changeState();
                    break;
                case 0x3:
                    listView.removeFooterView(footer);
                    removeFV = true;
                    Toast.makeText(getActivity(),"没有更多了",Toast.LENGTH_SHORT).show();
                    break;
                case 0x12:
                    //加载数据并更新请求状态
                    addDate();
                    changeState();
                    break;
                default:
                    break;
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_zhaojianzi,container,false);
        popview = inflater.inflate(R.layout.main_zhaojianzhi_pop,container,false);
        footer = inflater.from(getActivity()).inflate(R.layout.listview_footer,null);
        init();
        return view;
    }
    private void init(){
        //获取屏幕的宽
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        width = metric.widthPixels;
        height = metric.heightPixels;
        //获取网络请求队列
        mOkHttpClient = new OkHttpClient();
        //绑定广播
        myreceiver = new MyReceiver();
        filter = new IntentFilter("City_changed");
        getActivity().registerReceiver(myreceiver,filter);
        findid();
        initDiaDate();
        initListView();
        setListener();
        //下拉刷新
        pulltoRefrash();
    }
    private void findid(){
        button1 = (RadioButton)view.findViewById(R.id.zhaojianzi_RadioButton1);
        button2 = (RadioButton)view.findViewById(R.id.zhaojianzi_RadioButton2);
        button3 = (RadioButton)view.findViewById(R.id.zhaojianzi_RadioButton3);
        RadioGroup = view.findViewById(R.id.LinearLayout_RadioButton);
        location = (RadioButton)view.findViewById(R.id.main_zhaojianzhi_location);//位置
        listView = (ListView)view.findViewById(R.id.zhaojianzi_list);//兼职详情的List
        relativeTop = (RelativeLayout)view.findViewById(R.id.main_zhaojianzhi_reTop1_);
        //dialog 的id
        dialogView = popview.findViewById(R.id.jianzhi_dialog_view);
        DialogListvew = (ListView)popview.findViewById(R.id.zhaojianzhi_dialog_list);
        Dialogbutton = (Button)popview.findViewById(R.id.zhaojianzhi_dialog_sure);
        relativeLayout = (RelativeLayout)popview.findViewById(R.id.zhaojianzhi_dialog_Relative);
        //下拉id
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.zjz_swipeRefreshLayout);
    }

    /**
     * 初始化兼职类型的listDate数据
     */
    public void initDiaDate(){
        //显示当前城市
        location.setText(staticTools.getCity());
        for(int i = 0;i<jobtype.length;i++){
            Map<String,Object> map = new HashMap<>();
            map.put("key",jobtype[i]);
            map.put("checked",false);
            KindDate.add(map);
        }
        KindDate.get(0).put("checked",true);
        for(int i = 0;i<workplace.length;i++){
            Map<String,Object> map = new HashMap<>();
            map.put("key",workplace[i]);
            map.put("checked",false);
            AreaDate.add(map);
        }
        AreaDate.get(0).put("checked",true);
        for(int i = 0;i<pubtime.length;i++){
            Map<String,Object> map = new HashMap<>();
            map.put("key",pubtime[i]);
            map.put("checked",false);
            OrderDate.add(map);
        }
        OrderDate.get(0).put("checked",true);
    }
    private void setListener(){
        button1.setOnClickListener(new lister());
        button2.setOnClickListener(new lister());
        button3.setOnClickListener(new lister());

        dialogView.setOnClickListener(new lister());
        Dialogbutton.setOnClickListener(new lister());
        location.setOnClickListener(new lister());
        //监听兼职列表Listview 的点击事件
        listView.setOnItemClickListener(new MyItemlister());
        //监听listview 是否滑到底部
        listView.setOnScrollListener(new MyScrollLister());
    }
    private class MyItemlister implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(position<listDate.size()){
                int Jid  = (int)listDate.get(position).get("ID");
                int bid = (int)listDate.get(position).get("Bid");
                Intent intent = new Intent(getActivity(), jianzhi_mes.class);
                Bundle bundle = new Bundle();
                bundle.putInt("jid",Jid);
                bundle.putInt("Bid",bid);
                intent.putExtras(bundle);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        }
    }
    private class MyScrollLister implements  AbsListView.OnScrollListener{
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            switch (scrollState) {
                // 当不滚动时
                case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                    //当正在滚动时
                case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                    // 判断滚动到底部
                    downRefrash(view);
                    break;
            }
        }
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    }
    // 上拉，判断滚动是否到底部,到底就加载数据
    public void downRefrash(AbsListView view){
        if ( view.getLastVisiblePosition() == (view.getCount() - 1)&&!isRefrashing) {
            currentIndex += 10;
            getHttp();
        }
    }
    /**
     * 初始化List ,绑定adapter 与 listDate(兼职列表信息)
     */
    private void initListView(){
        listView.addFooterView(footer);
        adapter = new main_simpleAdapter(getActivity(),listDate,
                R.layout.main_home_listitem,
                new String[]{"图片","标题","工资","发布时间","结算","地点"}
                ,new int[]{ R.id.main_list_image,
                R.id.main_list_TextTitle,
                R.id.main_list_TextMoney,
                R.id.main_list_TextTime,
                R.id.main_list_TextOver,
                R.id.main_list_TextLaction}
        );
        listView.setAdapter(adapter);
        listView.removeFooterView(footer);
    }
    /**
     * 下拉刷新
     */
    private void pulltoRefrash(){
        swipeRefreshLayout.setSize(SwipeRefreshLayout.MEASURED_STATE_TOO_SMALL);
        // 设置下拉多少距离之后开始刷新数据
        swipeRefreshLayout.setDistanceToTriggerSync(200);
        // 设置进度条背景颜色
        swipeRefreshLayout.setOverScrollMode( View.OVER_SCROLL_NEVER );
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(getActivity(),R.color.white));
        // 设置刷新动画的颜色，可以设置1或者更多.
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(),R.color.base_color),
                ContextCompat.getColor(getActivity(),R.color.colorAccent),
                ContextCompat.getColor(getActivity(),R.color.ml_text_red),
                ContextCompat.getColor(getActivity(),R.color.ml_primary),
                ContextCompat.getColor(getActivity(),R.color.ml_primary_dark));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /**
                 * 注意：AsyncTask是单线程顺序执行的线程任务。
                 * 如果涉及到大量、耗时的线程加载任务，可以考虑自己利用Thread(ExecutorService线程池)+Handler+Message实现。
                 * */
                currentIndex  = 0 ;
                ispull  = true;//是下拉刷新
                getHttp();
            }
        });
    }

    /**
     * 获取json数据
     */
    private void getHttp(){
        if(isRefrashing)
        {
            return;
        }
        isRefrashing = true;
        try{
            RequestBody requestBody = RequestBody.create(MediaType.
                    parse("application/json; charset=utf-8"),
                    getparam().toString());
            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            mOkHttpClient.newCall(request).enqueue(new Callback(){
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(0x2);
                }
                @Override
                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                    Log.e(TAG,response.toString());
                    if(response.code()==200){
//                        //如果是下拉刷新
                        StringBuilder builder = new StringBuilder();
                        builder.append(response.body().string());
                        staticTools.getStaticTools().myLog(TAG,"response is "+builder.toString());
                        praseJson(builder.toString());
                        //更新请求状态
                        handler.sendEmptyMessage(0x12);
                    }
                    else{
                        handler.sendEmptyMessage(0x2);
                    }
                }
            });
        }catch(Exception e){
            e.printStackTrace();
            handler.sendEmptyMessage(0x2);
        }
    }

    /**
     * 获取请求的三个参数值
     * @return
     */

    private JSONObject getparam(){
        HashMap<String,String> map = new HashMap();
        //加10
        Log.e(TAG,"currentIndex is "+ currentIndex);
        map.put("currentIndex",currentIndex+"");
        map.put("jianGe",10+"");
//        map.put("jobtype",null);
//        map.put("workplace",null);
//        map.put("pubtime",null);

//        for(int i = 0;i<KindDate.size();i++){
//            if((Boolean)KindDate.get(i).get("checked")){
//                if(i<jobtype.length){
//                    map.put("jobtype",i+"");
//                    break;
//                }
//            }
//        }
//        for(int i = 0;i<AreaDate.size();i++){
//            if((Boolean)AreaDate.get(i).get("checked")){
//                if(i<workplace.length){
//                    map.put("workplace",workplace[i]);
//                    break;
//                }
//
//            }
//        }
//        for(int i = 0;i<OrderDate.size();i++){
//           if((Boolean)OrderDate.get(i).get("checked")) {
//               if(i<pubtime.length){
//                   map.put("pubtime",pubtime[i]);
//                   break;
//               }
//           }
//        }
        JSONObject object = new JSONObject(map);
        return object;
    }

    //改变刷新状态
    private void changeState(){
        if(ispull){
            if(removeFV){
                listView.addFooterView(footer);
                removeFV = false;
            }
//            listView.addFooterView(footer);
            swipeRefreshLayout.setRefreshing(false);
            ispull = false;//改变下拉刷新状态
        }
        //如果第一次加载数据就判断是否添加footer
        if(!isAdded&&listDate.size()>=7){
            listView.addFooterView(footer);
        }
        isRefrashing = false;
        isAdded = true;
    }

    /**
     * 解析json数组
     * @param string
     */
    private void praseJson(String string) {
        try{
            jobList.clear();//清除joblist 的数据
            JSONArray array = new JSONArray(string);
            for(int i = 0 ;i<array.length();i++){
                Job job = new Job();//兼职对象
                JSONObject object = array.getJSONObject(i);
                job.setTitle(object.getString("title"));
                job.setJobtype(object.getString("jobtype"));
                job.setArea(object.getString("area"));
                job.setJurl(staticTools.url + object.getString("jurl"));
                Long d = object.getLong("pubtime");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String  time = sdf.format(d);
                job.setPubtime(time);

                job.setSalary(object.getString("salary"));
                job.setWorktime(object.getString("worktime"));
                job.setJid(object.getInt("jid"));
                job.setBusinessid(object.getInt("businessid"));
                Log.e(TAG," image uri is "+job.getJurl());
                jobList.add(job);
             }
        }catch(Exception e){
            e.printStackTrace();
        }
        if(0==jobList.size()){
            handler.sendEmptyMessage(0x3);
        }
    }
    /**
     * 添加list的数据
     */
    private void addDate(){
        if(jobList.size()==0)
        {
            Toast.makeText(getActivity(),"没有更多了",Toast.LENGTH_SHORT).show();
            return;
        }
        if(ispull){
            listDate.clear();
//            if(listView.getFooterViewsCount()!=0){
//                listView.removeView(footer);
//            }
        }
        for(int i = 0;i<jobList.size();i++){
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("图片",jobList.get(i).getJobtype());
            map.put("标题",jobList.get(i).getTitle());
            map.put("工资",jobList.get(i).getSalary()+"/小时");
            map.put("发布时间",jobList.get(i).getPubtime());
            map.put("结算","日结");
            map.put("地点",jobList.get(i).getArea());
            map.put("ID",jobList.get(i).getJid());
            map.put("Bid",jobList.get(i).getBusinessid());
            map.put("Jurl",jobList.get(i).getJurl());
            listDate.add(map);
        }
         adapter.notifyDataSetChanged();
    }


    /**
     * 兼职对话框的点击联动作用
     */
    private class myDItemlister implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(int i = 0;i<list.size();i++){
                    list.get(i).put("checked",false);
                }
            list.get(position).put("checked",true);
            adapterDL.notifyDataSetChanged();
        }
    }
    private class myOItemlister implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(int i = 0;i<list.size();i++){
                    list.get(i).put("checked",false);
                }
            list.get(position).put("checked",true);
            adapter2.notifyDataSetChanged();
        }
    }
    //点击事件
    private class lister implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.zhaojianzi_RadioButton1:
                    button1.setChecked(true);
                    changeView(1);
                    break;
                case R.id.zhaojianzi_RadioButton2:
                    button2.setChecked(true);
                    changeView(2);
                    break;
                case R.id.zhaojianzi_RadioButton3:
                    button3.setChecked(true);
                    changeView(3);
                    break;
                case R.id.jianzhi_dialog_view:
                    changeDate(state);
                    pop.dismiss();
                    break;
                case R.id.zhaojianzhi_dialog_sure:
                    ispull = true;
                    swipeRefreshLayout.setRefreshing(true);
                    getHttp();
                    pop.dismiss();
                    break;
                case R.id.main_zhaojianzhi_location:
                    //跳转到定位界面
                    Intent intent1 = new Intent(getActivity(),location.class);
                    startActivityForResult(intent1, 1);//表示可以返回结果
                    getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    break;
                default:
                    break;
            }
        }
    }
    /**
     * 重写startActivityForResult返回调用的方法
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, intent);
//resultCode是返回的code
        if(resultCode==2){
//            requestCode是开启intent的请求code
            if(requestCode==1){
                String Loc = intent.getStringExtra("city");//接收返回数据
                if(null!=location){
                    location.setText(Loc);
                }
            }
        }
    }

    /**
     * 显示兼职类型的view
     * @param i
     */
    private void changeView(int i){
        state = i;
        if(i<4){
            //设置adapter
            Log.e(TAG," ---->changeView ");
            isinitPop = false;
            addDLListData(i);//添加数据
            setHeight();//获取整个view 的高度
            showPop();
        }
    }

    /**
     * height 是屏幕高度
     * relativeTop 是布局最顶部的view
     *
     */
    public void showPop(){
        int PopHeight = height-relativeTop.getHeight()-RadioGroup.getHeight();
        Log.e(TAG,"height is "+height+"relativeTop "+relativeTop.getHeight()
                +"--->> showPop  RadioGroup is "+RadioGroup.getHeight());

        View v  = LayoutInflater.from(getActivity()).inflate(R.layout.main_zhaojianzhi_pop,null);
        pop = new PopupWindow(popview,ViewGroup.LayoutParams.MATCH_PARENT
                ,PopHeight,true);
//            pop.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        Log.e(TAG,"--->>  height is "+height +" pop height is "+pop.getHeight());
        ColorDrawable cd = new ColorDrawable(66000000);
        pop.setBackgroundDrawable(cd);//设置背景代码，和设置可以聚焦，点击外面和Back键才会消失。
//        pop.setContentView(v);
        pop.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        //自定义动画
//        pop.setAnimationStyle(R.style.jianpopWindow_animation);
        pop.setTouchable(true); // 设置popupwindow可点击
        pop.setOutsideTouchable(true); // 设置popupwindow外部可点击
        pop.setFocusable(true); // 获取焦点
             /* 设置popupwindow的位置 */
        pop.showAsDropDown(RadioGroup);//在button1下方显示
        pop.update();
        pop.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                /**** 如果点击了popupwindow的外部，popupwindow也会消失 ****/
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    changeDate(state);//修改list 的数据
                    pop.dismiss();
                    return true;
                }
                return false;
            }
        });

        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                notshowButton();//设置Radiobutton 为不点击状态
            }
        });

    }
    /**
     * 添加对话框dialoglist 的数据
     */
    private void addDLListData(int a){
        Log.e(TAG," addDLListDate is  "+a);
        switch (a){
            case 1:
                list = KindDate;
                break;
            case 2:
                list = AreaDate;
                break;
            case 3:
                list = OrderDate;
                break;
            default:
                break;
        }
        Log.e(TAG," ---->date size is  "+list.size());
        if(a<=2){
            adapterDL = new zhaojianzhi_dialogAdapter(getActivity(),list,
                    R.layout.main_zhaojianzhi_dialog_list_item
                    ,new String[]{"key","checked"},
                    new int[]{R.id.zhaojianzhi_dialog_item_text,
                            R.id.zhaojianzhi_dialog_item_checkBox,
                            R.id.zhaojianzhi_dialog_item_Image});
            DialogListvew.setAdapter(adapterDL);
            DialogListvew.setOnItemClickListener(new myDItemlister());

        }
        else {
            adapter2 = new zhaojianzhi_dialogAdapter2(getActivity(),list,
                    R.layout.main_zhaojianzhi_dialog_list_item
                    ,new String[]{"key","checked"},
                    new int[]{R.id.zhaojianzhi_dialog_item_text,
                            R.id.zhaojianzhi_dialog_item_checkBox,
                            R.id.zhaojianzhi_dialog_item_Image});
            DialogListvew.setAdapter(adapter2);
            DialogListvew.setOnItemClickListener(new myOItemlister());
        }
    }
    private void setHeight(){
        //获取list的高度
        int tot = list.size();
        if(tot>7){
           tot= 7;
        }
        ListAdapter listAdapter = DialogListvew.getAdapter();
        if (listAdapter == null) {
            return;
        }
        View listItem = listAdapter.getView(1, null, DialogListvew);
        listItem.measure(0, 0);
        int ItemHeight = listItem.getMeasuredHeight();
         popHeight = tot* ItemHeight + Dialogbutton.getHeight()+ 10;
        Log.e(TAG,"---->> setHeight RelativeLayout height is "+popHeight);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(width,popHeight);
        relativeLayout.setLayoutParams(param);

//        ViewTreeObserver observer = popview.getViewTreeObserver();
//        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            @Override
//            public boolean onPreDraw() {
//                Log.e(TAG,"relativeLayout complete init  height ");
//                if(!isinitPop){
//                    showPop();
//                    Log.e(TAG,"relativeLayout complete init  height "+relativeLayout.getHeight());
//                    isinitPop = true;
//                }
//                return false;
//            }
//        });
    }
    private void changeDate(int t){
        DialogListvew.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
    //监听事件
    private  class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            location.setText(staticTools.getCity());
        }

    }
    private void notshowButton(){
        button1.setChecked(false);
        button2.setChecked(false);
        button3.setChecked(false);
    }

    /**
     * 检查网络是否可用
     * @return
     */
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        //解除广播绑定
        getActivity().unregisterReceiver(myreceiver);
    }
    /**
     *开始创建调用onStart,onResume
     */
    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    /**
     * 打开另一个activity,退出程序，调用onPause,onStop
     */
    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();

    }
    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
    }
    @Override
    public void onDetach() {
        // TODO Auto-generated method stub
        super.onDetach();
    }
    //fragment 是否被隐藏，解决viewPager 有白画面的情况
    @Override
    public void onHiddenChanged (boolean hidden){
//        if(!isNetworkConnected()) {
////                Toast.makeText(getActivity(),"网络不可用",Toast.LENGTH_SHORT).show();
////                return;
//            }
        if (isNetworkConnected()&&!isAdded&&!hidden){
            staticTools.getStaticTools().myLog(TAG,"getHTTp in hidden ");
                    ispull = true;
                    swipeRefreshLayout.setRefreshing(true);
                    getHttp();
                }
    }
}
