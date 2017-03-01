package com.example.wangqiang.jianmi1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.control.UserControl;
import com.example.my.myworkedEdit;
import com.example.mytools.staticTools;
import com.example.object.User;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * Created by wangqiang on 2016/6/27.
 */
public class apply extends Activity {
    private String TAG= "apply";
    private User user;
    private InputMethodManager imm;
    private EditText getEdit;
    private Button back, Bapply;
    private TextView name, edit;
    private String working;
    private int jid, bid;
    private RequestQueue requestQueue;
    private OkHttpClient mOkHttpClient;

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private String url = staticTools.url + "/order";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apply);
        requestQueue = Volley.newRequestQueue(apply.this);
        mOkHttpClient = new OkHttpClient();
        Intent intent = getIntent();
        jid = intent.getIntExtra("jid", 0);
        bid = intent.getIntExtra("bid", 0);
        Log.e(TAG," ----->>jid is "+jid+" uid is "+bid);
        user =  UserControl.getUserControl().getUser();
        findid();
        initView();
        setlist();
    }

    private void findid() {
        back = (Button) findViewById(R.id.apply_back);
        name = (TextView) findViewById(R.id.apply_myName);
        edit = (TextView) findViewById(R.id.apply_write);
        getEdit = (EditText) findViewById(R.id.apply_moreEdit);
        Bapply = (Button) findViewById(R.id.apply_button);

    }

    private void initView() {
        //隐藏EditText
        imm = (InputMethodManager) getSystemService(apply.this.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getEdit.getWindowToken(), 0);
        name.setText("我是：" + user.getRealname() + "," + user.getGender());
    }

    private void setlist() {
        back.setOnClickListener(new lister());
        Bapply.setOnClickListener(new lister());
        edit.setOnClickListener(new lister());

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    private class lister implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.apply_back:
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    break;
                case R.id.apply_button:
                    apply();
                    break;
                case R.id.apply_write:
                    Intent intent = new Intent(apply.this, myworkedEdit.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    break;
                default:
                    break;

            }
        }
    }

    private void apply() {
        working = getEdit.getText().toString();
        shoeDialog();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("jid", jid);
        map.put("bid", bid);
        map.put("uid", user.getUid());
        JSONObject object = new JSONObject(map);
        Log.e("apply", "apply json is " + object.toString() + "-------");
        postjson(object);
    }

    private void postjson(JSONObject object) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), object.toString());
        final okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "response is failure");
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                Log.e(TAG, "response is "+response.toString());
                if (response.code() == 200) {
                    Intent intent = new Intent(apply.this,applySuccess.class);
                    startActivity(intent);
                    alertDialog.cancel();
                    finish();
                }
            }
        });
    }

    private void postMes() {
        shoeDialog();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("jid", jid);
        map.put("bid", bid);
        map.put("uid", user.getUid());

        JSONObject object = new JSONObject(map);
        Log.e("apply", "apply json is " + object.toString());
        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.GET, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("response ", "response is success ");
                        alertDialog.cancel();
                        Intent intent = new Intent(apply.this, applySuccess.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("onfauilure", "server have Error response");
                alertDialog.cancel();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");
                return headers;
            }
        };
        requestQueue.add(jsonRequest);//添加请求到队列中
    }

    private void shoeDialog() {
        alertDialogBuilder = new AlertDialog.Builder(apply.this);
        View view = getLayoutInflater().from(this).inflate(R.layout.jianzhi_message_dialog, null);
        alertDialog = alertDialogBuilder
                .setView(view)
                .create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
