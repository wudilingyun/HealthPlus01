package com.vee.myhealth.ui.animation;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

/**
 * @author lingyun
 *
 */
public class OxygenAnimation extends SurfaceView implements Callback {

	private OxygenThread ot;

	public OxygenAnimation(Context context, AttributeSet attrs) {
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
		ot = new OxygenThread(this, getHolder(), getContext());

		Log.i("lingyun", "ot=" + ot);
		ot.start();

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		if (ot != null && ot.isAlive()) {
			try { // 销毁绘图线程
				ot.setRunning(false);
				ot.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
