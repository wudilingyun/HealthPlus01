package com.vee.healthplus.util.sporttrack.weather;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class DisplayImageOptionsUtils {

	private static DisplayImageOptionsUtils instance;
	private static DisplayImageOptions options;
	private static DisplayImageOptions customOptions;
	private static int RES = -1;

	public static DisplayImageOptionsUtils getInstance() {
		if (instance == null) {
			instance = new DisplayImageOptionsUtils();
		}
		return instance;
	}

	public DisplayImageOptions getOptions() {
		if (options == null) {
			options = new DisplayImageOptions.Builder()
					
					.cacheInMemory(true).cacheOnDisc(true)
					.bitmapConfig(Bitmap.Config.ARGB_8888).build();
			//.showImageForEmptyUri(R.drawable.ic_launcher)
			//.showImageOnFail(R.drawable.ic_launcher)
		}
		return options;
	}

	public DisplayImageOptions getOptions(int res) {
		if (customOptions == null && RES != res) {
			customOptions = new DisplayImageOptions.Builder()
					.showImageForEmptyUri(res).showImageOnFail(res)
					.cacheInMemory(true).cacheOnDisc(true)
					.bitmapConfig(Bitmap.Config.ARGB_8888).build();
		}
		return customOptions;
	}

	public static Drawable BitmapToDrawable(Bitmap bitmap, Context context) {
		if (bitmap == null) {
			return null;
		}
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getApplicationContext().getResources().getDisplayMetrics();
		
		BitmapDrawable drawable = new BitmapDrawable(bitmap);
		drawable.setTargetDensity(dm.densityDpi);
	
		return drawable;
	}
}
