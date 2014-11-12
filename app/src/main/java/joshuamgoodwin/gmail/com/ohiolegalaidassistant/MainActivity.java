package joshuamgoodwin.gmail.com.ohiolegalaidassistant;

import java.util.Locale;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.*;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Typeface;

import joshuamgoodwin.gmail.com.*;

public class MainActivity extends Activity {
    
	private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
	
	private DrawerLayout mDrawerLayout;
    private ExpandableListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
	
	private boolean firstCalled;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mNavigationDrawerItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawerlayout);
		
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ExpandableListView) findViewById(R.id.left_drawer);
		createListsForDrawer();

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        ExpandableListAdapter adapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);		
		mDrawerList.setAdapter(adapter);
	
		// Listview on child click listener
        mDrawerList.setOnChildClickListener(new OnChildClickListener() {

				@Override
				public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
					String child = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
					//String child = list.get(childPosition);
					selectItem(listDataHeader.get(groupPosition), groupPosition, childPosition, child);
					return false;
				}
			});
			
		// Listview Group expanded listener
		mDrawerList.setOnGroupExpandListener(new OnGroupExpandListener() {

				@Override
				public void onGroupExpand(int groupPosition) {
					Toast.makeText(getApplicationContext(),
								   listDataHeader.get(groupPosition) + " Expanded",
								   Toast.LENGTH_SHORT).show();
				}
			});
			
		// Listview Group collasped listener
		mDrawerList.setOnGroupCollapseListener(new OnGroupCollapseListener() {

				@Override
				public void onGroupCollapse(int groupPosition) {
					Toast.makeText(getApplicationContext(),
								   listDataHeader.get(groupPosition) + " Collapsed",
								   Toast.LENGTH_SHORT).show();

				}
			});

			
			
		//	mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  // host Activity 
                mDrawerLayout,         // DrawerLayout object 
                R.drawable.ic_drawer,  // nav drawer image to replace 'Up' caret 
                R.string.drawer_open,  // "open drawer" description for accessibility 
                R.string.drawer_close  // "close drawer" description for accessibility 
                ) {
            public void onDrawerClosed(View view) {
                
            }

            public void onDrawerOpened(View drawerView) {
                
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            Fragment fragmentName = new Welcome();
			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction ft = fragmentManager.beginTransaction();
			ft.replace(R.id.content_frame, fragmentName, "WELCOME");
			// ft.addToBackStack(null);
			ft.commit();
        }
		
    }

	
    private void selectItem(String groupName,  int groupPosition, int childPosition, String childName) {
        // update the main content by replacing fragments
		Fragment fragmentName = new Welcome();
		String tag = "WELCOME";
        Bundle bundle = new Bundle();
		boolean welcomeVisible = false;
		// Welcome welcome = (Welcome)getFragmentManager().findFragmentByTag("WELCOME");
		// if (welcome.isVisible()) welcomeVisible = true;
		if (groupName.equals("Calculators")) {
			switch (childPosition) {
				case 0:
					fragmentName = new OwfCalculator();
					tag = "OWF_CALCULATOR";
					break;
				case 1:
					fragmentName = new FplCalculator();
					tag = "FPL_CALCULATOR";
					break;
				case 2:
					fragmentName = new FsCalculator();
					tag = "FS_CALCULATOR";
					break;
				case 3:
					fragmentName = new WebViewFragment();
					tag = "WEBVIEW";
					bundle = new Bundle();
					bundle.putString("address", getString(R.string.child_support_address));
					fragmentName.setArguments(bundle);
					break;
				case 4:
					fragmentName = new APRCalculator();
					tag = "APR";
					break;
				default:
					fragmentName = new Welcome();
			} 
		} else if (groupName.equals("Court Dockets")){
			tag = "WEBVIEW";
			if (childName.equals(getString(R.string.add_court))){
				fragmentName = new AddCourtWebsiteFragment();
			} else if (childName.equals(getString(R.string.edit_courts))){
				fragmentName = new ShowCourtsFragment();
			} else {
					CourtSitesDAO dao = new CourtSitesDAO(this);
					
					String address = dao.addressFromName(childName);
					Toast toast = Toast.makeText(this, address + "is the address", Toast.LENGTH_LONG);
					toast.show();
					bundle = new Bundle();
					bundle.putString("address", address);
					fragmentName = new WebViewFragment();
					fragmentName.setArguments(bundle);
				
			}
		} else if (groupName.equals("Rules")) {
            fragmentName = new RulesFragment();
            tag = "RULES";
            if (childName.equals("Federal Rules of Evidence")) {
                bundle.putString("ruleSet", "fre_by_rule");
            }
            if (childName.equals("Ohio Rules of Civil Procedure")) {
                bundle.putString("ruleSet", "ohio_rules_cp");
            }
            fragmentName.setArguments(bundle);
        }
		
		setFragment(fragmentName, tag);
		
        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(childPosition, true);
        setTitle(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition));
        mDrawerLayout.closeDrawer(mDrawerList);
		
    }
	
	private void setFragment(Fragment fragmentName, String tag){
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();

        ft.replace(R.id.content_frame, fragmentName, tag);

		// if (welcomeVisible()) ft.addToBackStack(null);
		ft.commit();
	}
	
	private boolean welcomeVisible(){
		Welcome welcome = (Welcome) getFragmentManager().findFragmentByTag("WELCOME");
		if (welcome != null && welcome.isVisible()){
			return true;
		} else {
			return false;
		}
	}
	
	private boolean webviewVisible(){
		WebViewFragment webFragment = (WebViewFragment) getFragmentManager().findFragmentByTag("WEBVIEW");
		if (webFragment != null && webFragment.isVisible()){
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void onBackPressed(){
		
		if (welcomeVisible() == false){ 
			Fragment fragmentName = new Welcome();
			String tag = "WELCOME";
			setFragment(fragmentName, tag);
		} else {
            super.onBackPressed();
        }
	}

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
	
	@Override
public boolean onOptionsItemSelected(MenuItem item) {
     // Pass the event to ActionBarDrawerToggle, if it returns
     // true, then it has handled the app icon touch event
     if (mDrawerToggle.onOptionsItemSelected(item)) {
         return true;
     }
     // Handle your other action bar items...

     return super.onOptionsItemSelected(item);
}

	 private void createListsForDrawer(){
		 
		 listDataHeader = new ArrayList<String>();
		 listDataChild = new HashMap<String, List<String>>();

		 // put header data into an arraylist
		 String[] headers = getResources().getStringArray(R.array.DrawerHeadings);
		 for (int i = 0; i < headers.length; i++) {
			 listDataHeader.add(headers[i]);
		 }
		 
		 // Adding data for calculators
		 List<String> calculators = new ArrayList<String>();
		 String[] childList1 = getResources().getStringArray(R.array.Calculators);
		 for (int i = 0; i < childList1.length; i++){
			 calculators.add(childList1[i]);
		 }	
		 
		 // add data to the court dockets
		 List<String> courtDockets = new ArrayList<String>();
		 CourtSitesDAO dao = new CourtSitesDAO(this);
		 courtDockets = dao.courtNamesList();
		 courtDockets.add(getString(R.string.add_court));
		 courtDockets.add(getString(R.string.edit_courts));
		 
		 // add data to the cm list
		 List<String> cmSites = new ArrayList<String>();
		 CaseManagementDAO cmDAO = new CaseManagementDAO(this);
		 cmSites = cmDAO.caseManagementNamesList();
		 cmSites.add(getString(R.string.add_cm));
		 cmSites.add(getString(R.string.edit_cm));
		 
		 // add data to the forms list
		 List<String> forms = new ArrayList<String>();
		 forms.add("Add form (under construction)");

         // Adding data for rules
         List<String> rules = new ArrayList<String>();
         String[] rulesList = getResources().getStringArray(R.array.Rules);
         for (int i = 0; i < rulesList.length; i++){
             rules.add(rulesList[i]);
         }

		 listDataChild.put(listDataHeader.get(0), calculators); // add calculators
		 listDataChild.put(listDataHeader.get(1), cmSites);// add cm sites
		 listDataChild.put(listDataHeader.get(2), courtDockets); // add court dockets
		 listDataChild.put(listDataHeader.get(3), forms); // add forms data
         listDataChild.put(listDataHeader.get(4), rules); // add rules data
	 }
	 

	public class ExpandableListAdapter extends BaseExpandableListAdapter {

		private Context _context;
		private List<String> _listDataHeader; // header titles
		// child data in format of header title, child title
		private HashMap<String, List<String>> _listDataChild;

		public ExpandableListAdapter(Context context, List<String> listDataHeader,
									 HashMap<String, List<String>> listChildData) {
			this._context = context;
			this._listDataHeader = listDataHeader;
			this._listDataChild = listChildData;
		}

		@Override
		public Object getChild(int groupPosition, int childPosititon) {
			return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public View getChildView(int groupPosition, final int childPosition,
								 boolean isLastChild, View convertView, ViewGroup parent) {

			final String childText = (String) getChild(groupPosition, childPosition);

			if (convertView == null) {
				LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = infalInflater.inflate(R.layout.drawer_list_item, null);
			}

			TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

			txtListChild.setText(childText);
			return convertView;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			return this._listDataHeader.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return this._listDataHeader.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
								 View convertView, ViewGroup parent) {
			String headerTitle = (String) getGroup(groupPosition);
			if (convertView == null) {
				LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = infalInflater.inflate(R.layout.drawer_list_group, null);
			}

			TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
			lblListHeader.setTypeface(null, Typeface.BOLD);
			lblListHeader.setText(headerTitle);

			return convertView;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
	}
}
