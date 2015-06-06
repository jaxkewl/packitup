package com.marshong.packitup.ui.storage;

import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.marshong.packitup.R;
import com.marshong.packitup.data.DBContract;

/**
 * Created by martin on 2/19/2015.
 */
//LoaderCallbacks interface is a simple contract the the LoaderManager uses to report data back to the client,
//implement the 3 required methods below.
//Also, ListFragment is needed to call method setListAdapter in the init() method.
public class StorageListFragment2 extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public final static String TAG = StorageListFragment2.class.getSimpleName();

    // The loader's unique id. Loader ids are specific to the Activity or
    // Fragment in which they reside.
    private final static int LOADER_ID = 10;
    private final static int LOADER_ID_NAV_DRAWER = 1;
    private final static int LOADER_ID_LOC_NAME = 2;
    private final static int LOADER_ID_CONTAINERS = 3;

    // below are shared preference values
    SharedPreferences mPref;
    String mItemTextColor;
    String mContTextColor;
    String mLocTextColor;
    String mTextSize;

    //these class fields take care of the font
    Typeface tf;
    TextView mTextViewStorageTitle;

    //these class fields are related to the nav drawer
    private DrawerLayout mDrawerLayout;
    private RelativeLayout mDrawerRL;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private String[] mNavDrawerItems = {"Item 1", "Item 2", "Item 3", "Item 4"};
    private SimpleCursorAdapter mNavDrawAdapter;

    private SimpleCursorAdapter mAdapter;

    //the following keeps track of which group of items to display
    private int mSelectedLocationId = 1;    //this comes from the nav drawer
    private String mSelectedLocName;
    private int mSelectedContainerId = 0;   //this comes from the view pager


    public StorageListFragment2() {
        //required empty constructor
        //Log.d(TAG, "constructor");
    }


    private void setNavDrawerItems() {
        //use a cursor loader on the location table
        Log.d(TAG, "setNavDrawerItems...");

        // mNavDrawerItems

        //we want to use a cursor loader adapter to map the data (ContentProvider to the View)
        mNavDrawerItems = new String[]{DBContract.Location.LOCATION_NAME};   //name of the table column

        //Note: to use your own custom layout, the toFields, must be pointing to the name of the
        //ID in of your text view in the custom layout.
        //int[] toFields = new int[]{R.id.textViewPrettyList};
        int[] toFields = new int[]{R.id.textViewLocName}; //points to a view, inside of a layout.xml

        //the loader manager is an abstract class that is associated with an activity or fragment for
        //managing loader instances.

        // "this" (the last argument used in initLoader) is the Activity
        // (which implements the LoaderCallbacks<Cursor>
        // interface) is the callbacks object through which we will interact
        // with the LoaderManager. The LoaderManager uses this object to
        // instantiate the Loader and to notify the client when data is made
        // available/unavailable.
        getLoaderManager().initLoader(LOADER_ID_NAV_DRAWER, null, this);

        mNavDrawAdapter = new SimpleCursorAdapter(
                getActivity(),
                //android.R.layout.simple_expandable_list_item_1,
                //android.R.layout.simple_list_item_multiple_choice,
                R.layout.storage_list_item,  //Note: use your own custom layout here.
                null, //this is the cursor, we'll set it later after the cursor loads in onLoadFinished
                mNavDrawerItems,
                toFields,
                0
        );
        //setListAdapter(mAdapter);
    }


    private void initNavDrawerView() {

        // DrawerLayout
        mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawerLayout);

        // RelativeLayout container that holds the Nav drawer menu
        mDrawerRL = (RelativeLayout) getActivity().findViewById(R.id.drawerRL);

        // Nav drawer ListView
        mDrawerList = (ListView) getActivity().findViewById(R.id.drawerList);

        // Set the adapter for drawer list
        //mDrawerList.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mNavDrawerItems));
        mDrawerList.setAdapter(mNavDrawAdapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

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

        mSelectedLocName = mNavDrawerItems[position];
        //Toast.makeText(getActivity(), "Selected " + mSelectedLocName, Toast.LENGTH_LONG).show();

        // Highlight the selected drawer item
        mDrawerList.setItemChecked(position, true);

        // Close drawer
        mDrawerLayout.closeDrawer(mDrawerRL);

        //we need to get the unique location ID from the content provider
        String selection = DBContract.Location.LOCATION_NAME + "=?";    //this is selection statement i.e. "name=? AND email=?"
        String[] selectionArgs = {mSelectedLocName};    //this is the list of arguments that replace the question mark in the selection statement

        Cursor c = getActivity().getContentResolver().query(DBContract.Location.CONTENT_LOCATION_URI,
                DBContract.Location.LOCATION_PROJECTION,
                selection,
                selectionArgs,
                null);

        if (c.moveToFirst()) {
            mSelectedLocationId = c.getInt(c.getColumnIndexOrThrow(DBContract.Location.LOCATION_ID));
            Toast.makeText(getActivity(), "Selected " + mSelectedLocName + "   pos:" + position + "   id: " + mSelectedLocationId, Toast.LENGTH_LONG).show();

        }
        //close the cursor
        c.close();

        //GOTCHA: after selecting a new location (i.e. primary key) we need to reload the cursor loader with
        //the new location so our main list changes.
        getLoaderManager().restartLoader(LOADER_ID_CONTAINERS, null, this);
    }

    /**
     * Initialize the navigation drawer
     */
    private void initDrawer() {

        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, null, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle("Hello World");
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle("Bye World");

            }

        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        //GOTCHA: need to syncstate in order for hamburger symbol to show up
        mDrawerToggle.syncState();
        ((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((ActionBarActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        setNavDrawerItems();
    }


    private void initJustContainers(View view) {
        Log.d(TAG, "init...");

        //get the font from the assets folder and set the font type
        tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/PermanentMarker.ttf");
        mTextViewStorageTitle = (TextView) view.findViewById(R.id.text_view_storage_title);
        mTextViewStorageTitle.setTypeface(tf, Typeface.BOLD);


        //we want to use a cursor loader adapter to map the data (ContentProvider to the View)
        String[] fromFields = new String[]{DBContract.Container.CONTAINER_NAME};   //name of the table column

        //Note: to use your own custom layout, the toFields, must be pointing to the name of the
        //ID in of your text view in the custom layout.
        //int[] toFields = new int[]{R.id.textViewPrettyList};
        int[] toFields = new int[]{R.id.textViewItemName}; //points to a view, inside of a layout.xml

        //the loader manager is an abstract class that is associated with an activity or fragment for
        //managing loader instances.

        // "this" (the last argument used in initLoader) is the Activity
        // (which implements the LoaderCallbacks<Cursor>
        // interface) is the callbacks object through which we will interact
        // with the LoaderManager. The LoaderManager uses this object to
        // instantiate the Loader and to notify the client when data is made
        // available/unavailable.
        getLoaderManager().initLoader(LOADER_ID_CONTAINERS, null, this);

        mAdapter = new SimpleCursorAdapter(
                getActivity(),
                //android.R.layout.simple_expandable_list_item_1,
                //android.R.layout.simple_list_item_multiple_choice,
                R.layout.storage_list_item,  //Note: use your own custom layout here.
                null, //this is the cursor, we'll set it later after the cursor loads in onLoadFinished
                fromFields,
                toFields,
                0
        );
        setListAdapter(mAdapter);


        //below is the code for the nav drawer
        initNavDrawerView();
        initDrawer();
    }


    private void init(View view) {
        Log.d(TAG, "init...");

        //get the font from the assets folder and set the font type
        tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/PermanentMarker.ttf");
        mTextViewStorageTitle = (TextView) view.findViewById(R.id.text_view_storage_title);
        mTextViewStorageTitle.setTypeface(tf, Typeface.BOLD);


        //we want to use a cursor loader adapter to map the data (ContentProvider to the View)
        String[] fromFields = new String[]{DBContract.Item.ITEM_NAME};   //name of the table column

        //Note: to use your own custom layout, the toFields, must be pointing to the name of the
        //ID in of your text view in the custom layout.
        //int[] toFields = new int[]{R.id.textViewPrettyList};
        int[] toFields = new int[]{R.id.textViewItemName}; //points to a view, inside of a layout.xml

        //the loader manager is an abstract class that is associated with an activity or fragment for
        //managing loader instances.

        // "this" (the last argument used in initLoader) is the Activity
        // (which implements the LoaderCallbacks<Cursor>
        // interface) is the callbacks object through which we will interact
        // with the LoaderManager. The LoaderManager uses this object to
        // instantiate the Loader and to notify the client when data is made
        // available/unavailable.
        getLoaderManager().initLoader(LOADER_ID, null, this);

        mAdapter = new SimpleCursorAdapter(
                getActivity(),
                //android.R.layout.simple_expandable_list_item_1,
                //android.R.layout.simple_list_item_multiple_choice,
                R.layout.storage_list_item,  //Note: use your own custom layout here.
                null, //this is the cursor, we'll set it later after the cursor loads in onLoadFinished
                fromFields,
                toFields,
                0
        );
        setListAdapter(mAdapter);


        //below is the code for the nav drawer
        initNavDrawerView();
        initDrawer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView StorageListFragment");

        //inflate the view first so you can use the view objects.
        View view = inflater.inflate(R.layout.fragment_storage_list, container, false);

        //get the shared prefs
        mPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mLocTextColor = mPref.getString("loc_text_color", "#0047AB");
        mContTextColor = mPref.getString("cont_text_color", "#8BA870");
        mItemTextColor = mPref.getString("item_text_color", "#000000");
        mTextSize = mPref.getString("textSize", "18");
        float textSizeFloat = Float.parseFloat(mTextSize);

        //for readability, put in a separate method.
        initJustContainers(view);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


    }


    /*The following 3 methods are required by implementing LoaderManager.LoaderCallbacks<Cursor>*/

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader..." + " id: " + id);
        CursorLoader cursorLoader = null;

        switch (id) {
            case LOADER_ID_CONTAINERS:
                Log.d(TAG, "URI:" + DBContract.Container.CONTENT_CONTAINER_FILTER_URI);

                for (String i : DBContract.Container.CONTAINER_PROJECTION) {
                    Log.d(TAG, "projection: " + i);
                }

                //Note: this is how you associate a cursor loader with the LoaderManager.
                //when you initialized the manager, you associated it with a loader id.
                //Since this method is required, define your CursorLoader here.
                String selection = DBContract.Container.CONTAINER_LOCATION + "=?";
                String[] selectionArgs = {Integer.toString(mSelectedLocationId)};

                cursorLoader = new CursorLoader(getActivity(),
                        DBContract.Container.CONTENT_CONTAINER_FILTER_URI,
                        DBContract.Container.CONTAINER_PROJECTION,
                        selection,
                        selectionArgs,
                        null
                );
                break;
            case LOADER_ID:
                Log.d(TAG, "URI:" + DBContract.Item.CONTENT_ITEM_URI);

                for (String i : DBContract.Item.ITEM_PROJECTION) {
                    Log.d(TAG, "projection: " + i);
                }

                //Note: this is how you associate a cursor loader with the LoaderManager.
                //when you initialized the manager, you associated it with a loader id.
                //Since this method is required, define your CursorLoader here.

                cursorLoader = new CursorLoader(getActivity(),
                        DBContract.Item.CONTENT_ITEM_URI,
                        DBContract.Item.ITEM_PROJECTION,
                        null,
                        null,
                        null
                );
                break;
            case LOADER_ID_NAV_DRAWER:
                Log.d(TAG, "URI:" + DBContract.Location.CONTENT_LOCATION_URI);

                for (String i : DBContract.Location.LOCATION_PROJECTION) {
                    Log.d(TAG, "projection: " + i);
                }

                //Note: this is how you associate a cursor loader with the LoaderManager.
                //Since this method is required, define your CursorLoader here.

                cursorLoader = new CursorLoader(getActivity(),
                        DBContract.Location.CONTENT_LOCATION_URI,
                        DBContract.Location.LOCATION_PROJECTION,
                        null,
                        null,
                        null
                );

                break;
            default:

        }
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished... " + loader.getId());

        // A switch-case is useful when dealing with multiple Loaders/IDs
        switch (loader.getId()) {
            case LOADER_ID_CONTAINERS:
                for (String s : data.getColumnNames()) {
                    Log.d(TAG, "onLoadFinished... " + s);
                }

                // The asynchronous load is complete and the data
                // is now available for use. Only now can we associate
                // the queried Cursor with the SimpleCursorAdapter.
                mAdapter.swapCursor(data);
                break;
            case LOADER_ID:
                for (String s : data.getColumnNames()) {
                    Log.d(TAG, "onLoadFinished... " + s);
                }

                // The asynchronous load is complete and the data
                // is now available for use. Only now can we associate
                // the queried Cursor with the SimpleCursorAdapter.
                mAdapter.swapCursor(data);
                break;
            case LOADER_ID_NAV_DRAWER:
                for (String s : data.getColumnNames()) {
                    Log.d(TAG, "onLoadFinished... " + s);
                }
                mNavDrawerItems = new String[data.getCount()];
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

                mDrawerList.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mNavDrawerItems));

                /*for (int i = 1; i <= data.getCount(); i++) {
                    //Log.d(TAG, "drawer item#: " + i + " " + data.);
                    mNavDrawerItems[i] = data.getString(i);
                }*/

                break;
        }
        // The listview now displays the queried data.
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // For whatever reason, the Loader's data is now unavailable.
        // Remove any references to the old data by replacing it with
        // a null Cursor.
        mAdapter.swapCursor(null);
    }
}
