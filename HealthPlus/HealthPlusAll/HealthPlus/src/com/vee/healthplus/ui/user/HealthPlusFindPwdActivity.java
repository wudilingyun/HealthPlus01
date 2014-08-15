package com.vee.healthplus.ui.user;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.web.client.ResourceAccessException;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vee.healthplus.R;
import com.vee.healthplus.activity.BaseFragmentActivity;
import com.vee.healthplus.util.user.GetFindVerifyCodeTask;
import com.vee.healthplus.util.user.GetFindVerifyCodeTask.GetFindVerifyCodeCallBack;
import com.vee.healthplus.util.user.SetNewPwdWithVerifyCodeTask;
import com.vee.healthplus.util.user.SetNewPwdWithVerifyCodeTask.SetNewPwdWithVerifyCodeCallBack;
import com.vee.healthplus.widget.CustomProgressDialog;

@SuppressLint("ResourceAsColor")
public class HealthPlusFindPwdActivity extends BaseFragmentActivity implements
		View.OnClickListener, SetNewPwdWithVerifyCodeCallBack,
		OnFocusChangeListener, GetFindVerifyCodeCallBack {

	private EditText userName_et, yz_et, pwd_et, confirm_et;
	private Button getBtn, submitBtn;
	private ImageView unameImg, yzImg, pwdImg, confirmImg;
	private TextView tipTv;

	private CustomProgressDialog progressDialog = null;

	public HealthPlusFindPwdActivity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		View view = View.inflate(this, R.layout.healthplus_find_layout, null);
		setContainer(view);
		getHeaderView().setHeaderTitle("找回密码");
		setRightBtnVisible(View.GONE);
		setLeftBtnVisible(View.VISIBLE);
		setLeftBtnType(1);
		initView(view);
	}

	private void initView(View view) {

		userName_et = (EditText) view
				.findViewById(R.id.health_plus_find_uname_input_et);
		yz_et = (EditText) view.findViewById(R.id.health_plus_find_yz_input_et);
		getBtn = (Button) view.findViewById(R.id.health_plus_find_get_btn);
		submitBtn = (Button) view
				.findViewById(R.id.health_plus_find_submit_btn);
		pwd_et = (EditText) findViewById(R.id.health_plus_find_pwd_input_et);
		confirm_et = (EditText) findViewById(R.id.health_plus_find_pwd_confirm_input_et);
		unameImg = (ImageView) findViewById(R.id.health_plus_find_uname_img);
		yzImg = (ImageView) findViewById(R.id.health_plus_find_yz_img);
		pwdImg = (ImageView) findViewById(R.id.health_plus_find_pwd_img);
		confirmImg = (ImageView) findViewById(R.id.health_plus_find_pwd_confirm_img);
		tipTv = (TextView) findViewById(R.id.health_plus_find_tip_tv);
		String source = "请输入您的注册手机号码，通过<font color='red'>点击“获取验证码”</font>，我们会将验证码发送至您的手机号码中， 验证码正确提交后，请重新填写新密码。";
		tipTv.setText(Html.fromHtml(source));
		userName_et.setOnFocusChangeListener(this);
		yz_et.setOnFocusChangeListener(this);
		confirm_et.setOnFocusChangeListener(this);
		pwd_et.setOnFocusChangeListener(this);
		getBtn.setOnClickListener(this);
		submitBtn.setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.health_plus_find_get_btn:
			if (userName_et.getText().toString().length() != 0) {
				if (checkMobileNumber(userName_et.getText().toString())) {
					getBtn.setEnabled(false);
					getBtn.setText("正在发送验证码...");
					new GetFindVerifyCodeTask(this, userName_et.getText()
							.toString(), this).execute();
				} else {
					Toast.makeText(this, "手机号不正确", Toast.LENGTH_SHORT).show();
				}

			} else {
				Toast.makeText(this, "手机号码不能为空", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.health_plus_find_submit_btn:
			int s = pwd_et.getText().toString().length();
			Pattern p = Pattern.compile("[0-9]*");
			Matcher m = p.matcher(pwd_et.getText().toString());
			Pattern p1 = Pattern.compile("[a-zA-Z]*");
			Matcher m1 = p1.matcher(pwd_et.getText().toString());
			Pattern p2 = Pattern.compile("[a-z0-9A-Z\\.\\_]*");
			Matcher m2 = p2.matcher(pwd_et.getText().toString());

			if (yz_et.getText().toString().length() == 0) {
				Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			if (checkMobileNumber(userName_et.getText().toString())) {

				if (pwd_et.getText().toString()
						.equals(confirm_et.getText().toString())) {
					if (!m.matches() && !m1.matches() && m2.matches()) {
						if (s >= 6 && s <= 12) {
							new SetNewPwdWithVerifyCodeTask(this, userName_et
									.getText().toString(), pwd_et.getText()
									.toString(), yz_et.getText().toString(),
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
					Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
				}

			} else {
				Toast.makeText(this, "手机号码不正确", Toast.LENGTH_SHORT).show();
			}

			break;
		}
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
	public void onResume() {
		super.onResume();
		progressDialog = CustomProgressDialog.createDialog(this);
		progressDialog.setMessage(this.getString(R.string.registing));
		progressDialog.setCanceledOnTouchOutside(false);
	}

	private void displayResult(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.health_plus_find_uname_input_et:
			if (hasFocus) {
				unameImg.setImageResource(R.drawable.health_plus_uname_focus);
			} else {
				unameImg.setImageResource(R.drawable.health_plus_uname_normal);
			}
			break;

		case R.id.health_plus_find_yz_input_et:
			if (hasFocus) {
				yzImg.setImageResource(R.drawable.health_plus_yz_focus);
			} else {
				yzImg.setImageResource(R.drawable.health_plus_yz_normal);
			}
			break;
		case R.id.health_plus_find_pwd_input_et:
			if (hasFocus) {
				pwdImg.setImageResource(R.drawable.health_plus_pwd_focus);
			} else {
				pwdImg.setImageResource(R.drawable.health_plus_pwd_normal);
			}
			break;
		case R.id.health_plus_find_pwd_confirm_input_et:
			if (hasFocus) {
				confirmImg.setImageResource(R.drawable.health_plus_pwd_focus);
			} else {
				confirmImg.setImageResource(R.drawable.health_plus_pwd_normal);
			}
			break;

		}
	}

	@Override
	public void onFinishGetFindVerifyCode(int reflag) {
		// TODO Auto-generated method stub
		switch (reflag) {
		case 103:
			displayResult("authKey出错");
			break;
		case 104:
			displayResult("参数不全");
			break;
		case 105:
			displayResult("手机格式错误");
			break;

		case 106:
			displayResult("此手机号已经注册");
			break;

		case 107:
			displayResult("此手机号不存在");
			break;

		case 108:
			Toast.makeText(this, "验证短信稍后发送到您手机", Toast.LENGTH_LONG).show();
			break;
		case 109:
			displayResult("短信发送失败");
			break;
		case 110:
			displayResult("短信验证码超时");
			break;
		case 111:
			displayResult("短信验证码不正确");
			break;
		case 112:
			displayResult("短信验证通过");
			break;
		default:
			displayResult("服务器内部错误");

		}
		enableYzBtnHandler.sendEmptyMessageDelayed(1, 1000);
	}

	@Override
	public void onErrorGetFindVerifyCode(Exception exception) {
		// TODO Auto-generated method stub
		if (exception.getCause() instanceof ConnectTimeoutException) {
			System.out.println("ConnectionTimeoutException");
		}
		if (exception instanceof ResourceAccessException) {
			System.out.println("ResourceAccessException");
		}
		displayResult("网络连接出错");
		enableYzBtnHandler.sendEmptyMessageDelayed(1, 1500);
	}

	@SuppressLint("HandlerLeak")
	private Handler enableYzBtnHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			getBtn.setEnabled(true);
			getBtn.setText("获取验证码");
		}
	};

	@Override
	public void onFinishUpdatePwd(int reflag) {
		System.out.print("onFinishUpdatePwd.generalResponse=" + reflag);
		progressDialog.dismiss();
		switch (reflag) {
		case 104:
			displayResult("参数不全");
			break;
		case 105:
			displayResult("手机格式错误");
			break;

		case 106:
			displayResult("此手机号已经注册");
			break;

		case 107:
			displayResult("此手机号不存在");
			break;

		case 108:
			displayResult("短信发送成功");
			break;
		case 109:
			displayResult("短信发送失败");
			break;
		case 110:
			displayResult("短信验证码超时");
			break;
		case 111:
			displayResult("短信验证码不正确");
			break;
		case 112:
			displayResult("短信验证通过");
			break;
		case 103:
			displayResult("通信密钥不正确");
			break;
		case 0:
			displayResult("帐号长度不符合规则(5~50)");
			break;
		case 1:
			displayResult("修改密码失败");
			break;
		case 5:
			displayResult("新密码设置成功，请牢记");
			finish();
			break;
		default:
			displayResult("服务器内部错误");
		}

	}

	@Override
	public void onErrorUpdatePwd(Exception e) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "密码修改失败！", Toast.LENGTH_SHORT).show();
		progressDialog.dismiss();
		finish();
	}

}
