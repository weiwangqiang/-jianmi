package com.example.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.adapter.main_simpleAdapter;
import com.example.adapter.viewpager_adapter;
import com.example.mytools.parse;
import com.example.mytools.staticTools;
import com.example.object.Job;
import com.example.wangqiang.jianmi1.R;
import com.example.wangqiang.jianmi1.jianzhi_mes;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 *  Created by wangqiang on 2016/5/16.
 */
public class main_fragment extends Fragment {

    //自己定义的工具类
    private staticTools tools;
    private String  TAG = "main_fragment";
    private MyReceiver myreceiver;//广播事件
    private IntentFilter filter;//广播的过滤条件
    //控制显示对应的Radiobutton
    Timer mTimer;
    TimerTask mTask;
    private int current = 1,currentpoistion = 1;
    private int currentIndex = 0,headHeight = 0;//记录headView的高度
    private View view,pagerImage0,pagerImage1,pagerImage2,pagerImage3,pagerImage4,Header,footer;
    private RelativeLayout moreRelative;
    private ImageView search;
    private ViewPager viewpager;
    private GridView gridview;
    private ListView listView;
    //显示text的TextView
    private TextView title,more;
    //listview的adapter
    private SimpleAdapter gridViewAdapter;


    private main_simpleAdapter listadapter;
    private ArrayList<Map<String,Object>> listDate = new ArrayList<>();
    private RadioButton location,radio1,radio2,radio3;
    //存放viewpager View的list
    private ArrayList<View> pagerList;
    private ImageView image0,image1,image2,image3,image4;
    //下拉刷新
    private SwipeRefreshLayout swipeRefreshLayout;
    //isTaskRun判断是否在执行定时任务 isRefreshing 判断是否在刷新 added 判断是否添加数据
    private Boolean isRefrashing = false,isTaskRun = false,added = false;
    //是否是下拉刷新
    private Boolean ispull = false;
    //是否获取header height
    private Boolean getHeadHeight = false;
    //网络资源
    private OkHttpClient mOkHttpClient ;
    private List<Job> joblist = new ArrayList<>();
    private StringBuffer stringBuffer = new StringBuffer();
    private String url =  staticTools.url+"/getJobPage";
    //Gridview的图片资源
    private int[] image = new int[]{
            R.drawable.all,R.drawable.news,
            R.drawable.techer,R.drawable.talk
    };
    private int[] viewImage = new int[]{R.drawable.jianzhi,R.drawable.jianzhi1,R.drawable.jianzhi2};
    //gridVeiw的text资源
    private String[] text = new String[] {
            "全部","最新","家教","客服"
    };
    private Handler handler = new Handler(){
            public void handleMessage(Message mes){
               switch(mes.what){
                   case 0x1:
                       //上拉加载网络数据
                       addDate();//重新添加网络数据
                       //改变刷新状态
                       changeToRefrashable();
                       break;
                   case 0x2:
                       //加载网络数据异常，改变刷新状态
                       changeToRefrashable();
                       break;
//                   开启定时任务
                   case 0x3:
                       showViewPager();
                       break;
                   default:
                       break;
               }
            }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       //list的头
        Header = inflater.from(getActivity()).inflate(R.layout.home_list_header,null);
        footer = inflater.from(getActivity()).inflate(R.layout.listview_footer,null);
        view = inflater.inflate(R.layout.main_home,container,false);
        init(inflater);
        return view;
    }

    /**
     * 加载主界面的画面
     * @param inflater
     */
    private void init(LayoutInflater inflater){
//        requestQueue = Volley.newRequestQueue(getActivity());//实例化网络请求
        mOkHttpClient = new OkHttpClient();
        initDaohang(inflater);
        findId();
        setListener();
        initViewPager();
        initGridView();
        initListView();
        getHttp();
        //实例化工具类
        tools = new staticTools();
        location.setText(tools.getCity());//获取当前城市
        //绑定广播
        myreceiver = new MyReceiver();
        filter = new IntentFilter("City_changed");
        getActivity().registerReceiver(myreceiver,filter);
        pulltoRefrash();

    }

    /**
     *  加载导航图片
     * @param inflater
     */
    public void initDaohang(LayoutInflater inflater){
        pagerImage0 = inflater.inflate(R.layout.mainpager_item3,null);
        pagerImage1 = inflater.inflate(R.layout.mainpager_item,null);
        pagerImage2 = inflater.inflate(R.layout.mainpager_item2,null);
        pagerImage3 = inflater.inflate(R.layout.mainpager_item3,null);
        pagerImage4 = inflater.inflate(R.layout.mainpager_item,null);
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
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(
                ContextCompat.getColor(getActivity(),R.color.white));
        // 设置刷新动画的颜色，可以设置1或者更多.
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(),R.color.base_color),
                ContextCompat.getColor(getActivity(),R.color.base_color));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
                public void onRefresh() {
                //把列表上的数据清除
                ispull = true;
                listDate.clear();
                listadapter.notifyDataSetChanged();
                staticTools.getStaticTools().myLog(TAG,"start getHttp");
                getHttp();
    }
});
    }
    /**
     *  获取网络资源
     */
    private void getHttp(){
        //如果正在刷新就返回
        staticTools.getStaticTools().myLog(TAG," in getHttpMethod isRefrashing is  "+ isRefrashing);
        if(isRefrashing) {
            staticTools.getStaticTools().myLog(TAG,"isRefreshing back");
            swipeRefreshLayout.setRefreshing(false);
            return;
        }
        isRefrashing = true;
        staticTools.getStaticTools().myLog(TAG,"url is "+url);
        //RequestQueue是一个请求队列对象，它可以缓存所有的HTTP请求，
        try{
            RequestBody requestBody = RequestBody.create(MediaType.
                    parse("application/json; charset=utf-8"), getparam());
            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            mOkHttpClient.newCall(request).enqueue(new Callback(){
                @Override
                public void onFailure(Call call, IOException e) {
                    staticTools.getStaticTools().myLog(TAG,"onFailure  ");
                    e.printStackTrace();
                    handler.sendEmptyMessage(0x2);
                }
                @Override
                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                    Log.e(TAG,"response is "+response.toString());
                    if(response.code()==200){
//                       //如果是下拉刷新
                        stringBuffer.append(response.body().string());
                        praseJson(stringBuffer.toString());
                        handler.sendEmptyMessage(0x1);
                        //更新请求状态
                    }
                    else{
                        handler.sendEmptyMessage(0x2);
                    }
                }
            });
        }catch(Exception e){
            staticTools.getStaticTools().myLog(TAG,"Exception  ");
            e.printStackTrace();
            handler.sendEmptyMessage(0x2);
        }
    }
    private String getparam(){
        Map<String,String> map = new HashMap();
        Log.e(TAG,"currentIndex is "+ currentIndex);
        map.put("currentIndex",currentIndex+"");
        map.put("jianGe",10+"");
//        map.put("jobtype",null);
//        map.put("workplace",null);
//        map.put("pubtime",null);
        JSONObject object =  new JSONObject(map);
        return object.toString();
    }
    /**
     * 改变可以刷新状态
     */
  public void changeToRefrashable(){
      staticTools.getStaticTools().myLog(TAG,"changeToRefreshable ispull is  "+ ispull);
      isRefrashing = false;
     if(ispull){
          swipeRefreshLayout.setRefreshing(false);
         ispull = false;
     }
  }
    /**
     * 解析json数据
     * @param string
     */
    private void praseJson(String string) {
        try{
            joblist.clear();//清除joblist 的数据
            JSONArray array = new JSONArray(string);
                for(int i = 0 ;i<array.length();i++){
                    JSONObject object = array.getJSONObject(i);
                    parse p = new parse();
                    joblist.add(p.parseJob(object));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 添加list的数据
     */
    private void addDate(){
        if(ispull){
            listDate.clear();
            listadapter.notifyDataSetChanged();
        }
        for(int i = 0;i<joblist.size();i++){
            Map<String,Object> map = new HashMap<>();
            map.put("图片",joblist.get(i).getJobtype());
            map.put("标题",joblist.get(i).getTitle());
            map.put("工资",joblist.get(i).getSalary()+"/小时");
            map.put("发布时间",joblist.get(i).getPubtime());
            map.put("结算","日结");
            map.put("地点",joblist.get(i).getArea());
            map.put("ID",joblist.get(i).getJid());
            map.put("Bid",joblist.get(i).getBusinessid());
            map.put("Jurl",joblist.get(i).getJurl());
            listDate.add(map);
        }
        added  = true;
        listadapter.notifyDataSetChanged();
    }

    public void findId(){
        viewpager = (ViewPager)(Header.findViewById(R.id.main_viewPager));
        gridview = (GridView)(Header.findViewById(R.id.main_GridView));
        //Viewpager 的导航点
        radio1 =  (RadioButton)(Header.findViewById(R.id.main_Radio1));
        radio2 =  (RadioButton)(Header.findViewById(R.id.main_Radio2));
        radio3 =  (RadioButton)(Header.findViewById(R.id.main_Radio3));
        //显示广告的text
        title  = (TextView)Header.findViewById(R.id.main_viewpager_text);
        title.setText("注册兼米号，一大波好礼等你来拿");
        //显示推荐更多
        more  =(TextView)Header.findViewById(R.id.home_Header_more);

        //下拉
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshLayout);
        //显示主界面]定位信息的radiobutton
        location =  (RadioButton)(view.findViewById(R.id.main_location));
        search   =    (ImageView)(view.findViewById(R.id.main_search));
      //viewpager 的图片
        image0  = (ImageView)pagerImage0.findViewById(R.id.viewpager_item3);
        image1  = (ImageView)pagerImage1.findViewById(R.id.viewpager_item);
        image2  = (ImageView)pagerImage2.findViewById(R.id.viewpager_item2);
        image3  = (ImageView)pagerImage3.findViewById(R.id.viewpager_item3);
        image4  = (ImageView)pagerImage4.findViewById(R.id.viewpager_item);
        //listview兼职列表
        listView = (ListView) view.findViewById(R.id.home_listView);
        moreRelative = (RelativeLayout)view.findViewById(R.id.main_home_more);
    }
    /**
     * 设置监听
     */
    private void setListener(){
        location.setOnClickListener(new listener());
        search.setOnClickListener(new listener());
        more.setOnClickListener(new listener());
        moreRelative.findViewById(R.id.home_Header_more).setOnClickListener(new listener());
    }
    private class listener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.main_location:
                    Intent intent1 = new Intent(getActivity(), com.example.main.location.class);
                    startActivityForResult(intent1, 1);//表示可以返回结果
                    getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    break;
                case R.id.main_search:
                    Intent intent2 = new Intent(getActivity(), com.example.main.search.class);
                    startActivity(intent2);
                    getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    break;
                case R.id.home_Header_more:
                    Intent intent = new Intent("Main_changed");
                    getActivity().sendBroadcast(intent);
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
     * 加载veiwpage内容
     */
    public void initViewPager(){
        image0.setImageResource(viewImage[2]);
        image1.setImageResource(viewImage[0]);
        image2.setImageResource(viewImage[1]);
        image3.setImageResource(viewImage[2]);
        image4.setImageResource(viewImage[0]);

        pagerList =  new ArrayList<View>();
        pagerList.add(pagerImage0);
        pagerList.add(pagerImage1);
        pagerList.add(pagerImage2);
        pagerList.add(pagerImage3);
        pagerList.add(pagerImage4);
        changeSpee();
        viewpager.setAdapter(new viewpager_adapter(pagerList));
        viewpager.setCurrentItem(currentpoistion);
        viewpager.addOnPageChangeListener(new myPagerChanger());
        viewpager.setPageTransformer(true,new mytransformer());
    }
    private void changeSpee(){
        try {
            Field mfield;
            myScroller scroller;
            mfield = main_fragment.class.getDeclaredField("scroller");
            mfield.setAccessible(true);
            Interpolator sInterpolator = new AccelerateDecelerateInterpolator();
             scroller = new myScroller(viewpager.getContext(),
                    sInterpolator);
            mfield.set(viewpager, scroller);
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
    }

    /**
     * 添加Gridview tem所需资源到list
     */
    public void initGridView() {
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        for(int i = 0;i<text.length;i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", image[i]);
            map.put("text", text[i]);
            list.add(map);
        }
        gridViewAdapter = new SimpleAdapter(getActivity(),list,R.layout.main_gridview_item
        ,new String[]{"image","text"},new int[]{R.id.gridView_item_Image,R.id.gridView_item_Text});
        gridview.setAdapter(gridViewAdapter);
        gridview.setOnItemClickListener(new GrideVItemlister());
    }

    /**
     * 四个导航图标
     */
    public class GrideVItemlister implements AdapterView.OnItemClickListener{
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch(position){
            //全部兼职
            case 0:
                //查看全部兼职
                Intent intent = new Intent("Main_changed");
                getActivity().sendBroadcast(intent);
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                //跳转到客服
//                Intent intentK = new Intent(getActivity(),LoginHuan.class);
//                startActivity(intentK);
//                getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                break;
            default: break;
        }
    }
}

    /**
     * 加载list界面
     */
    private void initListView(){
        listView.addHeaderView(Header);
//        listView.addFooterView(footer);
//        listView.removeFooterView(footer);
        listadapter = new main_simpleAdapter(getActivity(),listDate,
                R.layout.main_home_listitem,
                new String[]{"图片","标题","工资","发布时间","结算","地点"}
                ,new int[]{ R.id.main_list_image,
                            R.id.main_list_TextTitle,
                            R.id.main_list_TextMoney,
                            R.id.main_list_TextTime,
                            R.id.main_list_TextOver,
                            R.id.main_list_TextLaction}
                        );
        listView.setAdapter(listadapter);
        listView.setOnItemClickListener(new itemlister());
        listView.setOnScrollListener(new myScrollList());
        listView.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis(), MotionEvent.ACTION_CANCEL, 0, 0, 0));
    }
    private class myScrollList implements AbsListView.OnScrollListener {
        int oldfirst ;
        //向上滑动
        private   SparseArray recordSp = new SparseArray(0);
        private int mCurrentfirstVisibleItem = 0;
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            Log.d(TAG,"view childCount is "+ view.getChildCount()+" view firstView position  is "+view.getFirstVisiblePosition());
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
              if(firstVisibleItem==0&&getHeader()<headHeight){
                moreRelative.setVisibility(View.GONE);
            }
            else if(firstVisibleItem==0&&getHeader()>=headHeight){
                moreRelative.setVisibility(View.VISIBLE);
            } else if(firstVisibleItem>0){
                moreRelative.setVisibility(View.VISIBLE);
            }
            oldfirst = firstVisibleItem;
    }
    }
    private  float getHeader(){
        View v = listView.getChildAt(0);
        if(v!=null){
            return -v.getTop();
        }
        return -1;
    }

    /**
     * 获取header的高度（去掉moreView）
     */
    private void getHeadHeight(){
        ViewTreeObserver observer = Header.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                if(!getHeadHeight){
                    int Height = Header.getMeasuredHeight();
                    View v = getActivity().findViewById(R.id.main_head_more);
                    headHeight = Height - v.getHeight();
                }
                getHeadHeight = true;
                return true;
            }
        });
    }
    /**
     * 加载兼职列表list的item 监听
     */
    private class itemlister implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            position--;
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
    //由定时器更新整个HeaderVeiw
    public void showViewPager(){
        switch (current){
            case 1:
                radio1.setChecked(true);
                break;
            case 2:
                radio2.setChecked(true);
                break;
            case 3:
                radio3.setChecked(true);
                break;
            default:
                break;
        }
        viewpager.setCurrentItem(current);
        current++;
      if(current==4){
          current = 1;
      }
    }
    //viewpager 控制小圆点的更新显示
    private class myPagerChanger implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            currentpoistion = position;
            setcurrentItem();
            //更新radiobutton 的状态
            upCircular();
        }
        /* *
         * state: 0空闲，1是滑行中，2加载完毕
         */
        @Override
        public void onPageScrollStateChanged(int state) {
            //控制计时器的状态
            if(state==0&&!isTaskRun){
                staticTools.getStaticTools().myLog(TAG,"pagerScroll change start Task");
                startTask();
           }else if(state==1&&isTaskRun){
                staticTools.getStaticTools().myLog(TAG,"pagerScroll change stop Task");
                stopTask();
            }
        }
    }
    //重新给viewpager定位
    private void setcurrentItem(){
        if(currentpoistion  ==0){
            currentpoistion = 3;
        }
        else if(currentpoistion==4){
            currentpoistion=1;
        }
        viewpager.setCurrentItem(currentpoistion, false);
    }
    /**
     * 开启定时任务
     */
    private void startTask() {
        // TODO Auto-generated method stub
        isTaskRun = true;
        mTimer = new Timer();
        mTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0x3);
            }
        };
        //第二个参数是多少时间后开始执行，第三个是执行间隔
        mTimer.schedule(mTask,5 * 1000, 6 * 1000);// 这里设置自动切换的时间，单位是毫秒，2*1000表示2秒
    }

    /**
     * 停止定时任务
     */
    private void stopTask() {
        // TODO Auto-generated method stub
        isTaskRun = false;
        mTimer.cancel();
    }
    /**
     * 更新小圆点状态
     *
     */
    private void upCircular() {
        switch(currentpoistion){
            case 1:
                radio1.setChecked(true);
                break;
            case 2:
                radio2.setChecked(true);
                break;
            case 3:
                radio3.setChecked(true);
                break;
            default:
                break;
        }
        current = currentpoistion;
    }
    //广播监听事件
    private  class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            location.setText(tools.getCity());
        }

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
        //如果不是正在开启计时器，就启动计时器
        if(!isTaskRun){
            startTask();
            isTaskRun = true;
        }
        staticTools.getStaticTools().myLog(TAG,"isTaskRun is "+isTaskRun);
        getHeadHeight();
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
        //如果计时器启动，就停止
        if(isTaskRun){
            stopTask();
            isTaskRun = false;
        }
        staticTools.getStaticTools().myLog(TAG,"isTaskRun is "+isTaskRun);
        getHeadHeight();
    }
    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        //解除广播的绑定
        getActivity().unregisterReceiver(myreceiver);
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        // TODO Auto-generated method stub
        super.onDetach();
    }
    //fragment 是否被隐藏，解决viewPager 有白画面的情况
    @Override
    public void onHiddenChanged (boolean hidden){
        changeViewPager(hidden);
        if(!isRefrashing&&!added&&!hidden){
            ispull = true;
            swipeRefreshLayout.setRefreshing(true);
            getHttp();
        }
    }
 public void changeViewPager(Boolean hidden){
     //如果在显示
     if(!hidden&&!isTaskRun){
         startTask();
         isTaskRun = true;
     }
     //如果被隐藏，并且在定时
     else if(hidden&&isTaskRun){
         stopTask();
         isTaskRun = false;
     }
 }

}
