package joshuamgoodwin.gmail.com.ohiolegalaidassistant;

import android.support.v4.app.DialogFragment;
import android.app.Activity;
import android.app.*;
import android.os.*;
import android.view.*;
import android.content.*;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

public class IncomeDialogFragment extends DialogFragment {
	
	public interface IncomeDialogListener {
		public void onDialogPositiveClick(DialogFragment dialog);
		public void onDialogNegativeClick(DialogFragment dialog);
	}
	
	IncomeDialogListener mListener;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (IncomeDialogListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement IncomeDialogListener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();

		final View v = inflater.inflate(R.layout.dialog_frequency_layout, null);
		Spinner s = (Spinner)v.findViewById(R.id.frequency_spinner);
		EditText hours = (EditText)v.findViewById(R.id.hours);
        setSpinner(s, hours);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int id){
				mListener.onDialogPositiveClick(IncomeDialogFragment.this);
			}
		})
			.setNegativeButton("Cancel",new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int id){
					mListener.onDialogNegativeClick(IncomeDialogFragment.this);
				}
		})
			.setTitle("Income Information")
            .setView(v);
		return builder.create();
	}
	
	private void setSpinner(Spinner s, final EditText hours) {
		final Spinner frequencySpinner = s;

        // create array adapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
																			 R.array.frequency, android.R.layout.simple_spinner_dropdown_item);

        // set layout for when dropdown shown
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // apply adapter to spinner
        frequencySpinner.setAdapter(adapter);

        // set spinner default to monthly
        frequencySpinner.setSelection(4);

        frequencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (frequencySpinner.getSelectedItemPosition() == 0) {
                    hours.setVisibility(View.VISIBLE);
                } else {
                    hours.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

	}

	
	
}
