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
    public static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static final DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    //the empty string is a placeholder for none
    public static final String[] FILTER_NAME = {"", "HIGH PITCH", "LOW PITCH", "HIGH FREQ", "LOW FREQ"};
}
