package com.vee.healthplus.ui.setting;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vee.healthplus.R;
import com.yunfox.s4aservicetest.response.ExamHistory;

public class TestHistoryListAdapter extends BaseAdapter {

	LayoutInflater inflater;
	List<ExamHistory> testlist;
	List<Bitmap> bitmaps;

	public TestHistoryListAdapter(Context context) {
		super();
		inflater = LayoutInflater.from(context);
		testlist = new ArrayList<ExamHistory>();
	}

	public void listaddAdapter(List<ExamHistory> newslist) {
		this.testlist=newslist;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return testlist.size();
	}

	@Override
	public ExamHistory getItem(int position) {
		// TODO Auto-generated method stub
		return testlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = null;
		if (convertView != null) {
			view = convertView;

		} else {

			view = (View) inflater.inflate(R.layout.test_history_item, parent,
					false);

			ViewHolder v = new ViewHolder();

			v.testIndex = (TextView) view.findViewById(R.id.test_history_index);
			v.testName = (TextView) view.findViewById(R.id.test_history_name);
			v.testResult = (TextView) view
					.findViewById(R.id.test_history_result);
			v.testTime = (TextView) view.findViewById(R.id.test_history_time);
			// v.newsbrief = (TextView) view.findViewById(R.id.newsbrief);
			view.setTag(v);
		}

		ViewHolder v = (ViewHolder) view.getTag();
		if(position%2==0){
			view.setBackgroundColor(0xFFA5A5A5);
		}else{
			view.setBackgroundColor(0xFFEFEFEF);
		}
		v.testIndex.setText(position + 1 + "");
		v.testName.setText(testlist.get(position).getType());
		v.testResult.setText(testlist.get(position).getTestresult());
		Timestamp time = testlist.get(position).getTesttime();
		SimpleDateFormat df = new SimpleDateFormat("MM.dd HH:mm");
		String str = df.format(time);
		v.testTime.setText(str);
		return view;
	}

	public class ViewHolder {
		private TextView testIndex, testName, testResult, testTime;
	}

}
