package com.example.mytools;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.wangqiang.jianmi1.R;

/**
 * Created by wangqiang on 2016/7/23.
 */
public class ResumeEditDialog  implements View.OnClickListener{
    private Context context;
    private Button button1,button2,cancel;
    private View view;
    private ResumeLister resumeLister;
    private Dialog dialog;
    private String text1,text2;
    public ResumeEditDialog(Context context,String text1,String text2){
        this.context = context;
        this.text1 = text1;
        this.text2 = text2;
    }

    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.ResumeDialog_item1:
                    resumeLister.Item1();
                    dialog.cancel();
                    break;
                case R.id.ResumeDialog_item2:
                    resumeLister.Item2();
                    dialog.cancel();
                    break;
                case R.id.ResumeDialog_cancel:
                    dialog.cancel();
                    break;
                default:
                    break;
            }
    }
    public void setlister(ResumeLister resumeLister){
        this.resumeLister = resumeLister;
    }
    public interface ResumeLister{
        void Item1();
        void Item2();
    }
    public Dialog getEditDialog(){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.main_my_resume_dialog,null);
        dialog = new Dialog(context);
        findid();
        button1.setText(text1);
        button2.setText(text2);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        cancel.setOnClickListener(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // must be called before set content
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        // 设置宽度为屏宽、靠近屏幕底部。
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(R.color.transparent);
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);
        window.setWindowAnimations(R.style.popWindow_animation);

        return dialog;
    }
    private void findid(){
        button1 = (Button)view.findViewById(R.id.ResumeDialog_item1);
        button2 = (Button)view.findViewById(R.id.ResumeDialog_item2);
        cancel = (Button)view.findViewById(R.id.ResumeDialog_cancel);
    }
}
