package com.vee.healthplus.ui.heahth_exam;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressDiaogdownload {
	ProgressDialog progressDialog;
	Context context;

	public ProgressDiaogdownload(Context context) {
		super();
		this.context = context;
	}

	public void showProgressDialog(String message) {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(context);
			progressDialog.setIndeterminate(true);
			progressDialog.setCanceledOnTouchOutside(false);
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
