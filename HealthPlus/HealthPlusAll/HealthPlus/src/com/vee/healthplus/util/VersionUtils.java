package com.vee.healthplus.util;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.vee.healthplus.R;
import com.vee.healthplus.common.Common;
import com.vee.healthplus.http.Response;
import com.vee.healthplus.http.ResponseException;
import com.vee.healthplus.ui.setting.UpdateActivity;
import com.vee.healthplus.widget.CustomDialog;

public class VersionUtils {

	private LoadingDialogUtil ldu;
	public static JSONArray jsa = null;
	public static String mServer = null;
	private Context context;
	private JSONArray mVersionJSONArray = null;
	private boolean mHaveNewVersion = false;
	private String mMessage = "";
	private static VersionUtils instance;
	private static final int UPDATE_YES_IN = 0;
	private static final int UPDATE_NO_IN = 1;
	private static final int UPDATE_YES_OUT = 2;
	private static final int UPDATE_NO_OUT = 3;

	public static VersionUtils getInstance() {
		if (instance == null) {
			instance = new VersionUtils();
		}
		return instance;
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			try {
				if (ldu != null)
					ldu.hide();
				if (msg.what == UPDATE_YES_IN) {
					queryUpdate(true);
				} else if (msg.what == UPDATE_NO_IN) {
					queryUpdate(false);
				} else if (msg.what == UPDATE_YES_OUT) {
					// new NotificationMgr(context)
					// .showUpdateNotification(mMessage);
				} else if (msg.what == UPDATE_NO_OUT) {

				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		;
	};

	public void checkVersion(final Context context, final boolean isSilence) {
		if (!isSilence) {
			ldu = new LoadingDialogUtil(context);
			ldu.show(R.string.update_checking);
		}
		this.context = context;
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				getServerAddress();

				if (canUpdate(context)) {
					handler.sendEmptyMessage(UPDATE_YES_IN);
				} else if (!isSilence)
					handler.sendEmptyMessage(UPDATE_NO_IN);
			}
		}.start();
	}

	public void checkVersion(final Context context) {
		this.context = context;
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				getServerAddress();

				if (canUpdate(context)) {
					handler.sendEmptyMessage(UPDATE_YES_IN);
				} else
					handler.sendEmptyMessage(UPDATE_NO_OUT);
			}
		}.start();
	}

	private void getServerAddress() {
		try {
				mServer = Common.getUpdate_Server(context);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void queryUpdate(boolean ret) {
		CustomDialog.Builder customBuilder = new CustomDialog.Builder(context);
		if (ret) {
			customBuilder.setTitle(R.string.update_note);
			// customBuilder.setTitleBG(R.drawable.white);

			if (jsa != null) {
				try {
					StringBuffer updateInfor = new StringBuffer();
					updateInfor.append(jsa.getJSONObject(0).getString(
							"description"));
					for (int kk = 1; kk < jsa.length(); kk++) {
						updateInfor.append("\n");
						updateInfor.append(jsa.getJSONObject(kk).getString(
								"description"));
					}
					customBuilder.setMessage(updateInfor.toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			customBuilder.setPositiveButton(R.string.update_yes,
					new CustomDialog.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();

							if (Environment.getExternalStorageState().equals(
									Environment.MEDIA_MOUNTED)) {
								// Intent intent = new
								// Intent(Intent.ACTION_VIEW,
								// Uri.parse(mServer
								// + Common.UPDATE_APK_NAME));
								// context.startActivity(intent);

								download(mServer + Common.UPDATE_APK_NAME);

								// Intent intent = new Intent(context,
								// UpdateActivity.class);
								// intent.putExtra("server", mServer);
								// context.startActivity(intent);

							} else {
								Toast.makeText(context, R.string.no_sdcard,
										Toast.LENGTH_LONG).show();
							}
						}
					}).setNegativeButton(R.string.update_no,
					new CustomDialog.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
		} else {
			customBuilder
					.setTitle("提示")
					.setPositiveButton(R.string.OK,
							new CustomDialog.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							}).setMessage(R.string.update_is_latest);
			
		
		}
		
		Dialog dialog = customBuilder.create();
		dialog.show();

	}

	public long download(String url) {
		DownloadManager downloadManager = (DownloadManager) context
				.getSystemService(Context.DOWNLOAD_SERVICE);

		Uri uri = Uri.parse(url);
		DownloadManager.Request request = new DownloadManager.Request(uri);

		// 设置允许使用的网络类型，这里是移动网络和wifi都可以
		request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
				| DownloadManager.Request.NETWORK_WIFI);

		// 禁止发出通知，既后台下载，如果要使用这一句必须声明一个权限：android.permission.DOWNLOAD_WITHOUT_NOTIFICATION
		// request.setShowRunningNotification(false);

		// 不显示下载界面
		request.setVisibleInDownloadsUi(false);
		/*
		 * 设置下载后文件存放的位置,如果sdcard不可用，那么设置这个将报错，因此最好不设置如果sdcard可用，下载后的文件
		 * 在/mnt/sdcard
		 * /Android/data/packageName/files目录下面，如果sdcard不可用,设置了下面这个将报错，
		 * 不设置，下载后的文件在/cache这个 目录下面
		 */
		// request.setDestinationInExternalFilesDir(this, null, "tar.apk");
		return downloadManager.enqueue(request);
	}

	public boolean canUpdate(Context context) {
		String url = mServer + Common.UPDATE_VERSION_JSON;
		System.out.println("要解析的url" + url);
		if (getServerVer(url)) {
			Log.i("version","getServerVer=true");
			if (parseServerVer(context)) {
				Log.i("version","parseServerVer=true");
				return true;
			}
		}
		return false;
	}

	private boolean getServerVer(String url) {
		Response response = httpGetResponse(url);
		if (response == null) {
			Log.i("version","response == null");
			return false;
		}
		try {

			mVersionJSONArray = response.asJSONArray();
			//System.out.println("就要看看这个" + mVersionJSONArray.toString());
			if (mVersionJSONArray == null) {
				Log.i("version","mVersionJSONArray == null");
				return false;
			}else {
				return true;
			}
		} catch (ResponseException e) {
			Log.i("version","mVersionJSONArray .e");
			return false;
		}
	}

	private boolean parseServerVer(Context context) {
		int currentVerCode = getVerCode(context);
		Log.i("version","currentVerCode="+currentVerCode);
		if (currentVerCode < 0) {
			return false;
		}
		int newVerCode = -1;
		StringBuffer stringbuffer = new StringBuffer();
		int length = mVersionJSONArray.length();
		JSONObject jsonObject = null;
		stringbuffer.append(context.getResources().getText(
				R.string.update_current_version));
		stringbuffer.append(getVerName(context));
		stringbuffer.append("\n\n");
		for (int i = 0; i < length; i++) {
			try {
				jsonObject = mVersionJSONArray.getJSONObject(i);
				if (jsonObject == null) {
					return false;
				} else {
					newVerCode = jsonObject.getInt("verCode");
					Log.i("version","newVerCode="+newVerCode);
					if (newVerCode > currentVerCode) {
						if (!mHaveNewVersion) {
							mHaveNewVersion = true;
						}
						String verName = jsonObject.getString("verName");

						JSONArray jsonArray = jsonObject
								.getJSONArray("descriptionArray");
						jsa = jsonArray;

						try {
							mMessage = jsonObject.getString("updateContent");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						StringBuffer desStringBuffer = new StringBuffer();
						if (jsonArray != null) {
							for (int j = 0; j < jsonArray.length(); j++) {
								desStringBuffer.append(j + 1);
								desStringBuffer.append(" ");
								desStringBuffer.append(jsonArray.getJSONObject(
										j).getString("description"));
								desStringBuffer.append("\n");
							}
						}
						stringbuffer.append(context.getResources().getText(
								R.string.update_new_version));
						stringbuffer.append(verName);
						stringbuffer.append("\n");
						stringbuffer.append(desStringBuffer);
						Log.e("xuxuxu", stringbuffer.toString());
					} else {
						return false;
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	private Response httpGetResponse(String url) {
		Response response = null;
		

		HttpResponse httpResponse =null; 
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(url);
			httpResponse = client.execute(get);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				response = new Response(httpResponse);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	public int getVerCode(Context context) {
		int verCode = -1;
		try {
			verCode = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
		}
		return verCode;
	}

	public String getVerName(Context context) {
		String verName = "";
		try {
			verName = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
		}
		return verName;
	}
}
