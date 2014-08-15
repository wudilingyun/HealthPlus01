package com.vee.healthplus.ui.setting;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.text.method.DigitsKeyListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vee.healthplus.R;
import com.vee.healthplus.common.Common;
import com.vee.healthplus.heahth_news_utils.CheckNetWorkStatus;
import com.vee.healthplus.util.InstallSataUtil;
import com.vee.healthplus.widget.HeaderView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingFeedBack extends Activity implements
		HeaderView.OnHeaderClickListener, OnCheckedChangeListener,
		OnClickListener {

	private Context mContext;
	private CheckBox feedback_question1, feedback_question2,
			feedback_question3, feedback_question4, feedback_question5,
			feedback_question6;

	private HashMap<String, Boolean> hm = new HashMap<String, Boolean>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_feedback);
		this.mContext = this;
		HeaderView headerView = (HeaderView) findViewById(R.id.setting_feadback_header);
		headerView.setOnHeaderClickListener(this);
		headerView.setGravity(Gravity.CENTER);
		feedback_question1 = (CheckBox) findViewById(R.id.setting_feedback_question1);
		feedback_question2 = (CheckBox) findViewById(R.id.setting_feedback_question2);
		feedback_question3 = (CheckBox) findViewById(R.id.setting_feedback_question3);
		feedback_question4 = (CheckBox) findViewById(R.id.setting_feedback_question4);
		feedback_question5 = (CheckBox) findViewById(R.id.setting_feedback_question5);
		feedback_question6 = (CheckBox) findViewById(R.id.setting_feedback_question6);
		initCheckbox();
		feedback_question6.setChecked(true);
		initHm();
		hm.put(feedback_question6.getText().toString(), true);

		feedback_question1.setOnClickListener(this);
		feedback_question2.setOnClickListener(this);
		feedback_question3.setOnClickListener(this);
		feedback_question4.setOnClickListener(this);
		feedback_question5.setOnClickListener(this);
		feedback_question6.setOnClickListener(this);
		// feedback_question1.setOnCheckedChangeListener(this);
		// feedback_question2.setOnCheckedChangeListener(this);
		// feedback_question3.setOnCheckedChangeListener(this);
		// feedback_question4.setOnCheckedChangeListener(this);
		// feedback_question5.setOnCheckedChangeListener(this);
		// feedback_question6.setOnCheckedChangeListener(this);
		final EditText feedback_content = (EditText) findViewById(R.id.setting_feedback_content);
		final EditText feedback_contact = (EditText) findViewById(R.id.setting_feedback_contact);

		String digits = getResources().getString(
				R.string.setting_feedback_contact_limit);
		feedback_contact.setKeyListener(DigitsKeyListener.getInstance(digits));
		Button feedback_send = (Button) findViewById(R.id.setting_feedback_send);
		Button feedback_copynumber = (Button) findViewById(R.id.setting_feedback_copynumber);
		final TextView qqnumber = (TextView) findViewById(R.id.setting_feedback_qqnumber);
		feedback_send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				StringBuffer content = new StringBuffer();
				Iterator<Entry<String, Boolean>> iterator = hm.entrySet()
						.iterator();
				while (iterator.hasNext()) {
					Entry<String, Boolean> entry = (Entry<String, Boolean>) iterator
							.next();
					String key = entry.getKey();
					Boolean value = entry.getValue();
					if (value) {
						content.append(key + ";");
					}
				}
				if (content.toString().length() < 1) {
					Toast.makeText(
							mContext,
							mContext.getResources().getString(
									R.string.setting_feedback_wrong1),
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (feedback_content.getText().toString().trim().length() > 1) {
					content.append("OtherQuestion:"
							+ feedback_content.getText().toString() + ";");
				} else {
					Toast.makeText(
							mContext,
							mContext.getResources().getString(
									R.string.setting_feedback_wrong3),
							Toast.LENGTH_SHORT).show();
					return;
				}

				Pattern p = Pattern.compile("[0-9]*");
				Matcher m = p.matcher(feedback_contact.getText().toString());
				if (feedback_contact.getText().toString().trim().length() > 1) {
					if (m.matches()) {
						if (!isMobileNO(feedback_contact.getText().toString()
								.trim())) {
							Toast.makeText(
									mContext,
									mContext.getResources()
											.getString(
													R.string.hp_userinfoediterror_phone),
									Toast.LENGTH_SHORT).show();
							return;
						}

					} else {
						if (!isEmail(feedback_contact.getText().toString())) {
							Toast.makeText(
									mContext,
									mContext.getResources()
											.getString(
													R.string.hp_userinfoediterror_email),
									Toast.LENGTH_SHORT).show();
							return;
						}
					}
				} else {
					Toast.makeText(
							mContext,
							mContext.getResources().getString(
									R.string.setting_feedback_wrong2),
							Toast.LENGTH_SHORT).show();
					return;
				}

				content.append("ContactWay:"
						+ feedback_contact.getText().toString() + ";");
				// MyEasyGameConstant.ReportComments(mContext,
				// content.toString());
				if(CheckNetWorkStatus.Status(getApplication())){
					new InstallSataUtil(mContext).ReportComments(
							content.toString(), Common.getAppId(mContext));
					Toast.makeText(
							mContext,
							getResources().getString(
									R.string.setting_feedback_send_success),
							Toast.LENGTH_SHORT).show();
					SettingFeedBack.this.finish();
				}else {
					Toast.makeText(getApplication(), "请检查网络连接", Toast.LENGTH_SHORT).show();
				}
				
			}
		});

		feedback_copynumber.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
				cm.setText(qqnumber.getText().toString());
				Toast.makeText(
						mContext,
						getResources().getString(
								R.string.setting_feedback_copyok),
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	// 判断手机格式是否正确
	public boolean isMobileNO(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);

		return m.matches();
	}

	private static boolean isEmail(String strEmail) {
		String strPattern = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(strEmail);
		return m.matches();
	}

	@Override
	public void OnHeaderClick(View v, int option) {
		// TODO Auto-generated method stub
		switch (option) {
		case HeaderView.HEADER_BACK:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		initCheckbox();
		initHm();
		buttonView.setChecked(true);
		if (isChecked) {
			if (buttonView.getId() == R.id.setting_feedback_question1) {
				hm.put(feedback_question1.getText().toString(), true);
			} else if (buttonView.getId() == R.id.setting_feedback_question2) {
				hm.put(feedback_question2.getText().toString(), true);
			} else if (buttonView.getId() == R.id.setting_feedback_question3) {
				hm.put(feedback_question3.getText().toString(), true);
			} else if (buttonView.getId() == R.id.setting_feedback_question4) {
				hm.put(feedback_question4.getText().toString(), true);
			} else if (buttonView.getId() == R.id.setting_feedback_question5) {
				hm.put(feedback_question5.getText().toString(), true);
			} else if (buttonView.getId() == R.id.setting_feedback_question6) {
				hm.put(feedback_question6.getText().toString(), true);
			}
		} else {
			if (buttonView.getId() == R.id.setting_feedback_question1) {
				hm.put(feedback_question1.getText().toString(), false);
			} else if (buttonView.getId() == R.id.setting_feedback_question2) {
				hm.put(feedback_question2.getText().toString(), false);
			} else if (buttonView.getId() == R.id.setting_feedback_question3) {
				hm.put(feedback_question3.getText().toString(), false);
			} else if (buttonView.getId() == R.id.setting_feedback_question4) {
				hm.put(feedback_question4.getText().toString(), false);
			} else if (buttonView.getId() == R.id.setting_feedback_question5) {
				hm.put(feedback_question5.getText().toString(), false);
			} else if (buttonView.getId() == R.id.setting_feedback_question6) {
				hm.put(feedback_question6.getText().toString(), false);
			}
		}
	}

	private void initCheckbox() {
		feedback_question1.setChecked(false);
		feedback_question2.setChecked(false);
		feedback_question3.setChecked(false);
		feedback_question4.setChecked(false);
		feedback_question5.setChecked(false);
		feedback_question6.setChecked(false);
	}

	private void initHm() {
		hm.put(feedback_question1.getText().toString(), false);
		hm.put(feedback_question2.getText().toString(), false);
		hm.put(feedback_question3.getText().toString(), false);
		hm.put(feedback_question4.getText().toString(), false);
		hm.put(feedback_question5.getText().toString(), false);
		hm.put(feedback_question6.getText().toString(), false);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		initCheckbox();
		initHm();

		if (v.getId() == R.id.setting_feedback_question1) {
			hm.put(feedback_question1.getText().toString(), true);
			feedback_question1.setChecked(true);
		} else if (v.getId() == R.id.setting_feedback_question2) {
			hm.put(feedback_question2.getText().toString(), true);
			feedback_question2.setChecked(true);
		} else if (v.getId() == R.id.setting_feedback_question3) {
			hm.put(feedback_question3.getText().toString(), true);
			feedback_question3.setChecked(true);
		} else if (v.getId() == R.id.setting_feedback_question4) {
			hm.put(feedback_question4.getText().toString(), true);
			feedback_question4.setChecked(true);
		} else if (v.getId() == R.id.setting_feedback_question5) {
			hm.put(feedback_question5.getText().toString(), true);
			feedback_question5.setChecked(true);
		} else if (v.getId() == R.id.setting_feedback_question6) {
			hm.put(feedback_question6.getText().toString(), true);
			feedback_question6.setChecked(true);
		}

	}
}
