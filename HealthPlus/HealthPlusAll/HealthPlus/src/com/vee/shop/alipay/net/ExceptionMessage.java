package com.vee.shop.alipay.net;

import android.content.Context;

public class ExceptionMessage {
	public static String getExceptionMsg(Context context, int id) {
		return context.getResources().getString(id);
	}
}
