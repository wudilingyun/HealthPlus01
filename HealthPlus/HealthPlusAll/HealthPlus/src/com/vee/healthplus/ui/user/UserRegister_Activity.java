package com.vee.healthplus.ui.user;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vee.healthplus.R;
import com.vee.healthplus.util.user.RegisterTask;
import com.vee.healthplus.util.user.SignInTask;
import com.vee.healthplus.widget.CustomDialog;
import com.vee.healthplus.widget.CustomProgressDialog;

/**
 * Created by wangjiafeng on 13-11-11.
 */
public class UserRegister_Activity extends DialogFragment implements
		View.OnClickListener, RegisterTask.RegisterCallBack,
		SignInTask.SignInCallBack {

	private Context mContext;
	private EditText userName_et, userPwd_et, userNick_et;

	private CustomProgressDialog progressDialog = null;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		this.mContext = getActivity();
		View view = View.inflate(mContext, R.layout.hp_userregsiter, null);
		CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
		builder.setTitle(R.string.hp_userreg_register);
		initView(view);
		builder.setContentView(view);
		return builder.create();
	}

	private void initView(View view) {
		userName_et = (EditText) view.findViewById(R.id.userName_et);
		userPwd_et = (EditText) view.findViewById(R.id.userPwd_et);
		userNick_et = (EditText) view.findViewById(R.id.userNick_et);
		Button register_btn = (Button) view.findViewById(R.id.register_btn);
		register_btn.setOnClickListener(this);
		Button cannel_btn = (Button) view.findViewById(R.id.cannel_btn);
		cannel_btn.setOnClickListener(this);
		String digits = getResources().getString(R.string.user_resgiter_edit);
		userPwd_et.setKeyListener(DigitsKeyListener.getInstance(digits));
		userName_et.setKeyListener(DigitsKeyListener.getInstance(digits));
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.register_btn:
			int s = userPwd_et.getText().toString().length();
			Pattern p = Pattern.compile("[0-9]*");
			Matcher m = p.matcher(userPwd_et.getText().toString());
			Pattern p1 = Pattern.compile("[a-zA-Z]*");
			Matcher m1 = p1.matcher(userPwd_et.getText().toString());

			int s1 = userName_et.getText().toString().length();

				if (s1 >= 6 && s1 <= 15) {
					if (!m.matches() && !m1.matches()) {
						if (s >= 6 && s <= 15) {
							/*new RegisterTask(getActivity(), userName_et
									.getText().toString(), userPwd_et.getText()
									.toString(), userNick_et.getText()
									.toString(), this, this).execute();*/
							progressDialog.show();
						} else {
							Toast.makeText(
									getActivity(),
									getResources()
											.getString(
													R.string.user_password_length_toast),
									Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(
								getActivity(),
								getResources().getString(
										R.string.user_password_toast),
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(
							getActivity(),
							getResources()
									.getString(
											R.string.user_name_length_toast),
							Toast.LENGTH_SHORT).show();
				}

			

			break;
		case R.id.cannel_btn:
			dismiss();
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		progressDialog = CustomProgressDialog.createDialog(this.getActivity());
		progressDialog.setMessage(this.getString(R.string.registing));
	}

	private void displayRegisterError(String message) {
		// new
		// AlertDialog.Builder(mContext).setMessage(message).setCancelable(false)
		// .setPositiveButton("OK", null).create().show();
	}

	private void displayRegisterResult(String msg) {
		Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onFinishRegister(int reflag) {
		switch (reflag) {
		case 8:
			// 注册成功
			displayRegisterResult(getResources().getString(
					R.string.hp_userreg_success));
			break;
		case 102:
			// 注册帐号长度非法
			displayRegisterResult(getResources().getString(
					R.string.hp_userregserver_error102));
			progressDialog.dismiss();
			break;
		case 103:
			// 通信密钥不正确
			displayRegisterResult(getResources().getString(
					R.string.hp_userregserver_error103));
			progressDialog.dismiss();
			break;
		case 104:
			// 注册帐号非法，注册帐号必须是数字和字母组合，不能包含非法字符,@除外
			displayRegisterResult(getResources().getString(
					R.string.hp_userregserver_error104));
			progressDialog.dismiss();
			break;
		case 1:
			// 用户名或邮箱存在，无法注册
			displayRegisterResult(getResources().getString(
					R.string.hp_userregserver_error1));
			progressDialog.dismiss();
			break;
		case 5:
			// 用户基本信息写入失败
			displayRegisterResult(getResources().getString(
					R.string.hp_userregserver_error5));
			progressDialog.dismiss();
			break;
		case 7:
			// 用户扩展信息写入失败
			displayRegisterResult(getResources().getString(
					R.string.hp_userregserver_error7));
			progressDialog.dismiss();
			break;
		default:
			// 服务器内部错误
			displayRegisterResult(getResources().getString(
					R.string.hp_userregserver_errorother));
			progressDialog.dismiss();
			break;
		}
	}

	@Override
	public void onErrorRegister(Exception e) {
		progressDialog.dismiss();
	}

	@Override
	public void onFinishSignIn() {
		displayRegisterResult(getResources().getString(
				R.string.hp_userlogin_success));
		progressDialog.dismiss();
		startActivity(new Intent(getActivity(), UserInfoEdit.class));
		dismiss();
	}

	@Override
	public void onErrorSignIn(Exception e) {
		progressDialog.dismiss();
	}
}
