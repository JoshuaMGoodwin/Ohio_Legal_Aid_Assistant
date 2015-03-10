package joshuamgoodwin.gmail.com.ohiolegalaidassistant;

import android.app.DialogFragment;
import android.app.Activity;
import android.app.*;
import android.os.*;
import android.view.*;
import android.content.*;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

public class IncomeDialogFragment extends DialogFragment
{
	
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
		builder.setView(inflater.inflate(R.layout.dialog_frequency_layout, null));
		final View v = inflater.inflate(R.layout.dialog_frequency_layout, null);
		Spinner s = (Spinner)v.findViewById(R.id.frequency_spinner);
		setSpinner(s);
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
			.setTitle("Income Information");
		return builder.create();
	}
	
	private void setSpinner(Spinner s) {
		Spinner versionSpinner = s;

        // create array adapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
																			 R.array.frequency, android.R.layout.simple_spinner_dropdown_item);

        // set layout for when dropdown shown
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // apply adapter to spinner
        versionSpinner.setAdapter(adapter);

        // set spinner default to current year
        versionSpinner.setSelection(4);
	}

	
	
}
