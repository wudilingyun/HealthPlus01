package com.vee.healthplus.util.user;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import com.yunfox.s4aservicetest.response.GeneralResponse;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.web.client.ResourceAccessException;

public class SetNewPwdWithVerifyCodeTask extends
		AsyncTask<Void, Void, GeneralResponse> {
	private Exception exception;
	private String phonenumber;
	private String verifycode;
	private String newpassword;
	private Activity activity;
	private SetNewPwdWithVerifyCodeCallBack callBack;

	public SetNewPwdWithVerifyCodeTask(Activity activity, String phonenumber,
			String newpassword, String verifycode, SetNewPwdWithVerifyCodeCallBack callBack) {
		this.phonenumber = phonenumber;
		this.newpassword = newpassword;
		this.verifycode = verifycode;
		this.activity = activity;
		this.callBack = callBack;
	}

	@Override
	protected void onPreExecute() {

	}

	@Override
	protected GeneralResponse doInBackground(Void... params) {
		// TODO Auto-generated method stub
		try {
			GeneralResponse generalResponse = SpringAndroidService.getInstance(
					activity.getApplication()).updatepasswordwithverifycode(
					phonenumber, newpassword, verifycode);
			return generalResponse;
		} catch (Exception e) {
			Log.e("xuxuxu", e.getMessage());
			this.exception = e;
		}

		return null;
	}

	@Override
	protected void onPostExecute(GeneralResponse generalResponse) {
		if (exception != null) {
			String message;
			if (exception instanceof DuplicateConnectionException) {
				message = "The connection already exists.";
			} else if (exception instanceof ResourceAccessException
					&& exception.getCause() instanceof ConnectTimeoutException) {
				message = "connect time out";
			} else {
				message = "A problem occurred with the network connection. Please try again in a few minutes.";
			}
			callBack.onErrorUpdatePwd(exception);
		}

		if (generalResponse != null) {
			callBack.onFinishUpdatePwd(generalResponse.getReturncode());
		}
	}

	public interface SetNewPwdWithVerifyCodeCallBack {
		public void onFinishUpdatePwd(int reflag);

		public void onErrorUpdatePwd(Exception e);
	}
}
