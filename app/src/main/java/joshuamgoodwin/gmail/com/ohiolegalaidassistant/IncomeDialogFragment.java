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
import android.widget.Toast;

public class IncomeDialogFragment extends DialogFragment {

    private OnUpdateIncomeListener callback;

    public interface OnUpdateIncomeListener {
        public void onIncomeSubmit(String result);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            callback = (OnUpdateIncomeListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling Fragment must implement OnUpdateIncomeListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View v = inflater.inflate(R.layout.dialog_frequency_layout, null);
        Spinner s = (Spinner) v.findViewById(R.id.frequency_spinner);
        final EditText hours = (EditText) v.findViewById(R.id.hours);
        setSpinner(s, hours);
        Bundle bundle = getArguments();
        builder.setView(v)
                .setTitle(bundle.getString("title"))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Grab the text from the input
                        // TODO spin out to methods and implement error checking in case edittexts are blank both income and hours
                        EditText income = (EditText) v.findViewById(R.id.base_amount);
                        Spinner spinner = (Spinner) v.findViewById(R.id.frequency_spinner);
                        EditText hours = (EditText) v.findViewById(R.id.hours);
                        if (income.getText().toString().equals("")) {
                            callback.onIncomeSubmit("0.00");
                        }
                        String result = "";
                        switch (spinner.getSelectedItemPosition()) {
                            case 0:
                                // hourly
                                result = String.valueOf(Double.parseDouble(hours.getText().toString()) * Double.parseDouble(income.getText().toString()) * 52);
                                break;
                            case 1:
                                // weekly
                                result = String.valueOf(Double.parseDouble(income.getText().toString()) * 52);
                                break;
                            case 2:
                                // every other week
                                result = String.valueOf(Double.parseDouble(income.getText().toString()) * 26);
                                break;
                            case 3:
                                // twice per month
                                result = String.valueOf(Double.parseDouble(income.getText().toString()) * 24);
                                break;
                            case 4:
                                // monthly
                                result = String.valueOf(Double.parseDouble(income.getText().toString()) * 12);
                                break;
                            case 5:
                                // annual
                                result = String.valueOf(Double.parseDouble(income.getText().toString()));
                                break;
                        }
                        callback.onIncomeSubmit(result);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        IncomeDialogFragment.this.getDialog().cancel();
                    }
                });
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