package com.vee.myhealth.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.vee.healthplus.R;

/**
 * @author lingyun
 * 
 */
public class NewTestResultActivity extends Activity {
	private Intent mIntent;
	TextView tvTestType;
	TextView tvTestResult;
	String testResult;
	String testType;
	ImageView imPointer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_result_layout);
		mIntent = getIntent();
		tvTestType = (TextView) findViewById(R.id.text_test_type);
		tvTestResult = (TextView) findViewById(R.id.text_test_result);
		imPointer = (ImageView) findViewById(R.id.image_middle_wheel_pointer);
		initView();
	}

	private static void rotatePointer(ImageView paramImageView, float paramFloat) {
		RotateAnimation animation = new RotateAnimation(0, paramFloat,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setFillAfter(true);
		animation.setDuration(800);
		paramImageView.startAnimation(animation);

	}

	private void initView() {
		testType = mIntent.getStringExtra("test_type");
		testResult = mIntent.getStringExtra("test_result");
		if (testType != null && testType.equals("ecg")) {
			if (testResult != null && testResult.equals("low")) {
				tvTestResult.setText("Low");
				tvTestResult.setTextColor(Color.BLUE);
				rotatePointer(imPointer, -90);
			} else if (testResult != null && testResult.equals("normal")) {
				tvTestResult.setText("Normal");
			} else if (testResult != null && testResult.equals("high")) {
				tvTestResult.setText("High");
				tvTestResult.setTextColor(Color.RED);
				rotatePointer(imPointer, 90);
			}
		}
	}

}
