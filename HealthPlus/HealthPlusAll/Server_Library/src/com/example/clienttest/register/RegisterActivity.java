package com.example.clienttest.register;

import java.util.Map;

import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.example.clienttest.AbstractGreenhouseActivity;
import com.example.clienttest.R;

public class RegisterActivity extends AbstractGreenhouseActivity {
	private static final String TAG = RegisterActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		findViewById(R.id.button_register_cancel).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(final View view) {
						finish();
					}
				});

		findViewById(R.id.button_register_register).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(final View view) {
						new RegisterTask().execute();
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

	private void displayRegisterError(String message) {
		new AlertDialog.Builder(this).setMessage(message).setCancelable(false)
				.setPositiveButton("OK", null).create().show();
	}

	// ***************************************
	// Private classes
	// ***************************************
	@SuppressWarnings("unused")
	private class RegisterTask extends AsyncTask<Void, Void, Void> {
		private MultiValueMap<String, String> formData;
		private Exception exception;
		private RegisterResponse registerResponse;

		@Override
		protected void onPreExecute() {
			formData = new LinkedMultiValueMap<String, String>();

			EditText editText = (EditText) findViewById(R.id.register_username);
			String username = editText.getText().toString().trim();
			formData.add("username", username);

			editText = (EditText) findViewById(R.id.register_password);
			String password = editText.getText().toString().trim();
			formData.add("password", password);
		}

		@Override
		@SuppressWarnings("unchecked")
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				final String url = getApplicationContext().getApiUrlBase()
						+ "android/register";

				Log.d(TAG, url);
				HttpHeaders requestHeaders = new HttpHeaders();
				requestHeaders
						.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
				HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(
						formData, requestHeaders);
				RestTemplate restTemplate = new RestTemplate(true);
				if (restTemplate.getRequestFactory() instanceof SimpleClientHttpRequestFactory) {
					((SimpleClientHttpRequestFactory) restTemplate
							.getRequestFactory()).setConnectTimeout(10 * 1000);
					((SimpleClientHttpRequestFactory) restTemplate
							.getRequestFactory()).setReadTimeout(10 * 1000);
				} else if (restTemplate.getRequestFactory() instanceof HttpComponentsClientHttpRequestFactory) {
					Log.d("HTTP", "HttpClient is used");
					((HttpComponentsClientHttpRequestFactory) restTemplate
							.getRequestFactory()).setReadTimeout(10 * 1000);
					((HttpComponentsClientHttpRequestFactory) restTemplate
							.getRequestFactory()).setConnectTimeout(10 * 1000);
				}

				Map<String, Object> responseBody = restTemplate.exchange(url,
						HttpMethod.POST, requestEntity, Map.class).getBody();
				Log.d(TAG, responseBody.toString());

				registerResponse = extractRegisterResponse(responseBody);

			} catch (Exception e) {
				Log.e(TAG, e.getLocalizedMessage(), e);
				this.exception = e;
				if (e.getCause() instanceof ConnectTimeoutException) {
					System.out.println("ConnectionTimeoutException");
				}
				if (e instanceof ResourceAccessException) {
					System.out.println("ResourceAccessException");
				}
				registerResponse = null;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void v) {
			if (exception != null) {
				String message;
				if (exception instanceof HttpClientErrorException) {
					System.out.println("HttpClientErrorException");
				}
				if (exception instanceof HttpClientErrorException
						&& ((((HttpClientErrorException) exception)
								.getStatusCode() == HttpStatus.BAD_REQUEST) || ((HttpClientErrorException) exception)
								.getStatusCode() == HttpStatus.UNAUTHORIZED)) {
					message = "Your email or password was entered incorrectly.";
				} else if (exception instanceof DuplicateConnectionException) {
					message = "The connection already exists.";
				} else if (exception instanceof ResourceAccessException
						&& exception.getCause() instanceof ConnectTimeoutException) {
					message = "connect time out";
				} else {
					Log.e(TAG, exception.getLocalizedMessage(), exception);
					message = "A problem occurred with the network connection. Please try again in a few minutes.";
				}
				displayRegisterError(message);
			}

			if (registerResponse != null) {
				switch (registerResponse.getReturncode()) {
				case 8:
					// 娉ㄥ唽鎴愬姛
					displayRegisterError(""
							+ registerResponse.getMemberid());
					break;
				case 102:
					// 娉ㄥ唽甯愬彿闀垮害闈炴硶
					displayRegisterError(registerResponse.getDescription());
					break;
				case 103:
					// 閫氫俊瀵嗛挜涓嶆纭�
					displayRegisterError(registerResponse.getDescription());
					break;
				case 104:
					// 娉ㄥ唽甯愬彿闈炴硶锛屾敞鍐屽笎鍙峰繀椤绘槸鏁板瓧鍜屽瓧姣嶇粍鍚堬紝涓嶈兘鍖呭惈闈炴硶瀛楃,@闄ゅ
					displayRegisterError(registerResponse.getDescription());
					break;
				case 1:
					// 鐢ㄦ埛鍚嶆垨閭瀛樺湪锛屾棤娉曟敞鍐�
					displayRegisterError(registerResponse.getDescription());
					break;
				case 5:
					// 鐢ㄦ埛鍩烘湰淇℃伅鍐欏叆澶辫触
					displayRegisterError(registerResponse.getDescription());
					break;
				case 7:
					// 鐢ㄦ埛鎵╁睍淇℃伅鍐欏叆澶辫触
					displayRegisterError(registerResponse.getDescription());
					break;
				default:
					// 鏈嶅姟鍣ㄥ唴閮ㄩ敊璇�
					displayRegisterError(registerResponse.getDescription());
					break;
				}
			}
		}

		private RegisterResponse extractRegisterResponse(
				Map<String, Object> result) {
			return new RegisterResponse((String) result.get("description"),
					getIntegerValue(result, "returncode"), getLongValue(result,
							"memberid"));
		}

		// Retrieves object from map into an Integer, regardless of the object's
		// actual type. Allows for flexibility in object type (eg, "3600" vs
		// 3600).
		private Integer getIntegerValue(Map<String, Object> map, String key) {
			try {
				return Integer.valueOf(String.valueOf(map.get(key))); // normalize
																		// to
																		// String
																		// before
																		// creating
																		// integer
																		// value;
			} catch (NumberFormatException e) {
				return null;
			}
		}

		// Retrieves object from map into an Long, regardless of the object's
		// actual type. Allows for flexibility in object type (eg, "3600" vs
		// 3600).
		private Long getLongValue(Map<String, Object> map, String key) {
			try {
				return Long.valueOf(String.valueOf(map.get(key))); // normalize
																	// to String
																	// before
																	// creating
																	// integer
																	// value;
			} catch (NumberFormatException e) {
				return null;
			}
		}

		private class RegisterResponse {
			private String description;
			private int returncode;
			private Long memberid;

			public RegisterResponse(String sDescription, int iReturncode,
					Long lMemberid) {
				this.description = sDescription;
				this.returncode = iReturncode;
				this.memberid = lMemberid;
			}

			public String getDescription() {
				return description;
			}

			public void setDescription(String description) {
				this.description = description;
			}

			public int getReturncode() {
				return returncode;
			}

			public void setReturncode(int returncode) {
				this.returncode = returncode;
			}

			public Long getMemberid() {
				return memberid;
			}

			public void setMemberid(Long memberid) {
				this.memberid = memberid;
			}
		}
	}
}
