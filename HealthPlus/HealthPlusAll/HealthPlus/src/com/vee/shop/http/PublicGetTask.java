/**
 * 
 */
package com.vee.shop.http;

import java.util.List;

import org.apache.http.NameValuePair;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.vee.shop.util.ApplicationUtils;

public class PublicGetTask extends AsyncTask<Void, Void, String> {

	private String actionUrl;
	private List<NameValuePair> params;
	private Context mContext;
	private ProgressDialog dialog;

	public ProgressDialog getDialog() {
		return dialog;
	}

	public PublicGetTask(String actionUrl, List<NameValuePair> params,
			Context context) {
		super();
		this.actionUrl = actionUrl;
		this.params = params;
		this.mContext = context;
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
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
	}

	@Override
	protected String doInBackground(Void... s) {
		return httpUtil.getJsonString(actionUrl, params);
	}

}
