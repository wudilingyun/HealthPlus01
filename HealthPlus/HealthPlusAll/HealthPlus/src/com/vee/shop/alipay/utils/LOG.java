package com.vee.shop.alipay.utils;

import android.util.Log;

/**
 * 本地LOG的输出
 * 
 * @author zhou
 * 
 */
public class LOG {

	private static int LEVEL = 1;// 可以通过修订LEVEL的值，修改本地LOG输出的级别；

	/**
	 * Priority constant for the DBGln method; use Log.v.
	 */
	public static void v(boolean DBG, String TAG, String MSG) {
		if (DBG && LEVEL >= Log.VERBOSE) {
			Log.v(TAG, MSG);
		}
	}

	/**
	 * Priority constant for the DBGln method; use Log.d.
	 */
	public static void d(boolean DBG, String TAG, String MSG) {
		if (DBG && LEVEL >= Log.DEBUG) {
			Log.d(TAG, MSG);
		}
	}

	/**
	 * Priority constant for the DBGln method; use Log.i.
	 */
	public static void i(boolean DBG, String TAG, String MSG) {
		if (DBG && LEVEL >= Log.INFO) {
			Log.i(TAG, MSG);
		}
	}

	/**
	 * Priority constant for the DBGln method; use Log.w.
	 */
	public static void w(boolean DBG, String TAG, String MSG) {
		if (DBG && LEVEL >= Log.WARN) {
			Log.w(TAG, MSG);
		}
	}

	/**
	 * Priority constant for the DBGln method; use Log.e.
	 */
	public static void e(boolean DBG, String TAG, String MSG) {
		if (DBG && LEVEL >= Log.ERROR) {
			Log.e(TAG, MSG);
		}
	}

}
