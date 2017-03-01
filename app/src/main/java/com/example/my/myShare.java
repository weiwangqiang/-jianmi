package com.example.my;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.wangqiang.jianmi1.R;

/**
 * Created by wangqiang on 2016/7/7.
 */
public class myShare extends Activity {
    private Button back;
    private TextView register,profit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_my_share);
        findId();
        setlister();
        init();
    }
    private void findId(){
            back = (Button)findViewById(R.id.share_back);
            profit = (TextView)findViewById(R.id.main_my_shareProfit);
    }
    private void setlister(){
            back.setOnClickListener(new mylister());
            profit.setOnClickListener(new mylister());
    }
    private class mylister implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.share_back:
                    finish();
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                    break;
                case R.id.main_my_shareProfit:
                    Intent intent = new Intent(myShare.this,myProfit.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
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
