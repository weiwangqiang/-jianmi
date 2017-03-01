package com.example.my;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.adapter.baomingAdapter;
import com.example.mytools.parse;
import com.example.mytools.waitDialog;
import com.example.object.Orders;
import com.example.wangqiang.jianmi1.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wangqiang on 2016/7/10.
 * 报名
 */
public class fragmentBM extends Fragment{
    private String TAG = "fragmentBM";
    private View view;
    private ListView listview;
    private RequestQueue requestQueue;

    private List<Map<String,String>> list = new ArrayList<>();
    private Dialog waitdialog;
    private List<Orders> data = new ArrayList<>();
    private baomingAdapter adapter;
    private String url;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_my_apply_list,container,false);
        Bundle  bundle = getArguments();
        url = bundle.getString("url");
        init();
        return view;
    }
    public View getView(){
        return view;
    }
    public void init(){
        listview = (ListView)view.findViewById(R.id.my_apply_list);
        getMessage();
    }
    public void getMessage(){
        waitdialog = new waitDialog(getActivity());
        waitdialog.show();
        requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseJson(response);
                        Log.d("TAG", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", error.getMessage(), error);
            }
        });
        requestQueue.add(stringRequest);

    }
    public void parseJson(String response){
        Log.e(TAG,"response is "+response);
        parse p = new parse();
        data = p.parseOrder(response);
        initList();
    }
    public void initList(){
        adapter = new baomingAdapter(getActivity(),data,
                R.layout.main_my_apply_listitem,
                new String[]{"image","title","time","money","location"},
                new int[]{R.id.my_apply_listItem_image,
                        R.id.my_apply_listItem_title,
                        R.id.my_apply_listItem_money,
                        R.id.my_apply_listItem_time,
                        R.id.my_apply_listItem_location,
                        R.id.my_apply_listItem_connect,
                        R.id.my_apply_listItem_cancel
                });
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("list","listview is checked");
                Toast.makeText(getActivity(),"listview",Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }
//调用唤醒
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    /**
     * 打开另一个activity,退出程序，调用onPause,onStop
     */
    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }
    //移动viewpager 会销毁view 但不彻底销毁
    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
    }
//退出activity 则将fragment 彻底销毁
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        // TODO Auto-generated method stub
        super.onDetach();
    }
    @Override
    public void onHiddenChanged (boolean hidden){
        Log.e("fragment 报名 is ",""+hidden);
    }


}
