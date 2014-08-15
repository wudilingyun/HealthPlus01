package com.vee.myhealth.activity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.vee.healthplus.R;
import com.vee.healthplus.activity.BaseFragmentActivity;
import com.vee.healthplus.heahth_news_http.ImageLoader;
import com.vee.healthplus.http.StatisticsUtils;
import com.vee.healthplus.ui.main.QuitDialog;
import com.vee.healthplus.util.user.HP_DBModel;
import com.vee.healthplus.util.user.HP_User;
import com.vee.myhealth.bean.TZtest;
import com.vee.myhealth.util.SqlDataCallBack;
import com.vee.myhealth.util.SqlForTest;

public class TiZhiActivity extends BaseFragmentActivity implements
		SqlDataCallBack<TZtest>, OnItemClickListener, OnClickListener {
	private SQLiteDatabase database;
	private SqlForTest sqlForTest;
	private TestAdapter testAdapter;
	private ListView testListView;
	private Button submit_butt;
	private int selectId = 0;
	private List<TZtest> tsList;
	private TextView qid;
	private ProgressBar progressBar;
	private int progresscount;
	private int userId;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		View view = View.inflate(this, R.layout.health_tizhi_list, null);
		setContainer(view);
		getHeaderView().setHeaderTitle("体质测试");
		setRightBtnVisible(View.GONE);
		setLeftBtnVisible(View.VISIBLE);
		setLeftBtnType(1);
		setLeftBtnRes(R.drawable.hp_w_header_view_back);
		userId = HP_User.getOnLineUserId(this);
		init();
		sqlForTest = new SqlForTest(this);
		getData();
	}

	void init() {
		testListView = (ListView) findViewById(R.id.tizhi_list);
		submit_butt = (Button) findViewById(R.id.submit_butt);
		submit_butt.setOnClickListener(this);
		testListView.setOnItemClickListener(this);
		testAdapter = new TestAdapter(this);
		testListView.setAdapter(testAdapter);
		qid = (TextView) findViewById(R.id.examcount);

		progressBar = (ProgressBar) findViewById(R.id.exam_progressBar);

		progressBar.setProgress(0);
	}

	void getData() {
		// 从数据库获得当前测试帐号的性别信息。
		int userid = HP_User.getOnLineUserId(this);
		if (userid == 0) {
			// 没登录就等着把
			sqlForTest.getSymptomFromDB("-1");
		} else {
			HP_User user = HP_DBModel.getInstance(this).queryUserInfoByUserId(
					userid, true);
			sqlForTest.getSymptomFromDB(user.userSex + "");
		}
	}

	@Override
	public void getData(List<TZtest> test) {
		// TODO Auto-generated method stub
		tsList = (List<TZtest>) test;
		testAdapter.listaddAdapter(tsList);
		testAdapter.notifyDataSetChanged();
		qid.setText(0 + "/" + tsList.size());
		progressBar.setMax(tsList.size());
	}

	private class TestAdapter extends BaseAdapter {

		LayoutInflater inflater;
		LinkedList<TZtest> newslist;
		private ImageLoader imageLoader;
		private List<Bitmap> imgbitmap;// 要加载的图片
		private Context context;
		List<Bitmap> bitmaps;
		private List<Boolean> radio1, radio2, radio3, radio4, radio5;
		private HashMap<Integer, Integer> cheMap = new HashMap<Integer, Integer>();
		private HashMap<TZtest, Integer> scoremMap = new HashMap<TZtest, Integer>();

		public TestAdapter(Context context) {
			super();
			inflater = LayoutInflater.from(context);
			newslist = new LinkedList<TZtest>();
			this.context = context;

		}

		public void listaddAdapter(List<TZtest> newslist) {
			this.newslist.clear();
			this.newslist.addAll(newslist);
			for (int i = 0; i < newslist.size(); i++) {
				cheMap.put(i, 0);
			}
		}

		HashMap<TZtest, Integer> getScoreMap() {
			return scoremMap;

		}

		@Override
		public boolean isEnabled(int position) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return newslist.size();
		}

		@Override
		public TZtest getItem(int position) {
			return newslist.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final TZtest tZtest = (TZtest) getItem(position);
			View view = null;
			if (convertView != null) {
				view = convertView;

			} else {

				view = (View) inflater.inflate(R.layout.health_tizhi_list_item,
						parent, false);

				final ViewHolder v = new ViewHolder();

				v.content = (TextView) view.findViewById(R.id.question_tv);
				v.radioGroup = (RadioGroup) view
						.findViewById(R.id.answer_radiogroup);
				v.rb1 = (RadioButton) view.findViewById(R.id.ans1_rb);
				v.rb2 = (RadioButton) view.findViewById(R.id.ans2_rb);
				v.rb3 = (RadioButton) view.findViewById(R.id.ans3_rb);
				v.rb4 = (RadioButton) view.findViewById(R.id.ans4_rb);
				v.rb5 = (RadioButton) view.findViewById(R.id.ans5_rb);
				v.rb4.setVisibility(View.VISIBLE);
				v.rb5.setVisibility(View.VISIBLE);

				view.setTag(v);
			}

			final ViewHolder v = (ViewHolder) view.getTag();
			v.content.setText(newslist.get(position).getNum() + "."
					+ newslist.get(position).getQuestion());
			v.radioGroup.setOnCheckedChangeListener(null);
			v.rb1.setTag(1);
			v.rb2.setTag(2);
			v.rb3.setTag(3);
			v.rb4.setTag(4);
			v.rb5.setTag(5);
			v.radioGroup.setId(position);
			/*
			 * if (0 == position) { System.out.println(position);
			 * v.radioGroup.setVisibility(View.VISIBLE); }else{
			 * v.radioGroup.setVisibility(View.GONE); }
			 */

			System.out.println("当前id" + testListView.getSelectedItemPosition());

			if (cheMap.size() > 0) {
				Iterator it = cheMap.keySet().iterator();
				while (it.hasNext()) {
					int id = (Integer) it.next();
					if (id == v.radioGroup.getId()) {
						if (cheMap.get(id) == 0) {
							v.radioGroup.clearCheck();
						} else {
							if (cheMap.get(id) == R.id.ans1_rb) {
								v.radioGroup.check(R.id.ans1_rb);
							} else if (cheMap.get(id) == R.id.ans2_rb) {
								v.radioGroup.check(R.id.ans2_rb);
							} else if (cheMap.get(id) == R.id.ans3_rb) {
								v.radioGroup.check(R.id.ans3_rb);
							} else if (cheMap.get(id) == R.id.ans4_rb) {
								v.radioGroup.check(R.id.ans4_rb);
							} else if (cheMap.get(id) == R.id.ans5_rb) {
								v.radioGroup.check(R.id.ans5_rb);
							}
						}
					}
				}
			}

			v.radioGroup
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(RadioGroup radioGroup,
								int checkedId) {

							int id = radioGroup.getId();
							if (checkedId != -1) {

								System.out.println("点击才调用");
								RadioButton tempButton = (RadioButton) findViewById(checkedId);
								scoremMap.put(tZtest,
										(Integer) tempButton.getTag());

								progresscount = scoremMap.values().size();
								qid.setText(progresscount + "/" + tsList.size());
								progressBar.setProgress(progresscount);

								System.out.println("选择的分数是"
										+ tempButton.getTag());
							}
							switch (checkedId) {
							case R.id.ans1_rb:
								cheMap.put(id, R.id.ans1_rb);
								break;
							case R.id.ans2_rb:
								cheMap.put(id, R.id.ans2_rb);
								break;
							case R.id.ans3_rb:
								cheMap.put(id, R.id.ans3_rb);
								break;
							case R.id.ans4_rb:
								cheMap.put(id, R.id.ans4_rb);
								break;
							case R.id.ans5_rb:
								cheMap.put(id, R.id.ans5_rb);
								break;
							default:
								cheMap.put(id, 0);
								break;
							}
						}

					});

			return view;
		}

		public class ViewHolder {

			private int position;
			private TextView content;
			private RadioGroup radioGroup;
			public RadioButton rb1, rb2, rb3, rb4, rb5;
			private ImageView imghead;
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
		QuitDialog qd = new QuitDialog(true, "提示");
		qd.show(this.getSupportFragmentManager(), "exam");

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.submit_butt:
			// TODO 跳转页面显示最后计算完分数hou的结果
			if (testAdapter.getScoreMap().size() == tsList.size()) {
				Intent intent = new Intent(TiZhiActivity.this,
						TestResultActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("tzscore", testAdapter.getScoreMap());
				intent.putExtras(bundle);
				intent.putExtra("flag", "110");
				intent.putExtra("testname", "体质测试");
				StatisticsUtils.testStatistics(TiZhiActivity.this, userId + "",
						StatisticsUtils.TEST_SM_ID, StatisticsUtils.TEST_SM);
				startActivity(intent);
				this.finish();
			} else {
				Toast.makeText(this, "没答完亲", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void getResult(Object c) {
		// TODO Auto-generated method stub

	}

}
