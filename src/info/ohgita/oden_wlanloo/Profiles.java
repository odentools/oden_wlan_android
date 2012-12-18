package info.ohgita.oden_wlanloo;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Profiles {
	private Context AppContext;
	private DatabaseHelper databaseHelper;

	/*
	 * Constructor
	 */
	public Profiles(Context context) {
		AppContext = context;
	}

	public boolean returnIsExistItem_ById(int dbItemId) {
		ArrayList<ProfileItem> arr = returnDBItems();
		for (int i = 0; i < arr.size(); i++) {
			ProfileItem prof = (ProfileItem) arr.get(i);
			if (prof.DBItemId == dbItemId) {
				return true;
			}
		}
		return false;
	}

	public ArrayList<ProfileItem> returnDBItems() {
		ArrayList<ProfileItem> arr = new ArrayList<ProfileItem>();

		databaseHelper = new DatabaseHelper(AppContext);

		SQLiteDatabase db = databaseHelper.getReadableDatabase();

		// public Cursor query(String table, String[] columns, String selection,
		// String[] selectionArgs, String groupBy, String having, String orderBy)
		// 参照: http://d.hatena.ne.jp/kuwalab/20110212/1297508786
		Cursor cursor = db
				.query("profiles",// table = テーブル名
						new String[] { "_id", "PROFILE_CLASS", "LOGIN_ID",
								"LOGIN_PW" },// columns = 取得するカラム
						null,// selection = WHERE句(WHEREを除いて指定)
						null,// selectionArgs
						null,// groupBy = groupBy句(〃)
						null,// having = having句(〃)
						null// orderBy = orderBy句(〃)
				);

		cursor.moveToFirst();
		for (int i = 0; i < cursor.getCount(); i++) {// cursor.getCount() = 取得件数

			ProfileItem prof = new ProfileItem(AppContext, cursor.getInt(0));
			prof.profileClassName = cursor.getString(1);
			prof.loginId = cursor.getString(2);
			prof.loginPw = cursor.getString(3);

			arr.add(prof);

			cursor.moveToNext();
		}
		cursor.close();
		return arr;
	}
}

class ProfileItem {
	private Context AppContext;
	private DatabaseHelper databaseHelper;

	protected int DBItemId;
	protected String profileClassName;
	protected String loginId;
	protected String loginPw;

	public ProfileItem(Context context, int id) {
		AppContext = context;
		DBItemId = id;
		return;
	}

	public String getClassName(){
		return profileClassName;
	}
	
	public String getLoginId(){
		return loginId;
	}
	
	public String getLoginPw(){
		return loginPw;
	}
	
	public void setClassName(String value){
		profileClassName = value;
	}
	
	public void setLoginId(String value){
		loginId = value;
	}
	
	public void setLoginPw(String value){
		loginPw = value;
	}
	
	public void delete(){
		databaseHelper = new DatabaseHelper(AppContext);
		SQLiteDatabase db = databaseHelper.getReadableDatabase();
		db.delete(
	       	"profiles",  "_id = "+DBItemId,//whereClause - WHERE句
        	null//whereArgs - WHERE句中の?を置換する
        );
	    db.close();
	}
	
	public boolean save() {
		Profiles profs = new Profiles(AppContext);
		if (DBItemId != -1 && profs.returnIsExistItem_ById(DBItemId)) {
			// Update...

			databaseHelper = new DatabaseHelper(AppContext);

			SQLiteDatabase db = databaseHelper.getReadableDatabase();

			ContentValues cv = new ContentValues();
			cv.put("PROFILE_CLASS", this.profileClassName);
			cv.put("LOGIN_ID", this.loginId);
			cv.put("LOGIN_PW", this.loginPw);

			// public long update(String table, ContentValues values, String
			// whereClause, String[] whereArgs)
			// 参考: http://individualmemo.blog104.fc2.com/blog-entry-49.html
			db.update("profiles",// table - テーブル名
					cv,// values (ContentValues)
					"_id = " + DBItemId + "",// whereClause - WHERE句
					null// whereArgs - WHERE句中の?を置換する
			);
			db.close();
			return true;
		} else {
			/* Insert */

			databaseHelper = new DatabaseHelper(AppContext);

			SQLiteDatabase db = databaseHelper.getReadableDatabase();

			ContentValues cv = new ContentValues();
			cv.put("PROFILE_CLASS", this.profileClassName);
			cv.put("LOGIN_ID", this.loginId);
			cv.put("LOGIN_PW", this.loginPw);

			// public long insert(String table, String nullColumnHack,
			// ContentValues values)
			db.insert("profiles",// table
					"",// nullColumnHack
					cv // values (ContentValues)
			);

			db.close();

			return true;
		}
	}
}