package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.wangqiang.jianmi1.R;

import java.util.List;
import java.util.Map;

/**
 * Created by wangqiang on 2016/6/12.
 *z在线聊天list 的adapter
 */
public class server_taikAdapter extends SimpleAdapter
{
    Context context;
    List<? extends Map<String ,?>> data ;
    int resource;
    String[] from;
    int[] to;
    public server_taikAdapter(Context context, List<? extends Map<String ,?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.context = context;
        this.data  = data;
        this.resource = resource;
        this.from  =from;
        this.to = to;
    }
    @Override
    public View getView (int position, View convertView, ViewGroup parent){
       View view = LayoutInflater.from(context).inflate(resource, null);
       View[] views = new View[to.length];
        for(int i = 0;i<to.length;i++){
            View tv = view.findViewById(to[i]);
            views[i] = tv;
        }
        view.setTag(views);
        View[] holders = (View[]) view.getTag();
        //判断是哪一个发送消息
         String who = this.data.get(position).get(from[0]).toString();
        switch (who){
            case "my":
                addmy(position,holders);
                break;
            case "kefu":
                addkefu(position,holders);
                break;
            default:
                break;
        }
        return view;

    }
    private void addmy(int position, View[] holders ){
        //获取时间
        String time = this.data.get(position).get(from[1]).toString();
        if(!time.isEmpty()){
            ((TextView)holders[0]).setText(time);
        }else {
            holders[0].setVisibility(View.GONE);
        }
        holders[1].setVisibility(View.GONE);
        ((TextView)holders[6]).setText(this.data.get(position).get(from[3]).toString());
    }
    private void addkefu(int position, View[] holders ){
        //获取时间
        String time = this.data.get(position).get(from[1]).toString();
        if(!time.isEmpty()){
            ((TextView)holders[0]).setText(time);
        }else {
            holders[0].setVisibility(View.GONE);
        }
        holders[4].setVisibility(View.GONE);
        ((TextView)holders[3]).setText(this.data.get(position).get(from[3]).toString());
    }

}
