package com.marshong.packitup.data;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by martin on 3/10/2015.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String TAG = DBHelper.class.getSimpleName();

    private static final HashMap<String, String> mColumnMap = buildColumnMap();

    public DBHelper(Context context) {
        super(context, DBContract.DATABASE_NAME, null, DBContract.DB_VERSION);
    }


    /**
     * Builds a map for all columns that may be requested, which will be given to the
     * SQLiteQueryBuilder. This is a good way to define aliases for column names, but must include
     * all columns, even if the value is the key. This allows the ContentProvider to request
     * columns w/o the need to know real column names and create the alias itself.
     */
    private static HashMap<String, String> buildColumnMap() {

        Log.d(TAG, "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% BUILDING COLUMN MAP!!!!!");

        HashMap<String, String> map = new HashMap<String, String>();
        map.put(DBContract.Item.ITEM_NAME, DBContract.Item.ITEM_NAME);
        map.put(DBContract.Item.ITEM_DESCR, DBContract.Item.ITEM_DESCR);
        map.put(DBContract.Item.CONTAINER_REF, DBContract.Item.CONTAINER_REF);

        //mandatory
        map.put(DBContract.Item.ITEM_ID, DBContract.Item.ITEM_ID);

        //map.put(SearchManager.SUGGEST_COLUMN_SHORTCUT_ID,  "rowid AS " + SearchManager.SUGGEST_COLUMN_SHORTCUT_ID);
        //map.put( SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID, "rowid AS " + SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID );

        //map.put(SearchManager.SUGGEST_COLUMN_SHORTCUT_ID, DBContract.Item.ITEM_ID + " AS " + SearchManager.SUGGEST_COLUMN_SHORTCUT_ID);
        //map.put( SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID, DBContract.Item.ITEM_ID + " AS " + SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID );

        //map.put(DBContract.Item.ITEM_NAME, DBContract.Item.ITEM_NAME + " AS " + SearchManager.SUGGEST_COLUMN_TEXT_1);
        //map.put(DBContract.Item.ITEM_ID, DBContract.Item.ITEM_ID + " AS " + SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID);

        map.put(SearchManager.SUGGEST_COLUMN_TEXT_1, DBContract.Item.ITEM_NAME + " AS " + SearchManager.SUGGEST_COLUMN_TEXT_1);
        map.put(SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID, DBContract.Item.ITEM_ID + " AS " + SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID);

        // Icon for Suggestions ( Optional )
        //map.put( SearchManager.SUGGEST_COLUMN_ICON_1, FIELD_FLAG + " as " + SearchManager.SUGGEST_COLUMN_ICON_1);

        // This value will be appended to the Intent data on selecting an item from Search result or Suggestions ( Optional )
        //map.put( SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID, FIELD_ID + " as " + SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID );


        //map.put(DBContract.Item.ITEM_NAME, DBContract.Item.ITEM_NAME);
        //map.put(DBContract.Item.ITEM_DESCR, DBContract.Item.ITEM_DESCR);
        //map.put(DBContract.Item.CONTAINER_REF, DBContract.Item.CONTAINER_REF);

        //map.put(DBContract.Item.ITEM_ID, DBContract.Item.ITEM_ID + " as " + DBContract.Item.ITEM_ID);
        //map.put(SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID, "rowid AS " + SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID);
        //map.put(SearchManager.SUGGEST_COLUMN_SHORTCUT_ID, "rowid AS " + SearchManager.SUGGEST_COLUMN_SHORTCUT_ID);
        //map.put(SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID, " AS " + SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID);
        //map.put(SearchManager.SUGGEST_COLUMN_SHORTCUT_ID, " AS " +SearchManager.SUGGEST_COLUMN_SHORTCUT_ID);


        //columnMap.put(BaseColumns._ID, ToursDBOpenHelper.COLUMN_ID + " AS " + BaseColumns._ID);
        //map.put(SearchManager.SUGGEST_COLUMN_TEXT_1, DBContract.Item.ITEM_NAME + " AS " + SearchManager.SUGGEST_COLUMN_TEXT_1);
        //map.put(SearchManager.SUGGEST_COLUMN_INTENT_DATA, DBContract.Item.ITEM_ID + " AS " + SearchManager.SUGGEST_COLUMN_INTENT_DATA);

        //map.put(SearchManager.SUGGEST_COLUMN_TEXT_1, DBContract.Item.ITEM_NAME);
        //map.put(SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID, DBContract.Item.ITEM_ID + " rowid AS " + SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID);


        for (String name : map.keySet()) {
            String key = name.toString();
            String value = map.get(name).toString();
            Log.d(TAG, "%%%%%%%%%%%%%%% " + key + "                " + value);
        }


        return map;
    }

    //methods for finding search suggestions

    /**
     * Returns a Cursor over all words that match the given query
     *
     * @param query   The string to search for
     * @param columns The columns to include, if null then all are included
     * @return Cursor over all words that match, or null if none found.
     */
    public Cursor getWordMatches(String query, String[] columns) {
        Log.d(TAG, "getWordMatches called " + query + " " + columns.toString());
        //String selection = DBContract.Item.ITEM_NAME + " MATCH ?";
        String selection = DBContract.Item.ITEM_NAME + " LIKE ?";
        String[] selectionArgs = new String[]{query + "%"};


        Log.d(TAG, "selection: " + selection + " query: " + query);

        String selArgs = "";
        if (null != selectionArgs) {
            for (String s : selectionArgs) {
                selArgs += s + " ";
            }
        }
        Log.d(TAG, "selectionArgs: " + selArgs);

        return query(selection, selectionArgs, columns);

        /* This builds a query that looks like:
         *     SELECT <columns> FROM <table> WHERE <KEY_WORD> MATCH 'query*'
         * which is an FTS3 search for the query text (plus a wildcard) inside the word column.
         *
         * - "rowid" is the unique id for all rows but we need this value for the "_id" column in
         *    order for the Adapters to work, so the columns need to make "_id" an alias for "rowid"
         * - "rowid" also needs to be used by the SUGGEST_COLUMN_INTENT_DATA alias in order
         *   for suggestions to carry the proper intent data.
         *   These aliases are defined in the DictionaryProvider when queries are made.
         * - This can be revised to also search the definition text with FTS3 by changing
         *   the selection clause to use FTS_VIRTUAL_TABLE instead of KEY_WORD (to search across
         *   the entire table, but sorting the relevance could be difficult.
         */
    }


    /**
     * Performs a database query.
     *
     * @param selection     The selection clause
     * @param selectionArgs Selection arguments for "?" components in the selection
     * @param columns       The columns to return
     * @return A Cursor over all rows matching the query
     */
    private Cursor query(String selection, String[] selectionArgs, String[] columns) {
        /* The SQLiteBuilder provides a map for all possible columns requested to
         * actual columns in the database, creating a simple column alias mechanism
         * by which the ContentProvider does not need to know the real column names
         */
        Log.d(TAG, "%%%%%%%%%DictionaryDB query called " + selection);

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(DBContract.Item.ITEM_TABLE);

        builder.setProjectionMap(mColumnMap);


        Log.d(TAG, "setting queryBuilder table(s) to: " + builder.getTables());
        String dProj = "";

        if (null != columns) {
            for (String s : columns) {
                dProj += s + " ";
            }
        }
        Log.d(TAG, "projection: " + dProj);

        if (null != selectionArgs) {
            String dSel = "";
            for (String s : selectionArgs) {
                dSel += s + " ";
            }
            Log.d(TAG, "selectionArgs: " + dSel);
        }
        Log.d(TAG, "selections: " + selection);


        Cursor cursor = builder.query(getReadableDatabase(),
                columns, selection, selectionArgs, null, null, null);

        Log.d(TAG, "container count: " + cursor.getCount());

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
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
