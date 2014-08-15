package com.vee.healthplus.util.user;

import java.util.ArrayList;
import java.util.List;

import com.vee.healthplus.heahth_news_beans.NewsCollectinfor;
import com.vee.myhealth.bean.JPushBean;
import com.vee.myhealth.bean.NewFriendBean;
import com.vee.myhealth.bean.TestCollectinfor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by wangjiafeng on 13-10-23.
 */
public class HP_DBModel {

	private static HP_DBModel model;
	private static SQLiteDatabase database;

	public static HP_DBModel getInstance(Context mContext) {
		if (model == null) {
			model = new HP_DBModel();
		}
		database = new HP_DBHelper(mContext, HP_DBCommons.DBNAME, null, 4)
				.getWritableDatabase();
		return model;
	}

	public SQLiteDatabase getSQLiteDatabase() {
		return database;
	}

	public void insertUserInfo(HP_User user, boolean used) {
		if (queryUserInfoByUserId(user.userId, false) != null) {
			updateUserInfo(user, false);
		} else {
			String insertUserInfo = "INSERT INTO "
					+ HP_DBCommons.USERINFO_TABLENAME + " ("
					+ HP_DBCommons.USERID + "," + HP_DBCommons.USERNAME + ","
					+ HP_DBCommons.USERNICK + "," + HP_DBCommons.EMAIL + ","
					+ HP_DBCommons.PHONE + "," + HP_DBCommons.REMARK + ","
					+ HP_DBCommons.USERAGE + "," + HP_DBCommons.USERHEIGHT
					+ "," + HP_DBCommons.USERWEIGHT + ","
					+ HP_DBCommons.USERSEX + "," + HP_DBCommons.UPDATETIME
					+ "," + HP_DBCommons.PHOTO + ") " + "VALUES ('"
					+ user.userId + "','" + user.userName + "','"
					+ user.userNick + "','" + user.email + "','" + user.phone
					+ "','" + user.remark + "'," + user.userAge + ","
					+ user.userHeight + "," + user.userWeight + ","
					+ user.userSex + "," + user.updateTime + ",'"
					+ user.photourl + "')";
			database.execSQL(insertUserInfo);
		}
		if (used)
			database.close();
	}

	public void updateUserInfo(HP_User user, boolean used) {
		ContentValues values = new ContentValues();
		if (user.userAge > 0)
			values.put(HP_DBCommons.USERAGE, user.userAge);
		if (user.userHeight > 0)
			values.put(HP_DBCommons.USERHEIGHT, user.userHeight);
		if (user.userWeight > 0)
			values.put(HP_DBCommons.USERWEIGHT, user.userWeight);
		if (user.userSex > -2)
			values.put(HP_DBCommons.USERSEX, user.userSex);
		values.put(HP_DBCommons.UPDATETIME, user.updateTime);
		values.put(HP_DBCommons.USERNICK, user.userNick);
		values.put(HP_DBCommons.EMAIL, user.email);
		values.put(HP_DBCommons.PHONE, user.phone);
		values.put(HP_DBCommons.PHOTO, user.photourl);
		database.update(HP_DBCommons.USERINFO_TABLENAME, values,
				HP_DBCommons.USERNAME + "=?", new String[] { user.userName });
		// insertUserWeightInfo(user, false);
		if (used)
			database.close();
	}

	public HP_User queryUserInfoByUserName(String name, boolean used) {
		Cursor cursor = database.rawQuery("SELECT * FROM "
				+ HP_DBCommons.USERINFO_TABLENAME + " WHERE "
				+ HP_DBCommons.USERNAME + " ='" + name + "'", null);
		HP_User user = null;
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			int id = cursor.getInt(cursor.getColumnIndex(HP_DBCommons.USERID));
			int userAge = cursor.getInt(cursor
					.getColumnIndex(HP_DBCommons.USERAGE));
			int userHeight = cursor.getInt(cursor
					.getColumnIndex(HP_DBCommons.USERHEIGHT));
			float userWeight = cursor.getFloat(cursor
					.getColumnIndex(HP_DBCommons.USERWEIGHT));
			int userSex = cursor.getInt(cursor
					.getColumnIndex(HP_DBCommons.USERSEX));
			user = new HP_User();
			user.userId = id;
			user.userName = name;
			user.userAge = userAge;
			user.userHeight = userHeight;
			user.userWeight = userWeight;
			user.userSex = userSex;
			cursor.close();
		}
		if (used)
			database.close();
		return user;
	}

	public HP_User queryUserInfoByUserId(int id, boolean used) {
		Cursor cursor = database.rawQuery("SELECT * FROM "
				+ HP_DBCommons.USERINFO_TABLENAME + " WHERE "
				+ HP_DBCommons.USERID + " = " + id, null);
		HP_User user = null;
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			String name = cursor.getString(cursor
					.getColumnIndex(HP_DBCommons.USERNAME));
			String nick = cursor.getString(cursor
					.getColumnIndex(HP_DBCommons.USERNICK));
			String email = cursor.getString(cursor
					.getColumnIndex(HP_DBCommons.EMAIL));
			String phone = cursor.getString(cursor
					.getColumnIndex(HP_DBCommons.PHONE));
			String remark = cursor.getString(cursor
					.getColumnIndex(HP_DBCommons.REMARK));
			int userAge = cursor.getInt(cursor
					.getColumnIndex(HP_DBCommons.USERAGE));
			int userHeight = cursor.getInt(cursor
					.getColumnIndex(HP_DBCommons.USERHEIGHT));
			float userWeight = cursor.getFloat(cursor
					.getColumnIndex(HP_DBCommons.USERWEIGHT));
			int userSex = cursor.getInt(cursor
					.getColumnIndex(HP_DBCommons.USERSEX));
			String photourl = cursor.getString(cursor
					.getColumnIndex(HP_DBCommons.PHOTO));
			user = new HP_User();
			user.userId = id;
			user.userName = name;
			user.userNick = nick;
			user.email = email;
			user.phone = phone;
			user.remark = remark;
			user.userAge = userAge;
			user.userHeight = userHeight;
			user.userWeight = userWeight;
			user.userSex = userSex;
			user.photourl = photourl;
			cursor.close();
		}
		if (used)
			database.close();
		return user;
	}

	public void insertUserWeightInfo(int userId, float weight, long date,
			boolean used) {
		try {
			String insertUserWeightInfo = "INSERT INTO "
					+ HP_DBCommons.USERWEIGHT_TABLENAME + " ("
					+ HP_DBCommons.USERID + "," + HP_DBCommons.USERWEIGHT + ","
					+ HP_DBCommons.UPDATETIME + ") VALUES (" + userId + ","
					+ weight + "," + date + ")";
			database.execSQL(insertUserWeightInfo);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (used)
				database.close();
		}
	}

	public interface UserWeightCallBack {
		public void queryUserWeightCallBack(float userWeight, long createTime);
	}

	public void queryUserWeightInfoByUserId(int id, boolean used,
			UserWeightCallBack callBack) {
		Cursor cursor = database.rawQuery("SELECT * FROM "
				+ HP_DBCommons.USERWEIGHT_TABLENAME + " WHERE "
				+ HP_DBCommons.USERID + " = " + id + "", null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				float userWeight = cursor.getFloat(cursor
						.getColumnIndex(HP_DBCommons.USERWEIGHT));
				long createTime = cursor.getLong(cursor
						.getColumnIndex(HP_DBCommons.UPDATETIME));
				callBack.queryUserWeightCallBack(userWeight, createTime);
			}
			cursor.close();
		}
		if (used)
			database.close();
	}

	/*
	 * 添加收藏
	 */
	public void insertUserCollect(int userId, String title, String imgurl,
			String weburl) {
		try {
			String insertUserCollect = "INSERT INTO "
					+ HP_DBCommons.USERCOLLECT_TABLENAME + " ("
					+ HP_DBCommons.USERID + "," + HP_DBCommons.TITLE + ","
					+ HP_DBCommons.IMGURL + "," + HP_DBCommons.WEBURL
					+ ") VALUES (" + userId + ",'" + title + "','" + imgurl
					+ "','" + weburl + "')";
			System.out.println("添加收藏=" + insertUserCollect);
			database.execSQL(insertUserCollect);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			database.close();
		}
	}

	/*
	 * 取消收藏
	 */

	public void deletUserCollect(int userId, String title, String imgurl,
			String weburl) {
		try {
			String delUserinfor = "DELETE FROM "
					+ HP_DBCommons.USERCOLLECT_TABLENAME + " WHERE "
					+ HP_DBCommons.USERID + " =" + userId + " and "
					+ HP_DBCommons.TITLE + "='" + title + "' and "
					+ HP_DBCommons.IMGURL + "='" + imgurl + "'";
			System.out.println("删除语句" + delUserinfor);
			database.execSQL(delUserinfor);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			database.close();
		}
	}

	/*
	 * 查看收藏状态
	 */

	public Boolean queryUserBooleanCollectInfor(int userId, String title,
			String imgurl) {
		String sql = "SELECT * FROM " + HP_DBCommons.USERCOLLECT_TABLENAME
				+ " WHERE " + HP_DBCommons.USERID + " =" + userId + " and "
				+ HP_DBCommons.TITLE + "='" + title + "'" + " and "
				+ HP_DBCommons.IMGURL + "='" + imgurl + "'";
		System.out.println("查询状态语句=" + sql);
		Cursor cursor = database.rawQuery(sql, null);

		if (cursor != null && cursor.getCount() > 0) {

			cursor.close();
			database.close();
			System.out.println("已经有了");
			return true;
		} else {
			cursor.close();
			database.close();
			return false;
		}

	}

	/*
	 * 获得收藏列表
	 */

	public List<NewsCollectinfor> queryUserCollectInfor(int userId) {
		List<NewsCollectinfor> newslist = new ArrayList<NewsCollectinfor>();
		String sql = "SELECT * FROM " + HP_DBCommons.USERCOLLECT_TABLENAME
				+ " WHERE " + HP_DBCommons.USERID + " =" + userId;
		System.out.println("获得收藏列表=" + sql);
		Cursor cursor = database.rawQuery(sql, null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {

				String title = cursor.getString(2);
				String imgurl = cursor.getString(3);
				String weburl = cursor.getString(4);
				NewsCollectinfor newsCollectinfor = new NewsCollectinfor();
				newsCollectinfor.setTitle(title);
				newsCollectinfor.setImgurl(imgurl);
				newsCollectinfor.setWeburl(weburl);
				newslist.add(newsCollectinfor);
			}
			cursor.close();
			database.close();
			return newslist;
		} else {
			cursor.close();
			database.close();
			return null;
		}

	}

	/*
	 * 获得测试列表
	 */
	public List<TestCollectinfor> queryUserTestInfor(int userId) {
		List<TestCollectinfor> testlist = new ArrayList<TestCollectinfor>();
		String sql = "SELECT * FROM " + HP_DBCommons.USERTEST_TABLENAME
				+ " WHERE " + HP_DBCommons.USERID + " =" + userId;
		System.out.println("获得收藏列表=" + sql);
		Cursor cursor = database.rawQuery(sql, null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {

				String name = cursor.getString(2);
				String result = cursor.getString(3);
				long time = cursor.getLong(4);
				TestCollectinfor testCollectinfor = new TestCollectinfor();
				testCollectinfor.setName(name);
				testCollectinfor.setResult(result);
				testCollectinfor.setCreattime(time);
				testlist.add(testCollectinfor);
			}
			cursor.close();
			database.close();
			return testlist;
		} else {
			cursor.close();
			database.close();
			return null;
		}

	}

	/*
	 * 添加测试列表
	 */

	public void insertUserTest(int userId, String name, String result, long time) {
		try {
			String insertUserCollect = "INSERT INTO "
					+ HP_DBCommons.USERTEST_TABLENAME + " ("
					+ HP_DBCommons.USERID + "," + HP_DBCommons.TESTNAME + ","
					+ HP_DBCommons.TESTRESULT + "," + HP_DBCommons.TESTTIME
					+ ") VALUES (" + userId + ",'" + name + "','" + result
					+ "','" + time + "')";
			System.out.println("添加测试=" + insertUserCollect);
			database.execSQL(insertUserCollect);
			System.out.println("成功");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			database.close();
		}
	}

	/************************************* 以下数据库用于推送功能添加标签使用 *****************/
	/*
	 * 查询测试题是否存在
	 */

	public Boolean queryTestResultDate(int userId, String name) {

		try {
			String sql = "select * from "
					+ HP_DBCommons.USERTEST_TABLENAME_COVER + " WHERE "
					+ HP_DBCommons.USERID + " =" + userId + " and "
					+ HP_DBCommons.TESTNAME + " ='" + name + "'";
			System.out.println("sql" + sql);
			Cursor cursor = database.rawQuery(sql, null);
			if (cursor.getCount() < 1) {
				cursor.close();
				return false;
			} else {
				cursor.close();
				return true;

			}
		} catch (Exception e) {
			return null;
		}

	}

	/*
	 * 如果不存在插入新的测试结果
	 */
	public void insertUserTestByCover(int userId, String name, String result,
			long time) {
		try {
			String insertUserCollect = "INSERT INTO "
					+ HP_DBCommons.USERTEST_TABLENAME_COVER + " ("
					+ HP_DBCommons.USERID + "," + HP_DBCommons.TESTNAME + ","
					+ HP_DBCommons.TESTRESULT + "," + HP_DBCommons.TESTTIME
					+ ") VALUES (" + userId + ",'" + name + "','" + result
					+ "','" + time + "')";
			System.out.println("添加测试=" + insertUserCollect);
			database.execSQL(insertUserCollect);
			System.out.println("成功");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			database.close();
		}
	}

	/*
	 * 若是已经存在就更新这条数据
	 */

	public void updateTestResult(int userId, String name, String result,
			long time) {

		try {
			String sql = "update " + HP_DBCommons.USERTEST_TABLENAME_COVER
					+ " set " + HP_DBCommons.TESTRESULT + "=?" + " , "
					+ HP_DBCommons.TESTTIME + "=?" + " WHERE "
					+ HP_DBCommons.USERID + " =" + userId + " and "
					+ HP_DBCommons.TESTNAME + " ='" + name + "'";
			Object[] params = new Object[2];
			params[0] = result;
			params[1] = time;
			database.execSQL(sql, params);
			System.out.println("sql成功" + sql);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			database.close();
		}

	}

	/*
	 * 获得测试列表-覆盖的
	 */
	public List<TestCollectinfor> queryUserTestList(int userId) {
		List<TestCollectinfor> testlist = new ArrayList<TestCollectinfor>();
		String sql = "SELECT * FROM " + HP_DBCommons.USERTEST_TABLENAME_COVER
				+ " WHERE " + HP_DBCommons.USERID + " =" + userId;
		Cursor cursor = database.rawQuery(sql, null);
		try {

			if (cursor != null && cursor.getCount() > 0) {
				while (cursor.moveToNext()) {

					String name = cursor.getString(2);
					String result = cursor.getString(3);
					long time = cursor.getLong(4);
					TestCollectinfor testCollectinfor = new TestCollectinfor();
					testCollectinfor.setName(name);
					testCollectinfor.setResult(result);
					testCollectinfor.setCreattime(time);
					testlist.add(testCollectinfor);
				}
				return testlist;
			} else {

				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
			database.close();
		}
		return null;

	}

	/**
	 * 获取推送列表
	 */
	public List<JPushBean> queryJPushList(int userId) {
		List<JPushBean> jBeanList = new ArrayList<JPushBean>();
		String sql = "SELECT * FROM " + HP_DBCommons.JPUSH_TABLENAME
				+ " WHERE " + HP_DBCommons.USERID + " =" + userId;
		Cursor cursor = database.rawQuery(sql, null);
		try {
			if (cursor != null && cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					String title = cursor.getString(2);
					String content = cursor.getString(3);
					String imgUrl = cursor.getString(4);
					long time = cursor.getLong(5);
					int readFlag = cursor.getInt(6);
					JPushBean jBean = new JPushBean();
					jBean.setTitle(title);
					jBean.setContent(content);
					jBean.setImgUrl(imgUrl);
					jBean.setTime(time);
					jBean.setReadFlag(readFlag);
					jBeanList.add(jBean);
				}
				return jBeanList;
			} else {
				return jBeanList;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
			database.close();
		}
		return jBeanList;

	}

	/*
	 * 添加新的JPush
	 */

	public void insertJPush(int userId, String title, String content,
			String imgUrl, long time) {
		try {
			String insertJPush = "INSERT INTO " + HP_DBCommons.JPUSH_TABLENAME
					+ " (" + HP_DBCommons.USERID + ","
					+ HP_DBCommons.JPUSHTITLE + "," + HP_DBCommons.JPUSHCONTENT
					+ "," + HP_DBCommons.JPUSHIMG + ","
					+ HP_DBCommons.JPUSHTIME + "," + HP_DBCommons.JPUSHREADFLAG
					+ ") VALUES (" + userId + ",'" + title + "','" + content
					+ "','" + imgUrl + "','" + time + "'," + 1 + ")";
			System.out.println("添加推送=" + insertJPush);
			database.execSQL(insertJPush);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			database.close();
		}
	}

	/*
	 * 设置JPush为已读
	 */

	public void updateJPushReadFlag(int userId, String title, String content) {

		try {
			String sql = "update " + HP_DBCommons.JPUSH_TABLENAME + " set "
					+ HP_DBCommons.JPUSHREADFLAG + "=?" + " WHERE "
					+ HP_DBCommons.USERID + " =" + userId + " and "
					+ HP_DBCommons.JPUSHTITLE + " ='" + title + "'" + " and "
					+ HP_DBCommons.JPUSHCONTENT + " ='" + content + "'";
			Object[] params = new Object[1];
			params[0] = 0;
			database.execSQL(sql, params);
			System.out.println("sql:updateJPushReadFlag=" + sql);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			database.close();
		}

	}

	/*
	 * 查询未读JPush数量
	 */
	public int queryUnReadJPushCount(int userId) {

		try {

			String sql = "SELECT * FROM " + HP_DBCommons.JPUSH_TABLENAME
					+ " WHERE " + HP_DBCommons.USERID + " =" + userId + " and "
					+ HP_DBCommons.JPUSHREADFLAG + " =" + 1;
			Cursor cursor = database.rawQuery(sql, null);
			int count = cursor.getCount();
			System.out.println("sql:queryUnReadJPushCount=" + count);
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		} finally {
			database.close();
		}

	}

	/*
	 * 删除JPush
	 */

	public void deleteJPush(int userId, String title, String content,long time) {
		try {
			String delJPush = "DELETE FROM " + HP_DBCommons.JPUSH_TABLENAME
					+ " WHERE " + HP_DBCommons.USERID + " =" + userId + " and "
					+ HP_DBCommons.JPUSHTITLE + "='" + title + "' and "
					+ HP_DBCommons.JPUSHCONTENT + "='" + content + "' and "
					+ HP_DBCommons.JPUSHTIME + "='" + time + "'";
			System.out.println("删除语句" + delJPush);
			database.execSQL(delJPush);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			database.close();
		}
	}

	/*
	 * 添加新康友
	 */
	public void insertNewFriend(int userid, int accountid, String accountname,
			String accountavatar, int friendaddi, int iaddfriend, long uptime,
			int readflag) {
		try {
			String insertNewFriend = "INSERT INTO "
					+ HP_DBCommons.NEWFRIEND_TABLENAME
					+ " (USERID,ACCOUNTID,ACCOUNTNAME,ACCOUNTAVATAR,FRIENDADDI,IADDFRIEND,UPDATETIME,READFLAG)"
					+ " VALUES ('" + userid + "','" + accountid + "','"
					+ accountname + "','" + accountavatar + "','" + friendaddi
					+ "','" + iaddfriend + "','" + uptime + "','" + 1 + "')";
			System.out.println("添加新康友=" + insertNewFriend);
			database.execSQL(insertNewFriend);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			database.close();
		}
	}

	/*
	 * 更新，我加了好友
	 */
	public void updateIAddFriend(int userid, int accountid) {
		try {
			String updateNewFriend = "UPDATE "
					+ HP_DBCommons.NEWFRIEND_TABLENAME
					+ " SET IADDFRIEND=1 WHERE USERID=" + userid
					+ " AND ACCOUNTID=" + accountid;
			database.execSQL(updateNewFriend);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			database.close();
		}
	}

	/*
	 * 更新，好友加了我
	 */
	public void updateFriendAddI(int userid, int accountid) {
		try {
			String updateNewFriend = "UPDATE "
					+ HP_DBCommons.NEWFRIEND_TABLENAME
					+ " SET FRIENDADDI=1 WHERE USERID=" + userid
					+ " AND ACCOUNTID=" + accountid;
			database.execSQL(updateNewFriend);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			database.close();
		}
	}

	/*
	 * 根据userid和朋友id查找用户
	 */
	public NewFriendBean getNewFriendByUseridAndFriendid(int userid,
			int friendid) {
		NewFriendBean jBean = null;
		try {
			String selectNewFriend = "SELECT * FROM "
					+ HP_DBCommons.NEWFRIEND_TABLENAME + " WHERE USERID="
					+ userid + " AND ACCOUNTID=" + friendid;
			Cursor cursor = database.rawQuery(selectNewFriend, null);
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				int accountid = cursor.getInt(cursor
						.getColumnIndex("ACCOUNTID"));
				String accountname = cursor.getString(cursor
						.getColumnIndex("ACCOUNTNAME"));
				String accountavatar = cursor.getString(cursor
						.getColumnIndex("ACCOUNTAVATAR"));
				int friendaddi = cursor.getInt(cursor
						.getColumnIndex("FRIENDADDI"));
				int iaddfriend = cursor.getInt(cursor
						.getColumnIndex("IADDFRIEND"));
				long updatetime = cursor.getLong(cursor
						.getColumnIndex("UPDATETIME"));
				int readflag = cursor.getInt(cursor.getColumnIndex("READFLAG"));

				jBean = new NewFriendBean();
				jBean.setAccountid(accountid);
				jBean.setAccountname(accountname);
				jBean.setAccountavatar(accountavatar);
				jBean.setFriendaddi(friendaddi);
				jBean.setIaddfriend(iaddfriend);
				jBean.setReadflag(readflag);
				jBean.setUpdatetime(updatetime);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			database.close();
		}

		return jBean;
	}

	/*
	 * 查询未读新康友数量
	 */
	public int queryUnReadNewFriendCount(int userid) {
		try {

			String sql = "SELECT * FROM " + HP_DBCommons.NEWFRIEND_TABLENAME
					+ " WHERE USERID=" + userid + " AND READFLAG=1";
			Cursor cursor = database.rawQuery(sql, null);
			int count = cursor.getCount();
			System.out.println("queryUnReadNewFriendCount成功=" + count);
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		} finally {
			database.close();
		}
	}

	/**
	 * 获取推送列表
	 */
	public List<NewFriendBean> queryNewFriendList(int userid) {
		List<NewFriendBean> jBeanList = new ArrayList<NewFriendBean>();
		String sql = "SELECT * FROM " + HP_DBCommons.NEWFRIEND_TABLENAME
				+ " WHERE USERID=" + userid + " ORDER BY _ID DESC";
		Cursor cursor = database.rawQuery(sql, null);
		try {
			if (cursor != null && cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					int accountid = cursor.getInt(cursor
							.getColumnIndex("ACCOUNTID"));
					String accountname = cursor.getString(cursor
							.getColumnIndex("ACCOUNTNAME"));
					String accountavatar = cursor.getString(cursor
							.getColumnIndex("ACCOUNTAVATAR"));
					int friendaddi = cursor.getInt(cursor
							.getColumnIndex("FRIENDADDI"));
					int iaddfriend = cursor.getInt(cursor
							.getColumnIndex("IADDFRIEND"));
					long updatetime = cursor.getLong(cursor
							.getColumnIndex("UPDATETIME"));
					int readflag = cursor.getInt(cursor
							.getColumnIndex("READFLAG"));

					NewFriendBean jBean = new NewFriendBean();
					jBean.setAccountid(accountid);
					jBean.setAccountname(accountname);
					jBean.setAccountavatar(accountavatar);
					jBean.setFriendaddi(friendaddi);
					jBean.setIaddfriend(iaddfriend);
					jBean.setReadflag(readflag);
					jBean.setUpdatetime(updatetime);

					jBeanList.add(jBean);
				}
				return jBeanList;
			} else {
				return jBeanList;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
			database.close();
		}
		return jBeanList;
	}
}
