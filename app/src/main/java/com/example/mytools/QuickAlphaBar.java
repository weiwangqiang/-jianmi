package com.example.mytools;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;


/**
 * Created by wangqiang on 2016/6/16.
 */
public class QuickAlphaBar extends ImageButton{

    private String[] letters = new String[] { "#", "A", "B", "C", "D", "E",
            "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X", "Y", "Z" };
    private Paint mPaint = new Paint();
    private int select = 0;
    private int singleHeight;
    private ListView mListView;
    private TextView mAlphsTextView;
    private HashMap<String, Integer> alphaIndexer;
    private Handler mHandler = new Handler();

    public QuickAlphaBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    /* (non-Javadoc)
     * @see android.view.View#onTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int lastSelect = select;
        int index = (int) (event.getY() / singleHeight);
        if (index >= 0 && index < letters.length) {
            String alpha = letters[index];
            if (alphaIndexer.containsKey(alpha)) {
                int position = alphaIndexer.get(alpha);
                if (mListView.getHeaderViewsCount() > 0) {
                    mListView.setSelectionFromTop(position
                            + mListView.getHeaderViewsCount(), 0);
                } else {
                    mListView.setSelectionFromTop(position, 0);
                }
                mAlphsTextView.setText(letters[index]);
            }
        }
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(lastSelect != index) {
                    if(index >= 0 && index < letters.length) {
                        select = index;
                        invalidate();
                    }
                }
                mAlphsTextView.setVisibility(VISIBLE);
                break;
            case MotionEvent.ACTION_MOVE:
                if(lastSelect != index) {
                    if(index >= 0 && index < letters.length) {
                        select = index;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                select = -1;
                mAlphsTextView.setVisibility(INVISIBLE);
                break;
        }
        return super.onTouchEvent(event);
    }

    /* (non-Javadoc)
     * @see android.widget.ImageView#onDraw(android.graphics.Canvas)
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        singleHeight = getHeight() / letters.length;
        for(int i=0;i<letters.length;i++){
            mPaint.setTextSize(50);
            mPaint.setTypeface(Typeface.DEFAULT_BOLD);
            mPaint.setAntiAlias(true);
            mPaint.setColor(Color.GRAY);
            if(i == select) {
                mPaint.setColor(Color.parseColor("#00BFFF"));
                mPaint.setFakeBoldText(true);
            }
            float x = getWidth() / 2 - mPaint.measureText(letters[i]) / 2;
            float y = singleHeight * (i + 1);
            canvas.drawText(letters[i], x, y, mPaint);
            mPaint.reset();
        }
    }

    public void setListView(ListView listView) {
        mListView = listView;
    }

    public void setAlphaIndexer(HashMap<String, Integer> alphaIndexer) {
        this.alphaIndexer = alphaIndexer;
    }

    public void setTextView(TextView textView) {
        this.mAlphsTextView = textView;
    }
}
