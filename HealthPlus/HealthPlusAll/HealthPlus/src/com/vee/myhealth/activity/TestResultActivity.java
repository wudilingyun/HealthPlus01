package com.vee.myhealth.activity;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.vee.healthplus.R;
import com.vee.healthplus.heahth_news_utils.CheckNetWorkStatus;
import com.vee.healthplus.util.user.HP_DBModel;
import com.vee.healthplus.util.user.HP_User;
import com.vee.myhealth.bean.HealthQuestionEntity;
import com.vee.myhealth.bean.HealthResultEntity;
import com.vee.myhealth.bean.Health_Report;
import com.vee.myhealth.bean.TZtest;
import com.vee.myhealth.util.SqlDataCallBack;
import com.vee.myhealth.util.SqlForTest;
import com.yunfox.s4aservicetest.response.GeneralResponse;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TestResultActivity extends FragmentActivity implements
		SqlDataCallBack<Health_Report> {

	private HashMap<HealthQuestionEntity, Integer> scoremMap = new HashMap<HealthQuestionEntity, Integer>();
	private HashMap<TZtest, Integer> tzscoremMap = new HashMap<TZtest, Integer>();
	private CalculateScore calculateScore;
	private String result = "";// 测试结果
	private SqlForTest sqlForTest;
	private TextView temp_text, content_tv, tips_tv;
	private int userid = 0;
	private String testname;
	private long currDate = System.currentTimeMillis();
	private static String IMAPATH = "testresult_img";
	private ImageView result_iv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		View view = View.inflate(this, R.layout.myhealth_tizhi_result, null);
		setContentView(view);
		init();
		setData();
	}

	void setData() {
		sqlForTest = new SqlForTest(this);
		Intent intent = getIntent();
		String flag = intent.getStringExtra("flag");
		String type = intent.getStringExtra("type");
		@SuppressWarnings("rawtypes")
		Serializable data = intent.getExtras().getSerializable("tzscore");
		testname = intent.getStringExtra("testname");
		
		//110为原来“体质测试”，已经废弃
		if (flag.equals("110")) {
			tzscoremMap = (HashMap<TZtest, Integer>) data;
			result = calculateScore.getScore(tzscoremMap);
			sqlForTest.getResultFromDB(result);
			tips_tv.setVisibility(View.GONE);
			result_iv.setVisibility(View.GONE);
		} else if (flag.equals("111") || flag.equals("112")
				|| flag.equals("113")|| flag.equals("114")) {
			scoremMap = (HashMap<HealthQuestionEntity, Integer>) data;
			result = getScore(scoremMap);
			sqlForTest.getHealthResult(flag, Integer.parseInt(result));
		}

	}

	void init() {
		temp_text = (TextView) findViewById(R.id.title_tv);
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

		content_tv = (TextView) findViewById(R.id.content_tv);
		tips_tv = (TextView) findViewById(R.id.tips_tv);
		result_iv = (ImageView) findViewById(R.id.result_iv);

	}

	@Override
	public void getResult(Object c) {
		// TODO Auto-generated method stub
		/*
		 * if (c instanceof Health_Report) { Health_Report hReport =
		 * (Health_Report) c; String name = hReport.getFeature();
		 * temp_text.setText(hReport.getName());
		 * HP_DBModel.getInstance(this).insertUserTest(userid, testname,
		 * hReport.getName(), currDate); content_tv.setText(name); } else if (c
		 * instanceof HealthResultEntity) { HealthResultEntity hResultEntity =
		 * (HealthResultEntity) c; temp_text.setText(hResultEntity.getType());
		 * HP_DBModel.getInstance(this).insertUserTest(userid, testname,
		 * hResultEntity.getType(), currDate);
		 * content_tv.setText(hResultEntity.getResult()); }
		 */

		HealthResultEntity hResultEntity = (HealthResultEntity) c;
		String img_id = hResultEntity.getImage_id();
		getWeatherImage(img_id, result_iv);
		temp_text.setText(hResultEntity.getType());
		HP_DBModel.getInstance(this).insertUserTest(userid, testname,
				hResultEntity.getType(), currDate);
		
		content_tv.setText(hResultEntity.getResult());
		if (hResultEntity.getTips() != null && hResultEntity.getTips() != "") {
			tips_tv.setText(hResultEntity.getTips());
		}
		addIgnorList(hResultEntity.getType());
		if(CheckNetWorkStatus.Status(this)){
			new UpdateResult().execute(testname,currDate+"",hResultEntity.getType());
		}else{
			Toast.makeText(this, "未连接网络，测试数据无法保存", Toast.LENGTH_LONG).show();
		}
		
	}

	void addIgnorList(String result) {
		Boolean flag = HP_DBModel.getInstance(this).queryTestResultDate(userid,
				testname);
		if (flag) {
			HP_DBModel.getInstance(this).updateTestResult(userid, testname,
					result, currDate);
		}else {
			HP_DBModel.getInstance(this).insertUserTestByCover(userid, testname,
					result, currDate);
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

	public void getWeatherImage(String name, final ImageView view) {

		Bitmap weatherbt = getImageFromAssetsFile(IMAPATH + "/" + name + ".png");
		view.setImageBitmap(weatherbt);

	}

	/*
	 * 从assets读取图片
	 */
	private Bitmap getImageFromAssetsFile(String fileName) {
		Bitmap image = null;
		AssetManager am = this.getResources().getAssets();
		try {
			InputStream is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
			return image;
		} catch (IOException e) {

			e.printStackTrace();
			return null;
		}

	}
	
	
	private class UpdateResult extends AsyncTask<String, Void, Boolean> {

		private Exception exception;
		ProgressDialog dialog;

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				GeneralResponse gen=	SpringAndroidService.getInstance(getApplication())
						.newExamHistory(params[0], Long.parseLong(params[1]),
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
}
