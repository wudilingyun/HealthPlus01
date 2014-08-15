package com.vee.healthplus.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.vee.healthplus.R;
import com.vee.healthplus.util.AppPreferencesUtil;
import com.vee.shop.alipay.utils.MD5;

public class StatisticsUtils {

	// APP id号
	public static String APPID = null;
	public static String channel = null;
	// 已安装数
	public static String MAXINSTALL;
	// 是否打印调试信息
	private static boolean ISDEBUG = true;
	// 存放统计使用的共享参数的文件名
	public static final String ALLSYSTEMSETTING_PREFERENCES = "systemsetting";
	private static SharedPreferences settings;
	// Log标记
	private static final String TAG = StatisticsUtils.class.getSimpleName();
	// 启动统计的地址
	private static final String URL_START = "http://api-st.spacestats.com/startcount.php";
	// 安装统计的地址
	private static final String URL_INSTALL = "http://api-st.spacestats.com/installcount.php";
	// 功能统计的地址
	private static final String URL_FUNCTION = "http://api-st.spacestats.com/functionidcount.php";

	// 发送是否成功
	private static boolean isSuccess = false;

	// 测试统计标签
	public static final String TEST_YJK = "亚健康测试";
	public static final String TEST_YJK_ID = "001";
	public static final String TEST_SM = "睡眠测试";
	public static final String TEST_SM_ID = "002";
	public static final String TEST_XL = "心理测试";
	public static final String TEST_XL_ID = "003";
	public static final String TEST_JF = "减肥测试";
	public static final String TEST_JF_ID = "004";

	// 宝典统计标签
	public static final String NEW_JSJF = "健身减肥";
	public static final String NEW_JSJF_ID = "005";
	public static final String NEW_JBYF = "疾病预防";
	public static final String NEW_JBYF_ID = "006";
	public static final String NEW_YST = "养生堂";
	public static final String NEW_YST_ID = "007";
	public static final String NEW_QSYK = "轻松一刻";
	public static final String NEW_QSYK_ID = "008";
	public static final String NEW_YEBK = "育儿百科";
	public static final String NEW_YEBK_ID = "009";
	public static final String NEW_JJJH = "紧急救护";
	public static final String NEW_JJJH_ID = "010";
	public static final String NEW_LXHT = "两性话题";
	public static final String NEW_LXHT_ID = "011";
	public static final String NEW_ZXDB = "真相大白";
	public static final String NEW_ZXDB_ID = "012";

	// 模块统计标签
	public static final String MODULE_HY = "好友";
	public static final String MODULE_HY_ID = "013";
	public static final String MODULE_BD = "宝典";
	public static final String MODULE_BD_ID = "014";

	// 好友统计标签
	public static final String FRIEND_JKQ = "健康圈";
	public static final String FRIEND_JKQ_ID = "015";

	// 发送的数据
	private static String mobilemodel;
	private static String clientversion;
	private static String uuid;
	private static String vendor;
	private static String imei;
	private static String os;
	private static String osversion;
	private static String mobilenumber;
	private static String operator;
	private static final String key = "K1G0aeC4weNaScY5xdQ8711";

	private static String currentFunc;
	private static long currentFuncStartTimestamp;

	public static void startFunction(String funId) {
		currentFunc = funId;
		currentFuncStartTimestamp = SystemClock.elapsedRealtime();
	}

	public static void endFunction(Context context, String userid,
			String functionid, String function_name) {
		if (functionid != currentFunc) {
			Log.e("lingyun", "endFunction:functionid != currentFunc");
			return;
		}
		String time = (SystemClock.elapsedRealtime() / 1000 - currentFuncStartTimestamp / 1000)
				+ "";
		timeStatistics(context, userid, functionid, function_name, time);
	}

	@SuppressWarnings("static-access")
	public static boolean startCounts(Context context) {
		if (settings == null) {
			settings = context.getSharedPreferences(
					ALLSYSTEMSETTING_PREFERENCES, context.MODE_PRIVATE);
		}
		uuid = settings.getString("uuid", null);
		if (uuid == null) {
			uuid = UUID.randomUUID().toString();
			setUUID(uuid);
		}
		imei = getImei(context);
		APPID = getAPPID(context);
		channel = getChannel(context);
		os = getOS();
		osversion = getOSVersion();
		clientversion = getVerName(context.getApplicationContext());
		mobilemodel = getModel();
		vendor = android.os.Build.MANUFACTURER;
		mobilenumber = getPhoneNumber(context);
		operator = getCarrier(context);
		Log.i(TAG, "operator=" + operator);

		new Thread() {
			@Override
			public void run() {
				ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				params.add(new BasicNameValuePair("uuid", uuid));
				params.add(new BasicNameValuePair("imei", imei));
				params.add(new BasicNameValuePair("appid", APPID));
				params.add(new BasicNameValuePair("channelid", channel));
				params.add(new BasicNameValuePair("os", os));
				params.add(new BasicNameValuePair("osversion", osversion));
				params.add(new BasicNameValuePair("clientversion",
						clientversion));
				params.add(new BasicNameValuePair("mobilemodel", mobilemodel));
				params.add(new BasicNameValuePair("vendor", vendor));
				params.add(new BasicNameValuePair("mobilenumber", mobilenumber));
				params.add(new BasicNameValuePair("key", MD5.getMD5(uuid
						+ APPID + key)));
				Log.i(TAG,
						"==================paramsData=================="
								+ "\n uuid="
								+ uuid
								+ "\n imei="
								+ imei
								+ "\n appid="
								+ APPID
								+ "\n channelid="
								+ channel
								+ "\n os="
								+ os
								+ "\n osversion="
								+ osversion
								+ "\n clientversion="
								+ clientversion
								+ "\n mobilemodel="
								+ mobilemodel
								+ "\n vendor="
								+ vendor
								+ "\n mobilenumber="
								+ mobilenumber
								+ "\n key="
								+ MD5.getMD5(uuid + APPID + key)
								+ "\n==================paramsData==================");

				try {
					if (isFirstRun()) {
						Log.i(TAG, "start count send");
						isSuccess = myHttpPost(URL_START, params);
					} else {
						Log.i(TAG, "install count send");
						isSuccess = myHttpPost(URL_INSTALL, params);
						isSuccess = myHttpPost(URL_START, params);
						if (isSuccess) {
							setInstalled();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					isSuccess = false;
				}
			}
		}.start();
		return isSuccess;
	}

	public static void moduleStatistics(final Context context,
			final String userid, final String functionid,
			final String function_name) {
		new Thread() {
			@Override
			public void run() {
				ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				params.add(new BasicNameValuePair("userid", userid));
				params.add(new BasicNameValuePair("functionid", functionid));
				params.add(new BasicNameValuePair("function_name",
						function_name));
				params.add(new BasicNameValuePair("uuid", uuid));
				params.add(new BasicNameValuePair("imei", imei));
				params.add(new BasicNameValuePair("tsmi", "default"));
				params.add(new BasicNameValuePair("appid", APPID));
				params.add(new BasicNameValuePair("channelid", channel));
				params.add(new BasicNameValuePair("os", os));
				params.add(new BasicNameValuePair("osversion", osversion));
				params.add(new BasicNameValuePair("clientversion",
						clientversion));
				params.add(new BasicNameValuePair("key", MD5.getMD5(userid
						+ key)));
				Log.i(TAG, "==================paramsData=================="
						+ "\n userid="
						+ userid
						+ "\n functionid="
						+ functionid
						+ "\n function_name="
						+ function_name
						+ "\n uuid="
						+ uuid
						+ "\n imei="
						+ imei
						+ "\n appid="
						+ APPID
						+ "\n channelid="
						+ channel
						+ "\n os="
						+ os
						+ "\n osversion="
						+ osversion
						+ "\n clientversion="
						+ clientversion
						+ "\n key="
						+ MD5.getMD5(userid + key)
						+ "\n==================paramsData==================");

				try {
					Log.i(TAG, "send Click module");
					params.add(new BasicNameValuePair("function_time_interval",
							"1"));
					isSuccess = myHttpPost(URL_FUNCTION, params);

				} catch (Exception e) {
					e.printStackTrace();
					isSuccess = false;
				}
			}
		}.start();
	}

	public static void testStatistics(final Context context,
			final String userid, final String functionid,
			final String function_name) {
		new Thread() {
			@Override
			public void run() {
				ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				params.add(new BasicNameValuePair("userid", userid));
				params.add(new BasicNameValuePair("functionid", functionid));
				params.add(new BasicNameValuePair("function_name",
						function_name));
				params.add(new BasicNameValuePair("uuid", uuid));
				params.add(new BasicNameValuePair("imei", imei));
				params.add(new BasicNameValuePair("appid", APPID));
				params.add(new BasicNameValuePair("channelid", channel));
				params.add(new BasicNameValuePair("os", os));
				params.add(new BasicNameValuePair("osversion", osversion));
				params.add(new BasicNameValuePair("clientversion",
						clientversion));
				params.add(new BasicNameValuePair("key", MD5.getMD5(userid
						+ key)));
				Log.i(TAG, "==================paramsData=================="
						+ "\n userid="
						+ userid
						+ "\n functionid="
						+ functionid
						+ "\n function_name="
						+ function_name
						+ "\n uuid="
						+ uuid
						+ "\n imei="
						+ imei
						+ "\n appid="
						+ APPID
						+ "\n channelid="
						+ channel
						+ "\n os="
						+ os
						+ "\n osversion="
						+ osversion
						+ "\n clientversion="
						+ clientversion
						+ "\n key="
						+ MD5.getMD5(userid + key)
						+ "\n==================paramsData==================");

				try {
					Log.i(TAG, "send Click test");
					params.add(new BasicNameValuePair("function_time_interval",
							"0"));
					isSuccess = myHttpPost(URL_FUNCTION, params);

				} catch (Exception e) {
					e.printStackTrace();
					isSuccess = false;
				}
			}
		}.start();
	}

	/**
	 * 
	 * @param context
	 * @param userid
	 * @param functionid
	 * @param function_name
	 * @param time
	 */
	public static void timeStatistics(final Context context,
			final String userid, final String functionid,
			final String function_name, final String time) {
		new Thread() {
			@Override
			public void run() {
				ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				params.add(new BasicNameValuePair("userid", userid));
				params.add(new BasicNameValuePair("functionid", functionid));
				params.add(new BasicNameValuePair("function_name",
						function_name));
				params.add(new BasicNameValuePair("function_time_interval",
						time));
				params.add(new BasicNameValuePair("uuid", uuid));
				params.add(new BasicNameValuePair("imei", imei));
				params.add(new BasicNameValuePair("appid", APPID));
				params.add(new BasicNameValuePair("channelid", channel));
				params.add(new BasicNameValuePair("os", os));
				params.add(new BasicNameValuePair("osversion", osversion));
				params.add(new BasicNameValuePair("clientversion",
						clientversion));
				params.add(new BasicNameValuePair("key", MD5.getMD5(userid
						+ key)));

				Log.i(TAG, "==================paramsData=================="
						+ "\n userid="
						+ userid
						+ "\n functionid="
						+ functionid
						+ "\n function_name="
						+ function_name
						+ "\n function_time_interval="
						+ time
						+ "\n uuid="
						+ uuid
						+ "\n imei="
						+ imei
						+ "\n appid="
						+ APPID
						+ "\n channelid="
						+ channel
						+ "\n os="
						+ os
						+ "\n osversion="
						+ osversion
						+ "\n clientversion="
						+ clientversion
						+ "\n key="
						+ MD5.getMD5(userid + key)
						+ "\n==================paramsData==================");
				try {
					Log.i(TAG, "send statistics time");
					isSuccess = myHttpPost(URL_FUNCTION, params);
				} catch (Exception e) {
					e.printStackTrace();
					isSuccess = false;
				}
			}
		}.start();
	}

	private static boolean isFirstRun() {
		boolean isFirst = settings.getBoolean("isFirstRun", false);
		return isFirst;
	}

	private static boolean isFirstClickModule(Context context) {
		boolean isFirst = AppPreferencesUtil.getBooleanPref(context,
				"isFirstClickModule", true);
		return isFirst;
	}

	private static boolean isFirstClickTest(Context context) {
		boolean isFirst = AppPreferencesUtil.getBooleanPref(context,
				"isFirstClickTest", true);
		return isFirst;
	}

	private static void setClickedModule(Context context) {
		AppPreferencesUtil.setBooleanPref(context, "isFirstClickModule", false);
	}

	private static void setClickedTest(Context context) {
		AppPreferencesUtil.setBooleanPref(context, "isFirstClickTest", false);
	}

	private static void setInstalled() {
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("isFirstRun", true);
		editor.commit();
	}

	private static void setUUID(String uuid) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("uuid", uuid);
		editor.commit();
	}

	/**
	 * HttpClient工具类
	 * 
	 * @param uri
	 * @param data
	 * @return
	 */
	private static boolean myHttpPost(String uri,
			ArrayList<BasicNameValuePair> data) {
		HttpEntity requestHttpEntity;
		try {
			requestHttpEntity = new UrlEncodedFormEntity(data, HTTP.UTF_8);
			HttpPost httpPost = new HttpPost(uri);
			httpPost.setEntity(requestHttpEntity);
			HttpClient httpClient = new DefaultHttpClient();

			HttpResponse httpResponse = httpClient.execute(httpPost);
			if (ISDEBUG) {
				Log.i(TAG, "response Code:"
						+ httpResponse.getStatusLine().getStatusCode());
			}
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				StringBuilder builder = new StringBuilder();
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(httpResponse.getEntity()
								.getContent()));
				for (String s = bufferedReader.readLine(); s != null; s = bufferedReader
						.readLine()) {
					builder.append(s);
				}
				Log.i(TAG, builder.toString());
				return true;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	private static String getVerName(Context context) {
		String verName = "";
		try {
			verName = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {

		}
		return verName;
	}

	/*
	 * public static String getChannel(Context context) { if (channel == null) {
	 * try { PackageManager pm = context.getPackageManager(); ApplicationInfo ai
	 * = pm.getApplicationInfo( context.getPackageName(),
	 * PackageManager.GET_META_DATA); Bundle bundle = ai.metaData; return
	 * bundle.getInt("CHANNEL") + ""; } catch (Exception e) { channel =
	 * "default"; } }
	 * 
	 * return channel; }
	 */

	public static String getChannel(Context context) {
		InputStream is = context.getResources().openRawResource(R.raw.channel);
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		try {
			channel = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (channel.equals("700071")) {
			channel = channel + "-360";
		} else if (channel.equals("700072")) {
			channel = channel + "-htc";
		} else if (channel.equals("700073")) {
			channel = channel + "-米赚";
		} else if (channel.equals("700074")) {
			channel = channel + "-华为";
		} else if (channel.equals("700075")) {
			channel = channel + "-小米";
		} else if (channel.equals("700076")) {
			channel = channel + "-联想";
		} else if (channel.equals("700077")) {
			channel = channel + "-oppo";
		} else if (channel.equals("700078")) {
			channel = channel + "-通用";
		} else if (channel.equals("700079")) {
			channel = channel + "-魅族";
		} else if (channel.equals("700080")) {
			channel = channel + "-安智市场";
		} else if (channel.equals("700081")) {
			channel = channel + "-搜狗市场";
		} else if (channel.equals("700082")) {
			channel = channel + "-美美相机";
		} else if (channel.equals("700083")) {
			channel = channel + "-应用宝";
		} else if (channel.equals("700084")) {
			channel = channel + "-百度助手";
		}else if (channel.equals("700085")) {
			channel = channel + "-淘宝";
		}else if (channel.equals("700086")) {
			channel = channel + "-豌豆荚";
		}else if (channel.equals("700087")) {
			channel = channel + "-金立";
		}else if (channel.equals("700088")) {
			channel = channel + "-应用汇";
		}else if (channel.equals("700089")) {
			channel = channel + "-TV大厅";
		}
		return channel;
	}

	public static String getAPPID(Context context) {
		if (APPID == null) {
			try {
				PackageManager pm = context.getPackageManager();
				ApplicationInfo ai = pm.getApplicationInfo(
						context.getPackageName(), PackageManager.GET_META_DATA);
				Bundle bundle = ai.metaData;
				return bundle.getInt("APP_ID") + "";
			} catch (Exception e) {
				APPID = "default";
			}
		}
		return APPID;
	}

	private static String getImei(Context context) {
		try {
			TelephonyManager telephonyManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			String imei = telephonyManager.getDeviceId();
			if (imei == "")
				return null;
			return imei;
		} catch (Exception e) {
		}
		return null;
	}

	private static String getPhoneNumber(Context context) {
		try {
			TelephonyManager telephonyManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			String phoneNumber = telephonyManager.getLine1Number();
			if (phoneNumber == "")
				return "123456789XY";
			return phoneNumber;
		} catch (Exception e) {
		}
		return null;
	}

	public static String getOS() {
		return "Android";
	}

	public static String getOSVersion() {
		return android.os.Build.VERSION.RELEASE;
	}

	public static String getModel() {
		return android.os.Build.MODEL;
	}

	public static String getBrand() {
		return android.os.Build.BRAND;
	}

	public static String getCarrier(Context context) {
		TelephonyManager manager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return manager.getNetworkOperatorName();

	}

}
