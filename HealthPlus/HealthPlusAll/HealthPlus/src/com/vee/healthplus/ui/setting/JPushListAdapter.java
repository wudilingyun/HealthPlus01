package com.vee.healthplus.ui.setting;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vee.healthplus.R;
import com.vee.myhealth.bean.JPushBean;

public class JPushListAdapter extends BaseAdapter {

	LayoutInflater inflater;
	List<JPushBean> jlist;
	List<Bitmap> bitmaps;

	public JPushListAdapter(Context context) {
		super();
		inflater = LayoutInflater.from(context);
		jlist = new ArrayList<JPushBean>();
	}

	public void listaddAdapter(List<JPushBean> jlist) {
		this.jlist=jlist;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return jlist.size();
	}

	@Override
	public JPushBean getItem(int position) {
		// TODO Auto-generated method stub
		return jlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = null;
		if (convertView != null) {
			view = convertView;

		} else {

			view = (View) inflater.inflate(R.layout.jpush_list_item,
					parent, false);

			ViewHolder v = new ViewHolder();

			v.jpushTitle = (TextView) view.findViewById(R.id.jpush_list_title_tv);
			// v.newsbrief = (TextView) view.findViewById(R.id.newsbrief);
			v.jpushContent = (TextView) view
					.findViewById(R.id.jpush_list_content_tv);
			v.jpushIndex = (TextView) view
					.findViewById(R.id.jpush_list_index_tv);
			v.jpushTime = (TextView) view
					.findViewById(R.id.jpush_list_time_tv);
			view.setTag(v);
		}

		ViewHolder v = (ViewHolder) view.getTag();
		if(position%2==0){
			view.setBackgroundColor(0x55A5A5A5);
		}else{
			view.setBackgroundColor(Color.WHITE);
		}
		v.jpushIndex.setText(position+1+".");
		v.jpushTitle.setText(jlist.get(position).getTitle());
		v.jpushContent.setText(jlist.get(position).getContent());
		long time = jlist.get(position).getTime();
		Date date = new Date(time);
		SimpleDateFormat df = new SimpleDateFormat("MM月dd日");
		String str = df.format(date);
		v.jpushTime.setText(str);
		return view;
	}

	public class ViewHolder {
		private TextView jpushTitle, jpushContent,jpushIndex,jpushTime;
	}

}
