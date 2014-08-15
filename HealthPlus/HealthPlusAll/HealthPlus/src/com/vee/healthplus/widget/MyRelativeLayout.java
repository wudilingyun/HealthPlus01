package com.vee.healthplus.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class MyRelativeLayout extends RelativeLayout {

    private DispatchTouchEventListener listener = null;

    public MyRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        if (listener == null)
            return super.dispatchTouchEvent(ev);
        if (listener.dispatchTouchEvent(ev)) {
            return true;
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }

    public interface DispatchTouchEventListener {
        public boolean dispatchTouchEvent(MotionEvent ev);
    }

    public void setDispatchTouchEventListener(DispatchTouchEventListener listener) {
        this.listener = listener;
    }
}
