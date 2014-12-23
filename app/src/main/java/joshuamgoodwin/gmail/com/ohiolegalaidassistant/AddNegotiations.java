package joshuamgoodwin.gmail.com.ohiolegalaidassistant;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by Goodwin on 12/22/2014.
 */

public class AddNegotiations extends Fragment {

    private EditText client_name, op_name, offer_date, offeror, custody, visitation, child_support, spousal_support,
            pensions, personal_property, real_property, cash_payments, court_costs, attorney_fees;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.custody_negotiations_layout, container, false);
        getAllViews(rootView);
        initializeSubmit(rootView);
        initializeClear(rootView);
        return rootView;
    }

    private void getAllViews(View v) {
        client_name = (EditText) v.findViewById(R.id.client_name);
        op_name = (EditText) v.findViewById(R.id.op_name);
        offer_date = (EditText) v.findViewById(R.id.offer_date);
        offeror = (EditText) v.findViewById(R.id.offeror);
        custody = (EditText) v.findViewById(R.id.custody);
        visitation = (EditText) v.findViewById(R.id.visitation);
        child_support = (EditText) v.findViewById(R.id.child_support);
        spousal_support = (EditText) v.findViewById(R.id.spousal_support);
        pensions = (EditText) v.findViewById(R.id.pensions);
        personal_property = (EditText) v.findViewById(R.id.personal_property);
        real_property = (EditText) v.findViewById(R.id.real_property);
        cash_payments = (EditText) v.findViewById(R.id.cash_payments);
        court_costs = (EditText) v.findViewById(R.id.court_costs);
        attorney_fees = (EditText) v.findViewById(R.id.attorney_fees);
    }

    private void initializeSubmit(View v) {
        ImageButton button = (ImageButton) v.findViewById(R.id.submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCase();
            }
        });
    }

    private void initializeClear (View v) {
        ImageButton button = (ImageButton) v.findViewById(R.id.clear);
    }

    private void saveCase() {
        String[] list = {client_name.getText().toString(), op_name.getText().toString(), offer_date.getText().toString(),
            offeror.getText().toString(), custody.getText().toString(), visitation.getText().toString(), child_support.getText().toString(),
            spousal_support.getText().toString(), pensions.getText().toString(), personal_property.getText().toString(),
            real_property.getText().toString(), cash_payments.getText().toString(), court_costs.getText().toString(),
            attorney_fees.getText().toString()};
        NegotiationsDAO dao = new NegotiationsDAO(getActivity());
        dao.addNewForm(list);
        ((MainActivity) getActivity()).setDrawer();
        Toast toast = Toast.makeText(getActivity(), "saved", Toast.LENGTH_LONG);
        toast.show();
    }
}
