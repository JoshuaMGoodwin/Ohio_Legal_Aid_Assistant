package joshuamgoodwin.gmail.com.ohiolegalaidassistant;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.*;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.internal.view.menu.ActionMenuItemView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.*;
import android.widget.TextView;
import android.widget.Spinner;
import android.graphics.Typeface;
import android.app.*;


public class MainActivity extends ActionBarActivity {

    private double results = 0;
    private int lastExpanded = -1;
	private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    private Fragment callingFragment;

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
        mDrawerList = (ExpandableListView) findViewById(R.id.left_drawer_list);


        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        setDrawer();
	
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

                if (lastExpanded != -1) {
                    if (lastExpanded != groupPosition) mDrawerList.collapseGroup(lastExpanded);}
                lastExpanded = groupPosition;

				}
			});
			
		// Listview Group collasped listener
		mDrawerList.setOnGroupCollapseListener(new OnGroupCollapseListener() {

				@Override
				public void onGroupCollapse(int groupPosition) {

				}
			});
			
		TextView about = (TextView) findViewById(R.id.about_drawer);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectItem("About", 0, 0, "");
            }
        });

        TextView license = (TextView) findViewById(R.id.license_drawer);
        license.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectItem("License", 0, 0, "");
            }
        });
			
			
		//	mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  // host Activity 
                mDrawerLayout,         // DrawerLayout object 
                //R.drawable.ic_drawer,  // nav drawer image to replace 'Up' caret
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
            selectItem("Welcome", 0, 0, "");
        }/* } else {
            setTitle(savedInstanceState.getCharSequence("mTitle"));
        }*/
        // changelog on start
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        boolean test = prefs.getBoolean("fifth", true);

        if (test) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage(Html.fromHtml(getString(R.string.changes)))
                    .setPositiveButton("OK", null)
                    .setTitle("What's New!");
            AlertDialog dialog = builder.create();
            dialog.show();

            // make sure it only runs first time
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("fifth", false);
            editor.commit();
        }
        getSupportActionBar().setTitle(R.string.app_name);
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

    private void selectItem(String groupName,  int groupPosition, int childPosition, String childName) {
        // update the main content by replacing fragments
        Fragment fragmentName = new Welcome();
        ActionMenuItemView search = (ActionMenuItemView) findViewById(R.id.action_search);
        //search.setVisibility(View.INVISIBLE);
        String tag = "WELCOME";
        Bundle bundle = new Bundle();
        boolean welcomeVisible = false;
        // Welcome welcome = (Welcome)getFragmentManager().findFragmentByTag("WELCOME");
        // if (welcome.isVisible()) welcomeVisible = true;
        if (groupName.equals("Calculators")) {
            if (childName.equals(getString(R.string.OWF_calculator))) {
                fragmentName = new OwfCalculator();
                tag = "OWF_CALCULATOR";
            } else if (childName.equals(getString(R.string.federal_poverty))) {
                fragmentName = new FplCalculatorController();
                tag = "FPL_CALCULATOR";
            } else if (childName.equals(getString(R.string.food_stamps))) {
                fragmentName = new FsCalculator();
                tag = "FS_CALCULATOR";
            } else if (childName.equals(getString(R.string.child_support))) {
                String url = "https://www.lawhelpinteractive.org/login_form?template_id=template.2014-06-05.4419790386&set_language=en";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            } else if (childName.equals(getString(R.string.APR))) {
                fragmentName = new APRCalculator();
                tag = "APR";
            } else if (childName.equals(getString(R.string.ACACalculator))) {
                fragmentName = new ACACalculator();
                tag = "ACA";
            } else if (childName.equals("Garnishment")) {
                fragmentName = new GarnishmentCalculator();
                tag = "GARNISHMENT";
            } else {
                fragmentName = new Welcome();
            }
        } else if (groupName.equals("Court Dockets")) {
            tag = "WEBVIEW";
            if (childName.equals(getString(R.string.edit_courts))) {
                fragmentName = new ShowCourtsFragment();
            } else {
                CourtSitesDAO dao = new CourtSitesDAO(this);

                String address = dao.addressFromName(childName);
                bundle = new Bundle();
                bundle.putString("address", address);
                fragmentName = new WebViewFragment();
                fragmentName.setArguments(bundle);

            }
        } else if (groupName.equals("Rules")) {
            fragmentName = new RulesFragment();
            //search.setVisibility(View.VISIBLE);
            tag = "RULES";
            if (childName.equals("Federal Rules of Evidence")) {
                bundle.putString("ruleSet", "fre_by_rule");
            }
            if (childName.equals("Federal Rules of Civil Procedure")) {
                bundle.putString("ruleSet", "federal_rules_cp");
            }
            if (childName.equals("Ohio Rules of Civil Procedure")) {
                bundle.putString("ruleSet", "ohio_rules_cp");
            }
            if (childName.equals("Ohio Rules of Evidence")) {
                bundle.putString("ruleSet", "ohio_rules_evidence");
            }
            if (childName.equals("Ohio Rules of Juvenile Procedure")) {
                bundle.putString("ruleSet", "ohio_rules_juvenile");
            }
            if (childName.equals("Ohio Rules of Appellate Procedure")) {
                bundle.putString("ruleSet", "ohio_appellate_rules");
            }
            fragmentName.setArguments(bundle);
        } else if (groupName.equals("About")) {
            fragmentName = new AboutFragment();
            tag = "ABOUT";
        } else if (groupName.equals("License")) {
            fragmentName = new LicenseFragment();
            tag = "LICENSE";
        } else if (groupName.equals("Forms")) {
            if (childName.equals("Exemption List")) {
                CopyAssets("exemption.pdf");
            } else if (childName.equals("Public Benefits Standards Help Sheet")) {
                CopyAssets("standards_help_sheet.pdf");
            } else if (childName.equals("Edit/Add Forms")) {
                fragmentName = new ShowFormsFragment();
                tag = "ADD FORMS";
            } else if (childName.equals("Medicaid Standards Help Sheet")) {
                CopyAssets("medicaid_help_sheet.pdf");
            } else {
                FormsDAO formsDao = new FormsDAO(this);
                String fileName = formsDao.addressFromName(childName);
                File file = new File(fileName);
                AssetManager am = getAssets();
                InputStream in = null;
                OutputStream out = null;
                try {
                    in = am.open(fileName);
                    out = openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);
                    copyFile(in, out);
                    in.close();
                    in = null;
                    out.close();
                    out = null;
                } catch (Exception e) {
                    Log.e("tag", e.getMessage());
                }

                Intent intent = new Intent(Intent.ACTION_VIEW);
                String ext = formsDao.extensionFromName(childName);
				String type;
				if (ext.equals("doc") || ext.equals("docx")) {
					// Word document
					type = "application/msword";
				} else if(ext.equals("pdf")) {
					// PDF file
					type = "application/pdf";
				} else if(ext.equals("ppt") || ext.equals("pptx")) {
					// Powerpoint file
					type = "application/vnd.ms-powerpoint";
				} else if(ext.equals("xls") || ext.equals("xlsx")) {
					// Excel file
					type = "application/vnd.ms-excel";
				} else if(ext.equals("zip") || ext.equals("rar")) {
					// WAV audio file
					type = "application/x-wav";
				} else if(ext.equals("rtf")) {
					// RTF file
					type = "application/rtf";
				} else if(ext.equals("wav") || ext.equals("mp3")) {
					// WAV audio file
					type = "audio/x-wav";
				} else if(ext.equals("gif")) {
					// GIF file
					type = "image/gif";
				} else if(ext.equals("jpg") || ext.equals("jpeg") || ext.equals("png")) {
					// JPG file
					type = "image/jpeg";
				} else if(ext.equals("txt")) {
					// Text file
					type = "text/plain";
				} else if(ext.equals("3gp") || ext.equals("mpg") || ext.equals("mpeg") || ext.equals("mpe") || ext.equals("mp4") || ext.equals("avi")) {
					// Video files
					type = "video/*";
				} else {
					//if you want you can also define the intent type for any other file

					//additionally use else clause below, to manage other unknown extensions
					//in this case, Android will show all applications installed on the device
					//so you can choose which application to use
					type = "*/*";
				}
				
                intent.setDataAndType(Uri.parse("file://" + fileName), type);
                startActivity(intent);
            }
        } else if (groupName.equals("Negotiations")) {
            if (childName.equals(getString(R.string.add_negotiations))) {
                fragmentName = new ShowNegotiationsFragment();
                tag = "Negotiations";
            }
        }
        else {
			fragmentName = new Welcome();
		}
		
		setFragment(fragmentName, tag);
		
        // update selected item and title, then close the drawer
		LinearLayout mDrawerContent = (LinearLayout) findViewById(R.id.left_drawer);
        mDrawerList.setItemChecked(childPosition, true);
        if (groupName.equals("About")) {
            setTitle("About");
        } else if (groupName.equals("License")) {
            setTitle("License");
        } else {
            setTitle(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition));
        }
        mDrawerLayout.closeDrawer(mDrawerContent);
		
    }
    public void OpenCourtEditor(Bundle bundle) {
        Fragment fragment = new AddCourtWebsiteFragment();
        fragment.setArguments(bundle);
        setFragment(fragment, "COURTS");
    }

    public void OpenFormEditor(Bundle bundle) {
        Fragment fragment = new AddNewForm();
        fragment.setArguments(bundle);
        setFragment(fragment, "FORMS");
    }

    public void AddNegotiation() {
        Fragment fragment = new AddNegotiations();
        setFragment(fragment, "Negotiations");
    }

    private void CopyAssets(String fileName) {
        AssetManager am = getAssets();
        InputStream in = null;
        OutputStream out = null;
        File file =new File(getFilesDir(), fileName);
        try {
            in = am.open(fileName);
            out = openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);
            copyFile(in, out);
            in.close();
            in = null;
            out.close();
            out = null;
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + getFilesDir() + "/" + fileName), "application/pdf");
        startActivity(intent);
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }
	
	private void setFragment(Fragment fragmentName, String tag){
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();

        ft.replace(R.id.content_frame, fragmentName, tag);

		// if (welcomeVisible()) ft.addToBackStack(null);
		ft.commit();
	}
	
	private boolean welcomeVisible(){
		Welcome welcome = (Welcome) getSupportFragmentManager().findFragmentByTag("WELCOME");
		if (welcome != null && welcome.isVisible()){
			return true;
		} else {
			return false;
		}
	}
	
	private boolean webviewVisible(){
		WebViewFragment webFragment = (WebViewFragment) getSupportFragmentManager().findFragmentByTag("WEBVIEW");
		if (webFragment != null && webFragment.isVisible()){
			return true;
		} else {
			return false;
		}
	}
    public void setDrawer(){
        createListsForDrawer();
        // set up the drawer's list view with items and click listener
        ExpandableListAdapter adapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        mDrawerList.setAdapter(adapter);
    }
	
	@Override
	public void onBackPressed(){
		
		if (welcomeVisible() == false){ 
			Fragment fragmentName = new Welcome();
			String tag = "WELCOME";
			setFragment(fragmentName, tag);
            getSupportActionBar().setTitle(R.string.app_name);
		} else {
            super.onBackPressed();
        }
	}

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);

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

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);
        return true;
    }*/

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
		 courtDockets.add(getString(R.string.edit_courts));
		 
		 /*// add data to the cm list
		 List<String> cmSites = new ArrayList<String>();
		 CaseManagementDAO cmDAO = new CaseManagementDAO(this);
		 cmSites = cmDAO.caseManagementNamesList();
		 cmSites.add(getString(R.string.add_cm));
		 cmSites.add(getString(R.string.edit_cm));*/
		 
		 // add data to the forms list
		 List<String> forms = new ArrayList<String>();
		 FormsDAO formsDao = new FormsDAO(this);
         forms = formsDao.formNamesList();
         forms.add("Exemption List");
         forms.add("Medicaid Standards Help Sheet");
         forms.add("Public Benefits Standards Help Sheet");
         forms.add("Edit/Add Forms");

         // Adding data for rules
         List<String> rules = new ArrayList<String>();
         String[] rulesList = getResources().getStringArray(R.array.Rules);
         for (int i = 0; i < rulesList.length; i++){
             rules.add(rulesList[i]);
         }

         // adding negotiations data
/*         List<String> negotiations = new ArrayList<String>();
         NegotiationsDAO dao3 = new NegotiationsDAO(this);
         negotiations = dao3.getNegotiationsForDrawer();
         negotiations.add(getString(R.string.add_negotiations));*/


		 listDataChild.put(listDataHeader.get(0), calculators); // add calculators
		 //listDataChild.put(listDataHeader.get(1), cmSites);// add cm sites
		 listDataChild.put(listDataHeader.get(1), courtDockets); // add court dockets
         listDataChild.put(listDataHeader.get(2), rules); // add rules data
         listDataChild.put(listDataHeader.get(3), forms); // add forms data
         //listDataChild.put(listDataHeader.get(4), negotiations);
	 }

  /*  @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putCharSequence("mTitle", mTitle);
    }*/

	 

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
