package com.vee.myhealth.ui.animation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class TemperatureAnimation extends SurfaceView implements Callback {
	
	private TemperatureThread tt;

	public TemperatureAnimation(Context context, AttributeSet attrs) {
		super(context, attrs);
		getHolder().addCallback(this);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		tt=new TemperatureThread(this, getHolder(), getContext());
		tt.start();

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		if (tt != null && tt.isAlive()) {
			try { // 销毁绘图线程
				tt.setRunning(false);
				tt.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
