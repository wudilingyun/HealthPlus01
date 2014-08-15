package com.vee.shop.http;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.social.MissingAuthorizationException;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.vee.healthplus.common.MyApplication;
import com.vee.shop.bean.CartItemBean;
import com.vee.shop.util.Constants;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class GetCartTask extends ProtectTask {

	private Activity activity;

	public GetCartTask(String actionUrl,
			MultiValueMap<String, String> formData, Context mContext,
			Activity activity) {
		super(actionUrl, formData, mContext);
		this.actionUrl = Constants.ACCOUNT_QUERY_CART_URL;
		this.activity = activity;
	}

	@Override
	protected Map<String, Object> doInBackground(Void... params) {
		try {
			return (SpringAndroidService.getInstance(activity.getApplication())
					.handleProtect(actionUrl, formData, Constants.HTTP_TYPE_GET));
		} catch (Exception e) {
			this.exception = e;
			return null;
		}
	}

	@Override
	protected void onPostExecute(Map<String, Object> result) {
		super.onPostExecute(result);
		if (exception != null) {
			if (exception instanceof HttpClientErrorException) {
				HttpClientErrorException httpError = (HttpClientErrorException) exception;
				if (httpError.getStatusCode() == HttpStatus.UNAUTHORIZED) {
					// go login
					// ((BaseActivity) mContext).signOut();
				}
			} else if (exception instanceof MissingAuthorizationException) {
				// go login
				// ((BaseActivity) mContext).signOut();
			}
		} else {
			if (null != result) {
				super.onPostExecute(result);
				if ((!TextUtils.isEmpty(result.toString()))
						&& (!result.toString().equals("null"))) {
					int count = 0;
					HashMap<String, CartItemBean> cartMap = httpUtil
							.parseCartMapNew(result);
					if (null != cartMap) {
						for (Map.Entry<String, CartItemBean> entry : cartMap
								.entrySet()) {
							count = count
									+ Integer.parseInt(entry.getValue()
											.getCount());
						}
					} else {
						count = 0;
					}
					MyApplication.setCartMap(cartMap);
					MyApplication.setCartNum(count);
				}

			}
		}

	}
}
