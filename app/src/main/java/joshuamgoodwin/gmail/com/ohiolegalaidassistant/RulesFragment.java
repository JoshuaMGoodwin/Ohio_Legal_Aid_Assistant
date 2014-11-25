package joshuamgoodwin.gmail.com.ohiolegalaidassistant;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.MotionEvent;

/**
 * Created by Goodwin on 11/10/2014.
 */
public class RulesFragment extends Fragment {
    private GestureDetector gesture;
    private boolean firstTime = true;

    private int broadTopicSelected;
	private int ruleSelected;

    private LinearLayout ll;

	private ScrollView sv;
	
    private Spinner broadTopics;
    private Spinner narrowTopics;

    private String ruleSet;
    String[] broadTopicArray;
    String[] narrowTopicString;

    private TextView details;
    ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.rules_fragment_layout, container, false);
        getViews(rootView);
        initializeSpinners();
        setBroadTopicsListener();
        setNarrowTopicsListener();
		setSVOnTouchListener();
		return rootView;
    }


    private void getViews(View rootView) {

        broadTopics = (Spinner) rootView.findViewById(R.id.broad_topic_spinner);
        narrowTopics = (Spinner) rootView.findViewById(R.id.narrow_topic_spinner);
        ll = (LinearLayout) rootView.findViewById(R.id.rules_linear_layout);
        sv = (ScrollView) rootView.findViewById(R.id.rules_sv);
        Bundle bundle = getArguments();
        ruleSet = bundle.getString("ruleSet");

    }

	private void setSVOnTouchListener(){
		gesture = new GestureDetector(getActivity(),
			new SimpleOnGestureListener() {

				@Override
				public boolean onDown(MotionEvent e) {
					return true;
				}

				@Override
				public boolean onFling(MotionEvent e1, MotionEvent e2, float VelocityX, float VelocityY) {

					final int MIN_DISTANCE = 120;
					final int MAX_VARIATION = 200;
					final int MIN_VEL = 200;
					// swipe with a lot of up in it, too slow, or not far enough so not a valid swipe
					if (Math.abs(e1.getY() - e2.getY()) > MAX_VARIATION || Math.abs(VelocityX) < MIN_VEL || Math.abs(e1.getX() - e2.getX()) < MIN_DISTANCE)
						return super.onFling(e1, e2, VelocityX, VelocityY);

					// swipe right to left so increase rule number
					if ((e1.getX() - e2.getX()) > 0) {
						if (ruleSelected + 1 < narrowTopicString.length) {
							narrowTopics.setSelection(ruleSelected + 1);
						} else if (broadTopics.getSelectedItemPosition() + 1 < broadTopicArray.length) {

							broadTopics.setSelection(broadTopics.getSelectedItemPosition() + 1);
						}
						return true;
					} else {
						// swipe left to right so decrease rule number
						if (ruleSelected > 0) {
							narrowTopics.setSelection(ruleSelected - 1);
						} else if (broadTopics.getSelectedItemPosition() > 0) {
							broadTopics.setSelection(broadTopicSelected - 2);
							//String[] test = getResources().getStringArray(getResources()
							//	.getIdentifier(ruleSet + "_" + (broadTopicSelected - 2),
							//	"array", "joshuagoodwin.gmail.com.ohiolegalaidassistant"));
							//Toast.makeText(getActivity(), "test.length:" + test.length, Toast.LENGTH_LONG).show();
							narrowTopics.setSelection(1, false);
								
						}
						return true;
					}
				}
			});
        sv.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					return gesture.onTouchEvent(event);
				}
			});
	}
	
    private void initializeSpinners() {

        setBroadTopicsSpinner();
        setNarrowTopicsSpinner(0);

    }

    private void setNarrowTopicsSpinner(int number) {

        narrowTopicString = getResources().getStringArray(getResources().getIdentifier(ruleSet + "_" + number, "array", "joshuamgoodwin.gmail.com.ohiolegalaidassistant"));
        ArrayAdapter<CharSequence> narrowRulesAdapter = new ArrayAdapter<CharSequence>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, narrowTopicString);
        narrowRulesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        narrowTopics.setAdapter(narrowRulesAdapter);


    }


    private void setBroadTopicsSpinner() {

        broadTopicArray = getResources().getStringArray(getResources().getIdentifier(ruleSet, "array", "joshuamgoodwin.gmail.com.ohiolegalaidassistant"));
        ArrayAdapter<CharSequence> broadRulesAdapter = new ArrayAdapter<CharSequence>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, broadTopicArray);
        broadRulesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        broadTopics.setAdapter(broadRulesAdapter);
		

    }

    private void setBroadTopicsListener() {

        broadTopics.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setNarrowTopicsSpinner(position);
				narrowTopics.setSelection(0, false);
				broadTopicSelected = position + 1;
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
                String string = ruleSet + "_" + Integer.toString(broadTopicSelected) + detail;
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
				
				ruleSelected = position;
				//Toast.makeText(getActivity(), "rule selected = " + ruleSelected, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}



