package com.vee.myhealth.ui;

import com.vee.healthplus.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * @author lingyun
 * 
 */
public class HeartRateTestActivity extends Activity {

	private TextView heartBeat;
	private TextView unit;

	
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				Intent intent = new Intent();
				intent.setClass(HeartRateTestActivity.this,
						NewTestResultActivity.class);
				startActivity(intent);
				finish();
				break;
			}
		}
	};

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.hr_test_layout);
		ImageView localImageView1 = (ImageView) findViewById(R.id.image_heart_animation);
		Animation localAnimation = AnimationUtils.loadAnimation(
				getApplicationContext(), R.anim.hr_twinkle_animation);
		localAnimation.setDuration(1250L);
		localImageView1.startAnimation(localAnimation);
		heartBeat = ((TextView) findViewById(R.id.text_value));
		unit = ((TextView) findViewById(R.id.text_measure));
		ImageView localImageView2 = (ImageView) findViewById(R.id.image_hr_beat);
		if (localImageView2 != null) {
			localImageView2.setBackgroundResource(R.drawable.hr_beat_animation);
			AnimationDrawable localDrawable = (AnimationDrawable) localImageView2.getBackground();
			localDrawable.setDither(true);
			localDrawable.start();
		}
		mHandler.sendEmptyMessageDelayed(1, 10000);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mHandler.removeMessages(1);
	}
}
