package com.vee.imandforum.forum;

import org.springframework.util.MultiValueMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vee.imandforum.R;
import com.yunfox.s4aservicetest.response.GeneralResponse;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class ReplyPostActivity extends Activity {
	private int fid;
	private int pid;
	private int tid;
	private Button replypostbutton;
	private String message;
	private EditText messageedittext;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reply_post);
		
		Intent intent = getIntent();
		fid = intent.getIntExtra("fid", 0);
		pid = intent.getIntExtra("pid", 0);
		tid = intent.getIntExtra("tid", 0);
		
		messageedittext = (EditText) findViewById(R.id.reply_content);
		
		replypostbutton = (Button)findViewById(R.id.ReplyPost);
		replypostbutton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				message = messageedittext.getText().toString();
				if(message.length() == 0 )
				{
					Toast.makeText(ReplyPostActivity.this, "请输入回复内容！", Toast.LENGTH_SHORT).show();
					return;
				}
				
				showProgressDialog("回帖中...");
				new ReplyPostTask().execute();
			}
		});
	}

	// ***************************************
	// Private classes
	// ***************************************
	private class ReplyPostTask extends AsyncTask<Void, Void, GeneralResponse> {
		private MultiValueMap<String, String> formData;
		private Exception exception;

		@Override
		protected GeneralResponse doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				GeneralResponse generalResponse = SpringAndroidService.getInstance(
						getApplication()).replyPost(fid, pid, tid, message);

				return generalResponse;

			} catch (Exception e) {
				this.exception = e;
			}

			return null;
		}

		@Override
		protected void onPostExecute(GeneralResponse generalResponse) {
			dismissProgressDialog();
			if (exception != null) {
				Toast.makeText(ReplyPostActivity.this, "回复失败", Toast.LENGTH_SHORT);
			}
			else{
				ReplyPostActivity.this.setResult(200);
				ReplyPostActivity.this.finish();
			}
		}
	}
	
	public void showProgressDialog(String message) {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setIndeterminate(true);
		}
		
		progressDialog.setMessage(message);
		progressDialog.show();
	}
	
	public void dismissProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}
}
