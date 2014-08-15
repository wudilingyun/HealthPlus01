package com.vee.healthplus.ui.user;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.vee.healthplus.R;
import com.vee.healthplus.ui.main.FirstActivity;
import com.vee.healthplus.util.user.HP_DBModel;
import com.vee.healthplus.util.user.HP_User;

public class TempActivity extends Activity {
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";
	public static final String MESSAGE_RECEIVED_ACTION = "com.vee.healthplus.MESSAGE_RECEIVED_ACTION";
	private TextView header_text;
	private ImageView header_lbtn_img, header_rbtn_img;
	TextView contView, timView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_notifi_jpush);
		gettitle();
		contView = (TextView) findViewById(R.id.jpush_content);
		timView = (TextView) findViewById(R.id.jpush_time);
		int userid =HP_User.getOnLineUserId(this);
		Intent intent = getIntent();
		if (null != intent) {
			Bundle bundle = getIntent().getExtras();
			String title = bundle.getString("title");
			String content = bundle.getString("content");
			long time=bundle.getLong("time");
			System.out.println("页面内容" + content);
			contView.setText(content);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String date = sdf.format(new Date(time));
			timView.setText(date);
			HP_DBModel.getInstance(this).updateJPushReadFlag(userid, title, content);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		System.out.println("newintent");
	}

	void gettitle() {

		header_text = (TextView) findViewById(R.id.header_text);
		header_lbtn_img = (ImageView) findViewById(R.id.header_lbtn_img);
		header_rbtn_img = (ImageView) findViewById(R.id.header_rbtn_img);
		header_rbtn_img.setVisibility(View.GONE);
		header_text.setText("推送通知");
		header_lbtn_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (FirstActivity.isForeground) {
					Intent intent = new Intent(TempActivity.this, FirstActivity.class);
					startActivity(intent);
				}
				finish();
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		if (FirstActivity.isForeground) {
			Intent intent = new Intent(TempActivity.this, FirstActivity.class);
			startActivity(intent);
		}

		finish();
	}

	public static boolean isServiceStarted(Context context, String PackageName) {
		boolean isStarted = false;
		try {
			ActivityManager mActivityManager = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			int intGetTastCounter = 1000;
			List<ActivityManager.RunningServiceInfo> mRunningService = mActivityManager
					.getRunningServices(intGetTastCounter);
			for (ActivityManager.RunningServiceInfo amService : mRunningService) {
				if (0 == amService.service.getPackageName().compareTo(
						PackageName)) {
					isStarted = true;
					System.out.println("情动了");
					break;
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return isStarted;
	}
}
