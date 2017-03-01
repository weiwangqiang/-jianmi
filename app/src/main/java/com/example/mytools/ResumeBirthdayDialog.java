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

import com.example.control.UserControl;
import com.example.wangqiang.jianmi1.R;

/**
 * 修改生日的对话框
 * Created by wangqiang on 2016/7/23.
 */
public class ResumeBirthdayDialog implements View.OnClickListener {
    private Boolean ischang = false;
    private int Myyear ,Mymonth,Myday;
    private Context context;
    private Button cancel;
    private DatePicker datePicker;
    private View view;
    private ResumeLister resumeLister;
    private Dialog dialog;
    public ResumeBirthdayDialog(Context context){
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_my_EditDatePicker:
                break;
            case R.id.BirthdayDialog_cancel:
                resumeLister.getTime(Myyear,Mymonth,Myday);
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
        void getTime(int year, int monthOfYear, int dayOfMonth);
    }
    public Dialog getEditDialog(){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.main_my_resume_birthdaydialog,null);
        dialog = new Dialog(context);
        findid();
        String time =  UserControl.getUserControl().getUser().getBirthday();
        String[] birth = time.split("-");
        if(birth.length==3){
                Myyear = Integer.parseInt(birth[0]);
                Mymonth = Integer.parseInt(birth[1])-1;
                Myday = Integer.parseInt(birth[2]);
        }
        else {
            Myyear = 1995;
            Mymonth = 0;
            Myday = 1;
        }
        datePicker.init(Myyear, Mymonth,Myday, new OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                ischang = true;
               Myyear = year;
                Mymonth = monthOfYear;
                Myday = dayOfMonth;
            }
        });
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
        datePicker  = (DatePicker)view.findViewById(R.id.main_my_EditDatePicker);
        cancel = (Button)view.findViewById(R.id.BirthdayDialog_cancel);
    }
}
