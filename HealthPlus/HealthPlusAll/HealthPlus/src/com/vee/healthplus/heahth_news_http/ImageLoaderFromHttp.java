package com.vee.healthplus.heahth_news_http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.social.MissingAuthorizationException;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import android.R.integer;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidquery.util.Progress;
import com.google.gson.Gson;
import com.vee.healthplus.TaskCallBack.TaskCallback;
import com.vee.healthplus.TaskCallBack.TaskResult;
import com.vee.healthplus.heahth_news_beans.Doc;
import com.vee.healthplus.heahth_news_beans.Root;
import com.vee.healthplus.heahth_news_utils.CheckNetWorkStatus;
import com.vee.healthplus.heahth_news_utils.JsonCache;
import com.vee.healthplus.http.HttpClient;
import com.vee.healthplus.http.Response;
import com.vee.healthplus.ui.heahth_exam.ProgressDiaogdownload;
import com.vee.healthplus.ui.heahth_news.MyNewsPagerAdapter;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class ImageLoaderFromHttp {

	private MyNewsPagerAdapter adapter;
	private List<String> urlList;
	private ViewPager viewPager;
	private ProgressDiaogdownload dialog;
	private List<String> tiList;
	private String hasNet = "1", JsonCach = "2", imgurl;
	private JsonCache jsonCache;
	private Context context;
	private TaskCallback taskCallbacks;

	public ImageLoaderFromHttp() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ImageLoaderFromHttp(Context context, MyNewsPagerAdapter adapter,
			ViewPager viewPager) {
		super();
		this.adapter = adapter;
		this.viewPager = viewPager;

		imgurl = Contact.HealthNES_URL_1;
		jsonCache = JsonCache.getInstance();
		this.context = context;
	}

	public void GetNews(TaskCallback callback) {
		taskCallbacks = callback;
		if (CheckNetWorkStatus.Status(context)) {
			new GetNewsImgTask().execute(imgurl, hasNet);

		} else {
			new GetNewsImgTask().execute(imgurl, JsonCach);
		}
	}

	private class GetNewsImgTask extends AsyncTask<String, Void, String> {
		private Exception exception;
		private HttpClient httpClient = new HttpClient();
		private StringBuffer stringBuffer = new StringBuffer();

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				switch (Integer.parseInt(params[1])) {
				case 1:
					// 下载ViewPager 图片
					Response response = httpClient.get(params[0]);
					InputStream isInputStream = response.asStream();

					BufferedReader reader = new BufferedReader(
							new InputStreamReader(isInputStream));

					String line = null;

					while ((line = reader.readLine()) != null) {
						stringBuffer.append(line);
					}
					jsonCache.saveJson(stringBuffer.toString(), params[0]);

					return stringBuffer.toString();
				case 2:
					String jsonCacheData = jsonCache.getJson(params[0]);
					return jsonCacheData;
				default:
					break;
				}

			} catch (Exception e) {
				this.exception = e;
			}
			return null;

		}

		@Override
		protected void onPostExecute(String data) {
			if (exception != null) {
				String message;

				if (exception instanceof HttpClientErrorException
						&& ((((HttpClientErrorException) exception)
								.getStatusCode() == HttpStatus.BAD_REQUEST) || ((HttpClientErrorException) exception)
								.getStatusCode() == HttpStatus.UNAUTHORIZED)) {
					message = "unauthorized,signout and signin again";

				}
				if (exception instanceof DuplicateConnectionException) {
					message = "The connection already exists.";
				} else if (exception instanceof ResourceAccessException
						&& exception.getCause() instanceof ConnectTimeoutException) {
					message = "connect time out";
				} else if (exception instanceof MissingAuthorizationException) {
					message = "please login first";
				} else {
					message = "您的网络有问题";
				}

			} else {
				if (data != null && data.length() > 0) {

					Gson gson = new Gson();
					Root root = gson.fromJson(data, Root.class);
					System.out.println(data.toString());
					urlList = new ArrayList<String>();
					tiList = new ArrayList<String>();
					List<Doc> docList = root.getResponse().getDocs();
					for (int i = 0; i < docList.size(); i++) {
						urlList.add(docList.get(i).getImgUrl());
						tiList.add(docList.get(i).getTitle());
					}
					adapter.addImages(urlList);
					viewPager.setAdapter(adapter);
					adapter.notifyDataSetChanged();
					// dialog.dismissProgressDialog();
					TaskResult taskResult = new TaskResult();
					taskResult.titles = tiList;
					System.out.println(taskResult.titles.toString());
					taskCallbacks.taskCallback(taskResult);
				}
			}
		}

	}
}
