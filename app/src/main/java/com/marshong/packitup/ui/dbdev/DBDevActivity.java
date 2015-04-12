package com.marshong.packitup.ui.dbdev;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.marshong.packitup.R;
import com.marshong.packitup.data.DBHelper;

public class DBDevActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbdev);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DBDevActivityFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dbdev, menu);
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
    public static class DBDevActivityFragment extends Fragment {

        DBHelper db;
        public static final String TAG = DBDevActivityFragment.class.getSimpleName();

        public DBDevActivityFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_dbdev, container, false);

            db = new DBHelper(getActivity());

            rootView.findViewById(R.id.button_insert_sample_entries).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Inserting Sample entries into DB");
                    db.insertSampleLocations();
                    db.insertSampleContainers();
                    db.insertSampleItems();
                    db.insertSampleOwners();
                    Toast.makeText(getActivity(),"Inserted sample locations, containers, and items.",Toast.LENGTH_SHORT).show();
                }
            });

            rootView.findViewById(R.id.button_wipe_db).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "wiping DB");
                    db.wipeAllFromDB();
                    db.insertDefaultLocAndCont();
                    Toast.makeText(getActivity(),"Wiped entire DB.",Toast.LENGTH_SHORT).show();
                }
            });


            return rootView;
        }
    }
}
