package com.marshong.packitup.ui.item;

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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.marshong.packitup.R;
import com.marshong.packitup.data.DBHelper;
import com.marshong.packitup.model.Item;
import com.marshong.packitup.ui.storage.StorageListActivity;

import java.util.ArrayList;

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
    public static class AddItemFragment extends Fragment {

        public static final String TAG = AddItemFragment.class.getSimpleName();
        private EditText mEditTextItemName;
        private EditText mEditTextItemDescr;
        private Spinner mSpinnerContainers;


        DBHelper db;

        public AddItemFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_add_item, container, false);

            Log.d(TAG, "onCreateView");

            db = new DBHelper(getActivity());
            final ArrayList<String> containerNames = db.getAllContainerNames();

            mEditTextItemName = (EditText) rootView.findViewById(R.id.edit_text_item_name);
            mEditTextItemDescr = (EditText) rootView.findViewById(R.id.edit_text_item_descr);

            //setup the container spinner
            mSpinnerContainers = (Spinner) rootView.findViewById(R.id.spinner_item_container);
            ArrayAdapter<String> aa = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, containerNames);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            mSpinnerContainers.setAdapter(aa);

            rootView.findViewById(R.id.button_add_item).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Item item = new Item(mEditTextItemName.getText().toString(), mEditTextItemDescr.getText().toString());

                    int selectedItem = mSpinnerContainers.getSelectedItemPosition();
                    String contName = containerNames.get(selectedItem);

                    item.setContainer(contName);
                    Log.d(TAG, "adding item: " + item);
                    db.insertItem(item);
                    Toast.makeText(getActivity(), "Added new Item " + item, Toast.LENGTH_LONG);

                    Intent intent = new Intent(getActivity(),StorageListActivity.class);
                    startActivity(intent);
                }
            });


            return rootView;
        }
    }
}
