package com.example.mytools;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wangqiang.jianmi1.R;

/**
 * 修改真实姓名的对话框
 * Created by wangqiang on 2016/7/23.
 */
public class ResumeRealNameDialog implements View.OnClickListener {
    private Context context;
    private Button sure,cancel;
    private EditText getName;
    private TextView show;
    private View view;
    private ResumeLister resumeLister;
    private Dialog dialog;
    public ResumeRealNameDialog(Context context){
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.RealNameDialog_sure:
                String name = getName.getText().toString();
                if(name.length()==0){
                    Toast.makeText(context,"请输入有效值",Toast.LENGTH_SHORT).show();
                    return;
                }
                    resumeLister.getName(name);
                    dialog.cancel();
                break;
            case R.id.RealNameDialog_cancel:
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
        void getName(String name);
    }
    public Dialog getNameDialog(String text){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.main_my_resume_realnamedialog,null);
        dialog = new Dialog(context);
        findid();
        show.setText(text);
        sure.setOnClickListener(this);
        cancel.setOnClickListener(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // must be called before set content
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        // 设置宽度为屏宽、靠近屏幕底部。
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(R.color.transparent);
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);
        window.setWindowAnimations(R.style.popWindow_animation);
        return dialog;
    }
    private void findid(){
        getName  = (EditText)view.findViewById(R.id.resume_getRealName_Edit);
        sure = (Button)view.findViewById(R.id.RealNameDialog_sure);
        show = (TextView)view.findViewById(R.id.resume_getRealName_Text);
        cancel = (Button)view.findViewById(R.id.RealNameDialog_cancel);
    }
}
