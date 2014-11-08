package joshuamgoodwin.gmail.com.ohiolegalaidassistant;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gmail.joshuamgoodwin.CaseManagementSQLHelper;
import com.gmail.joshuamgoodwin.CaseManagementSites;

public class CaseManagementDAO {

	private SQLiteDatabase database;
	private CaseManagementSQLHelper databaseHelper;

	public CaseManagementDAO(Context context) {

		databaseHelper = new CaseManagementSQLHelper(context);
		database = databaseHelper.getWritableDatabase();

	}

	public void close() {
		// method to close the database
		database.close();

	}

	// method for adding new cm website
	public void addNewCMSite(String name, String address) {

		ContentValues contentValues = new ContentValues();
		contentValues.put("site_name", name);
		contentValues.put("site_address", address);
		database.insert("casemanagement", null, contentValues);

	}

	// method for removing case management sites
	public void deleteCMSite(int id) {
		database.delete("casemanagement", "_id = " + id, null);
	}

	public String showAddress(int id) {
		String[] tableColumns = new String[] {"_id", "site_address"};
		Cursor cursor = database.query("casemanagement", tableColumns, "_id = " + id, null, null, null, null);
		cursor.moveToFirst();
		String result = cursor.getString(1);
		cursor.close();
		return result;

	}

	public List<String> caseManagementNamesList() {

		String[] column = {"site_name"};
		List<String> list = new ArrayList<String>();
		Cursor cursor = database.query("casemanagement", column, null, null, null, null, "site_name COLLATE NOCASE");
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String name = cursor.getString(0);
			list.add(name);
			cursor.moveToNext();
		}
		cursor.close();
		return list;
	}

	public List<CaseManagementSites> getCMSites() {

		List<CaseManagementSites> list = new ArrayList<CaseManagementSites>();

		String[] tableColumns = new String[] {"_id", "site_name"};

		// query db
		Cursor cursor = database.query("casemanagement", tableColumns, null, null, null, null, tableColumns[1] + " COLLATE NOCASE");
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			CaseManagementSites cmSites = new CaseManagementSites();
			cmSites.setId(cursor.getInt(0));
			cmSites.setSiteName(cursor.getString(1));
			list.add(cmSites);
			cursor.moveToNext();
		}
		cursor.close();
		return list;
	}

	public String addressFromName(String name) {
		String[] tableColumns = new String[] {"site_name", "site_address"};
		Cursor cursor = database.query("casemanagement", tableColumns, "site_name = ?", new String[] {name}, null, null, null);
		cursor.moveToFirst();
		String result = cursor.getString(1);
		cursor.close();
		return result;
	}
}
