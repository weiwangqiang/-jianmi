package com.example.wangqiang.jianmi1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import com.example.my.myworkedEdit;
import com.example.mytools.staticTools;
import com.example.object.User;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**报名成功
 * Created by wangqiang on 2016/6/27.
 */
public class applySuccess extends Activity {
    private Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apply_success);
        findid();
        setlist();
    }
    private void findid(){
        back = (Button)findViewById(R.id.applySuccess_back);

    }
    private void setlist(){
        back.setOnClickListener(new lister());
    }
    private class lister implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.applySuccess_back:
                    finish();
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                    break;
                default:
                    break;

            }
        }
    }
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
