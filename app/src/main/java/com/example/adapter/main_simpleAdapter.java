package com.example.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.wangqiang.jianmi1.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by wangqiang on 2016/6/12.
 * 主界面list 的adapter
 */
public class main_simpleAdapter extends SimpleAdapter
{
    private String TAG  = "main_simpleAdapter";
    String filePath = Environment.getExternalStorageDirectory()+"/jianmi/pictures";
    Context context;
    List<? extends Map<String ,?>> data ;
    int resource;
    String[] from;
    int[] to;
    private RequestQueue requestQueue;
    ViewHolder holder;
    /**
     * Constructor
     *
     * @param context  The context where the View associated with this SimpleAdapter is running
     * @param data     A List of Maps. Each entry in the List corresponds to one row in the list. The
     *                 Maps contain the data for each row, and should include all the entries specified in
     *                 "from"
     * @param resource Resource identifier of a view layout that defines the views for this list
     *                 item. The layout file should include at least those named views defined in "to"
     * @param from     A list of column names that will be added to the Map associated with each
     *                 item.
     * @param to       The views that should display column in the "from" parameter. These should all be
     *                 TextViews. The first N views in this list are given the values of the first N columns
     */
    public main_simpleAdapter(Context context, List<? extends Map<String ,?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.context = context;
        this.data  = data;
        this.resource = resource;
        this.from  =from;
        this.to = to;
        requestQueue = Volley.newRequestQueue(context);
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
            holder.jishuan  = (TextView) convertView.findViewById(to[4]);
            holder.location = (TextView) convertView.findViewById(to[5]);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        String url = this.data.get(position).get("Jurl").toString();
        holder.imageView.setTag(url);
        String tag = (String)holder.imageView.getTag();
        File f = new File(filePath);
        if(!f.exists()){
            f.mkdirs();
        }
        if(tag!=null&&tag.equals(url)){
            File file = getFileByUrl(url);
            if(file.exists()){
              holder.imageView.setImageDrawable(Drawable.createFromPath(file.toString()));
            }else
            {
                getImage(holder.imageView,url,position);
            }
        }else
        {
            holder.imageView.setImageResource(R.drawable.load_waiting);
        }
        holder.title.setText(this.data.get(position).get(from[1]).toString());
        holder.salary.setText(this.data.get(position).get(from[2]).toString());
        holder.Pubtime.setText(this.data.get(position).get(from[3]).toString());
        holder.jishuan.setText(this.data.get(position).get(from[4]).toString());
        holder.location.setText(this.data.get(position).get(from[5]).toString());
        return convertView;
    }
    private class ViewHolder{
        ImageView imageView;
        TextView title;
        TextView salary;
        TextView Pubtime;
        TextView jishuan;
        TextView location;
    }
    private void getImage(final ImageView imageView, final String url,final int position) {

        ImageRequest imageRequest = new ImageRequest(
                url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                final Bitmap bitmap = response;
                final File file = getFileByUrl(url);
                if(!file.exists()){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
                             final byte[] Byte= outputStream.toByteArray();
                            try{
                                if(file!=null){
                                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                                    fileOutputStream.write(Byte);
                                    fileOutputStream.flush();
                                    fileOutputStream.close();
                                    outputStream.close();
                                }
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                if(response!=null) {
                    imageView.setImageBitmap(response);
                }
            }
        }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                imageView.setImageResource(R.drawable.load_waiting);
            }
        });
        requestQueue.add(imageRequest);
    }
    public File getFileByUrl(String url){
        String[] str = url.split("/");
        String pic = str[str.length-1];
        StringBuffer sb = new StringBuffer();
        sb.append(filePath);
        sb.append("/");
        sb.append(pic);
        File file = new File(sb.toString());
        return file;
    }
}
