package joshuamgoodwin.gmail.com.ohiolegalaidassistant;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class RulesFragment extends Fragment {
    private GestureDetector gesture;
    private boolean firstTime = true;
	
    private int broadTopicSelected;
    private int narrowPosition = 0;
	private int ruleSelected;

    private LinearLayout ll;
    private LinearLayout innerLL;

	private ScrollView sv;
	
    private Spinner broadTopics;
    private Spinner narrowTopics;

    private String ruleSet;
    private String theSearch = "";
    private String TAG = "OhioLegalAidAssistant:";

    String[] broadTopicArray;
    String[] narrowTopicString;

    private TextView details;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.rules_fragment_layout, container, false);
        getViews(rootView);
        initializeSpinners();
        setBroadTopicsListener();
        setNarrowTopicsListener();
		return rootView;
    }


    private void getViews(View rootView) {

        broadTopics = (Spinner) rootView.findViewById(R.id.broad_topic_spinner);
        narrowTopics = (Spinner) rootView.findViewById(R.id.narrow_topic_spinner);
        ll = (LinearLayout) rootView.findViewById(R.id.rules_linear_layout);
        Bundle bundle = getArguments();
        ruleSet = bundle.getString("ruleSet");

    }
	
	@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
       	super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.action_bar_menu, menu);
        
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

    private void showRules(int position, String search) {

        ll.removeAllViews();
            firstTime = false;
            sv = new ScrollView(getActivity());
            sv.setId(R.id.scroll);
            sv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            innerLL = new LinearLayout(getActivity());
            innerLL.setId(R.id.innerLL);
        innerLL.setOrientation(LinearLayout.VERTICAL);
            innerLL.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            ll.addView(sv);
            sv.addView(innerLL);
            setSVOnTouchListener();

        String detail = position < 9 ? "0" + Integer.toString(position + 1) : Integer.toString(position + 1);
        String string = ruleSet + "_" + Integer.toString(broadTopicSelected) + detail;
        String[] ruleArray = getResources().getStringArray(getResources().getIdentifier(string, "array", "joshuamgoodwin.gmail.com.ohiolegalaidassistant"));

        for (int i = 0; i < ruleArray.length; i++) {
            String wholeString = ruleArray[i];
            int paddingMultiplier = Integer.parseInt(wholeString.substring(0, 1));
            String actualString = wholeString.substring(1);
            if (!theSearch.equals("")) {
                if (actualString.contains(theSearch)) {
                    String newString = "<font color='#FF0000'><i>" + theSearch + "</i></font>";
                    actualString = actualString.replaceAll(theSearch, newString);
                }
                if (actualString.contains(theSearch.toUpperCase())) {
                    String newString = "<font color='#FF0000'><i>" + theSearch.toUpperCase() + "</i></font>";
                    actualString = actualString.replaceAll(theSearch.toUpperCase(), newString);
                }
                if (actualString.contains(theSearch.substring(0, 1).toUpperCase() + theSearch.substring(1))){
                    String newString = "<font color='#FF0000'><i>" + theSearch.toUpperCase() + "</i></font>";
                    actualString = actualString.replaceAll(theSearch.substring(0, 1).toUpperCase() + theSearch.substring(1), newString);
                }
            }
            TextView tv = new TextView(getActivity());
            // setPadding(left, top, right, bottom)
            tv.setPadding(5 * (paddingMultiplier * 5), 5, 5, 5);
            tv.setText(Html.fromHtml(actualString));
            innerLL.addView(tv);
        }
        ruleSelected = position;
        theSearch = "";

    }

    private void setNarrowTopicsListener() {

        narrowTopics.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showRules(position, "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case R.id.action_search:
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
				return true;
			default:
				return true;
			
		}
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
				while (!answerFound) {
					for (int k = 0; k < ruleList.length; k++) {
						String test = ruleList[k];
						String changedCase = search.substring(0, 1).toUpperCase() + search.substring(1);
						if (test.indexOf(search) >= 0 || test.indexOf(changedCase) >= 0 || test.indexOf(search.toUpperCase()) >= 0) {
							// the search term is in the string
							// ruletitle, ruleline, broadtopic, narrowtopic, search string
							String[] string = {ruleList[0].substring(1), ruleList[k].substring(1), Integer.toString(i), Integer.toString(j), search};
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

		// remove all views from rules area
		ll.removeAllViews();
		ListView lv = new ListView(getActivity());
		final SearchAdapter adapter = new SearchAdapter(getActivity(), incomingList);
		lv.setAdapter(adapter);
		
		lv.setLayoutParams(new ViewGroup.LayoutParams(
							   ViewGroup.LayoutParams.MATCH_PARENT,
							   ViewGroup.LayoutParams.MATCH_PARENT));
		lv.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> av, View v, int pos, long id){
				String[] data = adapter.getRow(pos);
				narrowPosition = Integer.parseInt(data[3]);
				broadTopics.setSelection(Integer.parseInt(data[2]), true);
                theSearch = data[4];


			}
		});
		ll.addView(lv);
        firstTime = true;
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
			ruleName.setText(Html.fromHtml(string[0]));
			searchText.setText(Html.fromHtml(string[1]));

			return rowView;
		}
		
		public String[] getRow(int pos){
			return list.get(pos);
		}
	}
	
}



