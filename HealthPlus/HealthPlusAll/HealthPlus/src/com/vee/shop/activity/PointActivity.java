package com.vee.shop.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;

import com.vee.healthplus.R;
import com.vee.shop.ui.PointListFragment;
import com.vee.shop.ui.PointListFragment.OnPointListListener;
import com.vee.shop.ui.PointOrderFragment;
import com.vee.shop.util.ApplicationUtils;
import com.yunfox.s4aservicetest.response.OrderPointBean;

public class PointActivity extends BaseHeaderActivity implements
		OnPointListListener {

	private static final String FRAGMENT_TAG_LIST = "point_fragment_tag_list";
	private static final String FRAGMENT_TAG_ORDER = "point_fragment_tag_order";
	private FragmentManager localFragmentManager;
	private PointListFragment mPointListFragment;
	private PointOrderFragment mPointOrderFragment;
	private boolean isList = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setUpTitleBar();
		setUpFragments();
	}

	private void setUpTitleBar() {
		setRightBtnVisible(View.INVISIBLE);
		setLeftBtnClickListenter(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isList) {
					finish();
				} else {
					FragmentTransaction localFragmentTransaction1 = localFragmentManager
							.beginTransaction();
					if (mPointListFragment == null) {
						mPointListFragment = new PointListFragment();
					}
					localFragmentTransaction1.replace(
							ApplicationUtils.getResId("id", "container"),
							mPointListFragment);
					isList = true;
					if (!(localFragmentTransaction1.isEmpty())) {
						localFragmentTransaction1.commitAllowingStateLoss();
						getSupportFragmentManager()
								.executePendingTransactions();
					}
				}
			}
		});
		updateHeaderTitle(getResources().getString(R.string.shop_account_point));
	}

	@Override
	public void onBackPressed() {
		if (isList) {
			super.onBackPressed();
			return;
		} else {
			FragmentTransaction localFragmentTransaction1 = localFragmentManager
					.beginTransaction();
			if (mPointListFragment == null) {
				mPointListFragment = new PointListFragment();
			}
			localFragmentTransaction1.replace(
					ApplicationUtils.getResId("id", "container"),
					mPointListFragment);
			isList = true;
			if (!(localFragmentTransaction1.isEmpty())) {
				localFragmentTransaction1.commitAllowingStateLoss();
				getSupportFragmentManager().executePendingTransactions();
			}
		}
	}

	private void setUpFragments() {
		localFragmentManager = getSupportFragmentManager();
		FragmentTransaction localFragmentTransaction = localFragmentManager
				.beginTransaction();
		mPointListFragment = (PointListFragment) localFragmentManager
				.findFragmentByTag(FRAGMENT_TAG_LIST);
		if (mPointListFragment == null) {
			mPointListFragment = new PointListFragment();
			localFragmentTransaction.add(
					ApplicationUtils.getResId("id", "container"),
					mPointListFragment, FRAGMENT_TAG_LIST);
		}
		isList = true;
		if (!(localFragmentTransaction.isEmpty())) {
			localFragmentTransaction.commitAllowingStateLoss();
			getSupportFragmentManager().executePendingTransactions();
		}
	}

	@Override
	public void OnPointListItemClick(OrderPointBean pointOrder) {

		FragmentTransaction localFragmentTransaction1 = localFragmentManager
				.beginTransaction();
		mPointOrderFragment = (PointOrderFragment) localFragmentManager
				.findFragmentByTag(FRAGMENT_TAG_ORDER);
		if (mPointOrderFragment == null) {
			mPointOrderFragment = new PointOrderFragment();
			Bundle bundle = new Bundle();
			bundle.putSerializable("selected_pointorder", pointOrder);
			mPointOrderFragment.setArguments(bundle);
		}
		localFragmentTransaction1.replace(
				ApplicationUtils.getResId("id", "container"),
				mPointOrderFragment);
		isList = false;
		if (!(localFragmentTransaction1.isEmpty())) {
			localFragmentTransaction1.commitAllowingStateLoss();
			getSupportFragmentManager().executePendingTransactions();
		}

	}
}
