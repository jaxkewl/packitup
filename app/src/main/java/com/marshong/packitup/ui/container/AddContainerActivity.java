package com.marshong.packitup.ui.container;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.marshong.packitup.R;
import com.marshong.packitup.data.DBHelper;


public class AddContainerActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_container);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new AddContainerFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_container, menu);
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
    public static class AddContainerFragment extends Fragment {
        public final static String TAG = AddContainerFragment.class.getSimpleName();
        DBHelper dbHelper;
        Button mButtonAddContainer;

        //container information
        EditText mEditTextContName;
        EditText mEditTextContDescr;
        Spinner mSpinnerContLoc;

        public AddContainerFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_add_container, container, false);

            dbHelper = new DBHelper(rootView.getContext());

/*
            //get all the locations stored in the DB to use in the location spinner
            final ArrayList<String> allLocations = dbHelper.getAllLocationNames();

            mEditTextContName = (EditText) rootView.findViewById(R.id.edit_text_container_name);
            mEditTextContDescr = (EditText) rootView.findViewById(R.id.edit_text_container_description);

            //populate the spinner
            mSpinnerContLoc = (Spinner) rootView.findViewById(R.id.spinner_location);
            ArrayAdapter<String> locAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, allLocations);
            locAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinnerContLoc.setAdapter(locAdapter);

            mButtonAddContainer = (Button) rootView.findViewById(R.id.button_submit_new_container);
            mButtonAddContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String contName = mEditTextContName.getText().toString();
                    String contDescr = mEditTextContDescr.getText().toString();
                    int selectedItem = mSpinnerContLoc.getSelectedItemPosition();
                    String locName = allLocations.get(selectedItem);

                    Log.d(TAG, "onClick adding container " + contName + " " + contDescr + " " + locName);

                    Container cont = new Container(contName, contDescr, locName);
                    dbHelper.insertContainer(cont);

*/
/*                    dbHelper.insertSampleLocations();
                    dbHelper.insertSampleContainers();
                    dbHelper.insertSampleItems();*//*

                    Toast.makeText(getActivity(), "Added new Container " + cont, Toast.LENGTH_LONG);

                    Intent intent = new Intent(getActivity(),StorageListActivity.class);
                    startActivity(intent);
                }
            });
*/


            return rootView;
        }
    }
}
