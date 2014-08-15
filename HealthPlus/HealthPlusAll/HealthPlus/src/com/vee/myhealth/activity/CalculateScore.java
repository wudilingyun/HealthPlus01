package com.vee.myhealth.activity;

import java.util.HashMap;
import java.util.Iterator;

import android.R.integer;

import com.baidu.platform.comapi.map.t;
import com.vee.myhealth.bean.TZtest;

public class CalculateScore {
	// 计算得分
	private static String PINGHE_ID = "10";
	private static String QIXU_ID = "11";
	private static String YANGXU_ID = "12";
	private static String YINXU_ID = "13";
	private static String TANSHI_ID = "14";
	private static String SHIRE_ID = "15";
	private static String XUEYU_ID = "16";
	private static String QIYU_ID = "17";
	private static String TEBING_ID = "18";
	int sumA, sumA1, SumA2, sumB, sumC, sumD, sumE, sumF, sumG, sumH, sumI = 0;
	int countA, countB, countC, countD, countE, countF, countG, countH,
			countI = 0;

	public String getScore(HashMap<TZtest, Integer> scoremMap) {
		Iterator<TZtest> i = scoremMap.keySet().iterator();
		while (i.hasNext()) {
			TZtest tZtest = (TZtest) i.next();
			System.out.println("对应的h_id" + tZtest.getH_id());
			if (tZtest.getH_id().equals(PINGHE_ID)) {
				countA = countA + 1;
				System.out.println("数量" + countA + "分数是"
						+ scoremMap.get(tZtest));
				if (tZtest.getAstersk() == "0") {
					sumA1 = (int) (sumA1 + scoremMap.get(tZtest));
				} else {
					SumA2 = SumA2 + reverseScore(scoremMap.get(tZtest));
				}
				sumA = sumA1 + SumA2;
			} else if (tZtest.getH_id().equals(QIXU_ID)) {
				countB = countB + 1;
				sumB = (int) (sumB + scoremMap.get(tZtest));

			} else if (tZtest.getH_id().equals(YANGXU_ID)) {
				countC = countC + 1;
				sumC = (int) (sumC + scoremMap.get(tZtest));
			} else if (tZtest.getH_id().equals(YINXU_ID)) {
				countD = countD + 1;
				sumD = (int) (sumD + scoremMap.get(tZtest));
			} else if (tZtest.getH_id().equals(TANSHI_ID)) {
				countE = countE + 1;
				sumE = (int) (sumE + scoremMap.get(tZtest));
			} else if (tZtest.getH_id().equals(SHIRE_ID)) {
				countF = countF + 1;
				sumF = (int) (sumF + scoremMap.get(tZtest));
			} else if (tZtest.getH_id().equals(XUEYU_ID)) {
				countG = countG + 1;
				sumG = (int) (sumG + scoremMap.get(tZtest));
			} else if (tZtest.getH_id().equals(QIYU_ID)) {
				countH = countH + 1;
				sumH = (int) (sumH + scoremMap.get(tZtest));
			} else if (tZtest.getH_id().equals(TEBING_ID)) {
				countI = countI + 1;
				sumI = (int) (sumI + scoremMap.get(tZtest));
			}

			if (tZtest.getRepeat().equals(QIXU_ID)) {
				countB = countB + 1;
				sumB = (int) (sumB + scoremMap.get(tZtest));
			} else if (tZtest.getRepeat().equals(YANGXU_ID)) {
				countC = countC + 1;
				sumC = (int) (sumC + scoremMap.get(tZtest));
			} else if (tZtest.getRepeat().equals(XUEYU_ID)) {
				countH = countH + 1;
				sumH = (int) (sumH + scoremMap.get(tZtest));
			}

		}
		String s = convertScore();
		return s;
	}

	// 逆向记录分数
	int reverseScore(int score) {
		int scorecc = (int) (score + 0);
		if (scorecc == 1) {
			scorecc = 5;
		} else if (scorecc == 2) {
			scorecc = 4;
		} else if (scorecc == 3) {
			scorecc = 3;
		} else if (scorecc == 4) {
			scorecc = 2;
		} else if (scorecc == 5) {
			scorecc = 1;
		}
		return scorecc;
	}

	/**
	 * 转换分数
	 */
	String convertScore() {
		String result = "";
		int s1 = ((sumA - countA) * 100 / (countA * 4));
		int s2 = ((sumB - countB) * 100 / (countB * 4));
		int s3 = ((sumC - countC) * 100 / (countC * 4));
		int s4 = ((sumD - countD) * 100 / (countD * 4));
		int s5 = ((sumE - countE) * 100 / (countE * 4));
		int s6 = ((sumF - countF) * 100 / (countF * 4));
		int s7 = ((sumG - countG) * 100 / (countG * 4));
		int s8 = ((sumH - countH) * 100 / (countH * 4));
		int s9 = ((sumI - countI) * 100 / (countI * 4));
		System.out.println("s1" + s1 + "s2" + s2 + "s3" + s3 + "s4" + s4 + "s5"
				+ s5 + "s6" + s6 + "s7" + s7 + "s8" + s8 + "s9" + s9);
		System.out.println(sumA + "b=" + sumB + "C=" + sumC + "D=" + sumD + "E"
				+ sumE + "F=" + sumF + "G=" + sumG + "H=" + sumH + "I=" + sumI);
		if (s1 >= 60 && s2 < 40 && s3 < 40 && s4 < 40 && s5 < 40 && s6 < 40
				&& s7 < 40 && s8 < 40 && s9 < 40) {
			result = PINGHE_ID;
		} else if (s1 <= 60 && s2 >= 30) {
			result = QIXU_ID;
		} else if (s1 <= 60 && s3 >= 30) {
			result = YANGXU_ID;
		} else if (s1 <= 60 && s4 >= 30) {
			result = YINXU_ID;
		} else if (s1 <= 60 && s5 >= 30) {
			result = TANSHI_ID;
		} else if (s1 <= 60 && s6 >= 30) {
			result = SHIRE_ID;
		} else if (s1 <= 60 && s7 >= 30) {
			result = XUEYU_ID;
		} else if (s1 <= 60 && s8 >= 30) {
			result = QIYU_ID;
		} else if (s1 <= 60 && s9 >= 30) {
			result = TEBING_ID;
		} else {
			result = PINGHE_ID;
		}
		return result;
	}
}
