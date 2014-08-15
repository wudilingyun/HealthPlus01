package com.vee.healthplus.util;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MultiDownLoadDBHelper extends SQLiteOpenHelper {

	private static final String TAG = "MultiDownLoadDBHelper";
	private SQLiteDatabase sql = null;
	private boolean isopen = false;
	private Object lock = new Object();

	public MultiDownLoadDBHelper(Context c) {
		super(c, MultiDownLoadDBInfo.DB_NAME, null,
				MultiDownLoadDBInfo.DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(MultiDownLoadDBInfo.CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	/** close the database */
	public void close() {
		try {
			if (isopen) {
				sql.close();
				isopen = false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addMultiDownLoadRecordForList(List<Info> infoList) {
		synchronized (lock) {
			// if (!isopen) {
			// isopen = true;
			sql = getWritableDatabase();
			// }
			for (int i = 0; i < infoList.size(); i++) {
				Info dl = infoList.get(i);
				ContentValues values = new ContentValues();

				values.put(MultiDownLoadDBInfo.ID, dl.id);
				values.put(MultiDownLoadDBInfo.NAME, dl.name);
				values.put(MultiDownLoadDBInfo.URL, dl.url);
				values.put(MultiDownLoadDBInfo.POSITION, dl.position);
				values.put(MultiDownLoadDBInfo.STATE, dl.state);
				values.put(MultiDownLoadDBInfo.PROGRESS, dl.progress);
				values.put(MultiDownLoadDBInfo.APPNAME, dl.appname);
				values.put(MultiDownLoadDBInfo.APPICON, dl.appicon);
				values.put(MultiDownLoadDBInfo.APPSIZE, dl.appsize);
				values.put(MultiDownLoadDBInfo.PKNAME, dl.pkname);
				sql.insert(MultiDownLoadDBInfo.TABLE_NAME, null, values);
			}
		}
	}

	public boolean addMultiDownLoadRecord(Info dl) {
		synchronized (lock) {
			// if (!isopen) {
			// isopen = true;
			sql = getWritableDatabase();
			// }
			ContentValues values = new ContentValues();

			values.put(MultiDownLoadDBInfo.ID, dl.id);
			values.put(MultiDownLoadDBInfo.NAME, dl.name);
			values.put(MultiDownLoadDBInfo.URL, dl.url);
			values.put(MultiDownLoadDBInfo.POSITION, dl.position);
			values.put(MultiDownLoadDBInfo.STATE, dl.state);
			values.put(MultiDownLoadDBInfo.PROGRESS, dl.progress);

			values.put(MultiDownLoadDBInfo.APPNAME, dl.appname);
			values.put(MultiDownLoadDBInfo.APPICON, dl.appicon);
			values.put(MultiDownLoadDBInfo.APPSIZE, dl.appsize);
			values.put(MultiDownLoadDBInfo.PKNAME, dl.pkname);
			long x = sql.insert(MultiDownLoadDBInfo.TABLE_NAME, null, values);
			if (x > 0) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean delAllRecord() {
		synchronized (lock) {
			if (!isopen) {
				isopen = true;
				sql = getWritableDatabase();
			}
			String s = "delete from " + MultiDownLoadDBInfo.TABLE_NAME;
			sql.execSQL(s);
			return true;
		}
	}

	public ArrayList<Info> getDownLoadRecordList() {
		synchronized (lock) {
			if (!isopen) {
				isopen = true;
				sql = getWritableDatabase();
			}
			ArrayList<Info> downloadList = new ArrayList<Info>();
			String[] columns = new String[] { MultiDownLoadDBInfo.ID,
					MultiDownLoadDBInfo.NAME, MultiDownLoadDBInfo.URL,
					MultiDownLoadDBInfo.POSITION, MultiDownLoadDBInfo.STATE,
					MultiDownLoadDBInfo.PROGRESS, MultiDownLoadDBInfo.APPNAME,
					MultiDownLoadDBInfo.APPICON, MultiDownLoadDBInfo.APPSIZE,
					MultiDownLoadDBInfo.PKNAME };

			Cursor cur = sql.query(MultiDownLoadDBInfo.TABLE_NAME, columns,
					null, null, null, null, null);

			while (cur.moveToNext()) {
				Info inf = new Info();

				inf.id = cur.getLong(0);
				inf.name = cur.getString(1);
				inf.url = cur.getString(2);
				inf.position = cur.getLong(3);
				inf.state = cur.getInt(4);
				inf.progress = cur.getInt(5);

				inf.appname = cur.getString(6);
				inf.appicon = cur.getString(7);
				inf.appsize = cur.getString(8);
				inf.pkname = cur.getString(9);

				downloadList.add(inf);
			}
			cur.close();
			System.gc();
			return downloadList;
		}
	}

	public Info getDownLoadInfoByPkgName(String pkgName) {
		Info info = null;
		synchronized (lock) {
			if (!isopen) {
				isopen = true;
				sql = getWritableDatabase();
			}
			String[] columns = new String[] { MultiDownLoadDBInfo.ID,
					MultiDownLoadDBInfo.NAME, MultiDownLoadDBInfo.URL,
					MultiDownLoadDBInfo.POSITION, MultiDownLoadDBInfo.STATE,
					MultiDownLoadDBInfo.PROGRESS, MultiDownLoadDBInfo.APPNAME,
					MultiDownLoadDBInfo.APPICON, MultiDownLoadDBInfo.APPSIZE,
					MultiDownLoadDBInfo.PKNAME };
			Cursor cur = sql.query(MultiDownLoadDBInfo.TABLE_NAME, columns,
					MultiDownLoadDBInfo.PKNAME + " = \"" + pkgName + "\"",
					null, null, null, null);
			if (cur != null) {
				while (cur.moveToNext()) {
					info = new Info();
					info.id = cur.getLong(0);
					info.name = cur.getString(1);
					info.url = cur.getString(2);
					info.position = cur.getLong(3);
					info.state = cur.getInt(4);
					info.progress = cur.getInt(5);
					info.appname = cur.getString(6);
					info.appicon = cur.getString(7);
					info.appsize = cur.getString(8);
					info.pkname = cur.getString(9);
				}
				cur.close();
			}
			System.gc();
		}
		return info;
	}

	public ArrayList<Info> getDownLoadRecordListByState(int state) {
		synchronized (lock) {
			if (!isopen) {
				isopen = true;
				sql = getWritableDatabase();
			}
			ArrayList<Info> downloadList = new ArrayList<Info>();
			String[] columns = new String[] { MultiDownLoadDBInfo.ID,
					MultiDownLoadDBInfo.NAME, MultiDownLoadDBInfo.URL,
					MultiDownLoadDBInfo.POSITION, MultiDownLoadDBInfo.STATE,
					MultiDownLoadDBInfo.PROGRESS, MultiDownLoadDBInfo.APPNAME,
					MultiDownLoadDBInfo.APPICON, MultiDownLoadDBInfo.APPSIZE,
					MultiDownLoadDBInfo.PKNAME };

			Cursor cur = sql.query(MultiDownLoadDBInfo.TABLE_NAME, columns,
					MultiDownLoadDBInfo.STATE + " = \"" + state + "\"", null,
					null, null, null);

			while (cur.moveToNext()) {
				Info inf = new Info();

				inf.id = cur.getLong(0);
				inf.name = cur.getString(1);
				inf.url = cur.getString(2);
				inf.position = cur.getLong(3);
				inf.state = cur.getInt(4);
				inf.progress = cur.getInt(5);

				inf.appname = cur.getString(6);
				inf.appicon = cur.getString(7);
				inf.appsize = cur.getString(8);
				inf.pkname = cur.getString(9);
				downloadList.add(inf);
			}
			cur.close();
			System.gc();
			return downloadList;
		}
	}

	// ///////////////////by name//////////////////////////
	public boolean delMultiDownLoadRecord(String name) {
		synchronized (lock) {
			// if (!isopen) {
			// isopen = true;
			sql = getWritableDatabase();
			// }
			sql.delete(MultiDownLoadDBInfo.TABLE_NAME, MultiDownLoadDBInfo.NAME
					+ "=\"" + name + "\"", null);

			return true;
		}
	}

	public boolean updateStateMultiDownLoadRecord(String name, long position,
			int state, int progress) {
		synchronized (lock) {
			if (!isopen) {
				isopen = true;
				sql = getWritableDatabase();
			}

			ContentValues values = new ContentValues();
			values.put(MultiDownLoadDBInfo.STATE, state);
			if (position != 0)
				values.put(MultiDownLoadDBInfo.POSITION, position);
			if (progress != 0)
				values.put(MultiDownLoadDBInfo.PROGRESS, progress);
			sql.update(MultiDownLoadDBInfo.TABLE_NAME, values,
					MultiDownLoadDBInfo.NAME + " = \"" + name + "\"", null);
			return true;
		}
	}

	public boolean haveMultiDownLoadRecord(String name) {
		synchronized (lock) {
			if (!isopen) {
				isopen = true;
				sql = getWritableDatabase();
			}
			String[] columns = new String[] { MultiDownLoadDBInfo.ID };
			Cursor cur = sql.query(MultiDownLoadDBInfo.TABLE_NAME, columns,
					MultiDownLoadDBInfo.NAME + " = \"" + name + "\"", null,
					null, null, null);
			boolean ret = false;
			while (cur.moveToNext()) {
				ret = true;
			}
			cur.close();
			System.gc();
			return ret;
		}
	}

	public Info getDownLoadRecordListByName(String name) {
		synchronized (lock) {
			if (!isopen) {
				isopen = true;
				sql = getWritableDatabase();
			}

			Info inf = null;
			String[] columns = new String[] { MultiDownLoadDBInfo.ID,
					MultiDownLoadDBInfo.NAME, MultiDownLoadDBInfo.URL,
					MultiDownLoadDBInfo.POSITION, MultiDownLoadDBInfo.STATE,
					MultiDownLoadDBInfo.PROGRESS, MultiDownLoadDBInfo.APPNAME,
					MultiDownLoadDBInfo.APPICON, MultiDownLoadDBInfo.APPSIZE,
					MultiDownLoadDBInfo.PKNAME };

			Cursor cur = sql.query(MultiDownLoadDBInfo.TABLE_NAME, columns,
					MultiDownLoadDBInfo.NAME + " = \"" + name + "\"", null,
					null, null, null);

			while (cur.moveToNext()) {
				inf = new Info();
				inf.id = cur.getLong(0);
				inf.name = cur.getString(1);
				inf.url = cur.getString(2);
				inf.position = cur.getLong(3);
				inf.state = cur.getInt(4);
				inf.progress = cur.getInt(5);

				inf.appname = cur.getString(6);
				inf.appicon = cur.getString(7);
				inf.appsize = cur.getString(8);
				inf.pkname = cur.getString(9);
			}
			cur.close();
			System.gc();
			return inf;
		}
	}

	public Info getDownLoadRecordListByPkgName(String pkgName) {
		synchronized (lock) {
			if (!isopen) {
				isopen = true;
				sql = getWritableDatabase();
			}

			Info inf = null;
			String[] columns = new String[] { MultiDownLoadDBInfo.ID,
					MultiDownLoadDBInfo.NAME, MultiDownLoadDBInfo.URL,
					MultiDownLoadDBInfo.POSITION, MultiDownLoadDBInfo.STATE,
					MultiDownLoadDBInfo.PROGRESS, MultiDownLoadDBInfo.APPNAME,
					MultiDownLoadDBInfo.APPICON, MultiDownLoadDBInfo.APPSIZE,
					MultiDownLoadDBInfo.PKNAME };

			Cursor cur = sql.query(MultiDownLoadDBInfo.TABLE_NAME, columns,
					MultiDownLoadDBInfo.PKNAME + " = \"" + pkgName + "\"",
					null, null, null, null);

			while (cur.moveToNext()) {
				inf = new Info();
				inf.id = cur.getLong(0);
				inf.name = cur.getString(1);
				inf.url = cur.getString(2);
				inf.position = cur.getLong(3);
				inf.state = cur.getInt(4);
				inf.progress = cur.getInt(5);

				inf.appname = cur.getString(6);
				inf.appicon = cur.getString(7);
				inf.appsize = cur.getString(8);
				inf.pkname = cur.getString(9);
			}
			cur.close();
			System.gc();
			return inf;
		}
	}

	static class MultiDownLoadDBInfo {

		public final static String DB_NAME = "multidownload.db";
		public final static int DB_VERSION = 2;

		public final static String TABLE_NAME = "multidownloadtable";

		public final static String ID = "dl_id";
		public final static String NAME = "dl_name";
		public final static String URL = "dl_url";
		public final static String POSITION = "dl_position";
		public final static String STATE = "dl_state";// start 1 running 2 pause
														// 3 finish 4 error 5
		public final static String PROGRESS = "dl_progress";
		public final static String APPNAME = "dl_appname";
		public final static String APPICON = "dl_appicon";
		public final static String APPSIZE = "dl_appsize";
		public final static String PKNAME = "dl_pkname";

		public final static String CREATE_TABLE = "create table IF NOT EXISTS multidownloadtable(dl_id integer primary key autoincrement,"
				+ "dl_name varchar(50) not null,dl_url varchar(100) not null,dl_position integer not null,dl_state integer not null,dl_progress integer not null,"
				+ "dl_appname varchar(50) not null,dl_appicon varchar(100) not null,dl_appsize varchar(50) not null,dl_pkname varchar(50) not null);";
	}

	public static class MultiDownLoadDBState {
		public final static int START = 1;
		public final static int RUNNING = 2;
		public final static int PAUSE = 3;
		public final static int FINISH = 4;
		public final static int ERROR = 5;
		public final static int DELAY = 6;
	}

	public static class Info {
		public Info() {
			;
		}

		public Info(long iid, String iname, String iurl, long iposition,
				int istate, int iprogress, String iappname, String iappicon,
				String iappsize, String ipkname) {
			id = iid;
			name = iname;
			url = iurl;
			position = iposition;
			state = istate;
			progress = iprogress;
			appname = iappname;
			appicon = iappicon;
			appsize = iappsize;
			pkname = ipkname;
		}

		public long id = 0;
		public String name = "";
		public String url = "";
		public long position = 0;
		public int state = 0;
		public int progress = 0;
		public String appname = "";
		public String appicon = "";
		public String appsize = "";
		public String pkname = "";
	}
}
