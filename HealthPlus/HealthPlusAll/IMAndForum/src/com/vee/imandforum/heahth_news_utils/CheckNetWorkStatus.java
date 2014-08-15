package com.vee.imandforum.heahth_news_utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class CheckNetWorkStatus {
	public static boolean Status(Context context) {
		boolean result;
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netinfo = cm.getActiveNetworkInfo();
		if (netinfo != null && netinfo.isConnected()&&netinfo.isAvailable()) {
			result = true;
			Log.i("TAG", "The net was connected");

		} else {
			result = false;
			Log.i("TAG", "The net was bad!");
		}
		return result;
	}
}
