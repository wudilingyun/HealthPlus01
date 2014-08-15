package com.vee.moments;

import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.vee.healthplus.R;
import com.vee.healthplus.util.user.HP_DBModel;
import com.vee.healthplus.util.user.HP_User;
import com.vee.moments.adapter.NewFriendAdapter;
import com.vee.myhealth.bean.NewFriendBean;
import com.yunfox.s4aservicetest.response.Friend;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class NewFriendListActivity extends Activity implements OnClickListener {
	private TextView header_text;
	private ImageView header_lbtn_img, header_rbtn_img;
	private ListView listViewNewFriendList;
	private NewFriendAdapter newFriendAdapter;
	private TextView tvEmpty;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		View view = View.inflate(this, R.layout.activity_new_friend_list, null);
		setContentView(view);
		settitle();

		listViewNewFriendList = (ListView) view
				.findViewById(R.id.newfriendlist);
		newFriendAdapter = new NewFriendAdapter(this);
		listViewNewFriendList.setAdapter(newFriendAdapter);

		tvEmpty = (TextView) view.findViewById(R.id.empty);
		listViewNewFriendList.setEmptyView(tvEmpty);
		
		new GetNewFriendsTask().execute();
	}

	void settitle() {

		header_text = (TextView) findViewById(R.id.header_text);
		header_lbtn_img = (ImageView) findViewById(R.id.header_lbtn_img);
		header_rbtn_img = (ImageView) findViewById(R.id.header_rbtn_img);
		header_rbtn_img.setVisibility(View.GONE);
		header_lbtn_img.setImageResource(R.drawable.hp_w_header_view_back);
		header_text.setText(getString(R.string.momentsnewfriendlist));
		header_text.setOnClickListener(this);
		header_lbtn_img.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.header_lbtn_img:
			this.finish();
			break;
		default:
			break;
		}
	}

	// ***************************************
	// Private classes
	// ***************************************
	private class GetNewFriendsTask extends AsyncTask<Void, Void, List<NewFriendBean>> {

		private Exception exception;
		int userid;
		List<NewFriendBean> newFriendList;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			userid =HP_User.getOnLineUserId(NewFriendListActivity.this.getApplication());
		}

		@Override
		protected List<NewFriendBean> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				newFriendList = HP_DBModel.getInstance(NewFriendListActivity.this.getApplication()).queryNewFriendList(userid);

				return newFriendList;

			} catch (Exception e) {
				this.exception = e;
			}

			return null;
		}

		@Override
		protected void onPostExecute(List<NewFriendBean> friendList) {
			// TODO Auto-generated method stub
			if (exception != null) {
				System.out.println("获取朋友列表失败");
			}

			if (friendList != null && friendList.size() > 0) {
				newFriendAdapter.addFriendList(friendList);
				newFriendAdapter.notifyDataSetChanged();
			}
		}
	}
}
