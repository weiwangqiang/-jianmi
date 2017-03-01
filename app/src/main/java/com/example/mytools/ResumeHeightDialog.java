package com.example.mytools;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.NumberPicker;

import com.example.control.UserControl;
import com.example.object.User;
import com.example.wangqiang.jianmi1.R;

/**
 * 修改生日的对话框
 * Created by wangqiang on 2016/7/23.
 */
public class ResumeHeightDialog implements View.OnClickListener {
    private Context context;
    private Button sure;
    private NumberPicker picker;
    private View view;
    private ResumeLister resumeLister;
    private Dialog dialog;
    private int height ;
    private Boolean ischange = false;
    private String[] content = new String[151];
    public ResumeHeightDialog(Context context){
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.HeightDialog_sure:
                if(!ischange){
                    height = 150;
                }
                resumeLister.getHeight(ischange,height);
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
      void getHeight(Boolean isChange,int height);
    }
    public Dialog getEditDialog(){
        for(int i = 150;i>=0;i--){
            content[i] = (250-i)+"cm";
        }
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.main_my_resume_heightdialog,null);
        dialog = new Dialog(context);
        findid();
        picker.setDisplayedValues(content);
        picker.setMinValue(0);
        picker.setMaxValue(content.length-1);
        User user =  UserControl.getUserControl().getUser();
        if(user.getHeight()!=0){
            picker.setValue((250-user.getHeight()));
        }
        else
        {
            picker.setValue(100);
        }
        picker.setWrapSelectorWheel(false);
        picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                ischange = true;
                height = 250-newVal;
            }
        });
        sure.setOnClickListener(this);
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
        picker  = (NumberPicker)view.findViewById(R.id.main_my_HeightPicker);
        sure = (Button)view.findViewById(R.id.HeightDialog_sure);
    }
}
