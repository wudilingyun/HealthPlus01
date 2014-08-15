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
import com.vee.healthplus.util.user.UserInfoUtil;

public class AgeEditActivity extends Activity implements View.OnClickListener {
	private WheelView yearWv;
	private WheelView monthWv;
	private Button okBtn;
	private static int currentYear;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Window window = getWindow();
		window.setGravity(Gravity.BOTTOM);
		window.setWindowAnimations(R.style.dialog_updown);
		setContentView(R.layout.personal_info_age_edit_layout);
		Time t = new Time();
		t.setToNow();
		currentYear = t.year;
		yearWv = (WheelView) findViewById(R.id.age_edit_year_wv);
		monthWv = (WheelView) findViewById(R.id.age_edit_month_wv);
		yearWv.setViewAdapter(new Age_Year_Adapter(this));
		monthWv.setViewAdapter(new Age_Month_Adapter(this));
		int age = getIntent().getExtras().getInt("age");
		yearWv.setCurrentItem(currentYear - 1900
				- UserInfoUtil.getAgeFromBirthDay(age));
		monthWv.setCurrentItem(UserInfoUtil.getMonthFromBirthDay(age) - 1);
		okBtn = (Button) findViewById(R.id.age_edit_ok_btn);
		okBtn.setOnClickListener(this);

	}

	/*
	 * private void initView(View layout) { age_wv = (WheelView)
	 * layout.findViewById(R.id.age_wv); age_wv.setViewAdapter(new
	 * Age_Adapter(getActivity())); age_wv.setCurrentItem(user.userAge - 1);
	 * Button setDistanceTarget_btn = (Button) layout.findViewById(R.id.ok_btn);
	 * setDistanceTarget_btn.setOnClickListener(this); }
	 */

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.age_edit_ok_btn:
			Intent data = getIntent();
			Bundle bundle = data.getExtras();
			Log.i("lingyun",
					"yearWv.getCurrentItem()=" + yearWv.getCurrentItem());
			Log.i("lingyun",
					"yearWv.getCurrentItem()=" + monthWv.getCurrentItem());
			StringBuffer sb = new StringBuffer();
			sb.append(1900 + yearWv.getCurrentItem() + "");
			if (monthWv.getCurrentItem() < 9) {
				sb.append("0" + (monthWv.getCurrentItem() + 1));
			} else {
				sb.append((monthWv.getCurrentItem() + 1));
			}
			sb.append("00");
			Log.i("lingyun", "AgeEditActivity.sb=" + sb.toString());
			bundle.putInt("age", Integer.valueOf(sb.toString()));
			data.putExtras(bundle);
			AgeEditActivity.this.setResult(RESULT_OK, data);
			finish();
			break;
		}
	}

	private class Age_Year_Adapter extends AbstractWheelTextAdapter {

		private int maxYears = currentYear - 1900 + 1;

		protected Age_Year_Adapter(Context context) {
			super(context);
		}

		@Override
		protected CharSequence getItemText(int index) {
			return String.valueOf(1900 + index);
		}

		@Override
		public int getItemsCount() {
			return maxYears;
		}
	}

	private class Age_Month_Adapter extends AbstractWheelTextAdapter {

		private int maxMonth = 12;

		protected Age_Month_Adapter(Context context) {
			super(context);
		}

		@Override
		protected CharSequence getItemText(int index) {
			return String.valueOf(index + 1);
		}

		@Override
		public int getItemsCount() {
			return maxMonth;
		}
	}

}
