package com.vee.healthplus.ui.user;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

public class ResizeLayout extends RelativeLayout {

	public ResizeLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	private static int count = 0;

	public ResizeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (handler != null && oldh != 0) {
			Bundle data = new Bundle();
			data.putInt("height", h);
			Message msg=handler.obtainMessage();
			msg.setData(data);
			handler.sendMessage(msg);
		}

	}

	Handler handler;

	public void setHandler(Handler h) {
		handler = h;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

}
