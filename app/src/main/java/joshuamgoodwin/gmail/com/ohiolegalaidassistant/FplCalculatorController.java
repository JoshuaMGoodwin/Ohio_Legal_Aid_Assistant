package joshuamgoodwin.gmail.com.ohiolegalaidassistant;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.EditText;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.Toast;
import android.os.Bundle;
import android.app.AlertDialog;
import joshuamgoodwin.gmail.com.ohiolegalaidassistant.FederalPovertyCalculator;
import android.view.View.*;


public class FplCalculatorController extends Fragment implements IncomeDialogFragment.OnUpdateIncomeListener {


    public final static String[] VERSION_ARRAY = {"2015", "2014"};

	private Bundle savedInstanceState;

	private EditText etAGSize;
	private EditText etGrossEarnedIncome;

	private Spinner versionSpinner;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle instanceState){

        savedInstanceState = instanceState;
        // get view
        View rootView = inflater.inflate(R.layout.fpl_calculator_layout, container, false);

        // populate the version spinner
        populateFrequencySpinner(rootView);

        // set up the edittexts
        initializeViews(rootView);

        // set up the clear button
        clearButton(rootView);

        // set up the submit button
        submitButton(rootView);

        //return view
        return rootView;
	}


    private void populateFrequencySpinner(View v) {
        versionSpinner = (Spinner) v.findViewById(R.id.version_spinner);

        // create array adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, VERSION_ARRAY);

        // set layout for when dropdown shown
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // apply adapter to spinner
        versionSpinner.setAdapter(adapter);

        // set spinner default to current year
        versionSpinner.setSelection(0);
    }

	private void initializeViews(View rootView) {
		etAGSize = (EditText)rootView.findViewById(R.id.etAGSize);
		etGrossEarnedIncome = (EditText)rootView.findViewById(R.id.etGrossEarnedIncome);
		etGrossEarnedIncome.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                showIncomeDialog("Gross Earned Income");
			}
		});
        etGrossEarnedIncome.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showIncomeDialog("Gross Earned Income");
                }
            }
        });
	}

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Make sure fragment codes match up
        if (requestCode == 0) {
            String etGrossEarnedIncome = data.getStringExtra(
                    "result");
        }
    }

	private void clearButton(View rootView) {

		ImageButton button = (ImageButton)rootView.findViewById(R.id.clear);
        //addShadow(button);

		// set button's onClickListener
		button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAll();
            }
        });
	}

	private void resetAll() {

		// reset the spinner and edit texts
		etAGSize.setText("");
		etGrossEarnedIncome.setText("");

	}

	private void submitButton(View rootView) {

		ImageButton button = (ImageButton) rootView.findViewById(R.id.submit);
		button.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {

					// check to see if AGSize is filled in
					if (AGSizeMissing()) return;

					// calculate percentage of poverty
					double annualIncome = etGrossEarnedIncome.getText().toString().equals("") ? 0.0 :
						Double.parseDouble(etGrossEarnedIncome.getText().toString());

                    FederalPovertyCalculator calc = new FederalPovertyCalculator();

                    calc.setSize(getAGSize());
                    calc.setAnnualIncome(annualIncome);
                    calc.setYear(versionSpinner.getSelectedItem().toString());

					showResults(calc.getResults());

				}

			});
	}

	private boolean AGSizeMissing() {

		// see if AGSize is greater than 0.
		// if it is, return false
		// if it is less than 1, show toast and return true

		String test = etAGSize.getText().toString();

		if (test.equals("") || test.equals("0")) {
			String text = "The assistance group size must be 1 or larger";
			errorToast(text);
			return true;
		} else {
			return false;
		}

	}

	private void errorToast(String text){

		Toast toast = Toast.makeText(getActivity(), text, Toast.LENGTH_LONG);
		toast.show();

	}

	private int getAGSize() {

		return Integer.parseInt(etAGSize.getText().toString());

	}

	private void showResults(Double results) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("This household is at " + results + "% of the Federal Poverty Level")
			.setPositiveButton("OK", null)
			.setTitle("Percentage of Poverty");
		builder.create().show();

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {

		super.onSaveInstanceState(outState);
		outState.putString("AGSize", etAGSize.getText().toString());
		outState.putString("etGrossEarnedIncome", etGrossEarnedIncome.getText().toString());
	}
    private void showIncomeDialog(String title) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        Bundle args = new Bundle();
        args.putString("title", title);
        IncomeDialogFragment dialog = new IncomeDialogFragment();
        dialog.setTargetFragment(this, 0);
        dialog.setArguments(args);
        dialog.show(fm, "IncomeDialog");
    }

    @Override
    public void onIncomeSubmit(String annualIncome) {
        // Do stuff
        etGrossEarnedIncome.setText(annualIncome);
    }
}
