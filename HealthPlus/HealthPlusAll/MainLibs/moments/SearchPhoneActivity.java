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
import android.widget.EditText;
import android.widget.Toast;

import com.vee.healthplus.R;
import com.vee.healthplus.activity.BaseFragmentActivity;
import com.yunfox.s4aservicetest.response.SearchUserResponse;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class SearchPhoneActivity extends BaseFragmentActivity {
	String searchcontent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = View.inflate(this, R.layout.activity_search_phone, null);
		setContainer(view);

		Button searchButton = (Button) findViewById(R.id.searchButton);
		searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EditText editTextSearchContent = (EditText) findViewById(R.id.editTextSearchPhone);
				searchcontent = editTextSearchContent.getText().toString();
				if (searchcontent == null || searchcontent.length() == 0) {
					Toast.makeText(SearchPhoneActivity.this,
							"Please type something", Toast.LENGTH_SHORT).show();
				} else {
					if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB)
					{
						new SearchUserTask().executeOnExecutor(Executors.newCachedThreadPool());
					}
					else
					{
						new SearchUserTask().execute();
					}
				}
			}
		});
	}

	// ***************************************
	// Private classes
	// ***************************************
	private class SearchUserTask extends
			AsyncTask<Void, Void, SearchUserResponse> {

		private Exception exception;
		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			dialog = new ProgressDialog(SearchPhoneActivity.this);
			dialog.show();
		}

		@Override
		protected SearchUserResponse doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				SearchUserResponse searchUserResponse = SpringAndroidService
						.getInstance(getApplication())
						.searchUser(searchcontent);

				return searchUserResponse;

			} catch (Exception e) {
				this.exception = e;
			}

			return null;
		}

		@Override
		protected void onPostExecute(SearchUserResponse searchUserResponse) {
			// TODO Auto-generated method stub
			dialog.dismiss();
			if (exception != null) {

			}

			if (searchUserResponse != null) {
				if (searchUserResponse.getFriendid() == 0) {
					Toast.makeText(SearchPhoneActivity.this, "no user find",
							Toast.LENGTH_SHORT).show();
				} else {
					Intent intent = new Intent(SearchPhoneActivity.this,
							FriendDetailActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("searchuserresponse",
							searchUserResponse);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}
		}
	}

}
