package com.vee.moments;

import com.vee.healthplus.R;
import com.vee.healthplus.R.layout;
import com.vee.healthplus.R.menu;
import com.vee.healthplus.activity.BaseFragmentActivity;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class AddContsctsActivity extends BaseFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = View.inflate(this, R.layout.activity_add_contscts, null);
		setContainer(view);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_contscts, menu);
		return true;
	}

}
