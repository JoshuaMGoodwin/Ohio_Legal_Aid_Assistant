package joshuamgoodwin.gmail.com.ohiolegalaidassistant;

import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import joshuamgoodwin.gmail.com.ohiolegalaidassistant.FederalPovertyCalculator;

import java.util.Calendar;
import java.util.zip.Inflater;

/**
 * Created by Goodwin on 12/13/2014.
 */
public class ACACalculator extends Fragment {

    private static final int PERSONAL_EXEMPTION = 3950;
    private static final int[] STANDARD_DEDUCTION = {6200, 12400, 6200, 9100};
    private static final int[] SINGLE_AMOUNTS = {9075, 36900, 89350, 186350, 405100, 406750, 9999999};
    private static final int[] MARRIED_SEP = {9075, 36900, 74425, 113425, 202550, 228800, 9999999};
    private static final int[] MARRIED_JOINT = {18150, 73800, 148850, 226850, 405100, 457600, 9999999};
    private static final int[] HOH = {12950, 49400, 127550, 206600, 405100, 432200, 9999999};
    private static final double[] RATES = {0.1, 0.15, 0.25, 0.28, 0.33, 0.35, 0.396};

    private double motherAgi, fatherAgi, motherForeign, fatherForeign, motherExempt, fatherExempt, motherSSA, fatherSSA, motherMagi, fatherMagi, motherFpl, fatherFpl, fatherTaxableAmount, motherTaxableAmount, fatherTax, motherTax;

    private EditText mother_agi;
    private EditText father_agi;
    private EditText mother_ssa;
    private EditText father_ssa;
    private EditText father_foreign;
    private EditText mother_foreign;
    private EditText father_interest;
    private EditText mother_interest;
    private TextView mother_magi;
    private TextView father_magi;
    private TextView father_fpl;
    private TextView mother_fpl;
    private TextView mother_taxable_income;
    private TextView father_taxable_income;
    private TextView mother_tax_owed;
    private TextView father_tax_owed;
    private TextView mother_exemptions;
    private TextView father_exemptions;
    private TextView father_standard_deduct;
    private TextView mother_standard_deduct;
    private CheckBox motherMedicaid, fatherMedicaid, motherPTC, fatherPTC;

    private int motherFilingStatus, fatherFilingStatus, motherClaimedDependents, fatherClaimedDependents, motherNumberOFExemptions, fatherNumberOfExemptions;

    private Spinner mother_filing;
    private Spinner father_filing;
    private Spinner mother_dependents;
    private Spinner father_dependents;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.aca_calculator_layout, container, false);
        getViews(rootView);
        populateSpinners();
        InitializeSubmitButton(rootView);
        InitializeClearButton(rootView);
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
        mother_magi = (TextView) v.findViewById(R.id.mother_magi);
        father_magi = (TextView) v.findViewById(R.id.father_magi);
        mother_fpl = (TextView) v.findViewById(R.id.mother_fpl);
        father_fpl = (TextView) v.findViewById(R.id.father_fpl);
        mother_taxable_income = (TextView) v.findViewById(R.id.mother_taxable_income);
        father_taxable_income = (TextView) v.findViewById(R.id.father_taxable_income);
        father_tax_owed = (TextView) v.findViewById(R.id.father_tax_owed);
        mother_tax_owed = (TextView) v.findViewById(R.id.mother_tax_owed);
        father_exemptions = (TextView) v.findViewById(R.id.father_exemptions);
        mother_exemptions = (TextView) v.findViewById(R.id.mother_exemptions);
        mother_standard_deduct = (TextView) v.findViewById(R.id.mother_standard_deduct);
        father_standard_deduct = (TextView) v.findViewById(R.id.father_standard_deduct);
        motherMedicaid = (CheckBox) v.findViewById(R.id.mother_medicaid);
        fatherMedicaid = (CheckBox) v.findViewById(R.id.father_medicaid);
        motherPTC = (CheckBox) v.findViewById(R.id.mother_ptc);
        fatherPTC = (CheckBox) v.findViewById(R.id.father_ptc);
    }

    private void InitializeSubmitButton(View v) {
        ImageButton button = (ImageButton) v.findViewById(R.id.submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalculateResults();
            }
        });
    }

    private void InitializeClearButton(View v) {
        ImageButton button = (ImageButton) v.findViewById(R.id.clear);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mother_agi.setText("0");
                father_agi.setText("0");
                father_ssa.setText("0");
                mother_ssa.setText("0");
                father_foreign.setText("0");
                mother_foreign.setText("0");
                father_interest.setText("0");
                mother_interest.setText("0");
                mother_filing.setSelection(0, false);
                father_filing.setSelection(0, false);
                father_dependents.setSelection(0, false);
                mother_dependents.setSelection(0, false);
                mother_magi.setText("0");
                father_magi.setText("0");
                mother_fpl.setText("0");
                father_fpl.setText("0");
                mother_taxable_income.setText("0");
                father_taxable_income.setText("0");
                father_tax_owed.setText("0");
                mother_tax_owed.setText("0");
                father_exemptions.setText("0");
                mother_exemptions.setText("0");
                mother_standard_deduct.setText("0");
                father_standard_deduct.setText("0");
                motherMedicaid.setChecked(false);
                fatherMedicaid.setChecked(false);
                motherPTC.setChecked(false);
                fatherPTC.setChecked(false);
            }
        });
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

    private void CalculateResults() {

        getVariables();
        DisplayMagi();
        DisplayFpl();
        DisplayTaxableAmount();
        DisplayTax();
        DisplayDeductionExemptions();
        DisplayHealthcareOutcome();
        Toast.makeText(getActivity(), "Scroll to bottom for results", Toast.LENGTH_LONG).show();

    }

    private void getVariables() {
        motherAgi = getDouble(mother_agi.getText().toString());
        fatherAgi = getDouble(father_agi.getText().toString());
        motherFilingStatus = mother_filing.getSelectedItemPosition();
        fatherFilingStatus = father_filing.getSelectedItemPosition();
        motherClaimedDependents = mother_dependents.getSelectedItemPosition();
        fatherClaimedDependents = father_dependents.getSelectedItemPosition();
        motherNumberOFExemptions = NumberOfExemptions(motherFilingStatus, motherClaimedDependents);
        fatherNumberOfExemptions = NumberOfExemptions(fatherFilingStatus, fatherClaimedDependents);
        motherForeign = getDouble(mother_foreign.getText().toString());
        fatherForeign = getDouble(father_foreign.getText().toString());
        motherExempt = getDouble(mother_interest.getText().toString());
        fatherExempt = getDouble(father_interest.getText().toString());
        motherSSA = getDouble(mother_ssa.getText().toString());
        fatherSSA = getDouble(father_ssa.getText().toString());
    }
	
	private double getDouble(String string) {
		try {
			return Double.parseDouble(string);
		} catch (NumberFormatException e) {
			return 0;	
		}
	}

    private void DisplayMagi() {

        motherMagi = motherAgi - motherForeign - motherExempt - motherSSA;
        fatherMagi = fatherAgi - fatherForeign - fatherExempt - fatherSSA;
        mother_magi.setText(Double.toString(motherMagi));
        father_magi.setText(Double.toString(fatherMagi));

    }

    private void DisplayFpl() {

        motherFpl = DetermineFpl(motherNumberOFExemptions, motherFilingStatus, motherMagi);
        fatherFpl = DetermineFpl(fatherNumberOfExemptions, fatherFilingStatus, fatherMagi);
        mother_fpl.setText(Double.toString(motherFpl));
        father_fpl.setText(Double.toString(fatherFpl));

    }

    private void DisplayTaxableAmount() {

        fatherTaxableAmount = fatherAgi - STANDARD_DEDUCTION[fatherFilingStatus] - (fatherNumberOfExemptions * PERSONAL_EXEMPTION);
        motherTaxableAmount = motherAgi - STANDARD_DEDUCTION[motherFilingStatus] - (motherNumberOFExemptions * PERSONAL_EXEMPTION);
        mother_taxable_income.setText(Double.toString(motherTaxableAmount));
        father_taxable_income.setText(Double.toString(fatherTaxableAmount));

    }

    private void DisplayTax() {
        fatherTax = CalculateTax(fatherTaxableAmount, fatherFilingStatus);
        motherTax = CalculateTax(motherTaxableAmount, motherFilingStatus);
        mother_tax_owed.setText(Double.toString(motherTax));
        father_tax_owed.setText(Double.toString(fatherTax));
    }

    private void DisplayDeductionExemptions() {

        father_exemptions.setText(Double.toString(fatherNumberOfExemptions * PERSONAL_EXEMPTION));
        mother_exemptions.setText(Double.toString(motherNumberOFExemptions * PERSONAL_EXEMPTION));
        mother_standard_deduct.setText(Double.toString(STANDARD_DEDUCTION[motherFilingStatus]));
        father_standard_deduct.setText(Double.toString(STANDARD_DEDUCTION[fatherFilingStatus]));

    }

    private void DisplayHealthcareOutcome() {
        if (motherFpl < 138) {
            motherMedicaid.setChecked(true);
        } else {
            motherMedicaid.setChecked(false);
        }

        if (fatherFpl < 138) {
            fatherMedicaid.setChecked(true);
        } else {
            fatherMedicaid.setChecked(false);
        }

        if (fatherFpl < 400 && fatherFpl > 100) {
            fatherPTC.setChecked(true);
        } else {
            fatherPTC.setChecked(false);
        }

        if (motherFpl < 400 && motherFpl > 100) {
            motherPTC.setChecked(true);
        } else {
            motherPTC.setChecked(false);
        }

    }
    private int NumberOfExemptions(int filingStatus, int claimedDependents) {
        int dependents = claimedDependents;
        if (filingStatus == 1) {
            dependents += 2;
        } else {
            dependents += 1;
        }
        return dependents;
    }

    private double DetermineFpl(int HouseholdSize, int filingStatus, double Agi) {

        FederalPovertyCalculator calc = new FederalPovertyCalculator();
        String year = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
        calc.setYear(year);
        calc.setAnnualIncome(Agi);
        calc.setSize(HouseholdSize);
        return calc.getResults();

    }

    private double CalculateTax(double taxableAmount, int filingStatus) {

        int[] topAmounts;

        if (filingStatus == 0) {
            topAmounts = SINGLE_AMOUNTS;
        } else if (filingStatus == 1) {
            topAmounts = MARRIED_JOINT;
        } else if (filingStatus == 2) {
            topAmounts = MARRIED_SEP;
        } else {
            topAmounts = HOH;
        }

        double tempTaxable = taxableAmount;
        double tax = 0;
        int i = 0;
        double priorBase = 0;
        while (tempTaxable > 0) {

            if (tempTaxable + priorBase >= topAmounts[i]) {
                tax += (topAmounts[i] - priorBase) * RATES[i];
                tempTaxable += priorBase - topAmounts[i];
                priorBase = topAmounts[i];
            } else {
                tax += tempTaxable * RATES[i];
                tempTaxable = 0;
            }

            i++;

        }

        return tax;
    }
}
