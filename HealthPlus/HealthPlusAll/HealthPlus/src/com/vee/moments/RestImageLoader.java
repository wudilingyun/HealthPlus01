package com.vee.moments;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.vee.healthplus.heahth_news_http.ImageGetFromHttp;
import com.vee.healthplus.heahth_news_utils.ImageFileCache;
import com.vee.healthplus.heahth_news_utils.ImageMemoryCache;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

public class RestImageLoader {
	private static RestImageLoader instance;

	//线程池控制器
	private ExecutorService executorService;

	//内存缓存对象
	private ImageMemoryCache memoryCache;

	//文件缓存对象
	private ImageFileCache fileCache;

	//任务列表
	private Map<String, ImageView> taskMap;

	private boolean allowLoad = true;
private Context context;
	private RestImageLoader(Context context) {
		//获取当前Java虚拟机所需要使用到的处理器的数量
		int cpuNums = Runtime.getRuntime().availableProcessors();
		//创建线程池控制器对象，线程池当中线程的数量为处理器数量加一
		this.executorService = Executors.newFixedThreadPool(cpuNums + 1);
		this.context = context;
		this.memoryCache = new ImageMemoryCache(context);
		this.fileCache = new ImageFileCache();
		this.taskMap = new HashMap<String, ImageView>();
	}

	//生成ImageLoader对象
	public static RestImageLoader getInstance(Context context) {
		if (instance == null) {
			instance = new RestImageLoader(context);
		}
		return instance;
	}

	public void resotre() {
		this.allowLoad = true;
	}

	public void lock() {
		this.allowLoad = false;
	}

	public void unlock() {
		this.allowLoad = true;
		doTask();
	}

	//1.从缓存当中查找是否已经存在与该url对应的图片对象
	//2.如果缓存当中没有该图片对象，则开启新线程下载
	public void addTask(String url, ImageView img) {
		//根据URL查询内存缓存当中是否存在该图片
		Bitmap bitmap = memoryCache.getBitmapFromCache(url);
		if (bitmap != null) {
			img.setImageBitmap(bitmap);
		} else {
			synchronized (taskMap) {
				//将图片的url设置给img对象的tag属性
				img.setTag(url);
				//向taskMap当中添加一个任务
				taskMap.put(Integer.toString(img.hashCode()), img);
			}
			if (allowLoad) {
				doTask();
			}
		}
	}

	public void doTask() {
		synchronized (taskMap) {
			Collection<ImageView> con = taskMap.values();
			for (ImageView i : con) {
				if (i != null) {
					if (i.getTag() != null) {
						loadImage((String) i.getTag(), i);
					}
				}
			}
			taskMap.clear();
		}
	}

	private void loadImage(String url, ImageView img) {
		System.out.println("loadImage---->" + url);
		TaskHandler handler = new TaskHandler(url,img);
		TaskWithResult task = new TaskWithResult(handler, url);
		this.executorService.submit(task);
	}

	private class TaskWithResult implements Callable<String> {
		private String url;
		private Handler handler;

		public TaskWithResult(Handler handler, String url) {
			this.url = url;
			this.handler = handler;
		} 

		@Override
		public String call() throws Exception {
			
			System.out.println("call-------------------------------");
			Message msg = new Message();
			msg.obj = getBitmap(url);
			if (msg.obj != null) {
				handler.sendMessage(msg);
			}
			return url;
		}
	}

	private Bitmap getBitmap(String url) {
		//从内存缓存当中获取bitmap对象
		Bitmap result = memoryCache.getBitmapFromCache(url);

		if (result == null) {
			//从文件缓存当中获取bitmap对象
			result = fileCache.getImage(url);
			if (result == null) {
				//从网络当中下载数据
				byte[] bimage = SpringAndroidService.getInstance((Application) context.getApplicationContext()).downloadImageByUrl(url);
				result = BitmapFactory.decodeByteArray(bimage, 0,
						bimage.length);
				//result = ImageGetFromHttp.downloadBitmap(url);
				if (result != null) {
					//将下载的图片分别存至内存缓存和文件缓存
					fileCache.saveBitmap(result, url);
					memoryCache.addBitmapToCache(url, result);
				} else {
					memoryCache.addBitmapToCache(url, result);
				}
			}
		}
		return result;
	}

	private class TaskHandler extends Handler {
		String url;
		ImageView img;

		public TaskHandler(String url, ImageView img) {
			this.url = url;
			this.img = img;
		}

		public void handleMessage(Message msg) {
			if (img.getTag().equals(url)) {
				if (msg.obj != null) {
					Bitmap bitmap = (Bitmap) msg.obj;
					img.setImageBitmap(bitmap);
				}
			}
		}
	}
}
