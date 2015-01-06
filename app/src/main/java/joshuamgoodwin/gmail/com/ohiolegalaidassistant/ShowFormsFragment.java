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

import joshuamgoodwin.gmail.com.ohiolegalaidassistant.Forms;
import joshuamgoodwin.gmail.com.ohiolegalaidassistant.FormsDAO;
import java.security.*;
import android.content.*;

public class ShowFormsFragment extends ListFragment {

    FormsDAO dao;
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
                String path;
                String name;
                String identified;
                String extension;
                boolean newForm = false;

                // nothing selected
                if (positionSelected == -1) {
                    return;
                } else if (positionSelected == 0) {
                    // add new form
                    path = "";
                    name = "";
                    identified = "";
                    extension = "";

                    newForm = true;
                } else if (positionSelected > 0) {
                    Forms forms = (Forms) getListAdapter().getItem(positionSelected);
                    String[] form = dao.getForm(forms.getId());
                    // form[number, name, path, ext]
                    path = form[2];
                    name = form[1];
                    identified = form[0];
                    extension = form[3];
                } else {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("path", path);
                bundle.putString("name", name);
                bundle.putString("id", identified);
                bundle.putBoolean("newForm", newForm);
                bundle.putString("extension", extension);
                ((MainActivity)getActivity()).OpenFormEditor(bundle);
            }
        });
    }

    private void initializeDeleteButton(View view) {
        ImageButton button = (ImageButton) view.findViewById(R.id.clear);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (address.equals("") || address.equals(getString(R.string.AddCourt))) {
                    return;
                } else {
                   Forms forms = (Forms) getListAdapter().getItem(positionSelected);
                    final String[] form = dao.getForm(forms.getId());
                    name = form[1];
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Are you sure you want to delete " + name + " ?")
                            .setTitle("Warning")
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    dao.deleteForm(Integer.parseInt(form[0]));
                                    setListAdapter(new FormListAdapter(getActivity(), dao.getFormsForEdit()));
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

        dao = new FormsDAO(getActivity());

        setListAdapter(new FormListAdapter(getActivity(), dao.getFormsForEdit()));

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        v.setSelected(true);
        if (position == 0) {
            address = getString(R.string.AddCourt);
        } else {
            Forms forms = (Forms) getListAdapter().getItem(position);
            address = dao.showFileName(forms.getId());
        }
        positionSelected = position;

    }

    public class FormListAdapter extends ArrayAdapter<Forms> {

        // List context
        private final Context context;
        // List values
        private final List<Forms> formsList;

        public FormListAdapter(Context context, List<Forms> formsList) {
            super(context, R.layout.court_names_list, formsList);
            this.context = context;
            this.formsList = formsList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.court_names_list, parent, false);

            TextView formName = (TextView) rowView.findViewById(R.id.court_name);
            formName.setText(formsList.get(position).getFormName());


            return rowView;
        }
    }
}

