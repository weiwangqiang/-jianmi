package com.example.mytools;

import android.util.Log;

import com.example.object.Job;
import com.example.object.Orders;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangqiang on 2016/11/18.
 */

public class parse  {
    private String TAG = "parse";
    public Job parseJob(JSONObject response){
        Job job = new Job();//兼职对象//
        try{
            job.setTitle(response.getString("title"));
            job.setJobtype(response.getString("jobtype"));
            job.setArea(response.getString("area"));
            //发布时间
            long time = response.getLong("pubtime");
            Date d = new Date(time);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            job.setPubtime(sdf.format(d));
            job.setSalary(response.getString("salary"));
            job.setWorktime(response.getString("worktime"));
            job.setJid(response.getInt("jid"));
            job.setBusinessid(response.getInt("businessid"));
            job.setPersonnum(response.getInt("personnum"));
            job.setLastnum(response.getInt("lastnum"));
            job.setBusinessid(response.getInt("businessid"));
            job.setContent(response.getString("content"));
            job.setEndtime(response.getString("endtime"));
            job.setWorkplace(response.getString("workplace"));
            job.setGender(response.getString("gender"));
            job.setInterviewtime(response.getString("interviewtime"));
            job.setJurl(staticTools.url+response.getString("jurl"));
//            Log.e(TAG,"pubTime is "+job.getPubtime()+" JID is "+job.getJid()+" UID is "+job.getBusinessid());
//            JSONArray array = response.getJSONArray("orders");
//            Log.e(TAG,"job title is "+
//                    job.getTitle()+"get JsonArray is "+array.toString()+"length is "+array.length());
        }catch(Exception e){
            e.printStackTrace();
        }
        return job;
    }
    public List<Orders> parseOrder(String response) {
        List<Orders> list = new ArrayList<>();
        try{
            JSONArray json = new JSONArray(response);
            for(int i = 0;i<json.length();i++){
                Orders orders = new Orders();
                JSONObject object = json.getJSONObject(i);
                orders.setOid(object.getInt("oid"));
                orders.setBid(object.getInt("bid"));
                orders.setJid(object.getInt("jid"));
                orders.setBid(object.getInt("bid"));
                Long time = object.getLong("applytime");
                Timestamp timestamp = new Timestamp(time);
//                Log.e("parse"," time is "+timestamp.toString());
                orders.setApplytime(timestamp);
                orders.setState(object.getInt("state"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }
}
