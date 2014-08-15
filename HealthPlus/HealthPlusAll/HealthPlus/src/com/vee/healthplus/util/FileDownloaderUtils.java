package com.vee.healthplus.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;


public class FileDownloaderUtils extends Thread {

	private Context mContext;
	private Handler mHandler;
	private String mDownloaderUrl;
	private String mFileName;
	private Message msg;

	public static final int MSG_DOWNLOAD_START = 0;
	public static final int MSG_DOWNLOADING = 1;
	public static final int MSG_DOWNLOAD_FINISH = 2;
	public static final int MSG_DOWNLOAD_FAIL = 3;

	public static String UPDATE_DOWNLOAD_FOLDER = "GameDownLoadDir";

	String tag = "DownloadService";

	public FileDownloaderUtils(Context mContext, Handler mHandler,
			String mDownloaderUrl, String mFileName) {
		this.mContext = mContext;
		this.mHandler = mHandler;
		this.mDownloaderUrl = mDownloaderUrl;
		this.mFileName = mFileName;
		this.msg = new Message();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				UPDATE_DOWNLOAD_FOLDER = MultiDownloadItem.DOWNLOADDIRNAME;
				Message downloadMsg = new Message();
				downloadMsg.what = MSG_DOWNLOAD_START;
				mHandler.sendMessage(downloadMsg);
				String sdCardRoot = Environment.getExternalStorageDirectory()
						.getAbsolutePath();
				File folder = new File(sdCardRoot + File.separator
						+ UPDATE_DOWNLOAD_FOLDER);
				if (!folder.exists()) {
					boolean b = folder.mkdirs();
				}

				File fileSavedPath = new File(folder, mFileName);
				if (fileSavedPath.exists()) {
					fileSavedPath.delete();
				}
				fileSavedPath.createNewFile();

				boolean downSuccess = downloadFileGet(mDownloaderUrl,
						fileSavedPath);

				if (downSuccess) {
					msg.obj = sdCardRoot + File.separator
							+ UPDATE_DOWNLOAD_FOLDER + File.separator
							+ mFileName;
					msg.what = MSG_DOWNLOAD_FINISH;
				} else {
					msg.what = MSG_DOWNLOAD_FAIL;
				}

			} else {
				msg.what = MSG_DOWNLOAD_FAIL;
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg.what = MSG_DOWNLOAD_FAIL;
		} finally {
			mHandler.sendMessage(msg);
		}
	}

	public boolean downloadFileGet(String downloadUrl, File savePath)
			throws Exception {
		int downloadFileSize = 0;
		long fileSize = 0;
		boolean result = false;

		HttpClient hc = new DefaultHttpClient();
		HttpGet http = new HttpGet(downloadUrl);
		http.setHeader("Accept", "*/*");
		http.setHeader("Accept-Language", "zh-tw, zh-cn, en");
		// http.setHeader("User-Agent", "Nokia N97");
		http.setHeader("Charset",
				"utf-8, utf-16, iso-8859-1, iso-10646-ucs-2, GB2312, windows-1252, us-ascii");
		http.setHeader("Connection", "Keep-Alive");

		HttpResponse hr = hc.execute(http);

		if (hr.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			fileSize = hr.getEntity().getContentLength();

			InputStream is = hr.getEntity().getContent();
			FileOutputStream fos = new FileOutputStream(savePath);
			byte[] buffer = new byte[5 * 1024];
			int i = 0;
			int tmpsize = 0;
			while ((i = is.read(buffer)) != -1) {
				downloadFileSize = downloadFileSize + i;
				tmpsize = tmpsize + i;
				fos.write(buffer, 0, i);
				if (tmpsize * 100 / fileSize >= 1) {
					Message proMsg = new Message();
					proMsg.what = MSG_DOWNLOADING;
					proMsg.obj = (int) downloadFileSize * 100 / fileSize;
					mHandler.sendMessage(proMsg);
					tmpsize = 0;
				}

			}

			fos.flush();
			fos.close();
			is.close();

			result = true;
		}
		return result;
	}
}
