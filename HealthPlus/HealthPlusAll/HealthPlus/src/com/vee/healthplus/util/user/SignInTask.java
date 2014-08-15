package com.vee.healthplus.util.user;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.http.HttpStatus;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.vee.healthplus.R;
import com.vee.healthplus.heahth_news_utils.CheckNetWorkStatus;
import com.vee.healthplus.ui.user.HealthPlusLoginActivity;
import com.vee.healthplus.util.AppPreferencesUtil;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

/**
 * Created by wangjiafeng on 13-11-14.
 */
public class SignInTask extends AsyncTask<Void, Void, Boolean> {
	private MultiValueMap<String, String> formData;
	private Exception exception;
	private String username;
	private String password;
	private Activity activity;
	private SignInCallBack callBack;

	public SignInTask(Activity activity, String username, String password,
			SignInCallBack callBack) {
		this.activity = activity;
		this.username = username;
		this.password = password;
		this.callBack = callBack;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected Boolean doInBackground(Void... params) {
		boolean result = false;
		try {
			result = SpringAndroidService
					.getInstance(activity.getApplication()).signin(username,
							password);
		} catch (Exception e) {
			this.exception = e;
		}
		return result;
	}

	@Override
	protected void onPostExecute(Boolean v) {
		if (exception != null) {
			String message;
			if (exception instanceof HttpClientErrorException
					&& ((((HttpClientErrorException) exception).getStatusCode() == HttpStatus.BAD_REQUEST) || ((HttpClientErrorException) exception)
							.getStatusCode() == HttpStatus.UNAUTHORIZED)) {
				message = "Auth failure,Your email or password was entered incorrectly.";
			} else if (exception instanceof DuplicateConnectionException) {
				message = "The connection already exists.";
			} else {
				message = "A problem occurred with the network connection. Please try again in a few minutes.";
			}
			callBack.onErrorSignIn(exception);
		} else {
			AppPreferencesUtil.setBooleanPref(activity,
					"isQQLogin", false);
			if (v) {
				callBack.onFinishSignIn();
			} else {
				Toast.makeText(
						activity,
						activity.getResources().getString(
								R.string.hp_userloginerror), Toast.LENGTH_LONG)
						.show();
			}
		}
	}

	public interface SignInCallBack {
		public void onFinishSignIn();

		public void onErrorSignIn(Exception e);
	}
}
