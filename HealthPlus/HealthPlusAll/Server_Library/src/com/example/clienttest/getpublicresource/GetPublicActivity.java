package com.example.clienttest.getpublicresource;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.example.clienttest.AbstractGreenhouseActivity;
import com.example.clienttest.R;

public class GetPublicActivity extends AbstractGreenhouseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_public);
		
		findViewById(R.id.button_public_get).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(final View view) {
						// TODO Auto-generated method stub
						new GetPublicTask().execute();
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.get_public, menu);
		return true;
	}

	private class GetPublicTask extends AsyncTask<Void, Void, Void> {
		private MultiValueMap<String, String> formData;
		private Exception exception;
		private String publictext;
		
		@Override
		protected void onPreExecute() {
			formData = new LinkedMultiValueMap<String, String>();
			EditText editPublicText = (EditText) findViewById(R.id.editpublicget);
			publictext = editPublicText.getText().toString();
			this.exception = null;
		}

		@Override
		@SuppressWarnings("unchecked")
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			try{
				final String url = getApplicationContext().getApiUrlBase() + "public/get";
				HttpHeaders requestHeaders = new HttpHeaders();
				requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
				HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(formData, requestHeaders);
				RestTemplate restTemplate = new RestTemplate(true);
				Map<String, Object> responseBody = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Map.class).getBody();
				Log.d(TAG, responseBody.toString());
				
				publictext = (String) responseBody.get("s");
				
			} catch (Exception e) {
				Log.e(TAG, e.getLocalizedMessage(), e);
				this.exception = e;
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void v){			
			if( this.exception != null )
			{
				processException(exception);
			}
			else
			{
				EditText editPublicText = (EditText) findViewById(R.id.editpublicget);
				editPublicText.setText(publictext);
			}
		}
	}
}
