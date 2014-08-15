package com.vee.shop.http;

import java.util.Map;

import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.social.MissingAuthorizationException;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.vee.shop.util.ApplicationUtils;
import com.vee.shop.util.LogUtil;
import com.vee.shop.util.ToastUtil;

public class ProtectTask extends AsyncTask<Void, Void, Map<String, Object>> {

	private static final String TAG = "HttpTask";
	protected ProgressDialog dialog;
	protected String actionUrl;
	protected MultiValueMap<String, String> formData;
	protected Context mContext;
	protected Exception exception;

	public ProtectTask(String actionUrl,
			MultiValueMap<String, String> formData, Context mContext) {
		super();
		this.actionUrl = actionUrl;
		this.formData = formData;
		this.mContext = mContext;
		this.exception = null;
		dialog = new ProgressDialog(mContext, ApplicationUtils.getResId(
				"style", "MyDialog"));
		dialog.setMessage(mContext.getResources().getString(
				ApplicationUtils.getResId("string", "shop_wait_message")));
		dialog.setIndeterminate(true);
		// dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.getWindow().setWindowAnimations(
				ApplicationUtils.getResId("style", "MyDialogAnim"));
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		// dialog.show();
	}

	@Override
	protected Map<String, Object> doInBackground(Void... params) {
		return null;
	}

	public ProgressDialog getDialog() {
		return dialog;
	}

	@Override
	protected void onPostExecute(Map<String, Object> result) {
		if (dialog.isShowing())
			dialog.dismiss();
		if (exception != null) {
			exception.printStackTrace();
			String message = null;
			if (exception instanceof ResourceAccessException
					&& exception.getCause() instanceof ConnectTimeoutException) {
				message = mContext.getResources().getString(
						ApplicationUtils.getResId("string",
								"shop_reg_error_timeout"));
				ToastUtil.show(mContext, message);
			} else if (exception instanceof HttpClientErrorException) {
				HttpClientErrorException httpError = (HttpClientErrorException) exception;
				if (httpError.getStatusCode() == HttpStatus.UNAUTHORIZED) {
					message = mContext.getResources().getString(
							ApplicationUtils.getResId("string",
									"shop_log_error_authorized"));
				}
			} else if (exception instanceof MissingAuthorizationException) {
				message = mContext.getResources().getString(
						ApplicationUtils.getResId("string",
								"shop_log_error_authorized"));
			} else if (exception instanceof DuplicateConnectionException) {
				message = mContext.getResources().getString(
						ApplicationUtils.getResId("string",
								"shop_reg_error_connect_exist"));
				ToastUtil.show(mContext, message);
			} else {
				message = mContext.getResources().getString(
						ApplicationUtils.getResId("string",
								"shop_reg_error_network"));
				ToastUtil.show(mContext, message);
			}
			if (null != message)
				LogUtil.d(TAG, message);
		}
	}
}
