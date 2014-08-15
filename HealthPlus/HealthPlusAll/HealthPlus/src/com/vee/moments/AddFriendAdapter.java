package com.vee.moments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vee.healthplus.R;

public class AddFriendAdapter extends BaseAdapter {
	private Context mContext;
	private String[] sTitles;
	private int icons[] = { R.drawable.moments_search_phone,
			R.drawable.moments_phone,
			R.drawable.moments_message };

	protected LayoutInflater _mInflater;
	
	public AddFriendAdapter(Context mContext, String[] strings) {
		super();
		this.mContext = mContext;
		this.sTitles = strings;
		_mInflater = LayoutInflater.from(this.mContext);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return sTitles.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if (convertView == null) {
			convertView = _mInflater.inflate(R.layout.moments_addfriend_listview_item, null);
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView
					.findViewById(R.id.addfriendimage);
			holder.title = (TextView) convertView
					.findViewById(R.id.addfriendname);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.title.setText(sTitles[position]);
		holder.icon.setImageResource(icons[position]);
		return convertView;
	}

	class ViewHolder {
		ImageView icon;
		TextView title;
	}
}
