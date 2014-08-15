package com.vee.myhealth.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.vee.healthplus.R;

/**
 * @author lingyun
 *
 */
public class TemperatureTestActivity extends Activity {
	
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				Intent intent = new Intent();
				intent.setClass(TemperatureTestActivity.this,
						NewTestResultActivity.class);
				startActivity(intent);
				finish();
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.temperature_test_layout);
		mHandler.sendEmptyMessageDelayed(1, 10000);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mHandler.removeMessages(1);
	}
	
	
}
