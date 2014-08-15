package com.vee.healthplus.util.user;

import android.app.Activity;
import android.os.AsyncTask;

import com.yunfox.s4aservicetest.response.GeneralResponse;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class GetFindVerifyCodeTask extends AsyncTask<Void, Void, Void> {
	private Exception exception;
	private GeneralResponse getFindVerifyCodeRegisterResponse;
	private String phonenumber;
	private Activity activity;
	private GetFindVerifyCodeCallBack callBack;

	public GetFindVerifyCodeTask(Activity activity, String phonenumber,
			GetFindVerifyCodeCallBack callBack) {
		this.activity = activity;
		this.phonenumber = phonenumber;
		this.callBack = callBack;
	}

	@Override
	protected void onPreExecute() {

	}

	@Override
	@SuppressWarnings("unchecked")
	protected Void doInBackground(Void... params) {
		try {
			getFindVerifyCodeRegisterResponse = SpringAndroidService.getInstance(
					activity.getApplication()).getfindpsshortmessageverifycode(
					phonenumber);
			System.out.println("getFindVerifyCodeRegisterResponse="+getFindVerifyCodeRegisterResponse.getReturncode());
		} catch (Exception e) {
			this.exception = e;
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void v) {
		if (exception != null) {
			getFindVerifyCodeRegisterResponse = null;
			callBack.onErrorGetFindVerifyCode(exception);
		}

		if (getFindVerifyCodeRegisterResponse != null) {
			callBack.onFinishGetFindVerifyCode(getFindVerifyCodeRegisterResponse.getReturncode());
		}
	}

	public interface GetFindVerifyCodeCallBack {
		public void onFinishGetFindVerifyCode(int reflag);

		public void onErrorGetFindVerifyCode(Exception exception);
	}
}
