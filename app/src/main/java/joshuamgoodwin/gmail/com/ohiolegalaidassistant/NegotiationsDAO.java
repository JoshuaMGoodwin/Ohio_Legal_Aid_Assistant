package joshuamgoodwin.gmail.com.ohiolegalaidassistant;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Goodwin on 12/21/2014.
 */
public class NegotiationsDAO {

    private SQLiteDatabase database;
    private NegotiationsSQLHelper databaseHelper;

    public NegotiationsDAO(Context context) {

        databaseHelper = new NegotiationsSQLHelper(context);
        database = databaseHelper.getWritableDatabase();

    }

    public List<Negotiations> getNegotiationsForEdit() {

        List<Negotiations> list = new ArrayList<Negotiations>();

        String[] tableColumns = new String[] {"_id", "client_name"};
        Negotiations negotiations = new Negotiations();
        negotiations.setClientName("Add New Form...");
        negotiations.setId(-1);
        list.add(negotiations);

        // query db
        Cursor cursor = database.query(true, "negotiations", tableColumns, null, null, tableColumns[1], null, tableColumns[1] + " COLLATE NOCASE", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            negotiations = new Negotiations();
            negotiations.setId(cursor.getInt(0));
            negotiations.setClientName(cursor.getString(1));
            list.add(negotiations);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public void deleteNegotiations(String clientName) {
        database.delete("negotiations", "client_name = " + clientName, null);
    }

}
