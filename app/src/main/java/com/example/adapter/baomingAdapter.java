package com.example.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.object.Orders;
import com.example.wangqiang.jianmi1.R;

import java.util.List;

/**报名adapter
 * Created by wangqiang on 2016/7/11.
 */
public class baomingAdapter extends BaseAdapter {
    Context context;
    List<Orders> data ;
    int resource;
    String[] from;
    private int posit;
    int[] to;
    ViewHolder holder;
    public baomingAdapter(Context context, List<Orders> data, int resource, String[] from, int[] to) {
        this.context = context;
        this.data  = data;
        this.resource = resource;
        this.from  =from;
        this.to = to;
    }
    /**
     * How many items are in the data set represented by this Adapter.
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return data.size();
    }
    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return null;
    }
    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent){
        if(null ==convertView){
            convertView = LayoutInflater.from(context).inflate(resource, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(to[0]);
            holder.title    = (TextView) convertView.findViewById(to[1]);
            holder.salary   = (TextView) convertView.findViewById(to[2]);
            holder.Pubtime  = (TextView) convertView.findViewById(to[3]);
            holder.location = (TextView) convertView.findViewById(to[4]);
            holder.connect  = (TextView) convertView.findViewById(to[5]);
            holder.cancel   = (TextView) convertView.findViewById(to[6]);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText("jis"+data.get(position).getJid());
        holder.salary.setText("state is "+data.get(position).getState());
        holder.Pubtime.setText(data.get(position).getApplytime().toString());
        holder.location.setText(data.get(position).getOid());
        holder.connect.setOnClickListener(new lister(position));
        holder.cancel.setOnClickListener(new lister(position));
        return convertView;
    }
    private class ViewHolder{
        ImageView imageView;
        TextView title;
        TextView salary;
        TextView Pubtime;
        TextView location;
        TextView connect;
        TextView cancel;
    }
  private class lister implements View.OnClickListener{
      private int position;
      private lister(int position){
       this.position = position;
      }

      @Override
      public void onClick(View v) {
         switch(v.getId()){
              case R.id.my_apply_listItem_connect:
                  Toast.makeText(context,"联系"+position,Toast.LENGTH_SHORT).show();
                  Log.e("adapter","adapter is checked");
                  break;
             case R.id.my_apply_listItem_cancel:
                 data.remove(position);
                 notifyDataSetChanged();
                 Toast.makeText(context,"删除",Toast.LENGTH_SHORT).show();
                 break;
          }
      }
  }
}





