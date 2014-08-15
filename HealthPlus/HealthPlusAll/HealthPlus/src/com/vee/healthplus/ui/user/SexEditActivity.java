package com.vee.healthplus.ui.user;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.vee.healthplus.R;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.util.user.ICallBack;


public class SexEditActivity extends Activity implements View.OnClickListener {
	private Button maleBtn;
	private Button femaleBtn;
	private String sex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.personal_info_sex_edit_layout);
		maleBtn = (Button) findViewById(R.id.sex_edit_male_btn);
		femaleBtn = (Button) findViewById(R.id.sex_edit_female_btn);
		maleBtn.setOnClickListener(this);
		femaleBtn.setOnClickListener(this);
		sex = getIntent().getExtras().getString("sex");
		if (sex.equals("男")) {
			maleBtn.setSelected(true);
		} else {
			femaleBtn.setSelected(true);
		}
	}

	@Override
	public void onClick(View view) {
		Intent data = getIntent();
		Bundle bundle = data.getExtras();
		switch (view.getId()) {
		case R.id.sex_edit_male_btn:
			bundle.putString("sex", "男");
			data.putExtras(bundle);
			break;
		case R.id.sex_edit_female_btn:
			bundle.putString("sex", "女");
			data.putExtras(bundle);
			break;
		}
		SexEditActivity.this.setResult(RESULT_OK, data);
		finish();
	}

}
