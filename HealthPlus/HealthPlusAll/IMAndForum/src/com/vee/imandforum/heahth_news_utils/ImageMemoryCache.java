package com.vee.imandforum.heahth_news_utils;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;

import android.support.v4.util.LruCache;

//本来是内存缓存
public class ImageMemoryCache {

	// 软引用缓存
	private static LinkedHashMap<String, SoftReference<Bitmap>> mSoftCache;
	// 设置它的容量
	private static final int SOFT_CACHE_SIZE = 15;
	// 硬引用缓存 LruCache 安卓提供方法。
	private static LruCache<String, Bitmap> mLruCache;

	@SuppressWarnings("serial")
	public ImageMemoryCache(Context context) {
		// 得到当前硬件可以分配给应用程序的内存大小，可能是32 24 或16
		int memClass = ((ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
		// 把当前硬件硬件分配到内存的四分之一，分配给硬引用缓存
		int cacheSize = 1024 * 1024 * memClass / 4;

		// 生成硬引用缓存对象。
		mLruCache = new LruCache<String, Bitmap>(cacheSize) {

			@Override
			protected void entryRemoved(boolean evicted, String key,
					Bitmap oldValue, Bitmap newValue) {
				if (oldValue != null) {
					// 把硬缓存中移除的数据oldValue放入到软缓存当中
					mSoftCache.put(key, new SoftReference<Bitmap>(oldValue));
				}
			}

			@Override
			protected int sizeOf(String key, Bitmap value) {
				if (value != null) {
					// 得到图片的大小
					return value.getRowBytes() * value.getHeight();
				} else {
					return 0;
				}
			}

		};

		// 生成软缓存对象
		mSoftCache = new LinkedHashMap<String, SoftReference<Bitmap>>(
				SOFT_CACHE_SIZE, 0.75f, true) {

			private static final long serialVersionUID = -2946103379414807087L;

			@Override
			protected boolean removeEldestEntry(
					Entry<String, SoftReference<Bitmap>> eldest) {
				if (size() > SOFT_CACHE_SIZE) {
					return true;
				}
				return false;
			}

		};
	}

	// 从缓存中获取Bitmap数据
	public Bitmap getBitmapFromCache(String url) {
		Bitmap bitmap;
		// 加上同步锁保证同一时刻只执行一个对象
		// 从硬引用获得数据
		synchronized (mLruCache) {
			bitmap = mLruCache.get(url);
			if (bitmap != null) {
				// 如果有bitmap对象。则将这个对象从硬引用中先移除在添加。放到前面防止被逐出。
				System.out.println("！！！！-----》命中硬存mLrucache");
				mLruCache.remove(url);
				mLruCache.put(url, bitmap);
				return bitmap;
			}
		}
		// 从软引用获得数据
		synchronized (mSoftCache) {
			SoftReference<Bitmap> bitmapReference = mSoftCache.get(url);
			if (bitmapReference != null) {
				bitmap = bitmapReference.get();
				if (bitmap != null) {
					// 把存在的这个图片提升到硬缓存
					System.out.println("！！！！命中软缓存mSoftCache");
				} else {
					mSoftCache.remove(url);
				}
			}
		}
		return null;

	}
	//增加硬缓存对象。
	public void addBitmapToCache(String url, Bitmap bitmap){
		if(bitmap != null){
			synchronized (mLruCache) {
				mLruCache.put(url, bitmap);
			}
		}
	}
	//清除硬缓存对象
	public void clearCache(){
		mSoftCache.clear();
		
	}
}
