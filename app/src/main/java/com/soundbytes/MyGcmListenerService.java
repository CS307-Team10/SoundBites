package com.soundbytes;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.soundbytes.db.DBHandlerResponse;
import com.soundbytes.db.FeedDatabaseHandler;
import com.soundbytes.views.SoundByteFeedView;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * Created by Olumide on 11/1/2015.
 */
public class MyGcmListenerService extends GcmListenerService implements DBHandlerResponse{
    private FeedDatabaseHandler dbHandler;
    private static final String TAG = "MyGcmListenerService";
    private boolean dbHandlerReady = false;

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String audio_id = data.getString("audioID");
        int id = dbHandler.getCount();
        boolean sent = Boolean.parseBoolean(data.getString("sent"));
        String friend = data.getString("friend");
        Date date, time;
        try {
            date = new Date(Long.parseLong(data.getString("date")));
        }catch (NullPointerException e){
            date = new Date();
        }
        String filter =  data.getString("filter");
        float speed = Float.parseFloat(data.getString("speed"));

        SoundByteFeedObject feedObject = new SoundByteFeedObject(id, sent, friend, date, null, filter, speed, false, audio_id);
        sendNotification(feedObject);
        if(dbHandlerReady && dbHandler != null) {
            dbHandler.addToFeedDB(feedObject);
            broadcastIntent();
        }else{
            dbHandler = FeedDatabaseHandler.getInstance(getApplicationContext(), this);
        }
        Log.v("db", "count is: "+dbHandler.getCount());
        dbHandler.getFeedObject(dbHandler.getCount() - 1);
    }

    public void onDBReady(){
        dbHandlerReady = true;
    }

    private void broadcastIntent(){
        Intent intent = new Intent();
        intent.setAction(SoundByteConstants.dbUpdateBroadcast);
        sendBroadcast(intent);
    }

    @Override
    public void onCreate(){
        super.onCreate();
        dbHandler = FeedDatabaseHandler.getInstance(getApplicationContext(), this);
    }

    private void sendNotification(SoundByteFeedObject feedObject) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("New SoundByte")
                .setContentText(String.format("%s sent you a SoundByte", feedObject.getFriend()))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
