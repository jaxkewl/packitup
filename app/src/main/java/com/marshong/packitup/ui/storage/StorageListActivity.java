package com.marshong.packitup.ui.storage;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.marshong.packitup.R;
import com.marshong.packitup.ui.container.AddContainerActivity;
import com.marshong.packitup.ui.dbdev.DBDevActivity;
import com.marshong.packitup.ui.item.AddItemActivity;
import com.marshong.packitup.ui.location.AddLocationActivity;
import com.marshong.packitup.ui.settings.SettingsActivity;


public class StorageListActivity extends ActionBarActivity {

    public final static String TAG = StorageListFragment.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage_list);

        Log.d(TAG, "onCreate StorageListActivity");

        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.storage_list_activity_container);

        if (null == fragment) {
            fragment = new StorageListFragment();
            fm.beginTransaction().add(R.id.storage_list_activity_container, fragment).commit();
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
        Log.d(TAG, "onOptionsItemSelected StorageListActivity " + item.toString());

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
            Toast.makeText(StorageListActivity.this, "Selected to Add a Container", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(StorageListActivity.this, AddContainerActivity.class);
            startActivity(intent);
        } else if (id == R.id.action_search) {
            Toast.makeText(StorageListActivity.this, "Selected Search Action", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.add_item) {
            Intent intent = new Intent(StorageListActivity.this, AddItemActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.add_location_menu_item) {
            Intent intent = new Intent(StorageListActivity.this, AddLocationActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.action_db_dev){
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
