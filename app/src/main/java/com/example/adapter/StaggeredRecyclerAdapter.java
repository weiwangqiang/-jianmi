package com.example.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.example.wangqiang.jianmi1.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangqiang on 2016/7/12.
 */
public class StaggeredRecyclerAdapter extends RecyclerView.Adapter<StaggeredRecyclerAdapter.MyViewHolder>{
    private List<Object> list ;
    private List<Integer> hei;
    private Context context;
    private MyViewHolder holder;
    private  Boolean ischecked = false;
    private int view;
    public StaggeredRecyclerAdapter(Context context,int view,List<Object> list ){
        this.context = context;
        this.view = view;
        this.list = list;
        hei = new ArrayList<>(32);
        for(int i= 0;i<8;i++){
            hei.add(100);
        }
        for(int n =0 ;n<24;n++){
            hei.add(180);
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(view, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        ViewGroup.LayoutParams lp = holder.rb.getLayoutParams();
        lp.height = hei.get(position);
        holder.rb.setLayoutParams(lp);
//        holder.rb.setBackgroundResource(R.drawable.jianzhi_mess_topbg_off);
        if(position<=8||position==16||position==24){
            holder.rb.setText(list.get(position).toString());
            holder.im.setVisibility(View.GONE);
            holder.rb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(position>0&&position<8)
                    {
                        changeTotle(position);
                    }
                    Log.e("list2","position is "+position+"text is "+list.get(position));
                }
            });
        }else
        {
            ischecked = (Boolean)list.get(position);
            if(ischecked){
                holder.im.setVisibility(View.VISIBLE);
            }
            holder.rb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   Boolean is = (Boolean) list.get(position);
                    changeImage(is,position);
                }
            });
        }

    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    private void changeTotle(int position){
        int count = 0;
        for(int i = 0 ;i<3;i++){
            position =position+8;
            Boolean is = (Boolean) list.get(position);
            if(is){
            count++;
            }
            Log.e("boolean","position is " +position+" boolean is"+is );
        }
        if(count!=3){
            changeTrue(position);
        }else
        {
            changeFalse(position);
        }
        Log.e("ewo ","I do not  have returned ");
    }
    private void changeTrue(int position){
        for(int i = 0 ;i<3;i++){
            position =position+8;
            list.set(position,true);
        }
        notifyDataSetChanged();
    }
    private void changeFalse(int position){
        for(int i = 0 ;i<3;i++){
            position =position+8;
            list.set(position,false);
        }
        notifyDataSetChanged();
    }
    private void changeImage(Boolean Checked,int position){
         if(!Checked){
             holder.im.setVisibility(View.VISIBLE);
         }
        else
         {
             holder.im.setVisibility(View.INVISIBLE);
         }
        list.set(position,!Checked);
        notifyDataSetChanged();
    }
 class MyViewHolder extends RecyclerView.ViewHolder{
    RadioButton rb;
     ImageView im;
     public MyViewHolder(View itemView) {
         super(itemView);
            rb = (RadioButton)itemView.findViewById(R.id.my_partTime_TimeText);
            im = (ImageView)itemView.findViewById(R.id.partTime_imageView);
     }
 }
    public List<Object> getList(){
        return list;
    }

}
