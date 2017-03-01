package com.example.mytools;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.example.wangqiang.jianmi1.R;

/**
 * Created by wangqiang on 2016/6/16.
 */
public class myEditText extends EditText
        implements View.OnFocusChangeListener,TextWatcher
{
    /**
     * 删除按钮的引用,Drawable是一个可以画的对象
     */
    private Drawable mClearDrawable,search;
    private Context context;
    private String TAG = "myEditText";

    public myEditText(Context context) {
        super(context);
        this.context = context;
        Log.e(TAG," isInEditMode() is " + isInEditMode());

        if(isInEditMode()){
           return;
        }
        init();
    }

    public myEditText(Context context, AttributeSet attrs) {
        //这里构造方法也很重要，不加这个很多属性不能再XML里面定义
        this(context, attrs, android.R.attr.editTextStyle);
        this.context = context;
        Log.e(TAG," isInEditMode() is " + isInEditMode());

        if(isInEditMode()){
            return;
        }
        init();
    }

    public myEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        Log.e(TAG," isInEditMode() is " + isInEditMode());
        if(isInEditMode()){
            return;
        }
        init();
    }
    private void init() {
        /**
         * textView.setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom)
         * 这个看你设定的是在哪个方向了，就把drawable放在哪个对应的位置。
         */
        //获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
        //设置输入类型
        setInputType(InputType.TYPE_TEXT_VARIATION_NORMAL);
//        setShadowLayer(1,10,10,R.color.base_color);
        if(!isInEditMode()){
            if(mClearDrawable==null){
                mClearDrawable = getCompoundDrawables()[2];
            }
            if(search==null){
                search = getCompoundDrawables()[0];//获取左边的图片
            }
        }

        if (mClearDrawable == null) {
            //使用默认图片
            mClearDrawable = ContextCompat.getDrawable(context, R.drawable.delete);
        }
        if(null == search){
            search = ContextCompat.getDrawable(context,R.drawable.search_bar);
        }
        search.setBounds(10, 0, search.getMinimumWidth()-10, search.getMinimumHeight()-20);
        mClearDrawable.setBounds(-10, 0, mClearDrawable.getMinimumWidth()-45,
                mClearDrawable.getMinimumHeight()-35);
        setClearIconVisible(false);
        setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }


    /**
     * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件
     * 当我们按下的位置 在  EditText的宽度 - 图标到控件右边的间距 - 图标的宽度  和
     * EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向没有考虑
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getCompoundDrawables()[2] != null) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                boolean touchable = event.getX() > (getWidth()
                        - getPaddingRight() - mClearDrawable.getIntrinsicWidth())
                        && (event.getX() < ((getWidth() - getPaddingRight())));
                if (touchable) {
                    this.setText("");
                }
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 当ClearEditText焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
    }


    /**
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     * @param visible
     */
    protected void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(search,
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }
    /**
     * 当输入框里面内容发生变化的时候回调的方法
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int count,
                              int after) {
        setClearIconVisible(s.length() > 0);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
