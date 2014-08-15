package com.vee.healthplus.ui.user;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.social.MissingAuthorizationException;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.vee.healthplus.R;
import com.vee.healthplus.activity.BaseFragmentActivity;
import com.vee.healthplus.common.MyApplication;
import com.vee.healthplus.heahth_news_beans.Doc;
import com.vee.healthplus.heahth_news_beans.Root;
import com.vee.healthplus.heahth_news_http.Contact;
import com.vee.healthplus.heahth_news_utils.BadgeView;
import com.vee.healthplus.heahth_news_utils.CheckNetWorkStatus;
import com.vee.healthplus.heahth_news_utils.JsonCache;
import com.vee.healthplus.http.HttpClient;
import com.vee.healthplus.http.Response;
import com.vee.healthplus.ui.heahth_exam.ProgressDiaogdownload;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.util.user.ICallBack;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class StatementDetailsActivity extends BaseFragmentActivity {
	WebView webView;
	String stateContent;
	private String contentUrl;
	private ProgressDiaogdownload ProgressDiaog = new ProgressDiaogdownload(
			StatementDetailsActivity.this);
	private JsonCache jsonCache;
	private String hasNet = "1", JsonCach = "2";

	private LinearLayout loFrameLayout;
	private ImageView loadImageView;
	private Animation news_loadAaAnimation;
	private SharedPreferences settings;
	private SharedPreferences.Editor localEditor;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);

		View view = View.inflate(this, R.layout.healthplus_statement_webview, null);
		setContainer(view);

		getHeaderView().setHeaderTitle("声明条款");
		getHeaderView().setBackGroundColor(R.color.blue);
		setRightBtnVisible(View.GONE);
		setLeftBtnVisible(View.VISIBLE);
		setLeftBtnType(1);
		webView = (WebView) findViewById(R.id.webwiewshow);
		jsonCache = JsonCache.getInstance();

		settings = this.getSharedPreferences("TestXML", 0);
		localEditor = settings.edit();
		init();
		getData();
		if (CheckNetWorkStatus.Status(this)) {
			new GetNewsContactTask().execute(contentUrl, hasNet);

		} else {
			Toast.makeText(this, "请检查网络连接", Toast.LENGTH_SHORT).show();
			new GetNewsContactTask().execute(contentUrl, JsonCach);
		}
	}

	void init() {
		news_loadAaAnimation = AnimationUtils.loadAnimation(this,
				R.anim.wait_heart_result);
		loFrameLayout = (LinearLayout) findViewById(R.id.loading_frame_webview);
		loadImageView = (ImageView) findViewById(R.id.img_rotate);
		loadImageView.setAnimation(news_loadAaAnimation);
	}

	void getData() {
		Intent intent = getIntent();
		contentUrl = Contact.HEALTHNEW_Content_URL_1;
	}

	private class GetNewsContactTask extends AsyncTask<String, Void, String> {
		private Exception exception;
		private HttpClient httpClient = new HttpClient();
		private StringBuffer stringBuffer = new StringBuffer();

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// ProgressDiaog.showProgressDialog("正在加载");
			loadImageView.startAnimation(news_loadAaAnimation);
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				switch (Integer.parseInt(params[1])) {
				case 1:
					Response response = httpClient.get(params[0]);
					System.out.println(contentUrl);
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
					String jsonData = jsonCache.getJson(params[0]);
					System.out.println("jsondata" + jsonData);
					if (jsonData != null) {
						return jsonData;
					} else {
						return null;
					}

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

					SpringAndroidService.getInstance(getApplication())
							.signOut();

					finish();
				}
				if (exception instanceof DuplicateConnectionException) {
					message = "The connection already exists.";
				} else if (exception instanceof ResourceAccessException
						&& exception.getCause() instanceof ConnectTimeoutException) {
					message = "connect time out";
				} else if (exception instanceof MissingAuthorizationException) {
					message = "please login first";
				} else {
					message = "A problem occurred with the network connection. Please try again in a few minutes.";
				}

				Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT)
						.show();
			} else {
				if (data != null) {
					Gson gson = new Gson();
					Root root = gson.fromJson(data, Root.class);
					if (root != null) {
						stateContent = root.getResponse().getDocs().get(0)
								.getContent();
						System.out.println(stateContent);
						webView.loadDataWithBaseURL(null, stateContent,
								"text/html", "utf-8", null);
						ProgressDiaog.dismissProgressDialog();
						loFrameLayout.setVisibility(View.GONE);
						loadImageView.clearAnimation();
					} else {
						ProgressDiaog.dismissProgressDialog();
						Toast.makeText(getApplication(), "请检查网络连接",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		}
	}

	Boolean getCollectStat(String currtUrl) {
		// 保存当前网页
		String str = settings.getString(currtUrl, "");
		System.out.println("当前网页是" + str);
		if (str == "" || str.equals(null)) {
			return false;
		} else {
			return true;
		}

	}
}
