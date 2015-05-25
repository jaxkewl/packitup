package com.marshong.packitup.ui.storage;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.marshong.packitup.R;
import com.marshong.packitup.data.DBHelper;
import com.marshong.packitup.model.Container;
import com.marshong.packitup.model.Item;

import java.util.ArrayList;

/**
 * Created by martin on 2/19/2015.
 */
public class StorageListFragment extends Fragment {

    DBHelper db;
    SharedPreferences mPref;

    String mItemTextColor;
    String mContTextColor;
    String mLocTextColor;
    String mTextSize;

    public final static String TAG = StorageListFragment.class.getSimpleName();

    public StorageListFragment() {
        //required empty constructor
        //Log.d(TAG, "constructor");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView StorageListFragment");

        //inflate the view first so you can use the view objects.
        View view = inflater.inflate(R.layout.fragment_storage_list, container, false);
/*

        //get the shared prefs
        mPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mLocTextColor = mPref.getString("loc_text_color", "#0047AB");
        mContTextColor = mPref.getString("cont_text_color", "#8BA870");
        mItemTextColor = mPref.getString("item_text_color", "#000000");
        mTextSize = mPref.getString("textSize", "18");
        float textSizeFloat = Float.parseFloat(mTextSize);


        //instantiate the DB helper
        db = new DBHelper(getActivity());

        //get all the items from the database
        ArrayList<Item> items = db.getAllItems();

        //get all the containers from the database
        ArrayList<Container> containers = db.getAllContainers();

        //get all the locations, this is how we will be sorting by
        ArrayList<String> locations = db.getAllLocationNames();

        ArrayList<String> locationsAdded = new ArrayList<>();

        ScrollView sv = new ScrollView(getActivity());
        LinearLayout ll = new LinearLayout(getActivity());

        ll.setOrientation(LinearLayout.VERTICAL);

        //we need to prepare a dynamic layout to display all the containers in some order
        for (String loc : locations) {

            //keep track of which locations were already added.
            if (!locationsAdded.contains(loc)) {
                Log.d(TAG, "we have not added " + loc + " yet, adding now");
                locationsAdded.add(loc);
            }
        }


        //now iterate through each unique location
        for (String loc : locationsAdded) {

            TextView tvLoc = new TextView(getActivity());
            tvLoc.setText(loc);

            tvLoc.setTypeface(Typeface.DEFAULT_BOLD);   //make the location bold
            //tvLoc.setTextColor(getResources().getColor(R.color.navy_blue));
            Log.d(TAG, "setting location color to: " + mLocTextColor);
            tvLoc.setTextColor(Color.parseColor(mLocTextColor));
            Log.d(TAG, "setting text size to: " + textSizeFloat);
            tvLoc.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizeFloat);
            //float scale = getResources().getDisplayMetrics().density;
            //int dpAsPixels = (int) (20 * scale + 0.5f);

            //tvLoc.setTextSize(WebSettings.TextSize.LARGER);
            ll.addView(tvLoc);

            //now iterate through our containers
            for (Container c : containers) {
                Log.d(TAG, "iterating through container: " + c + " loc " + loc);
                if (loc.equals(c.getLocation())) {
                    Log.d(TAG, "adding container " + c.getName() + " to location " + c.getLocation());
                    //add this container to this location
                    final String containerName = c.toString();
                    TextView tvContainer = new TextView(getActivity());
                    tvContainer.setText("  " + c.toString());
                    tvContainer.setClickable(true);
                    //tvContainer.setTextColor(getResources().getColor(R.color.cobalt_blue));
                    Log.d(TAG, "setting container color to: " + mContTextColor);
                    tvContainer.setTextColor(Color.parseColor(mContTextColor));
                    tvContainer.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizeFloat);
                    tvContainer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getActivity(), "Clicked on " + containerName, Toast.LENGTH_SHORT).show();
                        }
                    });
                    ll.addView(tvContainer);


                    //now find all the items within this container
                    for (Item item : items) {
                        Log.d(TAG, "iterating through item: " + item + " cont " + c);
                        if (item.getContainerID() == c.getId()) {
                            final String itemName = item.getName();
                            final String itemDescr = item.getDescr();
                            final int contId = item.getContainerID();
                            final String contName = item.getContainer();


                            Log.d(TAG, "found a match " + item.getContainerID() + " " + c.getId());
                            TextView tvItem = new TextView(getActivity());
                            tvItem.setText("    " + item.toString());
                            Log.d(TAG, "setting ITEM color to: " + mItemTextColor);
                            tvItem.setTextColor(Color.parseColor(mItemTextColor));
                            tvItem.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizeFloat);
                            tvItem.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(getActivity(), "Clicked on " + itemName, Toast.LENGTH_SHORT).show();

                                    //setup an intent to modify the item. add some extras so we know which item we
                                    //clicked on.
                                    Intent intent = new Intent(getActivity(), UpdateItemActivity.class);
                                    intent.putExtra(DBContract.Item.ITEM_NAME, itemName);
                                    intent.putExtra(DBContract.Item.ITEM_DESCR, itemDescr);
                                    intent.putExtra(DBContract.Container.CONTAINER_ID, contId);
                                    intent.putExtra(DBContract.Container.CONTAINER_NAME, contName);
                                    startActivity(intent);
                                }
                            });
                            ll.addView(tvItem);
                        }
                    }
                }
            }

            //draw our divider line
            //setup our line view object to draw horizontal lines
*/
/*            View lineView = new View(getActivity());
            LinearLayout.LayoutParams lineparams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 1);
            lineView.setLayoutParams(lineparams);
            lineView.setBackgroundColor(Color.BLACK);
            ll.addView(lineView);*//*

        }

        sv.addView(ll);
        getActivity().setContentView(sv);

        */
/*
        //ArrayList<Container> containers = populateBoxes();
        ArrayList<Container> containers = ic_db_icon.getAllContainers();


        ScrollView sv = new ScrollView(getActivity());
        LinearLayout ll = new LinearLayout(getActivity());

        ll.setOrientation(LinearLayout.VERTICAL);
        sv.addView(ll);

        TextView tv = new TextView(getActivity());

        tv.setText("Dynamic layouts ftw!");
        ll.addView(tv);

        EditText et = new EditText(getActivity());
        et.setText("weeeeeeeeeee~!");

        ll.addView(et);

        Button b = new Button(getActivity());

        b.setText("I don't do anything, but I was added dynamically. :)");
        ll.addView(b);

        for(int i = 0; i < 20; i++) {
            CheckBox cb = new CheckBox(getActivity());
            cb.setText("I'm dynamic!");
            ll.addView(cb);
        }
*//*


        //getActivity().setContentView(sv);
*/


        return view;
    }


/*    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView StorageListFragment");

        //inflate the view first so you can use the view objects.
        View view = inflater.inflate(R.layout.fragment_storage_list, container, false);

        ic_db_icon = new DBHelper(getActivity());

        //ArrayList<Container> containers = populateBoxes();
        ArrayList<Container> containers = ic_db_icon.getAllContainers();

        final ListView listView = (ListView) view.findViewById(R.id.listViewStorageList);
        final StorageListAdapter adapter = new StorageListAdapter(getActivity(), R.layout.storage_list_item, containers);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Container selContainer = (Container) parent.getItemAtPosition(position);
                Toast.makeText(getActivity(), "clicked on " + selContainer.getName(), Toast.LENGTH_SHORT).show();
            }
        });


        if (null != listView) {
            listView.setAdapter(adapter);
        }

        return view;
    }*/

    private ArrayList<Container> populateBoxes() {
        Log.d(TAG, "populating boxes");
        ArrayList<Container> containers = new ArrayList<>();


        Container container = new Container("box1", "descr", "Bathroom");
        container.addItem(new Item("medicine", "descr"));
        container.addItem(new Item("soap", "descr"));
        containers.add(container);

        container = new Container("box2", "descr", "Bedroom");
        container.addItem(new Item("guns", "descr"));
        container.addItem(new Item("blurays", "descr"));
        container.addItem(new Item("humidifier", "descr"));
        containers.add(container);

        container = new Container("box3", "descr", "Kitchen");
        container.addItem(new Item("pots", "descr"));
        container.addItem(new Item("cups", "descr"));
        containers.add(container);

        container = new Container("box4", "descr", "Garage");
        container.addItem(new Item("tools", "descr"));
        container.addItem(new Item("saw", "descr"));
        container.addItem(new Item("car equipment", "descr"));
        containers.add(container);

        container = new Container("box5", "descr", "Outside");
        container.addItem(new Item("tools", "descr"));
        container.addItem(new Item("pavers", "descr"));
        containers.add(container);

        container = new Container("box6", "descr", "Bedroom");
        container.addItem(new Item("clothes", "descr"));
        container.addItem(new Item("sweaters", "descr"));
        container.addItem(new Item("costumes", "descr"));
        containers.add(container);

        container = new Container("box7", "descr", "Bathroom");
        container.addItem(new Item("hats", "descr"));
        container.addItem(new Item("presents", "descr"));
        container.addItem(new Item("books", "descr"));
        container.addItem(new Item("computer", "descr"));
        container.addItem(new Item("piano", "descr"));
        containers.add(container);

        container = new Container("box8", "descr", "Bedroom");
        container.addItem(new Item("baby clothes", "descr"));
        container.addItem(new Item("baby socks", "descr"));
        containers.add(container);

        container = new Container("box9", "descr", "Bedroom");
        container.addItem(new Item("diapers", "descr"));
        container.addItem(new Item("baby medicine", "descr"));
        containers.add(container);

        container = new Container("box10", "descr", "Bedroom");
        container.addItem(new Item("toys", "descr"));
        container.addItem(new Item("legos", "descr"));
        containers.add(container);

        container = new Container("box11", "descr", "Bathroom");
        container.addItem(new Item("glasses", "descr"));
        container.addItem(new Item("tables", "descr"));
        containers.add(container);

        container = new Container("box12", "descr", "Garage");
        container.addItem(new Item("chemicals", "descr"));
        container.addItem(new Item("car stuff", "descr"));
        containers.add(container);

        container = new Container("box13", "descr", "Garage");
        container.addItem(new Item("fishing stuff", "descr"));
        container.addItem(new Item("car stuff", "descr"));
        containers.add(container);

        container = new Container("box14", "descr", "Bedroom");
        container.addItem(new Item("books", "descr"));
        container.addItem(new Item("music", "descr"));
        containers.add(container);

        return containers;
    }

    /*private ArrayList<Container> populateBoxes() {
        Log.d(TAG, "populating boxes");
        ArrayList<Container> containers = new ArrayList<>();


        Container container = new Container("box1", Globals.Rooms.Bathroom);
        container.addItem(new Item("medicine"));
        container.addItem(new Item("soap"));
        containers.add(container);

        container = new Container("box2", Globals.Rooms.Bedroom);
        container.addItem(new Item("guns"));
        container.addItem(new Item("blurays"));
        container.addItem(new Item("humidifier"));
        containers.add(container);

        container = new Container("box3", Globals.Rooms.Kitchen);
        container.addItem(new Item("pots"));
        container.addItem(new Item("cups"));
        containers.add(container);

        container = new Container("box4", Globals.Rooms.Garage);
        container.addItem(new Item("tools"));
        container.addItem(new Item("saw"));
        container.addItem(new Item("car equipment"));
        containers.add(container);

        container = new Container("box5", Globals.Rooms.Outside);
        container.addItem(new Item("tools"));
        container.addItem(new Item("pavers"));
        containers.add(container);

        container = new Container("box6", Globals.Rooms.Bedroom);
        container.addItem(new Item("clothes"));
        container.addItem(new Item("sweaters"));
        container.addItem(new Item("costumes"));
        containers.add(container);

        container = new Container("box7", Globals.Rooms.Bathroom);
        container.addItem(new Item("hats"));
        container.addItem(new Item("presents"));
        container.addItem(new Item("books"));
        container.addItem(new Item("computer"));
        container.addItem(new Item("piano"));
        containers.add(container);

        container = new Container("box8", Globals.Rooms.Bedroom);
        container.addItem(new Item("baby clothes"));
        container.addItem(new Item("baby socks"));
        containers.add(container);

        container = new Container("box9", Globals.Rooms.Bedroom);
        container.addItem(new Item("diapers"));
        container.addItem(new Item("baby medicine"));
        containers.add(container);

        container = new Container("box10", Globals.Rooms.Bedroom);
        container.addItem(new Item("toys"));
        container.addItem(new Item("legos"));
        containers.add(container);

        container = new Container("box11", Globals.Rooms.Bathroom);
        container.addItem(new Item("glasses"));
        container.addItem(new Item("tables"));
        containers.add(container);

        container = new Container("box12", Globals.Rooms.Garage);
        container.addItem(new Item("chemicals"));
        container.addItem(new Item("car stuff"));
        containers.add(container);

        container = new Container("box13", Globals.Rooms.Garage);
        container.addItem(new Item("fishing stuff"));
        container.addItem(new Item("car stuff"));
        containers.add(container);

        container = new Container("box14", Globals.Rooms.Bedroom);
        container.addItem(new Item("books"));
        container.addItem(new Item("music"));
        containers.add(container);

        return containers;
    }*/


/*    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate, creating test boxes");
        ArrayList<Box> boxes = populateBoxes();
        ListView listView = (ListView) findViewById(R.id.listViewStorageList);
        StorageListAdapter adapter = new StorageListAdapter(getActivity(), R.layout.storage_list_item, boxes);
        listView.setAdapter(adapter);
    }*/

    private class StorageListAdapter extends ArrayAdapter<Container> {
        private ArrayList<Container> containers;

        private StorageListAdapter(Context context, int resource, ArrayList<Container> containers) {
            super(context, resource, containers);
            this.containers = containers;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;

            if (null == view) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.storage_list_item, null);
            }

            Container container = containers.get(position);

            if (null != container) {
                TextView textViewRoom = (TextView) view.findViewById(R.id.textViewRoomName);
                TextView textViewBox = (TextView) view.findViewById(R.id.textViewBoxName);
                TextView textViewItem = (TextView) view.findViewById(R.id.textViewItemName);

                textViewBox.setText(container.getName());
                textViewItem.setText(container.getAllItemNames());
                textViewRoom.setText(container.getLocation().toString());
            }
            return view;
        }
    }
}
