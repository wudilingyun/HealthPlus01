package com.vee.moments;

import java.util.List;

import org.springframework.social.greenhouse.api.Profile;

import android.R.integer;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vee.healthplus.R;
import com.vee.healthplus.heahth_news_http.ImageLoader;
import com.vee.healthplus.heahth_news_utils.CheckNetWorkStatus;
import com.vee.healthplus.widget.CustomDialog;
import com.yunfox.s4aservicetest.response.Friend;
import com.yunfox.s4aservicetest.response.GeneralResponse;
import com.yunfox.s4aservicetest.response.SearchUserResponse;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class FriendListActivity extends FragmentActivity implements
		OnClickListener {
	private TextView header_text;
	private ImageView header_lbtn_img, header_rbtn_img;
	private FriendAdapter friendAdapter;
	private ListView listViewFriendlist;
	private Button buttonSearchFriend;
	private String searchcontent;
	private List<Friend> friendList;
	private ImageLoader imageLoader;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == 1)
		{
			if (resultCode == RESULT_OK)
			{
				new GetAllFriendsTask().execute();
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = View.inflate(this, R.layout.activity_friend_list, null);
		setContentView(view);
		settitle();

		listViewFriendlist = (ListView) view.findViewById(R.id.friendlist);
		imageLoader = ImageLoader.getInstance(this);
		friendAdapter = new FriendAdapter(this,imageLoader);
		listViewFriendlist.setAdapter(friendAdapter);

		buttonSearchFriend = (Button) view
				.findViewById(R.id.searchfriendbutton);
		buttonSearchFriend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EditText editTextSearchContent = (EditText) findViewById(R.id.searchfriendedittext);
				searchcontent = editTextSearchContent.getText().toString();
				if (searchcontent == null || searchcontent.length() == 0) {
					Toast.makeText(FriendListActivity.this,
							"输入为空", Toast.LENGTH_SHORT).show();
				} else {
					new SearchUserTask().execute();
				}
			}
		});
		
		listViewFriendlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				Friend friend = friendList.get(position);
				/*Intent intent = new Intent(FriendListActivity.this,
						UserMomentsActivity.class);
				intent.putExtra("friendid",
						friend.getFriendid());
				intent.putExtra("friendname", friend.getFriendname());
				intent.putExtra("friendavatar", friend.getFriendavatarurl());
				startActivity(intent);*/
				
				new SearchUserTask().execute(friend);
				
			}
		});
		
		
		listViewFriendlist.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View view,
					final int position, long id) {
				final Friend friend = friendList.get(position);
				if(CheckNetWorkStatus.Status(getApplication())){
					CustomDialog.Builder customBuilder = new CustomDialog.Builder(FriendListActivity.this);
					customBuilder
					.setTitle(friend.getFriendname())
					.setPositiveButton(R.string.OK,
							new CustomDialog.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									new DelFriendsTask().execute(friend.getFriendid(),position);
									dialog.dismiss();
								}
							}).setMessage("删除该好友?");
					Dialog dialog = customBuilder.create();
					dialog.show();
					
				}
				return false;
			}
			
		});
		new GetAllFriendsTask().execute();
	}

	void settitle() {

		header_text = (TextView) findViewById(R.id.header_text);
		header_lbtn_img = (ImageView) findViewById(R.id.header_lbtn_img);
		header_rbtn_img = (ImageView) findViewById(R.id.header_rbtn_img);
		header_rbtn_img.setVisibility(View.GONE);
		header_lbtn_img.setImageResource(R.drawable.hp_w_header_view_back);
		header_rbtn_img.setImageResource(R.drawable.moments_add_selector);
		header_text.setText(getString(R.string.momentsfriendlist));
		header_text.setOnClickListener(this);
		header_lbtn_img.setOnClickListener(this);
		header_rbtn_img.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.header_lbtn_img:
			this.finish();
			break;
		case R.id.header_rbtn_img:
			Intent intent = new Intent(this, AddFriendActivity.class);
			startActivityForResult(intent, 1);
			break;

		default:
			break;
		}
	}

	// ***************************************
	// Private classes
	// ***************************************
	private class GetAllFriendsTask extends AsyncTask<Void, Void, List<Friend>> {

		private Exception exception;
		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			//dialog = new ProgressDialog(FriendListActivity.this);
			//dialog.show();
		}

		@Override
		protected List<Friend> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				friendList = SpringAndroidService.getInstance(
						getApplication()).getAllFriend();

				return friendList;

			} catch (Exception e) {
				this.exception = e;
			}

			return null;
		}

		@Override
		protected void onPostExecute(List<Friend> friendList) {
			// TODO Auto-generated method stub
			//dialog.dismiss();
			if (exception != null) {
				System.out.println("获取朋友列表失败");
			}

			if (friendList != null && friendList.size() > 0) {
				friendAdapter.addFriendList(friendList);
				friendAdapter.notifyDataSetChanged();
			}else{
				friendAdapter.addFriendList(friendList);
				friendAdapter.notifyDataSetChanged();
				listViewFriendlist.setEmptyView(findViewById(R.id.empty_friend));
				//Toast.makeText(getApplication(), "您还没有好友", Toast.LENGTH_SHORT).show();
			}
		}
	}

	// ***************************************
	// Private classes
	// ***************************************
	private class SearchUserTask extends
			AsyncTask<Friend, Void, Profile> {

		private Exception exception;
		ProgressDialog dialog;
		private Friend friend;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			//dialog = new ProgressDialog(FriendListActivity.this);
			//dialog.show();
		}

		@Override
		protected Profile doInBackground(Friend... params) {
			// TODO Auto-generated method stub
			try {
				friend = params[0];
				Profile searchUserResponse = SpringAndroidService
						.getInstance(getApplication())
						.getProfileById(params[0].getFriendid());

				return searchUserResponse;

			} catch (Exception e) {
				this.exception = e;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Profile profile) {
			// TODO Auto-generated method stub
			//dialog.dismiss();
			if (exception != null) {

			}

			if (profile != null) {
					Intent intent = new Intent(FriendListActivity.this,
							UserMomentsActivity.class);
					intent.putExtra("friendid",
							friend.getFriendid());
					intent.putExtra("friendname", friend.getFriendname());
					intent.putExtra("friendavatar", friend.getFriendavatarurl());
					
					/*Bundle bundle = new Bundle();
					bundle.putSerializable("profile",
							profile);
					intent.putExtras(bundle);*/
					intent.putExtra("friendweight", profile.getWeight());
					intent.putExtra("friendage", profile.getAge());
					intent.putExtra("friendsex", profile.getGender());
					intent.putExtra("friendheight", profile.getHeight());
					startActivity(intent);
			}
		}
	}
	
	
	private class DelFriendsTask extends AsyncTask<Integer, Void, Boolean> {

		private Exception exception;
		ProgressDialog dialog;
		private int position;

		@Override
		protected Boolean doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			try {
				this.position = params[1];
			GeneralResponse gen = SpringAndroidService.getInstance(
						getApplication()).delFriend(params[0]);
			System.out.println("删除好友"+params[0]);
				if(gen!=null){
					if(gen.getReturncode()==200){
						return true;
					}else {
						return false;
					}
				}

			} catch (Exception e) {
				this.exception = e;
			}

			return false;
		}

		@Override
		protected void onPostExecute(Boolean flag) {
			// TODO Auto-generated method stub
			if (exception != null) {
				System.out.println("删除失败");
			}
			if(flag){
				Toast.makeText(getApplication(), "好友删除成功", Toast.LENGTH_SHORT).show();
				new GetAllFriendsTask().execute();
			}else {
				Toast.makeText(getApplication(), "好友删除失败", Toast.LENGTH_SHORT).show();
			}
			
		}
	}

}
