package com.soundbytes;

/**
 * Created by Olumide on 11/2/2015.
 */
public interface GCMKeyCallback {
    void onKeyReturned(String key);
    void onKeyDeleted();
}
