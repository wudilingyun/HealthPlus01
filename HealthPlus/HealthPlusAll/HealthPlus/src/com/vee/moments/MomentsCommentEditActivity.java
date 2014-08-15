package com.vee.moments;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vee.healthplus.R;
import com.yunfox.s4aservicetest.response.GeneralResponse;
import com.yunfox.s4aservicetest.response.MomentsComment;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class MomentsCommentEditActivity extends Activity implements
		View.OnClickListener {
	private Button sendButton;
	private EditText sendEditText;
	private String content = null;
	private int momentsid = 0;
	private int replytoid = 0;
	private List<MomentsComment> momentsCommentList = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Window window = getWindow();
		window.setGravity(Gravity.BOTTOM);
		window.setWindowAnimations(R.style.dialog_updown);
		setContentView(R.layout.moments_comment_edit_layout);
		sendButton = (Button) findViewById(R.id.moments_comment_button);
		sendEditText = (EditText) findViewById(R.id.moments_comment_edittext);
		sendButton.setOnClickListener(this);

		Intent intent = getIntent();
		momentsid = intent.getIntExtra("momentsid", 0);
		replytoid = intent.getIntExtra("replytoid", 0);
		momentsCommentList = (List<MomentsComment>) intent.getSerializableExtra("list");
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.moments_comment_button:
			content = sendEditText.getText().toString();
			if (content == null || content.length() == 0) {
				break;
			}
			
			new SaveCommentTask().execute();
			break;
		}
	}

	// ***************************************
	// Private classes
	// ***************************************
	private class SaveCommentTask extends
			AsyncTask<String, Void, GeneralResponse> {

		private Exception exception;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected GeneralResponse doInBackground(String... params) {
			// TODO Auto-generated method stub

			try {
				GeneralResponse generalResponse = SpringAndroidService
						.getInstance(
								MomentsCommentEditActivity.this
										.getApplication())
						.insertMomentscomment(momentsid, content, replytoid);
				return generalResponse;
			} catch (Exception e) {
				this.exception = e;
			}

			return null;
		}

		@Override
		protected void onPostExecute(GeneralResponse generalResponse) {
			// TODO Auto-generated method stub
			if (exception != null) {
				Toast.makeText(MomentsCommentEditActivity.this, "评论失败",
						Toast.LENGTH_SHORT).show();
			} else {
				if (generalResponse.getReturncode() != 200) {
					Toast.makeText(MomentsCommentEditActivity.this, "评论失败",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(MomentsCommentEditActivity.this, "评论成功",
							Toast.LENGTH_SHORT).show();
					MomentsComment momentsComment = new MomentsComment();
					momentsComment.setComment(content);
					momentsComment.setMomentsid(momentsid);
					momentsComment.setPosterid(replytoid);
					momentsCommentList.add(momentsComment);
					 
					setResult(RESULT_OK);
					MomentsCommentEditActivity.this.finish();
				}
			}
		}
	}
}
