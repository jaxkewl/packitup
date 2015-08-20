package com.marshong.packitup.util;

/**
 * Created by martin on 8/18/2015.
 */
public class Constants {

    //search for imgur api registration to get these values
    /**
     * Client ID for Android310 app registered with Imgur, oauth2.0 without callback url
     */
    public static final String IMGUR_CLIENT_ID = "014ba82d7b3e578";

    /**
     * Client Secret for Android310 app registered with Imgur
     */
    public static final String IMGUR_CLIENT_SECRET = "935d75ebcdef50b14c13c1cb334ab569fb9a081b";

    public static final boolean LOGGING = false;

    /*
     Client Auth
    */
    public static String getClientAuth() {
        return "Client-ID " + IMGUR_CLIENT_ID;
    }


    //this is the name of the shared preferences file
    public static final String sharedPrefName = "packitup_prefs";

    //store the page the user is currently on so we know what page to return to
    public static final String locationId = "locationId";
    public static final String containerId = "containerId";
}
