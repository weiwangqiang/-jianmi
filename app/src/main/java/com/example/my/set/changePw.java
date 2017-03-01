package com.example.my.set;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.wangqiang.jianmi1.R;

/**
 * Created by wangqiang on 2016/7/7.
 */
public class changePw extends Activity {
    private Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_my_set_setpw);
        findId();
        setlister();
        init();
    }
    private void findId(){
            back = (Button)findViewById(R.id.set_changePw_back);
    }
    private void setlister(){
            back.setOnClickListener(new mylister());
    }
    private class mylister implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.set_changePw_back:
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
