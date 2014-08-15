package com.vee.healthplus.ui.user;

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
import com.vee.healthplus.widget.HeaderView;
import com.vee.healthplus.widget.HeaderView.OnHeaderClickListener;

@SuppressLint("ResourceAsColor") 
public class EmailEditActivity extends BaseFragmentActivity implements
		View.OnClickListener {

	private EditText emailEt;
	private ImageView clearBtn;

	private OnHeaderClickListener headerClickListener = new OnHeaderClickListener() {

		@Override
		public void OnHeaderClick(View v, int option) {
			if (option == HeaderView.HEADER_BACK) {
				finish();
			} else if (option == HeaderView.HEADER_OK) {
				Pattern p = Pattern
						.compile("^([a-zA-Z0-9\\._-])+@([a-zA-Z0-9_-])+(\\.([a-zA-Z0-9_-])+)+$");
				Matcher m = p.matcher(emailEt.getText().toString().trim());
				if(!m.matches()){
					Toast.makeText(EmailEditActivity.this, "请输入正确的邮箱地址", Toast.LENGTH_LONG).show();
					return;
				}
				Intent data = getIntent();
				Bundle bundle = data.getExtras();
				bundle.putString("email", emailEt.getText().toString());
				data.putExtras(bundle);
				EmailEditActivity.this.setResult(RESULT_OK, data);
				finish();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		View view = View.inflate(this,
				R.layout.personal_info_email_edit_layout, null);
		setContainer(view);
		getHeaderView().setHeaderTitle("邮箱");
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

		emailEt = (EditText) view
				.findViewById(R.id.personal_info_email_edit_et);
		emailEt.setText(getIntent().getExtras().getString("email"));
		clearBtn = (ImageView) view
				.findViewById(R.id.personal_info_email_edit_clear_img);
		clearBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.personal_info_email_edit_clear_img:
			emailEt.setText("");
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

}
