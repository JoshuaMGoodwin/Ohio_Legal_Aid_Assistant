package joshuamgoodwin.gmail.com.ohiolegalaidassistant;

import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.app.Fragment;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.view.*;
import android.view.View.*;

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

        Button submit = (Button) rootView.findViewById(R.id.submit);
		submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                calculateAPR();

            }
        });
	}
	
	private void calculateAPR() {

        double baseRate = Double.parseDouble(etBaseRate.getText().toString());
        double amountBorrowed = Double.parseDouble(etAmountBorrowed.getText().toString());
        double costs = Double.parseDouble(etCosts.getText().toString());
        int numberOfPayments = Integer.parseInt(etNumberOfPayments.getText().toString());
	
		double rate = baseRate / 100 / 12;
		double monthlyPayment = ((amountBorrowed + costs) * rate * Math.pow(1 + rate, numberOfPayments)) / (Math.pow(1 + rate, numberOfPayments)-1);
		
		double testrate = rate;
		int iteration = 1;
		double testresult;
		//iterate until result = 0
		double testdiff = testrate;
		while (iteration <= 100) {
			testresult = ((testrate * Math.pow(1 + testrate, numberOfPayments)) / (Math.pow(1 + testrate, numberOfPayments) - 1)) - (monthlyPayment / amountBorrowed);
			if (Math.abs(testresult) < 0.0000001) break;
			if (testresult < 0) testrate += testdiff;
			else testrate -= testdiff;
			testdiff = testdiff / 2;
			iteration++;
		}
		testrate = (double)Math.round((testrate * 12 * 100) * 100000) / 100000;
		
		
		showDialog(testrate);
	
	}
	private void showDialog(Double result) {
		
		String text = "The APR is " + result + "%";
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage(text)
				.setPositiveButton("OK", null)
				.setTitle("APR");
			// Create the AlertDialog object and return it
			builder.create().show();
	}

}
