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
public class BodyFatThread extends Thread {

	private final int textSize;
	private boolean isRunning = true;

	private int position;
	private SurfaceHolder mHolder;
	private Context mContext;
	private int increasement = -4;
	private Bitmap ruler;
	private Bitmap pointer;
	private Bitmap bg;
	private Paint imagePaint;
	private Paint textPaint;
	private ProcessingLabel pl;

	public BodyFatThread(BodyFatAnimation paramBodyFatAnimation,
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
		ruler = BitmapFactory.decodeResource(localResources,
				R.drawable.bodyfat_ruler, localOptions);
		pointer = BitmapFactory.decodeResource(localResources,
				R.drawable.bodyfat_pointer, localOptions);
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

		int fullWidth = wm.getDefaultDisplay().getWidth();
		int fullHeight = wm.getDefaultDisplay().getHeight();
		pl = new ProcessingLabel(paramBodyFatAnimation.getContext(), fullWidth,
				fullHeight);
	}
	
	public void setRunning(boolean flag){
		isRunning=flag;
	}

	public final void run() {
		while (true) {
			if (!isRunning) {
				if (ruler != null)
					ruler.recycle();
				if (pointer != null)
					pointer.recycle();
				if (bg != null)
					bg.recycle();
				return;
			}
			Canvas localCanvas = mHolder.lockCanvas();
			if (localCanvas == null)
				continue;
			try {
				localCanvas.drawBitmap(bg, 0.0F, 0.0F, imagePaint);
				int i1 = position;
				int i2 = bg.getHeight() / 2 - ruler.getHeight() / 2 + 80;
				localCanvas.drawBitmap(ruler, i1, i2, null);
				int i3 = bg.getWidth() / 2 - pointer.getWidth() / 2;
				int i4 = bg.getHeight() / 2 + 80;
				localCanvas.drawBitmap(pointer, i3, i4, null);
				position += increasement;
				if (Math.abs(position) >= ruler.getWidth() - bg.getWidth()
						+ pointer.getHeight())
					increasement = 4;
				if (Math.abs(position) <= 0)
					increasement = -4;
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
