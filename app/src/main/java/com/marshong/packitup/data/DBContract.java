package com.marshong.packitup.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by martin on 3/10/2015.
 * <p/>
 * From android: A good way to organize a contract class is to put definitions
 * that are global to your whole database in the root level of the class.
 * Then create an inner class for each table that enumerates its columns.
 */

public class DBContract {

    // Name of the Content Provider, use package name by convention so that it's unique on device
    // kinda like domain name.
    public static final String CONTENT_AUTHORITY = "com.marshong.packitup";

    // A path that points to the task table.
    //Note: for multiple tables, you'll need another PATH, i.e. PATH_ANOTHERTABLE
    public static final String PATH_CONTAINER = "CONTAINER_TABLE";
    public static final String PATH_CONTAINER_FILTERED = "CONTAINER_TABLE_FILTERED";
    public static final String PATH_LOCATION = "LOCATION_TABLE";
    public static final String PATH_ITEM = "ITEM_TABLE";
    public static final String PATH_OWNER = "OWNER_TABLE";
    public static final String PATH_ITEM_URI = "ITEM_URI_TABLE";

    // Construct the Base Content Uri
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    //DB Name
    public static final String DATABASE_NAME = "storage_db";
    public static final int DB_VERSION = 1;


    public static final class Item implements BaseColumns {
        // -----------------------------------
        // ITEMS
        // -----------------------------------
        // Content Uri = Content Authority + Path
        //kinda like URL
        public static final Uri CONTENT_ITEM_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ITEM).build();

        //### item
        // Use MIME type prefix android.cursor.dir/ for returning multiple items
        public static final String CONTENT_ITEMS_TYPE =
                "vnd.android.cursor.dir/com.marshong.packitup.items";

        // Use MIME type prefix android.cursor.item/ for returning a single item
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/com.marshong.packitup.item";


        //item table
        public static final String ITEM_TABLE = PATH_ITEM;
        //Gotcha: The ID column must be named from the BaseColumns otherwise, the cursor loader
        //will not be able to find the column labeled _ID. in other words, you can not call the ID column
        //your own name, like "ITEM_ID".
        public static final String ITEM_ID = BaseColumns._ID;
        public static final String ITEM_NAME = "ITEM_NAME";
        public static final String ITEM_DESCR = "ITEM_DESCR";
        public static final String CONTAINER_REF = "CONTAINER_REF";

        public static final String CREATE_TABLE_ITEM = "CREATE TABLE " + ITEM_TABLE + " (" +
                ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                ITEM_NAME + " TEXT, " +
                ITEM_DESCR + " TEXT, " +
                CONTAINER_REF + " INTEGER, " +
                "FOREIGN KEY(" + CONTAINER_REF + ") REFERENCES " + Container.CONTAINER_TABLE + "(" + Container.CONTAINER_ID + "))";

        public static final String[] ITEM_PROJECTION = new String[]{
                ITEM_ID,
                ITEM_NAME,
                ITEM_DESCR,
                CONTAINER_REF};

        //Note: here is the SQL for an inner join to get all the items from a specified location
        //
        //select * from ITEM_TABLE inner join CONTAINER_TABLE on CONTAINER_TABLE.CONTAINER_LOCATION = 1;
        //             entries from ITEM_TABLE                     using CONTAINER_LOCATION of 1 as the ref.

    }


    public static final class Container implements BaseColumns {
        // -----------------------------------
        // Container
        // -----------------------------------
        // Content Uri = Content Authority + Path
        //kinda like URL
        public static final Uri CONTENT_CONTAINER_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CONTAINER).build();
        public static final Uri CONTENT_CONTAINER_FILTER_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CONTAINER_FILTERED ).build();


        //### container
        public static final String CONTENT_CONTAINERS_TYPE =
                "vnd.android.cursor.dir/com.marshong.packitup.containers";

        public static final String CONTENT_CONTAINER_TYPE =
                "vnd.android.cursor.item/com.marshong.packitup.container";

        //container table
        public static final String CONTAINER_TABLE = PATH_CONTAINER;
        public static final String CONTAINER_ID = BaseColumns._ID;
        public static final String CONTAINER_NAME = "CONTAINER_NAME";
        public static final String CONTAINER_DESCR = "CONTAINER_DESCR";
        public static final String CONTAINER_LOCATION = "CONTAINER_LOCATION";
        public static final String CONTAINER_OWNER = "CONTAINER_OWNER";
        public static final String CONTAINER_REMINDER = "CONTAINER_REMINDER";  // of the format MM/DD/YYYY HH:MM:SS AM/PM

        public static final String CREATE_TABLE_CONTAINER = "CREATE TABLE " + CONTAINER_TABLE + " (" +
                CONTAINER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                CONTAINER_NAME + " TEXT, " +
                CONTAINER_DESCR + " TEXT, " +
                CONTAINER_LOCATION + " INTEGER, " +
                CONTAINER_OWNER + " INTEGER, " +
                CONTAINER_REMINDER + " TEXT, " +
                "FOREIGN KEY(" + CONTAINER_OWNER + ") REFERENCES " + Owner.OWNER_TABLE + "(" + Owner.OWNER_ID + ")," +
                "FOREIGN KEY(" + CONTAINER_LOCATION + ") REFERENCES " + Location.LOCATION_TABLE + "(" + Location.LOCATION_ID + "))";

        //CREATE TABLE CONTAINER_TABLE (CONTAINER_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, CONTAINER_NAME TEXT, CONTAINER_DESCR TEXT, CONTAINER_LOCATION INTEGER, CONTAINER_OWNER INTEGER, CONTAINER_REMINDER TEXT, FOREIGN KEY(CONTAINER_OWNER) REFERENCES OWNER_TABLE(OWNER_ID),FOREIGN KEY(CONTAINER_LOCATION) REFERENCES LOCATION_TABLE(LOCATION_ID))


        public static final String[] CONTAINER_PROJECTION = new String[]{
                CONTAINER_ID,
                CONTAINER_NAME,
                CONTAINER_DESCR,
                CONTAINER_LOCATION,
                CONTAINER_OWNER,
                CONTAINER_REMINDER};
    }


    public static final class Location implements BaseColumns {
        // -----------------------------------
        // LOCATION
        // -----------------------------------
        // Content Uri = Content Authority + Path
        //kinda like URL
        public static final Uri CONTENT_LOCATION_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_LOCATION).build();

        //###location
        public static final String CONTENT_LOCATIONS_TYPE =
                "vnd.android.cursor.dir/com.marshong.packitup.locations";

        public static final String CONTENT_LOCATION_TYPE =
                "vnd.android.cursor.item/com.marshong.packitup.location";


        //location table
        public static final String LOCATION_TABLE = PATH_LOCATION;
        public static final String LOCATION_ID = BaseColumns._ID;
        public static final String LOCATION_NAME = "LOCATION_NAME";

        public static final String CREATE_TABLE_LOCATION = "CREATE TABLE " + LOCATION_TABLE + " (" +
                LOCATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                LOCATION_NAME + " TEXT)";

        public static final String[] LOCATION_PROJECTION = new String[]{
                LOCATION_ID,
                LOCATION_NAME};
    }


    public static final class Owner implements BaseColumns {
        // -----------------------------------
        // OWNER
        // -----------------------------------
        // Content Uri = Content Authority + Path
        //kinda like URL
        public static final Uri CONTENT_OWNER_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_OWNER).build();

        //###owner
        public static final String CONTENT_OWNERS_TYPE =
                "vnd.android.cursor.dir/com.marshong.packitup.owners";

        public static final String CONTENT_OWNER_TYPE =
                "vnd.android.cursor.item/com.marshong.packitup.owner";

        //owner information
        public static final String OWNER_TABLE = PATH_OWNER;
        public static final String OWNER_NAME = "OWNER_NAME";
        public static final String OWNER_ID = BaseColumns._ID;

        public static final String CREATE_TABLE_OWNER = "CREATE TABLE " + OWNER_TABLE + " (" +
                OWNER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                OWNER_NAME + " TEXT)";

        public static final String[] OWNER_PROJECTION = new String[]{
                OWNER_ID,
                OWNER_NAME};
    }


    public static final class ItemUri implements BaseColumns {
        // -----------------------------------
        // ITEM URI
        // -----------------------------------
        // Content Uri = Content Authority + Path
        //kinda like URL
        public static final Uri CONTENT_ITEM_URI_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ITEM_URI).build();

        //### item uri
        public static final String CONTENT_ITEM_URIS_TYPE =
                "vnd.android.cursor.dir/com.marshong.packitup.item_uris";

        public static final String CONTENT_ITEM_URI_TYPE =
                "vnd.android.cursor.item/com.marshong.packitup.item_uri";

        // ---------------------------
        //add on later
        // ---------------------------

        //item URI, for pictures and other resources
        public static final String ITEM_URI_TABLE = PATH_ITEM_URI;
        public static final String ITEM_URI_ID = BaseColumns._ID;
        public static final String ITEM_REF = "ITEM_REF";
        public static final String ITEM_URI = "ITEM_URI";

        public static final String CREATE_TABLE_ITEM_URI = "CREATE TABLE " + ITEM_URI_TABLE + " (" +
                ITEM_URI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                ITEM_URI + " TEXT, " +
                ITEM_REF + " INTEGER, " +
                "FOREIGN KEY(" + ITEM_REF + ") REFERENCES " + Item.ITEM_TABLE + "(" + Item.ITEM_ID + "))";

    }
}
