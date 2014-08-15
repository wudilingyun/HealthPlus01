package com.vee.shop.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.vee.shop.util.Constants;

public class BaseActivity extends FragmentActivity {
	private static final String TAG = "BaseActivity";
	private BroadcastReceiver netStateReceiver;
	private int errorCount;
	public Context mContext;
	public static SharedPreferences settings;
	public static SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		errorCount = 0;
		this.mContext = BaseActivity.this;
		if (settings == null) {
			settings = mContext.getSharedPreferences(
					Constants.ALLSYSTEMSETTING_PREFERENCES,
					Context.MODE_PRIVATE);
		}
		editor = settings.edit();
		netStateReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				/*
				 * State wifiState = null; State mobileState = null;
				 * ConnectivityManager cm = (ConnectivityManager) context
				 * .getSystemService(Context.CONNECTIVITY_SERVICE); wifiState =
				 * cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI) .getState();
				 * mobileState = cm
				 * .getNetworkInfo(ConnectivityManager.TYPE_MOBILE) .getState();
				 * if (wifiState != null && mobileState != null &&
				 * State.CONNECTED != wifiState && State.CONNECTED ==
				 * mobileState) { // TextUtil.showToast(context,
				 * "mobile network avaiable"); } else if (wifiState != null &&
				 * mobileState != null && State.CONNECTED != wifiState &&
				 * State.CONNECTED != mobileState) {
				 * MyApplication.setCartNum(0); if (ToastUtil.getErrorCount() ==
				 * 0) { ToastUtil.show(context, ApplicationUtils.getResId(
				 * "string", "shop_net_error_message", getPackageName()),
				 * Toast.LENGTH_SHORT);
				 * ToastUtil.setErrorCount(ToastUtil.getErrorCount() + 1); } }
				 * else if (wifiState != null && State.CONNECTED == wifiState) {
				 * // TextUtil.showToast(context, "wifi network avaiable"); if
				 * (ToastUtil.getErrorCount() > 0) {
				 * ToastUtil.setErrorCount(ToastUtil.getErrorCount() - 1); }
				 * 
				 * }
				 */
			}
		};
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
		this.registerReceiver(netStateReceiver, filter);
	}

	@Override
	protected void onStop() {
		super.onStop();
		this.unregisterReceiver(netStateReceiver);
	}

	// public MyApplication getMyApplication() {
	// return (MyApplication) super.getApplicationContext();
	// }

	// public void signOut() {
	// getMyApplication().getConnectionRepository().removeConnections(
	// getMyApplication().getConnectionFactory().getProviderId());
	// // startActivity(new Intent(this, MainActivity.class));
	// startActivity(new Intent(this, LoginActivity.class));
	// finish();
	// }
}
