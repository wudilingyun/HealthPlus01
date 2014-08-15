package com.example.clienttest.getprotectresource;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.social.MissingAuthorizationException;
import org.springframework.social.greenhouse.api.Greenhouse;
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

public class GetProtectActivity extends AbstractGreenhouseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_protect);
		
		findViewById(R.id.button_protect_get).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(final View view) {
						// TODO Auto-generated method stub
						new GetProtectTask().execute();
					}
				});
		
		findViewById(R.id.buttonprotectsignout).setOnClickListener(
				new OnClickListener() {
					
					@Override
					public void onClick(View view) {
						// TODO Auto-generated method stub
						signOut();
					}
				}
				);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.get_protect, menu);
		return true;
	}

	private class GetProtectTask extends AsyncTask<Void, Void, String> {
		private MultiValueMap<String, String> formData;
		private Exception exception;
		private String protecttext;
		
		@Override
		protected void onPreExecute() {
			formData = new LinkedMultiValueMap<String, String>();
			EditText editProtectText = (EditText) findViewById(R.id.editprotectget);
			protecttext = editProtectText.getText().toString();
			this.exception = null;
		}

		@Override
		protected String doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			try{
				checkConnection();
				
				return getApplicationContext().getPrimaryConnection().getApi().userOperations().getProtect();
				
			} catch (Exception e) {
				Log.e(TAG, e.getLocalizedMessage(), e);
				this.exception = e;
			}
			return null;
		}
		
		protected void checkConnection()
		{
			if(getApplicationContext().getConnectionRepository().findPrimaryConnection(Greenhouse.class) == null)
			{
				System.out.println("no primary connection ............");
				throw new MissingAuthorizationException(); 
			}
		}
		
		@Override
		protected void onPostExecute(String result){			
			if( this.exception != null )
			{
				processException(exception);
				
			}
			else
			{
				EditText editProtectText = (EditText) findViewById(R.id.editprotectget);
				editProtectText.setText(result);
			}
		}
	}
}
