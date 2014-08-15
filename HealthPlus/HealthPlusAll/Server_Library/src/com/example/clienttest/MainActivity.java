package com.example.clienttest;

import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.greenhouse.api.Greenhouse;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.clienttest.authorization.SignInActivity;
import com.example.clienttest.profile.ProfileActivity;
import com.example.clienttest.register.RegisterActivity;

public class MainActivity extends AbstractGreenhouseActivity {
	
	private ConnectionRepository connectionRepository;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		connectionRepository = getApplicationContext().getConnectionRepository();

		Intent intent;
		if (isConnected()) {
			intent = new Intent(this, ProfileActivity.class);
			startActivity(intent);
			finish();
		} else {
		
		setContentView(R.layout.activity_main);

		findViewById(R.id.button_register).setOnClickListener(
				new OnClickListener() {
					public void onClick(final View view) {
						startActivity(new Intent(MainActivity.this,
								RegisterActivity.class));
					}
				});

		findViewById(R.id.button_signin).setOnClickListener(
				new OnClickListener() {
					public void onClick(final View view) {
						startActivity(new Intent(MainActivity.this,
								SignInActivity.class));
					}
				});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// ***************************************
	// Private methods
	// ***************************************
	private boolean isConnected() {
		return connectionRepository.findPrimaryConnection(Greenhouse.class) != null;
	}
}
