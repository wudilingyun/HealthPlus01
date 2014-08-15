package com.vee.myhealth.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

public class IndexGalleryAdapter extends BaseAdapter {

	Context context;
	int layoutId;
	int to[];
	List<Integer> listData;

	public IndexGalleryAdapter(Context context, int layoutId,
			List<Integer> listData, int to[]) {
		this.context = context;
		this.layoutId = layoutId;
		this.listData = listData;
		this.to = to;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listData.size() == 0 ? 0 : listData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		final int pos = position;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(layoutId, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.imageView = (ImageView) convertView.findViewById(to[0]);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
			resetViewHolder(viewHolder);
		}
			//获得图片
		viewHolder.imageView.setImageResource(listData.get(pos));

		return convertView;
	}

	static class ViewHolder {
		ImageView imageView;
	}

	protected void resetViewHolder(ViewHolder viewHolder) {
		viewHolder.imageView.setImageBitmap(null);
	}
}
