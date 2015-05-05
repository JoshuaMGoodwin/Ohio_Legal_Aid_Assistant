package joshuamgoodwin.gmail.com.ohiolegalaidassistant;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import java.io.FileNotFoundException;

public class GarnishmentCalculator extends Fragment implements IncomeDialogFragment.OnUpdateIncomeListener{

    private EditText netIncome;
    private Spinner frequencySpinner;
    private ImageButton submit;
    private ImageButton clear;
    private static final double FEDERAL_HOURLY_MIN_WAGE = 7.25;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle instanceState){
        View rootView = inflater.inflate(R.layout.garnishment_layout, container, false);
        initializeViews(rootView);
        setSpinner();
        setClear();
        setSubmit();
        return rootView;
    }

    private void initializeViews(View rootView){
        netIncome = (EditText) rootView.findViewById(R.id.net_income);
        frequencySpinner = (Spinner) rootView.findViewById(R.id.frequency_spinner);
        submit = (ImageButton) rootView.findViewById(R.id.submit);
        clear = (ImageButton) rootView.findViewById(R.id.clear);
    }

    public void onIncomeSubmit(String results) {
        netIncome.setText(results + " per month");
    }

    private void setSpinner(){

        // Create array adapter  using string-array
        ArrayAdapter<CharSequence> adapter;
        adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.garn_frequency, android.R.layout.simple_spinner_dropdown_item);

        // set layout for when dropdown shown
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // apply adapter to spinner
        frequencySpinner.setAdapter(adapter);

        // set default to biWeekly
        frequencySpinner.setSelection(1);
    }

    private void setClear() {
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    resetAll();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

	private int getMultiplier() {
	
		switch (frequencySpinner.getSelectedItemPosition()) {
				// multiplier based on RC 2923.66(A)(13)
			case 0:
				return 30;
			case 1:
				return 60;
			case 2:
				return 65;
			case 3:
				return 130;
			default:
				return 60;
		}
	}
	
    private void setSubmit() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              
                int multiplier = getMultiplier();
                double minWageAmount = FEDERAL_HOURLY_MIN_WAGE * multiplier;
                double income = Double.parseDouble(netIncome.getText().toString());
                double exemptPercent = income * 0.75;
                double exempt = Math.max(minWageAmount, exemptPercent);
                double garnishable = Math.max(income - exempt, 0);
                displayResults(exempt, garnishable);
            }
        });
    }

    private void displayResults(double exempt, double garnishable) {
        String results;
        if (garnishable == 0) {
            results = "None of the income is garnishable pursuant to O.R.C. 2329.66(A)(13).";
        } else {
            results = "$" + exempt + " of the income is exempt, which means $" + garnishable + " is garnishable pursuant to O.R.C. 2329.66(A)(13).";
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Results")
                .setMessage(results)
                .setPositiveButton("OK", null);
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void resetAll() throws FileNotFoundException {
        frequencySpinner.setSelection(1, false);
        netIncome.setText("");
    }

}
