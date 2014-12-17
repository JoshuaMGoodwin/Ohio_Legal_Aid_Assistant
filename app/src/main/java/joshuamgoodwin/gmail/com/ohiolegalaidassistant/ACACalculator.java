package joshuamgoodwin.gmail.com.ohiolegalaidassistant;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.zip.Inflater;

/**
 * Created by Goodwin on 12/13/2014.
 */
public class ACACalculator extends Fragment {

    private EditText mother_agi;
    private EditText father_agi;
    private EditText mother_ssa;
    private EditText father_ssa;
    private EditText father_foreign;
    private EditText mother_foreign;
    private EditText father_interest;
    private EditText mother_interest;

    private Spinner mother_filing;
    private Spinner father_filing;
    private Spinner mother_dependents;
    private Spinner father_dependents;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.aca_calculator_layout, container, false);
        getViews(rootView);
        populateSpinners();
        return rootView;
    }

    private void getViews(View v) {

        mother_agi = (EditText) v.findViewById(R.id.mother_agi);
        father_agi = (EditText) v.findViewById(R.id.father_agi);
        father_ssa = (EditText) v.findViewById(R.id.father_ssa);
        mother_ssa = (EditText) v.findViewById(R.id.mother_ssa);
        father_foreign = (EditText) v.findViewById(R.id.father_foreign_income);
        mother_foreign = (EditText) v.findViewById(R.id.mother_foreign_income);
        father_interest = (EditText) v.findViewById(R.id.father_exempt_interest);
        mother_interest = (EditText) v.findViewById(R.id.mother_exempt_interest);
        mother_filing = (Spinner) v.findViewById(R.id.mother_filing_status);
        father_filing = (Spinner) v.findViewById(R.id.father_filing_status);
        father_dependents = (Spinner) v.findViewById(R.id.father_claimed_dependents);
        mother_dependents = (Spinner) v.findViewById(R.id.mother_claimed_dependents);

    }

    private void populateSpinners() {

        ArrayAdapter<CharSequence> filingAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.filing_status, android.R.layout.simple_spinner_dropdown_item);

        filingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        father_filing.setAdapter(filingAdapter);
        mother_filing.setAdapter(filingAdapter);

        ArrayAdapter<CharSequence> dependentAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.numbers, android.R.layout.simple_spinner_dropdown_item);

        dependentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        father_dependents.setAdapter(dependentAdapter);
        mother_dependents.setAdapter(dependentAdapter);

    }

}
