package com.vee.moments;

import com.vee.healthplus.R;
import com.vee.healthplus.R.layout;
import com.vee.healthplus.R.menu;
import com.vee.healthplus.activity.BaseFragmentActivity;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class FriendListActivity extends BaseFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = View.inflate(this, R.layout.activity_friend_list, null);
		setContainer(view);
	}
}
