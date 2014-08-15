package com.vee.healthplus.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.client.HttpClient;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;

import com.vee.healthplus.common.MyApplication;

public class MultiDownloadItem {
	private static final String TAG = "MultiDownloadItem";

	public static final String DOWNLOADDIRNAME = "GameDownLoadDir";
	private final static int THREAD_NUMMBER = 5;
	private final static int DOWNLOAD_START = 1;
	private final static int DOWNLOAD_UPDATE = 2;
	private final static int DOWNLOAD_FINISH = 3;
	private final static int DOWNLOAD_ERROR = 4;
	private final static int DOWNLOAD_PAUSE = 5;

	private Hashtable<String, DownloadInfo> mThreads = new Hashtable<String, DownloadInfo>();
	public static final int MAX_DOWNLOAD_NUM = 2;

	private Context mContext;
	private File mDownloadDir = null;
	private Handler mMainHandler = null;
	DownloadItemCb mCb = null;
	private ExecutorService mExecutorService = Executors
			.newFixedThreadPool(THREAD_NUMMBER);;

	public MultiDownloadItem(Context context, DownloadItemCb cb) {
		mContext = context;
		mMainHandler = new Handler(Looper.getMainLooper());
		mCb = cb;
	}

	public boolean canDownload() {
		if (mThreads.size() >= MAX_DOWNLOAD_NUM)
			return false;
		return true;
	}

	public boolean startDownload(String name, String url, DownloadItemCb cb,
			long position) {

		if (mThreads.containsKey(name)) {
			DownloadInfo di = (DownloadInfo) mThreads.get(name);
			if (di == null) {
				return false;
			}
			di.mDownloadInfoThreadRun.cancelStopDownload();
			return true;
		}

		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return false;
		}

		// check download dir
		mDownloadDir = new File(Environment.getExternalStorageDirectory()
				+ File.separator + DOWNLOADDIRNAME);
		if (!mDownloadDir.exists()) {
			mDownloadDir.mkdir();
		} else if (mDownloadDir.exists() && !mDownloadDir.isDirectory()) {
			return false;
		}

		DownloadThreadRun dthr = new DownloadThreadRun(name, url, cb, position);

		// sendDownloadEvent(null,DOWNLOAD_START,name,0,0);

		mExecutorService.execute(dthr);
		mThreads.put(name, new DownloadInfo(name, url, null, dthr));
		return true;
	}

	public void stopDownload(String name) {
		if (!mThreads.containsKey(name)) {
			return;
		}

		DownloadInfo di = (DownloadInfo) mThreads.get(name);
		if (di == null) {
			return;
		}

		di.mDownloadInfoThreadRun.stopDownload();
		mThreads.remove(name);
	}

	public void cancelStopDownload(String name) {
		if (!mThreads.containsKey(name)) {
			return;
		}

		DownloadInfo di = (DownloadInfo) mThreads.get(name);
		if (di == null) {
			return;
		}

		di.mDownloadInfoThreadRun.cancelStopDownload();
	}

	public void stopAllDownload() {
		for (Iterator itr = mThreads.keySet().iterator(); itr.hasNext();) {
			String key = (String) itr.next();
			DownloadInfo value = (DownloadInfo) mThreads.get(key);
			if (value != null)
				value.mDownloadInfoThreadRun.stopDownload();
		}
	}

	static class DownloadInfo {
		public String mDownloadInfoName = null;
		public String mDownloadInfoUrl = null;
		public Thread mDownloadInfoThread = null;
		public DownloadThreadRun mDownloadInfoThreadRun = null;

		public DownloadInfo(String name, String url, Thread th,
				DownloadThreadRun thr) {
			mDownloadInfoName = name;
			mDownloadInfoUrl = url;
			mDownloadInfoThread = th;
			mDownloadInfoThreadRun = thr;
		}
	}

	class DownloadThreadRun implements Runnable {
		private String mDownloadThreadName = null;
		private String mDownloadThreadUrl = null;
		private DownloadItemCb mDownloadThreadCb = null;
		private long mDownloadThreadStartPosition = 0;

		private File mDownloadThreadFile = null;
		private long mDownloadThreadFileSize = 0;
		private long mDownloadThreadDownloadSize = 0;
		private boolean mDownloadThreadStop = false;
		private Object mDownloadThreadStopLock = new Object();

		public DownloadThreadRun(String name, String url, DownloadItemCb cb,
				long position) {
			mDownloadThreadName = name;
			mDownloadThreadUrl = url;
			mDownloadThreadCb = cb;
			mDownloadThreadStartPosition = position;
		}

		public void stopDownload() {
			synchronized (mDownloadThreadStopLock) {
				mDownloadThreadStop = true;
			}
		}

		public void cancelStopDownload() {
			synchronized (mDownloadThreadStopLock) {
				mDownloadThreadStop = false;
			}
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub

			Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

			sendDownloadEvent(mDownloadThreadCb, DOWNLOAD_START,
					mDownloadThreadName, 0, 0);
			String strTmp = MyApplication.getRealApkName(mDownloadThreadName);

			mDownloadThreadFile = new File(mDownloadDir, strTmp);
			boolean exist = false;
			if (mDownloadThreadFile.exists())
				exist = true;

			HttpClient httpClient = null;
			InputStream inputStream = null;
			BufferedRandomAccessFile outputStream = null;
			try {
				// httpClient = new DefaultHttpClient();
				// HttpGet httpGet = new HttpGet(mDownloadThreadUrl);
				// HttpResponse httpResponse = httpClient.execute(httpGet);
				// HttpEntity httpEntity = httpResponse.getEntity();
				//
				// mDownloadThreadFileSize = httpEntity.getContentLength();

				URL url = new URL(mDownloadThreadUrl);
				HttpURLConnection conn1 = (HttpURLConnection) url
						.openConnection();
				mDownloadThreadFileSize = conn1.getContentLength();

				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setReadTimeout(180000);
				conn.setRequestMethod("GET");

				conn.setRequestProperty("Range", "bytes="
						+ mDownloadThreadStartPosition + "-"
						+ (mDownloadThreadFileSize - 1));

				conn.connect();
				inputStream = conn.getInputStream();

				outputStream = new BufferedRandomAccessFile(
						mDownloadThreadFile, "rwd");
				if (!exist)
					outputStream.setLength(mDownloadThreadFileSize);
				outputStream.seek(mDownloadThreadStartPosition);

				int count = 0;
				int update = 0;
				mDownloadThreadDownloadSize = mDownloadThreadStartPosition;

				// 2013.6.3 lideqiang add first progress is 5
				int state = (int) (mDownloadThreadDownloadSize * 100 / mDownloadThreadFileSize);
				sendDownloadEvent(mDownloadThreadCb, DOWNLOAD_UPDATE,
						mDownloadThreadName, state < 5 ? 5 : state,
						mDownloadThreadDownloadSize);
				// end

				byte[] tmp = new byte[5 * 1024];
				while ((count = inputStream.read(tmp)) != -1) {

					outputStream.write(tmp, 0, count);
					update += count;
					mDownloadThreadDownloadSize += count;

					int pb = (int) (update * 100 / mDownloadThreadFileSize);
					if (pb >= 5) {

						int progressBarState = (int) (mDownloadThreadDownloadSize * 100 / mDownloadThreadFileSize);
						sendDownloadEvent(mDownloadThreadCb, DOWNLOAD_UPDATE,
								mDownloadThreadName, progressBarState,
								mDownloadThreadDownloadSize);
						update = 0;
					}
					synchronized (mDownloadThreadStopLock) {
						if (mDownloadThreadStop) {
							sendDownloadEvent(mDownloadThreadCb,
									DOWNLOAD_PAUSE, mDownloadThreadName, 0,
									mDownloadThreadDownloadSize);
							break;
						}
					}
				}

				synchronized (mDownloadThreadStopLock) {
					if (!mDownloadThreadStop)
						sendDownloadEvent(mDownloadThreadCb, DOWNLOAD_FINISH,
								mDownloadThreadName, 0, 0);
				}
			} catch (Exception e) {
				e.printStackTrace();
				sendDownloadEvent(mDownloadThreadCb, DOWNLOAD_ERROR,
						mDownloadThreadName, 0, 0);
			} finally {
				try {
					if (inputStream != null)
						inputStream.close();
					if (outputStream != null)
						outputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					sendDownloadEvent(mDownloadThreadCb, DOWNLOAD_ERROR,
							mDownloadThreadName, 0, 0);
				}
				// if (httpClient != null)
				// httpClient.getConnectionManager().shutdown();
			}
		}
	}

	public interface DownloadItemCb {
		void DownloadStart(String name);

		void DownloadUpdate(String name, int pro, long position);

		void DownloadFinish(String name);

		void DownloadError(String name);

		void DownloadPause(String name, long position);

		void AllPause();
	}

	private void sendDownloadEvent(final DownloadItemCb cb, final int which,
			final String name, final int progress, final long position) {
		synchronized (mMainHandler) {
			mMainHandler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					switch (which) {
					case DOWNLOAD_START:
						if (cb != null)
							cb.DownloadStart(name);
						else if (mCb != null)
							mCb.DownloadStart(name);
						break;
					case DOWNLOAD_UPDATE:
						if (cb != null)
							cb.DownloadUpdate(name, progress, position);
						else if (mCb != null)
							mCb.DownloadUpdate(name, progress, position);
						break;
					case DOWNLOAD_FINISH:
						mThreads.remove(name);
						if (cb != null)
							cb.DownloadFinish(name);
						else if (mCb != null)
							mCb.DownloadFinish(name);
						break;
					case DOWNLOAD_ERROR:
						mThreads.remove(name);
						if (cb != null)
							cb.DownloadError(name);
						else if (mCb != null)
							mCb.DownloadError(name);
						break;
					case DOWNLOAD_PAUSE:
						mThreads.remove(name);
						if (cb != null)
							cb.DownloadPause(name, position);
						else if (mCb != null)
							mCb.DownloadPause(name, position);
						break;
					default:
						break;
					}
					if (mThreads.size() == 0) {
						if (cb != null)
							cb.AllPause();
						else if (mCb != null)
							mCb.AllPause();
					}

				}
			});
		}
	}
}
