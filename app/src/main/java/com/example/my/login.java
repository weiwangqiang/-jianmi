package com.example.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.control.LoginState;
import com.example.main.BaseActivity;
import com.example.mytools.JsonUtil;
import com.example.mytools.staticTools;
import com.example.object.QQLogin;
import com.example.object.User;
import com.example.wangqiang.jianmi1.R;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by wangqiang on 2016/7/7.
 */
public class login extends BaseActivity {
    private String TAG = "login";
    private AlertDialog.Builder alertDialogBuilder;
    private    AlertDialog alertDialog;
    private ImageView QQ,WeiXin;
    private RequestQueue requestQueue;
    private Button back,login;
    private TextView register,forget;
    private EditText getname,getpass;
    private InputMethodManager imm;
    private String name = "" ,pass="";
    private String url = staticTools.url+"/loginUser";//user?uid=1
    private User user;
    //腾讯第三方登陆
    public static String mAppid = "1105550774";
    private Tencent tencent;
    private UserInfo userInfo; //qq用户信息
    private QQLogin qq;
    private String openId;

    private static final String WEI_ID = "wx62e1081cd468f5e2";

    private IWXAPI api;
    private BaseUiListener baseUiListener;
    private UserInfoListener userInfoListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);//这是强制隐藏
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//这是改变editText的状态
        setContentView(R.layout.main_my_login);
        findId();
        init();
        setlister();
    }
    private void init(){
        api = WXAPIFactory.createWXAPI(this,WEI_ID,true);
        api.registerApp(WEI_ID);

        //初始化请求队列
        requestQueue = Volley.newRequestQueue(login.this);
        baseUiListener = new BaseUiListener();
        user = userControl.getUser();
        alertDialogBuilder =new AlertDialog.Builder(login.this);
        View view =  getLayoutInflater().from(this).inflate(R.layout.jianzhi_message_dialog,null);
        alertDialog = alertDialogBuilder
                .setView(view)
                .create();
        alertDialog.setCanceledOnTouchOutside(false);
        qq = new QQLogin();//自定义的对象
        //第一个参数就是上面所说的申请的APPID，第二个是全局的Context上下文，这句话实现了调用QQ登录
        tencent = Tencent.createInstance(mAppid,getApplicationContext());
    }
    private void findId(){
            back = (Button)findViewById(R.id.login_back);
            register = (TextView)findViewById(R.id.main_login_register);
            getname = (EditText)findViewById(R.id.main_login_name);
            getpass = (EditText)findViewById(R.id.main_login_Pass);
            login = (Button)findViewById(R.id.main_login);
            QQ = (ImageView)findViewById(R.id.login_QQ);
            WeiXin = (ImageView)findViewById(R.id.login_weixin);
            forget = (TextView)findViewById(R.id.main_login_forgetPass);
            getname.setText("15751005437");
            getpass.setText("e10adc3949ba59abbe56e057f20f883e");
            Set<User> set = new HashSet<>();
            set.add(user);
            Iterator iterator = set.iterator();
            while (iterator.hasNext()){
              iterator.next();
            }
    }
    private void setlister(){
            back.setOnClickListener(this);
            register.setOnClickListener(this);
            login.setOnClickListener(this);
            forget.setOnClickListener(this);
            QQ.setOnClickListener(this);
            WeiXin.setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.login_back:
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                break;
            case R.id.main_login_register:
                Intent reIntent = new Intent(login.this,register.class);
                startActivity(reIntent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                break;
            case R.id.main_login:
                login();
                break;
            case R.id.main_login_forgetPass:
                //忘记密码界面
                Intent forgetIntent = new Intent(login.this,forgetPassWord.class);
                startActivity(forgetIntent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                break;
            case R.id.login_QQ:
                alertDialog.show();
                login_QQ();
                break;
            case R.id.login_weixin:
                alertDialog.show();
                login_weixin();
                break;
            default:
                break;
        }
    }
    private void login(){
        alertDialog.show();
        name = getname.getText().toString().trim();
        pass = getpass.getText().toString().trim();
        Map<String,String> map = new HashMap<>();
        map.put("username",name);
        map.put("password",pass);
        JSONObject object = new JSONObject(map);
//        JSONObject j = new JSONObject(text);
        postjson(object);
        Log.e("json",object.toString());
    }
    private void postjson(JSONObject object){
        JsonRequest<JSONObject> jsonResquest =
                new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject object) {
                        parse(object);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("error","login Error is "+volleyError);
            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("dat    aType", "json");
                headers.put("ContentType", "application/json; charset=UTF-8");
                return headers;
            }
        };
        requestQueue.add(jsonResquest);
    }
    /**
     * 解析json
     */
    private void parse(JSONObject resposne){
//        Log.e("login","user is "+user.getRealname());
        if(resposne!=null){
            try{
                user = JsonUtil.getUserObject(resposne.toString());
                if(user != null){
                    user.setPicurl((staticTools.url+ user.getPicurl()).toString());
                    userControl.setUser(user);
                    userControl.setState(new LoginState());
                }
                Log.d(TAG, "parse: user json is "+JsonUtil.getJSONObject(user).toString());
//                saveUser(resposne);
            }
            catch(Exception e){
                e.printStackTrace();
            }
            Log.e("login","user is "+user.getRealname()+"picture is "+user.getPicurl());
            alertDialog.cancel();
            this.finish();
        }
    }
    /**
     *保存用户
     */
    private void saveUser(JSONObject resposne) throws JSONException {
        user.setUid(resposne.getInt("uid"));
        user.setRealname(resposne.getString("realname"));
        user.setPicurl(staticTools.url+resposne.getString("picurl"));
        user.setSchool(resposne.getString("school"));
        user.setCollege(resposne.getString("college"));
        user.setIntoyear(resposne.getString("intoyear"));//入学时间
        user.setWorknum(resposne.getInt("worknum"));
        user.setGoodas(resposne.getInt("goodas"));
        user.setBadas(resposne.getInt("badas"));
        user.setRole(resposne.getString("role"));
        user.setGender(resposne.getString("gender"));
        user.setHeight(resposne.getInt("height"));
        user.setStudent(resposne.getInt("isStu"));
        user.setBirthday(resposne.getString("birthday"));
        user.setGoodas(resposne.getInt("goodas"));
        Long time = resposne.getLong("registtime");

        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        user.setRegisttime(sdf.format(date));//注册时间
        user.setUsername(resposne.getString("username"));
        user.setCity(resposne.getString("city"));
        user.setArea(resposne.getString("area"));
        user.setEmail(resposne.getString("email"));
        user.setMoney(resposne.getString("money"));
        user.setIntroduction(resposne.getString("introduction"));

        user.setIcnFile( Environment.getExternalStorageDirectory() +
                "/jianmi/"+user.getUsername()+".png");
        Log.e("login","user is "+user.getRealname()+" goodas "+user.getGoodas());

        userControl.setState(new LoginState());
    }

    private void login_QQ(){
    /**通过这句代码，SDK实现了QQ的登录，这个方法有三个参数，
     * 第一个参数是context上下文，第二个参数SCOPO 是一个String类型的字符串，
     * 表示一些权限官方文档中的说明：应用需要获得哪些API的权限，
     * 由“，”分隔。例如：SCOPE = “get_user_info,add_t”；所有权限用“all”
     * 第三个参数，是一个事件监听器，IUiListener接口的实例，这里用的是该接口的实现类
     */
        tencent.login(login.this,"all",baseUiListener);
    }
    private void login_weixin(){
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "jianmi_login";
        api.sendReq(req);
    }
    public void getUserInfo()
    {
        userInfo = new UserInfo(login.this, tencent.getQQToken());
        userInfoListener = new UserInfoListener();
        userInfo.getUserInfo(userInfoListener);
    }
    /**
     * 登陆方法的回调
     */
    private class BaseUiListener implements IUiListener{

        @Override
        public void onComplete(Object o) {
            Toast.makeText(login.this,"success",Toast.LENGTH_SHORT).show();
            Log.e("QQlogin"," QQ Login response is "+o.toString());
            try{
                JSONObject json  = new JSONObject(o.toString());
                openId = json.getString("openid");

                String accessToken = json.getString("access_token");
                String expires = json.getString("expires_in");
                //设置用户的登陆信息
                tencent.setAccessToken(accessToken, expires);
                tencent.setOpenId(openId);

            }catch(Exception e){
                e.printStackTrace();
            }
            alertDialog.cancel();
            getUserInfo();
        }

        @Override
        public void onError(UiError uiError) {
            alertDialog.cancel();
            uiError.toString();
        }

        @Override
        public void onCancel() {
            alertDialog.cancel();
            Toast.makeText(login.this,"cancel",Toast.LENGTH_SHORT).show();
            Log.e("QQlogin"," client have canceled  ");
        }
    }

    /**
     * 获取用户信息的回调
     */
    private class UserInfoListener implements IUiListener{

        @Override
        public void onComplete(Object o) {
            Log.e("userlistener "," userInfoListener response is "+o.toString());
            try{
                JSONObject json = new JSONObject(o.toString());
                String imageurlS = json.getString("figureurl_qq_1").trim();
                String imageUrlB = json.getString("figureurl_qq_2").trim();
                String nickname = json.getString("nickname").trim();
                String city = json.getString("city").trim();
                String gender = json.getString("gender").trim();
                user = userControl.getUser();
                //保存用户的openID

                qq.setOpenID(openId);
                qq.setCity(city);
                qq.setNickName(nickname);
                qq.setImageUri(imageurlS);
                user.setQqLogin(qq);
                Log.e("response","user information is image url is "+
                        imageurlS+"nickbname"+nickname+"city is "+city);
                userControl.setState(new LoginState());
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) {

        }

        @Override
        public void onCancel() {

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode, resultCode, data, baseUiListener);
        if (requestCode == Constants.REQUEST_API) {
            if (resultCode == Constants.REQUEST_LOGIN ||
                    resultCode == Constants.REQUEST_QZONE_SHARE ||
                    resultCode == Constants.REQUEST_OLD_SHARE) {
                Tencent.handleResultData(data, baseUiListener);
            }
        }
    }
    @Override
    protected void onDestroy(){
        if (tencent != null) {
            //注销登录
            tencent.logout(login.this);
        }
        super.onDestroy();
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
