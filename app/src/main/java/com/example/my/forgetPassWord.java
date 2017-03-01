package com.example.my;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mytools.staticTools;
import com.example.wangqiang.jianmi1.R;

/**
 * 找回密码
 * Created by wangqiang on 2016/7/7.
 */
public class forgetPassWord extends Activity {
    private Button back,getCode,put;
    private EditText getphone,getPass,getSurePass,Editcode;
    private TextView register;
    private String url = staticTools.url+"/Code?phoneNumber=";//验证码
    private String phone,oldpass,newpass,code;
    private RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_my_forgetpass);
        requestQueue = Volley.newRequestQueue(forgetPassWord.this);
        findId();
        setlister();
    }
    private void findId(){
            back = (Button)findViewById(R.id.forget_back);
            getCode = (Button)findViewById(R.id.forget_getcode);
            put = (Button)findViewById(R.id.forget_put);
        getphone = (EditText)findViewById(R.id.main_forgetPhone);
        getPass = (EditText)findViewById(R.id.main_forgetPass);
        getSurePass = (EditText)findViewById(R.id.main_forget_surePass);
        Editcode = (EditText)findViewById(R.id.main_forgetCode);

    }
    private void setlister(){
            back.setOnClickListener(new mylister());
            getCode.setOnClickListener(new mylister());
        put.setOnClickListener(new mylister());
    }
    private class mylister implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.forget_back:
                    finish();
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                    break;
                case R.id.forget_put:
                    put();
                    break;
                case R.id.forget_getcode:
                    getCode();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 获验证码
     */
    private void getCode(){
        phone = getphone.getText().toString().trim();
        if(phone.length()==11){
            sendCode();
        }
        else
        {
            Toast.makeText(forgetPassWord.this,"手机号码不正确，请重新输入",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 提交
     */
    private void put(){
        phone = getphone.getText().toString().trim();
        oldpass = getPass.getText().toString().trim();
        newpass = getSurePass.getText().toString().trim();
        code = Editcode.getText().toString().trim();
        if(phone==null||oldpass==null||code==null||newpass==null){
            if(phone==null){
                Toast.makeText(forgetPassWord.this,"请输入手机号",Toast.LENGTH_SHORT).show();
            }
            if(oldpass==null){
                Toast.makeText(forgetPassWord.this,"请输入新密码",Toast.LENGTH_SHORT).show();
            }
            if(newpass==null){
                Toast.makeText(forgetPassWord.this,"请确认新密码",Toast.LENGTH_SHORT).show();
            }
            if(code==null){
                Toast.makeText(forgetPassWord.this,"验证码不能为空",Toast.LENGTH_SHORT).show();
            }
            return;
        }
        Boolean b =!oldpass.equals(newpass);
        if(!oldpass.equals(newpass)){
            Toast.makeText(forgetPassWord.this,"两次密码不正确，请重新输入",Toast.LENGTH_SHORT).show();
            return;
        }
    }
    private void sendCode(){
        url = url + phone;
        Log.e("url","forgetPassWord url is "+url);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
            Toast.makeText(forgetPassWord.this,"success!",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(forgetPassWord.this,"Error!",Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
