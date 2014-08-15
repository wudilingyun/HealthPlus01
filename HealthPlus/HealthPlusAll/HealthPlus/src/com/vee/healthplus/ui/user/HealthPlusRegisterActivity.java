package com.vee.healthplus.ui.user;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.web.client.ResourceAccessException;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.vee.healthplus.R;
import com.vee.healthplus.activity.BaseFragmentActivity;
import com.vee.healthplus.util.user.GetVerifyCodeTask;
import com.vee.healthplus.util.user.GetVerifyCodeTask.GetVerifyCodeCallBack;
import com.vee.healthplus.util.user.RegisterTask;
import com.vee.healthplus.util.user.SignInTask;
import com.vee.healthplus.widget.CustomProgressDialog;
import com.vee.healthplus.widget.HeaderView;

@SuppressLint("ResourceAsColor")
public class HealthPlusRegisterActivity extends BaseFragmentActivity implements
		View.OnClickListener, RegisterTask.RegisterCallBack,
		SignInTask.SignInCallBack, OnFocusChangeListener, GetVerifyCodeCallBack {

	private EditText userName_et, userPwd_et, userPwdConfirm_et, yz_et,
			nick_et;
	private CheckBox agreeBox;
	private Button readBtn, register_btn, yzBtn;

	private CustomProgressDialog progressDialog = null;
	private ImageView uname_img, pwd_img1, pwd_img2, yz_img, nick_iv;

	public HealthPlusRegisterActivity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		View view = View.inflate(this, R.layout.healthplus_regsiter_layout,
				null);
		setContainer(view);
		getHeaderView().setHeaderTitle("注册");
		getHeaderView().setHeaderTitleColor(
				R.color.register_headview_text_color_white);
		getHeaderView().setBackGroundColor(
				R.color.register_headview_bg_color_black);
		setRightBtnVisible(View.GONE);
		setLeftBtnVisible(View.VISIBLE);
		setLeftBtnType(HeaderView.HEADER_BACK);
		setLeftBtnRes(R.drawable.healthplus_headview_back_btn);
		initView(view);
	}

	private void initView(View view) {

		userName_et = (EditText) view
				.findViewById(R.id.health_plus_register_uname_input_et);
		userPwd_et = (EditText) view
				.findViewById(R.id.health_plus_register_pwd_input_et);
		userPwdConfirm_et = (EditText) view
				.findViewById(R.id.health_plus_register_pwd_confirm_input_et);
		yz_et = (EditText) view
				.findViewById(R.id.health_plus_register_yz_input_et);
		yzBtn = (Button) view
				.findViewById(R.id.health_plus_register_get_yz_btn);
		register_btn = (Button) view
				.findViewById(R.id.health_plus_register_btn);
		register_btn.setEnabled(true);
		readBtn = (Button) view.findViewById(R.id.health_plus_register_read);
		readBtn.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		yzBtn.setOnClickListener(this);
		register_btn.setOnClickListener(this);
		readBtn.setOnClickListener(this);
		agreeBox = (CheckBox) view
				.findViewById(R.id.health_plus_register_agree_box);

		uname_img = (ImageView) view
				.findViewById(R.id.health_plus_register_uname_img);
		pwd_img1 = (ImageView) view
				.findViewById(R.id.health_plus_register_pwd_img);
		pwd_img2 = (ImageView) view
				.findViewById(R.id.health_plus_register_pwd_confirm_img);
		yz_img = (ImageView) view
				.findViewById(R.id.health_plus_register_yz_img);
		nick_et = (EditText) view
				.findViewById(R.id.health_plus_register_nick_input_et);
		nick_iv = (ImageView) view
				.findViewById(R.id.health_plus_register_nick_img);
		userName_et.setOnFocusChangeListener(this);
		userPwd_et.setOnFocusChangeListener(this);
		userPwdConfirm_et.setOnFocusChangeListener(this);
		yz_et.setOnFocusChangeListener(this);
		nick_et.setOnFocusChangeListener(this);

		agreeBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				register_btn.setEnabled(isChecked ? true : false);

			}
		});
	}

	private static boolean checkMobileNumber(String mobileNumber) {
		boolean flag = false;
		try {
			Pattern regex = Pattern.compile("^1[3|5|8][0-9]{9}$");
			Matcher matcher = regex.matcher(mobileNumber);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.health_plus_register_get_yz_btn:
			if (userName_et.getText().toString().length() != 0) {
				if (checkMobileNumber(userName_et.getText().toString())) {
					yzBtn.setEnabled(false);
					yzBtn.setText("正在发送验证码...");
					new GetVerifyCodeTask(this, userName_et.getText()
							.toString(), this).execute();
				} else {
					Toast.makeText(this, "手机号不正确", Toast.LENGTH_SHORT).show();
				}

			} else {
				Toast.makeText(this, "手机号码不能为空", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.health_plus_register_btn:
			int s = userPwd_et.getText().toString().length();
			Pattern p = Pattern.compile("[0-9]*");
			Matcher m = p.matcher(userPwd_et.getText().toString());
			Pattern p1 = Pattern.compile("[a-zA-Z]*");
			Matcher m1 = p1.matcher(userPwd_et.getText().toString());
			Pattern p2 = Pattern.compile("[a-z0-9A-Z\\.\\_]*");
			Matcher m2 = p2.matcher(userPwd_et.getText().toString());
			String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
			Pattern p3 = Pattern.compile(regEx);
			Matcher m3 = p3.matcher(nick_et.getText().toString().trim());
			boolean ok = (m3.replaceAll("").trim().length()) == nick_et
					.getText().toString().trim().length();

			if (yz_et.getText().toString().length() == 0) {
				Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			if (nick_et.getText().toString().length() == 0) {
				Toast.makeText(this, "昵称不能为空", Toast.LENGTH_SHORT).show();
				return;
			}

			if (checkMobileNumber(userName_et.getText().toString())) {
				if (nick_et.getText().toString().trim().length() >= 4
						&& nick_et.getText().toString().trim().length() <= 30
						&& ok) {
					if (checkNick(nick_et.getText().toString().trim())) {
						if (userPwd_et.getText().toString()
								.equals(userPwdConfirm_et.getText().toString())) {
							if (!m.matches() && !m1.matches() && m2.matches()) {
								if (s >= 6 && s <= 12) {
									new RegisterTask(this, userName_et
											.getText().toString(), userPwd_et
											.getText().toString(), yz_et
											.getText().toString(), nick_et
											.getText().toString().trim(), this,
											this).execute();
									progressDialog.show();
								} else {
									Toast.makeText(
											this,
											getResources()
													.getString(
															R.string.user_password_length_toast),
											Toast.LENGTH_SHORT).show();
								}
							} else {
								Toast.makeText(
										this,
										getResources().getString(
												R.string.user_password_toast),
										Toast.LENGTH_SHORT).show();
							}
						} else {
							Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT)
									.show();
						}
					} else {
						Toast.makeText(this, "请重新编辑昵称", Toast.LENGTH_SHORT)
								.show();
					}
				} else {
					Toast.makeText(this, "昵称格式不正确", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(this, "手机号码不正确", Toast.LENGTH_SHORT).show();
			}

			break;
		case R.id.health_plus_register_read:
			Intent intent = new Intent(HealthPlusRegisterActivity.this,
					StatementDetailsTextActivity.class);
			startActivity(intent);
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		progressDialog = CustomProgressDialog.createDialog(this);
		progressDialog.setMessage(this.getString(R.string.registing));
		progressDialog.setCanceledOnTouchOutside(false);
	}

	private void displayShortMsg(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	private void displayRegisterResult(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onFinishRegister(int reflag) {

		switch (reflag) {
		case 103:
			displayRegisterResult("authKey出错");
			break;
		case 104:
			displayRegisterResult("参数不全");
			break;
		case 105:
			displayRegisterResult("手机格式错误");
			break;

		case 106:
			displayRegisterResult("此手机号已经注册");
			break;

		case 107:
			displayRegisterResult("此手机号不存在");
			break;

		case 108:
			displayRegisterResult("短信发送成功");
			break;
		case 109:
			displayRegisterResult("短信发送失败");
			break;
		case 110:
			displayRegisterResult("短信验证码已过期，请重新获取验证码");
			break;
		case 111:
			displayRegisterResult("短信验证码不正确");
			break;
		case 112:
			displayRegisterResult("短信验证通过");
			break;
		case 202:
			displayRegisterResult("注册帐号长度非法");
			break;
		case 203:
			displayRegisterResult("通信密钥不正确");
			break;
		case 204:
			displayRegisterResult("注册帐号非法，注册帐号必须是数字和字母组合，不能包含非法字符,@除外");
			break;
		case 700:
			displayRegisterResult("注册昵称为空");
			break;
		case 701:
			displayRegisterResult("昵称已存在");
			break;
		case 1:
			displayRegisterResult("此手机号已经注册");
			break;

		case 5:
			displayRegisterResult("用户基本信息写入失败");
			break;

		case 7:
			displayRegisterResult("用户扩展信息写入失败");
			break;
		case 8:// 注册成功
			displayRegisterResult("注册成功");
			break;
		default:
			displayRegisterResult("服务器内部注册错误");
			break;
		}
		progressDialog.dismiss();

	}

	@Override
	public void onErrorRegister(Exception exception) {
		progressDialog.dismiss();

		if (exception != null) {
			String message;
			if (exception instanceof ResourceAccessException
					&& exception.getCause() instanceof ConnectTimeoutException) {
				message = "连接超时";
			} else if (exception instanceof DuplicateConnectionException) {
				message = "连接已存在";
			} else {
				message = "网络连接错误";
			}
			displayShortMsg(message);
		}
	}

	@Override
	public void onFinishSignIn() {
		displayRegisterResult(getResources().getString(
				R.string.hp_userlogin_success));
		progressDialog.dismiss();
		// startActivity(new Intent(this, MainPage.class));
		Intent intent = new Intent();
		intent.setClass(this, HealthPlusPersonalInfoEditActivity.class);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			intent.putExtras(extras);
		}
		startActivity(intent);
		finish();
	}

	@Override
	public void onErrorSignIn(Exception e) {
		progressDialog.dismiss();
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.health_plus_register_uname_input_et:
			if (hasFocus) {
				uname_img.setImageResource(R.drawable.health_plus_uname_focus);
			} else {
				uname_img.setImageResource(R.drawable.health_plus_uname_normal);
			}
			break;

		case R.id.health_plus_register_yz_input_et:
			if (hasFocus) {
				yz_img.setImageResource(R.drawable.health_plus_yz_focus);
			} else {
				yz_img.setImageResource(R.drawable.health_plus_yz_normal);
			}
			break;
		case R.id.health_plus_register_pwd_input_et:
			if (hasFocus) {
				pwd_img1.setImageResource(R.drawable.health_plus_pwd_focus);
			} else {
				pwd_img1.setImageResource(R.drawable.health_plus_pwd_normal);
			}
			break;
		case R.id.health_plus_register_pwd_confirm_input_et:
			if (hasFocus) {
				pwd_img2.setImageResource(R.drawable.health_plus_pwd_focus);
			} else {
				pwd_img2.setImageResource(R.drawable.health_plus_pwd_normal);
			}
			break;
		case R.id.health_plus_register_nick_input_et:
			if (hasFocus) {
				nick_iv.setImageResource(R.drawable.health_plus_nick_focus);
			} else {
				nick_iv.setImageResource(R.drawable.health_plus_nick_normal);
			}
			break;
		}

	}

	@Override
	public void onFinishGetVerifyCode(int reg17FoxReturn) {
		// TODO Auto-generated method stub

		switch (reg17FoxReturn) {
		case 103:
			displayRegisterResult("authKey出错");
			break;
		case 104:
			displayRegisterResult("参数不全");
			break;
		case 105:
			displayRegisterResult("手机格式错误");
			break;
		case 106:
			displayRegisterResult("此手机号已经注册");
			break;
		case 107:
			displayRegisterResult("此手机号不存在");
			break;
		case 108:
			Toast.makeText(this, "验证短信稍后发送到您手机", Toast.LENGTH_LONG).show();
			break;
		case 109:
			displayRegisterResult("短信发送失败");
			break;
		case 110:
			displayRegisterResult("短信验证码超时，请重新获取验证码");
			break;
		case 111:
			displayRegisterResult("短信验证码不正确");
			break;
		case 112:
			displayRegisterResult("短信验证通过");
			break;
		default:
			displayRegisterResult("服务器内部错误");
		}
		enableYzBtnHandler.sendEmptyMessageDelayed(1, 1000);
	}

	@Override
	public void onErrorGetVerifyCode(Exception exception) {
		// TODO Auto-generated method stub
		if (exception.getCause() instanceof ConnectTimeoutException) {
			System.out.println("ConnectionTimeoutException");
		}
		if (exception instanceof ResourceAccessException) {
			System.out.println("ResourceAccessException");
		}
		displayShortMsg("网络连接出错");
		enableYzBtnHandler.sendEmptyMessageDelayed(1, 1500);
	}

	private Handler enableYzBtnHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			yzBtn.setEnabled(true);
			yzBtn.setText("获取验证码");
		}
	};

	public boolean checkNick(String str) {
		boolean flag = true;
		InputStream in = null;
		BufferedReader reader = null;
		try {
			in = getResources().getAssets().open("minganci.txt");
			reader = new BufferedReader(new InputStreamReader(in));
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (str.indexOf(line) != -1) {
					System.err.println("名称非法，包含敏感词： " + line);
					flag = false;
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return flag;
	}

}
