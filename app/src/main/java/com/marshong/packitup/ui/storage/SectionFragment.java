package com.marshong.packitup.ui.storage;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.marshong.packitup.R;
import com.marshong.packitup.data.DBContract;
import com.marshong.packitup.ui.item.UpdateItemActivity;

/**
 * A fragment representing a section of the ViewPager
 * this fragment needs to know what location ID and container ID it came from.
 */
public class SectionFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = SectionFragment.class.getSimpleName();

    public static final String containerArrayKey = "container_key";
    public static final String selectedIndexKey = "selected_Index_key";
    public static final String LOCATION_ID = "LOCATION_ID";
    public static final String CONTAINER_ID = "CONTAINER_ID";
    public static final String ITEM_ID = "ITEM_ID";


    private ListView listview;

    private Typeface tf;


    private int mContainerId;
    private int mLocationId;


    private String[] mItemNames;
    private String[] mItemIds;

    private void populateItems() {
        Log.d(TAG, "populateItems with containerId:" + mContainerId);

        String selection = "CONTAINER_REF=?";
        String[] selectionArgs = {Integer.toString(mContainerId)};

        //query the location table for all the location names.
        Cursor data = getActivity().getContentResolver().query(DBContract.Item.CONTENT_ITEM_URI,
                DBContract.Item.ITEM_PROJECTION,
                selection,
                selectionArgs,
                null);

        mItemNames = new String[data.getCount()];
        mItemIds = new String[data.getCount()];
        Log.d(TAG, "items count: " + data.getCount());
        int i = 0;
        if (data.moveToFirst()) {
            do {
                String item = data.getString(data.getColumnIndex(DBContract.Item.ITEM_NAME));
                String itemId = data.getString(data.getColumnIndex(DBContract.Item.ITEM_ID));
                Log.d(TAG, "found item name: " + item);
                mItemNames[i] = item;
                mItemIds[i] = itemId;
                i++;
            } while (data.moveToNext());
        }
        data.close();
    }


    private void init(View rootView) {
        Log.d(TAG, "init...");

        //get the font from the assets folder and set the font type
        tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/PermanentMarker.ttf");
        TextView textview = ((TextView) rootView.findViewById(R.id.text_view_view_pager_title));
        textview.setTypeface(tf);
        textview.setText("Items");


        Bundle args = getArguments();
        int mmContainerId = args.getInt(CONTAINER_ID);
        mLocationId = args.getInt(LOCATION_ID);
        int selectedIndex = args.getInt(selectedIndexKey);
        String[] mContainerIds = args.getStringArray(containerArrayKey);
        mContainerId = Integer.parseInt(mContainerIds[selectedIndex]);

        Log.d(TAG, "using sample: " + selectedIndex + " mcontainerId: " + mContainerId + " and mlocationId: " + mLocationId + " on SectionFragment: " + this.toString());
        populateItems();

        listview = (ListView) rootView.findViewById(R.id.listViewWords);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getActivity().getBaseContext(),
                android.R.layout.simple_list_item_1, mItemNames);

        listview.setAdapter(arrayAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick..." + "Selected position: " + position + " id: " + id + " itemName: " + mItemNames[position] + " itemId: " + mItemIds[position] + " locationId: " + mLocationId + " mContainerId: " + mContainerId);
                //Toast.makeText(getActivity(), "Selected position: " + position + " id: " + id + " itemName: " + mItemNames[position] + " itemId: " + mItemIds[position], Toast.LENGTH_LONG).show();

                Bundle args = new Bundle();
                //pass to the UpdateItemActivity the container Id, location Id, and item Id.
                args.putInt(SectionFragment.CONTAINER_ID, mContainerId);
                args.putInt(SectionFragment.LOCATION_ID, mLocationId);
                args.putInt(SectionFragment.ITEM_ID, Integer.parseInt(mItemIds[position]));

                Intent intent = new Intent(getActivity(), UpdateItemActivity.class);
                intent.putExtras(args);
                startActivity(intent);

            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_view_pager_section, container,
                false);

        Log.d(TAG, "onCreateView called...");
        init(rootView);

        return rootView;
    }

    @Override
    public void onRefresh() {

    }
}