package com.example.my;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.control.LogoutState;
import com.example.main.BaseActivity;
import com.example.my.set.changePw;
import com.example.my.set.tuishong;
import com.example.object.User;
import com.example.wangqiang.jianmi1.R;


/**
 * Created by wangqiang on 2016/7/7.
 */
public class mySet extends BaseActivity {
    private Button back,pull,setPw,OffLine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_my_set);
        findId();
        setlister();
        init();
    }
    private void findId(){
            back = (Button)findViewById(R.id.set_back);
            pull = (Button)findViewById(R.id.main_my_SetPullMes);
            setPw = (Button)findViewById(R.id.main_my_SetPassWord);
            OffLine = (Button)findViewById(R.id.main_my_setOutLogin);

    }
    private void setlister(){
            back.setOnClickListener(this);
        pull.setOnClickListener(this);
        setPw.setOnClickListener(this);
        OffLine.setOnClickListener(this);

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.set_back:
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                break;
            case R.id.main_my_SetPullMes:
                Intent intent = new Intent(mySet.this, tuishong.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                break;
            case R.id.main_my_SetPassWord:
                Intent intentset = new Intent(mySet.this, changePw.class);
                startActivity(intentset);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                break;

            case R.id.main_my_setOutLogin:
                if(userControl.isLogin()){
                    OffLine();
                }

                break;
            default:
                break;
        }
    }
    private void init(){

    }
    public void OffLine(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("提示"); //设置标题
        builder.setMessage("是否确认退出?"); //设置内容
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                OffLogin();
                dialog.dismiss(); //关闭dialog
            }
        })
        .setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    /**
     * 离线
     */
    public void OffLogin(){
        userControl.setState(new LogoutState());
        userControl.setUser(new User());
        Toast.makeText(this,"成功退出",Toast.LENGTH_SHORT   ).show();
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
