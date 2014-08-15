package com.vee.shop.activity;

import java.math.BigDecimal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vee.healthplus.R;
import com.vee.shop.alipay.RechargeTask;
import com.vee.shop.alipay.RechargeTask.HandleRechargeMessage;
import com.vee.shop.alipay.beans.Order;
import com.vee.shop.bean.CartItemBean;
import com.vee.shop.bean.OrderBean;
import com.vee.shop.http.httpUtil;
import com.vee.shop.ui.ShopHeaderView;
import com.vee.shop.util.ApplicationUtils;
import com.vee.shop.util.Constants;

public class PaymentActivity extends BaseActivity {
	private static final String TAG = "PaymentActivity";
	private OrderBean orderBean = null;
	private ShopHeaderView hv;
	private ImageView leftBtn;
	private TextView order_id;
	private TextView order_recipient;
	private TextView order_recipient_phone;
	private TextView order_detail_address;
	private TextView order_delivery_time;
	private TextView order_invoice_info;
	private TextView order_fee;
	private Button pay_btn;
	private boolean isPaySuc = false;
	private String payType;
	private BigDecimal bPrice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(ApplicationUtils.getResId("layout",
				"shop_payment_activity"));
		Intent intent = getIntent();
		String orderReturn = intent.getStringExtra("ordercommit_return_string");
		hv = (ShopHeaderView) findViewById(R.id.header);
		leftBtn = (ImageView) findViewById(R.id.header_lbtn_img);
		payType = intent.getStringExtra("ordercommit_return_payType");
		orderBean = httpUtil.parseOrder(orderReturn);
		bPrice = new BigDecimal(orderBean.getTotalprice());
		bPrice = bPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
		setupView();
	}

	public void setupView() {
		leftBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isPaySuc) {
					PaymentActivity.this.setResult(RESULT_OK);
					Log.d(TAG, "setresult  ok");
				} else {
					PaymentActivity.this.setResult(RESULT_CANCELED);
					Log.d(TAG, "setresult  cancel");
				}
				finish();

			}
		});
		hv.setHeaderTitle(getResources().getString(
				R.string.shop_checkout_form_payment_online));
		order_id = (TextView) findViewById(ApplicationUtils.getResId("id",
				"order_id"));
		order_recipient = (TextView) findViewById(ApplicationUtils.getResId(
				"id", "order_recipient"));
		order_recipient_phone = (TextView) findViewById(ApplicationUtils
				.getResId("id", "order_recipient_phone"));
		order_detail_address = (TextView) findViewById(ApplicationUtils
				.getResId("id", "order_detail_address"));
		order_delivery_time = (TextView) findViewById(ApplicationUtils
				.getResId("id", "order_delivery_time"));
		order_invoice_info = (TextView) findViewById(ApplicationUtils.getResId(
				"id", "order_invoice_info"));
		order_fee = (TextView) findViewById(ApplicationUtils.getResId("id",
				"order_fee"));

		if (null != orderBean) {
			order_id.setText(orderBean.getOrdernumber());
			order_recipient.setText(orderBean.getReceiver());
			order_recipient_phone.setText(orderBean.getMobile());
			order_detail_address.setText(orderBean.getAddress());
			if (orderBean.getReceivetime().equals(
					Constants.DELIVER_TIME_TYPE_NOLIMIT)) {
				order_delivery_time.setText(getResources().getString(
						ApplicationUtils.getResId("string",
								"shop_checkout_form_delivertime_nolimit")));
			} else if (orderBean.getReceivetime().equals(
					Constants.DELIVER_TIME_TYPE_WORKTIME)) {
				order_delivery_time.setText(getResources().getString(
						ApplicationUtils.getResId("string",
								"shop_checkout_form_delivertime_work")));
			} else if (orderBean.getReceivetime().equals(
					Constants.DELIVER_TIME_TYPE_HOLIDAY)) {
				order_delivery_time.setText(getResources().getString(
						ApplicationUtils.getResId("string",
								"shop_checkout_form_delivertime_holiday")));
			}
			if (orderBean.getInvoice().equals(Constants.INVOICE_TYPE_NO)) {
				order_invoice_info.setText(getResources().getString(
						ApplicationUtils.getResId("string",
								"shop_checkout_form_invoice_no")));
			} else if (orderBean.getInvoice().equals(
					Constants.INVOICE_TYPE_PRESON)) {
				order_invoice_info.setText(orderBean.getReceiver());
			} else if (orderBean.getInvoice().equals(
					Constants.INVOICE_TYPE_GROUP)) {
				order_invoice_info.setText(orderBean.getInvoiceName());
			}
			order_fee.setText(bPrice.toString());
		}

		pay_btn = (Button) findViewById(ApplicationUtils.getResId("id",
				"pay_btn"));
		pay_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// goto alipay
				try {
					if (orderBean.getCartItemList() != null
							&& orderBean.getCartItemList().size() > 0) {
						String orderDes = null;
						StringBuffer sb = new StringBuffer();
						for (CartItemBean item : orderBean.getCartItemList()) {
							if (item != null
									&& !TextUtils.isEmpty(item.getName())) {
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
						order.orderTitle = getResources()
								.getString(
										ApplicationUtils.getResId("string",
												"app_name"));
						// order.totalFee = Double.parseDouble(orderBean
						// .getTotalprice());
						order.totalFee = bPrice.doubleValue();
						// order.totalFee = 0.01d;
						order.outTradeno = orderBean.getOrdernumber();
						order.orderDesc = orderDes;
						RechargeTask chargeTask = new RechargeTask(
								PaymentActivity.this,
								new HandleRechargeMessage() {

									@Override
									public void handlePaySuccess() {
										pay_btn.setText("已付款");
										pay_btn.setEnabled(false);
										isPaySuc = true;
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
					} else {
						Toast.makeText(
								PaymentActivity.this,
								ApplicationUtils.getResId("string",
										"cartList_null"), 1).show();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		if (payType.equals(Constants.PAY_TYPE_FACE)) {
			pay_btn.setVisibility(View.INVISIBLE);
			hv.setHeaderTitle(getResources().getString(
					R.string.shop_checkout_form_payment_face));
		} else if (payType.equals(Constants.PAY_TYPE_ONLINE)) {
			hv.setHeaderTitle(getResources().getString(
					R.string.shop_checkout_form_payment_online));
			;
		}

	}

	@Override
	public void onBackPressed() {
		if (isPaySuc) {
			PaymentActivity.this.setResult(RESULT_OK);
		} else {
			PaymentActivity.this.setResult(RESULT_CANCELED);
		}
		super.onBackPressed();
	}

	public void showSuccessDialog() {
		AlertDialog.Builder tDialog = new AlertDialog.Builder(
				PaymentActivity.this);
		tDialog.setTitle(getString(ApplicationUtils.getResId("string",
				"dialogTitle")));
		tDialog.setMessage(getString(ApplicationUtils.getResId("string",
				"recharge_success")));
		tDialog.setPositiveButton(
				ApplicationUtils.getResId("string", "Ensure"),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						PaymentActivity.this.setResult(RESULT_OK);
						finish();
					}
				});
		tDialog.show();
	}

	@Override
	protected void onDestroy() {
		if (isPaySuc) {
			PaymentActivity.this.setResult(RESULT_OK);
		} else {
			PaymentActivity.this.setResult(RESULT_CANCELED);
		}
		super.onDestroy();
	}
}
