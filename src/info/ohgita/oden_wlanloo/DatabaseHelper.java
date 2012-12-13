package info.ohgita.oden_wlanloo;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "oden_wlanloo";
	private static final String CREATE_TABLE = "CREATE TABLE profiles (_id INTEGER PRIMARY KEY AUTOINCREMENT, PROFILE_CLASS TEXT, LOGIN_ID TEXT, LOGIN_PW TEXT);";
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, 1);
	}
	
	/*
	 * onCreate(db) - データベース初期化(初回)時に呼び出される。
	 * テーブル作成および初期データ登録を行う。*/
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE);
	}
	
	/*
	 * inUpgrade(db, oldVersion, new Version) - データベース再構成時に呼び出される
	 * テーブルの追加作成、再構成、DROPなどを行う。
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS profiles");
        onCreate(db);
	}
}