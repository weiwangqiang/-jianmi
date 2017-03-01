package com.example.my;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.adapter.ApplyAdapter;
import com.example.control.UserControl;
import com.example.mytools.parse;
import com.example.mytools.staticTools;
import com.example.mytools.waitDialog;
import com.example.object.Orders;
import com.example.object.User;
import com.example.wangqiang.jianmi1.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangqiang on 2016/7/7.
 */
public class myApply extends FragmentActivity implements View.OnClickListener{
    private String TAG = "myApply";
    private Button back;
    private ViewPager viewpager;
    private RadioGroup group;
    private Dialog waitdialog;
    private RequestQueue requestQueue;
    private List<Fragment> list = new ArrayList<>();
    private List<Orders> data = new ArrayList<>();
    private String url = staticTools.url +"/orders?";
    private User user;
    public UserControl userControl = UserControl.getUserControl();

    private int[] RbId= new int[]{R.id.my_apply_bm,R.id.my_apply_ly,R.id.my_apply_sg,R.id.my_apply_js};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_my_apply);
        user =  userControl.getUser();
        findId();
        setlister();
        init();
    }
    private void findId(){
            back = (Button)findViewById(R.id.applyList_back);
            viewpager = (ViewPager)findViewById(R.id.my_apply_pager);
            group = (RadioGroup)findViewById(R.id.my_apply_RadioGroup);
    }
    private void setlister(){
            back.setOnClickListener(this);
            group.setOnCheckedChangeListener(new changer());
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.applyList_back:
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                break;
            default:
                break;
        }
    }

    private class changer implements RadioGroup.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch(checkedId){
                case R.id.my_apply_bm:
                    viewpager.setCurrentItem(0);
                    break;
                case R.id.my_apply_ly:
                    viewpager.setCurrentItem(1);
                    break;
                case R.id.my_apply_sg:
                    viewpager.setCurrentItem(2);
                    break;
                case R.id.my_apply_js:
                    viewpager.setCurrentItem(3);
                    break;
                default:break;
            }
        }
    }

    private void init(){
        fragmentBM fragment = new fragmentBM();

        StringBuffer buffer = new StringBuffer(url);
        buffer.append("uid="+user.getUid());
        Bundle bundle = new Bundle();
        bundle.putString("url",buffer.toString());
        fragment.setArguments(bundle);
        list.add(fragment);//报名
        list.add(new fragmentLY());//录用
        list.add(new fragmentSG());//上岗
        list.add( new fragmentJS());//结算
        //刚开始只会加载前两个fragment
        ApplyAdapter adapter  = new ApplyAdapter(getSupportFragmentManager(), list);
        viewpager.setAdapter(adapter);
        viewpager.setOffscreenPageLimit(3);
        viewpager.setCurrentItem(0);
        viewpager.addOnPageChangeListener(new pagerlist());
//        getMessage();
    }

    public void getMessage(){
        waitdialog = new waitDialog(myApply.this);
        waitdialog.show();
        StringBuffer buffer = new StringBuffer(url);
        buffer.append("uid="+user.getUid());
        Log.e(TAG,"--->url is "+buffer.toString());
        requestQueue = Volley.newRequestQueue(myApply.this);
        StringRequest stringRequest = new StringRequest(buffer.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseJson(response);
                        Log.d("TAG", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", error.getMessage(), error);
            }
        });
        requestQueue.add(stringRequest);

    }
    public void parseJson(String response){
        Log.e(TAG,"response is "+response);
        parse p = new parse();
    }
    private class pagerlist implements ViewPager.OnPageChangeListener{
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
                    changButton(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
    //更新radioButton的状态
    private void changButton(int position){
        ((RadioButton)findViewById(RbId[position])).setChecked(true);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
