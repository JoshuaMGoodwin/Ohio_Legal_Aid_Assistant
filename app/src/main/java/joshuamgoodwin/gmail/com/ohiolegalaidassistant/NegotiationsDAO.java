package joshuamgoodwin.gmail.com.ohiolegalaidassistant;

import android.content.ContentValues;
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

    public void deleteNegotiationForClient(String name) {

        // deletes every negotiation for every client
        String[] args = {name};
        database.delete("negotiations", "client_name = ?", args);

    }

    public List<String> getNegotiationsForDrawer() {
        String[] column = {"client_name"};
        List<String> list = new ArrayList<String>();
        Cursor cursor = database.query(true, "negotiations", column, null, null, column[0], null, column[0] + " COLLATE NOCASE", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String name = cursor.getString(0);
            list.add(name);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
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

    public void addNewForm(String[] list) {
        ContentValues contentValues = new ContentValues();
        /*// {client_name.getText().toString(), op_name.getText().toString(), offer_date.getText().toString(),
        offeror.getText().toString(), custody.getText().toString(), visitation.getText().toString(), child_support.getText().toString(),
                spousal_support.getText().toString(), pensions.getText().toString(), personal_property.getText().toString(),
                real_property.getText().toString(), cash_payments.getText().toString(), court_costs.getText().toString(),
                attorney_fees.getText().toString()};*/
        contentValues.put("type", "custody");
        contentValues.put("client_name", list[0]);
        contentValues.put("op_name", list[1]);
        contentValues.put("offer_number", "");
        contentValues.put("offer_date", list[2]);
        contentValues.put("offeror", list[3]);
        contentValues.put("custody", list[4]);
        contentValues.put("visitation", list[5]);
        contentValues.put("child_support", list[6]);
        contentValues.put("spousal_support", list[7]);
        contentValues.put("personal_property_division", list[9]);
        contentValues.put("real_property_division", list[10]);
        contentValues.put("pensions", list[8]);
        contentValues.put("cash_1", list[11]);
        contentValues.put("court_costs", list[12]);
        contentValues.put("attorney_fees", list[13]);
        contentValues.put("cash_2", " ");
        contentValues.put("cash_3", " ");
        contentValues.put("cash_4", " ");
        contentValues.put("cash_5", " ");
        contentValues.put("other_1", " ");
        contentValues.put("other_2", " ");
        contentValues.put("other_3", " ");
        contentValues.put("other_4", " ");
        contentValues.put("other_5", " ");
        contentValues.put("extension", " ");
        database.insert("negotiations", null, contentValues);
    }

}
