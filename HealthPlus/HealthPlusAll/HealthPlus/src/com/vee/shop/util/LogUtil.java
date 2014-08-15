/**
 * 
 */
package com.vee.shop.util;

import android.util.Log;

public class LogUtil {
	private static final String APP_TAG = "17VEEShop";
	private static final String TAG_FORMAT = "%s-%s";

	public static void d(String tag, String content) {
		// if (MyApplication.isUserDebug())
		Log.d(String.format(TAG_FORMAT, new Object[] { APP_TAG, tag }), content);
	}

	public static void e(String tag, String content) {
		// if (MyApplication.isUserDebug())
		Log.e(String.format(TAG_FORMAT, new Object[] { APP_TAG, tag }), content);
	}

	public static void w(String tag, String content) {
		// if (MyApplication.isUserDebug())
		Log.w(String.format(TAG_FORMAT, new Object[] { APP_TAG, tag }), content);
	}
}
