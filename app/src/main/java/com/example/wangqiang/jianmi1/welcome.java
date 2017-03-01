package com.example.wangqiang.jianmi1;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.toolbox.Volley;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by wangqiang on 2016/7/3.
 */
public class welcome  extends Activity {
    private Animation Loadmation;
    private AlphaAnimation  start;
    private ScaleAnimation animation;
    private ImageView welcome,loading;
    private Button button;
    private Boolean isIn = false;
    //下拉刷新
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.welcome);
        init();
    }
    private void init(){
        welcome  = (ImageView)findViewById(R.id.welcome_image);
        loading = (ImageView)findViewById(R.id.welcome_Loading);
        button =  (Button)findViewById(R.id.welcome_button);
        Loadmation = AnimationUtils.loadAnimation(this,R.anim.loading);//点击跳转后等待的动画
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
                loading.startAnimation(Loadmation);
                ToMainActivity();
            }
        });
         animation =new ScaleAnimation(1.0f, 1.03f, 1.0f, 1.03f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        start = new AlphaAnimation(0.5f, 1.0f);
        animation.setDuration(4000);//设置动画持续时间
        start.setDuration(4000);
//        welcome.setAnimation(start);
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(animation);
        set.addAnimation(start);
        welcome.startAnimation(set);//图片的动画
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                try{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                Thread.sleep(3000);
                                ToMainActivity();
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
            @Override
            public void onAnimationEnd(Animation animation) {

            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    //跳转到主界面
    public void ToMainActivity(){
        if(!isIn){
            isIn = true;//避免二次跳转
            Intent intent = new Intent(welcome.this,MainActivity.class);
            startActivity(intent);
            welcome.this.finish();
        }
    }

    @Override
    public void onPause() {
        JPushInterface.onPause(welcome.this);
        super.onPause();
    }
    @Override
    public void onResume() {
        super.onResume();
        JPushInterface.onResume(welcome.this);
    }
}
