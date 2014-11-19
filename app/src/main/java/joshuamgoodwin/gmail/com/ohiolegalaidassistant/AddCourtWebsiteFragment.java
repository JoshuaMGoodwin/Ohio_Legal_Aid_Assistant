package joshuamgoodwin.gmail.com.ohiolegalaidassistant;

import android.app.Fragment;
import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Button.*;
import android.widget.Toast;
import android.view.View;
import android.view.View.*;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.database.Cursor;

public class AddCourtWebsiteFragment extends Fragment {

	private Button buttonClear;
	private Button buttonAdd;
	
	private CourtSitesDAO dao;

	private EditText etWebsiteName;
	private EditText etWebsiteURL;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.new_court_site_layout, container, false);
		initializeLayoutElements(rootView);
		dao = new CourtSitesDAO(getActivity());
		initializeAddEntry(rootView);
		initializeClear(rootView);
		return rootView;
	}
	
	private void initializeLayoutElements(View rootView) {
	
		// initialize the EditTexts and Buttons
		etWebsiteName = (EditText) rootView.findViewById(R.id.etWebsiteName);
		etWebsiteURL = (EditText) rootView.findViewById(R.id.etWebsiteURL);
		buttonAdd = (Button) rootView.findViewById(R.id.addEntry);
		buttonClear = (Button) rootView.findViewById(R.id.clear);
	
	}

	private void initializeAddEntry(View rootView) {
	
		// initialize addEntry button and set onclick listener
		buttonAdd.setOnClickListener (new OnClickListener() {
		
			@Override
			public void onClick(View v) {
			
				String courtName = etWebsiteName.getText().toString();
				String courtAddress = etWebsiteURL.getText().toString();
				dao.addNewCourt(courtName, courtAddress);
				resetAll();
               ((MainActivity)getActivity()).setDrawer();
				Toast toast = Toast.makeText(getActivity(), "New Court website successfully added", Toast.LENGTH_LONG);
				toast.show();
			
			}
		});
	}

	private void initializeClear(View rootView) {
	
		buttonClear.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				resetAll();
			}
		});
	}
	
	private void resetAll() {
		etWebsiteName.setText("");
		etWebsiteURL.setText("");		
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		dao.close();
	}
}
