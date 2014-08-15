package com.vee.healthplus.util.sporttrack.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class WeatherUtil {
	private static WeatherUtil instance;

	public static final int WEAHTER_CITY = 5;
	public static final int WEAHTER_CONTENT = 6;

	private Context context;
	private Map<String, String> mapAllNameID;

	private Handler handler;

	public WeatherUtil(Context context, Handler handler) {
		this.context = context;
		this.handler = handler;
		initData();
	}

	private void initData() {
		ImageLoader.getInstance().init(
				ImageLoaderConfiguration.createDefault(context));
		NameIDMap nameIDMap = NameIDMap.getInstance();
		mapAllNameID = nameIDMap.getMapAllNameID();
	}

	public static WeatherUtil getInstance(Context context, Handler handler) {
		// if (instance == null) {
		instance = new WeatherUtil(context, handler);
		// }
		return instance;
	}

	public void getWeather(String city) {
		try {
			String id = mapAllNameID.get(city);
			String strUrl = "http://www.weather.com.cn/data/cityinfo/" + id
					+ ".html";
			System.out.println("当前网址" + strUrl);
			downloadTread(strUrl, WEAHTER_CONTENT);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void downloadTread(final String urlStr, final int tag) {
		new Thread() {
			@Override
			public void run() {
				super.run();
				String str = download(urlStr);
				Message msg = handler.obtainMessage();
				msg.arg1 = tag;
				Bundle bundle = new Bundle();
				bundle.putString("json", str);
				msg.setData(bundle);
				handler.sendMessage(msg);
			}
		}.start();

	}

	private String download(String urlStr) {
		StringBuffer sb = new StringBuffer();
		String line = null;
		BufferedReader buffer = null;
		try {
			URL url = new URL(urlStr);
			HttpURLConnection urlConn = (HttpURLConnection) url
					.openConnection();
			buffer = new BufferedReader(new InputStreamReader(
					urlConn.getInputStream()));
			while ((line = buffer.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (buffer != null)
					buffer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public void getWeatherImage(String name, final ImageView view) {

		Bitmap weatherbt = getImageFromAssetsFile("weatherPic/" + name);
		view.setImageBitmap(weatherbt);

	}

	/*
	 * 从assets读取图片
	 */
	private Bitmap getImageFromAssetsFile(String fileName) {
		Bitmap image = null;
		AssetManager am = context.getResources().getAssets();
		try {
			InputStream is = am.open(fileName);
			is.available();
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return image;

	}

	public void parseJsonAndShow(String jsonData, TextView textView,
			ImageView view) {
		StringBuffer stringBuffer = new StringBuffer();
		try {
			JSONObject allWeatherData = new JSONObject(jsonData);
			JSONObject weatherData = allWeatherData
					.getJSONObject("weatherinfo");
			//
			StringBuffer weatherInfo1 = new StringBuffer();
			// weatherInfo1.append("地点:" + weatherData.getString("city"));
			// weatherInfo1.append("\n时间:" + weatherData.getString("date_y")
			// + "		" + weatherData.getString("week"));
			// weatherInfo1.append("\n今日天气:" +
			// weatherData.getString("weather1"));
			stringBuffer.append(weatherData.getString("weather1") + " ");
			// weatherInfo1.append("\n温度:" + weatherData.getString("temp1"));
			stringBuffer.append(weatherData.getString("temp1") + " ");

			getWeatherImage(weatherData.getString("img1"), view);
			textView.setText(stringBuffer.toString());
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public HashMap<String, String> parseJsonAndShow(String jsonData) {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		StringBuffer stringBuffer = new StringBuffer();
		StringBuffer stringBuffer_temp = new StringBuffer();
		StringBuffer stringBuffer_city = new StringBuffer();
		try {
			JSONObject allWeatherData = new JSONObject(jsonData);
			JSONObject weatherData = allWeatherData
					.getJSONObject("weatherinfo");
			//
			StringBuffer weatherInfo1 = new StringBuffer();
			// weatherInfo1.append("地点:" + weatherData.getString("city"));
			// weatherInfo1.append("\n时间:" + weatherData.getString("date_y")
			// + "		" + weatherData.getString("week"));
			// weatherInfo1.append("\n今日天气:" +
			// weatherData.getString("weather1"));
			stringBuffer.append(weatherData.getString("weather") + " ");
			// weatherInfo1.append("\n温度:" + weatherData.getString("temp1"));
			stringBuffer_temp.append(weatherData.getString("temp2") + "~"
					+ weatherData.getString("temp1"));
			stringBuffer_city.append(weatherData.getString("city") + "");
			hashMap.put("txt", stringBuffer.toString());
			hashMap.put("temp", stringBuffer_temp.toString());
			hashMap.put("img", weatherData.getString("img1"));
			hashMap.put("city", stringBuffer_city.toString());
			return hashMap;
		} catch (Exception e) {
			e.printStackTrace();
			return hashMap;
		}
	}

}