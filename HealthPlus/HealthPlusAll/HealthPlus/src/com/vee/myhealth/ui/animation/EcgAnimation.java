package com.vee.myhealth.ui.animation;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

/**
 * @author lingyun
 * 
 */
public class EcgAnimation extends SurfaceView implements Callback {

	public int[] pointY = { 0,0,1,0,0,1,0,0,1,0 };//模拟数据

	private EcgThread et;

	public EcgAnimation(Context context, AttributeSet attrs) {
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
		et = new EcgThread(this, getHolder(), getContext());
		et.start();

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		if (et != null && et.isAlive()) {
			try { // 销毁绘图线程
				et.setRunning(false);
				et.join();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
