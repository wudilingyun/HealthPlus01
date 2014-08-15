package com.vee.moments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vee.healthplus.R;
import com.vee.healthplus.util.SystemMethod;

public class AddFriendActivity extends FragmentActivity implements OnClickListener {
	private TextView header_text;
	private ImageView header_lbtn_img, header_rbtn_img;
	private static String[] mTitles;
	private AddFriendAdapter mAdapter;
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult( requestCode, resultCode, data);
		
		if (requestCode == 1)
		{
			if (resultCode == RESULT_OK)
			{
				setResult(RESULT_OK);
				AddFriendActivity.this.finish();
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = View.inflate(this, R.layout.activity_add_friend, null);
		setContentView(view);
		
		settitle(this.getString(R.string.addfriend));		

		ListView listViewAddFriend = (ListView) findViewById(R.id.addfriendlist);
		mTitles = SystemMethod.getStringArray(this, R.array.addfriend);
		mAdapter = new AddFriendAdapter(this, mTitles);
		listViewAddFriend.setAdapter(mAdapter);
		
		listViewAddFriend.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				if(position == 0)
				{
					Intent intent = new Intent(AddFriendActivity.this,
							SearchPhoneActivity.class);
					startActivityForResult(intent, 1);
				}
				else if(position == 1)
				{
					Intent intent = new Intent(AddFriendActivity.this,
							AddContsctsActivity.class);
					startActivity(intent);					
				}
			}
		});
	}
	
	void settitle(String name) {

		header_text = (TextView) findViewById(R.id.header_text);
		header_lbtn_img = (ImageView) findViewById(R.id.header_lbtn_img);
		header_rbtn_img = (ImageView) findViewById(R.id.header_rbtn_img);
		header_rbtn_img.setVisibility(View.GONE);
		header_text.setText(name);
		header_text.setOnClickListener(this);
		header_lbtn_img.setOnClickListener(this);
		header_rbtn_img.setOnClickListener(this);
	}

/*	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_friend, menu);
		return true;
	}*/

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.header_lbtn_img:
			this.finish();
			break;
		case R.id.header_rbtn_img:

			break;

		default:
			break;
		}
	}

}
