package com.vee.shop.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.vee.healthplus.R;
import com.vee.shop.bean.AddressBean;
import com.vee.shop.ui.AddressEditFragment;
import com.vee.shop.ui.AddressEditFragment.OnAddSucListener;
import com.vee.shop.ui.AddressEditFragment.OnEditTextListener;
import com.vee.shop.ui.AddressListFragment;
import com.vee.shop.ui.AddressListFragment.OnAddressChangeListener;
import com.vee.shop.ui.BaseFragment;
import com.vee.shop.ui.CheckoutFragment;
import com.vee.shop.ui.CheckoutFragment.OnCheckoutListener;
import com.vee.shop.ui.OrderCommitFragment;
import com.vee.shop.ui.OrderCommitFragment.OnOrderListener;
import com.vee.shop.util.ApplicationUtils;
import com.vee.shop.util.Constants;

public class CheckoutActivity extends BaseHeaderActivity implements
		OnCheckoutListener, OnOrderListener, OnAddSucListener,
		OnAddressChangeListener, OnEditTextListener {
	private static final String TAG = "CheckoutActivity";

	private static final String FRAGMENT_TAG_CHECKOUT = "shopping_fragment_tag_checkout";
	private static final String FRAGMENT_TAG_ORDERCOMMIT = "shopping_fragment_tag_ordercommit";
	private static final String FRAGMENT_TAG_ADDADDRESS = "shopping_fragment_tag_addaddress";
	private static final String FRAGMENT_TAG_CHOOSEADDRESS = "shopping_fragment_tag_chooseaddress";

	private FragmentManager localFragmentManager;
	private BaseFragment mCheckoutFragment;
	private BaseFragment mOrderCommitFragment;
	private BaseFragment mAddAddressFragment;
	private BaseFragment mChooseAddressFragment;

	private Boolean isCheckout = true;
	private boolean isEditAddress = false;
	private String payType;
	private boolean isOrderSuc = false;

	private int addressId = 0;

	private EditText et1;
	private EditText et2;
	private EditText et3;
	private EditText et4;

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

		mCheckoutFragment = (BaseFragment) localFragmentManager
				.findFragmentByTag(FRAGMENT_TAG_CHECKOUT);
		if (mCheckoutFragment == null) {
			mCheckoutFragment = new CheckoutFragment();
		}
		Bundle bundle = new Bundle();
		bundle.putInt("address_list_item_choosed", addressId);
		mCheckoutFragment.setArguments(bundle);
		localFragmentTransaction1.add(
				ApplicationUtils.getResId("id", "container"),
				mCheckoutFragment, FRAGMENT_TAG_CHECKOUT);
		isCheckout = true;
		isEditAddress = false;
		updateHeaderTitle(getResources()
				.getString(R.string.shop_checkout_title));
		if (!(localFragmentTransaction1.isEmpty())) {
			localFragmentTransaction1.commitAllowingStateLoss();
			getSupportFragmentManager().executePendingTransactions();
		}
	}

	private void setUpTitleBar() {
		setRightBtnVisible(View.INVISIBLE);
		setLeftBtnClickListenter(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isCheckout) {
					if (isOrderSuc) {
						setResult(RESULT_OK);
					} else {
						setResult(RESULT_CANCELED);
					}
					finish();
				} else {
					FragmentTransaction localFragmentTransaction1 = localFragmentManager
							.beginTransaction();
					if (isEditAddress) {
						clearFocus();
						mChooseAddressFragment = (BaseFragment) localFragmentManager
								.findFragmentByTag(FRAGMENT_TAG_CHOOSEADDRESS);
						if (mChooseAddressFragment == null) {
							mChooseAddressFragment = new AddressListFragment();
						}
						isCheckout = false;
						isEditAddress = false;
						localFragmentTransaction1.replace(
								ApplicationUtils.getResId("id", "container"),
								mChooseAddressFragment);
						updateHeaderTitle(getResources().getString(
								R.string.use_address));
					} else {
						mCheckoutFragment = (BaseFragment) localFragmentManager
								.findFragmentByTag(FRAGMENT_TAG_CHECKOUT);
						if (mCheckoutFragment == null) {
							mCheckoutFragment = new CheckoutFragment();
						}
						Bundle bundle = new Bundle();
						bundle.putInt("address_list_item_choosed", addressId);
						mCheckoutFragment.setArguments(bundle);
						localFragmentTransaction1.replace(
								ApplicationUtils.getResId("id", "container"),
								mCheckoutFragment);
						isCheckout = true;
						isEditAddress = false;
						updateHeaderTitle(getResources().getString(
								R.string.shop_checkout_title));
					}

					if (!(localFragmentTransaction1.isEmpty())) {
						localFragmentTransaction1.commitAllowingStateLoss();
						getSupportFragmentManager()
								.executePendingTransactions();
					}
				}
			}
		});
		updateHeaderTitle(getResources()
				.getString(R.string.shop_checkout_title));
	}

	@Override
	public void onBackPressed() {
		if (isCheckout) {
			if (isOrderSuc) {
				setResult(RESULT_OK);
			} else {
				setResult(RESULT_CANCELED);
			}
			super.onBackPressed();
			return;
		} else {
			FragmentTransaction localFragmentTransaction1 = localFragmentManager
					.beginTransaction();
			if (isEditAddress) {
				clearFocus();
				mChooseAddressFragment = (BaseFragment) localFragmentManager
						.findFragmentByTag(FRAGMENT_TAG_CHOOSEADDRESS);
				if (mChooseAddressFragment == null) {
					mChooseAddressFragment = new AddressListFragment();
				}
				isCheckout = false;
				isEditAddress = false;
				localFragmentTransaction1.replace(
						ApplicationUtils.getResId("id", "container"),
						mChooseAddressFragment);
				updateHeaderTitle(getResources()
						.getString(R.string.use_address));
			} else {
				mCheckoutFragment = (BaseFragment) localFragmentManager
						.findFragmentByTag(FRAGMENT_TAG_CHECKOUT);
				if (mCheckoutFragment == null) {
					mCheckoutFragment = new CheckoutFragment();
				}
				Bundle bundle = new Bundle();
				bundle.putInt("address_list_item_choosed", addressId);
				mCheckoutFragment.setArguments(bundle);
				localFragmentTransaction1.replace(
						ApplicationUtils.getResId("id", "container"),
						mCheckoutFragment);
				isCheckout = true;
				isEditAddress = false;
				updateHeaderTitle(getResources().getString(
						R.string.shop_checkout_title));
			}

			if (!(localFragmentTransaction1.isEmpty())) {
				localFragmentTransaction1.commitAllowingStateLoss();
				getSupportFragmentManager().executePendingTransactions();
			}
		}
	}

	@Override
	public void OnCheckout(String result, String payType) {
		this.payType = payType;
		updateHeaderTitle(getResources().getString(
				R.string.order_submit_button_submit));
		FragmentTransaction localFragmentTransaction1 = localFragmentManager
				.beginTransaction();
		mOrderCommitFragment = (BaseFragment) localFragmentManager
				.findFragmentByTag(FRAGMENT_TAG_ORDERCOMMIT);
		if (mOrderCommitFragment == null) {
			mOrderCommitFragment = new OrderCommitFragment();
			Bundle bundle = new Bundle();
			bundle.putString("checkout_return_string", result);
			mOrderCommitFragment.setArguments(bundle);

		}
		isCheckout = false;
		isEditAddress = false;
		localFragmentTransaction1.replace(
				ApplicationUtils.getResId("id", "container"),
				mOrderCommitFragment);
		if (!(localFragmentTransaction1.isEmpty())) {
			localFragmentTransaction1.commitAllowingStateLoss();
			getSupportFragmentManager().executePendingTransactions();
		}
	}

	@Override
	public void onAddAddress() {
		updateHeaderTitle(getResources().getString(
				R.string.shop_checkout_add_address));
		FragmentTransaction localFragmentTransaction1 = localFragmentManager
				.beginTransaction();
		mAddAddressFragment = (BaseFragment) localFragmentManager
				.findFragmentByTag(FRAGMENT_TAG_ADDADDRESS);
		if (mAddAddressFragment == null) {
			mAddAddressFragment = new AddressEditFragment();
			Bundle bundle = new Bundle();
			bundle.putParcelable("address_list_item_choosed", null);
			mAddAddressFragment.setArguments(bundle);
		}
		isCheckout = false;
		isEditAddress = true;
		localFragmentTransaction1.replace(
				ApplicationUtils.getResId("id", "container"),
				mAddAddressFragment);
		if (!(localFragmentTransaction1.isEmpty())) {
			localFragmentTransaction1.commitAllowingStateLoss();
			getSupportFragmentManager().executePendingTransactions();
		}

	}

	@Override
	public void onChooseAddress() {
		updateHeaderTitle(getResources().getString(R.string.use_address));
		FragmentTransaction localFragmentTransaction1 = localFragmentManager
				.beginTransaction();
		mChooseAddressFragment = (BaseFragment) localFragmentManager
				.findFragmentByTag(FRAGMENT_TAG_CHOOSEADDRESS);
		if (mChooseAddressFragment == null) {
			mChooseAddressFragment = new AddressListFragment();
		}
		isCheckout = false;
		isEditAddress = false;
		localFragmentTransaction1.replace(
				ApplicationUtils.getResId("id", "container"),
				mChooseAddressFragment);
		if (!(localFragmentTransaction1.isEmpty())) {
			localFragmentTransaction1.commitAllowingStateLoss();
			getSupportFragmentManager().executePendingTransactions();
		}
	}

	@Override
	public void OnOrderCommit(String result) {
		isOrderSuc = true;
		Intent intent = new Intent();
		intent.setClass(this, PaymentActivity.class);
		intent.putExtra("ordercommit_return_string", result);
		intent.putExtra("ordercommit_return_payType", payType);
		startActivityForResult(intent, Constants.PAYMENT_REQUESTCODE);
	}

	@Override
	public void OnAddressChange(AddressBean addressBean) {
		updateHeaderTitle(getResources().getString(
				R.string.shop_checkout_add_address));
		FragmentTransaction localFragmentTransaction1 = localFragmentManager
				.beginTransaction();
		mAddAddressFragment = (BaseFragment) localFragmentManager
				.findFragmentByTag(FRAGMENT_TAG_ADDADDRESS);
		if (mAddAddressFragment == null) {
			mAddAddressFragment = new AddressEditFragment();
			Bundle bundle = new Bundle();
			bundle.putParcelable("address_list_item_choosed", null);
			mAddAddressFragment.setArguments(bundle);
		}
		isCheckout = false;
		isEditAddress = true;
		localFragmentTransaction1.replace(
				ApplicationUtils.getResId("id", "container"),
				mAddAddressFragment);
		if (!(localFragmentTransaction1.isEmpty())) {
			localFragmentTransaction1.commitAllowingStateLoss();
			getSupportFragmentManager().executePendingTransactions();
		}

	}

	@Override
	public void OnAddressChoose(int position) {
		addressId = position;
		FragmentTransaction localFragmentTransaction1 = localFragmentManager
				.beginTransaction();
		mCheckoutFragment = (BaseFragment) localFragmentManager
				.findFragmentByTag(FRAGMENT_TAG_CHECKOUT);
		if (mCheckoutFragment == null) {
			mCheckoutFragment = new CheckoutFragment();
			Bundle bundle = new Bundle();
			bundle.putInt("address_list_item_choosed", addressId);
			mCheckoutFragment.setArguments(bundle);
			localFragmentTransaction1.replace(
					ApplicationUtils.getResId("id", "container"),
					mCheckoutFragment);

		}
		isCheckout = true;
		isEditAddress = false;
		updateHeaderTitle(getResources()
				.getString(R.string.shop_checkout_title));
		if (!(localFragmentTransaction1.isEmpty())) {
			localFragmentTransaction1.commitAllowingStateLoss();
			getSupportFragmentManager().executePendingTransactions();
		}
	}

	@Override
	public void OnAddSuc() {
		FragmentTransaction localFragmentTransaction1 = localFragmentManager
				.beginTransaction();
		mCheckoutFragment = (BaseFragment) localFragmentManager
				.findFragmentByTag(FRAGMENT_TAG_CHECKOUT);
		if (mCheckoutFragment == null) {
			mCheckoutFragment = new CheckoutFragment();
			Bundle bundle = new Bundle();
			bundle.putInt("address_list_item_choosed", -1);
			mCheckoutFragment.setArguments(bundle);
			localFragmentTransaction1.replace(
					ApplicationUtils.getResId("id", "container"),
					mCheckoutFragment);
		}
		isCheckout = true;
		isEditAddress = false;
		updateHeaderTitle(getResources()
				.getString(R.string.shop_checkout_title));
		if (!(localFragmentTransaction1.isEmpty())) {
			localFragmentTransaction1.commitAllowingStateLoss();
			getSupportFragmentManager().executePendingTransactions();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_OK:
			setResult(RESULT_OK);
			finish();
			break;
		default:
			setResult(RESULT_OK);
			finish();
			break;
		}
	}

	@Override
	public void OnEtSet(EditText e1, EditText e2, EditText e3, EditText e4) {
		et1 = e1;
		et2 = e2;
		et3 = e3;
		et4 = e4;
		InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(et1.getWindowToken(), 0);
		inputMethodManager.hideSoftInputFromWindow(et2.getWindowToken(), 0);
		inputMethodManager.hideSoftInputFromWindow(et3.getWindowToken(), 0);
		inputMethodManager.hideSoftInputFromWindow(et4.getWindowToken(), 0);
		if (null != et1)
			et1.clearFocus();
		if (null != et2)
			et2.clearFocus();
		if (null != et3)
			et3.clearFocus();
		if (null != et4)
			et4.clearFocus();
	}

	public void clearFocus() {
		InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(et1.getWindowToken(), 0);
		inputMethodManager.hideSoftInputFromWindow(et2.getWindowToken(), 0);
		inputMethodManager.hideSoftInputFromWindow(et3.getWindowToken(), 0);
		inputMethodManager.hideSoftInputFromWindow(et4.getWindowToken(), 0);
		if (null != et1)
			et1.clearFocus();
		if (null != et2)
			et2.clearFocus();
		if (null != et3)
			et3.clearFocus();
		if (null != et4)
			et4.clearFocus();
	}
}
