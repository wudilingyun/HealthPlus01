package com.vee.imandforum.main;

import java.io.UnsupportedEncodingException;
import java.util.List;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.vee.imandforum.R;
import com.vee.imandforum.heahth_news_http.ImageLoader;
import com.vee.imandforum.im.ChatActivity;
import com.vee.imandforum.im.FriendAdapter;
import com.vee.mqtt.MqttService;
import com.yunfox.s4aservicetest.response.Friend;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class IMFragment extends SherlockFragment {
	private NewFriendMessageReceiver newFriendMessageReceiver = null;
	private ListView friendsListView;
	private FriendAdapter friendAdapter;
	private ImageLoader imageLoader;
	private List<Friend> friendList;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.im_fragment, container, false);
		
		friendsListView = (ListView) view.findViewById(R.id.friendslistview);
		friendsListView.setEmptyView(view.findViewById(R.id.empty_frinends));
		
		imageLoader = ImageLoader.getInstance(this.getActivity());
		friendAdapter = new FriendAdapter(this.getActivity(),imageLoader);
		friendsListView.setAdapter(friendAdapter);
		
		friendsListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Friend friend = friendList.get(position);
				Intent intent = new Intent(IMFragment.this.getActivity(), ChatActivity.class);
				intent.putExtra("myid", friend.getAccountid());
				intent.putExtra("friendid", friend.getFriendid());
				intent.putExtra("friendname", friend.getFriendname());
				Bundle bundle = new Bundle();
				bundle.putSerializable("friend", friend);
				intent.putExtras(bundle);
				
				startActivity(intent);
			}
		});

		// mqtt message receiver
		if (newFriendMessageReceiver == null) {
			newFriendMessageReceiver = new NewFriendMessageReceiver();
		}
		IntentFilter filter = new IntentFilter();
		filter.addAction(MqttService.ACTION_MESSAGE_ARRIVED);
		filter.setPriority(100);
		getActivity().registerReceiver(newFriendMessageReceiver, filter);

		// mqtt service
		int userid = SpringAndroidService.getInstance(getActivity().getApplication()).getMyId();
		if (userid > 0) {
			Intent intent = new Intent(getActivity().getApplicationContext(),
					MqttService.class);
			intent.putExtra("userid", userid);
			getActivity().getApplication().startService(intent);
		}
		
		new GetAllFriendsTask().execute();

		return view;
	}

	private class NewFriendMessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			byte[] textBody = intent.getByteArrayExtra("textbody");
			try {
				String sTextBody = new String(textBody, "UTF-8");
				System.out.println("broadcast message..." + sTextBody);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
							getActivity().getApplication()).getAllFriend();

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
					return;
				}

				if (friendList != null && friendList.size() > 0) {
					friendAdapter.addFriendList(friendList);
					friendAdapter.notifyDataSetChanged();
				}else{
					friendAdapter.addFriendList(friendList);
					friendAdapter.notifyDataSetChanged();
				}
			}
		}
}
