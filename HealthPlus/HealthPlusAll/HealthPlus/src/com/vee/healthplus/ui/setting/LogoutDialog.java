package com.vee.healthplus.ui.setting;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.vee.healthplus.R;
import com.vee.healthplus.util.AppPreferencesUtil;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.widget.CustomDialog;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

/**
 * Created by xujizhe on 13-11-29.
 */
@SuppressLint("ValidFragment")
public class LogoutDialog extends DialogFragment {

	public String title;

	public LogoutDialog() {
		title = getActivity().getResources().getString(R.string.update_note);
	}

	public LogoutDialog(String title) {
		this.title = title;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		View layout = View.inflate(getActivity(),
				R.layout.logout_dialog_layout, null);
		CustomDialog.Builder customBuilder = new CustomDialog.Builder(
				getActivity());
		customBuilder
				.setTitle(this.title)
				.setContentView(layout)
				.setPositiveButton("安全退出",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								try {
									SpringAndroidService.getInstance(
											getActivity().getApplication())
											.signOut();
									HP_User.setOnLineUserId(getActivity(), 0);
								} catch (Exception e) {
									e.printStackTrace();
								}
								Toast.makeText(getActivity(), "已退出", 0).show();

							}
						})
				.setNegativeButton(
						getActivity().getResources().getString(R.string.CANCLE),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dismiss();
							}
						});

		return customBuilder.create();
	}

}
