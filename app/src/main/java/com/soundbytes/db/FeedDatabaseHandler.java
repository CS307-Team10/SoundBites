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
import java.text.ParseException;
import java.util.Date;

/**
 * Created by Olumide on 11/4/2015.
 */
public class FeedDatabaseHandler extends SQLiteOpenHelper implements DBAsyncResponse{
    private static final String DB_NAME = "News_Feed";
    private static final String TABLE_NAME = "News_Feed";
    private static final int VERSION = 1;
    private static final String ID = "id";
    private static final String DATE = "date";
    private static final String TIME = "time";
    private static final String SOUND_PATH = "sound_path";
    private static final String IS_SENT = "sent_or_nah";
    private static final String FRIEND = "friend";
    private static final String FILTER = "filter";
    private static final String SPEED = "playback_speed";
    private static final String READ = "read";
    private static final String AUDIO_ID = "audio_id"; //used to retrieve file from server
    private SQLiteDatabase db = null;
    private DBHandlerResponse dbHandlerResponse;
    private static FeedDatabaseHandler feedDatabaseHandler = null;

    private final String SELECT_QUERY = "SELECT * FROM " + TABLE_NAME;

    private FeedDatabaseHandler(Context context, DBHandlerResponse dbHandlerResponse){
        //Although dbHandlerResponse snd context would always be the same object
        // The constructor is setup this way to prevent casting errors if used incorrectly
        super(context, DB_NAME, null, VERSION);
        this.dbHandlerResponse = dbHandlerResponse;
        //Use Writable Db for both reading and writing. It's easier and simpler
        AsyncGetWritableDB asyncGetWritableDB = new AsyncGetWritableDB(this, false);
        asyncGetWritableDB.delegate = this;
        asyncGetWritableDB.execute();
    }

    public static synchronized FeedDatabaseHandler getInstance(Context context, DBHandlerResponse dbHandlerResponse){
        if(feedDatabaseHandler == null) {
            feedDatabaseHandler = new FeedDatabaseHandler(context.getApplicationContext(), dbHandlerResponse);
        }else{
            AsyncGetWritableDB asyncGetWritableDB = new FeedDatabaseHandler.AsyncGetWritableDB(null, true);
            asyncGetWritableDB.delegate = null;
            asyncGetWritableDB.dbHandlerResponse = dbHandlerResponse;
            asyncGetWritableDB.execute();
        }
        return feedDatabaseHandler;
    }

    public void processFinish(SQLiteDatabase db){
        this.db = db;
        dbHandlerResponse.onDBReady();
        Log.v("Db", "process finish");
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        Log.e("Db", "onCreate");
        Log.e("Db", "onCreate");
        this.db = db;//Just in case
        //TABLE FORMAT
        //ID | SENT? | TO/FROM| DATE | TIME |FILEPATH | FILTER | SPEED | READ | UNIQUE FILE ID
        String CREATE_TABLE =
                String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s REAL, %s TEXT, %s TEXT)",
                        TABLE_NAME, ID, IS_SENT, FRIEND, DATE, TIME, SOUND_PATH, FILTER, SPEED, READ, AUDIO_ID);
        db.execSQL(CREATE_TABLE);
        Log.v("Db", "onCreate");
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

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVer, int newVer){
        int incVer = oldVer;
        while(--incVer >= newVer){
            switch(incVer){
                case 1:
                    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
                    onCreate(db);
            }
        }
    }

    //This also includes Hidden items
    public int getCount() {
        Cursor cursor = db.rawQuery(SELECT_QUERY, null);
        cursor.moveToFirst();
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public void markAsRead(int id){
        SoundByteFeedObject feedObject = getFeedObject(id);
        feedObject.markAsRead();
        updateDBFeed(feedObject);
    }

    public void updateFilePath(int id, File soundfile){
        SoundByteFeedObject feedObject = getFeedObject(id);
        feedObject.setFilePath(soundfile);
        updateDBFeed(feedObject);
    }

    public SoundByteFeedObject getFeedObject(int id){
        File soundFile = null;
        Date date = null;
        String friend = null;
        String filter = null;
        float speed = 1;
        boolean sent = false;
        boolean read = false;
        Cursor cursor = db.rawQuery(SELECT_QUERY, null);
        String audio_id = null;

        if(cursor.moveToFirst()){
            cursor.moveToPosition(id);
            //ID | SENT? | TO/FROM| DATE | TIME |FILEPATH | FILTER | SPEED | READ | UNIQUE FILE ID
            sent = "1".equals(cursor.getString(1));
            read = "1".equals(cursor.getString(8));
            friend = cursor.getString(2);
            try {
                date = SoundByteConstants.dateFormat.parse(cursor.getString(3));
            }
            catch(ParseException p){
                //Not sure what to do here
            }
            String soundPath = cursor.getString(5);
            try{
                soundFile = new File(soundPath);
            }catch (Exception e){
                //Do nothing
            }
            filter = cursor.getString(6);
            speed = cursor.getFloat(7);
            audio_id = cursor.getString(9);
        }
        cursor.close();
        return new SoundByteFeedObject(id, sent, friend, date, soundFile, Integer.parseInt(filter), speed, read, audio_id);
    }

    public void addToFeedDB(SoundByteFeedObject  feedObject){
        if(db != null){
            ContentValues values = new ContentValues();
            values.put(ID, feedObject.getId());
            values.put(IS_SENT, feedObject.getIsSent());
            values.put(FRIEND, feedObject.getFriend());
            values.put(SOUND_PATH, feedObject.getAudioPath());
            values.put(AUDIO_ID, feedObject.getAudioID());
            values.put(DATE, SoundByteConstants.dateFormat.format(feedObject.getDate()));
            values.put(TIME, SoundByteConstants.dateFormat.format(feedObject.getDate()));
            values.put(SOUND_PATH, feedObject.getAudioPath());
            values.put(FILTER, feedObject.getFilter());
            values.put(SPEED, feedObject.getPlaybackSpeed());
            values.put(READ, feedObject.hasBeenOpened());
            db.insert(TABLE_NAME, null, values);
        }
    }

    private void updateDBFeed(SoundByteFeedObject  feedObject){
        if(db != null){
            ContentValues values = new ContentValues();
            values.put(ID, feedObject.getId());
            values.put(IS_SENT, feedObject.getIsSent());
            values.put(FRIEND, feedObject.getFriend());
            values.put(AUDIO_ID, feedObject.getAudioID());
            values.put(SOUND_PATH, feedObject.getAudioPath());
            values.put(DATE, SoundByteConstants.dateFormat.format(feedObject.getDate()));
            values.put(TIME, SoundByteConstants.dateFormat.format(feedObject.getDate()));
            values.put(SOUND_PATH, feedObject.getAudioPath());
            values.put(FILTER, feedObject.getFilter());
            values.put(SPEED, feedObject.getPlaybackSpeed());
            values.put(READ, feedObject.hasBeenOpened());
            db.replace(TABLE_NAME, null, values);
        }
    }

    public static class AsyncGetWritableDB extends AsyncTask<Object, Void, SQLiteDatabase> {
        private FeedDatabaseHandler dbHandler;
        public DBAsyncResponse delegate = null;
        public DBHandlerResponse dbHandlerResponse = null;
        private boolean isWorkAround;

        public AsyncGetWritableDB(FeedDatabaseHandler dbh, boolean isWorkAround){
            dbHandler = dbh;
            this.isWorkAround = isWorkAround;
        }
        @Override
        protected SQLiteDatabase doInBackground(Object ... objects) {
            Log.e("BD", "BDBD");
            if(!isWorkAround)
                return dbHandler.getReadableDatabase();
            else
                return null;
        }

        @Override
        protected void onPostExecute(SQLiteDatabase db){
            if(delegate != null)
                delegate.processFinish(db);
            else if(dbHandlerResponse != null)
                dbHandlerResponse.onDBReady();
        }
    }
}

