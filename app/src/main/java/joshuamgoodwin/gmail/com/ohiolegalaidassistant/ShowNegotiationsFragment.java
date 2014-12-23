package joshuamgoodwin.gmail.com.ohiolegalaidassistant;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Goodwin on 12/21/2014.
 */
public class ShowNegotiationsFragment extends ListFragment {

    private int positionSelected;
    private NegotiationsDAO dao;
    private String clientName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.court_view_layout, container, false);
        initializeEditButton(rootView);
        initializeDeleteButton(rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        dao = new NegotiationsDAO(getActivity());

        setListAdapter(new NegotiationsListAdapter(getActivity(), dao.getNegotiationsForEdit()));

    }

    private void initializeEditButton(View v) {
        ImageButton button = (ImageButton) v.findViewById(R.id.edit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        v.setSelected(true);
        if (position == 0) {
            clientName = getString(R.string.add_negotiations);
            ((MainActivity)getActivity()).AddNegotiation();
        } else {
            Negotiations negotiations = (Negotiations) getListAdapter().getItem(position);
            clientName = negotiations.getClientName();
        }
        Toast.makeText(getActivity(), "Client name: " + clientName, Toast.LENGTH_LONG).show();

    }

    private void initializeDeleteButton (View v) {
        ImageButton button = (ImageButton) v.findViewById(R.id.delete);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });
    }

    public class NegotiationsListAdapter extends ArrayAdapter<Negotiations> {

        // List context
        private final Context context;
        // List values
        private final List<Negotiations> negotiationsList;

        public NegotiationsListAdapter(Context context, List<Negotiations> negotiationsList) {
            super(context, R.layout.court_names_list, negotiationsList);
            this.context = context;
            this.negotiationsList = negotiationsList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.court_names_list, parent, false);

            TextView negotiationsName = (TextView) rowView.findViewById(R.id.court_name);
            negotiationsName.setText(negotiationsList.get(position).getClientName());


            return rowView;
        }
    }
}
