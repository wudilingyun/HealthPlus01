package com.vee.healthplus.ui.user;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.vee.healthplus.R;
import com.vee.healthplus.activity.BaseFragmentActivity;
import com.vee.healthplus.util.user.HP_DBModel;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.widget.HeaderView;
import com.vee.healthplus.widget.HeaderView.OnHeaderClickListener;

@SuppressLint("ResourceAsColor")
public class UsernameEditActivity extends BaseFragmentActivity implements
		View.OnClickListener {

	private EditText unameEt;
	private ImageView clearBtn;
	private OnHeaderClickListener headerClickListener = new OnHeaderClickListener() {

		@Override
		public void OnHeaderClick(View v, int option) {
			if (option == HeaderView.HEADER_BACK) {
				finish();
			} else if (option == HeaderView.HEADER_OK) {
				String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
				Pattern p3 = Pattern.compile(regEx);
				Matcher m3 = p3.matcher(unameEt.getText().toString().trim());
				boolean ok=(m3.replaceAll("").trim().length())==unameEt.getText().toString().trim().length();
				if (unameEt.getText().toString().trim() == null
						|| unameEt.getText().toString().trim().length() < 4
						|| unameEt.getText().toString().trim().length() > 30||!ok) {
					Toast.makeText(UsernameEditActivity.this, "格式不正确",
							Toast.LENGTH_LONG).show();
					return;
				}
				if(!checkNick(unameEt.getText().toString().trim())){
					Toast.makeText(UsernameEditActivity.this, "请重新编辑昵称",
							Toast.LENGTH_LONG).show();
					return;
				}
				Intent data = getIntent();
				Bundle bundle = data.getExtras();
				bundle.putString("uname", unameEt.getText().toString());
				data.putExtras(bundle);
				UsernameEditActivity.this.setResult(RESULT_OK, data);
				finish();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		View view = View.inflate(this,
				R.layout.personal_info_username_edit_layout, null);
		setContainer(view);
		getHeaderView().setHeaderTitle("昵称");
		getHeaderView().setHeaderTitleColor(
				R.color.register_headview_text_color_white);
		getHeaderView().setBackGroundColor(
				R.color.register_headview_bg_color_black);
		setRightBtnVisible(View.VISIBLE);
		setRightBtnRes(R.drawable.healthplus_headview_ok_btn);
		setRightBtnType(HeaderView.HEADER_OK);
		setLeftBtnVisible(View.VISIBLE);
		setLeftBtnType(HeaderView.HEADER_BACK);
		setLeftBtnRes(R.drawable.healthplus_headview_back_btn);
		setHeaderClickListener(headerClickListener);
		initView(view);
	}

	private void initView(View view) {

		unameEt = (EditText) view
				.findViewById(R.id.personal_info_uname_edit_et);
		unameEt.setText(getIntent().getExtras().getString("uname"));
		clearBtn = (ImageView) view
				.findViewById(R.id.personal_info_uname_edit_clear_img);
		clearBtn.setOnClickListener(this);
		String digits = getResources().getString(R.string.user_resgiter_edit);
		// userPwd_et.setKeyListener(DigitsKeyListener.getInstance(digits));
		// userName_et.setKeyListener(DigitsKeyListener.getInstance(digits));
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.personal_info_uname_edit_clear_img:
			unameEt.setText("");
			break;
		case R.id.cannel_btn:
			finish();
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private void displayRegisterError(String message) {
		// new
		// AlertDialog.Builder(mContext).setMessage(message).setCancelable(false)
		// .setPositiveButton("OK", null).create().show();
	}

	private void displayRegisterResult(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}
	
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
