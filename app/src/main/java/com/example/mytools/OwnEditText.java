package com.example.mytools;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EditText;

import com.example.wangqiang.jianmi1.R;

/**
 * Created by wangqiang on 2016/11/26.
 */

public class OwnEditText extends EditText  implements TextWatcher {
    private int mLength;
    private Drawable mDrawable,search;
    private Context context;
    private String TAG = "OwnEditText";
    public OwnEditText(Context context) {
        super(context);
        this.context = context;
        Log.e(TAG," in isEditMode"+isInEditMode());

// TODO Auto-generated constructor stub
        init();
    }
    public OwnEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        Log.e(TAG,"  in isEditMode"+isInEditMode());

        // TODO Auto-generated constructor stub
        init();
    }

    public OwnEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        Log.e(TAG,"  in isEditMode"+isInEditMode());

        // TODO Auto-generated constructor stub
        init();
    }



    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTextChanged(CharSequence text, int start,
                              int lengthBefore, int lengthAfter) {
        // TODO Auto-generated method stub
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        mLength = text.length();
        setImage();
    }

    /**
     * 大坑 getCompoundDrawables 会获取不到drawable 导致无法加载drawable
     */
    public void init(){
        search = getCompoundDrawables()[0];//获取左边的图片
        if(search==null){
            Log.e(TAG,"search is null and isEditMode "+isInEditMode());
            search = ContextCompat.getDrawable(context,R.drawable.search_bar);
        }
        search.setBounds(20, 0, search.getMinimumWidth()-20, search.getMinimumHeight()-40);
        if(mDrawable==null){
            if(!isInEditMode()){
                //造成错误的代码段
                Log.e(TAG,"mDrawable is null and isEditMode "+isInEditMode());
                mDrawable = getCompoundDrawables()[2];
                if(mDrawable==null){
                    Log.e(TAG,"get mDrawable  and isEditMode "+isInEditMode());
                    mDrawable = ContextCompat.getDrawable(context, R.drawable.delete);
                }
                mDrawable.setBounds(-10, 0, mDrawable.getMinimumWidth()-35, mDrawable.getMinimumHeight()-25);
            }
        }
        setImage();
    }

    public void setImage(){
        if(mLength>0){
            Log.e(TAG,"show  and isEditMode "+isInEditMode());

            setCompoundDrawablesWithIntrinsicBounds(search, null, mDrawable, null);
        }else{
            Log.e(TAG,"hide  and isEditMode "+isInEditMode());
            setCompoundDrawablesWithIntrinsicBounds(search, null, null, null);
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if(event.getAction()==MotionEvent.ACTION_UP){
            boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight()) && (event.getX() < ((getWidth() - getPaddingRight())));

// boolean touchable = event.getX() > (getPaddingLeft()) && (event.getX() < (getTotalPaddingLeft()));

            if(touchable){
                setText("");
            }
        }
        return super.onTouchEvent(event);
    }

}
