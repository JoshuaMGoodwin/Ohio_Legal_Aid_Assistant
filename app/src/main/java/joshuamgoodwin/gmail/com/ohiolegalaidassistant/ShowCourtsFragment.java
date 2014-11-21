package joshuamgoodwin.gmail.com.ohiolegalaidassistant;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.*;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.ListFragment;

import joshuamgoodwin.gmail.com.ohiolegalaidassistant.CourtSites;
import joshuamgoodwin.gmail.com.ohiolegalaidassistant.CourtSitesDAO;
import java.security.*;

public class ShowCourtsFragment extends ListFragment {

	CourtSitesDAO dao;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
	
		super.onActivityCreated(savedInstanceState);
		
		dao = new CourtSitesDAO(getActivity());
		
		setListAdapter(new CourtListAdapter(getActivity(), dao.getCourts()));
	
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
	
		CourtSites courtSites = (CourtSites)getListAdapter().getItem(position);
		//int selected = courtSites.getId();
        String address = dao.showAddress(courtSites.getId());
        dao.deleteCourt(courtSites.getId());
		setListAdapter(new CourtListAdapter(getActivity(), dao.getCourts()));
        ((MainActivity)getActivity()).setDrawer();
		Toast toast = Toast.makeText(getActivity(), address + " was deleted", Toast.LENGTH_LONG);
		toast.show();
	
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
