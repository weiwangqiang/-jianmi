package com.example.my;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.main.BaseActivity;
import com.example.object.User;
import com.example.wangqiang.jianmi1.R;

import java.io.File;

/**
 * 用户简历
 * Created by wangqiang on 2016/7/7.
 */
public class myWorked extends BaseActivity {
    private String TAG = "myWorked";
    private RequestQueue requestQueue;
    private ImageView usericn;
    private Button back;
    private User user;
    private TextView register,edit,name,myWrite,city,school,study,entry,introduce,telephone,email;
    private RatingBar credit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_my_myworked);
        requestQueue = Volley.newRequestQueue(this);
        user = userControl.getUser();
        findId();
        setlister();
        init();
    }
    private void findId(){
        usericn = (ImageView)findViewById(R.id.main_my_worked_icn);
        back = (Button)findViewById(R.id.myWorked_back);
        edit = (TextView)findViewById(R.id.my_worked_edit);
        name = (TextView)findViewById(R.id.apply_worked_name);
        myWrite = (TextView)findViewById(R.id.my_worked_qianming);//签名
        city = (TextView)findViewById(R.id.main_my_city);
        school = (TextView)findViewById(R.id.main_my_school);
        study = (TextView)findViewById(R.id.main_my_study);
        entry = (TextView)findViewById(R.id.main_my_entry);
        introduce = (TextView)findViewById(R.id.main_my_ownIntroduce);
        credit = (RatingBar)findViewById(R.id.main_my_UserCreditr);
        telephone = (TextView)findViewById(R.id.main_my_telephone);
        email = (TextView)findViewById(R.id.main_my_email);
    }
    private void setlister(){
            back.setOnClickListener(this);
        edit.setOnClickListener(this);

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.myWorked_back:
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                break;
            case R.id.my_worked_edit:
                Intent intent = new Intent(myWorked.this,myworkedEdit.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                break;
            default:
                break;
        }
    }
    private void init(){
        //添加用户头像
        String url = userControl.getUser().getPicurl();
        Log.w("main_my","main_icn  url is "+url);
        File file = new File(user.getIcnFile());
        Log.w(TAG," 用户的头像路径为： "+file);
        if(file.exists()){
            Log.w(TAG,"用户头像从本地获取");
            usericn.setImageDrawable(Drawable.createFromPath(user.getIcnFile()));
        }
        else if(url!=null){
            Log.w(TAG,"用户头像从网络获取");
            initImage(url);
        }
//        name,myWrite,city,school,study,entry,introduce
        name.setText(user.getRealname());
        myWrite.setText(user.getRole());//个性签名
        city.setText("所在城市:   "+user.getCity());
        school.setText("就读大学:   "+user.getSchool());
        study.setText("所在专业:   "+user.getCollege());//专业
        entry.setText("入学时间:   "+user.getIntoyear());//入学时间
        introduce.setText(user.getIntroduction()    );
        credit.setRating((float)user.getGoodas());//评价
        telephone.setText("电话:   "+user.getUsername());
        email.setText("邮箱:   "+user.getEmail());
    }
    private void initImage(String url){
        ImageRequest imageRequest = new ImageRequest(
                url,new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        usericn.setImageBitmap(response);
                        Log.w(TAG,"从网络上加载图片成功");
                    }
                }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG,"从网络上加载图片失败");
            }
        });
        requestQueue.add(imageRequest);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
         init();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
