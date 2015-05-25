package com.marshong.packitup.ui.storage;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;

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
    private final static int LOADER_ID = 0;

    // below are shared preference values
    SharedPreferences mPref;
    String mItemTextColor;
    String mContTextColor;
    String mLocTextColor;
    String mTextSize;

    private SimpleCursorAdapter mAdapter;

    public StorageListFragment2() {
        //required empty constructor
        //Log.d(TAG, "constructor");
    }

    private void init(View view) {

        //we want to use a cursor loader adapter to map the data (ContentProvider to the View)
        Log.d(TAG, "init...");
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
                null,
                fromFields,
                toFields,
                0
        );
        setListAdapter(mAdapter);
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
        init(view);

        return view;
    }



    /*The following 3 methods are required by implementing LoaderManager.LoaderCallbacks<Cursor>*/

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader... URI:" + DBContract.Item.CONTENT_ITEM_URI);

        for (String i : DBContract.Item.ITEM_PROJECTION) {
            Log.d(TAG, "projection: " + i);
        }

        //Note: this is how you associate a cursor loader with the LoaderManager.
        //Since this method is required, define your CursorLoader here.

        CursorLoader cursorLoader = new CursorLoader(getActivity(),
                DBContract.Item.CONTENT_ITEM_URI,
                DBContract.Item.ITEM_PROJECTION,
                null,
                null,
                null
        );

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // The asynchronous load is complete and the data
        // is now available for use. Only now can we associate
        // the queried Cursor with the SimpleCursorAdapter.
        mAdapter.swapCursor(data);


        /*// A switch-case is useful when dealing with multiple Loaders/IDs
        switch (loader.getId()) {
            case LOADER_ID:
                // The asynchronous load is complete and the data
                // is now available for use. Only now can we associate
                // the queried Cursor with the SimpleCursorAdapter.
                mAdapter.swapCursor(cursor);
                break;
        }
        // The listview now displays the queried data.*/
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // For whatever reason, the Loader's data is now unavailable.
        // Remove any references to the old data by replacing it with
        // a null Cursor.
        mAdapter.swapCursor(null);
    }
}
