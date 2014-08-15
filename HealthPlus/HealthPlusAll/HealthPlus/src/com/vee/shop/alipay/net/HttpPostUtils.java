package com.vee.shop.alipay.net;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;

import com.vee.healthplus.R;
import com.vee.shop.alipay.beans.SignBeans;

public class HttpPostUtils {
	/**
	 * post请求数据
	 * 
	 * @param url
	 * @param context
	 * @param params
	 * @return 返回的数据
	 * @throws MyException
	 */
	public static String getPostStr(String url, Context context,
			ArrayList<BasicNameValuePair> params) throws MyException {

		HttpEntity requestHttpEntity;
		HttpResponse response = null;

		try {
			HttpPost httpPost = new HttpPost(url);
			HttpClient httpClient = new DefaultHttpClient();
			if (isWifi(context)) {
				String host = Proxy.getDefaultHost();// 此处Proxy源自android.net
				int port = Proxy.getPort(context);// 同上
				HttpHost httpHost = new HttpHost(host, port);
				httpClient.getParams().setParameter(
						ConnRoutePNames.DEFAULT_PROXY, httpHost);
			}

			requestHttpEntity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
			httpPost.setEntity(requestHttpEntity);

			response = httpClient.execute(httpPost);

			if (response != null
					&& response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String jsonstr = EntityUtils.toString(response.getEntity(),
						HTTP.UTF_8);

				// writetoFile(context, jsonstr);
				return jsonstr;
			} else {
				return null;
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new MyException(MyException.ERRORCODE_UNKNOW_CODE,
					ExceptionMessage
							.getExceptionMsg(context, R.string.netError));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new MyException(MyException.ERRORCODE_UNKNOW_CODE,
					ExceptionMessage
							.getExceptionMsg(context, R.string.netError));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new MyException(MyException.ERRORCODE_UNKNOW_CODE,
					ExceptionMessage
							.getExceptionMsg(context, R.string.netError));
		}
	}

	/**
	 * 是否是wifi
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWifi(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		String proxyHost = android.net.Proxy.getDefaultHost();// 通过andorid.net.Proxy可以获取默认的代理地址
		boolean isProxy = false;
		if (cm != null) {
			NetworkInfo ni = cm.getActiveNetworkInfo();
			if (ni != null) {
				if (ni.getTypeName().equalsIgnoreCase("WIFI")) {
					isProxy = false;
				} else if (ni.getTypeName().equalsIgnoreCase("MOBILE")) {
					if (proxyHost != null && !proxyHost.equals("")) {
						// WAP方式
						isProxy = true;
					} else {
						// GPRS方式
						isProxy = false;
					}
				}
			}
		}
		return isProxy;
	}

	/**
	 * 从xml数据中已获取签名数据
	 * 
	 * @param xmlStr
	 * @return
	 */
	public static SignBeans getSinBeans(String xmlStr) {
		SignBeans _siBeans = new SignBeans();
		int contentStart = xmlStr.indexOf("<content>") + 9;
		int contentend = xmlStr.indexOf("</content>");
		_siBeans.content = xmlStr.substring(contentStart, contentend);

		int signStart = xmlStr.indexOf("<sign>") + 6;
		int signtend = xmlStr.indexOf("</sign>");

		_siBeans.sign = xmlStr.substring(signStart, signtend);

		return _siBeans;

	}
}