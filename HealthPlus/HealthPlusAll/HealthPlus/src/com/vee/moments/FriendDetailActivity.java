package com.vee.moments;

import org.springframework.social.greenhouse.api.Profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.vee.healthplus.R;
import com.vee.healthplus.activity.BaseFragmentActivity;
import com.vee.healthplus.heahth_news_http.ImageLoader;
import com.vee.healthplus.util.user.HP_DBModel;
import com.vee.healthplus.util.user.HP_User;
import com.vee.myhealth.bean.NewFriendBean;
import com.yunfox.s4aservicetest.response.GeneralResponse;
import com.yunfox.s4aservicetest.response.SearchUserResponse;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class FriendDetailActivity extends BaseFragmentActivity {
	private SearchUserResponse searchUserResponse;
	private String searchcontent;
	private AsyncHttpClient mClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = View.inflate(this, R.layout.activity_friend_detail, null);
		setContainer(view);

		searchUserResponse = (SearchUserResponse) getIntent().getExtras()
				.getSerializable("searchuserresponse");
		searchcontent = getIntent().getStringExtra("searchcontent");

		ImageView friendIconImageView = (ImageView) findViewById(R.id.friend_icon);
		String friendAvatar = searchUserResponse.getFriendavatar();
		ImageLoader.getInstance(this)
				.addTask(friendAvatar, friendIconImageView);

		TextView friendNameTextView = (TextView) findViewById(R.id.friend_name);
		friendNameTextView.setText(searchUserResponse.getFriendname());

		ImageView frinedSexImageView = (ImageView) findViewById(R.id.friend_sex);
		if (searchUserResponse.getGender() == -1) {
			frinedSexImageView.setImageResource(R.drawable.boy_icon);
		} else {
			frinedSexImageView.setImageResource(R.drawable.girl_icon);
		}

		Button buttonAddFriend = (Button) findViewById(R.id.addfriend);
		Button buttonBrowseMoments = (Button) findViewById(R.id.browsemoments);

		int userid = HP_User.getOnLineUserId(FriendDetailActivity.this);
		if (userid != 0) {
			HP_User user = HP_DBModel.getInstance(FriendDetailActivity.this)
					.queryUserInfoByUserId(
							HP_User.getOnLineUserId(FriendDetailActivity.this),
							true);
			String username = user.userName;

			if (searchcontent.compareTo(username) == 0
					|| searchUserResponse.getIaddfriend() != 0||searchcontent.compareTo(user.userNick)==0) {
				buttonAddFriend.setVisibility(View.GONE);
				buttonBrowseMoments.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						/*Intent intent = new Intent(FriendDetailActivity.this,
								UserMomentsActivity.class);
						intent.putExtra("friendid",
								searchUserResponse.getFriendid());
						intent.putExtra("friendname",
								searchUserResponse.getFriendname());
						intent.putExtra("friendavatar",
								searchUserResponse.getFriendavatar());

						Bundle bundle = new Bundle();
						bundle.putSerializable("searchUserResponse",
								searchUserResponse);
						intent.putExtras(bundle);
						startActivity(intent);
						setResult(RESULT_OK);
						FriendDetailActivity.this.finish();*/
						new SearchUserTask().execute(searchUserResponse.getFriendid());
					}
				});
			} else {
				buttonBrowseMoments.setVisibility(View.GONE);
				buttonAddFriend.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						new AddFriendTask().execute();
					}
				});
			}
		}

		iniClient();

		/*
		 * if (searchUserResponse.getIaddfriend() == 0) {
		 * buttonBrowseMoments.setVisibility(View.GONE);
		 * buttonAddFriend.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub new AddFriendTask().execute(); } }); } else {
		 * buttonAddFriend.setVisibility(View.GONE);
		 * buttonBrowseMoments.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub Intent intent = new Intent(FriendDetailActivity.this,
		 * UserMomentsActivity.class); intent.putExtra("friendid",
		 * searchUserResponse.getFriendid()); startActivity(intent);
		 * FriendDetailActivity.this.finish(); } }); }
		 */
	}

	private class SearchUserTask extends AsyncTask<Integer, Void, Profile> {

		private Exception exception;
		ProgressDialog dialog;
		private int friend;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			// dialog = new ProgressDialog(FriendListActivity.this);
			// dialog.show();
		}

		@Override
		protected Profile doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			try {
				friend = params[0];
				Profile searchUserResponse = SpringAndroidService.getInstance(
						getApplication()).getProfileById(
						params[0]);

				return searchUserResponse;

			} catch (Exception e) {
				this.exception = e;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Profile profile) {
			// TODO Auto-generated method stub
			// dialog.dismiss();
			if (exception != null) {

			}

			if (profile != null) {
				Intent intent = new Intent(FriendDetailActivity.this,
						UserMomentsActivity.class);
				intent.putExtra("friendid", friend);
				intent.putExtra("friendname", profile.getNickname());
				intent.putExtra("friendavatar", profile.getMediumavatarurl());

				/*
				 * Bundle bundle = new Bundle();
				 * bundle.putSerializable("profile", profile);
				 * intent.putExtras(bundle);
				 */
				intent.putExtra("friendweight", profile.getWeight());
				intent.putExtra("friendage", profile.getAge());
				intent.putExtra("friendsex", profile.getGender());
				intent.putExtra("friendheight", profile.getHeight());
				startActivity(intent);
				setResult(RESULT_OK);
				FriendDetailActivity.this.finish();
			}
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
					Toast.makeText(FriendDetailActivity.this, "添加好友成功",
							Toast.LENGTH_SHORT).show();
					setResult(RESULT_OK);
					int userid = HP_User
							.getOnLineUserId(FriendDetailActivity.this
									.getApplication());
					HP_User user = HP_DBModel.getInstance(
							FriendDetailActivity.this).queryUserInfoByUserId(
							userid, true);

					String username = user.userNick;
					String useravatar = user.photourl;

					long time = System.currentTimeMillis();
					NewFriendBean newFriendBean = HP_DBModel.getInstance(
							FriendDetailActivity.this.getApplication())
							.getNewFriendByUseridAndFriendid(userid,
									searchUserResponse.getFriendid());
					if (newFriendBean == null) {
						HP_DBModel.getInstance(
								FriendDetailActivity.this.getApplication())
								.insertNewFriend(userid,
										searchUserResponse.getFriendid(),
										searchUserResponse.getFriendname(),
										searchUserResponse.getFriendavatar(),
										0, 1, time, 0);
					} else {
						HP_DBModel.getInstance(
								FriendDetailActivity.this.getApplication())
								.updateIAddFriend(userid,
										searchUserResponse.getFriendid());
					}
					FriendDetailActivity.this.finish();
				} else {
					Toast.makeText(FriendDetailActivity.this,
							generalResponse.getDescription(),
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	/**
	 * initial asyncHttpClient
	 */
	private void iniClient() {

		mClient = new AsyncHttpClient();

		/*
		 * String cookie = SPUtils.getString(this, "cookie"); if
		 * (TextUtils.isEmpty(cookie)) { UIUtils.showToast(this, "Not login!");
		 * Intent intent = new Intent(this, LoginActivity.class);
		 * startActivity(intent); finish(); }
		 * 
		 * mClient.addHeader("Cookie", cookie);
		 */
	}



}
