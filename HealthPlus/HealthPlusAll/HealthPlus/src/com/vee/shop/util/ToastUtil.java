/**
 * 
 */
package com.vee.shop.util;

import android.content.Context;
import android.widget.Toast;

/**
 * @author Felix
 * 
 */
public class ToastUtil {
	private static Toast lastToast = null;
	public static int errorCount;

	public static void clear() {
		if (lastToast != null)
			lastToast.cancel();
	}

	public static void show(Context context, int textId) {
		show(context, context.getString(textId), 0);
	}

	public static void show(Context context, int textId, int duration) {
		show(context, context.getString(textId), duration);
	}

	public static void show(Context context, CharSequence text) {
		show(context, text, 0);
	}

	public static void show(Context context, CharSequence text, int duration) {
		if (lastToast != null)
			lastToast.cancel();
		Toast localToast = Toast.makeText(context, text, duration);
		lastToast = localToast;
		localToast.show();
	}

	public static int getErrorCount() {
		return errorCount;
	}

	public static void setErrorCount(int errorCount) {
		ToastUtil.errorCount = errorCount;
	}

}
