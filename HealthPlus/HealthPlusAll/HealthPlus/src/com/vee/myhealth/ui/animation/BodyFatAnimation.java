package com.vee.myhealth.ui.animation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

/**
 * @author lingyun
 *
 */
public class BodyFatAnimation extends SurfaceView implements Callback {
	
	private BodyFatThread bt;

	public BodyFatAnimation(Context context, AttributeSet attrs) {
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
		bt=new BodyFatThread(this, getHolder(), getContext());
		bt.start();

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		if (bt != null && bt.isAlive()) {
			try { // 销毁绘图线程
				bt.setRunning(false);
				bt.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
