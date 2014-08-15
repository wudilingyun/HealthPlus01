package com.vee.healthplus.util.user;

import android.content.Context;
import android.text.format.Time;
import android.util.Log;

/**
 * Created by lingyun
 */
public class UserInfoUtil {

	private static HP_User currentUser;
	private Context mContext;

	public UserInfoUtil(Context c) {
		mContext = c;
		currentUser = HP_DBModel.getInstance(mContext).queryUserInfoByUserId(
				HP_User.getOnLineUserId(mContext), true);
	}

	public static String getUserNickName(Context context) {
		currentUser = HP_DBModel.getInstance(context).queryUserInfoByUserId(
				HP_User.getOnLineUserId(context), true);
		if (currentUser == null) {
			return "未设置";
		}
		String nickname = currentUser.userNick;
		if (nickname.equals("") || nickname == null) {
			nickname = "未设置";
		}
		return nickname;
	}

	public static int getAgeFromBirthDay(int bir) {
		Time t = new Time();
		t.setToNow();
		if (bir < 10000000) {
			return 0;
		}
		return t.year - (bir / 10000);
	}

	public static int getMonthFromBirthDay(int bir) {
		if (bir < 10000000) {
			return 0;
		}
		String month = (bir + "").substring(4, 6);
		Log.i("lingyun", "month=" + month);
		return Integer.valueOf(month);
	}

}
