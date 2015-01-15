package joshuamgoodwin.gmail.com.ohiolegalaidassistant;

import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Toast;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.widget.DrawerLayout;
import android.widget.ListView;
import android.widget.AdapterView.*;
import android.widget.*;



public class OwfCalculator extends Fragment {

    private static final int[][] OWF_PAYMENT_STANDARD = {
            {282,386,473,582,682,759,848,940,1034,1127,1218,1312}, // January 2015
            {277,380,465,572,671,746,834,924,1017,1108,1198,1290} // July 2014
    };

    private static final int[][] INITIAL_ELIGIBILITY_STANDARD = {
            {487,656,825,994,1163,1333,1502,1671,1840,2009,2178,2348}, // January 2015
            {487,656,825,994,1163,1333,1502,1671,1840,2009,2178,2348} // July 2014
    };

    private Bundle savedInstanceState;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle instanceState) {

        View rootView = inflater.inflate(R.layout.owf_calculator, container, false);
        savedInstanceState = instanceState;
        populateFrequencySpinner(rootView);
        populateVersionSpinner(rootView);
        resetButton(rootView);
        submitButton(rootView);
        initializeVariables(rootView);
        resetAll();

        // see if anything in SavedInstanceState Bundle from
        // prior restarts

        return rootView;

    }

    public OwfCalculator(){
        // emptypublicconstructor
    }

    public void onActivityCreated(){

        // see if anything in SavedInstanceState Bundle from
        // prior restarts
        if (savedInstanceState != null) getInstanceState(savedInstanceState);



    }

    private void initializeVariables(View rootView){
        etAGSize = (EditText)rootView.findViewById(R.id.AGSize);
        etDeemedIncome = (EditText)rootView.findViewById(R.id.deemedIncome);
        etDependentCare = (EditText)rootView.findViewById(R.id.dependentCare);
        etHoursPerWeek = (EditText)rootView.findViewById(R.id.hoursPerWeek);
        etGrossEarnedIncome = (EditText)rootView.findViewById(R.id.grossEarnedIncome);
        etUnearnedIncome = (EditText)rootView.findViewById(R.id.unearnedIncome);
    }

    private void resetAll(){
        etAGSize.setText("");
        etDeemedIncome.setText("");
        etDependentCare.setText("");
        etHoursPerWeek.setText("");
        etGrossEarnedIncome.setText("");
        etUnearnedIncome.setText("");
        spinner.setSelection(4);
        versionSpinner.setSelection(0);
    }

    private void getInstanceState(Bundle savedInstanceState) {

        etAGSize.setText(savedInstanceState.getString("etAGSize"));
        etDeemedIncome.setText(savedInstanceState.getString("etDeemedIncome"));
        etDependentCare.setText(savedInstanceState.getString("etDependentCare"));
        etGrossEarnedIncome.setText(savedInstanceState.getString("etGrossEarnedIncome"));
        etHoursPerWeek.setText(savedInstanceState.getString("etHoursPerWeek"));
        etUnearnedIncome.setText(savedInstanceState.getString("etUnearnedIncome"));
        int spinnerPosition = savedInstanceState.getInt("spinner");
        spinner.setSelection(spinnerPosition);
        if (spinnerPosition == 0) {
            etHoursPerWeek.setVisibility(View.VISIBLE);
            TextView tvHoursPerWeek = (TextView)getView().findViewById(R.id.tvHoursPerWeek);
            tvHoursPerWeek.setVisibility(View.VISIBLE);
        }
    }

    private void populateFrequencySpinner(View rootView) {

        // populate the frequency spinner with string-array

        spinner = (Spinner) rootView.findViewById(R.id.spinnerGrossPayFrequency);

        // Create array adapter  using string-array
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.frequency, android.R.layout.simple_spinner_dropdown_item);

        // set layout for when dropdown shown
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // apply adapter to spinner
        spinner.setAdapter(adapter);

        // set default to monthly
        spinner.setSelection(4);

        // set listener to change visibility of hours per week as needed
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                // if frequency spinner = hourly, show hours per week fields, otherwise hide them
                if (position == 0) {
                    etHoursPerWeek = (EditText)getView().findViewById(R.id.hoursPerWeek);
                    etHoursPerWeek.setVisibility(View.VISIBLE);
                    TextView tvHoursPerWeek = (TextView)getView().findViewById(R.id.tvHoursPerWeek);
                    tvHoursPerWeek.setVisibility(View.VISIBLE);
                } else {
                    etHoursPerWeek = (EditText)getView().findViewById(R.id.hoursPerWeek);
                    etHoursPerWeek.setVisibility(View.GONE);
                    TextView tvHoursPerWeek = (TextView)getView().findViewById(R.id.tvHoursPerWeek);
                    tvHoursPerWeek.setVisibility(View.GONE);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?>arg0){}

        });

    }

    private void populateVersionSpinner(View v) {

        versionSpinner = (Spinner) v.findViewById(R.id.owf_version_spinner);
        // Create array adapter  using string-array
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.owf_versions, android.R.layout.simple_spinner_dropdown_item);

        // set layout for when dropdown shown
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // apply adapter to spinner
        versionSpinner.setAdapter(adapter);

        // set default to monthly
        versionSpinner.setSelection(0);

        // set listener to change visibility of hours per week as needed
        versionSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                version = position;

            }
            @Override
            public void onNothingSelected(AdapterView<?>arg0){}

        });

    }

    private void submitButton(View rootView) {
        ImageButton button = (ImageButton) rootView.findViewById(R.id.submit);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                // check to see if any data is missing
                boolean dataMissing = checkForMissingData();

                // if data is missing, leave method
                if (dataMissing) return;

                // set variables from edit text fields
                setVariables();

                // determine initial eligibility
                boolean initialEligibilityMet = checkInitialEligibility();

                // if the initial eligibility test fails, exit method
                if (!initialEligibilityMet) return;

                // see if countable income exceeds payment standard
                boolean countableIncomeStandardMet = checkCountableIncome();

                // if countable income test fails, exit method
                if (!countableIncomeStandardMet) return;

                // if both tests passed, show dialog of results
                displayResults();
            }
        });
    }

    private void resetButton(View rootView) {
        ImageButton button = (ImageButton) rootView.findViewById(R.id.clear);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                resetAll();
            }
        });
    }

    private boolean checkForMissingData() {

        etAGSize = (EditText)getView().findViewById(R.id.AGSize);
        etDeemedIncome = (EditText)getView().findViewById(R.id.deemedIncome);
        etDependentCare = (EditText)getView().findViewById(R.id.dependentCare);
        etHoursPerWeek = (EditText)getView().findViewById(R.id.hoursPerWeek);
        etGrossEarnedIncome = (EditText)getView().findViewById(R.id.grossEarnedIncome);
        etUnearnedIncome = (EditText)getView().findViewById(R.id.unearnedIncome);

        // check to see if earned income is null, and if so, set to 0
        mGrossEarnedIncomeRaw = etGrossEarnedIncome.getText().toString().equals("") ? 0 :
                Double.parseDouble(etGrossEarnedIncome.getText().toString());

        // check to see if hours is null and needs to be filled
        if (etHoursPerWeek.getText().toString().equals("0") || etHoursPerWeek.getText().toString().equals("")) {
            if (etHoursPerWeek.isShown() && mGrossEarnedIncomeRaw != 0) {
                Toast toast = Toast.makeText(getActivity(), "Input the number of hours per week being worked", Toast.LENGTH_LONG);
                toast.show();
                return true;
            }
        }

        // check AGSize to make sure it is > 0

        String test = etAGSize.getText().toString();
        if (test.equals("") || test.equals("0")) {
            Toast toast = Toast.makeText(getActivity(), "Assistance group size must be 1 or larger", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL| Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
            return true;
        } else {
            // have to do - 1 b/c arrays start counting from 0, not 1
            mAGSize = Integer.parseInt(test) - 1;
        }

        return false;
    }

    private void setVariables() {

        // get values from edit texts (AG Size set in error check)
        mGrossEarnedIncomeFinal = getGrossIncome();

        mDeemedIncome = etDeemedIncome.getText().toString().equals("") ?
                0 : Double.parseDouble(etDeemedIncome.getText().toString());

        mUnearnedIncome = etUnearnedIncome.getText().toString().equals("") ?
                0 : Double.parseDouble(etUnearnedIncome.getText().toString());

        mDependentCare = etDependentCare.getText().toString().equals("") ?
                0 : Double.parseDouble(etDependentCare.getText().toString());

        // round everything down (drop cents)
        // OAC 5101:1-23-20(E)(1)
        mGrossEarnedIncomeFinal = Math.floor(mGrossEarnedIncomeFinal);
        mDeemedIncome = Math.floor(mDeemedIncome);
        mDependentCare = Math.floor(mDependentCare);
        mUnearnedIncome = Math.floor(mUnearnedIncome);

    }

    private double getGrossIncome(){

        Double hoursPerWeek = 0.0;

        if (etHoursPerWeek.isShown()) {
            hoursPerWeek = Double.parseDouble(etHoursPerWeek.getText().toString());
        }


        switch (spinner.getSelectedItemPosition()){

            case(0): // hourly
                return hoursPerWeek * mGrossEarnedIncomeRaw * 4.3;
            case(1): // weekly
                return mGrossEarnedIncomeRaw * 4.3;
            case(2): // every other week
                return mGrossEarnedIncomeRaw * 2.15;
            case(3): // twice per month
                return mGrossEarnedIncomeRaw * 2;
            case(4): // monthly
                return mGrossEarnedIncomeRaw;
            case(5): // annual
                return mGrossEarnedIncomeRaw / 12;
            default:
                return 0;
        }

    }

    private boolean checkInitialEligibility() {

        // this is the initial eligibility test
        // OAC 5101:1-23-20(H)(1)

        // calculate gross income for purposes of initial eligibility
        int grossIncomeTotal = (int)Math.floor(mGrossEarnedIncomeFinal - mDependentCare + mDeemedIncome + mUnearnedIncome);

        // see if initial eligibility test in OAC 5101:1-23-20(H)(1) is met
        if (grossIncomeTotal > INITIAL_ELIGIBILITY_STANDARD[version][mAGSize]) {
            // if the income is too high, show alert dialog with explanation
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Initial Eligibility Test Failed. The total gross income of $" + grossIncomeTotal + " exceeds the initial eligibility standard of $" + INITIAL_ELIGIBILITY_STANDARD[version][mAGSize] + " by $" + (grossIncomeTotal - INITIAL_ELIGIBILITY_STANDARD[version][mAGSize]))
                    .setPositiveButton("OK", null)
                    .setTitle("Ineligible");
            // Create the AlertDialog object and return it
            builder.create().show();
            // return false to let function know eligibility failed
            return false;
        } else {
            // if the initial eligibility test passes
            return true;
        }

    }

    private boolean checkCountableIncome() {

		/* 	see if countable income exceeds OWF payment standard
		 OAC 5101:1-23-20(H)(2)
		 return true if standard is met
		 return false if test fails */

        // calculate countable income from OAC 5101:1-23-20(H)(2)(a)-(b)

        // make sure earnedincome isn't negative
        int adjustedEarnedIncome = (int)Math.floor((mGrossEarnedIncomeFinal - 250) / 2);
        if (adjustedEarnedIncome <= 0) adjustedEarnedIncome = 0;
        countableIncome = (int)Math.floor(adjustedEarnedIncome - mDependentCare + mUnearnedIncome + mDeemedIncome);

        // compare countable income against payment standard
        if (countableIncome >= OWF_PAYMENT_STANDARD[version][mAGSize]) {
            // if the countable income is too high, show alert dialog with explanation
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Countable Income Test Failed. The total countable income of $" + countableIncome + " exceeds the OWF payment standard of $" +
                    OWF_PAYMENT_STANDARD[version][mAGSize] + " by $" + (countableIncome - OWF_PAYMENT_STANDARD[version][mAGSize]))
                    .setPositiveButton("OK", null)
                    .setTitle("Ineligible");
            // Create the AlertDialog object and return it
            builder.create().show();
            // return false to let function know eligibility failed
            return false;
        } else {
            // countable income test passed
            return true;
        }

    }

    private void displayResults() {

        // display dialog showing how much OWF person can get
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Eligible for OWF in the amount of $" + (OWF_PAYMENT_STANDARD[version][mAGSize] - countableIncome) + " per month")
                .setPositiveButton("OK", null)
                .setTitle("Eligible");
        // Create the AlertDialog object and return it
        builder.create().show();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putString("etAGSize", etAGSize.getText().toString());
        outState.putString("etDeemedIncome", etDeemedIncome.getText().toString());
        outState.putString("etDependentCare", etDependentCare.getText().toString());
        outState.putString("etGrossEarnedIncome", etGrossEarnedIncome.getText().toString());
        outState.putString("etHoursPerWeek", etHoursPerWeek.getText().toString());
        outState.putString("etUnearnedIncome", etUnearnedIncome.getText().toString());
        outState.putInt("spinner", spinner.getSelectedItemPosition());

    }

    private double mDeemedIncome;
    private double mDependentCare;
    private double mGrossEarnedIncomeRaw;
    private double mGrossEarnedIncomeFinal;
    private double mUnearnedIncome;

    private int mAGSize;
    private int countableIncome;
    private int version;

    private EditText etAGSize;
    private EditText etDeemedIncome;
    private EditText etDependentCare;
    private EditText etGrossEarnedIncome;
    private EditText etHoursPerWeek;
    private EditText etUnearnedIncome;

    private Spinner spinner;
    private Spinner versionSpinner;

}
