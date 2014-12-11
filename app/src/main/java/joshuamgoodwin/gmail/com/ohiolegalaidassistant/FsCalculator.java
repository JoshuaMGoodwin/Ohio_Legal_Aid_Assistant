package joshuamgoodwin.gmail.com.ohiolegalaidassistant;
 
import android.support.v4.app.Fragment;
import android.view.*;
import android.os.*;
import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Spinner.*;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CheckBox;
import android.widget.ArrayAdapter;
import android.widget.ArrayAdapter.*;
import android.widget.AdapterView;
import android.widget.Button;
 
 
 
public class FsCalculator extends Fragment {
 
    private static final int[] FA_ALLOTMENT = { 194, 357, 511, 649, 771, 925, 1022, 1169, 1315, 1461, 1607, 1753 };
    private static final int[] STANDARD_DEDUCTION = { 155, 155, 155, 165, 193, 221, 221, 221, 221, 221, 221, 221 };
    private static final int[] NET_STANDARD = { 973, 1311, 1650, 1988, 2326, 2665, 3003, 3341, 3680, 4019, 4358, 4697 };
    private static final int[] GROSS_INCOME_LIMIT = { 1265, 1705, 2144, 2584, 3024, 3464, 3904, 4344, 4784, 5224, 5664, 6104 };
    private static final int[] GROSS_INCOME_165 = { 1605, 2163, 2722, 3280, 3838, 4396, 4955, 5513, 6072, 6631, 7190, 7749 };
    private static final int[] GROSS_INCOME_200 = { 1945, 2622, 3299, 3975, 4652, 5329, 6005, 6682, 7359, 8035, 8712, 9389 };
    private static final int STANDARD_SHELTER_HOMELESS = 143;
    private static final int EXCESS_INCOME_DEDUCTION = 20;
    private static final int EXCESS_MEDICAL_DEDUCTION = 35;
    private static final int DEPENDENT_CARE_DEDUCTION = 0;
    private static final int MINNIMUM_MONTHLY_ALLOTMENT = 16;
    private static final int STANDARD_UTILITY_ALLOWANCE = 498;
    private static final int LIMITED_UTILITY_ALLOWANCE = 330;
    private static final int SINGLE_UTILITY_ALLOWANCE = 73;
    private static final int STANDARD_TELEPHONE_ALLOWANCE = 39;
    private static final int LIMIT_ON_SHELTER_DEDUCTION = 490;
    
	private boolean isAged;
	private boolean isDisabled;
	 
    private CheckBox cbElectricGasOil;
    private CheckBox cbGarbageTrash;
    private CheckBox cbHeatingCooling;
    private CheckBox cbHomeless;
    private CheckBox cbPhone;
    private CheckBox cbWaterSewer;
     
    private double earnedIncome;
    private double earnedHoursPerWeek;
    private double unearnedHoursPerWeek;
    private double unearnedIncome;
     
    private EditText etAGsize;
    private EditText etChildSupport;
    private EditText etDependentCare;
    private EditText etEarnedIncome;
    private EditText etEarnedHoursPerWeek;
    private EditText etMedicalExpenses;
    private EditText etPropertyInsurance;
    private EditText etPropertyTaxes;
    private EditText etRent;
    private EditText etUnearnedIncome;
    private EditText etUnearnedHoursPerWeek;
     
    private int AGSize;
    private int childSupport;
    private int dependentCare;
    private int finalEarnedIncome;
	private int finalNetIncome;
    private int finalUnearnedIncome;
    private int grossIncomeAmount;
    private int medicalExpenses;
    private int propertyInsurance;
    private int propertyTaxes;
    private int rent;
    private int totalGrossIncome;
    private int utilityAllowance;
     
    private Spinner AGSSISpinner;
    private Spinner frequencyEarnedSpinner;
    private Spinner frequencyUnearnedSpinner;
	private Spinner AGagedSpinner;
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fs_calculator_layout, container, false);
        initializeViews(rootView);
        populateFrequencySpinners(rootView);
        populateAGSpinners(rootView);
        initializeHomelessCheck(rootView);
        initializeClearButton(rootView);
        initializeSubmitButton(rootView);
        // if (savedInstanceState != null) restoreState(savedInstanceState);
        return rootView;
    }
     
    public void FsCalculator(){
         
    }
 
    private void initializeViews(View rootView){
         
        // initialize check boxes
        cbElectricGasOil = (CheckBox) rootView.findViewById(R.id.electricGasOil);
        cbGarbageTrash = (CheckBox) rootView.findViewById(R.id.garbageTrash);
        cbHeatingCooling = (CheckBox) rootView.findViewById(R.id.heatingCooling);
        cbHomeless = (CheckBox) rootView.findViewById(R.id.clientHomeless);
        cbPhone = (CheckBox) rootView.findViewById(R.id.phone);
        cbWaterSewer = (CheckBox) rootView.findViewById(R.id.waterSewer);
         
        // initialize spinners
        AGSSISpinner = (Spinner) rootView.findViewById(R.id.AGSSISpinner);
        frequencyEarnedSpinner = (Spinner) rootView.findViewById(R.id.frequencyEarnedSpinner);
        frequencyUnearnedSpinner = (Spinner) rootView.findViewById(R.id.frequencyUnearnedSpinner);
		AGagedSpinner = (Spinner) rootView.findViewById(R.id.AGagedSpinner);
         
        // initialize EditTexts
        etAGsize = (EditText) rootView.findViewById(R.id.etAGSize);
        etChildSupport = (EditText) rootView.findViewById(R.id.etChildSupport);
        etDependentCare = (EditText) rootView.findViewById(R.id.etDependentCare);
        etEarnedIncome = (EditText) rootView.findViewById(R.id.etEarnedIncome);
        etEarnedHoursPerWeek = (EditText) rootView.findViewById(R.id.etEarnedHoursPerWeek);
        etMedicalExpenses = (EditText) rootView.findViewById(R.id.etMedicalExpenses);
        etPropertyInsurance = (EditText) rootView.findViewById(R.id.etPropertyInsurance);
        etPropertyTaxes = (EditText) rootView.findViewById(R.id.etPropertyTaxes);
        etRent = (EditText) rootView.findViewById(R.id.etRent);
        etUnearnedIncome = (EditText) rootView.findViewById(R.id.etUnearnedIncome);
        etUnearnedHoursPerWeek = (EditText) rootView.findViewById(R.id.etUnearnedHoursPerWeek);
         
    }
     
    private void populateFrequencySpinners(View root) {
     
        // create array adapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.frequency, android.R.layout.simple_spinner_dropdown_item);
     
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         
        frequencyEarnedSpinner.setAdapter(adapter);
        frequencyUnearnedSpinner.setAdapter(adapter);
         
        // set default settings of spinners
        frequencyEarnedSpinner.setSelection(4);
        frequencyUnearnedSpinner.setSelection(4);
         
        frequencyEarnedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
         
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // if frequency spinner = hourly, show hourly texts
                TextView tvHours = (TextView) getView().findViewById(R.id.tvEarnedHoursPerWeek);
                if (position == 0) {
                    etEarnedHoursPerWeek.setVisibility(View.VISIBLE);
                    tvHours.setVisibility(View.VISIBLE);
                } else {
                    etEarnedHoursPerWeek.setVisibility(View.GONE);
                    tvHours.setVisibility(View.GONE);
                }
            }
             
            @Override
            public void onNothingSelected(AdapterView<?>arg0){}
        });
         
        frequencyUnearnedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // if frequency spinner = hourly, show hourly texts
                TextView tvHours = (TextView) getView().findViewById(R.id.tvUnearnedHoursPerWeek);
                if (position == 0) {
                    etUnearnedHoursPerWeek.setVisibility(View.VISIBLE);
                    tvHours.setVisibility(View.VISIBLE);
                } else {
                    etUnearnedHoursPerWeek.setVisibility(View.GONE);
                    tvHours.setVisibility(View.GONE);
                }
            }
             
            @Override
            public void onNothingSelected(AdapterView<?>arg0){}
        }); 
    }
 
    private void populateAGSpinners(View rootView) {
        // create array adapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.yes_no, android.R.layout.simple_spinner_dropdown_item);
     
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        AGSSISpinner.setAdapter(adapter);
		AGagedSpinner.setAdapter(adapter);
         
        AGSSISpinner.setSelection(1);
		AGagedSpinner.setSelection(1);
         
        AGagedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
             
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0) {
                    etMedicalExpenses.setEnabled(true);
                } else {
                    etMedicalExpenses.setEnabled(false);
                    etMedicalExpenses.setText("0");
                }
            }
             
            @Override
            public void onNothingSelected(AdapterView<?>arg0){}
             
        });
         
    }
     
    private void initializeClearButton(View rootView){
     
        ImageButton button = (ImageButton) rootView.findViewById(R.id.fsReset);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                resetAll();
            }
         
        });
    }
     
    private void initializeSubmitButton(View rootView) {

        ImageButton button = (ImageButton) rootView.findViewById(R.id.fsSubmit);
        button.setOnClickListener(new View.OnClickListener() {
         
            @Override
            public void onClick(View v) {
                submitPressed();
            }
         
        });
    }
     
    private void resetAll(){
     
        AGagedSpinner.setSelection(1);
        AGSSISpinner.setSelection(1);
        frequencyEarnedSpinner.setSelection(4);
        frequencyUnearnedSpinner.setSelection(4);
         
        etAGsize.setText("");
        etChildSupport.setText("");
        etDependentCare.setText("");
        etEarnedIncome.setText("");
        etEarnedHoursPerWeek.setText("");
        etMedicalExpenses.setText("");
        etPropertyInsurance.setText("");
        etPropertyTaxes.setText("");
        etRent.setText("");
        etUnearnedHoursPerWeek.setText(""); 
         
        cbGarbageTrash.setEnabled(true);
        cbHeatingCooling.setEnabled(true);
        cbHomeless.setEnabled(true);
        cbWaterSewer.setEnabled(true);
        cbPhone.setEnabled(true);
        cbElectricGasOil.setEnabled(true);
         
    }
     
    private void submitPressed() {
     
        // check for valid AG size
        boolean validAGSize = isValidAGSize();
        // toast and finish if invalid AG size
        if (!validAGSize) {
            Toast toast = Toast.makeText(getActivity(), "AG Size must be 1 or larger", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
         
        // check for hours chosen but 0 hours entered
        boolean appropriateHours = areHoursAppropriate();
        if (!appropriateHours) {
            Toast toast = Toast.makeText(getActivity(), "Must have number greater than 0 if calculating incomes at hourly rate", Toast.LENGTH_LONG);
            toast.show();
            return;
        } 
         
        // fill all other variables
        getVariables();
         
        // figure utility allowance
        calculateUtilityAllowance();
     
        // set net income and other standards
        grossIncomeAmount = GROSS_INCOME_LIMIT[AGSize - 1];
         
        if (!checkTotalGrossIncome()) {
            String results = "The total gross income of $" + totalGrossIncome + " exceeds the gross income limit of $" + grossIncomeAmount + " by $" + (totalGrossIncome - grossIncomeAmount);
            ineligibleDialog("Ineligible", results);
			return;
        }
		
		if (!checkNetIncome()) {
			String results = "The total net income of $" + finalNetIncome + " exceeds the net income limit of $" + NET_STANDARD[AGSize - 1] + "by $" + (finalNetIncome - NET_STANDARD[AGSize -1]);
			ineligibleDialog("Ineligible", results);
			return;
		}
		
		/* 	OAC 5101:4-4-27
		*	(c) If the assistance group is subject to the net income standard, compare 
		*	the assistance group's net monthly income to the maximum net monthly income 
		*	standard. If the assistance group's net income is greater than the net 
		*	monthly income standard, the assistance group is ineligible. If the 
		*	assistance group's net income is equal to or less than the net monthly income 
		*	standard, the assistance group is eligible. Multiply the net monthly income by 
		*	thirty per cent.
		*	(d) Round the product up to the next whole dollar if it ends in one cent 
		*	through ninety-nine cents */
		
		finalNetIncome = finalNetIncome < 0 ? 0 : finalNetIncome;
		
		int benefitAmount = FA_ALLOTMENT[AGSize - 1] - (int)Math.ceil(finalNetIncome * 0.3);
		
		/* 	OAC 5101:4-4-27
		*	(f) If the benefit is for a one or two person assistance group and the 
		*	computation results in a benefit of less than the minimum benefit 
		*	allotment, round up to the minimum benefit amount. */
		
		if (isDisabled || isAged || AGSize <= 3) benefitAmount = benefitAmount < MINNIMUM_MONTHLY_ALLOTMENT ? MINNIMUM_MONTHLY_ALLOTMENT : benefitAmount;
        
		String results = "Eligible for food stamps in the amount of $" + benefitAmount + " per month";
		ineligibleDialog("Eligible", results);
		
    }
    
	private boolean checkNetIncome() {
	
		// 5101:4-4-31(S)
		
		/*	5101:4-4-31(S)(2) Earned income deduction: Multiply the total gross 
		*	monthly earned income by twenty per cent and subtract that amount 
		*	from the total gross income. */
		
		finalNetIncome = totalGrossIncome - (int)Math.floor(finalEarnedIncome * 0.2);
		
		/* (3) Standard deduction: Subtract the standard deduction. */
		
		finalNetIncome -= STANDARD_DEDUCTION[AGSize - 1];
		
		/* 	(4) Excess medical deduction: If the AG is entitled to an excess 
		*	medical deduction, determine if total medical expenses exceed 
		*	thirty-five dollars. If so, subtract that portion which exceeds 
		*	thirty-five dollars. */
		
		medicalExpenses = medicalExpenses - EXCESS_MEDICAL_DEDUCTION <= 0 ? 0 : medicalExpenses - EXCESS_MEDICAL_DEDUCTION;
		finalNetIncome -= medicalExpenses;
		
		/* (5) Dependent care deduction: Subtract monthly dependent care expenses, if any. */
		
		finalNetIncome -= dependentCare;
		
		/* 	(6) Legally obligated child support deduction: Subtract the allowable 
		*	monthly child support payments in accordance with rule 5101:4-4-23 of 
		*	the Administrative Code. */
		
		finalNetIncome -= childSupport;
		
		/* 	(7) Standard homeless shelter deduction: Subtract the standard homeless 
		*	shelter deduction amount if any, up to the maximum of one hundred 
		*	forty-three dollars if the AG is homeless and it incurs shelter costs during the month. */
		
		if (cbHomeless.isChecked()) finalNetIncome -= STANDARD_SHELTER_HOMELESS;
		
		/*	(8) Determining any excess shelter cost: Total the allowable shelter 
		*	expenses to determine shelter costs, unless a deduction has been 
		*	subtracted in accordance with paragraph (S)(7) of this rule. Subtract 
		*	from total shelter costs fifty per cent of the AG's monthly income after 
		*	all the above deductions have been subtracted. The remaining amount, if any,
		*	is the excess shelter cost. If there is no excess shelter cost, go to the next step.
		*	(9) Applying any excess shelter cost :Subtract the excess shelter cost 
		*	up to the maximum amount allowed (unless the AG is entitled to the full 
		*	amount of its excess shelter expenses) from the AG's monthly income 
		*	after all other applicable deductions. AGs not subject to the shelter 
		*	limitation shall have the full amount exceeding fifty per cent of their
		*	adjusted income subtracted. The AG's net monthly income has been determined. */
		
		finalNetIncome -= calculateShelterDeduction();
		
		boolean result = finalNetIncome > NET_STANDARD[AGSize - 1] ? false : true;
		
		result = AGSSISpinner.getSelectedItemPosition() == 0 ? true : result;
		
		return result;
	
	}
	
	private int calculateShelterDeduction() {
	
		/*	OAC 5101:4-4-23 Food assistance: deductions from income.
		*	(E) Shelter costs: monthly shelter costs over fifty per cent of the assistance group's income 
		*	after all other deductions contained in this rule have been allowed. If the assistance group 
		*	does not contain an elderly or disabled member, as defined in rule 5101:4-1-03 of the 
		*	Administrative Code, the shelter deduction cannot exceed the maximum shelter deduction provided. 
		*	These assistance groups shall receive an excess shelter deduction for the entire monthly cost 
		*	that exceeds fifty per cent of the assistance group income after all other deductions 
		*	contained in this rule have been allowed. */
		
		int shelterExpenses = utilityAllowance + rent + propertyInsurance + propertyTaxes;
		shelterExpenses -= (finalNetIncome / 2);
		
		if (isAged) {
			shelterExpenses = shelterExpenses <= 0 ? 0 : shelterExpenses;
		} else {
		
			shelterExpenses = shelterExpenses <= 0 ? 0 : shelterExpenses;
			shelterExpenses = shelterExpenses > LIMIT_ON_SHELTER_DEDUCTION ? LIMIT_ON_SHELTER_DEDUCTION : shelterExpenses;
		}
	
		return shelterExpenses;
	
	}
	 
    private void ineligibleDialog(String title, String text){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(text)
            .setPositiveButton("OK", null)
            .setTitle(title);
        // Create the AlertDialog object and return it
        builder.create().show();
    }
     
    private boolean isValidAGSize() {
         
        String test = etAGsize.getText().toString();
        AGSize = test.equals("") ? 0 : Integer.parseInt(test);
        if (AGSize <= 0) {
            return false;
        } else {
            return true;
        }
         
    } 
     
    private boolean areHoursAppropriate() {
     
    unearnedHoursPerWeek = etUnearnedHoursPerWeek.getText().toString().equals("") ? 0 : Double.parseDouble(etChildSupport.getText().toString());
    earnedHoursPerWeek = etEarnedHoursPerWeek.getText().toString().equals("") ? 0 : Double.parseDouble(etEarnedHoursPerWeek.getText().toString());
     
    if (frequencyEarnedSpinner.getSelectedItemPosition() == 0 && earnedHoursPerWeek <= 0) return false;
    if (frequencyUnearnedSpinner.getSelectedItemPosition() == 0 && unearnedHoursPerWeek <= 0) return false;
     
    return true;
     
    }
     
    private void getVariables() {
 
        // get main variables, convert to doubles, then round down and cast as ints
        childSupport = etChildSupport.getText().toString().equals("") ? 0 : (int)Math.floor(Double.parseDouble(etChildSupport.getText().toString()));
        dependentCare = etDependentCare.getText().toString().equals("") ? 0 : (int)Math.floor(Double.parseDouble(etDependentCare.getText().toString()));
        medicalExpenses = etMedicalExpenses.getText().toString().equals("") ? 0 : (int)Math.floor(Double.parseDouble(etMedicalExpenses.getText().toString()));
        propertyInsurance = etPropertyInsurance.getText().toString().equals("") ? 0 : (int)Math.floor(Double.parseDouble(etPropertyInsurance.getText().toString()));
        propertyTaxes = etPropertyTaxes.getText().toString().equals("") ? 0 : (int)Math.floor(Double.parseDouble(etPropertyTaxes.getText().toString()));
        rent = etRent.getText().toString().equals("") ? 0 : (int)Math.floor(Double.parseDouble(etRent.getText().toString()));
        isAged = AGagedSpinner.getSelectedItemPosition() == 0 ? true : false;
		isDisabled = AGSSISpinner.getSelectedItemPosition() == 0 || AGagedSpinner.getSelectedItemPosition() == 0 ? true : false;

		
        // get incomes as doubles or processing later
        unearnedIncome = etUnearnedIncome.getText().toString().equals("") ? 0 : Double.parseDouble(etUnearnedIncome.getText().toString());
        earnedIncome = etEarnedIncome.getText().toString().equals("") ? 0 : Double.parseDouble(etEarnedIncome.getText().toString());
         
        // convert incomes into final incomes that are properly rounded
        switch (frequencyEarnedSpinner.getSelectedItemPosition()) {
         
            case 0: // hourly
                finalEarnedIncome = (int)Math.floor(earnedIncome * earnedHoursPerWeek * 4.3);
                break;
            case 1: // weekly
                finalEarnedIncome = (int)Math.floor(earnedIncome * 4.3);
                break;
            case 2: // every other week
                finalEarnedIncome = (int)Math.floor(earnedIncome * 2.15);
                break;
            case 3: // twice per month
                finalEarnedIncome = (int)Math.floor(earnedIncome * 2);
                break;
            case 4: // monthly
                finalEarnedIncome = (int)Math.floor(earnedIncome);
                break;
            case 5: // annually
                finalEarnedIncome = (int)Math.floor(earnedIncome / 12);
                break;
            default:
                break;
                     
        }
         
        switch (frequencyUnearnedSpinner.getSelectedItemPosition()) {
         
            case 0: // hourly
                finalUnearnedIncome = (int)Math.floor(unearnedIncome * unearnedHoursPerWeek * 4.3);
                break;
            case 1: // weekly
                finalUnearnedIncome = (int)Math.floor(unearnedIncome * 4.3);
                break;
            case 2: // every other week
                finalUnearnedIncome = (int)Math.floor(unearnedIncome * 2.15);
                break;
            case 3: // twice per month
                finalUnearnedIncome = (int)Math.floor(unearnedIncome * 2);
                break;
            case 4: // monthly
                finalUnearnedIncome = (int)Math.floor(unearnedIncome);
                break;
            case 5: // annually
                finalUnearnedIncome = (int)Math.floor(unearnedIncome / 12);
                break;
            default:
                break;
                     
        }
     
        // figure utility allowance
        calculateUtilityAllowance();
     
    }
     
    private boolean checkTotalGrossIncome() {
     
		/* OAC 5101:4-4-31
		(R) Method of calculating gross monthly income
		Except for AGs containing at least one member who is elderly or disabled as defined in rule 5101:4-1-03 of the Administrative Code, or considered categorically eligible, all AGs shall be subject to the gross income eligibility standard for the appropriate AG size. To determine the AG's total gross income, add the gross monthly income earned by all AG members and the total monthly unearned income of all AG members, minus income exclusions. If an AG has income from a farming operation (with gross proceeds of more than one thousand dollars per year) which operates at a loss, see rule 5101:4-6-11 of the Administrative Code. The total gross income is compared to the gross income eligibility standard for the appropriate AG size. If the total gross income is less than the standard, proceed with calculating the adjusted net income as described in paragraph (S) of this rule. If the total gross income is more than the standard, the AG is ineligible for program benefits and the case is either denied or terminated at this point. */
		
        totalGrossIncome = finalEarnedIncome + finalUnearnedIncome;
        boolean results = totalGrossIncome > grossIncomeAmount ? false : true;
		if (isAged || isDisabled) results = true;
        return results;
     
    }
     
    private void initializeHomelessCheck(View rootView) {
     
        cbHomeless.setOnClickListener(new View.OnClickListener() {
         
            @Override
            public void onClick(View v) {
             
                if (cbHomeless.isChecked()) {
                    // set utilities to disabled and unchecked
                    cbGarbageTrash.setEnabled(false);
                    cbGarbageTrash.setChecked(false);
                    cbHeatingCooling.setEnabled(false);
                    cbHeatingCooling.setChecked(false);
                    cbWaterSewer.setEnabled(false);
                    cbWaterSewer.setChecked(false);
                    cbPhone.setEnabled(false);
                    cbPhone.setChecked(false);
                    cbElectricGasOil.setEnabled(false);
                    cbElectricGasOil.setChecked(false);
                     
                    // set rent, taxes, and insurance to 0
                    etPropertyInsurance.setText("0");
                    etPropertyInsurance.setEnabled(false);
                    etPropertyTaxes.setText("0");
                    etPropertyTaxes.setEnabled(false);
                    etRent.setText("0");
                    etRent.setEnabled(false);
                     
                    // Toast explaining changes made
                    Toast toast = Toast.makeText(getActivity(), "Rent, taxes, and property insurance set to 0 as they aren't allowed for homeless applicants", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    cbGarbageTrash.setEnabled(true);
                    cbHeatingCooling.setEnabled(true);
                    cbWaterSewer.setEnabled(true);
                    cbPhone.setEnabled(true);
                    cbElectricGasOil.setEnabled(true);
                     
                    etPropertyInsurance.setEnabled(true);
                    etPropertyTaxes.setEnabled(true);
                    etRent.setEnabled(true);
                }
            }
        });
     
    }
     
    private void calculateUtilityAllowance() {
     
        /* int homeless = cbHomeless.isChecked() ? 1 : 0; */
        int phone = cbPhone.isChecked() ? 2 : 0;
        int heating = cbHeatingCooling.isChecked() ? 4 : 0;
        int electric = cbElectricGasOil.isChecked() ? 8 : 0;
        int water = cbWaterSewer.isChecked() ? 8 : 0;
        int garbage = cbGarbageTrash.isChecked() ? 8 : 0;
         
        int test = phone + heating + electric + water + garbage;
         
        switch (test) {
         
            case 0:
                utilityAllowance = 0;
                break;
            /* case 1:
                utilityAllowance = STANDARD_SHELTER_HOMELESS;
                break; */
            case 2:
                utilityAllowance = STANDARD_TELEPHONE_ALLOWANCE;
                break;
            case 4:
            case 6:
            case 12:
            case 14:
            case 20:
            case 22:
            case 28:
            case 30:
                utilityAllowance = STANDARD_UTILITY_ALLOWANCE;
                break;
            case 8:
                utilityAllowance = SINGLE_UTILITY_ALLOWANCE;
                break;
            default:
                utilityAllowance = LIMITED_UTILITY_ALLOWANCE;
                break;
         
        }
         
     
    }
     
    private void restoreState(Bundle savedInstanceState) {
         
        cbElectricGasOil.setChecked(savedInstanceState.getBoolean("cbElectricGasOil"));
        cbGarbageTrash.setChecked(savedInstanceState.getBoolean("cbGarbageTrash"));
        cbHeatingCooling.setChecked(savedInstanceState.getBoolean("cbHeatingCooling"));
        cbHomeless.setChecked(savedInstanceState.getBoolean("cbHomeless"));
        cbPhone.setChecked(savedInstanceState.getBoolean("cbPhone"));
        cbWaterSewer.setChecked(savedInstanceState.getBoolean("cbWaterSewer"));
         
        etAGsize.setText(savedInstanceState.getString("etAGsize"));
        etChildSupport.setText(savedInstanceState.getString("etChildSupport"));
        etDependentCare.setText(savedInstanceState.getString("etDependentCare"));
        etEarnedIncome.setText(savedInstanceState.getString("etEarnedHoursPerWeek"));
        etEarnedHoursPerWeek.setText(savedInstanceState.getString("etEarnedHoursPerWeek"));
        etMedicalExpenses.setText(savedInstanceState.getString("etMedicalExpenses"));
        etPropertyInsurance.setText(savedInstanceState.getString("etPropertyInsurance"));
        etPropertyTaxes.setText(savedInstanceState.getString("etPropertyTaxes"));
        etRent.setText(savedInstanceState.getString("etRent"));
        etUnearnedHoursPerWeek.setText(savedInstanceState.getString("etUnearnedHoursPerWeek"));
         
        AGagedSpinner.setSelection(savedInstanceState.getInt("AGagedSpinner"));
        AGSSISpinner.setSelection(savedInstanceState.getInt("AGSSISpinner"));
        frequencyEarnedSpinner.setSelection(savedInstanceState.getInt("frequencyEarnedSpinner"));
        frequencyUnearnedSpinner.setSelection(savedInstanceState.getInt("frequencyUnearnedSpinner"));
     
    }
     
    @Override
    public void onSaveInstanceState(Bundle OutState) {
     
        OutState.putBoolean("cbElectricGasOil", cbElectricGasOil.isChecked());
        OutState.putBoolean("cbGarbageTrash", cbGarbageTrash.isChecked());
        OutState.putBoolean("cbHeatingCooling", cbHeatingCooling.isChecked());
        OutState.putBoolean("cbHomeless", cbHomeless.isChecked());
        OutState.putBoolean("cbPhone", cbPhone.isChecked());
        OutState.putBoolean("cbWaterSewer", cbWaterSewer.isChecked());
         
        OutState.putString("etAGsize", etAGsize.getText().toString());
        OutState.putString("etChildSupport", etChildSupport.getText().toString());
        OutState.putString("etDependentCare", etDependentCare.getText().toString());
        OutState.putString("etEarnedIncome", etEarnedIncome.getText().toString());
        OutState.putString("etEarnedHoursPerWeek", etEarnedHoursPerWeek.getText().toString());
        OutState.putString("etMedicalExpenses", etMedicalExpenses.getText().toString());
        OutState.putString("etPropertyInsurance", etPropertyInsurance.getText().toString());
        OutState.putString("etPropertyTaxes", etPropertyTaxes.getText().toString());
        OutState.putString("etRent", etRent.getText().toString());
        OutState.putString("etUnearnedHoursPerWeek", etUnearnedHoursPerWeek.getText().toString());
         
        OutState.putInt("AGagedSpinner", AGagedSpinner.getSelectedItemPosition());
        OutState.putInt("AGSSISpinner", AGSSISpinner.getSelectedItemPosition());
        OutState.putInt("frequencyEarnedSpinner", frequencyEarnedSpinner.getSelectedItemPosition());
        OutState.putInt("frequencyUnearnedSpinner", frequencyUnearnedSpinner.getSelectedItemPosition());        
     
    }
}
