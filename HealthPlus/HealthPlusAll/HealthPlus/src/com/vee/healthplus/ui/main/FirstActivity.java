package com.vee.healthplus.ui.main;

import java.util.ArrayList;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;

import com.androidquery.util.Common;
import com.vee.healthplus.R;
import com.vee.healthplus.common.MyApplication;
import com.vee.healthplus.ui.user.HealthPlusLoginActivity;
import com.vee.healthplus.util.AppPreferencesUtil;
import com.vee.healthplus.util.InstallSataUtil;
import com.vee.healthplus.util.VersionUtils;
import com.vee.healthplus.util.user.HP_DBCommons;
import com.vee.healthplus.util.user.HP_DBHelper;
import com.vee.healthplus.util.user.HP_DBModel;
import com.vee.healthplus.util.user.HP_User;
import com.vee.myhealth.util.DBManager;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class FirstActivity extends Activity {
	private SQLiteDatabase dbHelper;
	private SQLiteDatabase database;
	public static boolean isForeground = true;

	private ViewPager vPager;
	private ArrayList<View> data;
	private ViewGroup main; // 主界面父容器
	private ViewGroup group; // 圆点父容器
	private ViewGroup textGroup;
	private TextView tv; // 圆点
	private TextView[] tvs; // 一排圆点
	private TextView text; // 上方字
	// private int[] imgViews;
	private String[] textViews;

	private View view1, view2, view3; // 图片View
	int version;
	Handler handler = new Handler(); 
	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (version != getVerCode(FirstActivity.this)) {
				System.out.println("新版本第一次运行");
				// handler.removeCallbacks(runnable);
				Intent intent = new Intent(FirstActivity.this, GuidePage.class);
				startActivity(intent);
				finish();
			} else {

					Intent intent;
					if (HP_User.getOnLineUserId(getApplication()) == 0) {
						intent = new Intent(FirstActivity.this,
								HealthPlusLoginActivity.class);
						Bundle extras = new Bundle();
						extras.putParcelable("cn", new ComponentName(
								"com.vee.healthplus",
								"com.vee.healthplus.ui.main.MainPage"));
						intent.putExtras(extras);
					} else {
						intent = new Intent(FirstActivity.this, MainPage.class);
					}
					startActivity(intent);
					finish();

			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		/*
		 * 添加首次导航
		 */
		AppPreferencesUtil.setBooleanPref(
				this, "isFirstShowLogin", true);//第一次进入login界面
		AppPreferencesUtil.setBooleanPref(
				this, "isFirstClickModule", true);//未点击模块
		AppPreferencesUtil.setBooleanPref(
				this, "isFirstClickTest", true);//未点击测试项
		setContentView(R.layout.welcome_page_activity);
		handler.postDelayed(runnable, 3000);
		init();
		version = AppPreferencesUtil.getIntPref(this, "version", 0);
		
	}

	void init() {
		ShareSDK.initSDK(this);
		startService();
		addAllDB();
		new InstallSataUtil(this).serverStat(com.vee.healthplus.common.Common.getAppId(this));
	}

	private void startService() {

	}

	void addAllDB() {
		dbHelper = new DBManager(this, DBManager.DB_NAME, null, 6)// 首页测试题数据库
				.getWritableDatabase();
		dbHelper.close();
		HP_DBModel.getInstance(this);
		// dbHelper.openDatabase();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isForeground = false;
		JPushInterface.onResume(this);
		
	
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		isForeground = false;
		JPushInterface.onPause(this);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		if (version != getVerCode(this)) {
			handler.removeCallbacks(runnable);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		isForeground = false;
	}

	public int getVerCode(Context context) {
		int verCode = -1;
		try {
			verCode = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
		}
		return verCode;
	}

}
