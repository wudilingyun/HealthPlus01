package com.vee.myhealth.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.vee.healthplus.R;
import com.vee.healthplus.util.user.HP_DBModel;
import com.vee.healthplus.util.user.HP_User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;

public class MyHealthUsersGroupActivity extends Activity implements
		OnClickListener {

	ListView listview;
	private Button close_bt;
	private TextView adduser_tv;
	private MyHealthUsersAdapter usersAdapter;
	private List<String> usernameList;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.health_myhealth_list_users);

		close_bt = (Button) findViewById(R.id.close_bt);
		adduser_tv = (TextView) findViewById(R.id.add_users);
		usernameList = new ArrayList<String>();
		int userid = HP_User.getOnLineUserId(this);
		HP_User user = HP_DBModel.getInstance(this)
				.queryUserInfoByUserId(userid, true);
		if(user.userName!=null){
			usernameList.add(user.userName);
		}
		usernameList.add("爸爸");
		usernameList.add("妈妈");
		usernameList.add("爷爷");
		adduser_tv.setOnClickListener(this);
		close_bt.setOnClickListener(this);
		listview = (ListView) findViewById(R.id.health_users_list);
		usersAdapter = new MyHealthUsersAdapter(this);
		usersAdapter.listaddAdapter(usernameList);
		listview.setAdapter(usersAdapter);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.close_bt:
			finish();
			break;
		case R.id.add_users:
			Intent intent = new Intent(this, MyHealthAddUsers.class);
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
	}
}
