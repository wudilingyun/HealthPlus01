package com.vee.healthplus.util;

import android.content.Context;

import com.vee.healthplus.widget.CustomProgressDialog;


public class LoadingDialogUtil {

	private CustomProgressDialog mLoadingDialog = null;
	private Context context;

	public LoadingDialogUtil(Context context) {
		this.context = context;
	}

	public void show(int stringId) {

		try {
			mLoadingDialog = CustomProgressDialog.createDialog(context);
			mLoadingDialog.setMessage(context.getResources().getString(stringId));
			mLoadingDialog.setCancelable(true);
			mLoadingDialog.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void show(String string) {

		try {
			mLoadingDialog = CustomProgressDialog.createDialog(context);
			mLoadingDialog.setMessage(string);
			mLoadingDialog.setCancelable(true);
			mLoadingDialog.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void hide() {

		try {
			if (mLoadingDialog != null /* && mLoadingDialog.isShowing() */) {
				mLoadingDialog.dismiss();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
