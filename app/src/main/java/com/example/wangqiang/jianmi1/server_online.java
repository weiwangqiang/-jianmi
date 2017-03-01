package com.example.wangqiang.jianmi1;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.adapter.server_taikAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 实现在线咨询
 * Created by wangqiang on 2016/6/25.
 */
public class server_online extends Activity{
    private RelativeLayout back;
    private TextView history;
    private ListView listView;
    //存放聊天记录的List
    private List<Map<String,String>> listDate = new ArrayList<Map<String,String>>();
    private List<Map<String,Object>> listGride  = new ArrayList<Map<String,Object>>();
    private server_taikAdapter adapter ;//listview 的适配器
    private SimpleAdapter GridAdapter;
    private InputMethodManager  imm;
    //底部的组件
    private Button send,getVoice,show;//show 显示gridView
    private RadioButton turnVoice;//切换到语音
    private EditText getText;//文字输入框
    private GridView gridView;//
    private Boolean isshow = false;//是否显示gridView
    private Boolean VoiceShow = false;//是否显示gridView
    Handler handler = new Handler(){
        public void handleMessage(Message message){
                switch (message.what){
                    case 0x1:
                        getMessage();
                        break;
                    case 0x12:
                        break;
                    default:
                        break;
                }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.server_online);
        findid();
        initList();
        initGridView();
        setlister();
}
    private void findid(){
        back = (RelativeLayout)findViewById(R.id.server_online_back);
        history = (TextView)findViewById(R.id.server_online_talkHistory);
        listView = (ListView)findViewById(R.id.server_online_list);
        //底部的组件
        send = (Button)findViewById(R.id.server_online_send);
        getText = (EditText)findViewById(R.id.server_online_EditText);
        getVoice  =(Button)findViewById(R.id.server_online_getVoice);//长按录音
        turnVoice  = (RadioButton)findViewById(R.id.server_online_trunVoice);
        gridView = (GridView)findViewById(R.id.server_talking_gridView);
        show = (Button)findViewById(R.id.server_online_show);//show 显示底部的gridView
    }
    private void initList(){
        //隐藏EditText
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//        imm = (InputMethodManager) getSystemService(server_online.this.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getText.getWindowToken(), 0);
        adapter = new server_taikAdapter(server_online.this,
                listDate,R.layout.server_online_listitem,
                new String[]{"who","time","image","message"},
                new int[]{R.id.server_online_time,
                        R.id.server_online_relative1,
                        R.id.server_talking_ServerImage,
                        R.id.server_talking_TextLeft,
                        R.id.server_online_relative2,
                        R.id.server_talking_myImage,
                        R.id.server_talking_TextRight
                });
        listView.setAdapter(adapter);
    }
    private void initGridView(){
        addGridDate();
            GridAdapter = new SimpleAdapter(this,
                    listGride,R.layout.share_item,
                    new String[]{"picture","text"},
                    new int[]{R.id.share_item_image,
                            R.id.share_item_text});
        gridView.setAdapter(GridAdapter);
    }
private void addGridDate(){
    String[] string = new String[]{"推荐兼职","图片","拍照","位置"};
    int[] picture = new int[]{R.drawable.chat_share_bg,
                R.drawable.chat_image_bg,
                R.drawable.chat_takepic_bg,
                R.drawable.chat_location_bg};
    for(int i = 0 ;i<4;i++){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("picture",picture[i]);
        map.put("text",string[i]);
        listGride.add(map);
    }

}
    private void setlister(){
        back.setOnClickListener(new mylister());
        getVoice.setOnClickListener(new mylister());
        send.setOnClickListener(new mylister());
        show.setOnClickListener(new mylister());//show 显示底部的gridView
        turnVoice.setOnClickListener(new mylister());
        getText.setOnClickListener(new mylister());
        listView.setOnScrollListener(new myscroll());
        gridView.setOnItemClickListener(new itemlister());
        getText.addTextChangedListener(new EditTextWatch());
    }
    private class mylister implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.server_online_back:
                    finish();
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                    break;
                case R.id.server_online_getVoice:
                    getVoice();
                    break;
                case R.id.server_online_send:
                    sendMessage();
                    history.setText("共有"+listDate.size()+"条记录");
                    break;
                case R.id.server_online_trunVoice:
                    turnVoice();
                    break;
                case R.id.server_online_show:
                    showGridView();
                    break;
                case R.id.server_online_EditText:
                    //eidtText被点击。就把gridView隐藏
                    gridView.setVisibility(View.GONE);
                    isshow = false;
                    break;
                default:
                    break;
            }
        }
    }


    /**
     * editText的监听
     */
    private class EditTextWatch implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(getText.getText().length()!=0){
                send.setVisibility(View.VISIBLE);
                show.setVisibility(View.GONE);
            }
            else if(getText.getText().length()==0){
                send.setVisibility(View.GONE);
                show.setVisibility(View.VISIBLE);
            }
        }
    }


    /**
     * 控制GridView的显示
     */
    private void showGridView(){
        if(isshow){
            gridView.setVisibility(View.GONE);
            isshow = false;
        }
        else if(!isshow){
            gridView.setVisibility(View.VISIBLE);
            isshow = true;
        }
    }
    //控制是否显示getText
    private void turnVoice(){
        gridView.setVisibility(View.GONE);
        isshow = false;
        if(VoiceShow){
            //没点击turnVoice,
            getText.setVisibility(View.VISIBLE);
            getVoice.setVisibility(View.GONE);
            turnVoice.setChecked(false);
            VoiceShow  = false;
        }
        else if(!VoiceShow){
            //点击了turnVoice
            getText.setVisibility(View.GONE);
            getVoice.setVisibility(View.VISIBLE);
            turnVoice.setChecked(true);
            VoiceShow = true;
        }
    }
    /**
     * gridview 的lister
     */
    private class itemlister implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        break;
                    default :
                        break;
                }
        }
    }
    private class myscroll implements AbsListView.OnScrollListener{
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            imm.hideSoftInputFromWindow(getText.getWindowToken(), 0);
            //       改变EditText的状态
//            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    }
        private void getVoice(){
            getText.setVisibility(View.GONE);
            getVoice.setVisibility(View.VISIBLE);
    }

    /**
     * 客户端发送消息
     */
    private void sendMessage(){
        String mes = getText.getText().toString().trim();
        if(mes.isEmpty()){
            return;
        }
        addChatListDate(getTime(), mes);
         new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Thread.sleep(1000);
                            //更新消息
                            handler.sendEmptyMessage(0x1);
                        }catch (Exception e){
                                e.printStackTrace();
                        }

                    }
                }).start();
    }
    /**
     * 获取当前时间
     * @return
     */
  private String getTime(){
      Calendar c = Calendar.getInstance();
      int mounth  = c.get(Calendar.MONTH) + 1;//Calendar的月份是从 0开始算的， 所以要+1.
      int day  = c.get(Calendar.DATE);
      int h = c.get(Calendar.HOUR_OF_DAY);
      int m = c.get(Calendar.MINUTE);
      return mounth + "月"+ day + "号" + h + ":"+m;
  }

    /**
     * 添加聊天内容
     * @param time
     * @param mes
     */
 private void addChatListDate(String time,String mes){
     Map<String,String> map = new HashMap<String,String>();
     map.put("who","my");
     map.put("time",""+time);
     map.put("image","url");
     map.put("message",mes);
     listDate.add(map);
     adapter.notifyDataSetChanged();
     listView.setSelection(adapter.getCount() - 1);//自动下滑到底部
     getText.setText("");
 }
    /**
     * 接受客服消息
     */
    private void getMessage(){
        String mes = "你好，欢迎使用兼米android客户端!250号为您服务";
        Map<String,String> map = new HashMap<String,String>();
        map.put("who","kefu");
        map.put("time","");
        map.put("image","url");
        map.put("message",mes);
        listDate.add(map);
        adapter.notifyDataSetChanged();
        listView.setSelection(adapter.getCount() - 1);
        history.setText("共有"+listDate.size()+"条记录");
    }
    public void onBackPressed(){
        if(isshow){
            gridView.setVisibility(View.GONE);
            isshow = false;
        }
        else if(!isshow){
            super.onBackPressed();
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        }

    }
}
