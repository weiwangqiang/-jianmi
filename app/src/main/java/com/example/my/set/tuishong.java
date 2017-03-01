package com.example.my.set;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.example.wangqiang.jianmi1.R;

/**
 * Created by wangqiang on 2016/7/7.
 */
public class tuishong extends Activity {
    private Button back;
    private RadioButton receive,voice,shock;
    private Boolean isRec = false,isVoice = false,isShock = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_my_set_tui);
        findId();
        setlister();
        init();
    }
    private void findId(){
        back = (Button)findViewById(R.id.set_tui_back);
        receive = (RadioButton)findViewById(R.id.switch_receive);
        voice = (RadioButton)findViewById(R.id.switch_voice);
        shock = (RadioButton)findViewById(R.id.switch_shock);

    }
    private void setlister(){
        back.setOnClickListener(new mylister());
        receive.setOnClickListener(new mylister());
        voice.setOnClickListener(new mylister());
        shock.setOnClickListener(new mylister());
    }
    private class mylister implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.set_tui_back:
                    finish();
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                    break;
                case R.id.switch_receive:
                    isRec = !isRec;
                    receive.setChecked(isRec);
                    break;
                case R.id.switch_voice:
                    isVoice = !isVoice;
                    voice.setChecked(isVoice);
                    break;
                case R.id.switch_shock:
                    isShock = !isShock;
                    shock.setChecked(isShock);
                    break;
                default:
                    break;
            }
        }
    }
    private void init(){

    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
