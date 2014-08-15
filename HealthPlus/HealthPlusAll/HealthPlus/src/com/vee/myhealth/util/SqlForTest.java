package com.vee.myhealth.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vee.healthplus.util.user.HP_DBModel;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.util.user.ICallBack;
import com.vee.myhealth.bean.Answer;
import com.vee.myhealth.bean.HealthQuestionEntity;
import com.vee.myhealth.bean.HealthResultEntity;
import com.vee.myhealth.bean.Health_Report;
import com.vee.myhealth.bean.TZtest;

public class SqlForTest<T> {
	private SQLiteDatabase database;
	private SqlDataCallBack<T> iCallBack;
	private TZtest test;
	//private Health_Report health_Report;
	private Answer answer;
	private List<T> testList;
	private Context context;
	private static SqlForTest instance = null;
	private HealthQuestionEntity healthQuestionEntity;
	private HealthResultEntity healthResultEntity;

	public SqlForTest(SqlDataCallBack<T> callBack) {
		iCallBack = callBack;
		database = SQLiteDatabase.openOrCreateDatabase(DBManager.DB_PATH + "/"
				+ DBManager.DB_NAME, null);
		answer = new Answer();
		//health_Report = new Health_Report();
		healthResultEntity = new HealthResultEntity();
		
		testList = new ArrayList<T>();
		// TODO Auto-generated constructor stub
	}

	public void getSymptomFromDB(String sex) {
		getList(sex);
		database.close();
	}

	public void getResultFromDB(String result) {
		getResultContent(result);
		database.close();
	}

	private void getList(String sex) {

		try {
			// 查询测试题
			String sql = "select * from " + DBManager.HABITUS_TEST_TABLE;
			// + " where sex='0' or sex=" + sex;
			System.out.println("sql" + sql);
			Cursor cursor = database.rawQuery(sql, null);

			if (cursor.getCount() < 1) {
				cursor.close();
			} else {
				while (cursor.moveToNext()) {
					String num = cursor.getString(0);
					String h_id = cursor.getString(1);
					String name = cursor.getString(2);
					String astersk = cursor.getString(3);
					String repeat = cursor.getString(4);
					String repeat_astersk = cursor.getString(5);
					test = new TZtest();
					test.setNum(num);
					test.setH_id(h_id);
					test.setQuestion(name);
					test.setAstersk(astersk);
					test.setRepeat(repeat);
					test.setRepeat_astersk(repeat_astersk);
					testList.add((T) test);
					Log.v("查询到数据", name);
				}

				if (iCallBack != null) {
					iCallBack.getData((List<T>) testList);

				}
				database.close();
				cursor.close();
			}

		} catch (Exception e) {

		}

	}

	private void getResultContent(String result) {

		try {
			String sql = "select * from " + DBManager.HABITUS_RESULT_TABLE
					+ " where h_id='" + result + "'";
			System.out.println("sql" + sql);
			Cursor cursor = database.rawQuery(sql, null);

			if (cursor.getCount() < 1) {
				cursor.close();
			} else {
				while (cursor.moveToNext()) {
					String name = cursor.getString(2);
					String feature = cursor.getString(3);
					String img_id = cursor.getString(4);
					
					/*health_Report.setName(name);
					health_Report.setFeature(feature);*/
					healthResultEntity.setType(name);
					healthResultEntity.setResult(feature);
					healthResultEntity.setImage_id(img_id);
					Log.v("查询到数据", name);
					if (iCallBack != null) {
						iCallBack.getResult(healthResultEntity);
					}

				}
				database.close();
				cursor.close();
			}

		} catch (Exception e) {

		}

	}

	/**
	 * 查询健康
	 * 
	 * @param h_id
	 *            测试题的编号
	 */
	public void getHealthContent(String h_id) {

		try {
			String sql = "select * from " + DBManager.HEALTH_QUESTIONS
					+ " where h_id=" + h_id ;
			System.out.println("sql" + sql);
			Cursor cursor = database.rawQuery(sql, null);

			if (cursor.getCount() < 1) {
				cursor.close();
			} else {
				while (cursor.moveToNext()) {
					int num = cursor.getInt(1);
					String question = cursor.getString(3);
					healthQuestionEntity = new HealthQuestionEntity();
					healthQuestionEntity.setId(num);
					healthQuestionEntity.setQuestion(question);
					testList.add((T) healthQuestionEntity);

				}
				if (iCallBack != null) {
					iCallBack.getData(testList);
				}

				cursor.close();
				database.close();
			}

		} catch (Exception e) {

		}

	}

	// 获得健康测试的结果
	/**
	 * 
	 * @param name_id
	 *            测试题编号
	 * @param score
	 *            测试得分
	 */
	public void getHealthResult(String name_id, int score) {

		try {
			String sql = "select * from " + DBManager.HEALTH_RESULT
					+ " where maxscore>=" + score + " and minscore<=" + score
					+ " and name_id=" + name_id;
			System.out.println("sql" + sql);
			Cursor cursor = database.rawQuery(sql, null);

			if (cursor.getCount() < 1) {
				cursor.close();
			} else {
				while (cursor.moveToNext()) {
					int num = cursor.getInt(1);
					String type = cursor.getString(5);
					String result = cursor.getString(6);
					String tips = cursor.getString(7);
					String image_id = cursor.getString(8);

					healthResultEntity.setType(type);
					healthResultEntity.setResult(result);
					healthResultEntity.setTips(tips);
					healthResultEntity.setImage_id(image_id);
				}

				if (iCallBack != null) {
					iCallBack.getResult(healthResultEntity);
				}

				cursor.close();
				database.close();

			}

		} catch (Exception e) {

		}

	}

	public void getWeightLossResult(String name_id, String type) {

		try {
			String sql = "select * from " + DBManager.HEALTH_RESULT
					+ " where type='" + type + "'";
			System.out.println("sql" + sql);
			Cursor cursor = database.rawQuery(sql, null);

			if (cursor.getCount() < 1) {
				cursor.close();
			} else {
				if (cursor.moveToFirst()) {
					int num = cursor.getInt(1);
					String types = cursor.getString(5);
					String result = cursor.getString(6);
					healthResultEntity.setType(type);
					healthResultEntity.setResult(result);
				}

				if (iCallBack != null) {
					iCallBack.getResult(healthResultEntity);
				}

				cursor.close();
				database.close();

			}

		} catch (Exception e) {

		}

	}

	/**
	 * 测试建议
	 * 
	 * @param name_id
	 *            测试题编号
	 */
	public void getHealthSuggest(String id) {

		try {
			String sql = "select * from " + DBManager.HEALTH_SUGGEST
					+ " where h_id='" + id + "'";
			System.out.println("sql" + sql);
			Cursor cursor = database.rawQuery(sql, null);

			if (cursor.getCount() < 1) {
				cursor.close();
			} else {
				if (cursor.moveToFirst()) {
					int num = cursor.getInt(1);
					String eat = cursor.getString(3);
					String sport = cursor.getString(4);
					String prevent = cursor.getString(5);
					healthResultEntity.setEat(eat);
					healthResultEntity.setSport(sport);
					healthResultEntity.setPrevent(prevent);
				}
				if (iCallBack != null) {
					iCallBack.getResult(healthResultEntity);
					System.out.println("准备回调");
				}

				cursor.close();
				database.close();
			}

		} catch (Exception e) {

		}

	}
}
