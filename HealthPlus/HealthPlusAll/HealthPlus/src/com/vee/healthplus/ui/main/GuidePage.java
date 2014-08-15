package com.vee.healthplus.ui.main;

import java.util.ArrayList;

import com.vee.healthplus.R;
import com.vee.healthplus.ui.user.HealthPlusLoginActivity;
import com.vee.healthplus.util.AppPreferencesUtil;
import com.vee.healthplus.util.user.HP_User;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

public class GuidePage extends FragmentActivity{
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
	private ViewPagerAdapter adatper; // ViewPager适配器
	private View view1, view2, view3; //
	private LayoutInflater inflater;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		 inflater = getLayoutInflater();
		main = (ViewGroup) inflater.inflate(R.layout.switch_activity, null);
		setContentView(main);
		init();
		AppPreferencesUtil.setIntPref(this, "version", getVerCode(this));
	}
	
	void init(){
		data = new ArrayList<View>();

		view1 = inflater.inflate(R.layout.switch_item, null);
		ImageView img1 = (ImageView) view1.findViewById(R.id.switch_img);
		img1.setImageResource(R.drawable.navigation_one);
		img1.setScaleType(ImageView.ScaleType.FIT_XY);

		view2 = inflater.inflate(R.layout.switch_item, null);
		ImageView img2 = (ImageView) view2.findViewById(R.id.switch_img);
		img2.setImageResource(R.drawable.navigation_two);
		img2.setScaleType(ImageView.ScaleType.FIT_XY);
		// img2.setLeft(150);

		view3 = inflater.inflate(R.layout.switch_item, null);
		ImageView img3 = (ImageView) view3.findViewById(R.id.switch_img);
		ImageView login = (ImageView) view3.findViewById(R.id.tiyan);
		// 添加最后一项点击按钮事件
		img3.setImageResource(R.drawable.navigation_three);
		img3.setScaleType(ImageView.ScaleType.FIT_XY);

		data.add(view1);
		data.add(view2);
		data.add(view3);
		login.setVisibility(View.VISIBLE);
		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent;
				if (HP_User.getOnLineUserId(getApplication()) == 0) {
					intent = new Intent(GuidePage.this,HealthPlusLoginActivity.class);
					Bundle extras = new Bundle();
					extras.putParcelable("cn", new ComponentName(
							"com.vee.healthplus",
							"com.vee.healthplus.ui.main.MainPage"));
					intent.putExtras(extras);
				} else {
					intent = new Intent(GuidePage.this, MainPage.class);
				}

				startActivity(intent);
				finish();
			}
		});

		// 下方圆点
		tvs = new TextView[data.size()];
		
		group = (ViewGroup) main.findViewById(R.id.viewGroup);
		vPager = (ViewPager) main.findViewById(R.id.viewPager);
		TextView tvbleak;

		for (int i = 0; i < data.size(); i++) {
			tv = new TextView(GuidePage.this);
			tvbleak = new TextView(GuidePage.this);
			tvbleak.setLayoutParams(new LayoutParams(20, 20));
			tvbleak.setBackgroundColor(0x00000000);
			tv.setLayoutParams(new LayoutParams(20, 20));
			tv.setPadding(0, 0, 0, 50);
			tvs[i] = tv;
			if (i == 0) {
				tvs[i].setBackgroundResource(R.drawable.guide_dot_green);
			} else {
				tvs[i].setBackgroundResource(R.drawable.guide_dot_white);
			}
			group.addView(tvs[i]);
			group.addView(tvbleak);
		}
		
	
		adatper = new ViewPagerAdapter(data);
		vPager.setAdapter(adatper);
		vPager.setOnPageChangeListener(new PagerListener());
	}
	
	class PagerListener implements OnPageChangeListener {

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		public void onPageSelected(int arg0) {
			for (int i = 0; i < tvs.length; i++) {
				tvs[arg0].setBackgroundResource(R.drawable.guide_dot_green);
				if (arg0 != i) {
					tvs[i].setBackgroundResource(R.drawable.guide_dot_white);
				}
			}

			if (arg0 == tvs.length - 1) {
				tvs[0].setVisibility(View.VISIBLE);
				tvs[1].setVisibility(View.VISIBLE);
				tvs[2].setVisibility(View.VISIBLE);
			} else {
				tvs[0].setVisibility(View.VISIBLE);
				tvs[1].setVisibility(View.VISIBLE);
				tvs[2].setVisibility(View.VISIBLE);
			}
		}
	}
	
	public class ViewPagerAdapter extends PagerAdapter {
		ArrayList<View> data;

		public ViewPagerAdapter(ArrayList<View> data) {
			this.data = data;
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			// TODO Auto-generated method stub
			((ViewPager) arg0).removeView(data.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			// TODO Auto-generated method stub
			((ViewPager) arg0).addView(data.get(arg1));
			return data.get(arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
			// TODO Auto-generated method stub

		}
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
