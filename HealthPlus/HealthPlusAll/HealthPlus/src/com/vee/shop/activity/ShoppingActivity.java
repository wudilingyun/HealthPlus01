package com.vee.shop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.vee.healthplus.R;
import com.vee.healthplus.common.MyApplication;
import com.vee.shop.bean.CartItemBean;
import com.vee.shop.ui.BaseFragment;
import com.vee.shop.ui.ShoppingEditFragment;
import com.vee.shop.ui.ShoppingEditFragment.OnDeleteListener;
import com.vee.shop.ui.ShoppingListFragment;
import com.vee.shop.ui.ShoppingListFragment.OnButtonClickListener;
import com.vee.shop.ui.ShoppingListFragment.OnListActionListener;
import com.vee.shop.util.ApplicationUtils;
import com.vee.shop.util.Constants;

public class ShoppingActivity extends BaseHeaderActivity implements
		OnListActionListener, OnDeleteListener, OnButtonClickListener {

	private static final String TAG = "ShoppingActivity";

	private static final String FRAGMENT_TAG_LIST = "shopping_fragment_tag_list";
	private static final String FRAGMENT_TAG_EDIT = "shopping_fragment_tag_edit";
	private BaseFragment mListFragment;
	private BaseFragment mEditFragment;
	private FragmentManager localFragmentManager;

	private boolean isList = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setUpTitleBar();
		setUpFragments();
	}

	private void setUpFragments() {
		localFragmentManager = getSupportFragmentManager();
		FragmentTransaction localFragmentTransaction1 = localFragmentManager
				.beginTransaction();

		mListFragment = (BaseFragment) localFragmentManager
				.findFragmentByTag(FRAGMENT_TAG_LIST);
		if (mListFragment == null) {
			mListFragment = new ShoppingListFragment();
			localFragmentTransaction1.add(
					ApplicationUtils.getResId("id", "container"),
					mListFragment, FRAGMENT_TAG_LIST);
		}
		isList = true;
		updateHeaderTitle(getResources().getString(
				R.string.shop_tab_indicator_shopping));
		if (!(localFragmentTransaction1.isEmpty())) {
			localFragmentTransaction1.commitAllowingStateLoss();
			getSupportFragmentManager().executePendingTransactions();
		}
	}

	private void setUpTitleBar() {
		updateHeaderTitle(getResources().getString(
				R.string.shop_tab_indicator_shopping));
		setRightBtnVisible(View.INVISIBLE);
		setLeftBtnClickListenter(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isList) {
					finish();
				} else {
					FragmentTransaction localFragmentTransaction1 = localFragmentManager
							.beginTransaction();
					if (mListFragment == null) {
						mListFragment = new ShoppingListFragment();
					}
					localFragmentTransaction1.replace(
							ApplicationUtils.getResId("id", "container"),
							mListFragment);
					isList = true;
					updateHeaderTitle(getResources().getString(
							R.string.shop_tab_indicator_shopping));
					if (!(localFragmentTransaction1.isEmpty())) {
						localFragmentTransaction1.commitAllowingStateLoss();
						getSupportFragmentManager()
								.executePendingTransactions();
					}
				}
			}
		});
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
				mListFragment = new ShoppingListFragment();
			}
			localFragmentTransaction1
					.replace(ApplicationUtils.getResId("id", "container"),
							mListFragment);
			isList = true;
			updateHeaderTitle(getResources().getString(
					R.string.shop_tab_indicator_shopping));
			if (!(localFragmentTransaction1.isEmpty())) {
				localFragmentTransaction1.commitAllowingStateLoss();
				getSupportFragmentManager().executePendingTransactions();
			}
		}
	}

	@Override
	public void OnListItemClick(CartItemBean item) {
		FragmentTransaction localFragmentTransaction1 = localFragmentManager
				.beginTransaction();
		mEditFragment = (BaseFragment) localFragmentManager
				.findFragmentByTag(FRAGMENT_TAG_EDIT);
		if (mEditFragment == null) {
			mEditFragment = new ShoppingEditFragment();
			Bundle bundle = new Bundle();
			bundle.putParcelable("cart_list_item_choosed", item);
			mEditFragment.setArguments(bundle);
			updateHeaderTitle(getResources().getString(
					R.string.shop_cart_modify));
		}
		localFragmentTransaction1.replace(
				ApplicationUtils.getResId("id", "container"), mEditFragment);
		isList = false;
		if (!(localFragmentTransaction1.isEmpty())) {
			localFragmentTransaction1.commitAllowingStateLoss();
			getSupportFragmentManager().executePendingTransactions();
		}
	}

	@Override
	public void OnDelte() {
		FragmentTransaction localFragmentTransaction1 = localFragmentManager
				.beginTransaction();
		if (mListFragment == null) {
			mListFragment = new ShoppingListFragment();
		}
		localFragmentTransaction1.replace(
				ApplicationUtils.getResId("id", "container"), mListFragment);
		isList = true;
		updateHeaderTitle(getResources().getString(
				R.string.shop_tab_indicator_shopping));
		if (!(localFragmentTransaction1.isEmpty())) {
			localFragmentTransaction1.commitAllowingStateLoss();
			getSupportFragmentManager().executePendingTransactions();
		}
	}

	@Override
	public void OnButtonClick() {
		if (MyApplication.getCartNum() > 0) {
			Intent intent = new Intent();
			intent.setClass(this, CheckoutActivity.class);
			startActivityForResult(intent, Constants.CHECKOU_REQUESTCODE);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_OK:
			Log.d(TAG, "onActivityResult ok");
			finish();
			break;
		case RESULT_CANCELED:
			Log.d(TAG, "onActivityResult cancel");
			FragmentTransaction localFragmentTransaction1 = localFragmentManager
					.beginTransaction();
			mListFragment = new ShoppingListFragment();
			localFragmentTransaction1
					.replace(ApplicationUtils.getResId("id", "container"),
							mListFragment);
			isList = true;
			updateHeaderTitle(getResources().getString(
					R.string.shop_tab_indicator_shopping));
			if (!(localFragmentTransaction1.isEmpty())) {
				localFragmentTransaction1.commitAllowingStateLoss();
				getSupportFragmentManager().executePendingTransactions();
			}
			break;
		default:
			Log.d(TAG, "onActivityResult default");
			finish();
			break;
		}
	}
}
