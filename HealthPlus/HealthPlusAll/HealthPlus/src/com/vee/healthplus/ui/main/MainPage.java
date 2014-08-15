package com.vee.healthplus.ui.main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.vee.healthplus.R;
import com.vee.healthplus.common.FragmentMsg;
import com.vee.healthplus.common.IFragmentMsg;
import com.vee.healthplus.http.StatisticsUtils;
import com.vee.healthplus.ui.heahth_news.Health_ValueBookListFragment;
import com.vee.healthplus.ui.setting.UserPageFragment;
import com.vee.healthplus.ui.user.HealthPlusLoginActivity;
import com.vee.healthplus.util.AppPreferencesUtil;
import com.vee.healthplus.util.VersionUtils;
import com.vee.healthplus.util.user.HP_DBModel;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.widget.HeaderView;
import com.vee.moments.MomentsFragment;
import com.vee.myhealth.bean.TestCollectinfor;
import com.vee.myhealth.ui.MyhealthFragment;

public class MainPage extends FragmentActivity implements IFragmentMsg,
		TagAliasCallback {
	private final String TAG = "MainPage";
	private Fragment curFragment;
	private List<Fragment> fragments = new ArrayList<Fragment>();
	private Set<String> tags = new HashSet();
	private FragmentManager fragmentManager;
	private ViewPager viewPager;// lingyun modify on github
	private ImageView mTab1, mTab2, mTab3, mTab4;
	private HeaderView hv;
	private ImageView leftBtn;
	private LinearLayout tab1ll, tab2ll, tab3ll, tab4ll;
	private int userId;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		AppPreferencesUtil.setBooleanPref(this, "isFirstShowLogin", false);
		setContentView(R.layout.main_fragment_activity);
		Log.v("zyl","执行oncreate");
		initView();
		userId = HP_User.getOnLineUserId(this);
		StatisticsUtils.startCounts(this);
		VersionUtils.getInstance().checkVersion(MainPage.this);
		Log.v("zyl","执行oncreate_1");
	}

	private void initView() {
		fragments.add(MyhealthFragment.newInstance());
		fragments.add(MomentsFragment.newInstance());
		fragments.add(Health_ValueBookListFragment.newInstance());
		fragments.add(UserPageFragment.newInstance());
		fragmentManager = this.getSupportFragmentManager();
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		viewPager.setAdapter(new MyFragmentPageAdapter(fragmentManager));
		viewPager.setOffscreenPageLimit(1);
		mTab1 = (ImageView) findViewById(R.id.main_home);
		mTab2 = (ImageView) findViewById(R.id.main_friends);
		mTab3 = (ImageView) findViewById(R.id.main_news);
		mTab4 = (ImageView) findViewById(R.id.main_wo);
		tab1ll = (LinearLayout) findViewById(R.id.tab1Layout);
		tab2ll = (LinearLayout) findViewById(R.id.tab2Layout);
		tab3ll = (LinearLayout) findViewById(R.id.tab3Layout);
		tab4ll = (LinearLayout) findViewById(R.id.tab4Layout);
		mTab1.setImageDrawable(getResources().getDrawable(
				R.drawable.tab_sport_selected));
		mTab2.setImageDrawable(getResources().getDrawable(
				R.drawable.tab_shop_normal));
		mTab3.setImageDrawable(getResources().getDrawable(
				R.drawable.tab_user_normal));
		mTab4.setImageDrawable(getResources().getDrawable(
				R.drawable.tab_more_normal));
		tab1ll.setOnClickListener(new MyOnClickListener(0));
		tab2ll.setOnClickListener(new MyOnClickListener(1));
		tab3ll.setOnClickListener(new MyOnClickListener(2));
		tab4ll.setOnClickListener(new MyOnClickListener(3));

		// updateFragmentToStack(SampleTabsWithIcons.newInstance());
		hv = (HeaderView) findViewById(R.id.header);
		leftBtn = (ImageView) findViewById(R.id.header_lbtn_img);
		leftBtn.setVisibility(View.VISIBLE);
		hv.setLeftRes(R.drawable.healthplus_headview_logo_btn);
		hv.setLeftOption(2);
	}

	void addTagForJPush() {
		if (userId != 0) {
			List<TestCollectinfor> TagList = HP_DBModel.getInstance(this)
					.queryUserTestList(userId);
			if (TagList != null && TagList.size() > 0) {
				for (int i = 0; i < TagList.size(); i++) {
					String s = TagList.get(i).getName()
							+ TagList.get(i).getResult();
					System.err.println("测试结果" + s);
					tags.add(s);
				}
				JPushInterface.setTags(getApplication(), tags, this);
			}
		}

	}

	private void updateFragmentToStack(FragmentMsg fMsg) {
		android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager()
				.beginTransaction();
		ft.setCustomAnimations(android.R.anim.slide_in_left,
				android.R.anim.slide_out_right);
		Log.i(TAG, "curFragment is null?" + curFragment);
		if (curFragment != null) {
			ft.remove(curFragment);
			Log.i(TAG, "remove curFragment!");
		}
		curFragment = fMsg.getObjFragment();
		ft.add(R.id.container, fMsg.getObjFragment());
		ft.commit();
	}

	@Override
	public void replaceFragment(FragmentMsg fMsg) {
		// TODO Auto-generated method stub
		Log.i(TAG, "main page replaceFragment");
		updateFragmentToStack(fMsg);
	}

	@Override
	public void updateHeaderTitle(String title) {
		updateHeaderTitle(title);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		AppPreferencesUtil.setBooleanPref(
				this, "isFirstShowLogin", true);
		System.exit(0);
	}

	@Override
	public void onBackPressed() {
		QuitDialog qd = new QuitDialog("提示");
		qd.show(getSupportFragmentManager(), "");
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0:
				mTab1.setImageDrawable(getResources().getDrawable(
						R.drawable.tab_sport_selected));
				mTab2.setImageDrawable(getResources().getDrawable(
						R.drawable.tab_shop_normal));
				mTab3.setImageDrawable(getResources().getDrawable(
						R.drawable.tab_user_normal));
				mTab4.setImageDrawable(getResources().getDrawable(
						R.drawable.tab_more_normal));
				break;
			case 1:
				StatisticsUtils.moduleStatistics(MainPage.this, userId+"", StatisticsUtils.MODULE_HY_ID, StatisticsUtils.MODULE_HY);
				mTab1.setImageDrawable(getResources().getDrawable(
						R.drawable.tab_sport_normal));
				mTab2.setImageDrawable(getResources().getDrawable(
						R.drawable.tab_shop_selected));
				mTab3.setImageDrawable(getResources().getDrawable(
						R.drawable.tab_user_normal));
				mTab4.setImageDrawable(getResources().getDrawable(
						R.drawable.tab_more_normal));
				break;
			case 2:
				StatisticsUtils.moduleStatistics(MainPage.this, userId+"", StatisticsUtils.MODULE_BD_ID, StatisticsUtils.MODULE_BD);
				mTab1.setImageDrawable(getResources().getDrawable(
						R.drawable.tab_sport_normal));
				mTab2.setImageDrawable(getResources().getDrawable(
						R.drawable.tab_shop_normal));
				mTab3.setImageDrawable(getResources().getDrawable(
						R.drawable.tab_user_selected));
				mTab4.setImageDrawable(getResources().getDrawable(
						R.drawable.tab_more_normal));
				break;
			case 3:
				mTab1.setImageDrawable(getResources().getDrawable(
						R.drawable.tab_sport_normal));
				mTab2.setImageDrawable(getResources().getDrawable(
						R.drawable.tab_shop_normal));
				mTab3.setImageDrawable(getResources().getDrawable(
						R.drawable.tab_user_normal));
				mTab4.setImageDrawable(getResources().getDrawable(
						R.drawable.tab_more_selected));
				break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	private class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			viewPager.setCurrentItem(index);
		}
	};

	private class MyPagerAdapter extends PagerAdapter {
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getCount() {
			return fragments.size();
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(fragments.get(position)
					.getView());
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			Log.i(TAG, "MyPagerAdapter.instantiateItem.position=" + position);
			Fragment fragment = fragments.get(position);
			if (!fragment.isAdded()) {
				FragmentTransaction ft = fragmentManager.beginTransaction();
				ft.add(fragment, fragment.getClass().getSimpleName());
				ft.commit();
				/**
				 * 在用FragmentTransaction.commit()方法提交FragmentTransaction对象后
				 * 会在进程的主线程中,用异步的方式来执行。 如果想要立即执行这个等待中的操作,就要调用这个方法(只能在主线程中调用)。
				 * 要注意的是,所有的回调和相关的行为都会在这个调用中被执行完成,因此要仔细确认这个方法的调用位置。
				 */
				fragmentManager.executePendingTransactions();
			}

			// if (fragment.getView().getParent() == null) {
			container.addView(fragment.getView());
			// }
			return fragment.getView();
		}
	}

	class MyFragmentPageAdapter extends FragmentPagerAdapter {
		public MyFragmentPageAdapter(FragmentManager fm) {
			super(fm);
			Log.i(TAG, "MyFragmentPageAdapter.MyFragmentPageAdapter=");

		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;
			Log.i(TAG, "MyFragmentPageAdapter.getItem=" + position);
			switch (position) {
			case 0:
				// fragment = SportModeFragment.newInstance();
				fragment = MyhealthFragment.newInstance();
				break;
			case 1:
				// fragment = HealthFragment.newInstance();
				// fragment = AskWeaknessFragment.NewInstance();
				fragment = MomentsFragment.newInstance();
				break;
			case 2:
				// fragment = Health_ValuableBook_Fragment.newInstance();
				fragment = Health_ValueBookListFragment.newInstance();
				break;
			case 3:
				fragment = UserPageFragment.newInstance();
				break;
			}
			return fragment;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return "PageTitle" + position;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return fragments.size();
		}

	}

	@Override
	public void gotResult(int arg0, String arg1, Set<String> arg2) {
		// TODO Auto-generated method stub

	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.v("zyl","执行onResume");
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.v("zyl","执行onPause");
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.v("zyl","执行onStop");
	}
	

}
