package com.vee.healthplus.util.user;

import android.content.Context;

import com.vee.healthplus.R;

import java.text.DecimalFormat;

/**
 * Created by wangjiafeng on 13-10-24.
 */
public class UserIndexUtils {

	/**
	 * 身体质量指数 BMI公式:BMI=体重(千克)/身高*身高(公尺,厘米/100); 18.5~24为正常范围 体重指数——男性：
	 * 体重指数——女性： 过轻——低于20 过轻——低于19 正常—— 20-25 正常——19-24 过重——25-30 过重——24-29
	 * 肥胖——30-35 肥胖—— 29-34 非常肥胖—— 高于35 非常肥胖——高于34
	 */
	public static String getUserBMI(Context mContext, HP_User user) {
		float bmi = user.userWeight
				/ ((user.userHeight / 100) * (user.userHeight / 100));
		DecimalFormat df = new DecimalFormat("##0.00");
		return df.format(bmi)
				+ mContext.getResources().getString(
						R.string.hp_userinfo_finebmi);
	}

	public static String getResult(Context mContext, HP_User user) {
		double bmi = user.userWeight
				/ (Math.pow((user.userHeight / 100.0), 2) );
		System.out.println("bm1" + bmi + "身高" + user.userHeight + "体重"
				+ user.userWeight);
		int textID = R.string.hp_userinfo_bmi_normallight;
		switch (user.userSex) {
		case -1:
			if (bmi < 20) {
				textID = R.string.hp_userinfo_bmi_morelight;
			} else if (20 <= bmi && bmi < 25) {
				textID = R.string.hp_userinfo_bmi_normallight;
			} else if (25 <= bmi && bmi < 30) {
				textID = R.string.hp_userinfo_bmi_fat;
			} else if (30 <= bmi && bmi < 35) {
				textID = R.string.hp_userinfo_bmi_morefat;
			} else if (bmi >= 35) {
				textID = R.string.hp_userinfo_bmi_morefatest;
			}
			break;
		default:
			if (bmi < 19) {
				textID = R.string.hp_userinfo_bmi_morelight;
			} else if (19 <= bmi && bmi < 24) {
				textID = R.string.hp_userinfo_bmi_normallight;
			} else if (24 <= bmi && bmi < 29) {
				textID = R.string.hp_userinfo_bmi_fat;
			} else if (29 <= bmi && bmi < 34) {
				textID = R.string.hp_userinfo_bmi_morefat;
			} else if (bmi >= 34) {
				textID = R.string.hp_userinfo_bmi_morefatest;
			}
			break;
		}
		return mContext.getResources().getString(textID);
	}

	/**
	 * 基础代谢率 女性:655 + (9.6 x 体重) + (1.7 x 身高) - (4.7X年龄) 男性:66 + (13.7 x 体重) +
	 * (5.0 x 身高) - (6.8x年龄)
	 */
	public static String getUserBMR(Context mContext, HP_User user) {
		float bmr = 0;
		if (user.userSex == 0) {
			bmr = 655F + (9.6F * user.userWeight) + (1.7F * user.userHeight)
					- (4.7F * user.userAge);
		} else {
			bmr = 66F + (13.7F * user.userWeight) + (5.0F * user.userHeight)
					- (6.8F * user.userAge);
		}
		DecimalFormat df = new DecimalFormat("##0.00");
		return df.format(bmr)
				+ mContext.getResources().getString(
						R.string.hp_userinfo_calorie_day);
	}

	/**
	 * 理想体重 理想体重（千克）=身高（厘米）-105
	 */
	public static String getIdealWeight(Context mContext, HP_User user) {
		return String.valueOf(user.userHeight - 105) + "kg";
	}

	/**
	 * 每天需要热量 每天需要热量＝655.096＋9.563×（W）＋1.85×（H）－4.676×（A） W：体重，公斤为单位 H：身高，公分为单位
	 * A：年龄，岁为单位 如需增重：每天需要热量+500-1000卡路里 如需减重：每天需要热量-500-1000卡路里
	 */
	public static String getEveryDayQuality(Context mContext, HP_User user) {
		DecimalFormat df = new DecimalFormat("##0.00");
		return String.valueOf(df.format(655.096 + 9.563 * user.userWeight
				+ 1.85 * user.userHeight - 4.676 * user.userAge))
				+ mContext.getResources().getString(
						R.string.hp_userinfo_calorie_day);
	}

	public static String getAddWeight(Context mContext, HP_User user) {
		DecimalFormat df = new DecimalFormat("##0.00");
		return String.valueOf(df.format(655.096 + 9.563 * user.userWeight
				+ 1.85 * user.userHeight - 4.676 * user.userAge + 500 - 1000))
				+ mContext.getResources().getString(
						R.string.hp_userinfo_calorie_day);
	}

	public static String getSubtrackWeight(Context mContext, HP_User user) {
		DecimalFormat df = new DecimalFormat("##0.00");
		return String.valueOf(df.format(655.096 + 9.563 * user.userWeight
				+ 1.85 * user.userHeight - 4.676 * user.userAge - 500 - 1000))
				+ mContext.getResources().getString(
						R.string.hp_userinfo_calorie_day);
	}
}
