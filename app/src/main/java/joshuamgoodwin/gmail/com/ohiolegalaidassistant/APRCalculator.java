package joshuamgoodwin.gmail.com.ohiolegalaidassistant;

import android.util.Log;
import android.widget.EditText;
import android.view.View;
import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class APRCalculator extends Fragment {

    private EditText etAmountBorrowed;
	private EditText etBaseRate;
	private EditText etCosts;
	private EditText etNumberOfPayments;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.apr_calculator, container, false);
		initializeViews(rootView);
        return rootView;
    }
	
	private void initializeViews(View rootView) {
	
		etAmountBorrowed = (EditText) rootView.findViewById(R.id.amountBorrowed);
		etBaseRate = (EditText) rootView.findViewById(R.id.baseRate);
		etCosts = (EditText) rootView.findViewById(R.id.costs);
		etNumberOfPayments = (EditText) rootView.findViewById(R.id.numberOfPayments);

        ImageButton submit = (ImageButton) rootView.findViewById(R.id.submit);
		submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                calculateAPR();

            }
        });

        ImageButton clear = (ImageButton) rootView.findViewById(R.id.clear);
        clear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                etAmountBorrowed.setText("");
                etBaseRate.setText("");
                etCosts.setText("");
                etNumberOfPayments.setText("");
            }
        });
	}
	
	// method to calculate APR
    private void calculateAPR() {

        // check for missing data to see if number of payments is missing or 0
        if (etNumberOfPayments.getText().toString().equals("") || etNumberOfPayments.getText().toString().equals("0")) {
            Toast.makeText(getActivity(), "The number of payments is missing or is 0", Toast.LENGTH_LONG).show();
            return;
        }

        // check to see if base rate is missing or 0
        if (etBaseRate.getText().toString().equals("") || etBaseRate.getText().toString().equals("0")) {
            Toast.makeText(getActivity(), "The base rate cannot be blank", Toast.LENGTH_LONG).show();
            return;
        }
        // check to see if amount borrowed is - or empty
        if (etAmountBorrowed.getText().toString().equals("") || etAmountBorrowed.getText().toString().equals("0")) {
            Toast.makeText(getActivity(), "Base rate cannot be blank or 0", Toast.LENGTH_LONG).show();
        }

        double baseRate = Double.parseDouble(etBaseRate.getText().toString());
        double amountBorrowed = Double.parseDouble(etAmountBorrowed.getText().toString());
        // costs are allowed to be 0 so set to 0 if not answered
        double costs = etCosts.getText().toString().equals("") ? 0.0 : Double.parseDouble(etCosts.getText().toString());
        int numberOfPayments = Integer.parseInt(etNumberOfPayments.getText().toString());

        // figure out rate on a monthly basis
		double rate = baseRate / 100 / 12;

        // calculate monthly payment
        double monthlyPayment = ((amountBorrowed + costs) * rate * Math.pow(1 + rate, numberOfPayments)) / (Math.pow(1 + rate, numberOfPayments)-1);
		
		double testrate = rate;
		int iteration = 1;
		double testresult;
		//iterate until result = 0
		double testdiff = testrate;
		while (iteration <= 100) {
			testresult = ((testrate * Math.pow(1 + testrate, numberOfPayments)) / (Math.pow(1 + testrate, numberOfPayments) - 1)) - (monthlyPayment / amountBorrowed);
			if (Math.abs(testresult) < 0.0000001) break;
            Log.d("testresult", Double.toString(Math.abs(testresult)));
			if (testresult < 0) testrate += testdiff;
			else testrate -= testdiff;
			testdiff = testdiff / 2;
			iteration++;
		}
        // round apr
		testrate = (double)Math.round((testrate * 12 * 100) * 10000) / 10000;
        // round monthly payment two two decimal places
        monthlyPayment = (double)Math.round(monthlyPayment * 100) / 100;
        double totalPayments = (double)Math.round((monthlyPayment * numberOfPayments) * 100) / 100;
        double totalInterest = (double)Math.round((totalPayments - amountBorrowed) * 100) /100;

        // display results
		showDialog(testrate, monthlyPayment, totalPayments, totalInterest);
	
	}

    // dialog to display results
    private void showDialog(Double result, Double monthlyPayment, Double totalPayments, Double totalInterest) {
		
		String text = "The APR is " + result + "% and the monthly payment is $" + monthlyPayment + ". Total paid is $" + totalPayments + ", of which, $" + totalInterest + " is interest.";
		// build alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage(text)
				.setPositiveButton("OK", null)
				.setTitle("APR");
			// Create the AlertDialog object and return it
			builder.create().show();
	}

}
