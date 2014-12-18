package joshuamgoodwin.gmail.com.ohiolegalaidassistant;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FormsSQLHelper extends SQLiteOpenHelper {

	public FormsSQLHelper(Context context) {
		super(context, "forms_db", null, 1);
	}

	/* 	
	 *	create the table
	 *	table name = forms
	 *	_id == autoincrement key
	 *	form_name == the user's name for the form as text
	 *	file_name == the name of the file in the system
	 */

	@Override
	public void onCreate(SQLiteDatabase database) {
		//create the table using SQL
		database.execSQL("CREATE TABLE forms (_id INTEGER PRIMARY KEY AUTOINCREMENT, form_name TEXT NOT NULL, file_name TEXT NOT NULL);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

		database.execSQL("DROP TABLE IF EXISTS forms");
		onCreate(database);

	}
}
