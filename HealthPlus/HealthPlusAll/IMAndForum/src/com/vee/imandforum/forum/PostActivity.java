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

public class PostActivity extends Activity {
	private Button newpostbutton;
	private EditText subjectedittext;
	private EditText messageedittext;
	private int fid;
	private String subject;
	private String message;
	
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post);

		Intent intent = getIntent();
		fid = intent.getIntExtra("fid", 0);

		subjectedittext = (EditText) findViewById(R.id.topic_title);
		messageedittext = (EditText) findViewById(R.id.topic_content);

		newpostbutton = (Button) findViewById(R.id.newpost);
		newpostbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				subject = subjectedittext.getText().toString();
				message = messageedittext.getText().toString();
				
				if(subject.length() == 0 )
				{
					Toast.makeText(PostActivity.this, "请输入帖子标题！", Toast.LENGTH_SHORT).show();
					return;
				}
				
				if(message.length() == 0 )
				{
					Toast.makeText(PostActivity.this, "请输入帖子内容！", Toast.LENGTH_SHORT).show();
					return;
				}
				
				showProgressDialog("发帖中...");
				new NewPostTask().execute();
			}
		});
	}

	// ***************************************
	// Private classes
	// ***************************************
	private class NewPostTask extends AsyncTask<Void, Void, GeneralResponse> {
		private MultiValueMap<String, String> formData;
		private Exception exception;

		@Override
		protected GeneralResponse doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				GeneralResponse generalResponse = SpringAndroidService.getInstance(
						getApplication()).newPost(fid, subject, message);

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
				Toast.makeText(PostActivity.this, "发帖失败", Toast.LENGTH_SHORT);
			}
			else{
				PostActivity.this.setResult(100);
				PostActivity.this.finish();
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
