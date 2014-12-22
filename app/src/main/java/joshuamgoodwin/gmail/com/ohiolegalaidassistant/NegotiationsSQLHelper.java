package joshuamgoodwin.gmail.com.ohiolegalaidassistant;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NegotiationsSQLHelper extends SQLiteOpenHelper {

    public NegotiationsSQLHelper(Context context) {
        super(context, "negotiations_db", null, 1);
    }

	/* 	
	 *	create the table
	 *	table name = negotiations
	 *	_id == autoincrement key
	 *  type == whether custody or other type of negotiation
	 *	client_name == the client's name for the form as text
	 *	op_name == opposing party's name
	 *  offer_number == the place in the offering history
	 *  offer_date == date and time offer made
	 *  offeror == who made the offer
	 *  custody == who will have custody
	 *  visitation == terms of visitation
	 *  child_support == amount of child support
	 *  spousal_support == amount of spousal support
	 *  personal_property_division == terms of property division
	 *  real_property_division == terms of real property divisions
	 *  pensions == terms of pensions
	 *  court_costs == who will pay court costs
	 *  attorney_fees == allocation of attorney fees
	 *  cash_1 == any cash payments
	 *  cash_2 == cash payments
	 *  cash_3 == cash payments
	 *  cash_4 == cash payments
	 *  cash_5 == cash payments
	 *  other_1 == other terms
	 *  other_2 == other terms
	 *  other_3 == other terms
	 *  other_4 == other terms
	 *  other_5 == other terms
	 */

    @Override
    public void onCreate(SQLiteDatabase database) {
        //create the table using SQL
        database.execSQL("CREATE TABLE negotiations (_id INTEGER PRIMARY KEY AUTOINCREMENT, type TEXT NO NULL, client_name TEXT NOT NULL, op_name TEXT NOT NULL, " +
                "offer_number TEXT NOT NULL, offer_date TEXT NOT NULL, offeror TEXT NOT NULL, custody TEXT NOT NULL," +
                " visitation TEXT NOT NULL, child_support TEXT NOT NULL, spousal_support TEXT NOT NULL, personal_property_division TEXT NOT NULL, " +
                "real_property_division TEXT NOT NULL, pensions TEXT NOT NULL, court_costs TEXT NOT NULL, attorney_fees TEXT NOT NULL, " +
                "cash_1 TEXT NOT NULL, cash_2 TEXT NOT NULL, cash_3 TEXT NOT NULL, cash_4 TEXT NOT NULL, cash_5 TEXT NOT NULL, " +
                "other_1 TEXT NOT NULL, other_2 TEXT NOT NULL, other_3 TEXT NOT NULL, other_4 TEXT NOT NULL, other_5 TEXT NOT NULL, extension TEXT NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

        database.execSQL("DROP TABLE IF EXISTS negotiations");
        onCreate(database);

    }
}
