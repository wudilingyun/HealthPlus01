package com.example.clienttest;

import android.os.Bundle;
import android.app.Activity;
import android.app.TabActivity;
import android.view.Menu;

public class MainTabWiget extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_tab_wiget);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_tab_wiget, menu);
		return true;
	}

}
