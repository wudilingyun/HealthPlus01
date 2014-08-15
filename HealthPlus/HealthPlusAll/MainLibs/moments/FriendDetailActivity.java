package com.vee.moments;

import java.util.concurrent.Executors;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.vee.healthplus.R;
import com.vee.healthplus.activity.BaseFragmentActivity;
import com.yunfox.s4aservicetest.response.GeneralResponse;
import com.yunfox.s4aservicetest.response.SearchUserResponse;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class FriendDetailActivity extends BaseFragmentActivity {
	private SearchUserResponse searchUserResponse;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = View.inflate(this, R.layout.activity_friend_detail, null);
		setContainer(view);

		searchUserResponse = (SearchUserResponse) getIntent().getExtras()
				.getSerializable("searchuserresponse");
		TextView textViewUsername = (TextView) findViewById(R.id.username);
		textViewUsername.setText(searchUserResponse.getFriendname());

		Button buttonAddFriend = (Button) findViewById(R.id.addfriend);
		Button buttonBrowseMoments = (Button) findViewById(R.id.browsemoments);
		if (searchUserResponse.getIaddfriend() == 0) {
			buttonBrowseMoments.setVisibility(View.GONE);
			buttonAddFriend.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB)
					{
						new AddFriendTask().executeOnExecutor(Executors.newCachedThreadPool());
					}
					else
					{
						new AddFriendTask().execute();
					}
				}
			});
		} else {
			buttonAddFriend.setVisibility(View.GONE);
			buttonBrowseMoments.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(FriendDetailActivity.this, UserMomentsActivity.class);
					intent.putExtra("friendid", searchUserResponse.getFriendid());
					startActivity(intent);
					FriendDetailActivity.this.finish();
				}
			});
		}
	}

	// ***************************************
	// Private classes
	// ***************************************
	private class AddFriendTask extends AsyncTask<Void, Void, GeneralResponse> {

		private Exception exception;
		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			dialog = new ProgressDialog(FriendDetailActivity.this);
			dialog.show();
		}

		@Override
		protected GeneralResponse doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				GeneralResponse generalResponse = SpringAndroidService
						.getInstance(getApplication()).addFriend(
								searchUserResponse.getFriendid());

				return generalResponse;

			} catch (Exception e) {
				this.exception = e;
			}

			return null;
		}

		@Override
		protected void onPostExecute(GeneralResponse generalResponse) {
			// TODO Auto-generated method stub
			dialog.dismiss();
			if (exception != null) {

			}

			if (generalResponse != null) {
				if (generalResponse.getReturncode() == 200) {
					Toast.makeText(FriendDetailActivity.this,
							"add friend success", Toast.LENGTH_SHORT).show();
					FriendDetailActivity.this.finish();
				} else {
					Toast.makeText(FriendDetailActivity.this,
							generalResponse.getDescription(),
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
}
