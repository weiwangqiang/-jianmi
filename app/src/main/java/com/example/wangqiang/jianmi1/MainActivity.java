package com.example.wangqiang.jianmi1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.jpush.myReceiver;
import com.example.main.main_fragment;
import com.example.my.main_my;
import com.example.mytools.NetState;
import com.example.mytools.staticTools;
import com.example.xiaoxi.main_xiaoxi;
import com.example.zhaojianzi.main_zhaojianzi;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
public class MainActivity extends FragmentActivity {
    /**
     * 应用主界面
     */
    private String TAG = "MainActivity";
    public static final String MESSAGE_RECEIVED_ACTION = "cn.jpush.android.intent.MESSAGE_RECEIVED";
    private static int current = 0 ;//当前显示的fragment
    private Long exitTime = 0L;
    private RadioButton button1,button2,button3,button4;
    private Button redXX,redMY;
    private List<RadioButton> blist = new ArrayList<>();
    private TextView internet;
    private MyReceiver myreceiver;//广播事件
    private IntentFilter filter;//广播的过滤条件
    private NetState receiver;//网络状态监听
    private RequestQueue requestQueue;
    private String tag = "MYTAG";
    private List<Fragment> fragments = new ArrayList<>();
    public  Handler handler = new Handler(){
        public void handleMessage(Message mes){
            switch(mes.what){
                case 0x1:
                    showView(1);
                    current = 1 ;
                    button2.setChecked(true);
                    break;
                default:
                    break;
            }
        }
    };

    //下拉刷新
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        init();
        initPush();
        Log.w(TAG,"-->>savedInstanceState is null ? ");
        if(savedInstanceState==null){
            Log.w(TAG,"savedInstanceState is null and init fragment");
            addFragment();
        }
        else
        {
            Log.w(TAG,"-->>savedInstanceState is not null ！ fragments size is "+fragments.size());
            Log.w(TAG,"fragmentManager size is "+getSupportFragmentManager().getFragments().size());
            fragments.clear();
            fragments.add(getSupportFragmentManager().findFragmentByTag("f0"));
            fragments.add(getSupportFragmentManager().findFragmentByTag("f1"));
            fragments.add(getSupportFragmentManager().findFragmentByTag("f2"));
            fragments.add(getSupportFragmentManager().findFragmentByTag("f3"));
            current = savedInstanceState.getInt("current");
            Log.w(TAG,"-->> get fragment by tag  0  class is  "+ fragments.get(0).getClass()+
                    "  2 tag is "+fragments.get(1).getTag() );
            Log.e(TAG,"current pager is "+current);
//            if(fragments.get(0) instanceof main_fragment){
////                getSupportFragmentManager().beginTransaction().add(R.id.main_framelayout,
////                        fragments.get(0)).commit();
////                changeView(0);
//                Log.w(TAG,"-->>show but donot add  main_fragment ");
//            }
        }
    }
    private void initPush(){
        JPushInterface.init(MainActivity.this);
        myReceiver receiver = new myReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(receiver, filter);
    }
    /**
     * 导入fragment 包时导入import android.support.v4.app.Fragment;不然会出错，
     * android  studio 自带的包版本太高，
     * 刚开不要加载全部的fragments
     */
    private void addFragment(){
        fragments.add(new main_fragment());
        fragments.add(new main_zhaojianzi());
        fragments.add(new main_xiaoxi());
        fragments.add(new main_my());
        getSupportFragmentManager().beginTransaction().add(R.id.main_framelayout,fragments.get(0),"f"+0).commit();
        for(int i = 1;i<fragments.size();i++){
            getSupportFragmentManager().beginTransaction().add(R.id.main_framelayout,fragments.get(i),"f"+i).commit();
            getSupportFragmentManager().beginTransaction().hide(fragments.get(i-1))
                    .show(fragments.get(i)).commit();
        }
        getSupportFragmentManager().beginTransaction().hide(fragments.get(fragments.size()-1))
                .show(fragments.get(0)).commit();
        showButton(0);
    }
    private void init(){
        findId();
    //  绑定网络监听
        bindNetlister();
        //绑定广播 监听more 的点击事件
        myreceiver = new MyReceiver();
        filter = new IntentFilter("Main_changed");
        this.registerReceiver(myreceiver,filter);
    }
    public void findId(){
        button1 =  (RadioButton)findViewById(R.id.main_home);
        button2 =  (RadioButton)findViewById(R.id.main_zhaojianzi);
        button3 =  (RadioButton)findViewById(R.id.main_xiaoxi);
        button4 =  (RadioButton)findViewById(R.id.main_my);
        redXX = (Button)findViewById(R.id.main_redCircleXX);
        redMY = (Button)findViewById(R.id.main_redCircleMY);

        blist.add(button1);
        blist.add(button2);
        blist.add(button3);
        blist.add(button4);
        button1.setOnClickListener(new mylister());
        button2.setOnClickListener(new mylister());
        button3.setOnClickListener(new mylister());
        button4.setOnClickListener(new mylister());
        //小红点提示
//        redXX.setText("9");
//        redMY.setText("12");
        internet  = (TextView)findViewById(R.id.main_notInternet_text);
    }

    /**
     * 绑定网络监听
     */
    public void bindNetlister(){
        receiver = new NetState();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver.setNetLister(new NetWorkLister());
        this.registerReceiver(receiver, filter);
        receiver.onReceive(MainActivity.this, null);
    }

    private class NetWorkLister implements NetState.NetLister{
        @Override
        public void OutInternet() {
            internet.setVisibility(View.VISIBLE);
        }
        @Override
        public void GetInternet() {
            internet.setVisibility(View.GONE);

        }
    }
    private class mylister implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.main_home:
                    changeView(0);
                    break;
                case R.id.main_zhaojianzi:
                    changeView(1);
                    break;
                case R.id.main_xiaoxi:
                    changeView(2);
                    break;
                case R.id.main_my:
                    changeView(3);
                    break;
                default:
                    break;

            }
        }
    }
    //更新界面
    private void changeView(int which){
        showButton(which);
        showView(which);
        current = which ;
    }
    private void showButton(int b){
        for(int i = 0;i<blist.size();i++){
            blist.get(i).setChecked(false);
        }
        blist.get(b).setChecked(true);
    }
    //广播监听事件
    private  class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            showButton(1);
            showView(1);
            current = 1 ;
        }
    }

    /***
     * hide 在add or show 方法的后面，可以解决fragment
     * 在使用animation 动画的时候，退出界面会变白的情况
     * @param c
     */
    public void showView(int c){
            if(!fragments.get(c).isAdded()){
                if(current < c){
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_left)
                            .add(R.id.main_framelayout,fragments.get(c),"f"+c)
                            .hide(fragments.get(current))
                            .commit();
                    Log.e(TAG,"current < c");
                } else if(current > c){
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right)
                            .add(R.id.main_framelayout,fragments.get(c),"f"+c)
                            .hide(fragments.get(current))
                            .commit();
                    Log.e(TAG,"current > c");
                }
                Log.e(TAG,"current class is "+fragments.get(current).getClass()+
                        "  next class is "+fragments.get(c).getClass());
                staticTools.getStaticTools().myLog(TAG,
                        "in do not added  case c is " + c+" current is "+current);
            }
            else  if (fragments.get(c).isAdded()) {
                if(current < c){
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_left)
                            .show(fragments.get(c))
                            .hide(fragments.get(current))
                            .commit();
                }
                else if(current > c)
                {
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right)
                            .show(fragments.get(c))
                            .hide(fragments.get(current))
                            .commit();
                }
                Log.e(TAG," current class is "+fragments.get(current).getClass()+
                        "  next class is "+fragments.get(c).getClass());
                staticTools.getStaticTools().myLog(TAG," added  case c is " + c+" current is "+current);
            }
        }

    //解除广播的绑定事件
    public void onBackPressed(){
        super.onBackPressed();
        if(null!=myreceiver){
            this.unregisterReceiver(myreceiver);
        }
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        //极光推送
        JPushInterface.onResume(MainActivity.this);
        //如果不是正在开启计时器，就启动计时器
        super.onResume();

    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        JPushInterface.onPause(MainActivity.this);
        super.onPause();
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        //根据标志取消所有的网络请求
        if(requestQueue!=null){
            requestQueue.cancelAll(tag);
        }
        super.onStop();

    }
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        //解除广播的绑定
        try{
            if(receiver!=null){
                this.unregisterReceiver(receiver);
            }
            if(myreceiver!=null){
                this.unregisterReceiver(myreceiver);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        super.onDestroy();
        current = 0;
    }
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        switch(keyCode)
        {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - exitTime > 4000) {                                         //如果两次按键时间间隔大于2秒，则不退出
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    exitTime = secondTime;//更新firstTime
                    return true;
                } else {                                                    //两次按键小于2秒时，退出应用
                    MainActivity.this.finish();
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("current",current);
        super.onSaveInstanceState(outState);
        Log.e(TAG,"-->>onStanceState be called current pager is "+current);
    }
}
