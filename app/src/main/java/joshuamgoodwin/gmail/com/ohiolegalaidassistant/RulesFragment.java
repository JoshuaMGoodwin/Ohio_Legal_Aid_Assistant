package joshuamgoodwin.gmail.com.ohiolegalaidassistant;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.MotionEvent;

/**
 * Created by Goodwin on 11/10/2014.
 */
public class RulesFragment extends Fragment /*implements SimpleGestureListener*/{

    private boolean firstTime = true;

    private int narrowTopicSelected;

    private LinearLayout ll;

    private Spinner broadTopics;
    private Spinner narrowTopics;

    private String ruleSet;

    private TextView details;
    ;

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
        ll = (LinearLayout) rootView.findViewById(R.id.rules_linear_layout);
        Bundle bundle = getArguments();
        ruleSet = bundle.getString("ruleSet");

    }

    private void initializeSpinners(){

        setBroadTopicsSpinner();
        setNarrowTopicsSpinner(0);

    }

    private void setNarrowTopicsSpinner(int number) {

        String[] narrowTopicString = getResources().getStringArray(getResources().getIdentifier(ruleSet + "_" + number, "array", "joshuamgoodwin.gmail.com.ohiolegalaidassistant"));
        ArrayAdapter<CharSequence> narrowRulesAdapter = new ArrayAdapter<CharSequence>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, narrowTopicString);
        narrowRulesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        narrowTopics.setAdapter(narrowRulesAdapter);

        narrowTopicSelected = number + 1;

    }

    private void setBroadTopicsSpinner() {

        String[] broadTopicArray = getResources().getStringArray(getResources().getIdentifier(ruleSet, "array", "joshuamgoodwin.gmail.com.ohiolegalaidassistant"));
        ArrayAdapter<CharSequence> broadRulesAdapter = new ArrayAdapter<CharSequence>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, broadTopicArray);
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


                if (firstTime) {
                    firstTime = false;
                } else {
                    ll.removeAllViews();
                }

            String detail = position < 9 ? "0" + Integer.toString(position + 1) : Integer.toString(position + 1);
            String string = ruleSet + "_" + Integer.toString(narrowTopicSelected) + detail;
            String[] ruleArray = getResources().getStringArray(getResources().getIdentifier(string, "array", "joshuamgoodwin.gmail.com.ohiolegalaidassistant"));

            for (int i = 0; i < ruleArray.length; i++) {

                String wholeString = ruleArray[i];
                int paddingMultiplier = Integer.parseInt(wholeString.substring(0, 1));
                String actualString = wholeString.substring(1);
                TextView tv = new TextView(getActivity());
                // setPadding(left, top, right, bottom)
                tv.setPadding(5 * (paddingMultiplier * 5), 5, 5, 5);
                tv.setText(Html.fromHtml(actualString));
                ll.addView(tv);

            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    });

}

}