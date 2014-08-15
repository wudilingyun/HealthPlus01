package com.vee.moments;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.vee.healthplus.R;
import com.vee.healthplus.activity.BaseFragmentActivity;

public class AddFriendActivity extends BaseFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = View.inflate(this, R.layout.activity_add_friend, null);
		setContainer(view);

		ListView listViewAddFriend = (ListView) findViewById(R.id.addfriendlist);
		
		String[] arrAddFriend = {getResources().getString(R.string.searchphone),getResources().getString(R.string.addcontacts)};
		ArrayAdapter<String> adapterAddFriend = new ArrayAdapter<String>(this, R.layout.addfriend_array_item, arrAddFriend);
		listViewAddFriend.setAdapter(adapterAddFriend);
		listViewAddFriend.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				Toast.makeText(AddFriendActivity.this, position + "is clicked", Toast.LENGTH_SHORT).show();
				if(position == 0)
				{
					Intent intent = new Intent(AddFriendActivity.this,
							SearchPhoneActivity.class);
					startActivity(intent);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_friend, menu);
		return true;
	}

}
