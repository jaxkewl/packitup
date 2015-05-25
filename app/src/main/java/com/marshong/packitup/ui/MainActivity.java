package com.marshong.packitup.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.marshong.packitup.R;
import com.marshong.packitup.data.DBHelper;
import com.marshong.packitup.ui.dbdev.DBDevActivity;
import com.marshong.packitup.ui.settings.SettingsActivity;
import com.marshong.packitup.ui.storage.StorageListActivity;


public class MainActivity extends ActionBarActivity {

    DBHelper db;
    public static final String TAG = MainActivity.class.getSimpleName();

    Button fromButton;
    Button toButton;
    Button mButtonStorage;

    LinearLayout mLLMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

/*        fromButton = (Button) findViewById(R.id.fromButtonMain);
        toButton = (Button) findViewById(R.id.toButtonMain);*/

        // get the image asset from the assets folder
        /*mLLMain = (LinearLayout)findViewById(R.id.ll_main);
        try {
            InputStream yellowBG = getAssets().open("drawable/yellow_textured_background.jpg");
            Drawable drawableYellowBG = Drawable.createFromStream(yellowBG, null);
            mLLMain.setBackground(drawableYellowBG);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Yellow Background missing " + e.getMessage(), Toast.LENGTH_LONG).show();
        }*/



        // process Box Activity
        mButtonStorage = (Button) findViewById(R.id.button_martin_stuff);
        mButtonStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StorageListActivity.class);
                startActivity(intent);
            }
        });

        doThingsOnlyOnce();
    }


    /*Using the shared preferences, determine if this app has been run before
    so we can do some thing only once*/
    private void doThingsOnlyOnce() {
        if (isFirstTime()) {
            Log.d(TAG, "first time running this app, preparing some things...");
            Toast.makeText(this, "App has not run before, executing first time actions", Toast.LENGTH_SHORT).show();
            db = new DBHelper(this);
            //db.insertDefaultLocAndCont();
        }
    }

    /*determine if this the first time running this app.*/
    private boolean isFirstTime() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean ranBefore = preferences.getBoolean("RanBefore", false);
        if (!ranBefore) {
            // first time
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("RanBefore", true);
            editor.commit();
        }
        return !ranBefore;
    }


    private void processFromActivity() {
        fromButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //we want to open up the From Main Activity Page
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, FromMainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            // start this activity
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
        } else if (id == R.id.action_search) {
            Toast.makeText(MainActivity.this, "Selected Search Action", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.action_db_dev) {
            Intent intent = new Intent(MainActivity.this, DBDevActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
