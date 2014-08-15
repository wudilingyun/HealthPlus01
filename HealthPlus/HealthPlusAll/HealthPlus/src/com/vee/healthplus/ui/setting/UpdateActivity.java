package com.vee.healthplus.ui.setting;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vee.healthplus.R;
import com.vee.healthplus.common.Common;
import com.vee.healthplus.common.MyApplication;
import com.vee.healthplus.util.FileDownloaderUtils;

public class UpdateActivity extends Activity {

    private Context mContext;
	private ProgressBar pb;
	private TextView tvPb;
	private Button btnRetry;
	private Button btnCancel;
	private RelativeLayout rl;
	private String path;

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case FileDownloaderUtils.MSG_DOWNLOAD_START:
				break;
			case FileDownloaderUtils.MSG_DOWNLOADING:
				long progress = (Long) msg.obj;
				updateUI((int) progress);
				break;
			case FileDownloaderUtils.MSG_DOWNLOAD_FINISH:
				MyApplication.startInstall(Common.UPDATE_APK_NAME, mContext);
				finish();
				break;
			case FileDownloaderUtils.MSG_DOWNLOAD_FAIL:
				updateErrorUI();
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_update);
		mContext = this;

		initView();

		path = getIntent().getStringExtra("server");
		if (path != null)
			path = path + Common.UPDATE_APK_NAME;

		startDownload();
	}

	protected void updateErrorUI() {
		// TODO Auto-generated method stub
		rl.setVisibility(View.VISIBLE);
	}

	protected void updateUI(int progress) {
		// TODO Auto-generated method stub
		pb.setProgress(progress);
		tvPb.setText(progress + "%");
	}

	private void initView() {
		// TODO Auto-generated method stub
		btnRetry = (Button) findViewById(R.id.easygame_update_btnretry);
		btnRetry.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startDownload();
				rl.setVisibility(View.GONE);
			}
		});

		btnCancel = (Button) findViewById(R.id.easygame_update_btncancel);
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		pb = (ProgressBar) findViewById(R.id.easygame_updatepb);
		tvPb = (TextView) findViewById(R.id.tvPb);
		rl = (RelativeLayout) findViewById(R.id.easygame_update_rl);
		rl.setVisibility(View.GONE);
	}

	private void startDownload() {
		// TODO Auto-generated method stub
		FileDownloaderUtils downloadThread = new FileDownloaderUtils(mContext,
				handler, path, Common.UPDATE_APK_NAME);

		downloadThread.start();
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			return false;
		}
		return super.dispatchKeyEvent(event);
	}
}
