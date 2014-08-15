package com.vee.healthplus.heahth_news_utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import android.content.Context;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View.OnClickListener;

/**
 * @fileName TextUtils.java
 * @package com.immomo.momo.android.util
 * @description 文本工具类
 * @author 任东卫
 * @email 86930007@qq.com
 * @version 1.0
 */
public class TextUtils {

	/**
	 * 根据月日获取星座
	 * 
	 * @param month
	 *            月
	 * @param day
	 *            日
	 * @return
	 */
	public static String getConstellation(int month, int day) {
		if ((month == 1 && day >= 20) || (month == 2 && day <= 18)) {
			return "水瓶座";
		} else if ((month == 2 && day >= 19) || (month == 3 && day <= 20)) {
			return "双鱼座";
		} else if ((month == 3 && day >= 21) || (month == 4 && day <= 19)) {
			return "白羊座";
		} else if ((month == 4 && day >= 20) || (month == 5 && day <= 20)) {
			return "金牛座";
		} else if ((month == 5 && day >= 21) || (month == 6 && day <= 21)) {
			return "双子座";
		} else if ((month == 6 && day >= 22) || (month == 7 && day <= 22)) {
			return "巨蟹座";
		} else if ((month == 7 && day >= 23) || (month == 8 && day <= 22)) {
			return "狮子座";
		} else if ((month == 8 && day >= 23) || (month == 9 && day <= 22)) {
			return "处女座";
		} else if ((month == 9 && day >= 23) || (month == 10 && day <= 23)) {
			return "天秤座";
		} else if ((month == 10 && day >= 24) || (month == 11 && day <= 22)) {
			return "天蝎座";
		} else if ((month == 11 && day >= 23) || (month == 12 && day <= 21)) {
			return "射手座";
		} else if ((((month != 12) || (day < 22)))
				&& (((month != 1) || (day > 19)))) {
			return "魔蝎座";
		}
		return "";
	}

	/**
	 * 根据年月日获取年龄
	 * 
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param day
	 *            日
	 * @return
	 */
	public static int getAge(int year, int month, int day) {
		int age = 0;
		Calendar calendar = Calendar.getInstance();
		if (calendar.get(Calendar.YEAR) == year) {
			if (calendar.get(Calendar.MONTH) == month) {
				if (calendar.get(Calendar.DAY_OF_MONTH) >= day) {
					age = calendar.get(Calendar.YEAR) - year + 1;
				} else {
					age = calendar.get(Calendar.YEAR) - year;
				}
			} else if (calendar.get(Calendar.MONTH) > month) {
				age = calendar.get(Calendar.YEAR) - year + 1;
			} else {
				age = calendar.get(Calendar.YEAR) - year;
			}
		} else {
			age = calendar.get(Calendar.YEAR) - year;
		}
		if (age < 0) {
			return 0;
		}
		return age;
	}

	/**
	 * 获取Assets中的json文本
	 * 
	 * @param context
	 *            上下文
	 * @param name
	 *            文本名称
	 * @return
	 */
	public static String getJson(Context context, String name) {
		if (name != null) {
			String path = "json/" + name;
			InputStream is = null;
			try {
				is = context.getAssets().open(path);
				return readTextFile(is);
			} catch (IOException e) {
				return null;
			} finally {
				try {
					if (is != null) {
						is.close();
						is = null;
					}
				} catch (IOException e) {

				}
			}
		}
		return null;
	}

	/**
	 * 从输入流中获取文本
	 * 
	 * @param inputStream
	 *            文本输入流
	 * @return
	 */
	public static String readTextFile(InputStream inputStream) {
		String readedStr = "";
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			String tmp;
			while ((tmp = br.readLine()) != null) {
				readedStr += tmp;
			}
			br.close();
			inputStream.close();
		} catch (UnsupportedEncodingException e) {
			return null;
		} catch (IOException e) {
			return null;
		}

		return readedStr;
	}

}
