package com.vee.shop.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.vee.shop.util.Constants;

public abstract class BaseFragment extends Fragment {
	// wait for complete
	// eg. network change
	public Context mContext;
	public static SharedPreferences settings;
	public static SharedPreferences.Editor editor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mContext = getActivity();
		if (settings == null) {
			settings = mContext.getSharedPreferences(
					Constants.ALLSYSTEMSETTING_PREFERENCES,
					Context.MODE_PRIVATE);
		}
		editor = settings.edit();
	}

	public void refreshData() {

	}
}