package com.vee.moments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.vee.healthplus.R;

public class SendInvitationActivity extends Activity implements OnClickListener {
	Button sendBtn;
	TextView nameTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moments_send_invitation_layout);
		initView();
		setListener();
	}

	private void initView() {
		sendBtn = (Button) findViewById(R.id.moments_invitation_send_btn);
		nameTv = (TextView) findViewById(R.id.moments_invitation_name_tv);
		nameTv.setText(getIntent().getStringExtra("name"));
	}

	private void setListener() {
		sendBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.moments_invitation_send_btn:
			String phone = getIntent().getStringExtra("phone");
			Uri uri = Uri.parse("smsto://" + phone);
			Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
			String content = "云医生是集用户健康检测、健康管理、健康追踪、健康资讯及用户健康交流平台于一体的健康管理软件。"
					+ "挺方便、实用的，推荐你用一下。"
					+ "下载地址：http://cdn.17vee.com/lmstation/shoujiweishi/700071/CloudDoctor.apk";
			intent.putExtra("sms_body", content);
			startActivity(intent);
			break;
		}
	}
}
