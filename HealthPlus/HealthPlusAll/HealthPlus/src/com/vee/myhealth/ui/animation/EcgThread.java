package com.vee.myhealth.ui.animation;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.WindowManager;

import com.vee.healthplus.R;

/**
 * @author lingyun
 * 
 */
public class EcgThread extends Thread {

	private final int textSize;
	private boolean isRunning = true;

	private int position;
	private SurfaceHolder mHolder;
	private Context mContext;
	private int startY = 200;
	private int startX = 0;
	private int increasement = 5;
	private ArrayList<Point> plist = new ArrayList<Point>();
	private int[] pys;
	private Bitmap bg;
	private Paint mPaint;
	private Paint imagePaint;
	private ProcessingLabel pl;
	int fullWidth;
	int fullHeight;

	EcgAnimation ea;

	public EcgThread(EcgAnimation paramEcgAnimation,
			SurfaceHolder paramSurfaceHolder, Context paramContext) {
		textSize = 24;
		imagePaint = new Paint();
		imagePaint.setAntiAlias(false);
		imagePaint.setDither(true);
		mPaint = new Paint();
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(5);
		mPaint.setColor(Color.WHITE);
		mHolder = paramSurfaceHolder;
		mContext = paramContext;
		ea = paramEcgAnimation;
		Resources localResources = mContext.getResources();
		BitmapFactory.Options localOptions = new BitmapFactory.Options();
		localOptions.inPurgeable = true;
		localOptions.inInputShareable = true;
		Bitmap temp = BitmapFactory.decodeResource(localResources,
				R.drawable.ecg_background, localOptions);
		DisplayMetrics metric = new DisplayMetrics();
		WindowManager wm = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(metric);
		float screenWidth = metric.widthPixels;
		float screenHeight = metric.heightPixels;
		Log.i("lingyun", "screenWidth=" + screenWidth + "screenHeight="
				+ screenHeight);
		Matrix matrix = new Matrix();
		float w = screenWidth / temp.getWidth();
		float h = screenHeight / temp.getHeight();
		Log.i("lingyun", "w=" + w + "h=" + h);
		matrix.postScale(w, h);// 获取缩放比例
		bg = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(), temp.getHeight(),
				matrix, true);// 根据缩放比例获取新的位图

		fullWidth = wm.getDefaultDisplay().getWidth();
		fullHeight = wm.getDefaultDisplay().getHeight();

		pl = new ProcessingLabel(paramEcgAnimation.getContext(), fullWidth,
				fullHeight);
		pys = paramEcgAnimation.pointY;
	}

	public void setRunning(boolean flag) {
		isRunning = flag;
	}

	int pSize = 0;
	int count = 0;

	public final void run() {
		while (true) {
			if (!isRunning) {
				if (bg != null)
					bg.recycle();
				return;
			}
			try {
				Canvas localCanvas = mHolder.lockCanvas();
				if (localCanvas == null)
					continue;
				localCanvas.drawBitmap(bg, 0.0F, 0.0F, imagePaint);
				
				//加载模拟数据
				int py = fullHeight / 2;
				if (count > pys.length - 1)
					count = 0;
				if(count==pys.length - 1){
					pys[count]=new Random().nextInt(100)+50;
				}
				Point p1 = new Point(0, py - pys[count]);
				Point p2 = new Point(6, py + pys[count]);
				
				pSize = plist.size();
				if (pSize == 0) {
					plist.add(p1);
					plist.add(p2);
				} else if (plist.get(pSize - 1).x > fullWidth) {
					// Log.i("prepareLine", "plist==" + plist.get(0).x);
					plist.remove(0);
					for (int i = 0; i < plist.size() - 1; i++) {
						if (i == 0)
							plist.get(i).x += 25;
						else
							plist.get(i).x += 20;
					}
					plist.add(p1);
					plist.add(p2);
					Log.i("lingyun", "plist.size=" + plist.size());
				} else {
					for (int i = 0; i < plist.size() - 1; i++) {
						plist.get(i).x += 20;
					}

					plist.add(p1);
					plist.add(p2);
				}
				
				//绘制曲线
				if (plist.size() >= 2) {
					for (int i = 0; i < plist.size() - 1; i++) {
						localCanvas.drawLine(plist.get(i).x, plist.get(i).y,
								plist.get(i + 1).x, plist.get(i + 1).y, mPaint);
					}
				}
				pl.a(localCanvas, System.currentTimeMillis());
				mHolder.unlockCanvasAndPost(localCanvas);
				sleep(50);

			} catch (Exception e) {
				Log.i("lingyun", "Exception................");
			} finally {

			}
			count++;
		}
	}
}
