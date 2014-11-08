package joshuamgoodwin.gmail.com.ohiolegalaidassistant;

import android.app.Fragment;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.EditText;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.*;
import android.widget.AdapterView;
import android.widget.Toast;
import android.os.Bundle;
import android.app.AlertDialog;


public class FplCalculator extends Fragment {

	public final static double POVERTY_START = 11670.0;
	public final static double POVERTY_INCREMENT = 4060.0;

	private Bundle savedInstanceState;

	private EditText etAGSize;
	private EditText etGrossEarnedIncome;
	private EditText etHoursPerWeek;

	private Spinner spinner;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle instanceState){

	savedInstanceState = instanceState;
	// get view
	View rootView = inflater.inflate(R.layout.fpl_calculator_layout, container, false);

	// populate the spinner
	populateSpinner(rootView);

	// set up the edittexts
	initializeViews(rootView);

	// set up the clear button
	clearButton(rootView);

	// set up the submit button
	submitButton(rootView);

	//return view
	return rootView;
	}

	private void populateSpinner(View rootView) {

		// populate the frequency spinner with a string array
		spinner = (Spinner) rootView.findViewById(R.id.spinnerGrossPayFrequency);

		// create array adapter
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
					R.array.frequency, android.R.layout.simple_spinner_dropdown_item);

		// set layout for when dropdown shown
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// apply adapter to spinner
		spinner.setAdapter(adapter);

		// set spinner default to monthly
		spinner.setSelection(4);

		// set spinner listener to change layout if hourly chosen
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id){
					// if frequency is hourly (0) then display hourly label and edittext
					etHoursPerWeek = (EditText) getView().findViewById(R.id.hoursPerWeek);
					TextView tvHoursPerWeek = (TextView) getView().findViewById(R.id.tvHoursPerWeek);
					if (position == 0) {
						etHoursPerWeek.setVisibility(View.VISIBLE);
						tvHoursPerWeek.setVisibility(View.VISIBLE);
					} else {
						etHoursPerWeek.setVisibility(View.INVISIBLE);
						tvHoursPerWeek.setVisibility(View.INVISIBLE);
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?>arg0){}

			});
	}

	private void initializeViews(View rootView) {
		etAGSize = (EditText)rootView.findViewById(R.id.etAGSize);
		etGrossEarnedIncome = (EditText)rootView.findViewById(R.id.etGrossEarnedIncome);
	}

	private void clearButton(View rootView) {

		Button button = (Button)rootView.findViewById(R.id.FPLClear);

		// set button's onClickListener
		button.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					resetAll();
				}
			});
	}

	private void resetAll() {

		// reset the spinner and edit texts
		spinner.setSelection(4);
		etAGSize.setText("");
		etHoursPerWeek.setText("");
		etGrossEarnedIncome.setText("");

	}

	private void submitButton(View rootView) {

		Button button = (Button) rootView.findViewById(R.id.FPLSubmit);
		button.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {

					// check to see if AGSize is filled in
					if (AGSizeMissing()) return;

					// if hourly rate is on spinner, make sure it doesn't equal 0
					if (hourlyRateMissing()) return;

					// calculate percentage of poverty
					double earnedIncomeRaw = etGrossEarnedIncome.getText().toString().equals("") ? 0.0 :
						Double.parseDouble(etGrossEarnedIncome.getText().toString());

					double annualIncome = calculateAnnualIncome(earnedIncomeRaw);

					double fpl = ((getAGSize() - 1) * POVERTY_INCREMENT) + POVERTY_START;

					double results = Math.floor(((annualIncome / fpl) * 100) * 100) / 100;

					showResults(results);

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

	private boolean hourlyRateMissing() {

		String test = etHoursPerWeek.getText().toString();
		if (etHoursPerWeek.isShown() && (test.equals("") || test.equals("0"))) {
			String text = "If calculating using hours per week, you must enter the number of hours per week worked";
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

	private double calculateAnnualIncome(Double rawIncome) {

		double hours = etHoursPerWeek.isShown() ? 
			Double.parseDouble(etHoursPerWeek.getText().toString()): 0.0;

		switch (spinner.getSelectedItemPosition()) {

			case(0): //hourly
				return hours * rawIncome * 52;
			case(1): // weekly
				return rawIncome * 52;
			case(2): // every other week
				return rawIncome * 26;
			case(3): // twice per month
				return rawIncome * 24;
			case(4): // monthly
				return rawIncome * 12;
			case(5): // annual
				return rawIncome;
			default:
				return 0.0;

		}

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

	public void onActivityCreated() {

		if (savedInstanceState != null) {
			TextView tvHoursPerWeek = (TextView) getView().findViewById(R.id.tvHoursPerWeek);
			etAGSize.setText(savedInstanceState.getString("AGSize"));
			etHoursPerWeek.setText(savedInstanceState.getString("etHoursPerWeek"));
			etGrossEarnedIncome.setText(savedInstanceState.getString("etGrossEarnedIncome"));
			spinner.setSelection(savedInstanceState.getInt("spinner"));
			if (spinner.getSelectedItemPosition() == 0) {
				etHoursPerWeek.setVisibility(View.VISIBLE);
				tvHoursPerWeek.setVisibility(View.VISIBLE);
			} else {
				etHoursPerWeek.setVisibility(View.INVISIBLE);
				tvHoursPerWeek.setVisibility(View.INVISIBLE);
			}

		}

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {

		super.onSaveInstanceState(outState);
		outState.putString("AGSize", etAGSize.getText().toString());
		outState.putString("etGrossEarnedIncome", etGrossEarnedIncome.getText().toString());
		outState.putString("etHoursPerWeek", etHoursPerWeek.getText().toString());
		outState.putInt("spinner", spinner.getSelectedItemPosition());
	}

}
