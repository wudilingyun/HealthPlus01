package com.vee.healthplus.heahth_news_utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class MyScorllView extends ScrollView{
	
	
	 public MyScorllView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	private float xDistance, yDistance, xLast, yLast;  
	  
	    public MyScorllView(Context context, AttributeSet attrs) {  
	        super(context, attrs);  
	    }  
	  
	    @Override  
	    public boolean onInterceptTouchEvent(MotionEvent ev) {  
	        switch (ev.getAction()) {  
	            case MotionEvent.ACTION_DOWN:  
	                xDistance = yDistance = 0f;  
	                xLast = ev.getX();  
	                yLast = ev.getY();  
	                break;  
	            case MotionEvent.ACTION_MOVE:  
	                final float curX = ev.getX();  
	                final float curY = ev.getY();  
	                  
	                xDistance += Math.abs(curX - xLast);  
	                yDistance += Math.abs(curY - yLast);  
	                xLast = curX;  
	                yLast = curY;  
	                  
	                if(xDistance > yDistance){  
	                    return false;  
	                }
	                break;
	            case MotionEvent.ACTION_UP:
	            	return false;
	        }  
	  
	        return super.onInterceptTouchEvent(ev);  
	    }  
}
