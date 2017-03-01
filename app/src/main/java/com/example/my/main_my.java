package com.example.my;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.SqlHelper.jz_helpe;
import com.example.main.BaseFragment;
import com.example.object.User;
import com.example.wangqiang.jianmi1.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import static com.example.wangqiang.jianmi1.R.drawable.position;

/**
 * Created by wangqiang on 2016/6/19.
 */
public class main_my extends BaseFragment {
    private String TAG = "main_my";
    private User user;
    private View view;
    private ImageView userIcn;
    private RequestQueue requestQueue;
    private jz_helpe helpe;
    private Button set;
    private RelativeLayout apply,realNmae,worked,health,evaluate,
            shixi,partTime,share,suggest,login,Usercollect;
    private TextView name,school,credit,collect;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_my,container,false);
        user = userControl.getUser();
        helpe = new  jz_helpe(getActivity());
        requestQueue = Volley.newRequestQueue(getActivity());
        init();
        return view;
    }
    private void init(){
        findId();
        setlister();
    }
    private void findId(){
        name = (TextView)view.findViewById(R.id.main_my_name);
        school = (TextView)view.findViewById(R.id.main_my_school);
        credit = (TextView)view.findViewById(R.id.main_my_credit);//信用
        collect = (TextView)view.findViewById(R.id.main_my_collect);//收藏

        set = (Button)view.findViewById(R.id.main_my_set);
        userIcn = (ImageView)view.findViewById(R.id.main_my_icn);
        apply = (RelativeLayout)view.findViewById(R.id.main_my_LApply);
        realNmae = (RelativeLayout)view.findViewById(R.id.main_my_LRealName);
        worked = (RelativeLayout)view.findViewById(R.id.main_my_LMyworked);
        health = (RelativeLayout)view.findViewById(R.id.main_my_LMyHealth);
        shixi = (RelativeLayout)view.findViewById(R.id.main_my_LMyshixi);
        partTime = (RelativeLayout)view.findViewById(R.id.main_my_LMyTime);
        login = (RelativeLayout)view.findViewById(R.id.main_my_loginRel);
        share = (RelativeLayout)view.findViewById(R.id.main_my_LMyshare);
        suggest = (RelativeLayout)view.findViewById(R.id.main_my_LMysuggest);
        Usercollect = (RelativeLayout)view.findViewById(R.id.main_my_collectRel);
        evaluate = (RelativeLayout)view.findViewById(R.id.main_my_evaluate);
    }
    private void setlister(){
        set.setOnClickListener(new mylister());
//        Usericn.setOnClickListener(new mylister());
        apply.setOnClickListener(new mylister());
        realNmae.setOnClickListener(new mylister());
        worked.setOnClickListener(new mylister());
        health.setOnClickListener(new mylister());
        shixi.setOnClickListener(new mylister());
        partTime.setOnClickListener(new mylister());
        share.setOnClickListener(new mylister());
        suggest.setOnClickListener(new mylister());
        login.setOnClickListener(new mylister());
        Usercollect.setOnClickListener(new mylister());
        evaluate.setOnClickListener(new mylister());
    }
    private class mylister implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.main_my_set:
                    skip(new Intent(getActivity(),mySet.class));
                    break;
                case R.id.main_my_loginRel:
                    userControl.resume(getActivity());
                    break;
                case R.id.main_my_LApply:
                    userControl.applyRecord(getActivity());
                    break;
                case R.id.main_my_LRealName:
                    userControl.realName(getActivity());

                    break;
                case R.id.main_my_LMyworked:
                    skipTo(new Intent(getActivity(),myWorked.class));
                    break;
                case R.id.main_my_LMyHealth:
                    userControl.health(getActivity());

                    break;
                case R.id.main_my_LMyshixi:
                    userControl.practice(getActivity());

                    break;
                case R.id.main_my_LMyTime:
                    userControl.jobWant(getActivity());

                    break;
                case R.id.main_my_LMyshare:
                    userControl.shareRecord(getActivity());

                    break;
                case R.id.main_my_LMysuggest:
                    userControl.feedBack(getActivity());
                    break;
                case R.id.main_my_collectRel:
                    userControl.collectRecord(getActivity());
                    break;
                case R.id.main_my_evaluate:
                    skipTo(new Intent(getActivity(),evaluate.class));
                    break;
                default:
                    break;
            }
        }
    }
    //判断是否登陆
    public boolean islogin(){
        return userControl.isLogin();
    }
    /**跳转到登陆界面
     **/
    public void login(){
        Intent intentJian = new Intent(getActivity(),login.class);
        startActivity(intentJian);
        getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }
    //是否登陆，否则跳转到登陆界面
    public void skipTo(Intent intent){
        if(islogin()){
            skip(intent);
        }
        //登陆就跳转到我的简历
        else
            login();
    }
    //跳转界面
    public void skip(Intent intent){
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }
    private void show(){
        user = userControl.getUser();
        if(user.getRealname().length()!=0){
            name.setText(user.getRealname());
            Log.e(TAG," name is "+name.getText());
        }
        else
            name.setText("点击登陆");
        if(user.getSchool().length()!=0){
            school.setText(user.getSchool());
            Log.e(TAG," school is "+school.getText());

        }
        else
            school.setText("");
        credit.setText(user.getCredit()+"分");
        File file = new File( user.getIcnFile());
        if(file.length()==0) {
            userIcn.setImageResource(R.drawable.load_waiting);
            return;
        }
        Log.w(TAG," 用户的头像路径为： "+file);
        if(file.exists()){
            Log.w(TAG,"从本地获取头像 ");
            userIcn.setImageDrawable(Drawable.createFromPath(user.getIcnFile()));
        }
        else if(user.getPicurl()!=null){
            String uri = user.getPicurl();
            initImage(uri);
        }
        credit.setText(user.getGoodas()+"分");
        collect.setText(helpe.getNumber(user.getUid()+""));
        Log.e(TAG," collect is "+collect.getText());

    }
    private void initImage(String url){
        ImageRequest imageRequest = new ImageRequest(
                url,new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                final Bitmap bitmap = response;
                final File file = getFileByUrl(user.getIcnFile());
                Log.e(TAG," position is "+position+" -->> file is "+file);
                if(!file.exists()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                            final byte[] Byte = outputStream.toByteArray();
                            try {
                                if (file != null) {
                                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                                    fileOutputStream.write(Byte);
                                    fileOutputStream.flush();
                                    fileOutputStream.close();
                                    outputStream.close();
                                }
                                Log.e(TAG, "success to write bitmap to locate file is --" + file);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                userIcn.setImageBitmap(response);

            }
        }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(imageRequest);
    }
        public File getFileByUrl(String url){
            File file = new File(url);
            return file;
        }
    /**
     *开始创建调用onStart,onResume
     */
    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        Log.e("HJJ", "main_my **** onStart...");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.e("HJJ", "main_my **** onResume...");
        // TODO Auto-generated method stub
        super.onResume();
        show();
    }

    /**
     * 打开另一个activity,退出程序，调用onPause,onStop
     */
    @Override
    public void onPause() {
        Log.e("HJJ", "main_my **** onPause...");
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.e("HJJ", "main_my **** onStop...");
        // TODO Auto-generated method stub
        super.onStop();

    }
    @Override
    public void onDestroyView() {
        Log.e("HJJ", "main_my **** onDestroyView...");
        // TODO Auto-generated method stub
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        Log.e("HJJ", "main_my **** onDestroy...");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.e("HJJ", "main_my **** onDetach...");
        // TODO Auto-generated method stub
        super.onDetach();
    }
    //fragment 是否被隐藏，解决viewPager 有白画面的情况
    @Override
    public void onHiddenChanged (boolean hidden){
        if(!hidden){
            show();
        }
        Log.e("main_my is hidded ",""+hidden);
    }

}
