package joshuamgoodwin.gmail.com.ohiolegalaidassistant;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CaseManagementSQLHelper extends SQLiteOpenHelper {

	public CaseManagementSQLHelper(Context context) {
		super(context, "casemanagement_db", null, 1);
	}

	/* 	
	 *	create the table
	 *	table name = courtsites
	 *	_id == autoincrement key
	 *	site_name == name of the site in the casemgmt sys as text
	 *	site_address == URL of site as text
	 */

	@Override
	public void onCreate(SQLiteDatabase database) {
		//create the table using SQL
		database.execSQL("CREATE TABLE casemanagement (_id INTEGER PRIMARY KEY AUTOINCREMENT, site_name TEXT NOT NULL, site_address TEXT NOT NULL);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

		database.execSQL("DROP TABLE IF EXISTS courtsites");
		onCreate(database);

	}
}
