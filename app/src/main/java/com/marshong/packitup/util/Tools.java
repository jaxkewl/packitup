package com.marshong.packitup.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Map;

/**
 * Created by martin on 8/18/2015.
 */
public class Tools {
    public static String TAG = Tools.class.getSimpleName();


    public static void saveIds(int containerId, int locationId, Activity activity) {
        Log.d(TAG, "saveIds called... containerId: " + containerId + " locationId: " + locationId);

        SharedPreferences sharedPref = activity.getSharedPreferences(Constants.sharedPrefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(Constants.locationId, locationId);
        editor.putInt(Constants.containerId, containerId);

        editor.commit();

        displayAllSharedPref(activity);
    }

    //shared preferences section
    public static void displayAllSharedPref(Activity activity) {
        Log.d(TAG, "displayAllSharedPref called");

        SharedPreferences sharedPref = activity.getSharedPreferences(Constants.sharedPrefName, Context.MODE_PRIVATE);

        Map<String, ?> keys = sharedPref.getAll();

        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            Log.d(TAG, "***************** ***************** " + entry.getKey() + ": " + entry.getValue().toString());
        }
    }

}
