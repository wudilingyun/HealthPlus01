package com.vee.myhealth.ui;

import com.vee.healthplus.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class MyHealthAddUsers extends Activity implements OnClickListener {
	private Button saveButton;
	private MyHealthUsername username;
	private RadioGroup sex_radio;
	private EditText username_tv, userheight_tv, userweight_tv, userage_tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.health_myhealth_addusers);
		init();
	}

	void init() {
		username = new MyHealthUsername();
		saveButton = (Button) findViewById(R.id.save_bt);
		saveButton.setOnClickListener(this);
		username_tv = (EditText) findViewById(R.id.username_tv);
		userheight_tv = (EditText) findViewById(R.id.userheight_tv);
		userweight_tv = (EditText) findViewById(R.id.userweight_tv);
		userage_tv = (EditText) findViewById(R.id.userage_tv);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.save_bt:
			username.setName(username_tv.getText().toString());
			username.setHeight(userheight_tv.getText().toString());
			username.setWeight(userweight_tv.getText().toString());
			username.setAge(userage_tv.getText().toString());
			Intent intent = new Intent(this, MyHealthUsersGroupActivity.class);
			intent.putExtra("name", username.getName());
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
	}
}
