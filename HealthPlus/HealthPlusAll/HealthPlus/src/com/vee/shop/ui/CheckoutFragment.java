package com.vee.shop.ui;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vee.shop.bean.AddressBean;
import com.vee.shop.http.ProtectTask;
import com.vee.shop.http.httpUtil;
import com.vee.shop.util.ApplicationUtils;
import com.vee.shop.util.Constants;
import com.vee.shop.util.TextUtil;
import com.vee.shop.util.ToastUtil;
import com.yunfox.s4aservicetest.response.CartAddressList;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class CheckoutFragment extends BaseFragment implements
		OnCheckedChangeListener {

	private RelativeLayout checkAddress;
	private RelativeLayout checkAddressEmpty;
	private TextView address_up;
	private TextView address_middle;
	private TextView address_bottom;
	private RadioGroup paymentGroup;
	private RadioGroup deliverGroup;
	private RadioGroup invoiceGroup;
	private LinearLayout invoice_title_bg;
	private EditText invoice_title;
	private TextView nextGo;
	private int addressIndex = 0;

	private ArrayList<AddressBean> addressList = new ArrayList<AddressBean>();
	private String addressString = null;
	private String receiverString = null;
	private String mobileString = null;
	private String payType = "2";
	private String deliverType = "1";
	private String invoiceType = "1";
	private String invoiceTitle = null;
	OnCheckoutListener mOnCheckoutListener;

	public interface OnCheckoutListener {
		public void OnCheckout(String result, String payType);

		public void onAddAddress();

		public void onChooseAddress();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mOnCheckoutListener = (OnCheckoutListener) activity;
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Bundle bundle = getArguments();
		addressIndex = bundle.getInt("address_list_item_choosed");
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View localView = inflater.inflate(
				ApplicationUtils.getResId("layout", "shop_checkout_fragment"),
				container, false);
		checkAddressEmpty = (RelativeLayout) localView
				.findViewById(ApplicationUtils.getResId("id",
						"checkout_address_empty"));
		checkAddressEmpty.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mOnCheckoutListener.onAddAddress();

			}
		});
		checkAddress = (RelativeLayout) localView.findViewById(ApplicationUtils
				.getResId("id", "checkout_address"));
		checkAddress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mOnCheckoutListener.onChooseAddress();
			}
		});
		address_up = (TextView) localView.findViewById(ApplicationUtils
				.getResId("id", "checkout_address_up"));
		address_middle = (TextView) localView.findViewById(ApplicationUtils
				.getResId("id", "checkout_address_middle"));
		address_bottom = (TextView) localView.findViewById(ApplicationUtils
				.getResId("id", "checkout_address_bottom"));
		paymentGroup = (RadioGroup) localView.findViewById(ApplicationUtils
				.getResId("id", "checkout_form_radiogroup_payment"));
		paymentGroup.setOnCheckedChangeListener(this);
		deliverGroup = (RadioGroup) localView.findViewById(ApplicationUtils
				.getResId("id", "checkout_form_radiogroup_delivertime"));
		deliverGroup.setOnCheckedChangeListener(this);
		invoiceGroup = (RadioGroup) localView.findViewById(ApplicationUtils
				.getResId("id", "checkout_form_radiogroup_invoice"));
		invoiceGroup.setOnCheckedChangeListener(this);
		invoice_title_bg = (LinearLayout) localView
				.findViewById(ApplicationUtils.getResId("id",
						"checkout_form_invoice_title_bg"));
		invoice_title = (EditText) localView.findViewById(ApplicationUtils
				.getResId("id", "checkout_form_invoice_title"));
		nextGo = (TextView) localView.findViewById(ApplicationUtils.getResId(
				"id", "checkout_next"));
		nextGo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				invoiceTitle = invoice_title.getText().toString().trim();
				if (null == addressString) {
					ToastUtil.show(mContext,
							ApplicationUtils.getResId("string", "tips_address"));
				} else if ((invoiceType.equals(Constants.INVOICE_TYPE_GROUP))
						&& ((null == invoiceTitle) || (invoiceTitle.length() == 0))) {
					ToastUtil.show(mContext, ApplicationUtils.getResId(
							"string", "tips_address_invoice"));
				} else {
					if ((invoiceType.equals(Constants.INVOICE_TYPE_NO))) {
						invoiceTitle = null;
					} else if ((invoiceType
							.equals(Constants.INVOICE_TYPE_PRESON))) {
						invoiceTitle = receiverString;
					} else if ((invoiceType
							.equals(Constants.INVOICE_TYPE_GROUP))) {
						invoiceTitle = invoice_title.getText().toString()
								.trim();
					}

					MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
					formData.add("address", addressString);
					formData.add("paytype", payType);
					formData.add("receivetime", deliverType);
					formData.add("fapiaotype", invoiceType);
					formData.add("danweifapiaoname", invoiceTitle);
					formData.add("mobile", mobileString);
					formData.add("receiver", receiverString);
					new myCheckoutTask(Constants.ACCOUNT_CHECKOUT_URL,
							formData, mContext).execute();
				}
			}
		});
		return localView;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

		if (checkedId == ApplicationUtils.getResId("id",
				"checkout_form_radiogroup_payment_online")) {
			payType = Constants.PAY_TYPE_ONLINE;
		} else if (checkedId == ApplicationUtils.getResId("id",
				"checkout_form_radiogroup_payment_face")) {
			payType = Constants.PAY_TYPE_FACE;
		} else if (checkedId == ApplicationUtils.getResId("id",
				"checkout_form_radiogroup_delivertime_nolimit")) {
			deliverType = Constants.DELIVER_TIME_TYPE_NOLIMIT;
		} else if (checkedId == ApplicationUtils.getResId("id",
				"checkout_form_radiogroup_delivertime_work")) {
			deliverType = Constants.DELIVER_TIME_TYPE_WORKTIME;
		} else if (checkedId == ApplicationUtils.getResId("id",
				"checkout_form_radiogroup_delivertime_holiday")) {
			deliverType = Constants.DELIVER_TIME_TYPE_HOLIDAY;
		} else if (checkedId == ApplicationUtils.getResId("id",
				"checkout_form_radiogroup_invoice_no")) {
			invoiceType = Constants.INVOICE_TYPE_NO;
			invoice_title_bg.setVisibility(View.GONE);
		} else if (checkedId == ApplicationUtils.getResId("id",
				"checkout_form_radiogroup_invoice_person")) {
			invoiceType = Constants.INVOICE_TYPE_PRESON;
			invoice_title_bg.setVisibility(View.GONE);
		} else if (checkedId == ApplicationUtils.getResId("id",
				"checkout_form_radiogroup_invoice_company")) {
			invoiceType = Constants.INVOICE_TYPE_GROUP;
			invoice_title_bg.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		if (addressList.size() == 0) {
			new myGetAddressTask(Constants.ACCOUNT_QUERY_ADDRESS_URL, null,
					mContext).execute();
		} else {
			checkAddress.setVisibility(View.VISIBLE);
			checkAddressEmpty.setVisibility(View.GONE);
			AddressBean aBean;
			if (addressIndex == -1) {
				aBean = addressList.get(addressList.size() - 1);
			} else {
				aBean = addressList.get(addressIndex);
			}
			address_up.setText(aBean.getProvince() + " " + aBean.getCity()
					+ " " + aBean.getDistrict());
			address_middle.setText(aBean.getDetail() + " "
					+ aBean.getPostcode());
			address_bottom.setText(aBean.getReceiver() + " "
					+ aBean.getMobile());
		}
	}

	class myGetAddressTask extends ProtectTask {

		CartAddressList serverAddressList;

		public myGetAddressTask(String actionUrl,
				MultiValueMap<String, String> formData, Context mContext) {
			super(actionUrl, formData, mContext);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Map<String, Object> doInBackground(Void... params) {
			try {
				;
				serverAddressList = (SpringAndroidService
						.getInstance(getActivity().getApplication())
						.handleProtectAddress(actionUrl, formData,
								Constants.HTTP_TYPE_GET));
			} catch (Exception e) {
				this.exception = e;
			}
			return super.doInBackground(params);
		}

		@Override
		protected void onPostExecute(Map<String, Object> result) {
			super.onPostExecute(result);
			String json;
			if ((exception == null) && (serverAddressList != null)) {
				json = TextUtil.createAddressJson(serverAddressList);
				addressList = httpUtil.parseAddressList(json);
			}
			if (addressList.size() != 0) {
				checkAddress.setVisibility(View.VISIBLE);
				checkAddressEmpty.setVisibility(View.GONE);
				AddressBean aBean;
				if (addressIndex == -1) {
					aBean = addressList.get(addressList.size() - 1);
				} else {
					aBean = addressList.get(addressIndex);
				}
				address_up.setText(aBean.getProvince() + " " + aBean.getCity()
						+ " " + aBean.getDistrict());
				address_middle.setText(aBean.getDetail() + " "
						+ aBean.getPostcode());
				address_bottom.setText(aBean.getReceiver() + " "
						+ aBean.getMobile());
				addressString = aBean.getProvince() + " " + aBean.getCity()
						+ " " + aBean.getDistrict() + aBean.getDetail() + " "
						+ aBean.getPostcode();
				receiverString = aBean.getReceiver();
				mobileString = aBean.getMobile();
			} else {
				checkAddress.setVisibility(View.GONE);
				checkAddressEmpty.setVisibility(View.VISIBLE);
			}
		}

	}

	class myCheckoutTask extends ProtectTask {

		public myCheckoutTask(String actionUrl,
				MultiValueMap<String, String> formData, Context mContext) {
			super(actionUrl, formData, mContext);
		}

		@Override
		protected Map<String, Object> doInBackground(Void... params) {
			try {
				return (SpringAndroidService.getInstance(getActivity()
						.getApplication()).handleProtect(actionUrl, formData,
						Constants.HTTP_TYPE_POST));
			} catch (Exception e) {
				this.exception = e;
				return null;
			}
		}

		@Override
		protected void onPostExecute(Map<String, Object> result) {
			super.onPostExecute(result);
			if ((!TextUtils.isEmpty(result.toString()))
					&& (!result.toString().equals("null"))) {
				mOnCheckoutListener.OnCheckout(
						TextUtil.createCheckoutJson(result), payType);
			} else {
				ToastUtil
						.show(mContext, ApplicationUtils.getResId("string",
								"order_submit_exception_send_data"),
								Toast.LENGTH_SHORT);
			}
		}

	}
}
