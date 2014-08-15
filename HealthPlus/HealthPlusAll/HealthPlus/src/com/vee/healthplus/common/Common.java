package com.vee.healthplus.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;

import com.vee.healthplus.R;
import com.vee.healthplus.http.StatisticsUtils;

public class Common {



//	public static  String APPLICATION_ID = "700071";
	public static  String UPDATE_SERVER = "http://cdn.17vee.com/lmstation/shoujiweishi/";
			//+ APPLICATION_ID + "/";
	public static final String UPDATE_VERSION_JSON = "version.json";
	public static final String UPDATE_APK_NAME = "CloudDoctor.apk";
	public static final String DOWNLOADDIRNAME = "CloudDoctor";
	public static String channel = null;
	
	public static String  getAppId(Context context){
		InputStream is = context.getResources().openRawResource(R.raw.channel);
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		try {
			channel = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return channel;
	}
	
	public static String getUpdate_Server(Context context){
		
		return UPDATE_SERVER+getAppId(context)+"/";
	}
	
	
}
