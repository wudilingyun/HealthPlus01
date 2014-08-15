package com.vee.shop.ui;

import java.util.Map;

import org.springframework.util.MultiValueMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vee.healthplus.R;
import com.vee.healthplus.common.MyApplication;
import com.vee.healthplus.util.user.HP_DBModel;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.util.user.ICallBack;
import com.vee.healthplus.widget.CustomDialog;
import com.vee.shop.activity.AddressActivity;
import com.vee.shop.activity.BaseHeaderActivity;
import com.vee.shop.activity.OrderListActivity;
import com.vee.shop.activity.PointActivity;
import com.vee.shop.activity.ServiceStationActivity;
import com.vee.shop.http.GetCartTask;
import com.vee.shop.util.ApplicationUtils;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class AccountFragment extends BaseFragment implements OnClickListener,
		ICallBack {
	private static final String TAG = "AccountFragment";

	// private View account_info_container;
	private LinearLayout not_login;
	private FrameLayout already_login;
	private TextView userName;
	private View orderList;
	private View orderEdit;
	private View addressList;
	private View stationList;
	private View pointList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View localView = inflater.inflate(
				ApplicationUtils.getResId("layout", "shop_account_fragment"),
				container, false);
		not_login = (LinearLayout) localView.findViewById(ApplicationUtils
				.getResId("id", "not_login"));
		not_login.setOnClickListener(this);
		already_login = (FrameLayout) localView.findViewById(ApplicationUtils
				.getResId("id", "already_login"));
		already_login.setOnClickListener(this);
		userName = (TextView) localView.findViewById(ApplicationUtils.getResId(
				"id", "account_user_name"));
		orderList = (View) localView.findViewById(ApplicationUtils.getResId(
				"id", "shop_account_order_list"));
		orderEdit = (View) localView.findViewById(ApplicationUtils.getResId(
				"id", "shop_account_order_edit"));
		addressList = (View) localView.findViewById(ApplicationUtils.getResId(
				"id", "shop_account_address"));
		stationList = (View) localView.findViewById(ApplicationUtils.getResId(
				"id", "shop_account_station"));
		pointList = (View) localView.findViewById(ApplicationUtils.getResId(
				"id", "shop_account_point"));
		userName.setText(ApplicationUtils.getResId("string",
				"shop_welcome_title"));
		orderList.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), OrderListActivity.class);
				startActivity(intent);
			}
		});
		orderEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
		addressList.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), AddressActivity.class);
				startActivity(intent);
			}
		});
		stationList.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), ServiceStationActivity.class);
				startActivity(intent);
			}
		});
		pointList.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), PointActivity.class);
				startActivity(intent);
			}
		});
		return localView;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == ApplicationUtils.getResId("id", "not_login")) {

		} else if (v.getId() == ApplicationUtils
				.getResId("id", "already_login")) {
			LogOutDialog ld = new LogOutDialog();
			ld.show(getActivity().getSupportFragmentManager(), "");
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		initLoginStat();
		new GetCartTask(null, null, mContext, getActivity()).execute();
	}

	private void initLoginStat() {
		if (HP_User.getOnLineUserId(mContext) == 0) {
			not_login.setVisibility(View.VISIBLE);
			already_login.setVisibility(View.GONE);
		} else {
			not_login.setVisibility(View.GONE);
			already_login.setVisibility(View.VISIBLE);
			HP_User user = HP_DBModel.getInstance(mContext)
					.queryUserInfoByUserId(HP_User.getOnLineUserId(mContext),
							true);
			userName.setText(getResources().getString(
					ApplicationUtils.getResId("string",
							"shop_welcome_title_header"))
					+ user.userName);
		}
		((BaseHeaderActivity) getActivity()).updateCartCount(MyApplication
				.getCartNum());
	}

	@Override
	public void onChange() {
		initLoginStat();
		new myGetCartTask(null, null, mContext, getActivity()).execute();
	}

	class myGetCartTask extends GetCartTask {

		public myGetCartTask(String actionUrl,
				MultiValueMap<String, String> formData, Context mContext,
				Activity activity) {
			super(actionUrl, formData, mContext, activity);
		}

		@Override
		protected void onPostExecute(Map<String, Object> result) {
			super.onPostExecute(result);
			((BaseHeaderActivity) getActivity()).updateCartCount(MyApplication
					.getCartNum());
		}

	}

	@Override
	public void onCancel() {

	}

	@SuppressLint("ValidFragment")
	class LogOutDialog extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			View layout = View.inflate(getActivity(),
					R.layout.shop_logout_dialog, null);
			CustomDialog.Builder customBuilder = new CustomDialog.Builder(
					getActivity());
			customBuilder
					.setTitle(
							getActivity().getResources().getString(
									R.string.shop_update_note))
					.setContentView(layout)
					.setPositiveButton(
							getActivity().getResources().getString(
									R.string.quit),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									SpringAndroidService.getInstance(
											getActivity().getApplication())
											.signOut();
									HP_User.setOnLineUserId(mContext, 0);
									MyApplication.setCartNum(0);
									if (null != MyApplication.getCartMap())
										MyApplication.getCartMap().clear();
									initLoginStat();
								}
							})
					.setNegativeButton(
							getActivity().getResources().getString(
									R.string.CANCLE),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dismiss();
								}
							});

			return customBuilder.create();
		}
	}
}
