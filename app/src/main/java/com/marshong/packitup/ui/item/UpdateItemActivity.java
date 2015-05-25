package com.marshong.packitup.ui.item;

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
import android.widget.Spinner;

import com.marshong.packitup.R;
import com.marshong.packitup.data.DBHelper;

import java.util.ArrayList;

public class UpdateItemActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_item);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new UpdateItemFragment())
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

        private EditText mEditTextUpdateItemName;
        private EditText mEditTextUpdateItemDescr;
        private Spinner mSpinnerUpdateContainers;
        private DBHelper db;

        public UpdateItemFragment() {
        }

        private int findContainerPos(ArrayList<String> containers, String containerName) {
            int contPos = 1;

            for (int i = 0; i < containerName.length(); i++) {
                if (containers.get(i).equals(containerName)) return i;
            }
            return contPos;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_update_item, container, false);

            Log.d(TAG, "onCreateView updateItemFragment");

            /*db = new DBHelper(getActivity());
            final ArrayList<String> containerNames = db.getAllContainerNames();

            Bundle extras = getActivity().getIntent().getExtras();
            String itemName = extras.getString(DBContract.Version1.ITEM_NAME);
            String itemDescr = extras.getString(DBContract.Version1.ITEM_DESCR);
            String contId = extras.getString(DBContract.Version1.CONTAINER_ID);
            String contName = extras.getString(DBContract.Version1.CONTAINER_NAME);

            //find which position the container name is at


            mEditTextUpdateItemName = (EditText) rootView.findViewById(R.id.edit_text_item_name_update);
            mEditTextUpdateItemDescr = (EditText) rootView.findViewById(R.id.edit_text_item_descr_update);

            mEditTextUpdateItemDescr.setText(itemDescr);
            mEditTextUpdateItemName.setText(itemName);

            //setup the container spinner
            mSpinnerUpdateContainers = (Spinner) rootView.findViewById(R.id.spinner_item_container_update);
            ArrayAdapter<String> aa = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, containerNames);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            mSpinnerUpdateContainers.setAdapter(aa);
            mSpinnerUpdateContainers.setSelection(findContainerPos(containerNames, contName));


            rootView.findViewById(R.id.button_update_item).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Item item = new Item(mEditTextUpdateItemName.getText().toString(), mEditTextUpdateItemDescr.getText().toString());

                    int selectedItem = mSpinnerUpdateContainers.getSelectedItemPosition();
                    String contName = containerNames.get(selectedItem);

                    item.setContainer(contName);
                    Log.d(TAG, "updating item: " + item);
                    //db.insertItem(item);
                    Toast.makeText(getActivity(), "Updated item " + item, Toast.LENGTH_LONG);

                    Intent intent = new Intent(getActivity(), StorageListActivity.class);
                    startActivity(intent);
                }
            });*/


            return rootView;
        }
    }
}
