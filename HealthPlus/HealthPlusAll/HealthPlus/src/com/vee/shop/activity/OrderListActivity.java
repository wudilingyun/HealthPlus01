package com.vee.shop.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;

import com.vee.healthplus.R;
import com.vee.shop.ui.BaseFragment;
import com.vee.shop.ui.OrderDetailFragment;
import com.vee.shop.ui.OrderDetailFragment.OnOrderCancelListener;
import com.vee.shop.ui.OrderDetailFragment.OnPayListener;
import com.vee.shop.ui.OrderListFragment;
import com.vee.shop.ui.OrderListFragment.OnListActionListener;
import com.vee.shop.util.ApplicationUtils;

public class OrderListActivity extends BaseHeaderActivity implements
		OnListActionListener, OnPayListener, OnOrderCancelListener {

	private static final String FRAGMENT_TAG_LIST = "order_fragment_tag_list";
	private static final String FRAGMENT_TAG_DETAIL = "order_fragment_tag_detail";
	private BaseFragment mListFragment;
	private BaseFragment mDetailFragment;

	private FragmentManager localFragmentManager;
	private boolean isList = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setUpTitleBar();
		setUpFragments();
	}

	private void setUpTitleBar() {
		setLeftBtnClickListenter(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isList) {
					finish();
				} else {
					FragmentTransaction localFragmentTransaction1 = localFragmentManager
							.beginTransaction();
					if (mListFragment == null) {
						mListFragment = new OrderListFragment();
					}
					localFragmentTransaction1.replace(
							ApplicationUtils.getResId("id", "container"),
							mListFragment);
					isList = true;
					if (!(localFragmentTransaction1.isEmpty())) {
						localFragmentTransaction1.commitAllowingStateLoss();
						getSupportFragmentManager()
								.executePendingTransactions();
					}
				}
			}
		});
		updateHeaderTitle(getResources().getString(
				R.string.account_my_order_list));
		setRightBtnVisible(View.INVISIBLE);
	}

	private void setUpFragments() {
		localFragmentManager = getSupportFragmentManager();
		FragmentTransaction localFragmentTransaction1 = localFragmentManager
				.beginTransaction();

		mListFragment = (BaseFragment) localFragmentManager
				.findFragmentByTag(FRAGMENT_TAG_LIST);
		if (mListFragment == null) {
			mListFragment = new OrderListFragment();
			localFragmentTransaction1.add(
					ApplicationUtils.getResId("id", "container"),
					mListFragment, FRAGMENT_TAG_LIST);
		}
		isList = true;
		if (!(localFragmentTransaction1.isEmpty())) {
			localFragmentTransaction1.commitAllowingStateLoss();
			getSupportFragmentManager().executePendingTransactions();
		}
	}

	@Override
	public void onBackPressed() {
		if (isList) {
			super.onBackPressed();
			return;
		} else {
			FragmentTransaction localFragmentTransaction1 = localFragmentManager
					.beginTransaction();
			if (mListFragment == null) {
				mListFragment = new OrderListFragment();
			}
			localFragmentTransaction1
					.replace(ApplicationUtils.getResId("id", "container"),
							mListFragment);
			isList = true;
			if (!(localFragmentTransaction1.isEmpty())) {
				localFragmentTransaction1.commitAllowingStateLoss();
				getSupportFragmentManager().executePendingTransactions();
			}
		}
	}

	@Override
	public void OnListItemClick(String url, String id) {
		FragmentTransaction localFragmentTransaction1 = localFragmentManager
				.beginTransaction();
		mDetailFragment = (BaseFragment) localFragmentManager
				.findFragmentByTag(FRAGMENT_TAG_DETAIL);
		if (mDetailFragment == null) {
			mDetailFragment = new OrderDetailFragment();
			Bundle bundle = new Bundle();
			bundle.putString("orderlist_detail_url", url);
			bundle.putString("orderlist_order_id", id);
			mDetailFragment.setArguments(bundle);

		}
		localFragmentTransaction1.replace(
				ApplicationUtils.getResId("id", "container"), mDetailFragment);
		isList = false;
		if (!(localFragmentTransaction1.isEmpty())) {
			localFragmentTransaction1.commitAllowingStateLoss();
			getSupportFragmentManager().executePendingTransactions();
		}
	}

	@Override
	public void OnCancel() {
		FragmentTransaction localFragmentTransaction1 = localFragmentManager
				.beginTransaction();
		if (mListFragment == null) {
			mListFragment = new OrderListFragment();
		}
		localFragmentTransaction1.replace(
				ApplicationUtils.getResId("id", "container"), mListFragment);
		isList = true;
		if (!(localFragmentTransaction1.isEmpty())) {
			localFragmentTransaction1.commitAllowingStateLoss();
			getSupportFragmentManager().executePendingTransactions();
		}
	}

	@Override
	public void OnPaySuc() {
		FragmentTransaction localFragmentTransaction1 = localFragmentManager
				.beginTransaction();
		if (mListFragment == null) {
			mListFragment = new OrderListFragment();
		}
		localFragmentTransaction1.replace(
				ApplicationUtils.getResId("id", "container"), mListFragment);
		isList = true;
		if (!(localFragmentTransaction1.isEmpty())) {
			localFragmentTransaction1.commitAllowingStateLoss();
			getSupportFragmentManager().executePendingTransactions();
		}
	}
}
