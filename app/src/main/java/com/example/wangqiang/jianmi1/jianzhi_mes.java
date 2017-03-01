package com.example.wangqiang.jianmi1;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.SqlHelper.jz_helpe;
import com.example.control.tencentBaseUiLister;
import com.example.main.BaseActivity;
import com.example.my.login;
import com.example.mytools.myScrollView;
import com.example.mytools.parse;
import com.example.mytools.shareDialog;
import com.example.mytools.staticTools;
import com.example.mytools.waitDialog;
import com.example.object.Job;
import com.example.object.User;
import com.tencent.connect.common.Constants;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.tauth.Tencent;

import org.json.JSONObject;


/**兼职界面的父界面
 * Created by wangqiang on 2016/6/22.
 */
public class jianzhi_mes extends BaseActivity implements
        IWXAPIEventHandler,myScrollView.OnScrollChangedListener{
    private String TAG = "jianzhi_mes";
    private RadioButton back,share,collect;
    private Button apply;
    private Animation in,out;
    private View content_header;
    private myScrollView scrollView;
    private View HeaderView,TitleView,BottomView,viewBg;
    private TextView HeaderTitle ;
    private TextView title,introduce,workPlace, salary,gender,seeNum,
            company,workDay,workTime,OverTime,workContent,pubTime,location,personnum;
    private TextView TopTitle,TopSalary;
    private ImageView logo,position;
    private RatingBar ratingBar;
    private jz_helpe helpe;

    //分享界面
    private shareDialog dialog;
    private tencentBaseUiLister baseuiLister;//qq分享回调监听
    private Boolean iscollect = false;
    private Boolean getViewHeight = false;


    private RequestQueue requestQueue;
    private Dialog sharedialog,waitdialog;
    private String url = staticTools.url+"/job?";//获取兼职详情
    private User user;
    private Job job ;
    private int jid = 0,bid = 0;//兼职id
    private int ViewTH= 0;//兼职详情View的高度
    private int statusHeight = 0;//状态栏高度
    private int pullHeight = 0;//上拉的高度
    public Handler handler = new Handler(){
        public void handleMessage(Message mes){
            switch(mes.what){
                case 0x1:
                    //请求成功
                    setViewObserver();
                    showContent();
                    break;
                case 0x2:
                    break;

                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jianzhi_message);
        setParam();
        findid();
        viewBg.setBackgroundResource(R.drawable.jianzhi_bg);
        initAnimation();
        setlist();
        getJob();
    }
    /*这里可以监听View 的初始化是否完成*/
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.e(TAG,"--->>onWindowFocusChanged");
    }
    public void setParam(){
        helpe = new jz_helpe(this);
        baseuiLister =  new tencentBaseUiLister(jianzhi_mes.this);
        sharedialog = shareDialog.newInstance(jianzhi_mes.this,baseuiLister);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        user = userControl.getUser();
        jid = bundle.getInt("jid");
        bid = bundle.getInt("Bid");
        url  = url+"jid="+jid+"&&uid="+user.getUid();
        Log.e(TAG," base Url is "+url);
    }
    public void initAnimation(){
        in = AnimationUtils.loadAnimation(this, R.anim.in_from_bottom);
        out = AnimationUtils.loadAnimation(this, R.anim.out_to_bottom);
    }
    private void findid(){
        back = (RadioButton)findViewById(R.id.jianzhi_message_back);
        share = (RadioButton) findViewById(R.id.jianzhi_message_share);
        collect = (RadioButton) findViewById(R.id.jianzhi_bottom_collect);
        apply = (Button) findViewById(R.id.jianzhi_mes_apply);//申请button
        HeaderView = findViewById(R.id.jianzhi_headerView);
        TitleView = findViewById(R.id.jianzhi_titleView);//第二个标题
        BottomView = findViewById(R.id.jianzhi_BottomView);
        HeaderTitle = (TextView)findViewById(R.id.jianzhi_HeaderTitle);//标题view兼职详情
        scrollView = (myScrollView)findViewById(R.id.jianzhi_scrollView);
        TopTitle = (TextView)findViewById(R.id.jianzhi_TopTitle);
        TopSalary = (TextView)findViewById(R.id.jianzhi_TopSalary);
        position = (ImageView)findViewById(R.id.jianzhi_mes_Position);//查看地图详情
        HeaderTitle.setAlpha(0);
        contentid();
    }
    public void contentid(){
        viewBg = findViewById(R.id.jianzhi_bg);//兼职详情的背景
        content_header = findViewById(R.id.content_header);
        title =(TextView)findViewById(R.id.jianzhi_mes_title);
        location = (TextView)findViewById(R.id.jianzhi_mes_location);//标题下的位置信息
        pubTime = (TextView)findViewById(R.id.jianzhi_mes_PubTime);
        seeNum =  (TextView)findViewById(R.id.jianzhi_mes_seeNum);//看过的人数
        introduce =(TextView)findViewById(R.id.jianzhi_mes_introduce);//兼职介绍
        salary = (TextView)findViewById(R.id.jianzhi_mes_salary);
        workPlace = (TextView)findViewById(R.id.jianzhi_mes_WorkPlace);
        personnum = (TextView)findViewById(R.id.jianzhi_mes_Renshu);
        gender = (TextView)findViewById(R.id.jianzhi_mes_gender);
        workDay = (TextView)findViewById(R.id.jianzhi_mes_workDay);
        workTime = (TextView)findViewById(R.id.jianzhi_mes_workTime);
        OverTime = (TextView)findViewById(R.id.jianzhi_mes_OverTime);
        workContent = (TextView)findViewById(R.id.jianzhi_mes_workContent);
        logo = (ImageView)findViewById(R.id.jianzhi_mes_company_logo);
        company = (TextView)findViewById(R.id.jianzhi_mes_company);
        ratingBar = (RatingBar)findViewById(R.id.jianzhi_mes_companyCredit);
    }


    private void setlist(){
        back.setOnClickListener(this);
        share.setOnClickListener(this);
        collect.setOnClickListener(this);
        position.setOnClickListener(this);
        workPlace.setOnClickListener(this);
        scrollView.setOnScrollChangedListener(this);
        apply.setOnClickListener(this);
    }
    private void initView(){
        Log.w(TAG,"--->initView");
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusHeight = getResources().getDimensionPixelSize(resourceId);
        }
        int height = metric.heightPixels;
        int H = introduce.getBottom() - title.getTop();
        int content_height = height - H -statusHeight - 5;
        ViewGroup.LayoutParams params = content_header.getLayoutParams();
        params.height = content_height;
        content_header.setLayoutParams(params);
        waitdialog.cancel();
    }

    private void getJob(){
        waitdialog = new waitDialog(jianzhi_mes.this);
        waitdialog.show();
        requestQueue = Volley.newRequestQueue(jianzhi_mes.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG,"jianzhi Message is "+ response.toString());
                        parseJson(response);
                        handler.sendEmptyMessage(0x1);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
    public void parseJson(JSONObject response){
        parse p = new parse();
        job = p.parseJob(response);
        if(null==job){
            Toast.makeText(this,"出错！",Toast.LENGTH_SHORT).show();
            return ;
        }
    }
    public void showContent(){
        title.setText(job.getTitle());
        TopTitle.setText(job.getTitle());
        TopSalary.setText(job.getSalary());
        location.setText(job.getArea());
        pubTime.setText(job.getPubtime());
        salary.setText("工资："+job.getSalary());
        workTime.setText("每天工作时间："+job.getWorktime());
        workDay.setText("工作日期：");
        personnum.setText("招聘人数："+job.getPersonnum()+"，还剩下名额："+job.getLastnum());
        workContent.setText(job.getContent());
        OverTime.setText("报名截止时间："+job.getEndtime());
        gender.setText("性别："+job.getGender());
        introduce.setText(getResources().getString(R.string.jianzhi_mes_introduce)+job.getInterview()+"");
        workPlace.setText("工作地点："+job.getWorkplace());
        Log.w(TAG,"---->showContent"+"pubtime is "+job.getPubtime());
    }
//微信请求回调
    @Override
    public void onReq(BaseReq baseReq) {
    Log.e(TAG,"onreq base Req is "+baseReq.toString());
    }
//微信回调
    @Override
    public void onResp(BaseResp baseResp) {

    }

    /**
     * scrollView 的监听
     * @param offset
     */
    @Override
    public void onScrollChange(int offset) {
        if(reachTitle()&&pullHeight==0){
            pullHeight = offset;
        }
        setAlpha(offset);
        setShow(offset);
    }

    /**
     * 控制veiwd的透明度
     * @param offset
     */
    public void setAlpha(int offset){
        Log.e(TAG,"pullHeight is "+pullHeight);
        if(offset<=0||(offset>0&&!reachTitle())){
            Log.w(TAG,"alpha is 0 ");
            HeaderTitle.setAlpha(0);
            TitleView.setAlpha(0);
        }else if(offset>0&&reachTitle()){
            if((offset - pullHeight)<=ViewTH){
                float alpha = ((float)offset - (float)pullHeight)/ViewTH;
                Log.w(TAG,"alpha is "+alpha);
                HeaderTitle.setAlpha(alpha);
                TitleView.setAlpha(alpha);
            }
            else{
                Log.w(TAG,"alpha total is 1 ");
                TitleView.setAlpha(1);
                HeaderTitle.setAlpha(1);
            }
        }
    }
    /**
     * 控制是否显示View
     */
    public void setShow(int offset){
        if(reachTitle()&&!ShowView()){
            Log.w(TAG,"view Visible");
            BottomView.setVisibility(View.VISIBLE);
            TitleView.setVisibility(View.VISIBLE);
            BottomView.startAnimation(in);
        }
        else if(!reachTitle()&&ShowView()&&offset<100)
        {
                 Log.w(TAG,"view gone");
                BottomView.setVisibility(View.GONE);
                TitleView.setVisibility(View.GONE);
                BottomView.startAnimation(out);
        }
    }
    //内容的title 是否到达TitleView处
    public Boolean reachTitle(){
        int[] position = new int[2];
        title.getLocationInWindow(position);
        int height = position[1] - statusHeight;
        ViewTH = HeaderTitle.getHeight();
        Log.w(TAG,"height is "+height+" ViewTH is "+ViewTH);
        return height <= ViewTH;
    }
    public Boolean ShowView(){
        return BottomView.getVisibility()==View.VISIBLE ;
    }

    /**
     * 申请兼职
     */
    public void apply(){
        userControl.apply(this,jid,bid);
    }
    /**
     * 分享功能
     */
    private void Share(){
        sharedialog.show();
    }

    /**
     * 显示地图
     */
    private void showMap(){
        Intent intent = new Intent(this,map.class);
        startActivity(intent);
    }

    public void collect(){
        if(!userControl.isLogin()) {
            Toast.makeText(this,"登录",0).show();
            return;//没有等登陆就返回
        }
        if(collect.isChecked()){
            helpe.insert(job,user.getUid()+"");
        }
        else
        if(!collect.isChecked()&& helpe.hasjid(jid+"",user.getUid()+"")){
            helpe.deletejid(jid+"",user.getUid()+"");
        }
    }
    /**
     * 是否显示已收藏
     */
    public void initCollect(){
        if(!islogin()) {
            return;
        }
        if(helpe.hasjid(jid+"",user.getUid()+"")){
            collect.setChecked(true);
        }
    }
    //判断是否登陆
    public boolean islogin(){
        return userControl.isLogin();
    }
    /**
     * 跳转到登陆界面
    */
    public void login(){
        Intent intent = new Intent(this,login.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    /**
     * 改变Collect的状态
     */
    private void ChangeCollect() {
        if(!islogin()){
            collect.setChecked(false);
            login();
            return;
        }
        iscollect = !iscollect;
        collect.setChecked(iscollect);
    }

//    //要想调用IUiListener 必须重写此函数
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
      Tencent.onActivityResultData(requestCode, resultCode, data,baseuiLister);
        switch (requestCode){
            case Constants.REQUEST_QQ_SHARE:
            case Constants.REQUEST_QZONE_SHARE:
                if(resultCode == Constants.ACTIVITY_OK)
                    Tencent.handleResultData(data, baseuiLister);
                break;
        }

    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.jianzhi_message_back:
                finish();
                break;
            case R.id.jianzhi_message_share:
                Share();
                break;
            case R.id.jianzhi_bottom_collect:

                ChangeCollect();
                break;
            case R.id.jianzhi_mes_Position:
                showMap();
                break;
            case R.id.jianzhi_mes_apply:
                apply();
                break;
            default:
                break;
        }
    }



    /**
     * 监听scrollView 是否初始化完成
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setViewObserver(){
        ViewTreeObserver observer = scrollView.getViewTreeObserver();
        observer.addOnDrawListener(new ViewTreeObserver.OnDrawListener(){
            @Override
            public void onDraw() {
                if(!getViewHeight){
                    initView();
                }
                getViewHeight = true;
            }
        });
    }
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    @Override
    public void onPause(){
        super.onPause();
        collect();
    }
    @Override
    public void onResume(){
        super.onResume();
        initCollect();
        user = userControl.getUser();
    }

}
