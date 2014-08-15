package com.vee.healthplus.ui.setting;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.vee.healthplus.R;
import com.vee.healthplus.activity.BaseFragmentActivity;
import com.vee.healthplus.widget.HeaderView;

public class HealthPlusAboutActivity extends BaseFragmentActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = View.inflate(this,
				R.layout.healthplus_setting_about_layout, null);
		setContainer(view);
		setRightBtnVisible(View.GONE);
		setLeftBtnType(HeaderView.HEADER_BACK);
		getHeaderView().setHeaderTitle("关于");
		setLeftBtnRes(R.drawable.healthplus_headview_back_btn);
		String str = null;
		try {
			str = getVersionName();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		TextView tv = (TextView) findViewById(R.id.health_plus_setting_about_version_tv);
		tv.setText(getResources().getString(R.string.setting_detail_version)
				+ str);
	}

	private String getVersionName() throws Exception {
		PackageManager packageManager = getPackageManager();
		PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),
				0);
		String version = packInfo.versionName;
		return version;
	}
}
