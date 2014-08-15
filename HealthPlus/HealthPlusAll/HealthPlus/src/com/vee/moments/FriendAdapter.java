package com.vee.moments;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vee.healthplus.R;
import com.vee.healthplus.heahth_news_http.ImageLoader;
import com.vee.healthplus.ui.heahth_news.Health_ValuableBook_NewsAdapter.ViewHolder;
import com.vee.healthplus.widget.RoundImageView;
import com.yunfox.s4aservicetest.response.Friend;

public class FriendAdapter extends BaseAdapter {
	Context context;
	LayoutInflater inflater;
	List<Friend> friendList;
	private ImageLoader imageLoader;
	public FriendAdapter(Context context,ImageLoader imageLoader) {
		super();
		this.context = context;
		inflater = LayoutInflater.from(context);
		friendList = new ArrayList<Friend>();
		this.imageLoader = imageLoader;
	}

	public void addFriendList(List<Friend> addFriendList) {
		friendList.clear();
		friendList.addAll(addFriendList);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return friendList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return friendList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder v = null;
		if (convertView != null) {
			v = (ViewHolder)convertView.getTag();
		} else {
			convertView = (View) inflater.inflate(R.layout.moments_friendlist_item,
					parent, false);
			v = new ViewHolder();
			v.textViewFriendName = (TextView) convertView.findViewById(R.id.friendname);
			// v.newsbrief = (TextView) view.findViewById(R.id.newsbrief);
			v.imageViewFriendAvatar = (RoundImageView) convertView
					.findViewById(R.id.friendavatar);
		
			convertView.setTag(v);
		}
		Friend friend = friendList.get(position);
		v.imageViewFriendAvatar.setImageResource(R.drawable.myhealth_users_avatar);
		v.textViewFriendName.setText(friend.getFriendname());
		String imgurl= friend.getFriendavatarurl();
		System.out.println("用户列表的头像url"+imgurl);
		imageLoader.addTask(imgurl, v.imageViewFriendAvatar);

		return convertView;
	}
	public class ViewHolder {

		private TextView textViewFriendName ;
		private ImageView imageViewFriendAvatar;
	}

}
