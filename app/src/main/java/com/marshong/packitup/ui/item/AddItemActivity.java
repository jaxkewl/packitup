package com.marshong.packitup.ui.item;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import com.marshong.packitup.R;
import com.marshong.packitup.data.DBContract;
import com.marshong.packitup.data.DBHelper;
import com.marshong.packitup.ui.storage.StorageListActivity;

public class AddItemActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        if (savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new AddItemFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class AddItemFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

        public static final String TAG = AddItemFragment.class.getSimpleName();
        private EditText mEditTextItemName;
        private EditText mEditTextItemDescr;
        private Spinner mSpinnerContainers;

        private SimpleCursorAdapter mAdapter;

        // The loader's unique id. Loader ids are specific to the Activity or
        // Fragment in which they reside.
        private final static int LOADER_ID = 0;


        DBHelper db;

        public AddItemFragment() {
        }

        //do some form validation  before attempting to insert into the database
        private void insertTaskToDb() {
            //Note: inserting values has nothing to do with using the cursor loader. we need to use
            //the ContentResolver, Create/Update/Delete



            String taskName = mEditTextItemName.getText().toString();
            String taskDesc = mEditTextItemDescr.getText().toString();
            boolean validTask = true;

            if (0 == taskName.trim().length()) {
                mEditTextItemName.setError("Enter a valid Item Name");
                validTask = false;
            }

            if (0 == taskDesc.trim().length()) {
                mEditTextItemDescr.setError("Enter a valid Item Description");
                validTask = false;
            }

            if (validTask) {
                Log.d(TAG, "Adding a task: " + taskName + " " + taskDesc);

                //Note: TaskProvider needs a URI and ContentValues as parameters.

                //First, create ContentValues to add data
                ContentValues contentValues = new ContentValues();
                contentValues.put(DBContract.Item.ITEM_NAME, taskName);
                contentValues.put(DBContract.Item.ITEM_DESCR, taskDesc);

                //Second, get the URI to insert a task
                getActivity().getContentResolver().insert(DBContract.Item.CONTENT_ITEM_URI, contentValues);

                Intent intent = new Intent(getActivity(), StorageListActivity.class);
                startActivity(intent);
            }
        }

        private void init(View rootView) {

            //we want to use a cursor loader adapter to map the data (ContentProvider to the View)
            Log.d(TAG, "init...");
            String[] fromFields = new String[]{DBContract.Container.CONTAINER_NAME};   //name of the table column

            //Note: to use your own custom layout, the toFields, must be pointing to the name of the
            //ID in of your text view in the custom layout.
            //int[] toFields = new int[]{R.id.textViewPrettyList};
            int[] toFields = new int[]{android.R.id.text1}; //points to a view, inside of a layout.xml or an android default view

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
                    android.R.layout.simple_spinner_item,
                    //R.layout.storage_list_item,  //Note: use your own custom layout here.
                    null,
                    fromFields,
                    toFields,
                    0
            );

            //setListAdapter(mAdapter);


            //setup the container spinner
            mSpinnerContainers = (Spinner) rootView.findViewById(R.id.spinner_item_container);
            mSpinnerContainers.setAdapter(mAdapter);
            //GOTCHA: Must implement all 3 methods when you implement LoaderManager.LoaderCallbacks<Cursor>

            mEditTextItemName = (EditText) rootView.findViewById(R.id.edit_text_item_name);
            mEditTextItemDescr = (EditText) rootView.findViewById(R.id.edit_text_item_descr);
            rootView.findViewById(R.id.button_add_item).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insertTaskToDb();
                }
            });
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_add_item, container, false);

            Log.d(TAG, "onCreateView");

            init(rootView);
            return rootView;
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            Log.d(TAG, id + ", onCreateLoader... URI:" + DBContract.Container.CONTENT_CONTAINER_URI);

            for (String i : DBContract.Container.CONTAINER_PROJECTION) {
                Log.d(TAG, "projection: " + i);
            }

            //Note: this is how you associate a cursor loader with the LoaderManager.
            //Since this method is required, define your CursorLoader here.

            CursorLoader cursorLoader = new CursorLoader(getActivity(),
                    DBContract.Container.CONTENT_CONTAINER_URI,
                    DBContract.Container.CONTAINER_PROJECTION,
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
            //GOTCHA: Must implement all 3 methods when you implement LoaderManager.LoaderCallbacks<Cursor>

            /* A switch-case is useful when dealing with multiple Loaders/IDs
               switch (loader.getId()) {
               case LOADER_ID:
                  The asynchronous load is complete and the data
                  is now available for use. Only now can we associate
                  the queried Cursor with the SimpleCursorAdapter.
                  mAdapter.swapCursor(cursor);
                  break;
            }
             The listview now displays the queried data.*/
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            // For whatever reason, the Loader's data is now unavailable.
            // Remove any references to the old data by replacing it with
            // a null Cursor.
            mAdapter.swapCursor(null);
            //GOTCHA: Must implement all 3 methods when you implement LoaderManager.LoaderCallbacks<Cursor>
        }
    }
}
