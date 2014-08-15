package com.vee.moments.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vee.healthplus.R;
import com.vee.healthplus.common.MyApplication;
import com.vee.healthplus.heahth_news_http.ImageLoader;
import com.vee.myhealth.bean.NewFriendBean;
import com.yunfox.s4aservicetest.response.GeneralResponse;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class NewFriendAdapter extends BaseAdapter {
	Context context;
	LayoutInflater inflater;
	List<NewFriendBean> friendList;
	
	public NewFriendAdapter(Context context) {
		super();
		this.context = context;
		inflater = LayoutInflater.from(context);
		friendList = new ArrayList<NewFriendBean>();
	}

	public void addFriendList(List<NewFriendBean> addFriendList) {
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
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	@SuppressLint({ "ResourceAsColor", "NewApi" })
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = null;
		if (convertView != null) {
			view = convertView;
		} else {
			view = (View) inflater.inflate(R.layout.moments_newfriendlist_item,
					parent, false);

		}
		TextView textViewFriendName = (TextView) view
				.findViewById(R.id.friendname);
		ImageView imageViewFriendAvatar = (ImageView) view
				.findViewById(R.id.friendavatar);
		TextView textViewAddFriendStatus = (TextView) view
				.findViewById(R.id.addfriendstatus);
		
		NewFriendBean friend = friendList.get(position);
		
		textViewFriendName.setText(friend.getAccountname());
		
		if(friend.getIaddfriend() == 1)
		{
			textViewAddFriendStatus.setText(context.getResources().getString(R.string.added));
			//textViewAddFriendStatus.setBackground(null);
			textViewAddFriendStatus.setTextColor(Color.GRAY);
			textViewAddFriendStatus.setTextSize(20.f);
			textViewAddFriendStatus.setTag(R.string.contact_state, "added");
		}
		else
		{
			textViewAddFriendStatus.setText( context.getResources().getString(R.string.add));
			textViewAddFriendStatus.setTextColor(Color.WHITE);
			textViewAddFriendStatus.setTextSize(16.f);
			textViewAddFriendStatus.setGravity(Gravity.CENTER);
			textViewAddFriendStatus.setTag(R.string.contact_state, "add");
			textViewAddFriendStatus.setBackgroundResource(R.drawable.btn_chatbtn3_normal);
			textViewAddFriendStatus.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					new AddFriendTask().execute((TextView) v);
				}
			});
		}
		imageViewFriendAvatar.setImageResource(R.drawable.myhealth_users_avatar);
		ImageLoader.getInstance(context).addTask(friend.getAccountavatar(), imageViewFriendAvatar);

		return view;
	}

	// ***************************************
		// Private classes
		// ***************************************
		private class AddFriendTask extends
				AsyncTask<TextView, Void, GeneralResponse> {

			private Exception exception;
			ProgressDialog dialog;
			TextView mTv;

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();

				dialog = new ProgressDialog(context);
				dialog.show();
			}

			@Override
			protected GeneralResponse doInBackground(TextView... tv) {
				// TODO Auto-generated method stub
				mTv=tv[0];
				try {
					GeneralResponse generalResponse = SpringAndroidService
							.getInstance(MyApplication.getInstance()).addFriend(
									(Integer) tv[0].getTag(R.string.contact_yysid));

					return generalResponse;

				} catch (Exception e) {
					this.exception = e;
					System.out.print(e.toString());
				}

				return null;
			}

			@Override
			protected void onPostExecute(GeneralResponse generalResponse) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if (exception != null) {
					Toast.makeText(context,
							"添加失败",
							Toast.LENGTH_SHORT).show();
				}

				if (generalResponse != null) {
					if (generalResponse.getReturncode() == 200) {
						Toast.makeText(context,
								"添加成功", Toast.LENGTH_SHORT).show();
						mTv.setText(context.getResources().getString(R.string.added));
						mTv.setTag(R.string.contact_state, "added");
					} else {
						Toast.makeText(context,
								"添加失败",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		}
}
