package com.vee.healthplus.ui.user;

import java.io.InputStream;

import org.apache.http.util.EncodingUtils;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vee.healthplus.R;
import com.vee.healthplus.activity.BaseFragmentActivity;
import com.vee.healthplus.heahth_news_utils.JsonCache;
import com.vee.healthplus.ui.heahth_exam.ProgressDiaogdownload;
import com.vee.healthplus.widget.HeaderView;

public class StatementDetailsTextActivity extends BaseFragmentActivity {
	WebView webView;
	String stateContent;
	private String contentUrl;
	private ProgressDiaogdownload ProgressDiaog = new ProgressDiaogdownload(
			StatementDetailsTextActivity.this);
	private JsonCache jsonCache;
	private String hasNet = "1", JsonCach = "2";

	private LinearLayout loFrameLayout;
	private ImageView loadImageView;
	private Animation news_loadAaAnimation;
	private SharedPreferences settings;
	private SharedPreferences.Editor localEditor;
	private TextView statementTv;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);

		View view = View.inflate(this, R.layout.healthplus_statement_textview,
				null);
		setContainer(view);

		getHeaderView().setHeaderTitle("声明条款");
		setRightBtnVisible(View.GONE);
		setLeftBtnVisible(View.VISIBLE);
		setLeftBtnType(HeaderView.HEADER_BACK);
		setLeftBtnRes(R.drawable.healthplus_headview_back_btn);
		statementTv = (TextView) findViewById(R.id.health_plus_statement_textview_tv);
		statementTv.setText(getFromAssets("xieyi.txt"));

	}

	public String getFromAssets(String fileName) {
		String result = "";
		try {
			InputStream in = getResources().getAssets().open(fileName);
			int lenght = in.available();
			byte[] buffer = new byte[lenght];
			in.read(buffer);
			result = EncodingUtils.getString(buffer, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
