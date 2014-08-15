package com.vee.myhealth.ui.animation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.WindowManager;

import com.vee.healthplus.R;

/**
 * @author lingyun
 * 
 */
public class OxygenThread extends Thread {
	private final String TAG = "OxygenThread";
	private boolean isRunning = true;
	private SurfaceHolder mHolder;
	private Context mContext;
	private Bitmap bg;
	private Bitmap tube;
	private Bitmap bubble;
	int x1;
	int y1;
	private ProcessingLabel pl;
	private ArrayList<OxygenBubble> mBubbles;
	private Paint mPaint;

	public Bitmap getBubble() {
		return bubble;
	}

	public int getY() {
		return y1;
	}

	public int getX() {
		return x1;
	}

	public int getTubeWidth() {
		return tube.getWidth();
	}

	public int getTubeHeight() {
		return tube.getHeight();
	}

	public OxygenThread(OxygenAnimation paramOxygenAnimation,
			SurfaceHolder paramSurfaceHolder, Context paramContext) {
		mBubbles = new ArrayList();
		mPaint = new Paint();
		mPaint.setAntiAlias(false);
		mPaint.setDither(true);
		mHolder = paramSurfaceHolder;
		mContext = paramContext;
		Resources localResources = mContext.getResources();
		BitmapFactory.Options localOptions = new BitmapFactory.Options();
		localOptions.inPurgeable = true;
		localOptions.inInputShareable = true;
		Bitmap temp = BitmapFactory.decodeResource(localResources,
				R.drawable.tests_background, localOptions);
		tube = BitmapFactory.decodeResource(localResources,
				R.drawable.oxygen_tube, localOptions);
		bubble = BitmapFactory.decodeResource(localResources,
				R.drawable.oxygen_bubble, localOptions);
		DisplayMetrics metric = new DisplayMetrics();
		WindowManager wm = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(metric);

		float screenWidth = metric.widthPixels;
		float screenHeight = metric.heightPixels;
		Log.i("TAG", "screenWidth=" + screenWidth + ",screenHeight="
				+ screenHeight);
		Matrix matrix = new Matrix();
		float w = screenWidth / temp.getWidth();
		float h = screenHeight / temp.getHeight();
		Log.i("lingyun", "w=" + w + "h=" + h);
		matrix.postScale(w, h);// 获取缩放比例
		bg = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(), temp.getHeight(),
				matrix, true);// 根据缩放比例获取新的位图

		x1 = bg.getWidth() / 2 - tube.getWidth() / 2;
		y1 = bg.getHeight() / 2 - tube.getHeight() / 2 + 35;
		mBubbles.add(new OxygenBubble(this, 50, tube.getWidth() / 2, 4, 60, 60));
		mBubbles.add(new OxygenBubble(this, 50, tube.getWidth() / 3, 3, 90, 90));
		mBubbles.add(new OxygenBubble(this, 50, 0, 7, 30, 30));
		mBubbles.add(new OxygenBubble(this, 50, tube.getWidth() / 4, 5, 30, 30));
		mBubbles.add(new OxygenBubble(this, 50, tube.getWidth() / 5, 6, 30, 30));
		int fullWidth = wm.getDefaultDisplay().getWidth();
		int fullHeight = wm.getDefaultDisplay().getHeight();
		Log.i(TAG, "fullWidth=" + fullWidth + ",fullHeight=" + fullHeight);
		pl = new ProcessingLabel(paramOxygenAnimation.getContext(), fullWidth,
				fullHeight);
	}

	public static void a() {
	}

	public void setRunning(boolean paramBoolean) {
		isRunning = paramBoolean;
	}

	public final void run() {
		Iterator localIterator2;

		while (true) {
			if (!isRunning) {
				if (bg != null)
					bg.recycle();
				if (tube != null)
					tube.recycle();
				if (bubble != null)
					bubble.recycle();
				localIterator2 = mBubbles.iterator();
				while (localIterator2.hasNext()) {
					OxygenBubble localf = (OxygenBubble) localIterator2.next();
					if (localf != null) {
						Bitmap localBitmap = localf.getBubbleImg();
						if (localBitmap != null)
							localBitmap.recycle();
					}
				}
				return;

			}
			localIterator2 = mBubbles.iterator();
			while (true) {
				if (!localIterator2.hasNext())
					break;
				Canvas localCanvas = mHolder.lockCanvas();
				if (localCanvas == null)
					break;
				try {
					localCanvas.drawBitmap(bg, 0.0F, 0.0F, mPaint);
					int x1 = bg.getWidth() / 2 - tube.getWidth() / 2;
					int y1 = bg.getHeight() / 2 - tube.getHeight() / 2 + 80;
					localCanvas.drawBitmap(tube, x1, y1, null);
					Iterator localIterator1 = mBubbles.iterator();
					while (true) {
						if (!localIterator1.hasNext()) {
							pl.a(localCanvas, System.currentTimeMillis());
							mHolder.unlockCanvasAndPost(localCanvas);
							break;
						}
						((OxygenBubble) localIterator1.next()).a(localCanvas);
					}
				} finally {
					// this.j.unlockCanvasAndPost(localCanvas);
				}

			}

		}

	}
}
