package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.wangqiang.jianmi1.R;

import java.util.List;
import java.util.Map;

/**
 * Created by wangqiang on 2016/6/12.
 * 主界面list 的adapter
 */
public class zhaojianzhi_dialogAdapter extends SimpleAdapter
{
    Context context;
    List<? extends Map<String ,?>> data ;
    int resource;
    String[] from;
    int[] to;

    public zhaojianzhi_dialogAdapter(Context context, List<? extends Map<String ,?>> data, int resource, String[] from, int[] to) {
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
        ((TextView)holders[0]).setText(this.data.get(position).get(from[0]).toString());
        Boolean b = (Boolean) this.data.get(position).get("checked");
        ((CheckBox)holders[1]).setChecked(b);
        return view;
    }
}
