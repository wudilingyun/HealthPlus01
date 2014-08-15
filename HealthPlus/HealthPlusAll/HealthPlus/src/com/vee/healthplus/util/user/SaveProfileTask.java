package com.vee.healthplus.util.user;

import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;

import android.app.Activity;
import android.os.AsyncTask;

import com.yunfox.s4aservicetest.response.GeneralResponse;
import com.yunfox.s4aservicetest.response.UploadAvatarResponse;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

/**
 * Created by wangjiafeng on 13-11-14.
 */
public class SaveProfileTask extends AsyncTask<Void, Void, Void> {
	private MultiValueMap<String, String> formData;
	private Exception exception;
	private Activity activity;
	private HP_User user;
	private SaveProfileCallBack callBack;
	private String hdpath;
	private GeneralResponse generalResponse;
	private UploadAvatarResponse uploadAvatarResponse;

	public SaveProfileTask(Activity activity, HP_User user,
			SaveProfileCallBack callBack, String hdpath) {
		this.activity = activity;
		this.user = user;
		this.callBack = callBack;
		this.hdpath = hdpath;
	}

	@Override
	protected void onPreExecute() {
		formData = new LinkedMultiValueMap<String, String>();

		String username = user.userName;
		formData.add("username", username);

		String nickname = user.userNick;
		formData.add("nickname", nickname);

		String email = user.email;
		formData.add("email", email);

		String phone = user.phone;
		formData.add("phone", phone);

		String weight = String.valueOf(user.userWeight);
		formData.add("weight", weight);

		String height = String.valueOf(user.userHeight);
		formData.add("height", height);

		String age = String.valueOf(user.userAge);
		formData.add("age", age);

		String remark = user.remark;
		formData.add("remark", remark);

		String gender = String.valueOf(user.userSex);
		formData.add("gender", gender);
	}

	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		try {
			generalResponse = SpringAndroidService.getInstance(
					activity.getApplication()).saveProfile(formData);
			uploadAvatarResponse = SpringAndroidService.getInstance(
					activity.getApplication()).uploadAvatar(hdpath);
		} catch (Exception e) {
			this.exception = e;
			e.printStackTrace();
		}

		return null;
	}

	@Override
	protected void onPostExecute(Void v) {
		if (exception != null) {
			callBack.onErrorSaveProfile(exception);
		}
		if (generalResponse != null) {
			callBack.onFinishSaveProfile(generalResponse.getReturncode());
		}
		if (uploadAvatarResponse != null) {
			callBack.onFinishUploadAvatar(uploadAvatarResponse.getReturncode());
		}
	}

	public interface SaveProfileCallBack {
		public void onFinishSaveProfile(int reflag);

		public void onErrorSaveProfile(Exception e);

		public void onFinishUploadAvatar(int reflag);

		public void onErrorUploadAvatar();
	}

}
