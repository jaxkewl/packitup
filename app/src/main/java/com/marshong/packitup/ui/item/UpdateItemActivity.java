package com.marshong.packitup.ui.item;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.marshong.packitup.R;
import com.marshong.packitup.data.DBContract;
import com.marshong.packitup.model.Item;
import com.marshong.packitup.ui.storage.SectionFragment;
import com.marshong.packitup.ui.storage.StorageListActivity;

import java.util.ArrayList;

public class UpdateItemActivity extends ActionBarActivity {

    public static final String TAG = UpdateItemActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_item);
        if (savedInstanceState == null) {

            //get the bundle that contains the ContainerId, Location Id, and Item Id
            Bundle bundle = getIntent().getExtras();

            /*int locationId = bundle.getInt(DBContract.Location.LOCATION_ID);
            int containerId = bundle.getInt(DBContract.Container.CONTAINER_ID);
            int itemId = bundle.getInt(DBContract.Item.ITEM_ID);*/

            //pass in the bundle to the fragment
            UpdateItemFragment fragment = new UpdateItemFragment();
            fragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_update_item, menu);
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
    public static class UpdateItemFragment extends Fragment {

        public static final String TAG = UpdateItemFragment.class.getSimpleName();

        //these fields will keep track of the container names and Ids
        ArrayList<String> mContainerNames;
        ArrayList<String> mContainerIds;


        //this is the locationId, we need to know this so the container spinner can only show containers from this location
        private int mLocationId;

        //this is the primary key of the item we are updating
        private int mItemId;

        //this is the item container, it should have the item information populated
        private Item mItem;

        private EditText mEditTextUpdateItemName;
        private EditText mEditTextUpdateItemDescr;
        private Spinner mSpinnerUpdateContainers;


        public UpdateItemFragment() {
        }

        private int findContainerPos(ArrayList<String> containers, String containerName) {
            Log.d(TAG, "findContainerPos using containerName: " + containerName + " and containers " + containers.size());
            for (String str : containers) {
                Log.d(TAG, str);
            }

            return containers.indexOf(containerName);
        }


        private void findValidContainers() {
            Log.d(TAG, "findValidContainers using location id: " + mLocationId);

            String selection = DBContract.Container.CONTAINER_LOCATION + "=?";
            String[] selectionArgs = {Integer.toString(mLocationId)};

            //use a content resolver and get the item information from the db
            //query the location table for all the location names.
            Cursor data = getActivity().getContentResolver().query(DBContract.Container.CONTENT_CONTAINER_URI,
                    DBContract.Container.CONTAINER_PROJECTION,
                    selection,
                    selectionArgs,
                    null);

            int i = 0;
            mContainerNames = new ArrayList<String>();
            mContainerIds = new ArrayList<String>();

            if (data.moveToFirst()) {

                do {
                    mContainerNames.add(data.getString(data.getColumnIndex(DBContract.Container.CONTAINER_NAME)));
                    mContainerIds.add(data.getString(data.getColumnIndex(DBContract.Container.CONTAINER_ID)));
                    Log.d(TAG, "found container: " + mContainerNames.get(i) + " with Id: " + mContainerIds.get(i));
                    i++;
                } while (data.moveToNext());
            }
            data.close();
        }

        private void createItem() {
            Log.d(TAG, "createItem using Id: " + mItemId);

            String selection = DBContract.Item.ITEM_ID + "= ?";
            String[] selectionArgs = {Integer.toString(mItemId)};

            //use a content resolver and get the item information from the db
            //query the location table for all the location names.
            Cursor data = getActivity().getContentResolver().query(DBContract.Item.CONTENT_ITEM_URI,
                    DBContract.Item.ITEM_PROJECTION,
                    selection,
                    selectionArgs,
                    null);

            int i = 0;

            if (data.moveToFirst()) {
                mItem = new Item("", "");
                do {
                    mItem.setName(data.getString(data.getColumnIndex(DBContract.Item.ITEM_NAME)));
                    mItem.setDescr(data.getString(data.getColumnIndex(DBContract.Item.ITEM_DESCR)));

                    int containerId = Integer.parseInt(data.getString(data.getColumnIndex(DBContract.Item.CONTAINER_REF)));
                    mItem.setContainerID(containerId);
                    mItem.setContainer("");

                    Log.d(TAG, "mItem: " + mItem);

                    i++;
                } while (data.moveToNext());
            }
            data.close();


        }

        private void init() {


        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_update_item, container, false);

            Log.d(TAG, "onCreateView updateItemFragment");


            Bundle extras = getActivity().getIntent().getExtras();
            mItemId = extras.getInt(SectionFragment.ITEM_ID);
            mLocationId = extras.getInt(SectionFragment.LOCATION_ID);
            int containerId = extras.getInt(SectionFragment.CONTAINER_ID);

            Log.d(TAG, "Found locationId: " + mLocationId + " and itemId: " + mItemId + " containerId: " + containerId);

            //find which position the container name is at
            createItem();
            findValidContainers();

            mEditTextUpdateItemName = (EditText) rootView.findViewById(R.id.edit_text_item_name_update);
            mEditTextUpdateItemDescr = (EditText) rootView.findViewById(R.id.edit_text_item_descr_update);

            Log.d(TAG, "mItem is populated: " + mItem);


            mEditTextUpdateItemDescr.setText(mItem.getDescr());
            mEditTextUpdateItemName.setText(mItem.getName());

            //setup the container spinner
            mSpinnerUpdateContainers = (Spinner) rootView.findViewById(R.id.spinner_item_container_update);
            ArrayAdapter<String> aa = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mContainerNames);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            mSpinnerUpdateContainers.setAdapter(aa);

            int containerPos = findContainerPos(mContainerNames, mItem.getContainer());
            if (containerPos <= 0) {
                containerPos = 1;
            }
            mSpinnerUpdateContainers.setSelection(containerPos);


            rootView.findViewById(R.id.button_update_item).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Item item = new Item(mEditTextUpdateItemName.getText().toString(), mEditTextUpdateItemDescr.getText().toString());

                    int selectedItem = mSpinnerUpdateContainers.getSelectedItemPosition();
                    String contName = mContainerNames.get(selectedItem);
                    int contId = Integer.parseInt(mContainerIds.get(selectedItem));

                    item.setContainer(contName);
                    item.setContainerID(contId);

                    Log.d(TAG, "updating item: " + item);
                    //db.insertItem(item);
                    Toast.makeText(getActivity(), "Updated item " + item, Toast.LENGTH_LONG);

                    Intent intent = new Intent(getActivity(), StorageListActivity.class);
                    startActivity(intent);
                }
            });


            return rootView;
        }
    }
}
