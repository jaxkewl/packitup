package com.marshong.packitup.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by martin on 3/10/2015.
 */
public class DBHelper2 extends SQLiteOpenHelper {

    public static final String TAG = DBHelper2.class.getSimpleName();

    public DBHelper2(Context context) {
        super(context, DBContract.DATABASE_NAME, null, DBContract.DB_VERSION);
    }

    public void wipeAllFromDB() {
        Log.d(TAG, "wiping all from DB");
        //wipe from items table first
/*        wipeAllItems();

        //wipe from container table next
        wipeAllContainers();

        //wipe from location tabl last.
        wipeAllLocations();

        //wipe all owners
        wipeAllOwners();*/
    }


    public void insertDefaultLocAndCont() {
/*        String defLocation = "Temp";
        String defCont = "Default Container";

        //insert in a default container and location, so there is initially something to display
        Log.d(TAG, "inserting default location: " + defLocation + " and container: " + defCont);

        insertLocation(defLocation);

        //set the container and the IDs since its the first one
        Container cont = new Container(defCont, "Temp Container", defLocation);
        //cont.setLocationID(1);
        int locationID = getLocationID(defLocation);

        cont.setId(locationID);

        insertContainer(cont);*/
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


    public void insertSampleItems() {
/*        Log.d(TAG, "inserting sample items");
        ArrayList<Container> allContainers = getAllContainers();

        for (int i = 0; i < 15; i++) {
            Item item = new Item("itemname" + i, "descr" + i);
            Random rand = new Random();
            //String[] locs = new String[]{"Bathroom", "BedRoom", "Living Room", "Backyard", "Frontyard", "Garage", "Kitchen"};
            int containerRef = rand.nextInt(allContainers.size());
            //Log.d(TAG, "------------ randomInt is " + containerRef);
            int containerID = getContainerID(allContainers.get(containerRef).getName());
            //Log.d(TAG, "+++++++++++ found container ID " + containerID);
            item.setContainerID(containerID);
            item.setContainer(allContainers.get(containerRef).getName());
            insertItem(item);
        }*/
    }
/*

    public void insertItem(Item item) {
        Log.d(TAG, "insertItem " + item);

        //make sure we have a valid location ID first
        if (-1 == item.getContainerID()) {
            //find the location ID and set it.
            Log.d(TAG, "Container ID not set, setting now for, " + item.getContainer());
            item.setContainerID(getContainerID(item.getContainer()));
        }

        SQLiteDatabase db = this.getWritableDatabase();

        //setup the insert values
        ContentValues cv = new ContentValues();
        cv.put(DBContract.Version1.ITEM_NAME, item.getName());
        cv.put(DBContract.Version1.ITEM_DESCR, item.getDescr());
        cv.put(DBContract.Version1.CONTAINER_REF, item.getContainerID());

        //insert the item
        db.insert(DBContract.Version1.ITEM_TABLE, null, cv);

        //close out the DB
        db.close();
    }


    public void insertSampleContainers() {
        Log.d(TAG, "inserting in sample containers");

        Random rand = new Random();
        //String[] locs = new String[]{"Bathroom", "BedRoom", "Living Room", "Backyard", "Frontyard", "Garage", "Kitchen"};

        ArrayList<String> allLocations = getAllLocationNames();
        //ArrayList<Integer> allLocationIds = getAllLocationIds();

        for (int i = 0; i < 5; i++) {
            int locChoice = rand.nextInt(allLocations.size());
            String location = allLocations.get(locChoice);

            Container c = new Container("Container" + i, "descr" + i, location);
            int locID = getLocationID(allLocations.get(locChoice));     //get the Loc ID using the name
            c.setLocationID(locID);
            insertContainer(c);
        }
    }

    public void insertContainer(Container container) {
        Log.d(TAG, "insertContainer " + container);

        //make sure we have a valid location ID first
        if (-1 == container.getLocationID()) {
            //find the location ID and set it.
            Log.d(TAG, "location ID not set, setting now for, " + container.getLocation());
            container.setLocationID(getLocationID(container.getLocation()));
        }

        // get writeable ic_db_icon
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DBContract.Version1.CONTAINER_NAME, container.getName());
        cv.put(DBContract.Version1.CONTAINER_DESCR, container.getDescr());


        cv.put(DBContract.Version1.CONTAINER_LOCATION, container.getLocationID());

        //insert new container
        db.insert(DBContract.Version1.CONTAINER_TABLE, null, cv);

        //close out ic_db_icon
        db.close();
    }


    public void insertSampleLocations() {
        Log.d(TAG, "inserting sample locations");
        String[] sampleLocs = new String[]{"Public Storage", "Attic", "Digital"};
        for (String str : sampleLocs) {
            insertLocation(str);
        }
    }

    public void insertLocation(String location) {
        Log.d(TAG, "insertLocation " + location);

        //get writeable ic_db_icon
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DBContract.Version1.LOCATION_NAME, location);

        //insert new location
        db.insert(DBContract.Version1.LOCATION_TABLE, null, cv);

        //close
        db.close();
    }

    public void insertSampleOwners() {
        Log.d(TAG, "inserting sample owners");
        String[] sampleOwners = new String[]{"Martin", "Marge", "Marcus", "Mason"};
        for (String str : sampleOwners) {
            insertOwner(str);
        }
    }

    public void insertOwner(String owner) {
        Log.d(TAG, "insertOwner " + owner);

        //get writeable ic_db_icon
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DBContract.Version1.OWNER_NAME, owner);

        //insert new location
        db.insert(DBContract.Version1.OWNER_TABLE, null, cv);

        //close
        db.close();
    }

    //this method will get all the locations
    public ArrayList<Integer> getAllLocationIds() {
        Log.d(TAG, "Getting all location Ids from " + DBContract.Version1.LOCATION_TABLE + " using projection: " + DBContract.Version1.LOCATION_PROJECTION.toString());

        ArrayList<Integer> locationIds = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DBContract.Version1.LOCATION_TABLE,
                DBContract.Version1.LOCATION_PROJECTION,
                null,
                null,
                null,
                null,
                null);

        Log.d(TAG, "Found " + cursor.getCount() + " locations");

        //move to the first result
        cursor.moveToFirst();

        //create a container object for each row from the container table
        while (!cursor.isAfterLast()) {
            int locationColID = cursor.getColumnIndex(DBContract.Version1.LOCATION_ID);
            int locationId = cursor.getInt(locationColID);
            locationIds.add(locationId);
            cursor.moveToNext();
        }

        //close
        cursor.close();
        db.close();

        return locationIds;
    }

    //this method will get all the locations
    public ArrayList<String> getAllLocationNames() {
        Log.d(TAG, "Getting all location names from " + DBContract.Version1.LOCATION_TABLE + " using projection: " + DBContract.Version1.LOCATION_PROJECTION.toString());

        ArrayList<String> locations = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DBContract.Version1.LOCATION_TABLE,
                DBContract.Version1.LOCATION_PROJECTION,
                null,
                null,
                null,
                null,
                null);

        Log.d(TAG, "Found " + cursor.getCount() + " locations");

        //move to the first result
        cursor.moveToFirst();

        //create a container object for each row from the container table
        while (!cursor.isAfterLast()) {
            int locationColID = cursor.getColumnIndex(DBContract.Version1.LOCATION_NAME);
            String location = cursor.getString(locationColID);
            locations.add(location);
            cursor.moveToNext();
        }

        //close
        cursor.close();
        db.close();

        return locations;
    }


    //this method will get all the containers
    public ArrayList<Container> getAllContainers() {
        Log.d(TAG, "Getting all containers from " + DBContract.Version1.CONTAINER_TABLE + " using projection: " + DBContract.Version1.CONTAINER_PROJECTION.toString());

        ArrayList<Container> containers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DBContract.Version1.CONTAINER_TABLE,
                DBContract.Version1.CONTAINER_PROJECTION,
                null,
                null,
                null,
                null,
                null);

        Log.d(TAG, "Found " + cursor.getCount() + " containers");

        //move to the first result
        cursor.moveToFirst();

        //create a container object for each row from the container table
        while (!cursor.isAfterLast()) {
            Container container = createContainerFromCursor(cursor);
            containers.add(container);
            cursor.moveToNext();
        }

        //close
        cursor.close();
        db.close();

        return containers;
    }

    public ArrayList<String> getAllContainerNames() {
        ArrayList<String> containerNames = new ArrayList<>();

        Log.d(TAG, "Getting all container names");
        ArrayList<Container> containers = getAllContainers();

        for (Container c : containers) {
            containerNames.add(c.getName());
        }

        return containerNames;
    }

    private Container createContainerFromCursor(Cursor cursor) {
        Log.d(TAG, "Creating container from cursor ");

        int containerID = cursor.getInt(0);

        //get container name
        int containerNameColID = cursor.getColumnIndex(DBContract.Version1.CONTAINER_NAME);
        String containerName = cursor.getString(containerNameColID);

        //get container descr
        int containerDescrColID = cursor.getColumnIndex(DBContract.Version1.CONTAINER_DESCR);
        String containerDescr = cursor.getString(containerDescrColID);

        //get location information
        int locationColID = cursor.getColumnIndex(DBContract.Version1.CONTAINER_LOCATION);
        int locationID = cursor.getInt(locationColID);

        String locationName = getLocationName(locationID);
        Container container = new Container(containerName, containerDescr, locationName);
        container.setLocationID(locationID);
        container.setId(containerID);
        return container;
    }


    //this method will get all the items
    public ArrayList<Item> getAllItems() {
        Log.d(TAG, "Getting all items from " + DBContract.Version1.ITEM_TABLE + " using projection: " + DBContract.Version1.ITEM_PROJECTION.toString());

        ArrayList<Item> items = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DBContract.Version1.ITEM_TABLE,
                DBContract.Version1.ITEM_PROJECTION,
                null,
                null,
                null,
                null,
                null);

        Log.d(TAG, "Found " + cursor.getCount() + " items");

        //move to the first result
        cursor.moveToFirst();

        //create a container object for each row from the container table
        while (!cursor.isAfterLast()) {
            Item item = createItemFromCursor(cursor);
            items.add(item);
            cursor.moveToNext();
        }

        //close
        cursor.close();
        db.close();

        return items;
    }


    private Item createItemFromCursor(Cursor cursor) {
        Log.d(TAG, "Creating item from cursor ");

        int itemID = cursor.getInt(0);

        //get item name
        int itemNameColID = cursor.getColumnIndex(DBContract.Version1.ITEM_NAME);
        String itemName = cursor.getString(itemNameColID);

        //get item descr
        int itemDescrColID = cursor.getColumnIndex(DBContract.Version1.ITEM_DESCR);
        String itemDescr = cursor.getString(itemDescrColID);

        //get container information
        int containerColID = cursor.getColumnIndex(DBContract.Version1.CONTAINER_REF);
        int containerID = cursor.getInt(containerColID);

        String containerName = getContainerName(containerID);
        Item item = new Item(itemName, itemDescr);
        item.setContainerID(containerID);
        item.setContainer(containerName);
        item.setId(itemID);
        return item;
    }


    //-------------------------------------------------------------//
    //these methods below will get a single row from each table
    //-------------------------------------------------------------//

    */
/*Returns the Unique Item ID from the items table*//*

    private int getItemID(Item item) {
        Log.d(TAG, "getItemID " + item);

        int itemID = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        String whereClause = DBContract.Version1.ITEM_NAME + "=?," + DBContract.Version1.CONTAINER_REF + "=?";
        String[] whereArgs = new String[]{item.getName(), Integer.toString(item.getContainerID())};

        Cursor cursor = db.query(DBContract.Version1.ITEM_TABLE, DBContract.Version1.ITEM_PROJECTION, whereClause, whereArgs, null, null, null);
        cursor.moveToFirst();
        itemID = cursor.getInt(0);
        db.close();
        return itemID;
    }

    //returns the location name
    private String getLocationName(int locationID) {
        Log.d(TAG, "getLocationName " + locationID);
        String locationName = "";

        //get the database and setup the query
        SQLiteDatabase db = this.getReadableDatabase();
        String whereClause = DBContract.Version1.LOCATION_ID + "=?";
        String[] whereArgs = new String[]{Integer.toString(locationID)};

        //execute the query and get the location ID from the results
        Cursor cursor = db.query(DBContract.Version1.LOCATION_TABLE, DBContract.Version1.LOCATION_PROJECTION, whereClause, whereArgs, null, null, null);
        cursor.moveToFirst();
        locationName = cursor.getString(1);  //this should be the Location String
        Log.d(TAG, "Found location " + locationName);
        db.close();
        return locationName;
    }


    //returns the unique ID from the location table
    private int getLocationID(String location) {
        Log.d(TAG, "getLocationID " + location);
        int locationID = -1;

        //get the database and setup the query
        SQLiteDatabase db = this.getReadableDatabase();
        String whereClause = DBContract.Version1.LOCATION_NAME + "=?";
        String[] whereArgs = new String[]{location};

        //execute the query and get the location ID from the results
        Cursor cursor = db.query(DBContract.Version1.LOCATION_TABLE, DBContract.Version1.LOCATION_PROJECTION, whereClause, whereArgs, null, null, null);
        cursor.moveToFirst();
        locationID = cursor.getInt(0);  //this should be the Location ID
        db.close();
        return locationID;
    }


    //returns the unique container ID from the container table
    private int getContainerID(Container container) {
        Log.d(TAG, "getContainerID " + container);
        int containerID = -1;

        //use the location name to get the unique location ID
        int containerLoc = getLocationID(container.getLocation());

        //get the database and setup the query
        SQLiteDatabase db = this.getReadableDatabase();
        String whereClause = DBContract.Version1.CONTAINER_LOCATION + "=?," +
                DBContract.Version1.CONTAINER_NAME + "=?, " +
                DBContract.Version1.CONTAINER_DESCR + "=?";


        String[] whereArgs = new String[]{Integer.toString(containerLoc), container.getName(), container.getDescr()};

        //execute the query and get the container ID from the results
        Cursor cursor = db.query(DBContract.Version1.CONTAINER_TABLE, DBContract.Version1.CONTAINER_PROJECTION, whereClause, whereArgs, null, null, null);
        cursor.moveToFirst();
        containerID = cursor.getInt(0); //this should be the container ID

        db.close();
        return containerID;
    }


    //returns the container name
    private String getContainerName(int containerID) {
        Log.d(TAG, "getContainerName " + containerID);
        String containerName = "";

        //get the database and setup the query
        SQLiteDatabase db = this.getReadableDatabase();
        String whereClause = DBContract.Version1.CONTAINER_ID + "=?";
        String[] whereArgs = new String[]{Integer.toString(containerID)};

        //execute the query and get the container name from the results
        Cursor cursor = db.query(DBContract.Version1.CONTAINER_TABLE, DBContract.Version1.CONTAINER_PROJECTION, whereClause, whereArgs, null, null, null);
        cursor.moveToFirst();
        containerName = cursor.getString(1);  //this should be the Container String
        Log.d(TAG, "Found container " + containerName);
        db.close();
        return containerName;
    }

    private int getContainerID(String container) {
        int containerID = 0;

        Log.d(TAG, "getting container ID for container name: " + container);
        SQLiteDatabase db = this.getReadableDatabase();
        String whereClause = DBContract.Version1.CONTAINER_NAME + "=?";
        String[] whereArgs = new String[]{container};

        //execute the query and get the container ID from the results
        Cursor cursor = db.query(DBContract.Version1.CONTAINER_TABLE, DBContract.Version1.CONTAINER_PROJECTION, whereClause, whereArgs, null, null, null);
        cursor.moveToFirst();
        containerID = cursor.getInt(0);  //this should be the Container ID
        Log.d(TAG, "Found container " + containerID);
        db.close();

        return containerID;
    }


    private void wipeAllOwners() {
        Log.d(TAG, "wiping all owners");
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DBContract.Version1.OWNER_TABLE, null, null);
        db.close();
    }

    private void wipeAllLocations() {
        Log.d(TAG, "wiping all locations");
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DBContract.Version1.LOCATION_TABLE, null, null);
        db.close();
    }


    private void wipeAllContainers() {
        Log.d(TAG, "wiping all containers");
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DBContract.Version1.CONTAINER_TABLE, null, null);
        db.close();
    }

    private void wipeAllItems() {
        Log.d(TAG, "wiping all items");
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DBContract.Version1.ITEM_TABLE, null, null);
        db.close();
    }
*/
}
