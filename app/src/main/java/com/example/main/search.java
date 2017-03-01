package com.example.main;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mytools.my_Edit;
import com.example.wangqiang.jianmi1.R;

/**
 * Created by wangqiang on 2016/6/15.
 */
public class search extends Activity
{
    private ListView listView;
    private Button back;
    private my_Edit EdText;
    private TextView search;
    private InputMethodManager imm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_search);
        imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
        findview();
        setlistener();
        imm.hideSoftInputFromWindow(EdText.getWindowToken(), 0); //强制隐藏键盘
    }
    private void findview(){
        back = (Button)findViewById(R.id.search_back);
        EdText = (my_Edit)findViewById(R.id.search_searchJianzhi);
        search = (TextView)findViewById(R.id.search_searchButton);
        listView = (ListView) findViewById(R.id.search_list);
    }

    /**
     * 设置监听
     */
    private void setlistener(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
    @Override
    protected void onResume(){
        super.onResume();
        imm.hideSoftInputFromWindow(EdText.getWindowToken(), 0); //强制隐藏键盘
    }
}
