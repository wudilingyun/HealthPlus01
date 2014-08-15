package com.vee.healthplus.util;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Hashtable;

public class SoftCache {
	private static final String TAG = "SoftCache";
	private Hashtable<String, SoftReference> mItems = null;
	private ReferenceQueue mQuery = null;

	public SoftCache() {
		mItems = new Hashtable<String, SoftReference>();
		mQuery = new ReferenceQueue();
	}

	public Object getSoftCacheItem(String id) {
		if (mItems.containsKey(id)) {
			SoftReference sr = mItems.get(id);
			Object ret = sr.get();
			return ret;
		}
		return null;
	}

	public boolean addSoftCacheItem(String id, Object in) {
		// cleanCache();
		if (mItems.containsKey(id)) {
			return false;
		}
		SoftReference ref = new SoftReference(in, mQuery);
		mItems.put(id, ref);
		return true;
	}

	public boolean removeSoftCacheItem(String id) {
		if (mItems.containsKey(id)) {
			SoftReference sr = mItems.get(id);
			mItems.remove(id);
			return true;
		}
		return false;
	}

	private void cleanCache() {
		SoftReference ref = null;
		while ((ref = (SoftReference) mQuery.poll()) != null) {
			mItems.remove(ref);
		}
	}

	public void clearCache() {
		cleanCache();
		mItems.clear();
		System.gc();
		System.runFinalization();
	}
}
