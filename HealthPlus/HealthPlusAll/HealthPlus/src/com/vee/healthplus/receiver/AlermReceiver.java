package com.vee.healthplus.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.vee.healthplus.common.Common;
import com.vee.healthplus.util.InstallSataUtil;

public class AlermReceiver extends BroadcastReceiver {

	private final static String TAG = "AlermReceiver";

	private Context mContext = null;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		mContext = context;
		new InstallSataUtil(mContext).timerServerStat(Common.getAppId(context));
	}

}
