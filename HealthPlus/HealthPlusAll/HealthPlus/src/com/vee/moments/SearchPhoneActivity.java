package com.vee.moments;

import java.util.concurrent.Executors;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vee.healthplus.R;
import com.vee.healthplus.activity.BaseFragmentActivity;
import com.yunfox.s4aservicetest.response.SearchUserResponse;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class SearchPhoneActivity extends FragmentActivity implements
		OnClickListener {
	String searchcontent;
	private TextView header_text;
	private ImageView header_lbtn_img, header_rbtn_img;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				setResult(RESULT_OK);
				SearchPhoneActivity.this.finish();
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = View.inflate(this, R.layout.activity_search_phone, null);
		setContentView(view);

		gettitle();

		Button searchButton = (Button) findViewById(R.id.searchButton);
		searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EditText editTextSearchContent = (EditText) findViewById(R.id.editTextSearchPhone);
				searchcontent = editTextSearchContent.getText().toString();
				if (searchcontent == null || searchcontent.length() == 0) {
					Toast.makeText(SearchPhoneActivity.this, "输入为空",
							Toast.LENGTH_SHORT).show();
				} else {
					new SearchUserTask().execute();
				}
			}
		});
	}

	void gettitle() {

		header_text = (TextView) findViewById(R.id.header_text);
		header_lbtn_img = (ImageView) findViewById(R.id.header_lbtn_img);
		header_rbtn_img = (ImageView) findViewById(R.id.header_rbtn_img);
		header_rbtn_img.setVisibility(View.GONE);
		header_text.setText("搜号码");
		header_text.setOnClickListener(this);
		header_lbtn_img.setOnClickListener(this);
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
					Toast.makeText(SearchPhoneActivity.this, "没有找到用户",
							Toast.LENGTH_SHORT).show();
				} else {
					Intent intent = new Intent(SearchPhoneActivity.this,
							FriendDetailActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("searchuserresponse",
							searchUserResponse);
					bundle.putString("searchcontent", searchcontent);
					intent.putExtras(bundle);
					setResult(RESULT_OK);
					startActivityForResult(intent, 1);
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		finish();
	}

}
