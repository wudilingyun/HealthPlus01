package com.vee.myhealth.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.security.auth.PrivateCredentialPermission;

import com.vee.healthplus.R;
import com.vee.healthplus.heahth_news_beans.Doc;
import com.vee.healthplus.heahth_news_http.ImageLoader;
import com.yunfox.s4aservicetest.response.Exam;
import com.yunfox.s4aservicetest.response.ExamType;

import android.R.string;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class MyHealthUsersAdapter extends BaseAdapter {

	LayoutInflater inflater;
	List<String> username;
	private ImageLoader imageLoader;
	private List<Bitmap> imgbitmap;// 要加载的图片
	private Context context;
	List<Bitmap> bitmaps;
	HashMap<String, Boolean> states = new HashMap<String, Boolean>();// 用于记录每个RadioButton的状态，

	public MyHealthUsersAdapter(Context context) {
		super();
		inflater = LayoutInflater.from(context);
		username = new ArrayList<String>();
		this.context = context;
	}

	public void listaddAdapter(List<String> username) {
		this.username.clear();
		this.username.addAll(username);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return username.size();
	}

	@Override
	public String getItem(int position) {
		// TODO Auto-generated method stub
		return username.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View view = null;
		if (convertView != null) {
			view = convertView;

		} else {

			view = (View) inflater.inflate(R.layout.health_myhealth_users_item,
					parent, false);

			ViewHolder v = new ViewHolder();

			v.username = (TextView) view.findViewById(R.id.username_tv);
			v.imageView_head = (ImageView) view.findViewById(R.id.user_head);
			view.setTag(v);
		}

		ViewHolder v = (ViewHolder) view.getTag();
		v.username.setText(username.get(position).toString());
		final RadioButton radio = (RadioButton) view
				.findViewById(R.id.radioButton1);
		if(position==0){
			radio.setChecked(true);
		}
		v.rdBtn = radio;

		v.rdBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				// 重置，确保最多只有一项被选中
				for (String key : states.keySet()) {
					states.put(key, false);

				}
				states.put(String.valueOf(position), radio.isChecked());
				MyHealthUsersAdapter.this.notifyDataSetChanged();
			}
		});

		boolean res = false;
		if (states.get(String.valueOf(position)) == null
				|| states.get(String.valueOf(position)) == false) {
			res = false;
			states.put(String.valueOf(position), false);
		} else
			res = true;

		v.rdBtn.setChecked(res);

		return view;
	}

	public class ViewHolder {

		private int position;
		private TextView username;
		private ImageView imageView_head;
		private RadioButton rdBtn;
	}

}
