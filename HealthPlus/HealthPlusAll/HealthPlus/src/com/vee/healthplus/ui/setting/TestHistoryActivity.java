package com.vee.healthplus.ui.setting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.vee.healthplus.R;
import com.vee.healthplus.activity.BaseFragmentActivity;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.widget.HeaderView;
import com.vee.healthplus.widget.HeaderView.OnHeaderClickListener;
import com.yunfox.s4aservicetest.response.ExamHistory;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

@SuppressLint("ResourceAsColor")
public class TestHistoryActivity extends BaseFragmentActivity implements
		View.OnClickListener {

	private ListView mList;
	private TextView noneTv;
	private List<ExamHistory> testCollectinfors = new ArrayList<ExamHistory>();
	private TestHistoryListAdapter adapter;
	private ProgressDialog dialog;

	private OnHeaderClickListener headerClickListener = new OnHeaderClickListener() {

		@Override
		public void OnHeaderClick(View v, int option) {
			if (option == HeaderView.HEADER_BACK) {
				finish();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		View view = View.inflate(this, R.layout.test_history_layout, null);
		setContainer(view);
		getHeaderView().setHeaderTitle("测试数据");
		setRightBtnVisible(View.INVISIBLE);
		setLeftBtnVisible(View.VISIBLE);
		setLeftBtnType(HeaderView.HEADER_BACK);
		setLeftBtnRes(R.drawable.healthplus_headview_back_btn);
		setHeaderClickListener(headerClickListener);
		initView(view);
		intiData();
	}

	private void initView(View view) {
		mList = (ListView) view.findViewById(R.id.test_history_ll);
		mList.setDividerHeight(0);
		noneTv = (TextView) view.findViewById(R.id.test_history_none_tv);
		dialog = new ProgressDialog(this);
		dialog.setCanceledOnTouchOutside(false);
	}

	private void intiData() {
		int userid = HP_User.getOnLineUserId(this);
		adapter = new TestHistoryListAdapter(this);
		try {
			dialog.show();
			new GetExamHistoryTask(this).execute();
		} catch (Exception e) {
			System.out.println("GetExamHistoryTask get error");
		}
		Log.i("lingyun", "testCollectinfors=" + testCollectinfors);

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {

		case R.id.cannel_btn:
			finish();
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	class GetExamHistoryTask extends AsyncTask<Void, Void, Void> {
		private Exception exception;
		private Activity activity;

		public GetExamHistoryTask(Activity activity) {
			this.activity = activity;
		}

		@Override
		protected void onPreExecute() {

		}

		@Override
		@SuppressWarnings("unchecked")
		protected Void doInBackground(Void... params) {
			try {
				testCollectinfors = SpringAndroidService.getInstance(
						activity.getApplication()).getExamHistoryList();
				Log.i("lingyun", "GetExamHistoryTask.testCollectinfors.size="
						+ testCollectinfors.size());
				Collections.sort(testCollectinfors, mComparator);
			} catch (Exception e) {
				this.exception = e;
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void v) {
			if (exception != null) {
				Log.i("lingyun", "GetExamHistoryTask.exception!!");
			}
			dialog.dismiss();
			if (testCollectinfors == null || testCollectinfors.size() == 0) {
				noneTv.setVisibility(View.VISIBLE);
			} else {
				adapter.listaddAdapter(testCollectinfors);
				mList.setAdapter(adapter);
				noneTv.setVisibility(View.GONE);
			}

		}
	}

	Comparator<ExamHistory> mComparator = new Comparator<ExamHistory>() {
		public int compare(ExamHistory e1, ExamHistory e2) {
			// 按时间排序
			Long sub = e2.getTesttime().getTime() - e1.getTesttime().getTime();
			return sub.intValue();
		}
	};

}
