package com.vee.healthplus.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.graphics.Path.Direction;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.vee.healthplus.R;
import com.vee.healthplus.activity.BaseFragmentActivity;
import com.vee.healthplus.common.MyApplication;
import com.vee.healthplus.heahth_news_utils.CheckNetWorkStatus;
import com.vee.healthplus.util.VersionUtils;
import com.vee.healthplus.util.user.ICallBack;
import com.vee.healthplus.widget.HeaderView;

public class HealthPlusSettingActivity extends BaseFragmentActivity implements
		ICallBack {
	private Context mContext;
	private HealthPlusSettingAdapter settingAdapter;
	private ListView settingList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this;
		View view = View
				.inflate(this, R.layout.healthplus_setting_layout, null);
		setContainer(view);
		settingList = (ListView) findViewById(R.id.health_plus_setting_lv);
		settingAdapter = new HealthPlusSettingAdapter(this);
		settingList.setAdapter(settingAdapter);
		setRightBtnVisible(View.GONE);
		setLeftBtnType(HeaderView.HEADER_BACK);
		getHeaderView().setHeaderTitle("设置");
		setLeftBtnRes(R.drawable.healthplus_headview_back_btn);
		settingList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				switch (position) {
				case 0:
					VersionUtils.getInstance().checkVersion(
							HealthPlusSettingActivity.this, false);
					break;
				case 1:
					intent = new Intent(HealthPlusSettingActivity.this,
							SettingFeedBack.class);
					startActivity(intent);
					break;
				case 2:
					String sendMsg = getResources().getString(
							R.string.hp_share_invite);
					String sendTitleUrl = getResources().getString(
							R.string.hp_share_address);
					if(CheckNetWorkStatus.Status(getApplication())){
						MyApplication.shareBySystem(mContext, sendMsg, "", sendTitleUrl, "",
								"",sendMsg);
					}else {
						Toast.makeText(getApplication(), "请检查网络连接", Toast.LENGTH_SHORT).show();
					}
					
					break;
				case 3:
					intent = new Intent(HealthPlusSettingActivity.this,
							HealthPlusAboutActivity.class);
					startActivity(intent);
					break;
				default:
					break;
				}
			}
		});
		settingList.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch(event.getAction()){
				case MotionEvent.ACTION_MOVE:
					return true;
				}
				return false;
			}
		});
		
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	public void notifyDateChange() {
		settingAdapter.notifyDataChange();
	}

	@Override
	public void onChange() {

	}

	@Override
	public void onCancel() {

	}

}
