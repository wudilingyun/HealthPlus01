package com.vee.myhealth.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.vee.healthplus.util.user.HP_DBCommons;
import com.vee.healthplus.util.user.HP_DBHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class DBManager extends SQLiteOpenHelper{
	

	private final int BUFFER_SIZE = 400000;
	public static final String DB_NAME = "habitus.db"; // 保存的数据库文件名
	public static final String PACKAGE_NAME = "com.vee.healthplus";
	public static final String DB_PATH = "/data"

	+ Environment.getDataDirectory().getAbsolutePath() + "/" + PACKAGE_NAME; // 在手机里存放数据库的位置
	public static final String SYMPTOM_BDA_TABLE = "BDA";
	public static final String SYMPTOM_BDC_TABLE = "BDC";
	public static final String SYMPTOM_ILL_TABLE = "ILL";
	public static final String SYMPTOM_SYM_TABLE = "SYM";
	// 数据库表名字
	public static final String HABITUS_TEST_TABLE = "test";
	public static final String HABITUS_RESULT_TABLE = "result";
	public static final String HEALTH_QUESTIONS = "health_questions";
	public static final String HEALTH_RESULT = "health_result";
	public static final String HEALTH_SUGGEST = "health_suggest";

	private SQLiteDatabase database;
	private Context context;
	public DBManager(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		this.context = context;
		// TODO Auto-generated constructor stub
	}



	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		InputStream is;
		System.out.println("导入");
		try {
			new File(DB_PATH + "/" + DB_NAME);
			is = context.getResources().getAssets()
					.open(DB_NAME);
			FileOutputStream fos = new FileOutputStream(DB_PATH + "/" + DB_NAME);
			byte[] buffer = new byte[BUFFER_SIZE];
			int count = 0;
			while ((count = is.read(buffer)) > 0) {
				fos.write(buffer, 0, count);
			}
			System.out.println("导入成功");
			fos.close();
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 欲导入的数据库
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		System.out.println("删除并重新创建");
		if(oldVersion!=newVersion){
			new File(DB_PATH + "/" + DB_NAME).delete();
			onCreate(db);
		}
		
	}
}
