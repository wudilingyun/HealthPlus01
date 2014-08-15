package com.vee.healthplus.ui.main;

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
import android.widget.TextView;

import com.vee.healthplus.R;
import com.vee.healthplus.util.AppPreferencesUtil;
import com.vee.healthplus.widget.CustomDialog;

/**
 * Created by xujizhe on 13-11-29.
 */
@SuppressLint("ValidFragment")
public class QuitDialog extends DialogFragment {

	public String title;
	private Boolean flag = false;
	public QuitDialog() {
		title = getActivity().getResources().getString(R.string.update_note);
	}
	public QuitDialog(Boolean flag,String title) {
		this.flag = flag;
		this.title = title;
	}

	public QuitDialog(String title) {
		this.title = title;
		flag = false;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		View layout  = View.inflate(getActivity(), R.layout.quit_warn, null);
		TextView quit_tView = (TextView)layout.findViewById(R.id.quit_tv);
		if(flag){
			quit_tView.setText("真的要退出当前测试吗？");
			 flag = false;
		}else {
			quit_tView.setText("真的要退出吗？");
		}
		CustomDialog.Builder customBuilder = new CustomDialog.Builder(
				getActivity());
		customBuilder
				.setTitle(this.title)
				.setContentView(layout)
				.setPositiveButton(
						getActivity().getResources().getString(R.string.quit),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
						
								getActivity().finish();
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
