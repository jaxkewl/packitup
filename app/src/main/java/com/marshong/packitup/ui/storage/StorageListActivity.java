package com.marshong.packitup.ui.storage;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.marshong.packitup.R;
import com.marshong.packitup.data.DBContract;
import com.marshong.packitup.ui.container.AddContainerActivity;
import com.marshong.packitup.ui.dbdev.DBDevActivity;
import com.marshong.packitup.ui.item.AddItemActivity;
import com.marshong.packitup.ui.location.AddLocationActivity;
import com.marshong.packitup.ui.settings.SettingsActivity;


public class StorageListActivity extends ActionBarActivity {

    public final static String TAG = StorageListActivity.class.getSimpleName();

    public final static String fontLoc = "fonts/PermanentMarker.ttf";

    private SectionsPagerAdapter mAppSectionsPagerAdapter;
    private ViewPager mViewPager;

    Typeface tf;

    //the following keeps track of which group of items to display
    private int mSelectedLocationId = 1;    //this comes from the location table
    private int mSelectedLocationPos = 1;    //this comes from the nav drawer item position
    private String mSelectedLocName;
    private int mLocationCount = 0;

    //these class fields are related to the nav drawer
    private DrawerLayout mDrawerLayout;
    private LinearLayout mDrawerRL;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private String[] mNavDrawerItems = {"Item 1", "Item 2", "Item 3", "Item 4"};
    private SimpleCursorAdapter mNavDrawAdapter;

    //these fields are for passing to the view pager
    private int mContainerCount;
    private String[] mContainerNames;
    private String[] mContainerIds;

    private void populateContainerInfo() {
        Log.d(TAG, "populateContainerInfo with locationId:" + mSelectedLocationId);

        String selection = "CONTAINER_LOCATION=?";
        String[] selectionArgs = {Integer.toString(mSelectedLocationId)};

        //query the location table for all the location names.
        Cursor data = getContentResolver().query(DBContract.Container.CONTENT_CONTAINER_URI,
                DBContract.Container.CONTAINER_PROJECTION,
                selection,
                selectionArgs,
                null);
        mContainerCount = data.getCount();
        mContainerNames = new String[data.getCount()];
        mContainerIds = new String[data.getCount()];
        Log.d(TAG, "container count: " + data.getCount());
        int i = 0;
        if (data.moveToFirst()) {
            do {
                String container = data.getString(data.getColumnIndex(DBContract.Container.CONTAINER_NAME));
                String containerId = data.getString(data.getColumnIndex(DBContract.Container.CONTAINER_ID));
                Log.d(TAG, "found container name: " + container + " with Id: " + containerId);
                mContainerNames[i] = container;
                mContainerIds[i] = containerId;
                i++;
            } while (data.moveToNext());
        }
        data.close();
    }


    private void populateNavDrawerLocations() {
        Log.d(TAG, "populateNavDrawerLocations...");

        //query the location table for all the location names.
        Cursor data = getContentResolver().query(DBContract.Location.CONTENT_LOCATION_URI,
                DBContract.Location.LOCATION_PROJECTION,
                null,
                null,
                null);

        mNavDrawerItems = new String[data.getCount()];
        mLocationCount = data.getCount();
        Log.d(TAG, "location count: " + data.getCount());
        int i = 0;
        if (data.moveToFirst()) {
            do {
                String loc = data.getString(data.getColumnIndex(DBContract.Location.LOCATION_NAME));
                Log.d(TAG, "drawer item: " + loc);
                mNavDrawerItems[i] = loc;
                i++;
            } while (data.moveToNext());
        }
        data.close();

        //reset the adapter after querying for new location
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mNavDrawerItems));

    }


    private void initNavDrawerView() {

        // DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        // RelativeLayout container that holds the Nav drawer menu
        mDrawerRL = (LinearLayout) findViewById(R.id.drawerRL);

        // Nav drawer ListView
        mDrawerList = (ListView) findViewById(R.id.drawerList);

        // Set the adapter for drawer list
        //ArrayAdapter navDrawerAdapter = new CustomNavDrawerAdapter(this, android.R.layout.simple_list_item_1, mNavDrawerItems, fontLoc);
        ArrayAdapter navDrawerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mNavDrawerItems);
        mDrawerList.setAdapter(navDrawerAdapter);
        mDrawerList.setAdapter(mNavDrawAdapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

    }


    /**
     * Initialize the navigation drawer
     */
    private void initDrawer() {


        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, null, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                getSupportActionBar().setTitle("Hello World");
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                getSupportActionBar().setTitle("Bye World");

            }

        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        //GOTCHA: need to syncstate in order for hamburger symbol to show up
        mDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        populateNavDrawerLocations();
    }


    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            selectItem(position);
        }
    }


    /**
     * Handle drawer item selection.
     * 1. Set the name of the location the user selected
     * 2. Find the ID of the Location using the location name from the location table
     * by using the LOCATION URI.
     */
    private void selectItem(int position) {
        Log.d(TAG, "selectItem: " + position);
        mSelectedLocName = mNavDrawerItems[position];
        Log.d(TAG, "selected location name: " + mSelectedLocName);
        //Toast.makeText(getActivity(), "Selected " + mSelectedLocName, Toast.LENGTH_LONG).show();

        // Highlight the selected drawer item
        mDrawerList.setItemChecked(position, true);

        // Close drawer
        mDrawerLayout.closeDrawer(mDrawerRL);

        //we need to get the unique location ID from the content provider
        String selection = DBContract.Location.LOCATION_NAME + "=?";    //this is selection statement i.e. "name=? AND email=?"
        String[] selectionArgs = {mSelectedLocName};    //this is the list of arguments that replace the question mark in the selection statement

        Cursor c = getContentResolver().query(DBContract.Location.CONTENT_LOCATION_URI,
                DBContract.Location.LOCATION_PROJECTION,
                selection,
                selectionArgs,
                null);

        if (c.moveToFirst()) {
            mSelectedLocationId = c.getInt(c.getColumnIndexOrThrow(DBContract.Location.LOCATION_ID));
            Toast.makeText(this, "Selected " + mSelectedLocName + "   pos:" + position + "   id: " + mSelectedLocationId, Toast.LENGTH_LONG).show();

        }
        //close the cursor
        c.close();

        //after a new location has been selected, we need to update the information that will be passed to
        //the viewpager
        populateContainerInfo();

        //start a new fragment with the new location
        loadViewPager();    //Note: we still want to load the view pager even if we just created a new location
        //      because, we want the correct view pager to load (blank in this case)


        TextView textViewLocName = (TextView) findViewById(R.id.text_view_location_name);
        textViewLocName.setText(mSelectedLocName);
        textViewLocName.setTypeface(tf);
    }


    private void loadViewPager() {
        Log.d(TAG, "loadViewPager...");
        // Create the adapter that will return a fragment for each ViewPager section
        mAppSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), mSelectedLocationId, mContainerCount, mContainerNames, mContainerIds);

        // Set up the ViewPager
        mViewPager = (ViewPager) findViewById(R.id.viewpager_container);

        // Attach the adapter to ViewPager
        mViewPager.destroyDrawingCache();
        mViewPager.setAdapter(mAppSectionsPagerAdapter);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage_list);

        Log.d(TAG, "onCreate StorageListActivity");

/*        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.storage_list_activity_container);

        if (null == fragment) {
            fragment = new StorageListFragment2();
            fm.beginTransaction().add(R.id.storage_list_activity_container, fragment).commit();
        }*/


        //get the font from the assets folder and set the font type
        tf = Typeface.createFromAsset(getAssets(), fontLoc);
        TextView textViewNavDrawerTitle = (TextView) findViewById(R.id.text_view_nav_drawer_title);
        textViewNavDrawerTitle.setTypeface(tf, Typeface.BOLD);


        //init the nav drawer stuff first
        initNavDrawerView();
        initDrawer();


        //if there are locations in the db, then populate something on the screen so it doesnt show up blank
        if (0 != mLocationCount) {
            selectItem(0);
        }

        //load the view pager with the current location Id
        if (mContainerCount > 0) {
            //loadViewPager();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_storage_list, menu);
        Log.d(TAG, "onCreateOptionsMenu StorageListActivity");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected StorageListActivity " + item.toString() + " selectedLocationId: " + mSelectedLocationId);

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(StorageListActivity.this, "Settings Menu", Toast.LENGTH_SHORT).show();
            // start this activity
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);

        } else if (id == R.id.addStorageContainer) {
            //Toast.makeText(StorageListActivity.this, "Selected to Add a Container", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(StorageListActivity.this, AddContainerActivity.class);

            Bundle args = new Bundle();
            //pass to the UpdateItemActivity the container Id, location Id, and item Id.
            args.putInt(SectionFragment.LOCATION_ID, mSelectedLocationId);
            intent.putExtras(args);


            startActivity(intent);
        } else if (id == R.id.action_search) {
            Toast.makeText(StorageListActivity.this, "Selected Search Action", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.add_item) {
            Intent intent = new Intent(StorageListActivity.this, AddItemActivity.class);

            Bundle args = new Bundle();
            //pass to the UpdateItemActivity the container Id, location Id, and item Id.
            args.putInt(SectionFragment.LOCATION_ID, mSelectedLocationId);
            intent.putExtras(args);

            startActivity(intent);
        } else if (id == R.id.add_location_menu_item) {
            Intent intent = new Intent(StorageListActivity.this, AddLocationActivity.class);
            startActivity(intent);
        } else if (id == R.id.action_db_dev) {
            Intent intent = new Intent(StorageListActivity.this, DBDevActivity.class);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Log.d(TAG, "placeHolderFragment, inflating fragment");
            View rootView = inflater.inflate(R.layout.fragment_storage_list, container, false);
            return rootView;
        }
    }
}
