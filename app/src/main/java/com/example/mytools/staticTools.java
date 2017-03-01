package com.example.mytools;

import android.util.Log;

/**
 * Created by wangqiang on 2016/7/3.
 */
public  class staticTools  {
    public static  String mycity = "镇江市";//全局的地理位置
    private static Boolean module;
    public static staticTools st = null;
//    public static String url = "http://119.29.178.251:8080/android";
    public static String url  = "http://192.168.0.100:8080/android";

        public static staticTools getStaticTools(){
                if(st==null){
                 st = new staticTools();
        }
         return st;
        }
        public static String getCity(){
                return mycity;
        }
        public static void  setCity(String city){
                mycity  =  city;
        }
        public void myLog(String tag,String message){
                if(module){
                Log.e(tag,message);
        }
        }
        public  void setDebug(Boolean b){
                module = b;
        }

}