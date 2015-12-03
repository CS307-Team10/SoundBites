package com.soundbytes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by Olumide on 10/7/2015.
 */
public class SoundByteConstants {
    public static final int TIME_LIMIT = 6000;
    public static final int MAX_TRACK_COUNT = 1;
    public static final String IS_GCM_KEY_STORED = "gcm_key";
    public static final String SENT = "SENT";
    public static final String RECEIVED = "RECEIVED";
    public static final String PACKAGE_NAME = "com.soundbytes";
    public static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
    public static final String dbUpdateBroadcast = PACKAGE_NAME+".DB_UPDATED";

    //the empty string is a placeholder for none
    public static final String[] FILTER_NAME = {"", "HIGH PITCH", "LOW PITCH", "SPEED UP", "SLOW DOWN"};
}
