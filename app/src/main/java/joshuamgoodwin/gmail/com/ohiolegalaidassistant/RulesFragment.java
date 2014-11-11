package joshuamgoodwin.gmail.com.ohiolegalaidassistant;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Goodwin on 11/10/2014.
 */
public class RulesFragment extends Fragment {

    private int narrowTopicSelected;

    private Spinner broadTopics;
    private Spinner narrowTopics;
    private TextView details;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.rules_fragment_layout, container, false);
        getViews(rootView);
        initializeSpinners();
        setBroadTopicsListener();
        setNarrowTopicsListener();
        return rootView;
    }

    private void getViews(View rootView){

        broadTopics = (Spinner) rootView.findViewById(R.id.broad_topic_spinner);
        narrowTopics = (Spinner) rootView.findViewById(R.id.narrow_topic_spinner);
        details = (TextView) rootView.findViewById(R.id.rules_text);

    }

    private void initializeSpinners(){

        setBroadTopicsSpinner();
        setNarrowTopicsSpinner(0);

    }

    private void setNarrowTopicsSpinner(int number) {

        String[] narrowTopicString = getResources().getStringArray(getResources().getIdentifier("fre_by_rule_" + number, "array", "joshuamgoodwin.gmail.com.ohiolegalaidassistant"));
        ArrayAdapter<CharSequence> narrowRulesAdapter = new ArrayAdapter<CharSequence>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, narrowTopicString);
        narrowRulesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        narrowTopics.setAdapter(narrowRulesAdapter);

        narrowTopicSelected = number + 1;

    }

    private void setBroadTopicsSpinner() {

        ArrayAdapter<CharSequence> broadRulesAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.fre_by_rule, android.R.layout.simple_spinner_dropdown_item);
        broadRulesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        broadTopics.setAdapter(broadRulesAdapter);

    }

    private void setBroadTopicsListener(){

        broadTopics.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setNarrowTopicsSpinner(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setNarrowTopicsListener() {

        narrowTopics.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String detail = position < 10 ? "0" + Integer.toString(position + 1) : Integer.toString(position + 1);
                String string = "fre_by_rule_" + Integer.toString(narrowTopicSelected) + detail;
                Toast toast = Toast.makeText(getActivity(), string, Toast.LENGTH_LONG);
                toast.show();
                details.setText(Html.fromHtml(getResources().getString(getResources().getIdentifier(string, "string", "joshuamgoodwin.gmail.com.ohiolegalaidassistant"))));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

}
