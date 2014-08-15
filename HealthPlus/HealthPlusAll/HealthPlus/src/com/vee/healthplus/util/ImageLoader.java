package com.vee.healthplus.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Process;
import android.widget.ImageView;

public class ImageLoader {
	private static final String TAG = "ImageLoader";
	private static final int THREAD_COUNT = 5;
	private SoftCache mSoftCache = new SoftCache();
	private ExecutorService mExecutorService = Executors
			.newFixedThreadPool(THREAD_COUNT);
	private Handler mMainHandler = new Handler();
	public static String IMG_PATH = Environment.getExternalStorageDirectory()
			.getAbsolutePath();
	private static String mDownloadDir = IMG_PATH + "/GameDownLoadDir";
	public static String mDownloadDirImage = mDownloadDir + "/ImageCache";
	private File mDownloadDirFileApk = new File(mDownloadDir);
	private static File mDownloadDirFile = new File(mDownloadDirImage);

	private Hashtable<String, Object> mThreads = new Hashtable<String, Object>();

	public ImageLoader() {

	}

	private void checkDir() {

		if (!mDownloadDirFileApk.exists())
			mDownloadDirFileApk.mkdir();
		else if (!mDownloadDirFileApk.isDirectory()) {
			mDownloadDirFileApk.delete();
			mDownloadDirFileApk.mkdir();
		}

		if (!mDownloadDirFile.exists())
			mDownloadDirFile.mkdir();
		else if (!mDownloadDirFile.isDirectory()) {
			mDownloadDirFile.delete();
			mDownloadDirFile.mkdir();
		}
	}

	public Drawable loadDrawable(final ImageView imageView,
			final String imageUrl, final ImageCallback imageCallback) {
		checkDir();
		Drawable ret = null;
		synchronized (mSoftCache) {
			ret = (Drawable) mSoftCache.getSoftCacheItem(imageUrl);
		}
		if (ret != null)
			return ret;
		ret = getImageFromFile(imageUrl);

		if (ret != null) {
			synchronized (mSoftCache) {
				mSoftCache.addSoftCacheItem(imageUrl, ret);
			}
			return ret;
		}

		synchronized (mThreads) {
			if (mThreads.containsKey(imageUrl)) {
				return null;
			} else {
				mThreads.put(imageUrl, new Object());
			}
		}
		mExecutorService.execute(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub

				Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

				final Drawable dr = getDrawableFromUrl(imageUrl);

				// loadImageFromUrl(imageUrl);
				synchronized (mSoftCache) {
					mSoftCache.addSoftCacheItem(imageUrl, dr);
				}
				synchronized (mThreads) {
					mThreads.remove(imageUrl);
				}
				mMainHandler.post(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						imageCallback.iamgeLoaded(imageView, dr, imageUrl);
					}
				});
			}
		});

		return null;
	}

	private Drawable loadImageFromUrl(String imageUrl) {
		Drawable ret = null;
		InputStream in = null;
		try {
			in = new URL(imageUrl).openStream();
			ret = Drawable.createFromStream(in, "image.png");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

	private Drawable getImageFromFile(String imageUrl) {
		File temp = null;
		final String filename = getFileNameByUrl(imageUrl);
		if ((temp = getFileByName(filename)) != null) {
			//
			// long localLength = temp.length();
			// long urlLength = getImageLengthFromUrl(imageUrl);
			// if (urlLength != 0 && urlLength != localLength)
			// return null;

			FileInputStream fis = null;
			Drawable db = null;
			try {
				fis = new FileInputStream(temp);
				db = Drawable.createFromStream(fis, null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (fis != null)
					try {
						fis.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			if (db != null)
				return db;
		}
		return null;
	}

	private long getImageLengthFromUrl(String imageUrl) {
		// TODO Auto-generated method stub
		try {
			HttpClient httpClient = null;
			FileOutputStream outputStream = null;
			InputStream inputStream = null;

			httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(imageUrl);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			inputStream = httpEntity.getContent();
			if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
				// break;
				return 0;

			return httpResponse.getEntity().getContentLength();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return 0;
		}
	}

	public static String getFileNameByUrl(String url) {
		if (url == null || url.equals("")) {
			return null;
		}
		String temp1 = url.substring(0, url.lastIndexOf("/"));
		String temp2 = url.substring(url.lastIndexOf("/") + 1);
		String temp3 = temp1.substring(temp1.lastIndexOf("/") + 1);
		StringBuffer sb = new StringBuffer();
		sb.append(temp3);
		sb.append("_");
		sb.append(temp2);
		String ret = sb.toString();
		return ret;
	}

	public static File getFileByName(String name) {
		File[] files = mDownloadDirFile.listFiles();
		if (files == null) {
			return null;
		}
		for (File file : files) {
			if (file.getName().equals(name))
				return file;
		}
		return null;
	}

	private Drawable getDrawableFromUrl(String imgUrl) {
		{
			// TODO Auto-generated method stub
			String filename = getFileNameByUrl(imgUrl);
			Drawable db = null;
			HttpClient httpClient = null;
			FileOutputStream outputStream = null;
			InputStream inputStream = null;
			try {
				// do {
				httpClient = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(imgUrl);
				HttpResponse httpResponse = httpClient.execute(httpGet);
				HttpEntity httpEntity = httpResponse.getEntity();
				inputStream = httpEntity.getContent();
				if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
					// break;
					return null;
				File imagefile = new File(mDownloadDirFile, filename);
				if (imagefile.exists())
					imagefile.delete();

				outputStream = new FileOutputStream(imagefile, true);
				byte[] tmp = new byte[1024];
				int count = 0;
				while ((count = inputStream.read(tmp)) != -1) {
					outputStream.write(tmp, 0, count);
				}

				FileInputStream fis = null;
				try {
					fis = new FileInputStream(imagefile);
					db = Drawable.createFromStream(fis, null);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					if (fis != null)
						try {
							fis.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}
				// } while (false);

			} catch (Exception e) {
				e.printStackTrace();
				File imagefile = new File(mDownloadDirFile, filename);
				if (imagefile.exists())
					imagefile.delete();
			} finally {
				try {
					if (inputStream != null)
						inputStream.close();
					if (outputStream != null)
						outputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (httpClient != null
						&& httpClient.getConnectionManager() != null)
					httpClient.getConnectionManager().shutdown();
			}
			return db;
		}
	}

	// public interface ImageCallback
	// {
	// public void imageLoaded(Drawable imageDrawable);
	// }
	public interface ImageCallback {
		public void iamgeLoaded(ImageView imageView, Drawable imageDrawable,
                                String imageString);
	}

	public Bitmap changeToBitMap(String bitmapId, Drawable drawable) {
		Bitmap ret = null;
		synchronized (mSoftCache) {
			ret = (Bitmap) mSoftCache.getSoftCacheItem(bitmapId);
		}
		if (ret != null)
			return ret;

		ret = new ToBitmap().drawableToBitmap(drawable);
		if (ret != null) {
			synchronized (mSoftCache) {
				mSoftCache.addSoftCacheItem(bitmapId, ret);
			}
		}
		return ret;
	}

	public byte[] getImageByteArrayFromFile(String imageUrl) {
		File temp = null;
		final String filename = getFileNameByUrl(imageUrl);
		if ((temp = getFileByName(filename)) != null) {
			FileInputStream fis = null;
			byte[] byteArray = null;
			try {
				fis = new FileInputStream(temp);
				int len = fis.available();
				byteArray = new byte[len];
				fis.read(byteArray);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (fis != null)
					try {
						fis.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			if (byteArray != null && byteArray.length > 0)
				return byteArray;
		}
		return null;
	}
}
