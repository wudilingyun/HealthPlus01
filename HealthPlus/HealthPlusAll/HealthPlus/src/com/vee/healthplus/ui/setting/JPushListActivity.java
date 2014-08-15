package com.vee.healthplus.ui.setting;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vee.healthplus.R;
import com.vee.healthplus.heahth_news_beans.NewsCollectinfor;
import com.vee.healthplus.ui.user.TempActivity;
import com.vee.healthplus.util.user.HP_DBModel;
import com.vee.healthplus.util.user.HP_User;
import com.vee.myhealth.bean.JPushBean;

@SuppressLint("NewApi")
public class JPushListActivity extends Activity implements OnClickListener {

	private ListView jpushLv;
	private JPushListAdapter adapter;
	private TextView header_text, jpush_none_tv;
	private ImageView header_lbtn_img, header_rbtn_img;
	private List<JPushBean> list = null;
	public Context mContext;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		mContext = this;
		View view = View.inflate(this, R.layout.jpush_list_activity_layout,
				null);
		setContentView(view);
		gettitle();
		init(view);
	}

	private void updateView() {
		try {
			list = HP_DBModel.getInstance(this).queryJPushList(
					HP_User.getOnLineUserId(this));// 列表获取函数
		} catch (Exception e) {
			// Toast.makeText(mContext, "推送列表获取失败", Toast.LENGTH_SHORT).show();
		}
		if (list != null && list.size() != 0) {
			// Toast.makeText(mContext, "推送列表获取完成", Toast.LENGTH_SHORT).show();
			adapter.listaddAdapter(list);
			adapter.notifyDataSetChanged();
		} else {
			jpush_none_tv.setVisibility(View.VISIBLE);
		}
	}

	void gettitle() {
		header_text = (TextView) findViewById(R.id.header_text);
		header_lbtn_img = (ImageView) findViewById(R.id.header_lbtn_img);
		header_rbtn_img = (ImageView) findViewById(R.id.header_rbtn_img);
		header_rbtn_img.setVisibility(View.GONE);
		header_text.setText("健康贴士");
		header_text.setOnClickListener(this);
		header_lbtn_img.setOnClickListener(this);
		header_rbtn_img.setOnClickListener(this);
	}

	void init(View view) {
		jpush_none_tv = (TextView) findViewById(R.id.jpush_list_none_tv);
		adapter = new JPushListAdapter(this);
		jpushLv = (ListView) view.findViewById(R.id.jpush_list_lv);
		jpushLv.setAdapter(adapter);
		jpushLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {
				Intent intent = new Intent(JPushListActivity.this,
						TempActivity.class);
				intent.putExtra("title", "健康贴士");
				intent.putExtra("content", list.get(arg2).getContent());
				intent.putExtra("time", list.get(arg2).getTime());
				startActivity(intent);
			}
		});
		jpushLv.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				// TODO Auto-generated method stub
				menu.setHeaderTitle("提示");
				menu.add(1, 1, 1, "删除此条贴士").setIcon(
						android.R.drawable.stat_notify_error);
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		updateView();
	}

	@Override
	public void onStop() {
		super.onStop();

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		int position = ((AdapterContextMenuInfo) item.getMenuInfo()).position;
		JPushBean nowJPushBean = list.get(position);
		switch (item.getItemId()) {
		case 1:
			HP_DBModel.getInstance(this).deleteJPush(
					HP_User.getOnLineUserId(mContext), nowJPushBean.getTitle(),
					nowJPushBean.getContent(),nowJPushBean.getTime());
			break;
		}
		updateView();
		return super.onContextItemSelected(item);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.header_lbtn_img:
			this.finish();
			break;
		case R.id.header_rbtn_img:
			break;
		default:
			break;
		}
	}

}
