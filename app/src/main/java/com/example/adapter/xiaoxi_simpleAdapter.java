package com.example.adapter;

import android.content.Context;
import android.graphics.Bitmap;
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

import java.util.List;
import java.util.Map;

/**
 * Created by wangqiang on 2016/6/12.
 * 主界面list 的adapter
 */
public class xiaoxi_simpleAdapter extends SimpleAdapter {
    Context context;
    List<? extends Map<String, ?>> data;
    int resource;
    String[] from;
    int[] to;
    private RequestQueue requestQueue;
    viewHolder holder;
    Bitmap bitmap;

    public xiaoxi_simpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.context = context;
        this.data = data;
        this.resource = resource;
        this.from = from;
        this.to = to;
        requestQueue = Volley.newRequestQueue(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, null);
            holder = new viewHolder();
            holder.image = (ImageView) convertView.findViewById(to[0]);
            holder.title = (TextView) convertView.findViewById(to[1]);
            holder.content = (TextView) convertView.findViewById(to[2]);
            convertView.setTag(holder);
        } else {
            holder = (viewHolder) convertView.getTag();
        }
        String url = this.data.get(position).get(from[0]).toString();
        //设置tag 防止image错位
        holder.image.setTag(url);
        String tag = (String)holder.image.getTag();
        if(tag!=null&&tag.equals(url)){
            getImage(holder.image,url);
        }
        holder.title.setText(this.data.get(position).get(from[1]).toString());
        holder.content.setText(this.data.get(position).get(from[2]).toString());
        return convertView;
    }

    class viewHolder {
        ImageView image;
        TextView title;
        TextView content;
    }

   private void getImage(final ImageView imageView, String url) {
        /**
         * 第三第四个参数分别用于指定允许图片最大的宽度和高度，如果指定的网络图片的宽度或高度大于这里的最大值
         * ，则会对图片进行压缩，指定成0的话就表示不管图片有多大，都不会进行压缩。
         * 第五个参数用于指定图片的颜色属性，Bitmap.Config下的几个常量都可以在这里使用，
         * 其中ARGB_8888可以展示最好的颜色属性，每个图片像素占据4个字节的大小，
         * 而RGB_565则表示每个图片像素占据2个字节大小。
         */
        ImageRequest imageRequest = new ImageRequest(
                url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                    imageView.setImageBitmap(response);
            }
        }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(imageRequest);
    }

}
