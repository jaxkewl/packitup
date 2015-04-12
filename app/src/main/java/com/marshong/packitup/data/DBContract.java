package com.marshong.packitup.data;

import android.provider.BaseColumns;

/**
 * Created by martin on 3/10/2015.
 */
public class DBContract implements BaseColumns {

    //DB Name
    public static final String DATABASE_NAME = "storage_db";
    public static final int DB_VERSION = 1;


    public static final class Version1 {

        //location table
        public static final String LOCATION_TABLE = "LOCATION_TABLE";
        public static final String LOCATION_ID = "LOCATION_ID";
        public static final String LOCATION_NAME = "LOCATION_NAME";

        public static final String CREATE_TABLE_LOCATION = "CREATE TABLE " + LOCATION_TABLE + " (" +
                LOCATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                LOCATION_NAME + " TEXT)";

        public static final String[] LOCATION_PROJECTION = new String[]{
                LOCATION_ID,
                LOCATION_NAME};


        //owner information
        public static final String OWNER_TABLE_NAME = "OWNER_TABLE";
        public static final String OWNER_NAME = "OWNER_NAME";
        public static final String OWNER_ID = "OWNER_ID";

        public static final String CREATE_TABLE_OWNER = "CREATE TABLE " + OWNER_TABLE_NAME + " (" +
                OWNER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                OWNER_NAME + " TEXT)";


        //container table
        public static final String CONTAINER_TABLE = "CONTAINER_TABLE";
        public static final String CONTAINER_ID = "CONTAINER_ID";
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
                "FOREIGN KEY(" + CONTAINER_OWNER + ") REFERENCES " + OWNER_TABLE_NAME + "(" + OWNER_ID + ")," +
                "FOREIGN KEY(" + CONTAINER_LOCATION + ") REFERENCES " + LOCATION_TABLE + "(" + LOCATION_ID + "))";

        //CREATE TABLE CONTAINER_TABLE (CONTAINER_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, CONTAINER_NAME TEXT, CONTAINER_DESCR TEXT, CONTAINER_LOCATION INTEGER, CONTAINER_OWNER INTEGER, CONTAINER_REMINDER TEXT, FOREIGN KEY(CONTAINER_OWNER) REFERENCES OWNER_TABLE(OWNER_ID),FOREIGN KEY(CONTAINER_LOCATION) REFERENCES LOCATION_TABLE(LOCATION_ID))


        public static final String[] CONTAINER_PROJECTION = new String[]{
                CONTAINER_ID,
                CONTAINER_NAME,
                CONTAINER_DESCR,
                CONTAINER_LOCATION,
                CONTAINER_OWNER,
                CONTAINER_REMINDER};


        //item table
        public static final String ITEM_TABLE = "ITEM_TABLE";
        public static final String ITEM_ID = "ITEM_ID";
        public static final String ITEM_NAME = "ITEM_NAME";
        public static final String ITEM_DESCR = "ITEM_DESCR";
        public static final String CONTAINER_REF = "CONTAINER_REF";

        public static final String CREATE_TABLE_ITEM = "CREATE TABLE " + ITEM_TABLE + " (" +
                ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                ITEM_NAME + " TEXT, " +
                ITEM_DESCR + " TEXT, " +
                CONTAINER_REF + " INTEGER, " +
                "FOREIGN KEY(" + CONTAINER_REF + ") REFERENCES " + CONTAINER_TABLE + "(" + CONTAINER_ID + "))";

        public static final String[] ITEM_PROJECTION = new String[]{
                ITEM_ID,
                ITEM_NAME,
                ITEM_DESCR,
                CONTAINER_REF};


        // ---------------------------
        //add on later
        // ---------------------------

        //item URI, for pictures and other resources
        public static final String ITEM_URI_TABLE = "ITEM_URI_TABLE";
        public static final String ITEM_URI_ID = "ITEM_URI_ID";
        public static final String ITEM_REF = "ITEM_REF";
        public static final String ITEM_URI = "ITEM_URI";

        public static final String CREATE_TABLE_ITEM_URI = "CREATE TABLE " + ITEM_URI_TABLE + " (" +
                ITEM_URI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                ITEM_URI + " TEXT, " +
                ITEM_REF + " INTEGER, " +
                "FOREIGN KEY(" + ITEM_REF + ") REFERENCES " + ITEM_TABLE + "(" + ITEM_ID + "))";




    }


}
