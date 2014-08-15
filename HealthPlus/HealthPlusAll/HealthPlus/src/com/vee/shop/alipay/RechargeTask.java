package com.vee.shop.alipay;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.Toast;

import com.vee.healthplus.R;
import com.vee.shop.alipay.beans.Order;
import com.vee.shop.alipay.beans.SignBeans;
import com.vee.shop.alipay.net.HttpPostUtils;
import com.vee.shop.alipay.net.MyException;
import com.vee.shop.alipay.utils.AlixId;
import com.vee.shop.alipay.utils.BaseHelper;
import com.vee.shop.alipay.utils.LOG;
import com.vee.shop.alipay.utils.MobileSecurePayHelper;
import com.vee.shop.alipay.utils.MobileSecurePayer;
import com.vee.shop.alipay.utils.PartnerConfig;
import com.vee.shop.alipay.utils.ResultChecker;

/**
 * @author xuyahui 2012-10-29 <br>
 */
public class RechargeTask extends AsyncTask<Order, Void, TaskMessage> {

	// ����֧��
	private Dialog mProgress = null;
	private Activity mActivity;
	private MobileSecurePayHelper mspHelper;
	private int points;
	static final String ALIPAY_CANCEL_STATUS = "6001";
	static final String VEE_SUCCESS_STATUS = "2";
	static final String VEE_ERROR_STATUS = "1";
	static final String VEE_CANCEL_STATUS = "9";
	static final boolean isPrintLog = true;
	private HandleRechargeMessage callback;

	public RechargeTask(Activity con, HandleRechargeMessage callback) {
		this.mActivity = con;
		this.callback = callback;
		mspHelper = new MobileSecurePayHelper(con);

		Dialog dialog = null;
		AlertDialog.Builder customBuilder = new AlertDialog.Builder(con);
		customBuilder.setMessage(R.string.recharge_ing);
		dialog = customBuilder.create();
		mProgress = dialog;
	}

	@Override
	protected void onPostExecute(TaskMessage result) {
		int errorCode = result.errorcode;
		switch (errorCode) {
		case 100:// ��ʼ����֧����
			break;
		default:
			if (mProgress != null) {
				mProgress.dismiss();
			}
			if (!mActivity.isFinishing()) {
				BaseHelper
						.showDialog(mActivity,
								mActivity.getString(R.string.dialogTitle),
								result.errorMessage,
								android.R.drawable.ic_dialog_alert);
			}
			break;
		}

		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (mspHelper.detectMobile_sp()) {// ���֧�����Ƿ��?
			closeProgress();
			mProgress.show();// ��ʾ����֧����
		} else {
			this.cancel(true);
		}
	}

	@Override
	protected TaskMessage doInBackground(Order... params) {
		Order order = params[0];
		// order.totalFee = 0.01d;
		// order.orderTitle = "mobilemall";
		// order.orderDesc = "foxGoods";
		TaskMessage taskMessage = new TaskMessage();

		try {
			// 准备订单信息
			String info = getInfo(order);
			LOG.v(isPrintLog, "orderInfo:", info);
			// start the pay.
			// 调用pay方法进行支付
			MobileSecurePayer msp = new MobileSecurePayer();
			boolean bRet = msp.pay(info, mHandler, AlixId.RQF_PAY, mActivity);
			if (bRet) {
				taskMessage.errorcode = 100;
				return taskMessage;
			} else {
				taskMessage.errorcode = 200;
				taskMessage.errorMessage = mActivity.getResources().getString(
						R.string.recharge_ing);
				return taskMessage;
			}
		} catch (Exception e) {
			taskMessage.errorcode = 300;
			taskMessage.errorMessage = e.getMessage();
			return taskMessage;
		}
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 * @return
	 */
	String getSignType() {
		String getSignType = "sign_type=" + "\"" + "RSA" + "\"";
		return getSignType;
	}

	/**
	 * 获取订单信息
	 * 
	 * @param order
	 * @return
	 * @throws MyException
	 * @throws UnsupportedEncodingException
	 */
	public String getInfo(Order order) throws MyException,
			UnsupportedEncodingException {
		ArrayList<BasicNameValuePair> orderParams = new ArrayList<BasicNameValuePair>();
		orderParams.add(new BasicNameValuePair("subject", order.orderTitle));
		orderParams.add(new BasicNameValuePair("body", order.orderDesc));
		orderParams
				.add(new BasicNameValuePair("total_fee", order.totalFee + ""));
		orderParams.add(new BasicNameValuePair("outtradeno", order.outTradeno));
		// 获取订单信息及签名xml字符串
		String signStr = HttpPostUtils.getPostStr(PartnerConfig.sign_url,
				mActivity, orderParams);
		if (signStr == null || "".equals(signStr.trim())) {
			this.cancel(true);
		}
		// 解析订单信息及签名
		SignBeans mSignBean = HttpPostUtils.getSinBeans(URLDecoder.decode(
				signStr, "UTF-8"));
		// 对签名进行编码
		String signUrlEncode = URLEncoder.encode(mSignBean.sign);
		// 组装好参数
		String info = mSignBean.content + "&sign=" + "\"" + signUrlEncode
				+ "\"" + "&" + getSignType();
		return info;
	}

	void closeProgress() {
		try {
			if (mProgress != null) {
				mProgress.dismiss();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			try {
				String strRet = (String) msg.obj;
				// strRet =
				// "resultStatus={9000};memo={};result={partner=\"2088701074813762\"&seller=\"2088701074813762\"&out_trade_no=\"2012111325870\"&subject=\"��ֵ\"&body=\"17VEE��Ϸ�����ֵ��\"&total_fee=\"0.01\"&notify_url=\"http%3A%2F%2Fssw.market.17vee.com%2Falipayrpd.php\"&success=\"true\"&sign_type=\"RSA\"&sign=\"PfnCm8wZ3aTroPbadH8VFctMXTdp4VhN24x1MrVitr4i59qr1Yy6JPE+9gyPLuyCq/Os+MAE8yJOd2Z73+KlkiXIYLlUP/GAuGjmnUtPj5GOp7qU7sXNgSu+xZUtTkDwTfXFE2Ka0cu+J0Fuc9mKolZchYPvxtJkQWhNoyeoq+s=\"}";
				// strRet =
				// "resultStatus={9000};memo={};result={partner=\"2088701074813762\"&seller=\"2088701074813762\"&out_trade_no=\"2012113026669\"&subject=\"充值\"&body=\"17VEE游戏大厅充值！\"&total_fee=\"0.01\"&notify_url=\"http%3A%2F%2Fssw.market.17vee.com%2Falipayrpd.php\"&success=\"true\"&sign_type=\"RSA\"&sign=\"QM1IDtvrgblCup36L/tFW1KkvI3qWrP4IwXq5wa92CvMsuxtcNM1VaPDKYEgpblQnoWPKZ3h7XWOC4mTkDVq6fvPtoOG0oQT+OcM51ruS3DxLLXGSXqAbMakjORdYqR9UBnYK8y2NwIhsNSKvFgaU3iMmVxQdg27kl6VVndlZYQ=\"}";
				String status = null;

				switch (msg.what) {
				case AlixId.RQF_PAY: {
					//
					closeProgress();

					try {
						String resultStatusStr = "resultStatus={";
						int statusStart = strRet.indexOf("resultStatus={");
						statusStart = statusStart + resultStatusStr.length();
						int statusEnd = strRet.indexOf("};memo=");
						String resultStatus = strRet.substring(statusStart,
								statusEnd);
						String memo = "memo=";
						int imemoStart = strRet.indexOf("memo=");
						imemoStart += memo.length();
						int imemoEnd = strRet.indexOf(";result=");
						memo = strRet.substring(imemoStart, imemoEnd);

						if (memo.indexOf("{") != -1 && memo.indexOf("}") != -1) {
							try {
								memo = memo.substring(memo.indexOf("{") + 1,
										memo.indexOf("}"));
							} catch (NullPointerException e) {
								e.printStackTrace();
							} catch (IndexOutOfBoundsException e) {
								e.printStackTrace();
							}
						}

						ResultChecker resultChecker = new ResultChecker(strRet);

						// int retVal = resultChecker.checkSign();
						// if (retVal == ResultChecker.RESULT_CHECK_SIGN_FAILED)
						// {
						// BaseHelper.showDialog(
						// mActivity,
						// mActivity.getString(R.string.dialogTitle),
						// mActivity.getString(
						// R.string.check_sign_failed),
						// android.R.drawable.ic_dialog_alert);
						// // }
						// else
						if (resultChecker.isPayOk()) {
							Toast.makeText(
									mActivity,
									mActivity
											.getString(R.string.recharge_success),
									Toast.LENGTH_SHORT).show();
							status = VEE_SUCCESS_STATUS;
						} else {

							if (memo == null || "".equals(memo)) {
								return;
							}

							BaseHelper.showDialog(mActivity,
									mActivity.getString(R.string.dialogTitle),
									memo, R.drawable.pay_dialog_info);
							if (ALIPAY_CANCEL_STATUS.endsWith(resultStatus)) {
								status = VEE_CANCEL_STATUS;
							} else {
								status = VEE_ERROR_STATUS;
							}
						}

					} catch (Exception e) {
						e.printStackTrace();
						BaseHelper.showDialog(mActivity, mActivity
								.getString(R.string.dialogTitle), mActivity
								.getString(R.string.recharge_fail_control),
								R.drawable.pay_dialog_info);
						status = VEE_ERROR_STATUS;
					} finally {
						if (status.equals(VEE_SUCCESS_STATUS)) {
							callback.handlePaySuccess();
						} else {
							callback.handlePayFaild();
						}
						// notifyServer(status, outTradeno, money);//
						// ���������Ҫ֪ͨ������?
					}
				}
					break;
				}
				super.handleMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	/**
	 * the OnCancelListener for lephone platform. lephone系统使用到的取消dialog监听
	 */
	public static class AlixOnCancelListener implements
			DialogInterface.OnCancelListener {
		Activity mcontext;

		public AlixOnCancelListener(Activity context) {
			mcontext = context;
		}

		public void onCancel(DialogInterface dialog) {
			mcontext.onKeyDown(KeyEvent.KEYCODE_BACK, null);
		}
	}

	public interface HandleRechargeMessage {
		// void handleMessage(Message msg);
		public void handlePaySuccess();

		public void handlePayFaild();
	}

}

class TaskMessage {
	int errorcode;
	String errorMessage;
}
