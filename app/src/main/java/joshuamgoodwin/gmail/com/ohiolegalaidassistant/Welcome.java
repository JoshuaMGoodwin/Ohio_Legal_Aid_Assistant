package joshuamgoodwin.gmail.com.ohiolegalaidassistant;

import android.support.v4.app.Fragment;
import android.view.*;
import android.os.*;

public class Welcome extends Fragment
{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.welcome, container, false);
		return rootView;
	}
	
	
	
}
