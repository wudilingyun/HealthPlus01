package com.vee.imandforum;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.vee.imandforum.authorization.SignInActivity;
import com.vee.imandforum.main.FragmentTabsPager;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class MainActivity extends Activity {

	// ***************************************
	// Private methods
	// ***************************************
	private boolean isConnected() {
		return SpringAndroidService.getInstance(getApplication()).isConnected();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = null;
		if(isConnected()){
			intent = new Intent(this, FragmentTabsPager.class);
		}
		else{
			intent = new Intent(this, SignInActivity.class);
		}
		
		startActivity(intent);
		finish();
	}
}
