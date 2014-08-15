package com.vee.myhealth.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.vee.healthplus.R;

/**
 * @author lingyun
 * 
 */
public class EcgTestActivity extends Activity {
	private TextView heartBeat;
	private TextView unit;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				Intent intent = new Intent();
				intent.setClass(EcgTestActivity.this,
						NewTestResultActivity.class);
				intent.putExtra("test_type", "ecg");
				intent.putExtra("test_result","low");
				startActivity(intent);
				finish();
				break;
			}
		}
	};

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.ecg_test_layout);
		ImageView localImageView1 = (ImageView) findViewById(R.id.image_heart_animation);
		Animation localAnimation = AnimationUtils.loadAnimation(
				getApplicationContext(), R.anim.hr_twinkle_animation);
		localAnimation.setDuration(1250L);
		localImageView1.startAnimation(localAnimation);
		heartBeat = ((TextView) findViewById(R.id.text_value));
		unit = ((TextView) findViewById(R.id.text_measure));
		mHandler.sendEmptyMessageDelayed(1, 10000);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mHandler.removeMessages(1);
	}
	
}
