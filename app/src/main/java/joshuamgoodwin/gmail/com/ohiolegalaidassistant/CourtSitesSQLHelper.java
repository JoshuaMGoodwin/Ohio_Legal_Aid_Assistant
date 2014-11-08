package joshuamgoodwin.gmail.com.ohiolegalaidassistant;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CourtSitesSQLHelper extends SQLiteOpenHelper {

	public CourtSitesSQLHelper(Context context) {
		super(context, "courtsites_db", null, 1);
	}
	
	/* 	
	*	create the table
	*	table name = courtsites
	*	_id == autoincrement key
	*	court_name == court's name as text
	*	court_address == court's address as text
	*/
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		//create the table using SQL
		database.execSQL("CREATE TABLE courtsites (_id INTEGER PRIMARY KEY AUTOINCREMENT, court_name TEXT NOT NULL, court_address TEXT NOT NULL);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		
		database.execSQL("DROP TABLE IF EXISTS courtsites");
		onCreate(database);
		
	}
}