package com.example.my;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wangqiang.jianmi1.R;

/**
 * Created by wangqiang on 2016/7/7.
 */
public class mySuggest extends Activity implements View.OnClickListener{
    private Button back;
    private TextView register;
    private InputMethodManager imm;

    private EditText email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);//这是强制隐藏
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//这是改变editText的状态
        setContentView(R.layout.main_my_mysuggest);
        findId();
        setlister();
        init();
    }
    private void findId(){
            back = (Button)findViewById(R.id.mySuggest_back);
            email = (EditText)findViewById(R.id.mySuggest_email);
    }
    private void setlister(){
            back.setOnClickListener(this);
            email.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.mySuggest_back:
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                break;
            case R.id.mySuggest_email:
                email.setFocusable(true);
                break;
            default:
                break;
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
