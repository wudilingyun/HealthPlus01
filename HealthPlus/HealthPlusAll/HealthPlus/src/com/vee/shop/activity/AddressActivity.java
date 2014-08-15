package com.vee.shop.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vee.healthplus.R;
import com.vee.shop.bean.AddressBean;
import com.vee.shop.ui.AddressEditFragment;
import com.vee.shop.ui.AddressEditFragment.OnAddSucListener;
import com.vee.shop.ui.AddressEditFragment.OnEditTextListener;
import com.vee.shop.ui.AddressListFragment;
import com.vee.shop.ui.AddressListFragment.OnAddressChangeListener;
import com.vee.shop.ui.BaseFragment;
import com.vee.shop.util.ApplicationUtils;

public class AddressActivity extends BaseHeaderActivity implements
		OnAddressChangeListener, OnAddSucListener, OnEditTextListener {
	private static final String TAG = "AddressActivity";
	private Context mContext;

	private FragmentManager localFragmentManager;
	private boolean isList = true;
	private static final String FRAGMENT_TAG_LIST = "address_fragment_tag_list";
	private static final String FRAGMENT_TAG_EDIT = "address_fragment_tag_edit";
	private BaseFragment mListFragment;
	private BaseFragment mEditFragment;
	private View titleBar;
	private LinearLayout titleRightbar;
	private ImageView titlebarButton;
	private TextView titlebarText;

	private EditText et1;
	private EditText et2;
	private EditText et3;
	private EditText et4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setUpFragments();
		setUpTitleBar();
	}

	private void setUpTitleBar() {
		setRightBtnVisible(View.INVISIBLE);
		updateHeaderTitle(getResources().getString(R.string.shop_address_title));
		setLeftBtnClickListenter(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isList) {
					finish();
				} else {
					clearFocus();
					AlertDialog.Builder quitDialog;
					quitDialog = new AlertDialog.Builder(AddressActivity.this);
					quitDialog.setMessage(getString(ApplicationUtils.getResId(
							"string", "edit_quit_message")));
					quitDialog.setPositiveButton(
							ApplicationUtils.getResId("string", "Ensure"),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									FragmentTransaction localFragmentTransaction1 = localFragmentManager
											.beginTransaction();
									if (mListFragment == null) {
										mListFragment = new AddressListFragment();
									}
									localFragmentTransaction1.replace(
											ApplicationUtils.getResId("id",
													"container"), mListFragment);
									isList = true;
									if (!(localFragmentTransaction1.isEmpty())) {
										localFragmentTransaction1
												.commitAllowingStateLoss();
										getSupportFragmentManager()
												.executePendingTransactions();
									}
								}
							});
					quitDialog.setNegativeButton(
							ApplicationUtils.getResId("string", "Cancel"),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

								}
							});
					quitDialog.show();
				}
			}
		});
	}

	private void setUpFragments() {
		localFragmentManager = getSupportFragmentManager();
		FragmentTransaction localFragmentTransaction1 = localFragmentManager
				.beginTransaction();

		mListFragment = (BaseFragment) localFragmentManager
				.findFragmentByTag(FRAGMENT_TAG_LIST);
		if (mListFragment == null) {
			mListFragment = new AddressListFragment();
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
			clearFocus();
			AlertDialog.Builder quitDialog;
			quitDialog = new AlertDialog.Builder(AddressActivity.this);
			quitDialog.setMessage(getString(ApplicationUtils.getResId("string",
					"edit_quit_message")));
			quitDialog.setPositiveButton(
					ApplicationUtils.getResId("string", "Ensure"),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							FragmentTransaction localFragmentTransaction1 = localFragmentManager
									.beginTransaction();
							if (mListFragment == null) {
								mListFragment = new AddressListFragment();
							}
							localFragmentTransaction1.replace(ApplicationUtils
									.getResId("id", "container"), mListFragment);
							isList = true;
							if (!(localFragmentTransaction1.isEmpty())) {
								localFragmentTransaction1
										.commitAllowingStateLoss();
								getSupportFragmentManager()
										.executePendingTransactions();
							}
						}
					});
			quitDialog.setNegativeButton(
					ApplicationUtils.getResId("string", "Cancel"),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
			quitDialog.show();
		}
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

	@Override
	public void OnAddressChange(AddressBean addressBean) {
		FragmentTransaction localFragmentTransaction1 = localFragmentManager
				.beginTransaction();
		mEditFragment = (BaseFragment) localFragmentManager
				.findFragmentByTag(FRAGMENT_TAG_EDIT);
		if (mEditFragment == null) {
			mEditFragment = new AddressEditFragment();
			Bundle bundle = new Bundle();
			bundle.putParcelable("address_list_item_choosed", addressBean);
			mEditFragment.setArguments(bundle);
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
	public void OnAddressChoose(int position) {

	}

	@Override
	public void OnEtSet(EditText e1, EditText e2, EditText e3, EditText e4) {
		et1 = e1;
		et2 = e2;
		et3 = e3;
		et4 = e4;
	}

	@Override
	public void OnAddSuc() {
		FragmentTransaction localFragmentTransaction1 = localFragmentManager
				.beginTransaction();
		if (mListFragment == null) {
			mListFragment = new AddressListFragment();
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
