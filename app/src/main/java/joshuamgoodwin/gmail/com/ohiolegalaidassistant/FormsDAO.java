package joshuamgoodwin.gmail.com.ohiolegalaidassistant;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import joshuamgoodwin.gmail.com.ohiolegalaidassistant.FormsSQLHelper;
import joshuamgoodwin.gmail.com.ohiolegalaidassistant.Forms;

public class FormsDAO {

	private SQLiteDatabase database;
	private FormsSQLHelper databaseHelper;

	public FormsDAO(Context context) {

		databaseHelper = new FormsSQLHelper(context);
		database = databaseHelper.getWritableDatabase();

	}

	public void close() {
		// method to close the database
		database.close();

	}

    public void editForm(String name, String id, String address, String ext) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("form_name", name);
        contentValues.put("file_name", address);
        contentValues.put("_id", id);
        contentValues.put("extension", ext);
        database.update("forms", contentValues,"_id = ?", new String[] {id});
    }

	// method for adding new form
	public void addNewForm(String name, String address, String ext) {

		ContentValues contentValues = new ContentValues();
		contentValues.put("form_name", name);
		contentValues.put("file_name", address);
        contentValues.put("extension", ext);
		database.insert("forms", null, contentValues);

	}

	// method for removing form
	public void deleteForm(int id) {
		database.delete("forms", "_id = " + id, null);
	}

	public String showFileName(int id) {
		String[] tableColumns = new String[] {"_id", "file_name"};
		Cursor cursor = database.query("forms", tableColumns, "_id = " + id, null, null, null, null);
		cursor.moveToFirst();
		String result = cursor.getString(1);
		cursor.close();
		return result;

	}

	public List<String> formNamesList() {

		String[] column = {"form_name"};
		List<String> list = new ArrayList<String>();
		Cursor cursor = database.query("forms", column, null, null, null, null, "form_name COLLATE NOCASE");
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String name = cursor.getString(0);
			list.add(name);
			cursor.moveToNext();
		}
		cursor.close();
		return list;
	}

	public List<Forms> getForms() {

		List<Forms> list = new ArrayList<Forms>();

		String[] tableColumns = new String[] {"_id", "form_name"};

		// query db
		Cursor cursor = database.query("forms", tableColumns, null, null, null, null, tableColumns[1] + " COLLATE NOCASE");
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			Forms forms = new Forms();
			forms.setId(cursor.getInt(0));
			forms.setFormName(cursor.getString(1));
			list.add(forms);
			cursor.moveToNext();
		}
		cursor.close();
		return list;
	}

    public List<Forms> getFormsForEdit() {

        List<Forms> list = new ArrayList<Forms>();

        String[] tableColumns = new String[] {"_id", "form_name"};
        Forms forms = new Forms();
        forms.setFormName("Add New Form...");
        forms.setId(-1);
        list.add(forms);

        // query db
        Cursor cursor = database.query("forms", tableColumns, null, null, null, null, tableColumns[1] + " COLLATE NOCASE");
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            forms = new Forms();
            forms.setId(cursor.getInt(0));
            forms.setFormName(cursor.getString(1));
            list.add(forms);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public String[] getForm(int id) {
        String[] tableColumns = new String[] {"_id", "form_name", "file_name", "extension"};
        Cursor cursor = database.query("forms", tableColumns, "_id = ?", new String[] {Integer.toString(id)}, null, null, null);
        cursor.moveToFirst();
        String number = cursor.getString(0);
        String name = cursor.getString(1);
        String fileName = cursor.getString(2);
        String extension = cursor.getString(3);
        return new String[] {number, name, fileName, extension};
    }

	public String addressFromName(String name) {
		String[] tableColumns = new String[] {"form_name", "file_name"};
		Cursor cursor = database.query("forms", tableColumns, "form_name = ?", new String[] {name}, null, null, null);
		cursor.moveToFirst();
		String result = cursor.getString(1);
		cursor.close();
		return result;
	}

    public String extensionFromName(String name) {
        String[] tableColumns = new String[] {"form_name", "extension"};
        Cursor cursor = database.query("forms", tableColumns, "form_name = ?", new String[] {name}, null, null, null);
        cursor.moveToFirst();
        String result = cursor.getString(1);
        cursor.close();
        return result;
    }
}
