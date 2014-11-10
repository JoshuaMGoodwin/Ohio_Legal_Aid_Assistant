package joshuamgoodwin.gmail.com.ohiolegalaidassistant;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import joshuamgoodwin.gmail.com.ohiolegalaidassistant.CourtSitesSQLHelper;
import joshuamgoodwin.gmail.com.ohiolegalaidassistant.CourtSites;

public class CourtSitesDAO {

	private SQLiteDatabase database;
	private CourtSitesSQLHelper databaseHelper;
	
	public CourtSitesDAO(Context context) {
	
		databaseHelper = new CourtSitesSQLHelper(context);
		database = databaseHelper.getWritableDatabase();
	
	}
	
	public void close() {
		// method to close the database
		database.close();
	
	}
	
	// method for adding new court entry
	public void addNewCourt(String name, String address) {
	
		ContentValues contentValues = new ContentValues();
		contentValues.put("court_name", name);
		contentValues.put("court_address", address);
		database.insert("courtsites", null, contentValues);
	
	}

	// method for removing courts
	public void deleteCourt(int id) {
		database.delete("courtsites", "_id = " + id, null);
	}
	
	public String showAddress(int id) {
		String[] tableColumns = new String[] {"_id", "court_address"};
		Cursor cursor = database.query("courtsites", tableColumns, "_id = " + id, null, null, null, null);
		cursor.moveToFirst();
		String result = cursor.getString(1);
		cursor.close();
		return result;
		
	}
	
	public List<String> courtNamesList() {
	
		String[] column = {"court_name"};
		List<String> list = new ArrayList<String>();
		Cursor cursor = database.query("courtsites", column, null, null, null, null, "court_name COLLATE NOCASE");
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String name = cursor.getString(0);
			list.add(name);
			cursor.moveToNext();
		}
		cursor.close();
		return list;
	}
	
	public List<CourtSites> getCourts() {
	
		List<CourtSites> list = new ArrayList<CourtSites>();
		
		String[] tableColumns = new String[] {"_id", "court_name"};
		
		// query db
		Cursor cursor = database.query("courtsites", tableColumns, null, null, null, null, tableColumns[1] + " COLLATE NOCASE");
		cursor.moveToFirst();
		
		while (!cursor.isAfterLast()) {
			CourtSites courtSites = new CourtSites();
			courtSites.setId(cursor.getInt(0));
			courtSites.setCourtName(cursor.getString(1));
			list.add(courtSites);
			cursor.moveToNext();
		}
		cursor.close();
		return list;
	}
	
	public String addressFromName(String name) {
		String[] tableColumns = new String[] {"court_name", "court_address"};
		Cursor cursor = database.query("courtsites", tableColumns, "court_name = ?", new String[] {name}, null, null, null);
		cursor.moveToFirst();
		String result = cursor.getString(1);
		cursor.close();
		return result;
	}
}
