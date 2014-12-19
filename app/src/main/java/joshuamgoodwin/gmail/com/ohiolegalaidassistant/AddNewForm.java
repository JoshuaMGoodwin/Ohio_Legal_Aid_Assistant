package joshuamgoodwin.gmail.com.ohiolegalaidassistant;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.Toast;
import android.content.Intent;
import android.os.Bundle;
import android.content.ActivityNotFoundException;
import joshuamgoodwin.gmail.com.ohiolegalaidassistant.FormsDAO;

public class AddNewForm extends Fragment {

	private Button add_button;

	private EditText new_form_name;

    private ImageButton add, clear;

	private static final int PICKFILE_RESULT_CODE = 100;

	private String filePath;
	private String fileName;

	private TextView file_name_text;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.add_new_forms_layout, container, false);
        getViews(rootView);
		InitializeGetFileButton();
        InitializeAddButton();
        return rootView;
	}

	private void getViews(View v) {
		new_form_name = (EditText) v.findViewById(R.id.new_form_name);
		add_button = (Button) v.findViewById(R.id.add_button);
		file_name_text = (TextView) v.findViewById(R.id.file_name_text);
        add = (ImageButton) v.findViewById(R.id.submit);
        clear = (ImageButton) v.findViewById(R.id.clear);
	}

	private void InitializeGetFileButton() {
		add_button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					getFile();
				}
			});
	}

	private void getFile() {
		try {
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("file/");
			startActivityForResult(intent, PICKFILE_RESULT_CODE);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(getActivity(), "You do not have a file explorer installed on your phone", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case PICKFILE_RESULT_CODE:
				if (resultCode== Activity.RESULT_OK) {
					filePath = data.getData().getPath();
					fileName = data.getData().getLastPathSegment();
					file_name_text.setText("File: " + fileName);
				}
				break;
			default:
				break;
		}
	}

    private void InitializeAddButton() {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FormsDAO dao = new FormsDAO(getActivity());
                int extensionPosition = filePath.lastIndexOf(".");
                String ext = filePath.substring(extensionPosition + 1);
                file_name_text.setText(ext);
                dao.addNewForm(new_form_name.getText().toString(), filePath, ext);
                ((MainActivity) getActivity()).setDrawer();
            }
        });
    }
}
