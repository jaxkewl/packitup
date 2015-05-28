package com.marshong.packitup.ui.location;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.marshong.packitup.R;
import com.marshong.packitup.data.DBContract;
import com.marshong.packitup.data.DBHelper;
import com.marshong.packitup.ui.storage.StorageListActivity;

public class AddLocationActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_location, menu);
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
    public static class PlaceholderFragment extends Fragment {

        public static final String TAG = PlaceholderFragment.class.getSimpleName();

        DBHelper db;
        EditText mEditTextLocation;

        public PlaceholderFragment() {
        }


        //do some form validation  before attempting to insert into the database
        private void insertLoc() {
            //Note: inserting values has nothing to do with using the cursor loader. we need to use
            //the ContentResolver, Create/Update/Delete


            String locName = mEditTextLocation.getText().toString();

            boolean validTask = true;

            if (0 == locName.trim().length()) {
                mEditTextLocation.setError("Enter a valid Location Name");
                validTask = false;
            }

            if (validTask) {
                Toast.makeText(getActivity(), "Added new location: " + mEditTextLocation.getText(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Adding a location: " + locName);

                //Note: TaskProvider needs a URI and ContentValues as parameters.

                //First, create ContentValues to add data
                ContentValues contentValues = new ContentValues();
                contentValues.put(DBContract.Location.LOCATION_NAME, locName);   //add to container name

                //Second, get the URI to insert a location
                getActivity().getContentResolver().insert(DBContract.Location.CONTENT_LOCATION_URI, contentValues);

                Intent intent = new Intent(getActivity(), StorageListActivity.class);
                startActivity(intent);
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            final View rootView = inflater.inflate(R.layout.fragment_add_location, container, false);

            mEditTextLocation = (EditText) rootView.findViewById(R.id.edit_text_location_name);
            rootView.findViewById(R.id.button_add_location_name).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insertLoc();
                }
            });

            return rootView;
        }
    }
}
