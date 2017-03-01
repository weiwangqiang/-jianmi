package com.example.my;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.wangqiang.jianmi1.R;

/**
 * Created by wangqiang on 2016/7/7.
 */
public class realName extends Activity {
    private Button back;
    private InputMethodManager imm;

    private TextView register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);//这是强制隐藏
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//这是改变editText的状态
        setContentView(R.layout.main_my_realname);
        findId();
        setlister();
        init();
    }
    private void findId(){
            back = (Button)findViewById(R.id.realName_back);
    }
    private void setlister(){
            back.setOnClickListener(new mylister());
    }
    private class mylister implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.realName_back:
                    finish();
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                    break;
                default:
                    break;
            }
        }
    }
    private void init(){

    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
