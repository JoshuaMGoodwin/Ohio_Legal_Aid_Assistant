package joshuamgoodwin.gmail.com.ohiolegalaidassistant;

import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.widget.EditText;

import android.widget.ImageButton;
import android.widget.Toast;
import android.view.View;
import android.view.View.*;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.database.Cursor;

public class AddCourtWebsiteFragment extends Fragment {

	private ImageButton buttonClear;
	private ImageButton buttonAdd;

    private boolean newCourt;

	private CourtSitesDAO dao;

	private EditText etWebsiteName;
	private EditText etWebsiteURL;

    private String id;

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
		buttonAdd = (ImageButton) rootView.findViewById(R.id.addEntry);
		buttonClear = (ImageButton) rootView.findViewById(R.id.clear);
        Bundle bundle = getArguments();
        newCourt = bundle.getBoolean("newCourt", false);
        if (bundle.getBoolean("newCourt") == false) {
            id = bundle.getString("id");
            etWebsiteName.setText(bundle.getString("name"));
            etWebsiteURL.setText(bundle.getString("address"));
        }
	
	}

	private void initializeAddEntry(View rootView) {
	
		// initialize addEntry button and set onclick listener
		buttonAdd.setOnClickListener (new OnClickListener() {
		
			@Override
			public void onClick(View v) {
            String courtName = etWebsiteName.getText().toString();
            String courtAddress = etWebsiteURL.getText().toString();
            String text;
            if (newCourt) {
                dao.addNewCourt(courtName, courtAddress);
                resetAll();
                text = courtName + " successfully added!";
            } else {
                String identifier = id;
                dao.editCourt(courtName, courtAddress, id);
                resetAll();
                text = courtName + " has been successfully updated!";
            }
                ((MainActivity) getActivity()).setDrawer();
                Toast toast = Toast.makeText(getActivity(), text, Toast.LENGTH_LONG);
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
