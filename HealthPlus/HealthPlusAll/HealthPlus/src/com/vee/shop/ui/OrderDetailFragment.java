package com.vee.shop.ui;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.util.MultiValueMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.util.AQUtility;
import com.vee.healthplus.R;
import com.vee.shop.alipay.RechargeTask;
import com.vee.shop.alipay.RechargeTask.HandleRechargeMessage;
import com.vee.shop.alipay.beans.Order;
import com.vee.shop.bean.CartItemBean;
import com.vee.shop.bean.OrderDetailBean;
import com.vee.shop.http.ProtectTask;
import com.vee.shop.http.httpUtil;
import com.vee.shop.util.ApplicationUtils;
import com.vee.shop.util.Constants;
import com.vee.shop.util.TextUtil;
import com.vee.shop.util.ToastUtil;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class OrderDetailFragment extends BaseFragment {
	private static final String TAG = "OrderDetailFragment";

	private ListView mListView;
	private String detailUrl;
	private String orderId;
	private myListAdapter mListAdapter;
	private TextView order_status;
	private TextView order_fee;
	private TextView add_time;
	private TextView order_id;
	private TextView receive_address;
	private TextView receive_time;
	private TextView invoice_info;
	private Button payButton;
	private ImageView cancelButton;
	private OrderDetailBean orderDetailBean;
	private ArrayList<CartItemBean> productList;
	OnPayListener mOnPayListener;
	OnOrderCancelListener mOnOrderCancelListener;
	private String backupJson;
	private BigDecimal bPrice;

	public interface OnPayListener {
		public void OnPaySuc();
	}

	public interface OnOrderCancelListener {
		public void OnCancel();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mOnPayListener = (OnPayListener) activity;
			mOnOrderCancelListener = (OnOrderCancelListener) activity;
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		detailUrl = bundle.getString("orderlist_detail_url");
		orderId = bundle.getString("orderlist_order_id");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View localView = inflater.inflate(ApplicationUtils.getResId("layout",
				"shop_orderlist_detail_fragment"), container, false);
		order_status = (TextView) localView.findViewById(ApplicationUtils
				.getResId("id", "order_status"));
		order_fee = (TextView) localView.findViewById(ApplicationUtils
				.getResId("id", "order_fee"));
		add_time = (TextView) localView.findViewById(ApplicationUtils.getResId(
				"id", "add_time"));
		order_id = (TextView) localView.findViewById(ApplicationUtils.getResId(
				"id", "order_id"));
		receive_address = (TextView) localView.findViewById(ApplicationUtils
				.getResId("id", "receive_address"));
		receive_time = (TextView) localView.findViewById(ApplicationUtils
				.getResId("id", "receive_time"));
		invoice_info = (TextView) localView.findViewById(ApplicationUtils
				.getResId("id", "invoice_info"));
		payButton = (Button) localView.findViewById(ApplicationUtils.getResId(
				"id", "pay_btn"));
		payButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// start alipay
				startPay();
			}
		});
		cancelButton = (ImageView) localView.findViewById(ApplicationUtils
				.getResId("id", "cancel_btn"));
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new myCancelOrderTask(Constants.ACCOUNT_ADD_DEL_CART_URL
						+ orderId + "/cancelorder", null, mContext).execute();
			}
		});
		mListView = (ListView) localView.findViewById(ApplicationUtils
				.getResId("id", "order_detail_fragment_listview"));
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			}
		});
		mListAdapter = new myListAdapter();
		return localView;
	}

	@Override
	public void onResume() {
		super.onResume();
		backupJson = settings.getString("OrderDetailJsonString" + orderId,
				"null");
		// List<NameValuePair> params = new ArrayList<NameValuePair>();
		// params.add(new BasicNameValuePair("username",
		// MyApplication.getUUID()));
		// myHttpGetAsyncTask mHttpGetAsyncTask = new myHttpGetAsyncTask(
		// detailUrl, params, mContext);
		// mHttpGetAsyncTask.execute();

		new myGetOrderDetailTask(detailUrl, null, mContext).execute();
	}

	class myListAdapter extends BaseAdapter {

		Holder holder;

		class Holder {
			ImageView pic;
			TextView title;
			TextView detail;
		}

		@Override
		public int getCount() {
			return productList.size();
		}

		@Override
		public Object getItem(int item) {
			return item;
		}

		@Override
		public long getItemId(int id) {
			return id;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final CartItemBean cib = productList.get(position);
			if (convertView == null) {
				holder = new Holder();
				convertView = LayoutInflater.from(mContext).inflate(
						ApplicationUtils.getResId("layout",
								"shop_orderdetail_list_item"), null);
				holder.title = (TextView) convertView
						.findViewById(ApplicationUtils.getResId("id",
								"product_title"));
				holder.detail = (TextView) convertView
						.findViewById(ApplicationUtils.getResId("id",
								"product_detail"));
				holder.pic = (ImageView) convertView
						.findViewById(ApplicationUtils.getResId("id",
								"product_photo"));
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			holder.title.setText(cib.getName());
			StringBuilder sb = new StringBuilder();
			sb.append(getResources()
					.getString(
							ApplicationUtils.getResId("string",
									"shop_cart_list_price")));
			sb.append(cib.getPrice());
			sb.append(" ");
			sb.append(getResources().getString(
					ApplicationUtils.getResId("string", "shop_cart_list_num")));
			sb.append(cib.getCount());
			sb.append("\r\n");
			sb.append(getResources()
					.getString(
							ApplicationUtils.getResId("string",
									"shop_cart_list_total")));
			BigDecimal priceBigDecimal = new BigDecimal(cib.getPrice());
			BigDecimal countBigDecimal = new BigDecimal(cib.getCount());
			sb.append(priceBigDecimal.multiply(countBigDecimal).toString());
			holder.detail.setText(sb.toString());
			if ((null == cib.getImgUrl()) || (cib.getImgUrl().equals(""))) {
				holder.pic.setBackgroundResource(ApplicationUtils.getResId(
						"drawable", "shop_img_defaultbg"));
			} else {
				AQuery aq = new AQuery(holder.pic);
				File cacheDir = new File(Constants.EXTERNAL_STORAGE_PATH
						+ "/17VEEShop/photocache");
				AQUtility.setCacheDir(cacheDir);
				aq.image(cib.getImgUrl(), Constants.isImgMemCache,
						Constants.isImgFileCache);
			}
			return convertView;
		}

	}

	class myCancelOrderTask extends ProtectTask {

		public myCancelOrderTask(String actionUrl,
				MultiValueMap<String, String> formData, Context mContext) {
			super(actionUrl, formData, mContext);
		}

		@Override
		protected Map<String, Object> doInBackground(Void... params) {
			try {
				return (SpringAndroidService.getInstance(getActivity()
						.getApplication()).handleProtect(actionUrl, formData,
						Constants.HTTP_TYPE_GET));
			} catch (Exception e) {
				Log.e(TAG, e.getLocalizedMessage(), e);
				this.exception = e;
				return null;
			}
		}

		@Override
		protected void onPostExecute(Map<String, Object> result) {
			super.onPostExecute(result);
			if ((!TextUtils.isEmpty(result.toString()))
					&& (!result.toString().equals("null"))) {
				String returncode = httpUtil.parseServerResponse(result
						.toString());
				if ("200".equals(returncode)) {
					mOnOrderCancelListener.OnCancel();
				} else {
					ToastUtil.show(mContext, ApplicationUtils.getResId(
							"string", "order_submit_exception_send_data"));
				}
			}
		}

	}

	class myGetOrderDetailTask extends ProtectTask {

		public myGetOrderDetailTask(String actionUrl,
				MultiValueMap<String, String> formData, Context mContext) {
			super(actionUrl, formData, mContext);
		}

		@Override
		protected Map<String, Object> doInBackground(Void... params) {
			try {
				return (SpringAndroidService.getInstance(getActivity()
						.getApplication()).handleProtect(actionUrl, formData,
						Constants.HTTP_TYPE_GET));
			} catch (Exception e) {
				Log.e(TAG, e.getLocalizedMessage(), e);
				this.exception = e;
				return null;
			}
		}

		@Override
		protected void onPostExecute(Map<String, Object> result) {
			super.onPostExecute(result);
			String json = null;
			if (exception != null) {
				// json = backupJson;
				json = null;
			} else if ((exception == null) && (null != result)) {
				json = TextUtil.createOrderDetailJson(result);
				editor.putString("OrderDetailJsonString" + orderId, json);
				editor.commit();
			}
			orderDetailBean = httpUtil.parseOrderDetail(json);
			if (null != orderDetailBean) {
				ArrayList<CartItemBean> oldProductList = orderDetailBean
						.getProductList();
				// productList = orderDetailBean.getProductList();
				productList = new ArrayList<CartItemBean>();
				for (int i = 0; i < oldProductList.size(); i++) {
					if (Integer.parseInt(oldProductList.get(i).getCount()) != 0) {
						productList.add(oldProductList.get(i));
					}
				}
				String status = orderDetailBean.getStatus();
				if (status.equals(Constants.ORDER_STATUS_NOPAY)) {
					order_status.setText(getResources().getString(
							ApplicationUtils.getResId("string",
									"order_view_status"))
							+ getResources().getString(
									ApplicationUtils.getResId("string",
											"order_list_status_nopay")));
					payButton.setVisibility(View.VISIBLE);
					cancelButton.setVisibility(View.VISIBLE);
				} else if (status.equals(Constants.ORDER_STATUS_WAIT_FOT_SEND)) {
					order_status
							.setText(getResources().getString(
									ApplicationUtils.getResId("string",
											"order_view_status"))
									+ getResources()
											.getString(
													ApplicationUtils
															.getResId("string",
																	"order_list_status_wait_for_send")));
					cancelButton.setVisibility(View.VISIBLE);
				} else if (status
						.equals(Constants.ORDER_STATUS_WAIT_FOT_RECEIVE)) {
					order_status
							.setText(getResources().getString(
									ApplicationUtils.getResId("string",
											"order_view_status"))
									+ getResources()
											.getString(
													ApplicationUtils
															.getResId("string",
																	"order_list_status_wait_for_receive")));
					cancelButton.setVisibility(View.VISIBLE);
				} else if (status.equals(Constants.ORDER_TYPE_DONE)) {
					order_status.setText(getResources().getString(
							ApplicationUtils.getResId("string",
									"order_view_status"))
							+ getResources().getString(
									ApplicationUtils.getResId("string",
											"order_list_type_done")));
				} else if (status.equals(Constants.ORDER_TYPE_UNDONE)) {
					order_status.setText(getResources().getString(
							ApplicationUtils.getResId("string",
									"order_view_status"))
							+ getResources().getString(
									ApplicationUtils.getResId("string",
											"order_list_type_undone")));
				} else if (status.equals(Constants.ORDER_TYPE_ALL)) {
					order_status.setText(getResources().getString(
							ApplicationUtils.getResId("string",
									"order_view_status"))
							+ getResources().getString(
									ApplicationUtils.getResId("string",
											"order_list_type_all")));
				}
				bPrice = new BigDecimal(orderDetailBean.getTotalprice());
				bPrice = bPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
				order_fee.setText(getResources().getString(
						ApplicationUtils.getResId("string", "order_view_fee"))
						+ bPrice.toString());
				orderDetailBean.getTotalprice();
				Timestamp ts = new Timestamp(Long.parseLong(orderDetailBean
						.getOrderdate()));
				DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				add_time.setText(sdf.format(ts));
				order_id.setText(orderDetailBean.getOrdernumber());
				receive_address.setText(orderDetailBean.getAddress() + "\r\n"
						+ orderDetailBean.getReceiver() + " "
						+ orderDetailBean.getMobile());
				String receivertime = orderDetailBean.getReceivetime();
				if (receivertime.equals(Constants.DELIVER_TIME_TYPE_NOLIMIT)) {
					receive_time.setText(getResources().getString(
							ApplicationUtils.getResId("string",
									"shop_checkout_form_delivertime_nolimit")));
				} else if (receivertime
						.equals(Constants.DELIVER_TIME_TYPE_WORKTIME)) {
					receive_time.setText(getResources().getString(
							ApplicationUtils.getResId("string",
									"shop_checkout_form_delivertime_work")));
				} else if (receivertime
						.equals(Constants.DELIVER_TIME_TYPE_HOLIDAY)) {
					receive_time.setText(getResources().getString(
							ApplicationUtils.getResId("string",
									"shop_checkout_form_delivertime_holiday")));
				}
				String invoiceType = orderDetailBean.getInvoice();
				if (invoiceType.equals(Constants.INVOICE_TYPE_NO)) {
					invoice_info.setText(getResources().getString(
							ApplicationUtils.getResId("string",
									"shop_checkout_form_invoice_no")));
				} else if (invoiceType.equals(Constants.INVOICE_TYPE_PRESON)) {
					invoice_info.setText(getResources().getString(
							ApplicationUtils.getResId("string",
									"shop_checkout_form_invoice_person")));
				} else if (invoiceType.equals(Constants.INVOICE_TYPE_GROUP)) {
					invoice_info.setText(orderDetailBean.getInvoiceName());
				}
				if (null != productList) {
					mListView.setAdapter(mListAdapter);
					ApplicationUtils
							.setListViewHeightBasedOnChildren(mListView);
				}
			}
		}

	}

	// class myHttpGetAsyncTask extends HttpGetAsyncTask {
	//
	// public myHttpGetAsyncTask(String actionUrl, List<NameValuePair> params,
	// Context context) {
	// super(actionUrl, params, context);
	// }
	//
	// @Override
	// protected void onPreExecute() {
	// super.onPreExecute();
	// getDialog().show();
	// }
	//
	// @Override
	// protected void onPostExecute(String result) {
	// super.onPostExecute(result);
	// getDialog().dismiss();
	// String json;
	// if ((!TextUtils.isEmpty(result)) && (!result.equals("null"))) {
	// editor.putString("OrderDetailJsonString" + orderId, result);
	// editor.commit();
	// json = result;
	// } else {
	// json = backupJson;
	// }
	// orderDetailBean = httpUtil.parseOrderDetail(json);
	// if (null != orderDetailBean) {
	// ArrayList<CartItemBean> oldProductList =
	// orderDetailBean.getProductList();
	// // productList = orderDetailBean.getProductList();
	// productList = new ArrayList<CartItemBean>();
	// for (int i = 0; i < oldProductList.size(); i++) {
	// if (Integer.parseInt(oldProductList.get(i).getCount()) != 0) {
	// productList.add(oldProductList.get(i));
	// }
	// }
	// String status = orderDetailBean.getStatus();
	// if (status.equals(Constants.ORDER_STATUS_NOPAY)) {
	// order_status.setText(getResources().getString(
	// ApplicationUtils.getResId("string",
	// "order_view_status"))
	// + getResources().getString(
	// ApplicationUtils.getResId("string",
	// "order_list_status_nopay")));
	// payButton.setVisibility(View.VISIBLE);
	// } else if (status.equals(Constants.ORDER_STATUS_WAIT_FOT_SEND)) {
	// order_status
	// .setText(getResources().getString(
	// ApplicationUtils.getResId("string",
	// "order_view_status"))
	// + getResources()
	// .getString(
	// ApplicationUtils
	// .getResId("string",
	// "order_list_status_wait_for_send")));
	// } else if (status
	// .equals(Constants.ORDER_STATUS_WAIT_FOT_RECEIVE)) {
	// order_status
	// .setText(getResources().getString(
	// ApplicationUtils.getResId("string",
	// "order_view_status"))
	// + getResources()
	// .getString(
	// ApplicationUtils
	// .getResId("string",
	// "order_list_status_wait_for_receive")));
	// } else if (status.equals(Constants.ORDER_TYPE_DONE)) {
	// order_status.setText(getResources().getString(
	// ApplicationUtils.getResId("string",
	// "order_view_status"))
	// + getResources().getString(
	// ApplicationUtils.getResId("string",
	// "order_list_type_done")));
	// } else if (status.equals(Constants.ORDER_TYPE_UNDONE)) {
	// order_status.setText(getResources().getString(
	// ApplicationUtils.getResId("string",
	// "order_view_status"))
	// + getResources().getString(
	// ApplicationUtils.getResId("string",
	// "order_list_type_undone")));
	// } else if (status.equals(Constants.ORDER_TYPE_ALL)) {
	// order_status.setText(getResources().getString(
	// ApplicationUtils.getResId("string",
	// "order_view_status"))
	// + getResources().getString(
	// ApplicationUtils.getResId("string",
	// "order_list_type_all")));
	// }
	// bPrice = new BigDecimal(orderDetailBean.getTotalprice());
	// bPrice = bPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
	// order_fee.setText(getResources().getString(
	// ApplicationUtils.getResId("string", "order_view_fee"))
	// + bPrice.toString());
	// orderDetailBean.getTotalprice();
	// Timestamp ts = new Timestamp(Long.parseLong(orderDetailBean
	// .getOrderdate()));
	// DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// add_time.setText(sdf.format(ts));
	// order_id.setText(orderDetailBean.getOrdernumber());
	// receive_address.setText(orderDetailBean.getAddress() + "\r\n"
	// + orderDetailBean.getReceiver() + " "
	// + orderDetailBean.getMobile());
	// String receivertime = orderDetailBean.getReceivetime();
	// if (receivertime.equals(Constants.DELIVER_TIME_TYPE_NOLIMIT)) {
	// receive_time.setText(getResources().getString(
	// ApplicationUtils.getResId("string",
	// "shop_checkout_form_delivertime_nolimit")));
	// } else if (receivertime
	// .equals(Constants.DELIVER_TIME_TYPE_WORKTIME)) {
	// receive_time.setText(getResources().getString(
	// ApplicationUtils.getResId("string",
	// "shop_checkout_form_delivertime_work")));
	// } else if (receivertime
	// .equals(Constants.DELIVER_TIME_TYPE_HOLIDAY)) {
	// receive_time.setText(getResources().getString(
	// ApplicationUtils.getResId("string",
	// "shop_checkout_form_delivertime_holiday")));
	// }
	// String invoiceType = orderDetailBean.getInvoice();
	// if (invoiceType.equals(Constants.INVOICE_TYPE_NO)) {
	// invoice_info.setText(getResources().getString(
	// ApplicationUtils.getResId("string",
	// "shop_checkout_form_invoice_no")));
	// } else if (invoiceType.equals(Constants.INVOICE_TYPE_PRESON)) {
	// invoice_info.setText(getResources().getString(
	// ApplicationUtils.getResId("string",
	// "shop_checkout_form_invoice_person")));
	// } else if (invoiceType.equals(Constants.INVOICE_TYPE_GROUP)) {
	// invoice_info.setText(orderDetailBean.getInvoiceName());
	// }
	// if (null != productList) {
	// mListView.setAdapter(mListAdapter);
	// ApplicationUtils
	// .setListViewHeightBasedOnChildren(mListView);
	// }
	// }
	//
	// }
	//
	// }

	public void startPay() {
		if (productList == null || productList.size() <= 0) {
			Toast.makeText(this.getActivity(),
					ApplicationUtils.getResId("string", "cartList_null"), 1)
					.show();
			return;
		}
		StringBuffer sb = new StringBuffer();
		String orderDes = null;
		for (CartItemBean item : productList) {
			if (item != null && !TextUtils.isEmpty(item.getName())) {
				sb.append(item.getName() + " ");
			}
		}
		if (sb.length() > 1) {
			sb.deleteCharAt(sb.length() - 1);
			if (sb.length() > 1024) {
				orderDes = sb.substring(0, 1021) + "...";
			} else {
				orderDes = sb.toString();
			}
		}
		Order order = new Order();
		order.orderTitle = getResources().getString(
				ApplicationUtils.getResId("string", "app_name"));
		// order.totalFee = Double.parseDouble(orderDetailBean.getTotalprice());
		order.totalFee = bPrice.doubleValue();
		// order.totalFee = 0.01d;
		order.outTradeno = orderDetailBean.getOrdernumber();
		order.orderDesc = orderDes;
		RechargeTask chargeTask = new RechargeTask(this.getActivity(),
				new HandleRechargeMessage() {

					@Override
					public void handlePaySuccess() {
						// payButton.setText("已付款");
						payButton.setText(getResources().getString(
								ApplicationUtils.getResId("string",
										"order_list_status_done")));
						payButton.setEnabled(false);
						showSuccessDialog();
					}

					@Override
					public void handlePayFaild() {
						// pay_btn.setText("已付款");
						// pay_btn.setEnabled(false);
						// showSuccessDialog();
					}
				});
		chargeTask.execute(order);
	}

	public void showSuccessDialog() {
		AlertDialog.Builder tDialog = new AlertDialog.Builder(
				this.getActivity());
		tDialog.setTitle(getString(ApplicationUtils.getResId("string",
				"dialogTitle")));
		tDialog.setMessage(getString(ApplicationUtils.getResId("string",
				"recharge_success")));
		tDialog.setPositiveButton(R.string.Ensure,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						mOnPayListener.OnPaySuc();
					}
				});
		tDialog.show();
	}
}
