package com.marshong.packitup.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by martin on 3/10/2015.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String TAG = DBHelper.class.getSimpleName();

    public DBHelper(Context context) {
        super(context, DBContract.DATABASE_NAME, null, DBContract.DB_VERSION);
    }



    //create the tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate " + DBContract.Location.CREATE_TABLE_LOCATION);
        db.execSQL(DBContract.Location.CREATE_TABLE_LOCATION);

        Log.d(TAG, "onCreate " + DBContract.Owner.CREATE_TABLE_OWNER);
        db.execSQL(DBContract.Owner.CREATE_TABLE_OWNER);

        Log.d(TAG, "onCreate " + DBContract.Container.CREATE_TABLE_CONTAINER);
        db.execSQL(DBContract.Container.CREATE_TABLE_CONTAINER);

        Log.d(TAG, "onCreate " + DBContract.Item.CREATE_TABLE_ITEM);
        db.execSQL(DBContract.Item.CREATE_TABLE_ITEM);

        Log.d(TAG, "onCreate " + DBContract.ItemUri.CREATE_TABLE_ITEM_URI);
        db.execSQL(DBContract.ItemUri.CREATE_TABLE_ITEM_URI);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
