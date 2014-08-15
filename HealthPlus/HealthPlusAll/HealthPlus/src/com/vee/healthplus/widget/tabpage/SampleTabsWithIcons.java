package com.vee.healthplus.widget.tabpage;

import java.lang.reflect.Field;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.vee.healthplus.R;
import com.vee.healthplus.activity.BaseFragmentActivity;
import com.vee.healthplus.common.IFragmentMsg;
import com.vee.healthplus.ui.heahth_news.Health_ValuableBookActivity;
import com.vee.healthplus.ui.heahth_news.Health_ValueBookListFragment;
import com.vee.healthplus.ui.setting.UserPageFragment;
import com.vee.healthplus.util.SystemMethod;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.widget.HeaderView;
import com.vee.moments.MomentsFragment;
import com.vee.myhealth.ui.MyhealthFragment;
import com.vee.shop.ui.AllProductFragment;

public class SampleTabsWithIcons extends Fragment {

	public static final String TAG = "xuxuxu";

	private static String[] mContent;

	private static String[] mIcon;

	private static int[] mIconId;

	private static FragmentPagerAdapter adapter;

	private static Fragment[] fragments = new Fragment[4];

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	public static SampleTabsWithIcons newInstance() {
		return new SampleTabsWithIcons();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "SampleTabsWithIcons.onCreate");
		mContent = SystemMethod.getStringArray(this.getActivity(),
				R.array.doctor_tab_name);
		mIcon = SystemMethod.getStringArray(this.getActivity(),
				R.array.tab_icons);
		/*fragments = new Fragment[] { MyhealthFragment.newInstance(),
				MomentsFragment.newInstance(),
				Health_ValueBookListFragment.newInstance(),
				UserPageFragment.newInstance() };*/

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.i(TAG, "SampleTabsWithIcons.onCreateView");
		View view = inflater.inflate(R.layout.simple_tabs, container, false);
		adapter = new GoogleMusicAdapter(this.getFragmentManager());
		ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
		pager.removeAllViews();
		pager.setAdapter(adapter);
		pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int i, float v, int i2) {
				Log.i("pager", "onPageScrolled");
			}

			@Override
			public void onPageSelected(int i) {
				switch (i) {
				case 0:
					// mFragmentMsgCallBack.updateHeaderTitle();
					Log.i("pager", "onPageSelected=0");
					break;
				case 1:
					Log.i("pager", "onPageSelected=1");
					break;
				case 2:
					Log.i("pager", "onPageSelected=2");
					break;
				case 3:
					Log.i("pager", "onPageSelected=3");
					break;
				}
			}

			@Override
			public void onPageScrollStateChanged(int i) {

			}
		});
		TabPageIndicator indicator = (TabPageIndicator) view
				.findViewById(R.id.indicator);
		indicator.setViewPager(pager);
		indicator.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {

				Log.i("pager", "indicator.onPageSelected()" + position);
				((BaseFragmentActivity) getActivity())
						.setRightBtnVisible(View.GONE);
				((BaseFragmentActivity) getActivity())
						.setRightTvVisible(View.GONE);

				((BaseFragmentActivity) getActivity()).getHeaderView()
						.setLeftOption(HeaderView.HEADER_LOGO);
				((BaseFragmentActivity) getActivity()).getHeaderView()
						.setLeftRes(R.drawable.healthplus_headview_logo_btn);
				((BaseFragmentActivity) getActivity()).getHeaderView()
						.setHeaderTitle(
								getActivity().getString(R.string.hp_app_name));

				if (position == 0) {
					if ((getActivity()) instanceof BaseFragmentActivity) {
						Activity activity = getActivity();
						((BaseFragmentActivity) activity)
								.setRightBtnVisible(View.GONE);
						((BaseFragmentActivity) getActivity())
								.setRightTvVisible(View.GONE);

					}
				} else if (position == 1) {
					if ((getActivity()) instanceof BaseFragmentActivity) {
						Activity activity = getActivity();
						((BaseFragmentActivity) activity)
								.setRightBtnVisible(View.GONE);
						((BaseFragmentActivity) getActivity())
								.setRightTvVisible(View.GONE);
					}

				} else if (position == 2) {
					if ((getActivity()) instanceof BaseFragmentActivity) {
						Activity activity = getActivity();
						((BaseFragmentActivity) activity)
								.setRightBtnVisible(View.GONE);
						((BaseFragmentActivity) getActivity())
								.setRightTvVisible(View.GONE);
					}

				}

				else if (position == 3) {
					if ((getActivity()) instanceof BaseFragmentActivity) {
						Activity activity = getActivity();
						if (HP_User.getOnLineUserId(activity) != 0) {
							((BaseFragmentActivity) activity)
									.setRightBtnVisible(View.GONE);
						} else {

						}
					}

				} else {
					if ((getActivity()) instanceof BaseFragmentActivity) {
						Activity activity = getActivity();
						((BaseFragmentActivity) activity)
								.setRightBtnVisible(View.GONE);
						((BaseFragmentActivity) activity)
								.setRightTvVisible(View.GONE);
					}

				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		return view;
	}

	class GoogleMusicAdapter extends FragmentPagerAdapter implements
			IconPagerAdapter {
		public GoogleMusicAdapter(FragmentManager fm) {
			super(fm);
			Log.i(TAG, "GoogleMusicAdapter.GoogleMusicAdapter=");
			FragmentTransaction fragmentTransaction = fm.beginTransaction();
			
			for (int i = 0; i < fragments.length; i++) {
				if (fragments[i] == null)
					continue;
				fragmentTransaction.remove(fragments[i]);
				Log.e("xuxuxu", fragments[i].getClass() + "");
			}
			fragmentTransaction.commit();

		}
		
		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;
			Log.i(TAG, "GoogleMusicAdapter.getItem=" + position);
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
			fragments[position] = fragment;
			return fragment;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return mContent[position % mContent.length].toUpperCase();
		}

		@Override
		public int getIconResId(int index) {
			return getNewId("drawable", mIcon[index]);
		}

		@Override
		public int getCount() {
			return mContent.length;
		}

		public Integer getNewId(String type, String filed) {
			String RClassName = "com.vee.healthplus.R";
			Object oj = null;
			try {
				Class<?> c = Class.forName(RClassName + "$" + type);
				Field f = c.getField(filed);
				oj = f.get(c.newInstance());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return Integer.parseInt(oj.toString());
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

}
