package joshuamgoodwin.gmail.com.ohiolegalaidassistant;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.app.AlertDialog;
import android.app.AlertDialog.*;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.MotionEvent;
import android.widget.*;

/**
 * Created by Goodwin on 11/10/2014.
 */
public class RulesFragment extends Fragment {
    private GestureDetector gesture;
    private boolean firstTime = true;

	private ImageButton buttonSearch;
	
    private int broadTopicSelected;
    private int narrowPosition = 0;
	private int ruleSelected;

    private LinearLayout ll;

	private ScrollView sv;
	
    private Spinner broadTopics;
    private Spinner narrowTopics;

    private String ruleSet;
    private String TAG = "OhioLegalAidAssistant:";

    String[] broadTopicArray;
    String[] narrowTopicString;

    private TextView details;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.rules_fragment_layout, container, false);
        getViews(rootView);
        initializeSpinners();
        setBroadTopicsListener();
        setNarrowTopicsListener();
		setSVOnTouchListener();
		setSearchListener();
		return rootView;
    }


    private void getViews(View rootView) {

        broadTopics = (Spinner) rootView.findViewById(R.id.broad_topic_spinner);
        narrowTopics = (Spinner) rootView.findViewById(R.id.narrow_topic_spinner);
        ll = (LinearLayout) rootView.findViewById(R.id.rules_linear_layout);
        sv = (ScrollView) rootView.findViewById(R.id.rules_sv);
		buttonSearch = (ImageButton) rootView.findViewById(R.id.rules_search);
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
                            int bt = broadTopics.getSelectedItemPosition();

							String[] test = getResources().getStringArray(getResources()
								.getIdentifier(ruleSet + "_" + (broadTopicSelected - 2),
								"array", "joshuamgoodwin.gmail.com.ohiolegalaidassistant"));
                            narrowPosition = test.length - 1;
                            broadTopics.setSelection(bt - 1, true);
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
				narrowTopics.setSelection(narrowPosition, false);
				broadTopicSelected = position + 1;
                narrowPosition = 0;
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
	
	private void setSearchListener() {
		buttonSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final EditText search = new EditText(getActivity());
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("Search")
					.setMessage("Enter your search term(s)")
					.setView(search)
					.setPositiveButton("Search", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.dismiss();
							runSearch(search.getText().toString());
							/// Toast.makeText(getActivity(), "your search term is " + search.getText().toString(), Toast.LENGTH_LONG).show();
						}
					})
					.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.dismiss();
						}
					});
				AlertDialog alert = builder.create();
				alert.show();
			}
		});
	}
	
	private void runSearch(String incomingSearch) {
		String search = incomingSearch;
		String[] masterBroadList = getResources().getStringArray(getResources().getIdentifier(ruleSet, "array", "joshuamgoodwin.gmail.com.ohiolegalaidassistant"));	
		ArrayList<String[]> finalList = new ArrayList<String[]>();

		// goes through all of the broad list
		for (int i = 0; i < masterBroadList.length; i++) {
			String[] narrowList = getResources().getStringArray(getResources().getIdentifier(ruleSet + "_" + i, "array", "joshuamgoodwin.gmail.com.ohiolegalaidassistant"));
			// go through narrow list
			for (int j = 0; j < narrowList.length; j++) {
				String ruleNumber = j < 9 ? "0" + Integer.toString(j + 1) : Integer.toString(j + 1);
				// this is the array for each rule
				String[] ruleList = getResources().getStringArray(getResources().getIdentifier(ruleSet + "_" + (i + 1) + ruleNumber, "array", "joshuamgoodwin.gmail.com.ohiolegalaidassistant"));
				// go through each line of the rule
				boolean answerFound = false;
				// only return one example per rule
				while (answerFound == false) {
					for (int k = 0; k < ruleList.length; k++) {
						String test = ruleList[k];
						if (test.indexOf(search) >= 0) {
							// the search term is in the string
							String[] string = {ruleList[0], ruleList[k]};
							finalList.add(string);
						} else {
							// the search term is not in the string
						}
					}
					answerFound = true;
				}	
			}
		}
		if (finalList.size() == 0) {
			Toast.makeText(getActivity(), "Your search returned no results.", Toast.LENGTH_LONG).show();
		} else {
			searchList(finalList);
		}
	}
	
	private void searchList(ArrayList<String[]> incomingList) {
		ArrayList<String[]> list = incomingList;
		// remove all views from rules area
		ll.removeAllViews();
		ListView lv = new ListView(getActivity());
		SearchAdapter adapter = new SearchAdapter(getActivity(), list);
		lv.setAdapter(adapter);
		
		lv.setLayoutParams(new ViewGroup.LayoutParams(
							   ViewGroup.LayoutParams.MATCH_PARENT,
							   ViewGroup.LayoutParams.MATCH_PARENT));
		ll.addView(lv);
	}
	
	public class SearchAdapter extends ArrayAdapter<String[]> {

		// List context
		private final Context context;
		// List values
		private final ArrayList<String[]> list;

		public SearchAdapter(Context context, ArrayList<String[]> inList) {
			super(context, R.layout.rule_search, inList);
			this.context = context;
			this.list = inList;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View rowView = inflater.inflate(R.layout.rule_search, parent, false);

			TextView ruleName = (TextView) rowView.findViewById(R.id.search_rule);
			TextView searchText = (TextView) rowView.findViewById(R.id.search_text);
			String[] string = list.get(position);
			ruleName.setText(string[0]);
			searchText.setText(string[1]);


			return rowView;
		}
	}
	
}



