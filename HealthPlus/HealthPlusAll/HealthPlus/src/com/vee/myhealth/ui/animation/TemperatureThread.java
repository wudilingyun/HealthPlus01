package com.vee.myhealth.ui.animation;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
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
public class TemperatureThread extends Thread {

	private final int textSize;
	private final int startHeight;
	private final int endHeight;
	private boolean isRunning = true;
	private SurfaceHolder mHolder;
	private Context mContext;
	private int currentHeight;
	private int increasement;
	private int[] pixels;
	private String Cunit;
	private String Funit;
	private Bitmap empty;
	private Bitmap full;
	private Bitmap bg;
	private Paint imagePaint;
	private Paint textPaint;
	private ProcessingLabel pl;

	public TemperatureThread(TemperatureAnimation paramTemperatureAnimation,
			SurfaceHolder paramSurfaceHolder, Context paramContext) {
		textSize = 24;
		imagePaint = new Paint();
		imagePaint.setAntiAlias(false);
		imagePaint.setDither(true);
		textPaint = new Paint();
		textPaint.setColor(Color.BLACK);
		textPaint.setTextAlign(Paint.Align.CENTER);
		textPaint.setTextSize(textSize);
		mHolder = paramSurfaceHolder;
		mContext = paramContext;
		Resources localResources = mContext.getResources();
		BitmapFactory.Options localOptions = new BitmapFactory.Options();
		localOptions.inPurgeable = true;
		localOptions.inInputShareable = true;
		empty = BitmapFactory.decodeResource(localResources,
				R.drawable.temperature_empty, localOptions);
		full = BitmapFactory.decodeResource(localResources,
				R.drawable.temperature_full, localOptions);
		Bitmap temp = BitmapFactory.decodeResource(localResources,
				R.drawable.tests_background, localOptions);
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
		pixels = new int[empty.getWidth() * empty.getHeight()];
		empty.getPixels(pixels, 0, empty.getWidth(), 0, 0, empty.getWidth(),
				empty.getHeight());
		Cunit = "℃";
		Funit = "℉";

		startHeight = full.getHeight()/4;
		endHeight = full.getHeight()*3/4;
		currentHeight = startHeight;
		increasement = 2;
		int fullWidth = wm.getDefaultDisplay().getWidth();
		int fullHeight = wm.getDefaultDisplay().getHeight();
		pl = new ProcessingLabel(paramTemperatureAnimation.getContext(),
				fullWidth, fullHeight);
	}
	
	public void setRunning(boolean flag){
		isRunning=flag;
	}


	public final void run() {
		while (true) {
			if (!isRunning) {
				if (full != null)
					full.recycle();
				if (empty != null)
					empty.recycle();
				if (bg != null)
					bg.recycle();
				return;
			}
			Canvas localCanvas = mHolder.lockCanvas();
			if (localCanvas == null)
				continue;
			try {
				localCanvas.drawBitmap(bg, 0.0F, 0.0F, imagePaint);
				int i1 = bg.getWidth() / 2 - full.getWidth() / 2;
				int i2 = bg.getHeight() / 2 - full.getHeight() / 2 + 80;
				localCanvas.drawBitmap(full, i1, i2, null);
				currentHeight += increasement;
				localCanvas.drawBitmap(pixels, 0, empty.getWidth(), i1, i2,
						empty.getWidth(), currentHeight, true, new Paint());
				int i3 = 20 * empty.getWidth() / 100;
				int i4 = 20 * empty.getHeight() / 100;
				localCanvas.drawText(Funit, i1 + empty.getWidth() - i3,
						i2 + i4, textPaint);
				localCanvas.drawText(Cunit, i3 + i1, i4 + i2, textPaint);
				if (currentHeight >= endHeight)
					increasement = -4;
				if (currentHeight <= startHeight)
					increasement = 4;
				pl.a(localCanvas, System.currentTimeMillis());
				mHolder.unlockCanvasAndPost(localCanvas);
			} catch (Exception e) {
				Log.i("lingyun", "Exception................");
			} finally {
				// mHolder.unlockCanvasAndPost(localCanvas);
			}
		}
	}
}
