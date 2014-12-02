package joshuamgoodwin.gmail.com.ohiolegalaidassistant;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.*;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.ListFragment;

import joshuamgoodwin.gmail.com.ohiolegalaidassistant.CourtSites;
import joshuamgoodwin.gmail.com.ohiolegalaidassistant.CourtSitesDAO;
import java.security.*;
import android.content.*;

public class ShowCourtsFragment extends ListFragment {

	CourtSitesDAO dao;
    String address = "";
	String name = "";
    int positionSelected = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.court_view_layout, container, false);
        initializeEditButton(rootView);
        initializeDeleteButton(rootView);
        return rootView;
    }

	private void initializeEditButton(View view){
        ImageButton edit = (ImageButton) view.findViewById(R.id.edit);
        edit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String address;
                String name;
                String identified;
                boolean newCourt = false;

                // nothing selected
                if (positionSelected == -1) {
                    return;
                } else if (positionSelected == 0) {
                    // add new court
                    address = "";
                    name = "";
                    identified = "";
                    newCourt = true;
                } else if (positionSelected > 0) {
                    CourtSites courtSites = (CourtSites) getListAdapter().getItem(positionSelected);
                    String[] court = dao.getCourt(courtSites.getId());
                    // court[number, name, address]
                    address = court[2];
                    name = court[1];
                    identified = court[0];
                } else {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("address", address);
                bundle.putString("name", name);
                bundle.putString("id", identified);
                bundle.putBoolean("newCourt", newCourt);
                ((MainActivity)getActivity()).OpenCourtEditor(bundle);
            }
        });
    }

    private void initializeDeleteButton(View view) {
        ImageButton button = (ImageButton) view.findViewById(R.id.delete);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (address.equals("") || address.equals(getString(R.string.AddCourt))) {
                    return;
                } else {
					CourtSites courtSites = (CourtSites) getListAdapter().getItem(positionSelected);
                    final String[] court = dao.getCourt(courtSites.getId());
					name = court[1];
                    // TODO: build alert dialog to confirm that user wants to delete court
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					builder.setMessage("Are you sure you want to delete " + name + " ?")
							.setTitle("Warning")
							.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.dismiss();
									dao.deleteCourt(Integer.parseInt(court[0]));
									setListAdapter(new CourtListAdapter(getActivity(), dao.getCourtsForEdit()));
									((MainActivity)getActivity()).setDrawer();
									Toast toast = Toast.makeText(getActivity(), name + " was deleted.", Toast.LENGTH_LONG);
									toast.show();
								}
							})
							.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.dismiss();		
								}
							});
					AlertDialog dialog = builder.create();
					dialog.show();
                }
            }
        });
    }

    @Override
	public void onActivityCreated(Bundle savedInstanceState) {
	
		super.onActivityCreated(savedInstanceState);
		
		dao = new CourtSitesDAO(getActivity());
		
		setListAdapter(new CourtListAdapter(getActivity(), dao.getCourtsForEdit()));
	
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
	
		v.setSelected(true);
        if (position == 0) {
            address = getString(R.string.AddCourt);
        } else {
            CourtSites courtSites = (CourtSites) getListAdapter().getItem(position);
            address = dao.showAddress(courtSites.getId());
        }
		positionSelected = position;

	}

	public class CourtListAdapter extends ArrayAdapter<CourtSites> {

		// List context
		private final Context context;
		// List values
		private final List<CourtSites> courtList;
		
		public CourtListAdapter(Context context, List<CourtSites> courtList) {
			super(context, R.layout.court_names_list, courtList);
			this.context = context;
			this.courtList = courtList;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			 
			View rowView = inflater.inflate(R.layout.court_names_list, parent, false);
			 
			TextView courtName = (TextView) rowView.findViewById(R.id.court_name);
			courtName.setText(courtList.get(position).getCourtName());

			 
			return rowView;
		}
	}
}
