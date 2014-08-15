package com.vee.healthplus.ui.heahth_news;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.social.MissingAuthorizationException;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import com.vee.healthplus.heahth_news_beans.NewsCollectinfor;
import com.vee.healthplus.heahth_news_beans.Root;
import com.vee.healthplus.heahth_news_http.Contact;
import com.vee.healthplus.heahth_news_utils.BadgeView;
import com.vee.healthplus.heahth_news_utils.CheckNetWorkStatus;
import com.vee.healthplus.heahth_news_utils.JsonCache;
import com.vee.healthplus.http.HttpClient;
import com.vee.healthplus.http.Response;
import com.vee.healthplus.ui.heahth_exam.ProgressDiaogdownload;
import com.vee.healthplus.ui.main.MainPage;
import com.vee.healthplus.ui.user.HealthPlusLoginActivity;
import com.vee.healthplus.util.user.HP_DBModel;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.util.user.ICallBack;
import com.vee.healthplus.widget.DrawableCenterTextView;
import com.vee.healthplus.widget.ShadowTextView;
import com.yunfox.s4aservicetest.response.GeneralResponse;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class Health_news_detailsActivity extends BaseFragmentActivity implements
		OnClickListener, ICallBack {
	WebView webView;
	String newscontent;
	List<Doc> all_news;
	String imgurl, weburl, title, brief;
	private String contentUrl;
	private ProgressDiaogdownload ProgressDiaog = new ProgressDiaogdownload(
			Health_news_detailsActivity.this);
	private JsonCache jsonCache;
	private String hasNet = "1", JsonCach = "2";

	private LinearLayout loFrameLayout;
	private ImageView loadImageView;
	private DrawableCenterTextView share_img, support_img, comment_img;
	private TextView support_count_img;
	private DrawableCenterTextView collect_img;
	private Animation news_loadAaAnimation;
	private BadgeView badgeView, badgeView_support;
	private SharedPreferences settings;
	private SharedPreferences.Editor localEditor;
	private int i = 0;
	private Bitmap shareimg_bitmap;
	private ImageView img;
	private int userid = 0;
	Drawable drawableleft_cancle, drawableleft_ok;
	private Boolean flag = false;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);

		View view = View.inflate(this, R.layout.health_news_webview, null);
		setContainer(view);
		getHeaderView().setHeaderTitle(getIntent().getStringExtra("name"));
		setRightBtnVisible(View.GONE);
		setLeftBtnVisible(View.VISIBLE);
		setLeftBtnType(1);
		setLeftBtnRes(R.drawable.hp_w_header_view_back);
		webView = (WebView) findViewById(R.id.webwiewshow);
		jsonCache = JsonCache.getInstance();
		init();
		getData();
		/*
		 * if (CheckNetWorkStatus.Status(this)) { new
		 * GetNewsContactTask().execute(contentUrl, hasNet); new
		 * getSupportCountAsync().execute(contentUrl); } else {
		 * Toast.makeText(this, "请检查网络连接", Toast.LENGTH_SHORT).show(); new
		 * GetNewsContactTask().execute(contentUrl, JsonCach); }
		 */
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == RESULT_OK||resultCode ==305)
			switch (requestCode) {
			case 1:
				clickSupport();
				break;
			case 2:
				getCollect();
				break;
			default:
				break;
			}
		super.onActivityResult(requestCode, resultCode, data);
	}

	void init() {
		news_loadAaAnimation = AnimationUtils.loadAnimation(this,
				R.anim.wait_heart_result);
		loFrameLayout = (LinearLayout) findViewById(R.id.loading_frame_webview);
		loadImageView = (ImageView) findViewById(R.id.img_rotate);
		loadImageView.setAnimation(news_loadAaAnimation);

		comment_img = (DrawableCenterTextView) findViewById(R.id.comment_img);
		support_img = (DrawableCenterTextView) findViewById(R.id.support_img);
		share_img = (DrawableCenterTextView) findViewById(R.id.share_img);
		collect_img = (DrawableCenterTextView) findViewById(R.id.collect_img);
		// support_count_img = (TextView) findViewById(R.id.support_count_img);
		share_img.setOnClickListener(this);
		support_img.setOnClickListener(this);
		comment_img.setOnClickListener(this);
		collect_img.setOnClickListener(this);
		userid = HP_User.getOnLineUserId(this);
	}

	void getData() {
		Intent intent = getIntent();
		weburl = (String) intent.getStringExtra("weburl");
		imgurl = (String) intent.getStringExtra("imgurl");
		title = (String) intent.getStringExtra("title");
		brief = (String) intent.getStringExtra("brief");
		if (CheckNetWorkStatus.Status(this)) {
			webView.loadUrl(weburl);
		} else {
			Toast.makeText(this, "请检查网络连接", Toast.LENGTH_SHORT).show();
		}

		all_news = new ArrayList<Doc>();
	

			Boolean flag = HP_DBModel.getInstance(this)
					.queryUserBooleanCollectInfor(userid, title, imgurl);

			drawableleft_cancle = getResources().getDrawable(
					R.drawable.collect_cancle);

			// / 这一步必须要做,否则不会显示.
			drawableleft_cancle.setBounds(0, 0,
					drawableleft_cancle.getMinimumWidth(),
					drawableleft_cancle.getMinimumHeight());

			drawableleft_ok = getResources().getDrawable(
					R.drawable.collect_normal);

			// / 这一步必须要做,否则不会显示.
			drawableleft_ok.setBounds(0, 0, drawableleft_ok.getMinimumWidth(),
					drawableleft_ok.getMinimumHeight());
			if (HP_User.getOnLineUserId(this)!= 0) {
			if (flag) { // 已经收藏
				collect_img.setCompoundDrawables(drawableleft_ok, null, null,
						null);
			} else {
				collect_img.setCompoundDrawables(drawableleft_cancle, null,
						null, null);
			}

			new BooleanDoSupportAsync().execute(imgurl);
		}
	}

	void clickSupport() {
		if (support_img.getText().toString() == "赞") {
			new SubmitSupportAsync().execute(imgurl);
		} else {
			new SubmitCancleSupportAsync().execute(imgurl);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.support_img:
			if (CheckNetWorkStatus.Status(this)) {
				if (HP_User.getOnLineUserId(this) == 0) {
					Intent intent = new Intent(this,
							HealthPlusLoginActivity.class);
					this.startActivityForResult(intent, 1);

					return;
				} else {
					clickSupport();
					return;
				}

			} else {
				Toast.makeText(this, "请检查网络连接", Toast.LENGTH_SHORT).show();
			}

			break;
		case R.id.collect_img:

			if (HP_User.getOnLineUserId(this) == 0) {
				Intent intent = new Intent(this, HealthPlusLoginActivity.class);
				this.startActivityForResult(intent, 2);
				return;

			} else if (HP_User.getOnLineUserId(this) != 0) {
				getCollect();

				return;
			}

			break;
		case R.id.share_img:
			if (CheckNetWorkStatus.Status(this)) {
				String sendMsg = getResources().getString(
						R.string.hp_share_invite);
				MyApplication.shareBySystem(this, title, imgurl, weburl, "",
						"", brief);
			} else {
				Toast.makeText(this, "请检查网络连接", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.comment_img:
			// 判断用户是否登录
			if (CheckNetWorkStatus.Status(this)) {
				if (HP_User.getOnLineUserId(this) == 0) {
					Intent intent = new Intent(this,
							HealthPlusLoginActivity.class);
					Bundle extras = new Bundle();
					extras.putParcelable(
							"cn",
							new ComponentName("com.vee.healthplus",
									"com.vee.healthplus.ui.heahth_news.Health_ValueBook_commentList_activity"));
					intent.putExtras(extras);
					startActivity(intent);
				} else if (HP_User.getOnLineUserId(this) != 0) {
					Intent intent3 = new Intent(
							Health_news_detailsActivity.this,
							Health_ValueBook_commentList_activity.class);
					intent3.putExtra("imgurl", imgurl);
					intent3.putExtra("weburl", weburl);
					startActivity(intent3);
					return;
				}
				return;

			} else {
				Toast.makeText(this, "请检查网络连接", Toast.LENGTH_SHORT).show();
			}

			break;

		default:
			break;
		}
	}

	void getCollect() {
		userid = HP_User.getOnLineUserId(this);
		Boolean bo = HP_DBModel.getInstance(this).queryUserBooleanCollectInfor(
				userid, title, imgurl);
		if (bo) {
			// 清除数据
			HP_DBModel.getInstance(this).deletUserCollect(userid, title,
					imgurl, weburl);
			collect_img.setCompoundDrawables(drawableleft_cancle, null, null,
					null);
			Toast.makeText(getApplicationContext(), "取消收藏", Toast.LENGTH_SHORT)
					.show();
		} else {
			// collect_img.setBackgroundResource(R.drawable.collect_select);
			HP_DBModel.getInstance(this).insertUserCollect(userid, title,
					imgurl, weburl);
			collect_img.setCompoundDrawables(drawableleft_ok, null, null, null);
			Toast.makeText(getApplicationContext(), "收藏成功", Toast.LENGTH_SHORT)
					.show();
		}

	}

	@Override
	public void onChange() {
		Intent intent = new Intent(this,
				Health_ValueBook_commentList_activity.class);
		intent.putExtra("imgurl", imgurl);
		startActivity(intent);
	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub

	}

	public class getSupportCountAsync extends
			AsyncTask<String, String, Integer> {
		private ICallBack iCallBack;

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			support_count_img.setText(String.valueOf(result));
		}

		@Override
		protected Integer doInBackground(String... params) {
			// 获取赞的个数
			try {
				int supportCount = SpringAndroidService.getInstance(
						getApplication()).getNewsSupportCount(
						params[0].toString());
				return supportCount;
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("异常是" + e.getMessage());
			}

			return null;
		}
	}

	public class SubmitSupportAsync extends AsyncTask<String, String, Integer> {
		private ICallBack iCallBack;

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			int currtCount = 0;
			if (result == 200) {
				support_img.setText("取消");
				Toast.makeText(getApplication(), "赞成功", Toast.LENGTH_SHORT)
						.show();
			} else {

				Toast.makeText(getApplication(), "赞失败" + result,
						Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected Integer doInBackground(String... params) {
			// 点击赞当前评论
			GeneralResponse gre = SpringAndroidService.getInstance(
					getApplication()).addsupporttonews(params[0]);
			return gre.getReturncode();

		}

	}

	public class SubmitCancleSupportAsync extends
			AsyncTask<String, String, Integer> {
		private ICallBack iCallBack;

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			int currtCount = 0;
			if (result == 200) {
				support_img.setText("赞");
				Toast.makeText(getApplication(), "取消赞", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(getApplication(), "取消失败" + result,
						Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected Integer doInBackground(String... params) {
			// 点击赞当前评论
			GeneralResponse gre = SpringAndroidService.getInstance(
					getApplication()).cancelsupporttonews(params[0]);
			return gre.getReturncode();

		}

	}

	public class BooleanDoSupportAsync extends
			AsyncTask<String, String, Boolean> {

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (!result) {
				support_img.setText("赞");

			} else {
				support_img.setText("取消");
				/*
				 * Toast.makeText(getApplication(), "已经赞过啦", Toast.LENGTH_SHORT)
				 * .show();
				 */

			}
		}

		@Override
		protected Boolean doInBackground(String... params) {
			// 点击赞当前评论
			try {
				Boolean flag = SpringAndroidService.getInstance(
						getApplication()).doISupportTheNews(params[0]);
				return flag;
			} catch (Exception e) {
				System.out.println("异常是" + e.getMessage());
			}
			return true;

		}

	}

}
