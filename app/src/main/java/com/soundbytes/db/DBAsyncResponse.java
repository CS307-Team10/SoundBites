package com.soundbytes.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Olumide on 11/4/2015.
 */
public interface DBAsyncResponse {
    void processFinish(SQLiteDatabase db);
}
