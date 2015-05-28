package com.marshong.packitup.data;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

/**
 * Created by martin on 5/22/2015.
 */

public class DBProvider extends ContentProvider {

    public final static String TAG = DBProvider.class.getSimpleName();

    private DBHelper mDbHelper;

    private static final UriMatcher sUriMatcher = createUriMatcher();

    // -------------------------------------
    // helper constants for the UriMatcher
    // -------------------------------------
    private static final int ITEM_ID = 100;
    private static final int ITEM_NAME = 101;

    private static final int ITEM_URI_ID = 102;
    private static final int ITEM_URI_NAME = 103;

    private static final int CONTAINER_ID = 200;
    private static final int CONTAINER_NAME = 201;

    private static final int LOCATION_ID = 300;
    private static final int LOCATION_NAME = 301;

    private static final int OWNER_ID = 400;
    private static final int OWNER_NAME = 401;


    private static UriMatcher createUriMatcher() {
        Log.d(TAG, "creating URI Matcher using " + DBContract.CONTENT_AUTHORITY);
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DBContract.CONTENT_AUTHORITY;

        //--------------------------------------------------------
        //this is how we will determine which table the uri is for.
        //we match the Uri to an integer value
        //--------------------------------------------------------

        uriMatcher.addURI(authority, DBContract.PATH_ITEM, ITEM_NAME);
        uriMatcher.addURI(authority, DBContract.PATH_ITEM + "/#", ITEM_ID);

        uriMatcher.addURI(authority, DBContract.PATH_ITEM_URI, ITEM_URI_NAME);
        uriMatcher.addURI(authority, DBContract.PATH_ITEM_URI + "/#", ITEM_URI_ID);

        uriMatcher.addURI(authority, DBContract.PATH_CONTAINER, CONTAINER_NAME);
        uriMatcher.addURI(authority, DBContract.PATH_CONTAINER + "/#", CONTAINER_ID);

        uriMatcher.addURI(authority, DBContract.PATH_LOCATION, LOCATION_NAME);
        uriMatcher.addURI(authority, DBContract.PATH_LOCATION + "/#", LOCATION_ID);

        return uriMatcher;
    }


    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreate from Provider called");

        //Note: this method runs on the UI thread

        //instantiate a new DBHelper class
        mDbHelper = new DBHelper(getContext());
        return true;
    }

    //GOTCHA: Make sure the column name of the ID, extends from BaseColumns._ID
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "query()... Querying the DB " + uri.toString());

        // Use SQLiteQueryBuilder for querying db
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // Record id
        String id;

        // Match Uri pattern
        int uriType = sUriMatcher.match(uri);

        Log.d(TAG, "URI Type: " + uriType);

        switch (uriType) {
            case ITEM_ID:
                // Set the table name after you determine which uri type was chosen
                queryBuilder.setTables(DBContract.Item.ITEM_TABLE);

                selection = DBContract.Item.ITEM_ID + " = ? ";
                id = uri.getLastPathSegment();
                selectionArgs = new String[]{id};
                Log.d(TAG, "looking for Item ID: " + id);
                break;
            case ITEM_NAME:
                //Log.d(TAG,"setting queryBuilder table to: " + DBContract.Item.ITEM_TABLE);
                // Set the table name after you determine which uri type was chosen
                queryBuilder.setTables(DBContract.Item.ITEM_TABLE);
                break;
            case LOCATION_NAME:
                // Set the table name after out which table was chosen
                queryBuilder.setTables(DBContract.Location.LOCATION_TABLE);
                break;
            case LOCATION_ID:
                // Set the table name after out which table was chosen
                queryBuilder.setTables(DBContract.Location.LOCATION_TABLE);

                selection = DBContract.Location.LOCATION_ID + " = ? ";
                id = uri.getLastPathSegment();
                selectionArgs = new String[]{id};
                Log.d(TAG, "looking for Location ID: " + id);

                break;

            case CONTAINER_ID:
                // Set the table name after out which table was chosen
                queryBuilder.setTables(DBContract.Container.CONTAINER_TABLE);

                selection = DBContract.Container.CONTAINER_ID + " = ? ";
                id = uri.getLastPathSegment();
                selectionArgs = new String[]{id};
                Log.d(TAG, "looking for CONTAINER ID: " + id);

                break;

            case CONTAINER_NAME:
                //Log.d(TAG,"setting queryBuilder table to: " + DBContract.Container.CONTAINER_TABLE);

                // Set the table name after out which table was chosen
                queryBuilder.setTables(DBContract.Container.CONTAINER_TABLE);
                break;

            case OWNER_ID:
                // Set the table name after out which table was chosen
                queryBuilder.setTables(DBContract.Owner.OWNER_TABLE);

                selection = DBContract.Owner.OWNER_ID + " = ? ";
                id = uri.getLastPathSegment();
                selectionArgs = new String[]{id};
                Log.d(TAG, "looking for OWNER ID: " + id);

                break;
            case OWNER_NAME:
                // Set the table name after out which table was chosen
                queryBuilder.setTables(DBContract.Owner.OWNER_TABLE);

                break;
            case ITEM_URI_ID:
                // Set the table name after out which table was chosen
                queryBuilder.setTables(DBContract.ItemUri.ITEM_URI_TABLE);

                selection = DBContract.ItemUri.ITEM_URI_ID + " = ? ";
                id = uri.getLastPathSegment();
                selectionArgs = new String[]{id};
                Log.d(TAG, "looking for URI ID: " + id);

                break;
            default:
                throw new IllegalArgumentException("!!!Forgot to handle UriType!!!: " + uri);

        }

        Log.d(TAG, "setting queryBuilder table(s) to: " + queryBuilder.getTables());
        String dProj = "";
        for (String s : projection) {
            dProj += s + " ";
        }
        Log.d(TAG, "projection: " + dProj);

        if (null!= selectionArgs) {
            String dSel = "";
            for (String s : selectionArgs) {
                dSel += s + " ";
            }
            Log.d(TAG, "selectionArgs: " + dSel);
        }
        Log.d(TAG, "selections: " + selection);
        Log.d(TAG, "sort order: " + sortOrder);


        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = queryBuilder.query(
                db,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        //GOTCHA: Registering an Observer in content resolver through cursor
        //So after updating something in the DB, call getContext().getContentResolver().notifyChange(insertedId, null);
        //like in method "insert" below
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }


    /*
        Note: Content Types are of the format type/subType
        vnd.android.cursor.dir - for when you expect the Cursor to contain 0..x items
        or
        vnd.android.cursor.item - for when you expect the Cursor to contain 1 item

        The subType portion can be either a well known subtype or something unique to your application
        So when using a ContentProvider you can customize the second subType portion of the MIME type,
        but not the first portion. e.g a valid MIME type for your apps ContentProvider could be:
        vnd.android.cursor.dir/vnd.myexample.whatever

        The MIME type returned from a ContentProvider can be used by an Intent to determine
        which activity to launch to handle the data retrieved from a given URI.

        vnd = vendor in MIME registration trees, android in this case

        getType(Uri uri) will usually only be called after a call to ContentResolver#getType(Uri uri).
        It is used by applications (either other third-party applications,
                                        if your ContentProvider has been exported, or your own)
        to retrieve the MIME type of the given content URL. If your app
        isn't concerned with the data's MIME type, it's perfectly fine to simply have the method return null.

        every CP must return the content type for its supported Uris.*//*
*/
    @Override
    public String getType(Uri uri) {
        Log.d(TAG, "getting Content Type from ContentProvider " + uri);

        switch ((sUriMatcher.match(uri))) {
            case ITEM_ID:
                return DBContract.Item.CONTENT_ITEM_TYPE;
            case ITEM_NAME:
                return DBContract.Item.CONTENT_ITEMS_TYPE;
            case CONTAINER_ID:
                return DBContract.Container.CONTENT_CONTAINER_TYPE;
            case CONTAINER_NAME:
                return DBContract.Container.CONTENT_CONTAINERS_TYPE;
            case OWNER_ID:
                return DBContract.Owner.CONTENT_OWNER_TYPE;
            case OWNER_NAME:
                return DBContract.Owner.CONTENT_OWNERS_TYPE;
            case ITEM_URI_ID:
                return DBContract.ItemUri.CONTENT_ITEM_URI_TYPE;
            case ITEM_URI_NAME:
                return DBContract.ItemUri.CONTENT_ITEM_URIS_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(TAG, "inserting into the DB... " + values);

        //get the object to the writeable DB
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Match Uri pattern
        int uriType = sUriMatcher.match(uri);
        Log.d(TAG, "URI Type: " + uriType);

        long rowId;

        switch (uriType) {
            case ITEM_NAME:

                rowId = db.insertOrThrow(DBContract.Item.ITEM_TABLE, null, values);

                //notify listeners of dataset changes
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(DBContract.Item.CONTENT_ITEM_URI, rowId);
            case CONTAINER_NAME:

                rowId = db.insertOrThrow(DBContract.Container.CONTAINER_TABLE, null, values);

                //notify listeners of dataset changes
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(DBContract.Container.CONTENT_CONTAINER_URI, rowId);

            case LOCATION_NAME:

                rowId = db.insertOrThrow(DBContract.Location.LOCATION_TABLE, null, values);

                //notify listeners of dataset changes
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(DBContract.Location.CONTENT_LOCATION_URI, rowId);
            default:
                throw new IllegalArgumentException("!!!Forgot to handle UriType!!!: " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}

