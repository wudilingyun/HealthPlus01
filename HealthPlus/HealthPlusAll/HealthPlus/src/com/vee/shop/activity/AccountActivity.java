package com.vee.shop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;

import com.vee.healthplus.R;
import com.vee.healthplus.common.MyApplication;
import com.vee.shop.ui.AccountFragment;
import com.vee.shop.ui.BaseFragment;

public class AccountActivity extends BaseHeaderActivity {
	private static final String TAG = "AccountActivity";
	private static final String FRAGMENT_TAG_ACCOUT = "account_fragment_tag_list";
	private BaseFragment mAccountFragment;
	private FragmentManager localFragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setUpTitleBar();
		setUpFragments();
	}

	@Override
	protected void onResume() {
		super.onResume();
		int count = MyApplication.getCartNum();
		if (count > 0) {
			setCountVisible(View.VISIBLE);
			updateCartCount(count);
			// Animation shake = AnimationUtils.loadAnimation(this,
			// ApplicationUtils.getResId("anim", "shop_shake"));
		} else {
			setCountVisible(View.INVISIBLE);
		}
	}

	private void setUpTitleBar() {
		updateHeaderTitle(getString(R.string.shop_title));
		setRightBtnClickListenter(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, ShoppingActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}

		});
	}

	private void setUpFragments() {
		localFragmentManager = getSupportFragmentManager();
		FragmentTransaction localFragmentTransaction1 = localFragmentManager
				.beginTransaction();

		mAccountFragment = (BaseFragment) localFragmentManager
				.findFragmentByTag(FRAGMENT_TAG_ACCOUT);
		if (mAccountFragment == null) {
			mAccountFragment = new AccountFragment();
			localFragmentTransaction1.add(R.id.container, mAccountFragment,
					FRAGMENT_TAG_ACCOUT);
		}
		if (!(localFragmentTransaction1.isEmpty())) {
			localFragmentTransaction1.commitAllowingStateLoss();
			getSupportFragmentManager().executePendingTransactions();
		}
	}
}
