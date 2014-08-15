package com.vee.imandforum.authorization;

import org.springframework.http.HttpStatus;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.vee.imandforum.R;
import com.vee.imandforum.main.FragmentTabsPager;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class SignInActivity extends Activity {
	protected static final String TAG = SignInActivity.class.getSimpleName();
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in);

		findViewById(R.id.button_signin_cancel).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(final View view) {
						finish();
					}
				});

		findViewById(R.id.button_signin_signin).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(final View view) {
						if (validateFormData()) {
							new SignInTask().execute();
						} else {
							displayAppAuthorizationError("Your email or password was entered incorrectly.");
						}
					}
				});
	}

	// ***************************************
	// Private methods
	// ***************************************
	private boolean validateFormData() {
		EditText editText = (EditText) findViewById(R.id.signin_email);
		String email = editText.getText().toString().trim();
		editText = (EditText) findViewById(R.id.signin_password);
		String password = editText.getText().toString().trim();
		if (email.length() > 0 && password.length() > 0) {
			return true;
		}
		return false;
	}

	private void displayAppAuthorizationError(String message) {
		new AlertDialog.Builder(this).setMessage(message).setCancelable(false)
				.setPositiveButton("OK", null).create().show();
	}

	// ***************************************
	// Private classes
	// ***************************************
	private class SignInTask extends AsyncTask<Void, Void, Void> {
		private MultiValueMap<String, String> formData;

		private Exception exception;
		private String email;
		private String password;

		@Override
		protected void onPreExecute() {
			showProgressDialog("Signing in...");

			EditText editText = (EditText) findViewById(R.id.signin_email);
			email = editText.getText().toString().trim();

			editText = (EditText) findViewById(R.id.signin_password);
			password = editText.getText().toString().trim();
		}

		@Override
		@SuppressWarnings("unchecked")
		protected Void doInBackground(Void... params) {
			try {
				SpringAndroidService.getInstance(getApplication()).signin(
						email, password);
			} catch (Exception e) {
				Log.e(TAG, e.getLocalizedMessage(), e);
				this.exception = e;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void v) {
			dismissProgressDialog();
			if (exception != null) {
				String message;
				if (exception instanceof HttpClientErrorException
						&& ((((HttpClientErrorException) exception)
								.getStatusCode() == HttpStatus.BAD_REQUEST) || ((HttpClientErrorException) exception)
								.getStatusCode() == HttpStatus.UNAUTHORIZED)) {
					message = "Auth failure,Your email or password was entered incorrectly.";
				} else if (exception instanceof DuplicateConnectionException) {
					message = "The connection already exists.";
				} else {
					Log.e(TAG, exception.getLocalizedMessage(), exception);
					message = "A problem occurred with the network connection. Please try again in a few minutes.";
				}
				displayAppAuthorizationError(message);
			} else {
				displayGreenhouseOptions();
			}
		}
	}

	private void displayGreenhouseOptions() {
		Intent intent = new Intent();
		intent.setClass(this, FragmentTabsPager.class);
		startActivity(intent);
		setResult(RESULT_OK);
		finish();
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
