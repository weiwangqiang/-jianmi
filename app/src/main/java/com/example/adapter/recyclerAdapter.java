package com.example.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.example.wangqiang.jianmi1.R;

import java.util.List;
import java.util.Map;

/**
 * Created by wangqiang on 2016/7/12.
 *
 */
public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHolder>
{
    private  MyViewHolder holder;
    public Context context;
    public int view;
    public List<Map<String,Object>> list;
    public recyclerAdapter(Context context,int view, List<Map<String,Object>> list ){
        this.context = context;
        this.view = view;
        this.list = list;
    }
    public int getHeight(){
        int height = holder.bt.getHeight()  * 6;
        return height;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(view, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        ViewGroup.LayoutParams params = holder.bt.getLayoutParams();
        params.height = RecyclerView.LayoutParams.WRAP_CONTENT;
        holder.bt.setLayoutParams(params);
         holder.bt.setText(list.get(position).get("kind").toString());
         Boolean ischeck = (Boolean) list.get(position).get("checked");
         holder.bt.setChecked(ischeck);
         holder.bt.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Boolean ischeck = !(Boolean) list.get(position).get("checked");
                 if(position==0&&ischeck){
                     for(int i = 1;i<list.size();i++){
                         list.get(i).put("checked",false);
                     }
                 }else if(position>0)
                 {
                     list.get(0).put("checked",false);
                 }
                 holder.bt.setChecked(ischeck);
                 list.get(position).put("checked",ischeck);
                 notifyDataSetChanged();
             }
         });
    }
    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return list.size();
    }
   public  class MyViewHolder extends RecyclerView.ViewHolder {
       RadioButton bt;
       public MyViewHolder(View itemView) {
         super(itemView);
        bt = (RadioButton) itemView.findViewById(R.id.my_partTime_kindText);
     }
 }
    public List<Map<String,Object>> getList(){
        return list;
    }
}
