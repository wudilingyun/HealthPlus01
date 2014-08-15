package com.vee.healthplus.ui.user;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.social.greenhouse.api.Profile;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.vee.healthplus.R;
import com.vee.healthplus.ui.main.MainPage;
import com.vee.healthplus.util.AppPreferencesUtil;
import com.vee.healthplus.util.user.GetProfileTask;
import com.vee.healthplus.util.user.HP_DBModel;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.util.user.QueryAllDayRecordByType;
import com.vee.healthplus.util.user.SignInTask;
import com.vee.healthplus.widget.CustomProgressDialog;
import com.yunfox.s4aservicetest.response.DayRecord;
import com.yunfox.s4aservicetest.response.RegisterResponse;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

@SuppressLint("HandlerLeak")
public class HealthPlusLoginActivity extends Activity implements
		View.OnClickListener, SignInTask.SignInCallBack,
		GetProfileTask.GetProfileCallBack {
	private EditText userName_et, userPwd_et;
	private CustomProgressDialog progressDialog = null;
	private LinearLayout input_ll;
	private ResizeLayout root_layout;
	private Button register_btn;
	private Button login_btn;
	private Button forget_btn, enterWithoutLogin_btn, qqLogin_btn;
	private TextView register_tv;
	private ImageView uname_img, pwd_img;
	public static Tencent mTencent;
	private UserInfo mInfo;
	private String qqNick;
	private static boolean qqFirstLogin;
	public static final String QQappid = "1101615333";

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			if (msg.getData().getInt("height") < getResources()
					.getDisplayMetrics().heightPixels * 2 / 3) {
				lp.setMargins(0, 0, 0, 0);
				lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
						RelativeLayout.TRUE);
				input_ll.setLayoutParams(lp);
				register_tv.setVisibility(View.GONE);
				register_btn.setVisibility(View.GONE);
				qqLogin_btn.setVisibility(View.GONE);
			} else {
				lp.setMargins(
						0,
						0,
						0,
						dip2px(Integer.valueOf(getResources().getString(
								R.string.health_plus_login_marginbottom))));
				lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
						RelativeLayout.TRUE);
				input_ll.setLayoutParams(lp);
				register_tv.setVisibility(View.VISIBLE);
				register_btn.setVisibility(View.VISIBLE);
				qqLogin_btn.setVisibility(View.VISIBLE);
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.healthplus_login_layout);
		mTencent = Tencent.createInstance(QQappid, this);
		initView();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (HP_User.getOnLineUserId(HealthPlusLoginActivity.this) != 0) {
			finish();
		}
		progressDialog = CustomProgressDialog.createDialog(this);
		progressDialog.setMessage("正在登录...");
		progressDialog.setCanceledOnTouchOutside(false);
	}

	private void initView() {
		root_layout = (ResizeLayout) findViewById(R.id.loginRootLayout);
		root_layout.setHandler(mHandler);
		input_ll = (LinearLayout) findViewById(R.id.health_plus_login_input_ll);
		userName_et = (EditText) findViewById(R.id.health_plus_uname_input_et);
		userPwd_et = (EditText) findViewById(R.id.health_plus_pwd_input_et);
		register_btn = (Button) findViewById(R.id.health_plus_goto_register_btn);
		login_btn = (Button) findViewById(R.id.health_plus_login_btn);
		register_tv = (TextView) findViewById(R.id.health_plus_register_text);
		forget_btn = (Button) findViewById(R.id.health_plus_forgetPwd_btn);
		enterWithoutLogin_btn = (Button) findViewById(R.id.health_plus_enterwithoutlogin_btn);
		qqLogin_btn = (Button) findViewById(R.id.health_plus_qqlogin_btn);
		forget_btn.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		enterWithoutLogin_btn.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		uname_img = (ImageView) findViewById(R.id.health_plus_uname_img);
		pwd_img = (ImageView) findViewById(R.id.health_plus_pwd_img);
		userName_et.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					uname_img
							.setImageResource(R.drawable.health_plus_uname_focus);
				} else {
					uname_img
							.setImageResource(R.drawable.health_plus_uname_normal);
				}
			}
		});

		userPwd_et.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					pwd_img.setImageResource(R.drawable.health_plus_pwd_focus);
				} else {
					pwd_img.setImageResource(R.drawable.health_plus_pwd_normal);
				}
			}
		});
		forget_btn.setOnClickListener(this);
		register_btn.setOnClickListener(this);
		login_btn.setOnClickListener(this);
		enterWithoutLogin_btn.setOnClickListener(this);
		qqLogin_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.health_plus_goto_register_btn:
			Intent intent = new Intent();
			intent.setClass(this, HealthPlusRegisterActivity.class);
			Bundle extras = getIntent().getExtras();
			if (extras != null) {
				intent.putExtras(extras);
			}
			setResult(305);
			startActivity(intent);
			break;
		case R.id.health_plus_login_btn:
			if (validateFormData()) {
				new SignInTask(this, userName_et.getText().toString(),
						userPwd_et.getText().toString(), this).execute();
				progressDialog.show();
			} else {

			}
			break;
		case R.id.health_plus_forgetPwd_btn:
			Intent intent2 = new Intent();
			intent2.setClass(this, HealthPlusFindPwdActivity.class);
			startActivity(intent2);
			break;
		case R.id.health_plus_enterwithoutlogin_btn:
			if (AppPreferencesUtil.getBooleanPref(this, "isFirstShowLogin",
					true)) {
				Intent intent3 = new Intent();
				intent3.setClass(this, MainPage.class);
				startActivity(intent3);
			} else {
				setResult(RESULT_CANCELED);
			}
			finish();
			break;
		case R.id.health_plus_qqlogin_btn:
			setResult(305);
			onClickLogin();
			break;
		}
	}

	private void onClickLogin() {
		if (mTencent == null) {
			Log.e("lingyun", "mTencent=null");
			return;
		}
		if (!mTencent.isSessionValid()) {
			IUiListener listener = new BaseUiListener() {
				@Override
				protected void doComplete(JSONObject values) {

					if (mTencent != null && mTencent.isSessionValid()
							&& mTencent.getOpenId() != null) {
						Log.i("lingyun",
								"mTencent.getOpenId():" + mTencent.getOpenId());
						getUserInfo();
					}
				}
			};
			mTencent.login(this, "all", listener);
			Log.i("lingyun", "FirstLaunch_SDK:" + SystemClock.elapsedRealtime());
		} else {
			Log.i("lingyun", "mTencent:logout");
			mTencent.logout(this);
		}
	}

	private void getUserInfo() {

		IUiListener listener = new IUiListener() {
			@Override
			public void onError(UiError e) {

			}

			@Override
			public void onComplete(final Object response) {
				if (((JSONObject) response).has("nickname")) {
					try {
						qqNick = ((JSONObject) response).getString("nickname");
						Log.i("lingyun", "qqNick=" + qqNick);
						if (qqNick != null) {
							new QQSignInTask(HealthPlusLoginActivity.this,
									mTencent.getOpenId()).execute();

						} else {
							Log.i("lingyun", "qqNick=null");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

			}

			@Override
			public void onCancel() {

			}
		};
		mInfo = new UserInfo(this, mTencent.getQQToken());
		mInfo.getUserInfo(listener);

	}

	private class BaseUiListener implements IUiListener {

		@Override
		public void onComplete(Object response) {
			Log.i("lingyun", "qq login response= \n" + response.toString());
			progressDialog.show();
			doComplete((JSONObject) response);
		}

		protected void doComplete(JSONObject values) {

		}

		@Override
		public void onError(UiError e) {
			Log.e("lingyun", "e.errorDetail=" + e.errorDetail
					+ "\n e.errorCode=" + e.errorCode);
			displayLoginResult("网络异常");
		}

		@Override
		public void onCancel() {
			//displayLoginResult("onCancel: ");
		}
	}

	private boolean validateFormData() {
		String uname = userName_et.getText().toString().trim();
		String password = userPwd_et.getText().toString().trim();
		if (uname.length() == 0) {
			displayAppAuthorizationError("用户名不能为空");
			return false;
		}
		if (password.length() == 0) {
			displayAppAuthorizationError("密码不能为空");
			return false;
		}
		if (uname.length() == 11 && password.length() <= 12
				&& password.length() >= 6) {
			return true;
		} else if (uname.length() != 11) {
			displayAppAuthorizationError("用户名必须为11位手机号码");
			return false;
		} else {
			displayAppAuthorizationError("用户名或密码输入不正确");
			return false;
		}
	}

	private void displayAppAuthorizationError(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

	private void displayLoginResult(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onFinishSignIn() {
		progressDialog.dismiss();
		new GetProfileTask(this, this).execute();
	}

	@Override
	public void onErrorSignIn(Exception exception) {
		Log.e("loginerro", exception.toString());
		progressDialog.dismiss();
		if (exception != null) {
			String message;
			if (exception instanceof HttpClientErrorException
					&& ((((HttpClientErrorException) exception).getStatusCode() == HttpStatus.BAD_REQUEST) || ((HttpClientErrorException) exception)
							.getStatusCode() == HttpStatus.UNAUTHORIZED)) {
				// message =
				// "Auth failure,Your email or password was entered incorrectly.";
				message = "用户名或者密码错误";
			} else if (exception instanceof ResourceAccessException
					&& exception.getCause() instanceof ConnectTimeoutException) {
				// message = "connect time out";
				message = "连接超时";
			} else if (exception instanceof DuplicateConnectionException) {
				// message = "The connection already exists.";
				message = "连接已存在";
			} else {
				// message =
				// "A problem occurred with the network connection. Please try again in a few minutes.";
				message = "网络连接错误";
			}
			displayLoginResult(message);
		}

	}

	@Override
	public void onFinishGetProfile(Profile profile) {
		HP_User user = new HP_User();
		user.userId = profile.getMemberid();
		user.userAge = profile.getAge();
		user.userName = profile.getUsername();
		user.userNick = profile.getNickname();
		user.userHeight = profile.getHeight();
		user.userWeight = profile.getWeight();
		user.userSex = profile.getGender();
		user.email = profile.getEmail();
		user.phone = profile.getPhone();
		user.remark = profile.getRemark();
		Log.i("lingyun",
				"profile.getRawavatarurl()=" + profile.getRawavatarurl()
						+ "profile.getRemark();=" + profile.getRemark());
		if (profile.getRawavatarurl() == "") {
			user.photourl = "http://www.mobifox.cn:12080/mm/default.jpg";
		} else {
			user.photourl = profile.getRawavatarurl();
		}
		HP_DBModel.getInstance(this).insertUserInfo(user, true);
		HP_User.setOnLineUserId(this, profile.getMemberid());
		displayLoginResult(this.getResources().getString(
				R.string.hp_userlogin_success));
		progressDialog.dismiss();
		Bundle b = getIntent().getExtras();
		if (b != null) {
			Intent i = new Intent();
			i.setComponent((ComponentName) b.getParcelable("cn"));
			Log.i("lingyun", "start LoginActivity ComponentName="
					+ ((ComponentName) b.getParcelable("cn")).toString());
			startActivity(i);
		} else {
			Log.i("lingyun", "start LoginActivity ComponentName=null");
			setResult(RESULT_OK);
		}
		finish();

	}

	@Override
	public void onErrorGetProfile(Exception e) {
		progressDialog.dismiss();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		setResult(RESULT_CANCELED);
		finish();
	}

	private int dip2px(float dpValue) {
		final float scale = getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	class QQRegisterTask extends AsyncTask<Void, Void, Void> {
		private Exception exception;
		private RegisterResponse registerResponse;
		private String openId;
		private String nick;
		private Activity activity;

		public QQRegisterTask(Activity activity, String openId, String nick) {
			this.activity = activity;
			this.openId = openId;
			this.nick = nick;
		}

		@Override
		protected void onPreExecute() {

		}

		@Override
		@SuppressWarnings("unchecked")
		protected Void doInBackground(Void... params) {
			Log.i("lingyun", "QQRegisterTask doInBackground");
			try {
				registerResponse = SpringAndroidService.getInstance(
						activity.getApplication()).registerqq(openId,
						"a123456", nick);
				Log.i("lingyun", "QQRegisterTask registerResponse="
						+ registerResponse.getReturncode());
			} catch (Exception e) {
				this.exception = e;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void v) {
			if (exception != null) {
				Log.i("lingyun", "onPostExecute QQRegisterTask exception");
				String message;
				if (exception instanceof HttpClientErrorException) {
				}
				if (exception instanceof HttpClientErrorException
						&& ((((HttpClientErrorException) exception)
								.getStatusCode() == HttpStatus.BAD_REQUEST) || ((HttpClientErrorException) exception)
								.getStatusCode() == HttpStatus.UNAUTHORIZED)) {
					message = "Your email or password was entered incorrectly.";
				} else if (exception instanceof DuplicateConnectionException) {
					message = "The connection already exists.";
				} else if (exception instanceof ResourceAccessException
						&& exception.getCause() instanceof ConnectTimeoutException) {
					message = "connect time out";
				} else {
					message = "A problem occurred with the network connection. Please try again in a few minutes.";
				}
				registerResponse = null;
			}

			if (registerResponse != null) {
				if (registerResponse.getReturncode() == 8) {
					Log.i("lingyun", "QQregister ok");
					HP_User user = new HP_User();
					user.userName = openId;
					user.userNick = nick;
					user.userId = Integer.valueOf(String
							.valueOf(registerResponse.getMemberid()));
					HP_DBModel.getInstance(activity).insertUserInfo(user, true);
					HP_User.setOnLineUserId(activity, Integer.valueOf(String
							.valueOf(registerResponse.getMemberid())));
					new QQSignInTask(activity, openId).execute();
				}
			}
		}

	}

	class QQSignInTask extends AsyncTask<Void, Void, Boolean> {
		private Exception exception;
		private String openId;
		private Activity activity;

		public QQSignInTask(Activity activity, String openId) {
			Log.i("lingyun", "QQSignInTask:");
			this.activity = activity;
			this.openId = openId;
		}

		@Override
		protected void onPreExecute() {

		}

		protected Boolean doInBackground(Void... params) {
			boolean result = false;
			try {
				Log.i("lingyun", "QQSignInTask.doInBackground:");
				result = SpringAndroidService.getInstance(
						activity.getApplication()).signinqq(openId, "a123456");
			} catch (Exception e) {
				this.exception = e;
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(Boolean v) {
			if (exception != null) {
				Log.i("lingyun", "onPostExecute.exception");
				if (exception instanceof HttpClientErrorException
						&& ((((HttpClientErrorException) exception)
								.getStatusCode() == HttpStatus.BAD_REQUEST) || ((HttpClientErrorException) exception)
								.getStatusCode() == HttpStatus.UNAUTHORIZED)) {
					Log.e("lingyun", "QQSignInTask.HttpClientErrorException");
					qqFirstLogin = true;
					new QQRegisterTask(activity, openId, qqNick).execute();
				} else if (exception instanceof ResourceAccessException
						&& exception.getCause() instanceof ConnectTimeoutException) {
					// message = "连接超时";
				} else if (exception instanceof DuplicateConnectionException) {
					// message = "连接已存在";
				} else {
					// message = "网络连接错误";
				}

			} else {
				if (v) {
					AppPreferencesUtil.setBooleanPref(
							HealthPlusLoginActivity.this, "isQQLogin", true);
					Log.i("lingyun", "qqFirstLogin=" + qqFirstLogin);
					if (qqFirstLogin) {
						progressDialog.dismiss();
						Intent intent = new Intent();
						intent.setClass(activity,
								HealthPlusPersonalInfoEditActivity.class);
						Bundle extras = getIntent().getExtras();
						if (extras != null) {
							intent.putExtras(extras);
						}
						startActivity(intent);
						finish();
						qqFirstLogin = false;
					} else {
						new GetProfileTask(activity,
								(GetProfileTask.GetProfileCallBack) activity)

						.execute();
					}

				} else {

				}
			}
		}

	}

}
