package com.vee.healthplus.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.vee.healthplus.heahth_news_utils.CheckNetWorkStatus;
import com.vee.healthplus.http.HttpClient;
import com.vee.healthplus.http.HttpException;
import com.vee.healthplus.http.Response;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class InstallSataUtil {

	public Context mContext;
	public String appId;

	public static final String INSTALLCOUNT = "http://shujutongji.mobifox.cn/userstatnew/installcount.php";
	public static final String STARTCOUNT = "http://shujutongji.mobifox.cn/userstatnew/startcount.php";
	public static final String STATPATH = "http://shujutongji.mobifox.cn/userstatnew/starttimekeycount.php";
	public static final String COMMENTS = "http://shujutongji.mobifox.cn/userstatnew/comments.php";

	public InstallSataUtil(Context mContext) {
		this.mContext = mContext;
	}

	public ArrayList<BasicNameValuePair> getCommonParams(Context context) {
		final String phoneModel = android.os.Build.MODEL;
		final String imei = "";
		final String vesion = getAppVersionName(context.getApplicationContext());
		final String mobilenumber = "";
		final String tmsi = "";

		String uuidTemp = null;
		uuidTemp = UUID.randomUUID().toString();

		final String uuid = uuidTemp;
		final String vendor = android.os.Build.MANUFACTURER;

		ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("uuid", uuid));
		params.add(new BasicNameValuePair("imei", imei));
		params.add(new BasicNameValuePair("tmsi", tmsi));
		params.add(new BasicNameValuePair("gameid", appId));
		params.add(new BasicNameValuePair("clientversion", vesion));
		params.add(new BasicNameValuePair("mobilemodel", phoneModel));
		params.add(new BasicNameValuePair("mobilenumber", mobilenumber));
		params.add(new BasicNameValuePair("vendor", vendor));

		return params;
	}

	public String getAppVersionName(Context context) {
		String versionName = "";
		try {
			// ---get the package info---
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
			if (versionName == null || versionName.length() <= 0) {
				return "";
			}
		} catch (Exception e) {
		}
		return versionName;

	}

	public void serverStat(String id) {
		// TODO Auto-generated method stub
		appId = id;
		httpsend.start();
	}

	private Thread httpsend = new Thread() {

		@Override
		public void run() {
			HttpClient hc = new HttpClient();
			ArrayList<BasicNameValuePair> params = getCommonParams(mContext);

			Response Response;
			try {
				if (isFirstRun()) {
					Response = hc.post(INSTALLCOUNT, params);
					setInstalled();
				} else {
					Response = hc.post(STARTCOUNT, params);
				}
			} catch (HttpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	};

	private void setInstalled() {
		AppPreferencesUtil.setBooleanPref(mContext, "isFirstInstall", false);
	}

	public boolean isFirstRun() {
		return AppPreferencesUtil.getBooleanPref(mContext, "isFirstInstall",
				true);
	}

	public void ReportComments(String msg, String id) {
		appId = id;
		final String comments = msg;
		Thread httpsendComment = new Thread() {
			@Override
			public void run() {
				HttpClient hc = new HttpClient();
				ArrayList<BasicNameValuePair> params = getCommonParams(mContext);

				params.add(new BasicNameValuePair("comments", comments));

				try {
					Response rs = hc.post(COMMENTS, params);
				} catch (HttpException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

					return;
				}
			}
		};

		httpsendComment.start();
	}

	/*public void stopTimerRecord() {
		Intent intent = new Intent(mContext, AlermReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(mContext, 0, intent,
				0);
		AlarmManager alarm = (AlarmManager) mContext
				.getSystemService(Context.ALARM_SERVICE);
		alarm.cancel(sender);
	}

	public void startTimerRecord() {

		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, c.getTime().getYear());
		c.set(Calendar.MONTH, c.getTime().getMonth());// Ҳ���������֣�0-11,һ��Ϊ0
		c.set(Calendar.DAY_OF_MONTH, c.getTime().getDay());
		c.set(Calendar.HOUR_OF_DAY, c.getTime().getHours());
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		Intent intent = new Intent(mContext, AlermReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(mContext, 0, intent, 0);
		AlarmManager am = (AlarmManager) mContext
				.getSystemService(Context.ALARM_SERVICE);
		am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),
				60 * 60 * 1000, pi);
	}*/

	public void timerServerStat(String id) {
		// TODO Auto-generated method stub
		appId = id;
		Thread httpSendTimer = new Thread() {

			@Override
			public void run() {
				HttpClient hc = new HttpClient();
				ArrayList<BasicNameValuePair> params = getCommonParams(mContext);

				Calendar c = Calendar.getInstance();
				int hour = c.getTime().getHours();

				params.add(new BasicNameValuePair("timekey", Integer
						.toString(hour)));

				Response mResponse = null;
				try {

					mResponse = hc.post(STATPATH, params);

				} catch (HttpException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {

				}

			}
		};

		httpSendTimer.start();
	}

}
