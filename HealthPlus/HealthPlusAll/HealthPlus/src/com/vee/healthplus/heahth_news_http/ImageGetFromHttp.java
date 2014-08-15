package com.vee.healthplus.heahth_news_http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageGetFromHttp {
	public static Bitmap downloadBitmap(String url) {
		final HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 7000);
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				7000);
		final HttpGet httpGet = new HttpGet(url);
		InputStream inputStream = null;
		Bitmap bitmap = null;
		try {
			HttpResponse response = httpClient.execute(httpGet);
			if (response.getStatusLine().getStatusCode() == 200) {
				inputStream = response.getEntity().getContent();
				
				bitmap = BitmapFactory.decodeStream(inputStream);
				
				/*if(!bitmap.isRecycled()){
					bitmap.recycle();
					System.gc();
				}*/
				//return bitmap;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// return compressImage(bitmap);
		return bitmap;
	}

	public static Bitmap compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		image.compress(Bitmap.CompressFormat.JPEG, 30, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;

		/*
		 * while (baos.toByteArray().length / 1024 > 10) { //
		 * 循环判断如果压缩后图片是否大于100kb,大于继续压缩 baos.reset();// 重置baos即清空baos
		 * image.compress(Bitmap.CompressFormat.JPEG, options, baos);//
		 * 这里压缩options%，把压缩后的数据存放到baos中 options -= 10;// 每次都减少10 }
		 */
		System.out.println("压缩后大小" + baos.toByteArray().length / 1024);
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap1 = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap1;
	}

}
