package com.vee.shop.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.vee.healthplus.R;
import com.vee.shop.ui.BaseFragment;
import com.vee.shop.ui.StationListFragment;

public class ServiceStationActivity extends BaseHeaderActivity {
	private static final String TAG = "ServiceStationActivity";

	private static final String FRAGMENT_TAG_LIST = "station_fragment_tag_list";
	private BaseFragment mListFragment;
	private FragmentManager localFragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		updateHeaderTitle(getResources().getString(
				R.string.shop_account_hotline));
		setRightBtnVisible(View.INVISIBLE);
		setUpFragments();
	}

	private void setUpFragments() {
		localFragmentManager = getSupportFragmentManager();
		FragmentTransaction localFragmentTransaction1 = localFragmentManager
				.beginTransaction();

		mListFragment = (BaseFragment) localFragmentManager
				.findFragmentByTag(FRAGMENT_TAG_LIST);
		if (mListFragment == null) {
			mListFragment = new StationListFragment();
			localFragmentTransaction1.add(R.id.container, mListFragment,
					FRAGMENT_TAG_LIST);
		}
		if (!(localFragmentTransaction1.isEmpty())) {
			localFragmentTransaction1.commitAllowingStateLoss();
			getSupportFragmentManager().executePendingTransactions();
		}
	}
}
