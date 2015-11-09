package com.soundbytes.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import com.soundbytes.SoundByteConstants;
import com.soundbytes.SoundByteFeedObject;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Olumide on 11/4/2015.
 */
public class FeedDatabaseHandler extends SQLiteOpenHelper implements DBAsyncResponse{
    private static final String DB_NAME = "News_Feed";
    private static final String TABLE_NAME = "News_Feed";
    private static final int VERSION = 1;

    //ID | SENT? | TO/FROM| DATE | TIME |FILENAME | FILTER | SPEED
    private static final String ID = "id";
    private static final String DATE = "date";
    private static final String TIME = "time";
    private static final String SOUND_PATH = "sound_path";
    private static final String IS_SENT = "sent_or_nah";
    private static final String FRIEND = "friend";
    private static final String FILTER = "filter";
    private static final String SPEED = "playback_speed";
    private SQLiteDatabase db = null;
    private Context context;
    private DBHandlerResponse dbHandlerResponse;
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    private Cursor cursor;

    private final String SELECT_QUERY = "SELECT * FROM " + TABLE_NAME;

    public FeedDatabaseHandler(Context context, DBHandlerResponse dbHandlerResponse){
        //Although dbHandlerResponse snd context would always be the same object
        // The constructor is setup this way to prevent casting errors if used incorrectly
        super(context, DB_NAME, null, VERSION);
        this.context = context;
        this.dbHandlerResponse = dbHandlerResponse;
        //Use Writable Db for both reading and writing. It's easier and simpler
        AsyncGetWritableDB asyncGetWritableDB = new AsyncGetWritableDB(this);
        asyncGetWritableDB.delegate = this;
        asyncGetWritableDB.execute();
    }

    public void processFinish(SQLiteDatabase db){
        this.db = db;
        cursor = db.rawQuery(SELECT_QUERY, null);
        dbHandlerResponse.onDBReady();
        Log.v("Db", "process finish");
    }

    public void cleanup(){
        db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        Log.e("Db", "onCreate");
        Log.e("Db", "onCreate");
        this.db = db;//Just in case
        //TABLE FORMAT
        //ID | SENT? | TO/FROM| DATE | TIME |FILENAME | FILTER | SPEED
        //ID | PICTURE PATH | CONCENTRATION| DATE | LONG | LAT | TIME | ICON PATH | HIDDEN | RGB | HSV |
        String CREATE_TABLE =
                String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s REAL)",
                        TABLE_NAME, ID, IS_SENT, FRIEND, DATE, TIME, SOUND_PATH, FILTER, SPEED);
        db.execSQL(CREATE_TABLE);
        Log.v("Db", "onCreate");
        cursor = db.rawQuery(SELECT_QUERY, null);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer){
        int incVer = oldVer;
        while(++incVer <= newVer){
            switch(incVer){
                case 2:
                    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
                    onCreate(db);
            }
        }
    }

    private List<SoundByteFeedObject> getFeed(){
        List<SoundByteFeedObject> feed = new ArrayList<SoundByteFeedObject>();
        //Loop through and return the stuff
        int count = getCount();
        SoundByteFeedObject feedObject;
        for(int i = 0; i < count; i++) {
            feedObject = getFeedObject(i);
            feed.add(feedObject);
        }
        return feed;
    }

    //This also includes Hidden items
    public int getCount() {
//        String countQuery = "SELECT  * FROM " + TABLE_NAME;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//        int count = cursor.getCount();
//        cursor.close();
//        return count;
        return 3;
    }

    public SoundByteFeedObject getFeedObject(int id){
        String soundPath = null;
        Date date = null;
        Date time = null;
        String friend = null;
        String filter = null;
        float speed = 1;
        boolean sent = false;

        if(cursor.moveToFirst()){
            cursor.moveToPosition(id);
            //ID | SENT? | TO/FROM| DATE | TIME |FILENAME | FILTER | SPEED
            sent = SoundByteConstants.SENT.equals(cursor.getString(1));
            friend = cursor.getString(2);
            try {
                date = dateFormat.parse(cursor.getString(3));
                time = timeFormat.parse(cursor.getString(4));
            }
            catch(ParseException p){
                //Not sure what to do here
            }
            soundPath = cursor.getString(5);
            filter = cursor.getString(6);
            speed = cursor.getFloat(7);
        }
        return new SoundByteFeedObject(id, sent, friend, date, time, /*new File(soundPath)*/null, filter, speed);
    }

    public void addToFeedDB(SoundByteFeedObject  feedObject){
        if(db != null){
            //ID | SENT? | TO/FROM| DATE | TIME |FILENAME | FILTER | SPEED
            ContentValues values = new ContentValues();
            values.put(ID, feedObject.getId());
            values.put(IS_SENT, feedObject.getIsSent());
            values.put(FRIEND, feedObject.getFriend());
            values.put(SOUND_PATH, feedObject.getAudioPath());
            values.put(DATE, dateFormat.format(feedObject.getDate()));
            values.put(TIME, timeFormat.format(feedObject.getTime()));
            values.put(SOUND_PATH, feedObject.getAudioPath());
            values.put(FILTER, feedObject.getFilter());
            values.put(SPEED, feedObject.getPlaybackSpeed());
            db.insert(TABLE_NAME, null, values);
        }
    }

    private class AsyncGetWritableDB extends AsyncTask<Object, Void, SQLiteDatabase> {
        private FeedDatabaseHandler dbHandler;
        public DBAsyncResponse delegate = null;

        public AsyncGetWritableDB(FeedDatabaseHandler dbh){
            dbHandler = dbh;
        }
        @Override
        protected SQLiteDatabase doInBackground(Object ... objects) {
            Log.e("BD", "BDBD");
            return dbHandler.getReadableDatabase();
        }

        @Override
        protected void onPostExecute(SQLiteDatabase db){
            delegate.processFinish(db);
        }
    }
}

