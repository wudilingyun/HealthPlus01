package com.vee.myhealth.activity;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.R.integer;
import android.R.string;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

import com.vee.healthplus.R;
import com.vee.healthplus.activity.BaseFragmentActivity;
import com.vee.healthplus.ui.setting.UpdateActivity;
import com.vee.healthplus.util.user.HP_DBModel;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.util.user.ICallBack;
import com.vee.healthplus.widget.PinnedHeaderListView;
import com.vee.healthplus.widget.PinnedHeaderListView.PinnedHeaderAdapter;
import com.vee.moments.FriendDetailActivity;
import com.vee.moments.SearchPhoneActivity;
import com.vee.myhealth.adapter.IndexGalleryAdapter;
import com.vee.myhealth.bean.HealthQuestionEntity;
import com.vee.myhealth.bean.HealthResultEntity;
import com.vee.myhealth.bean.Health_Report;
import com.vee.myhealth.bean.ResultEntity;
import com.vee.myhealth.bean.TZtest;
import com.vee.myhealth.util.SqlDataCallBack;
import com.vee.myhealth.util.SqlForTest;
import com.yunfox.s4aservicetest.response.Exam;
import com.yunfox.s4aservicetest.response.GeneralResponse;
import com.yunfox.s4aservicetest.response.SearchUserResponse;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class TiZhiResultActivity extends FragmentActivity implements
		SqlDataCallBack<Health_Report> {

	private HashMap<HealthQuestionEntity, Integer> scoremMap = new HashMap<HealthQuestionEntity, Integer>();
	private HashMap<TZtest, Integer> tzscoremMap = new HashMap<TZtest, Integer>();
	private CalculateScore calculateScore;
	private String result = "";// 测试结果
	private List<Health_Report> test;
	private SqlForTest sqlForTest;
	private TextView temp_text;
	private ArrayList<ResultEntity> reArrayList = new ArrayList<ResultEntity>();
	private ResultEntity resultEntity;
	private TestAdapter adapter;
	private PinnedHeaderListView section_list_view;
	private int userid = 0;
	private String testname;
	private long currDate = System.currentTimeMillis();
	private Gallery mStormGallery = null;
	private IndexGalleryAdapter mStormAdapter = null;
	private List<Integer> mStormListData = new ArrayList<Integer>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		View view = View.inflate(this, R.layout.health_tizhi_result, null);
		setContentView(view);
		init();
		setData();

	}

	void init() {
		temp_text = (TextView) findViewById(R.id.temp_text);
		adapter = new TestAdapter(LayoutInflater.from(this));
		section_list_view = (PinnedHeaderListView) findViewById(R.id.section_list_view);
		section_list_view.setAdapter(adapter);
		calculateScore = new CalculateScore();
		ImageView lefImageView = (ImageView) findViewById(R.id.header_lbtn_img);
		TextView textView = (TextView) findViewById(R.id.header_text);
		textView.setText("测试报告");
		userid = HP_User.getOnLineUserId(this);
		lefImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		mStormGallery = (Gallery) findViewById(R.id.index_jingqiu_gallery);
		// ======= 初始化ViewPager ========

		/*
		 * mStormListData.add(R.drawable.a); mStormListData.add( R.drawable.b);
		 * mStormListData.add( R.drawable.c);
		 */
		mStormAdapter = new IndexGalleryAdapter(this,
				R.layout.activity_index_gallery_item, mStormListData,
				new int[] { R.id.index_gallery_item_image, });
		mStormGallery.setSelection(2);
		mStormGallery.setAdapter(mStormAdapter);

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	//
	void setData() {
		sqlForTest = new SqlForTest(this);
		Intent intent = getIntent();
		String flag = intent.getStringExtra("flag");
		String type = intent.getStringExtra("type");
		@SuppressWarnings("rawtypes")
		Serializable data = intent.getExtras().getSerializable("tzscore");
		testname = intent.getStringExtra("testname");
		if (flag.equals("110")) {
			tzscoremMap = (HashMap<TZtest, Integer>) data;
			result = calculateScore.getScore(tzscoremMap);
			sqlForTest.getResultFromDB(result);
		} else if (flag.equals("111") || flag.equals("112")) {
			scoremMap = (HashMap<HealthQuestionEntity, Integer>) data;
			result = getScore(scoremMap);
			sqlForTest.getHealthResult(flag, Integer.parseInt(result));
		} else if (flag.equals("113")) {
			result = type;
			sqlForTest.getWeightLossResult(flag, result);
		}

	}

	String getScore(HashMap<HealthQuestionEntity, Integer> data) {
		if (data != null) {
			int score = 0;
			Iterator<HealthQuestionEntity> i = data.keySet().iterator();
			while (i.hasNext()) {
				HealthQuestionEntity id = (HealthQuestionEntity) i.next();
				score += data.get(id);

			}
			return score + "";
		}
		return null;

	}

	@Override
	public void getData(List<Health_Report> test) {
		// TODO Auto-generated method stub
	}

	@Override
	public void getResult(Object c) {
		String examName = null;
		// TODO Auto-generated method stub
		if (c instanceof Health_Report) {
			Health_Report hReport = (Health_Report) c;
			String[] strings = { "总体特征", "心理特征", "易患病", "外界环境影响", "饮食保健", "运动",
					"预防" };
			String[] name = { hReport.getFeature(), hReport.getHeart_feature(),
					hReport.getEasy_sicken(),
					hReport.getEnvironment_ataptation(), hReport.getBite_sup(),
					hReport.getSport(), hReport.getInterest() };
			temp_text.setText(hReport.getName());
			examName = hReport.getName();
			HP_DBModel.getInstance(this).insertUserTest(userid, testname,
					examName, currDate);

			for (int i = 0; i < strings.length; i++) {
				resultEntity = new ResultEntity();
				resultEntity.setTitle(strings[i]);
				resultEntity.setName(name[i]);
				reArrayList.add(resultEntity);
			}
		} else if (c instanceof HealthResultEntity) {
			HealthResultEntity hResultEntity = (HealthResultEntity) c;
			temp_text.setText(hResultEntity.getType());
			examName = hResultEntity.getType();
			HP_DBModel.getInstance(this).insertUserTest(userid, testname,
					examName, currDate);

			String[] strings = { "总体特征", "健康贴士", "饮食", "运动", "预防" };
			String[] name = { hResultEntity.getResult(),
					hResultEntity.getTips(), hResultEntity.getEat(),
					hResultEntity.getSport(), hResultEntity.getPrevent() };
			for (int i = 0; i < strings.length; i++) {

				resultEntity = new ResultEntity();
				resultEntity.setTitle(strings[i]);
				resultEntity.setName(name[i]);
				reArrayList.add(resultEntity);
			}

		}
		if( HP_User.getOnLineUserId(this)!=0){
			System.out.println("准备上传数据");
			new UpdateResult().execute(testname, currDate+"", examName);
		}else{
			System.out.println("没执行");
		}
			
		adapter.addList(reArrayList);
		adapter.notifyDataSetChanged();

	}

	private class UpdateResult extends AsyncTask<String, Void, Boolean> {

		private Exception exception;
		ProgressDialog dialog;

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				GeneralResponse gen=	SpringAndroidService.getInstance(getApplication())
						.newExamHistory(params[0], Integer.parseInt(params[1]),
								params[2]);
				if(gen.getReturncode()==200){
					return true;
				}else {
					return false;
				}

			} catch (Exception e) {
				this.exception = e;
			}

			return false;
		}

		@Override
		protected void onPostExecute(Boolean s) {
			// TODO Auto-generated method stub
			if (exception != null) {
				System.out.println("exception"+exception.toString());
			} else {
				System.out.println("success");
			}

		}
	}

	public class TestAdapter extends BaseAdapter implements
			PinnedHeaderAdapter, OnScrollListener {

		private LayoutInflater inflater;

		private ArrayList<ResultEntity> datas = new ArrayList<ResultEntity>();
		private int lastItem = 0;

		public TestAdapter(final LayoutInflater inflater) {
			this.inflater = inflater;
		}

		public void addList(List<ResultEntity> entities) {
			datas = (ArrayList<ResultEntity>) entities;
		}

		@Override
		public int getCount() {
			return datas.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = convertView;
			if (view == null) {
				view = inflater.inflate(R.layout.section_list_item, null);
			}
			final ResultEntity content = datas.get(position);
			final TextView header = (TextView) view
					.findViewById(R.id.header_tv);
			final TextView textView = (TextView) view
					.findViewById(R.id.example_text_view);

			/*
			 * if (lastItem == position) { header.setVisibility(View.INVISIBLE);
			 * } else { header.setVisibility(View.VISIBLE); }
			 */
			if (content.getName() == null) {
				textView.setVisibility(View.GONE);
				header.setVisibility(View.GONE);
			} else {
				textView.setText(content.getName());
				header.setText(content.getTitle());
			}
			return view;
		}

		@Override
		public int getPinnedHeaderState(int position) {
			// TODO Auto-generated method stub
			return PINNED_HEADER_PUSHED_UP;
		}

		@Override
		public void configurePinnedHeader(View header, int position) {
			// TODO Auto-generated method stub
			if (lastItem != position) {
				notifyDataSetChanged();
			}
			((TextView) header.findViewById(R.id.header_text)).setText(datas
					.get(position).getName());
			lastItem = position;
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			if (view instanceof PinnedHeaderListView) {
				((PinnedHeaderListView) view)
						.configureHeaderView(firstVisibleItem);
			}
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {

		}

	}

}
