package joshuamgoodwin.gmail.com.ohiolegalaidassistant;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Goodwin on 1/14/2015.
 */
public class LicenseFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.license_layout, container, false);
        return rootView;
    }
}
