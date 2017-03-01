package com.example.my;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.wangqiang.jianmi1.R;

/**
 * 钱包
 * Created by wangqiang on 2016/7/7.
 */
public class myProfit extends Activity {
    private Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_my_profit);
        findId();
        setlister();
        init();
    }
    private void findId(){
            back = (Button)findViewById(R.id.profit_back);
    }
    private void setlister(){
            back.setOnClickListener(new mylister());
    }
    private class mylister implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.profit_back:
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
