package com.example.my;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytools.staticTools;
import com.example.wangqiang.jianmi1.R;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * Created by wangqiang on 2016/7/7.
 */
public class register extends Activity {
    private String TAG = "register";
    private Button back,getCode,next;
    private TextView register;
    private EditText getphone,getPass,getSurePass,Editcode;
    private String url = staticTools.url+"/insertUser";
    private String Codeurl = staticTools.url+"/Code?phoneNumber=";//验证码
    private String phone,oldpass,newpass,code,putcode;
    private OkHttpClient mOkHttpClient ;
    private Handler handler = new Handler(){
        public void handleMessage(Message mes){
            switch (mes.what){
                case 0x1:
                    Toast.makeText(register.this,"注册成功！",Toast.LENGTH_SHORT).show();
                    break;
                case 0x2:
                    Toast.makeText(register.this,"发送成功！",Toast.LENGTH_SHORT).show();
                    break;
                case 0x3:
                    Toast.makeText(register.this,"失败！",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_my_register);
        mOkHttpClient = new OkHttpClient();
        findId();
        setlister();
    }
    private void findId(){
            back = (Button)findViewById(R.id.register_back);
            getCode = (Button)findViewById(R.id.register_getcode);
            next = (Button)findViewById(R.id.register_next);

        getphone = (EditText)findViewById(R.id.main__register_phone);
        getPass = (EditText)findViewById(R.id.main__register_password);
        getSurePass = (EditText)findViewById(R.id.main__register_password_sure);
        Editcode = (EditText)findViewById(R.id.main_register_code);
    }
    private void setlister(){
            back.setOnClickListener(new mylister());
            getCode.setOnClickListener(new mylister());
            next.setOnClickListener(new mylister());
    }
    private class mylister implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.register_back:
                    finish();
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                    break;
                case R.id.register_next:
                    put();
                    break;
                case R.id.register_getcode:
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
            Toast.makeText(register.this,"手机号码不正确，请重新输入",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 提交
     */
    private void put(){
        phone = getphone.getText().toString().trim();
        oldpass = getPass.getText().toString().trim();
        newpass = getSurePass.getText().toString().trim();
        putcode = Editcode.getText().toString().trim();
        if(phone.length()<11||oldpass.length()<6||newpass.length()<6){
            if(phone.length()<11){
                Toast.makeText(register.this,"手机号不正确",Toast.LENGTH_SHORT).show();
                return ;
            }
            if(oldpass.length()==0){
                Toast.makeText(register.this,"请确认密码",Toast.LENGTH_SHORT).show();
                return ;
            }
            if(newpass.length()<6){
                Toast.makeText(register.this,"请输入6位数以上密码",Toast.LENGTH_SHORT).show();
                return ;
            }
            if(putcode==null){
                Toast.makeText(register.this,"验证码不能为空",Toast.LENGTH_SHORT).show();
                return ;
            }
            return;
        }
        Boolean b =oldpass.equals(newpass);
        if(!b){
            Toast.makeText(register.this,"两次密码不正确，请重新输入",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!code.equals(putcode)){
            Toast.makeText(register.this,"验证码不正确",Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String,String> map = new HashMap<>();
        map.put("username",phone);
        map.put("password",newpass);
        long time =System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String  t = sdf.format(time);
        Log.e(TAG,"registTime is "+t);
        map.put("registtime",time+"");
        JSONObject jsonObject = new JSONObject(map);
        putMes(jsonObject);
    }
    private void putMes(JSONObject object){
        Log.w(TAG,"url is "+url+" json is \n"+object.toString());
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                object.toString());
        final okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.e(TAG,"error!");
                handler.sendEmptyMessage(0x3);
            }
            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                Log.i(TAG," postJson response code is "+response.code()+" body is  "+response.body().string());
                handler.sendEmptyMessage(0x1);
            }
        });
    }

    /**
     * 获取验证码
     */
    private void sendCode(){
        url = Codeurl + phone;
        Log.e("url","forgetPassWord url is "+url);
        final okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.e(TAG,"error!");
                handler.sendEmptyMessage(0x3);
            }
            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                StringBuilder sb = new StringBuilder();
                sb.append(response.body().string());
                Log.w(TAG,"response  is "+ sb.toString());

//                Log.i(TAG," postJson response code is "+response.code()+" body is  "+response.body().string());
                if(response.code()==200){
                    parse(sb.toString());
                }
                handler.sendEmptyMessage(0x2);
            }
        });
    }
    public void parse(String res){
        try{

            JSONObject object = new JSONObject(res);
//            JSONArray array = object.getJSONArray("code");

            code = object.getString("code");
            Log.e(TAG," code is "+code);

        }catch(Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
