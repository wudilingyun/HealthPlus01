package com.vee.healthplus.ui.user;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.R.integer;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.vee.healthplus.R;
import com.vee.healthplus.util.user.HP_DBModel;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.util.user.SaveProfileTask;

/**
 * Created by wangjiafeng on 13-11-18.
 */
public class UserInfoEdit extends UserBaseActivity implements
		View.OnClickListener, SaveProfileTask.SaveProfileCallBack,
		RadioGroup.OnCheckedChangeListener {

	private HP_User user;
	private EditText userNick_et, userPhone_et, userEmail_et;
	private Context mContext;
	private EditText userAge_et, userHeight_et, userWeight_et;
	private RadioGroup sex_rg;
	private RadioButton male_rb, female_rb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mContext = this;
		View layout = View.inflate(mContext, R.layout.userinfoedit, null);
		user = HP_DBModel.getInstance(mContext).queryUserInfoByUserId(
				HP_User.getOnLineUserId(mContext), true);
		initView(layout);
		setContentView(layout);
	}

	private void initView(View view) {
		TextView userName_tv = (TextView) view.findViewById(R.id.userName_tv);
		userName_tv.setText(user.userName);
		TextView pwdmotify_tv = (TextView) view.findViewById(R.id.pwdmotify_tv);
		pwdmotify_tv.setOnClickListener(this);
		if (HP_User.getOnLineUserId(mContext) != 0) {
			pwdmotify_tv.setVisibility(View.VISIBLE);
		} else {
			pwdmotify_tv.setVisibility(View.GONE);
		}
		userNick_et = (EditText) view.findViewById(R.id.userNick_et);
		userNick_et.setText(user.userNick);
		userPhone_et = (EditText) view.findViewById(R.id.userPhone_et);
		userPhone_et.setText(user.phone);
		userEmail_et = (EditText) view.findViewById(R.id.userEmail_et);
		userEmail_et.setText(user.email);
		userAge_et = (EditText) view.findViewById(R.id.userAge_et);
		userHeight_et = (EditText) view.findViewById(R.id.userHeight_et);
		userWeight_et = (EditText) view.findViewById(R.id.userWeight_et);
		userAge_et.setText(String.valueOf(user.userAge));
		userHeight_et.setText(String.valueOf(user.userHeight));
		userWeight_et.setText(String.valueOf(user.userWeight));
		sex_rg = (RadioGroup) view.findViewById(R.id.sex_rg);
		sex_rg.setOnCheckedChangeListener(this);
		male_rb = (RadioButton) view.findViewById(R.id.male_rb);
		female_rb = (RadioButton) view.findViewById(R.id.female_rb);
		Button save_btn = (Button) view.findViewById(R.id.save_btn);
		save_btn.setOnClickListener(this);
		switch (user.userSex) {
		case 0:
			female_rb.setChecked(true);
			break;
		case 1:
			male_rb.setChecked(true);
			break;
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.pwdmotify_tv:
			new UserPwdMotify().show(getSupportFragmentManager(), "");
			break;
		case R.id.save_btn:
			user.userNick = userNick_et.getText().toString();
			user.phone = userPhone_et.getText().toString();
			user.email = userEmail_et.getText().toString();
			user.userHeight = getIntEditValue(userHeight_et.getText()
					.toString());
			if (userWeight_et.getText().toString() == "") {
				userWeight_et.setText(0);
			}
			if (userAge_et.getText().toString() == "") {
				userAge_et.setText(0);
			}
			if (userHeight_et.getText().toString() == "") {
				userAge_et.setText(0);
			}

			user.userWeight = Float.valueOf(userWeight_et.getText().toString());
			user.userAge = getIntEditValue(userAge_et.getText().toString());
			if (user.userHeight < 75 || user.userHeight > 245) {
				displayResult(mContext.getResources().getString(
						R.string.hp_userinfoediterror_height));
				return;
			} else if (user.userWeight < 30 || user.userWeight > 150) {
				displayResult(mContext.getResources().getString(
						R.string.hp_userinfoediterror_weight));
				return;
			} else if (user.userAge < 16 || user.userAge > 100) {
				displayResult(mContext.getResources().getString(
						R.string.hp_userinfoediterror_age));
				return;
			} else if (!isMobileNO(user.phone)) {
				displayResult(mContext.getResources().getString(
						R.string.hp_userinfoediterror_phone));
				return;
			} else if (!isEmail(user.email)) {
				displayResult(mContext.getResources().getString(
						R.string.hp_userinfoediterror_email));
				return;
			}
			if (HP_User.getOnLineUserId(mContext) != 0) {
				try {
					/*new SaveProfileTask(UserInfoEdit.this, user,
							UserInfoEdit.this).execute();*/
				} catch (Exception ex) {
					ex.printStackTrace();
					Log.e("xuxuxu", ex.getMessage());
				}
			}
			HP_DBModel.getInstance(mContext).updateUserInfo(user, true);
			break;
		}
	}

	// 判断手机格式是否正确
	public boolean isMobileNO(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);

		return m.matches();
	}

	private static boolean isEmail(String strEmail) {
		String strPattern = "^[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";

		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(strEmail);
		return m.matches();
	}

	private void displayResult(String msg) {
		Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
	}

	private int getIntEditValue(String content) {
		if (content.length() > 0) {
			return Integer.valueOf(content);
		} else {
			return 0;
		}
	}

	@Override
	public void onFinishSaveProfile(int reflag) {
		if (reflag == 200) {
			Toast.makeText(
					mContext,
					mContext.getResources().getString(
							R.string.hp_userinfo_motifysuccess),
					Toast.LENGTH_SHORT).show();
			finish();
		}
	}

	@Override
	public void onErrorSaveProfile(Exception e) {

	}

	@Override
	public void onCheckedChanged(RadioGroup radioGroup, int i) {
		switch (i) {
		case R.id.male_rb:
			user.userSex = 1;
			break;
		case R.id.female_rb:
			user.userSex = 0;
			break;
		}
	}

	@Override
	public void onFinishUploadAvatar(int reflag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onErrorUploadAvatar() {
		// TODO Auto-generated method stub
		
	}
}
