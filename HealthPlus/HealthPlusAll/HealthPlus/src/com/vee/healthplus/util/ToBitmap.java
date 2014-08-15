package com.vee.healthplus.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

public class ToBitmap {

	public ToBitmap() {

	}

	public Bitmap drawableToBitmap(Drawable drawable) {
		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();

		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565;

		Bitmap bitmap = Bitmap.createBitmap(w, h, config);

		// Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		drawable.draw(canvas);

		return left(bitmap);
	}

	public Bitmap left(Bitmap bm) {
		int bmpW = bm.getWidth();
		int bmpH = bm.getHeight();
		Bitmap mBm = null;
		if (bmpW < bmpH) {
			Matrix mt = new Matrix();
			mt.setRotate(-90);
			mBm = Bitmap.createBitmap(bm, 0, 0, bmpW, bmpH, mt, true);
			bm.recycle();
			bm = null;
		} else {
			mBm = bm;
		}
		return mBm;
	}
}
